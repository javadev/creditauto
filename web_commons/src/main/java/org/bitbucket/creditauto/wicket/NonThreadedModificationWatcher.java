/*
 * $Id$
 *
 * Copyright (c) 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.wicket;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.wicket.util.listener.ChangeListenerSet;
import org.apache.wicket.util.listener.IChangeListener;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.time.Time;
import org.apache.wicket.util.watch.IModifiable;
import org.apache.wicket.util.watch.IModificationWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Monitors one or more <code>IModifiable</code> objects, calling a {@link IChangeListener
 * IChangeListener} when a given object's modification time changes.
 *
 * @author Jonathan Locke
 * @since 1.2.6
 */
public class NonThreadedModificationWatcher implements IModificationWatcher {
    /** logger */
    private static final Logger log = LoggerFactory.getLogger(NonThreadedModificationWatcher.class);

    /** maps <code>IModifiable</code> objects to <code>Entry</code> objects */
    private final Map<IModifiable, Entry> modifiableToEntry =
            new ConcurrentHashMap<IModifiable, Entry>();

    /** Container class for holding modifiable entries to watch. */
    private static final class Entry {
        // The most recent lastModificationTime polled on the object
        Time lastModifiedTime;

        // The set of listeners to call when the modifiable changes
        final ChangeListenerSet listeners = new ChangeListenerSet();

        // The modifiable thing
        IModifiable modifiable;
    }

    /** Default constructor for two-phase construction. */
    public NonThreadedModificationWatcher() {}

    /**
     * Constructor that accepts a <code>Duration</code> argument representing the poll frequency.
     *
     * @param pollFrequency how often to check on <code>IModifiable</code>s
     */
    public NonThreadedModificationWatcher(final Duration pollFrequency) {
        start(pollFrequency);
    }

    /**
     * @see
     *     org.apache.wicket.util.watch.IModificationWatcher#add(org.apache.wicket.util.watch.IModifiable,
     *     org.apache.wicket.util.listener.IChangeListener)
     */
    public final boolean add(final IModifiable modifiable, final IChangeListener listener) {
        // Look up entry for modifiable
        final Entry entry = modifiableToEntry.get(modifiable);

        // Found it?
        if (entry == null) {
            Time lastModifiedTime = modifiable.lastModifiedTime();
            if (lastModifiedTime != null) {
                // Construct new entry
                final Entry newEntry = new Entry();

                newEntry.modifiable = modifiable;
                newEntry.lastModifiedTime = lastModifiedTime;
                newEntry.listeners.add(listener);

                // Put in map
                modifiableToEntry.put(modifiable, newEntry);
            } else {
                // The IModifiable is not returning a valid lastModifiedTime
                log.info("Cannot track modifications to resource " + modifiable);
            }

            return true;
        } else {
            // Add listener to existing entry
            return entry.listeners.add(listener);
        }
    }

    /**
     * @see
     *     org.apache.wicket.util.watch.IModificationWatcher#remove(org.apache.wicket.util.watch.IModifiable)
     */
    public IModifiable remove(final IModifiable modifiable) {
        final Entry entry = modifiableToEntry.remove(modifiable);
        if (entry != null) {
            return entry.modifiable;
        }
        return null;
    }

    /**
     * @see
     *     org.apache.wicket.util.watch.IModificationWatcher#start(org.apache.wicket.util.time.Duration)
     */
    public void start(final Duration pollFrequency) {}

    protected void checkForModifications() {
        final Iterator<Entry> itor = modifiableToEntry.values().iterator();
        while (itor.hasNext()) {
            final Entry entry = itor.next();

            // If the modifiable has been modified after the last known
            // modification time
            final Time modifiableLastModified = entry.modifiable.lastModifiedTime();
            if ((modifiableLastModified != null)
                    && modifiableLastModified.after(entry.lastModifiedTime)) {
                // Notify all listeners that the modifiable was modified
                entry.listeners.notifyListeners();

                // Update timestamp
                entry.lastModifiedTime = modifiableLastModified;
            }
        }
    }

    /** @see org.apache.wicket.util.watch.IModificationWatcher#destroy() */
    public void destroy() {}

    /** @see org.apache.wicket.util.watch.IModificationWatcher#getEntries() */
    public final Set<IModifiable> getEntries() {
        return modifiableToEntry.keySet();
    }
}
