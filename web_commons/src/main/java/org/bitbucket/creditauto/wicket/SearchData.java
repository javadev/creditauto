/*
 * $Id$
 *
 * Copyright (c) 2011 (javadev75@gmail.com)
 */
package org.bitbucket.creditauto.wicket;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * .
 *
 * @author javadev
 * @version $Revision$ $Date$
 */
public class SearchData implements Serializable {
    private static final long serialVersionUID = -295829058L;

    private AllDossiers allDossiers = new AllDossiers();
    private AllUsers allUsers = new AllUsers();

    /** . */
    public class AllDossiers implements Serializable {
        private static final long serialVersionUID = -29298035636L;
        private Date from;
        private Date to;

        {
            Calendar now = new GregorianCalendar();
            now.add(Calendar.DATE, -90);
            from = now.getTime();
            Calendar now2 = new GregorianCalendar();
            now2.add(Calendar.DATE, 1);
            to = now2.getTime();
        }

        public void setFrom(Date from) {
            this.from = from;
        }

        public Date getFrom() {
            return from;
        }

        public void setTo(Date to) {
            this.to = to;
        }

        public Date getTo() {
            return to;
        }
    }

    /** . */
    public class AllUsers implements Serializable {
        private static final long serialVersionUID = -2485983598L;
        private String userLogin;
        private String userTableNumber;
        private String userName;
        private String powerOfAttNumber;
        private Date powerOfAttStart;
        private Date powerOfAttFinish;

        {
            Calendar now = new GregorianCalendar();
            now.add(Calendar.DATE, -90);
            powerOfAttStart = now.getTime();
            Calendar now2 = new GregorianCalendar();
            now2.add(Calendar.DATE, 1);
            powerOfAttFinish = now2.getTime();
        }

        public String getUserLogin() {
            return userLogin;
        }

        public void setUserLogin(String userLogin) {
            this.userLogin = userLogin;
        }

        public String getUserTableNumber() {
            return userTableNumber;
        }

        public void setUserTableNumber(String userTableNumber) {
            this.userTableNumber = userTableNumber;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPowerOfAttNumber() {
            return powerOfAttNumber;
        }

        public void setPowerOfAttNumber(String powerOfAttNumber) {
            this.powerOfAttNumber = powerOfAttNumber;
        }

        public Date getPowerOfAttStart() {
            return powerOfAttStart;
        }

        public void setPowerOfAttStart(Date powerOfAttStart) {
            this.powerOfAttStart = powerOfAttStart;
        }

        public Date getPowerOfAttFinish() {
            return powerOfAttFinish;
        }

        public void setPowerOfAttFinish(Date powerOfAttFinish) {
            this.powerOfAttFinish = powerOfAttFinish;
        }
    }

    public AllDossiers getAllDossiers() {
        return allDossiers;
    }

    public AllUsers getAllUsers() {
        return allUsers;
    }
}
