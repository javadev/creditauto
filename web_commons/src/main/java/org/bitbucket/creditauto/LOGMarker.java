/*
 * $Id$
 *
 * Copyright (c) 2012 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto;

/**
 * Marker format.
 * host/time-dependent-prefix (sysid)
 * -
 * incremental counter number (session number)
 * -
 * Java internal thread number, usefull fro lock debug
 * -
 * @author javadev
 * @version $Revision$ $Date$
 */
public class LOGMarker {

    private static final int MARKER_ARRAY_LENGTH = 5;
    private static final int MARKER_POSITION_HOST = 0;
    private static final int MARKER_POSITION_SESSION = 1;
    private static final int MARKER_POSITION_THREAD = 2;
    private static final int MARKER_POSITION_USER = 3;
    private static final int MARKER_POSITION_INSTANCE = 4;

    private static LOGMarker instance;

    private final String splitter = "-";

    /**
     * Marker prefix which dependent form host IP (last number).
     * */
    private String prefix;

    /**
     * Internal incremental counter.
     * */
    private volatile long counter;

    /**
     * Internal error counter.
     * */
    private volatile long errorNumber;

    /**
     * Classical singleton.
     *
     * @return LOGMarker instance
     * */
    public static LOGMarker instance() {
        if (instance != null) {
            return instance;
        }
        instance = new LOGMarker();
        instance.counter = 0;
        instance.errorNumber = 0;
        instance.prefix = "x";
        try {
            String hostip = java.net.InetAddress.getLocalHost().getHostAddress();
            String[] dig = hostip.split("\\.");
            instance.prefix = "h" + dig[3];
        } catch (Exception ex) {
            LOG.error(null, ex, "Exception:" + ex.getClass().getName() + ":" + ex.getMessage());
            // any exceptions here must be suppressed
        }
        return instance;
    }

    /**
     * Standard memory cleunup helper.
     * */
    public static void shutdown() {
        instance = null;
    }

    synchronized long nextCounterValue() {
        return ++counter;
    }

    synchronized long nextErrorNumberValue() {
        return ++errorNumber;
    }

    /**
     * Extract marker from Stirng. If ANY problems detected - create new marker.
     *
     * @return splitted data
     * @param mark from NDC (can be garbage)
     * @param sessionKey part of marker which should be stored in session.
     * */
    private String[] extractMarkerData(String mark, String sessionKey) {
        String[] ms;
        try {
            ms = mark.split(splitter);
            if (ms.length < MARKER_ARRAY_LENGTH) {
                String[] ms2 = new String[MARKER_ARRAY_LENGTH];
                System.arraycopy(ms, 1, ms2, 1, ms.length - 1);
                ms = ms2;
            }
            if (ms.length > MARKER_ARRAY_LENGTH) {
                throw new NullPointerException(":):" + ms.length);
            }
        } catch (Exception ex) {
            LOG.debug(this, "Excpetion:" + ex.getClass().getName() + ":" + ex.getMessage());
            ms = new String[MARKER_ARRAY_LENGTH];
            for (int i = 0; i < MARKER_ARRAY_LENGTH; i++) {
                ms[i] = "";
            }
            // if any (null pointer, array out of...)
        }
        if ("".equals(ms[MARKER_POSITION_HOST]) || ms[MARKER_POSITION_HOST] == null) {
            ms[MARKER_POSITION_HOST] = prefix;
        }
        if (sessionKey != null) {
            ms[MARKER_POSITION_SESSION] = sessionKey;
        }
        if ("".equals(ms[MARKER_POSITION_SESSION]) || ms[MARKER_POSITION_SESSION] == null) {
            ms[MARKER_POSITION_SESSION] = "" + nextCounterValue();
        }
        if ("".equals(ms[MARKER_POSITION_THREAD]) || ms[MARKER_POSITION_THREAD] == null) {
            ms[MARKER_POSITION_THREAD] = Long.toString(Thread.currentThread().getId());
        }
        return ms;
    }

    /**
     * Convert array into marker.
     * Mathod does not check array bounds!
     *
     * @param marks array of markers
     * @return converted array
     * */
    private String encodeMarker(String[] marks) {
        StringBuffer buf = null;
        for (String aStr : marks) {
            if (buf != null) {
                buf.append(splitter);
            } else {
                buf = new StringBuffer();
            }
            if (aStr != null) {
                buf.append(aStr);
            } else {
                buf.append("");
            }
        }
        if (buf != null) {
            return buf.toString();
        }
        return null;
    }

    /**
     * Create marker.
     *
     * @param sessionKey session key from HTTPSession
     * @return sessionKey which should be saved into HTTP Session.
     * */
    public String createMarker(String sessionKey) {
        String [] m = extractMarkerData(LOG.removeNDCMarker(), sessionKey);
        LOG.setNCDMarger(encodeMarker(m));
        return m[MARKER_POSITION_SESSION];
    }

    public void setUserAndInstanceId(String userId, String instId) {
        String [] m = extractMarkerData(LOG.removeNDCMarker(), null);
        m[MARKER_POSITION_USER] = userId;
        m[MARKER_POSITION_INSTANCE] = instId;
        LOG.setNCDMarger(encodeMarker(m));
    }

    /**
     * Set user Id to marker.
     *
     * @param userId user ID
     * */
    public void setUserId(String userId) {
        String [] m = extractMarkerData(LOG.removeNDCMarker(), null);
        m[MARKER_POSITION_USER] = userId;
        LOG.setNCDMarger(encodeMarker(m));
    }

    /**
     * Set instance Id to marker.
     * @param instId instance ID
     * */
    public void setInstanceId(String instId) {
        String[] m = extractMarkerData(LOG.removeNDCMarker(), null);
        m[MARKER_POSITION_INSTANCE] = instId;
        LOG.setNCDMarger(encodeMarker(m));
    }

    /**
     * Generate error marker for user interface.
     * Addiaionally can create LOG message about marker creation and marker value. (in info log level)
     *
     * @param obj to which object should be bind error message
     * @param addToLog add to log (othervice obj is ignored and can be null).
     * @return Error marker for user interface
     * */
    public String createErrorMarker(Object obj, boolean addToLog) {
        String m = "" + LOG.getNDCMarker() + "-" + nextErrorNumberValue();
        if (addToLog) {
            LOG.info(obj, "Error marker generated:" + m);
        }
        return m;
    }

    /**
     * Set marker.
     *
     * @param userId user ID
     * @param instId instance ID
     */
    public void insertMarker(String userId, String instId) {
        String[] m = extractMarkerData(null, null);
        m[MARKER_POSITION_USER] = userId;
        m[MARKER_POSITION_INSTANCE] = instId;
        LOG.setNCDMarger(encodeMarker(m));
    }

    /**
     * Remove marker.
     */
    public void removeMarker() {
        LOG.removeNDCMarker();
    }
}
