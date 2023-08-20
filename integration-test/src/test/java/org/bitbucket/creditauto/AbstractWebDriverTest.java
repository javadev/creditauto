package org.bitbucket.creditauto;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.thoughtworks.selenium.Selenium;

/**
 * AbstractWebDriverTest.
 */
public class AbstractWebDriverTest {
    /**
     * Base url for all http requests.
     */
    public static final String BASE_URL =
        "http://localhost:9090";

    /**
     * Base application name.
     */
    public static final String APP_NAME = "/coliseum";

    private static final Pattern PATTERN_SELECT = Pattern.compile("<select .*?id=\"(.*?)\"");
    private static final Pattern PATTERN_SELECT2 = Pattern.compile("<select .*?name=\"(.*?)\"");
    private static final Pattern PATTERN_INPUT = Pattern.compile("<input .*?id=\"(.*?)\"");
    private static final Pattern PATTERN_INPUT2 = Pattern.compile("<input .*?name=\"(.*?)\"");
    private static final Pattern PATTERN_TEXTAREA = Pattern.compile("<textarea .*?id=\"(.*?)\"");
    private static final Pattern PATTERN_TEXTAREA2 = Pattern.compile("<textarea .*?name=\"(.*?)\"");

    /**
     * MyWebDriverBackendSelenium.
     */
    private final class MyWebDriverBackendSelenium extends
            WebDriverBackedSelenium {
        private static final int SELECT_MAX_TRY = 10;
        private static final int CLICK_MAX_TRY = 3;

        private MyWebDriverBackendSelenium(WebDriver baseDriver, String baseUrl) {
            super(baseDriver, baseUrl);
        }

        @Override
        public void click(String locator) {
            try {
            super.click(partialId(locator));
            } catch (com.thoughtworks.selenium.SeleniumException e) {
                String message = e.getMessage();
                int tryCount = CLICK_MAX_TRY;
                while (message.contains("Timed out waiting") && tryCount > 0) {
                    echo(tryCount + " - " + message);
                    try {
                        Thread.sleep(1000);
                        try {
                            super.click(partialId(locator));
                            return;
                        } catch (com.thoughtworks.selenium.SeleniumException e1) {
                            message = e1.getMessage();
                        }
                    } catch (InterruptedException e1) {
                        e1.getMessage();
                    }
                    tryCount -= 1;
                }
                throw e;
            }
        }

        @Override
        public String getSelectedLabel(String selectLocator) {
            try {
            return super.getSelectedLabel(partialId(selectLocator));
            } catch (com.thoughtworks.selenium.SeleniumException e) {
                String message = e.getMessage();
                int tryCount = SELECT_MAX_TRY;
                while ((message.contains("not found") || message.contains("is not an option")) && tryCount > 0) {
                    echo(tryCount + " - " + message);
                    try {
                        Thread.sleep(1000);
                        try {
                            return super.getSelectedLabel(partialId(selectLocator));
                        } catch (com.thoughtworks.selenium.SeleniumException e1) {
                            message = e1.getMessage();
                        }
                    } catch (InterruptedException e1) {
                        e1.getMessage();
                    }
                    tryCount -= 1;
                }
                throw e;
            }
        }

        @Override
        public String getValue(String selectLocator) {
            try {
            return super.getValue(partialId(selectLocator));
            } catch (com.thoughtworks.selenium.SeleniumException e) {
                String message = e.getMessage();
                int tryCount = SELECT_MAX_TRY;
                while ((message.contains("not found") || message.contains("is not an option")) && tryCount > 0) {
                    echo(tryCount + " - " + message);
                    try {
                        Thread.sleep(1000);
                        try {
                            return super.getValue(partialId(selectLocator));
                        } catch (com.thoughtworks.selenium.SeleniumException e1) {
                            message = e1.getMessage();
                        }
                    } catch (InterruptedException e1) {
                        e1.getMessage();
                    }
                    tryCount -= 1;
                }
                throw e;
            }
        }

        @Override
        public void select(String selectLocator, String optionLocator) {
            try {
        LOG.info("select " + partialId(selectLocator));
            super.select(partialId(selectLocator), optionLocator);
            } catch (com.thoughtworks.selenium.SeleniumException e) {
                String message = e.getMessage();
                int tryCount = SELECT_MAX_TRY;
                while ((message.contains("not found") || message.contains("is not an option")) && tryCount > 0) {
                    echo(tryCount + " - " + message);
                    try {
                        Thread.sleep(1000);
                        try {
                            super.select(partialId(selectLocator), optionLocator);
                            return;
                        } catch (com.thoughtworks.selenium.SeleniumException e1) {
                            message = e1.getMessage();
                        }
                    } catch (InterruptedException e1) {
                        e1.getMessage();
                    }
                    tryCount -= 1;
                }
                throw e;
            }
        }

        @Override
        public void type(String locator, String value) {
            super.type(partialId(locator), value);
        }

        @Override
        public boolean isElementPresent(String locator) {
            return super.isElementPresent(partialId(locator));
        }

        @Override
        public void addSelection(String locator, String optionLocator) {
            super.addSelection(partialId(locator), optionLocator);
        }
        
        @Override
        public boolean isEditable(String locator) {
            return super.isEditable(partialId(locator));
        }

        @Override
        public String getTable(String locator) {
            return super.getTable(locator).replaceAll("\\n", " ").replaceAll("  ", " ");
        }

        @Override
        public String getText(String locator) {
            return super.getText(partialId(locator)).replaceAll("\\n", " ").replaceAll("  ", " ");
        }

        @Override
        public void attachFile(String locator, String value) {
            super.attachFile(partialId(locator), value);
        }

    }

    private final class MyHtmlUnitDriver extends HtmlUnitDriver {
        private MyHtmlUnitDriver(BrowserVersion version) {
            super(version);
        }

        public com.gargoylesoftware.htmlunit.WebClient getWebClient() {
            return super.getWebClient();
        }
    }

    private static final Log LOG = LogFactory.getLog(AbstractWebDriverTest.class);

    /**
     * WebDriver object.
     */
    protected WebDriver driver;
    /**
     * Selenium object.
     */
    protected Selenium selenium;

    private String appName;

    /**
     * Inits objects.
     */
    @Before public void setUp() {
        // You may use any WebDriver implementation. HtmlUnit is used here as an example
//        driver = new MyHtmlUnitDriver(BrowserVersion.FIREFOX_3);
//        ((HtmlUnitDriver) driver).setJavascriptEnabled(true);
//        ((MyHtmlUnitDriver) driver).getWebClient().setThrowExceptionOnScriptError(false);
        driver = new FirefoxDriver();

        // Create the Selenium implementation
        String baseUrlProperty = System.getProperty("BASE_URL");
        if (baseUrlProperty == null) {
            baseUrlProperty = BASE_URL;
        }
        String appNameProperty = System.getProperty("APP_NAME");
        appName = appNameProperty == null ? APP_NAME : appNameProperty;

        selenium = new MyWebDriverBackendSelenium(driver, baseUrlProperty);
    }

    /**
     * Tears down objects.
     */
    @After public void tearDown() {
        String source = selenium.getHtmlSource();
        if (!source.contains("<input type=\"text\" name=\"username\"")) {
            echo(source);
        }
        driver.close();
    }

    /**
     * Logs the message.
     * @param message to be logging
     */
    protected void echo(String message) {
        LOG.info(message);
    }

    /**
     * Gets app name.
     *
     * @return the bapp name.
     */
    protected String getAppName() {
        return appName;
    }

    /**
     * Finds for id in htmlSource().
     * @param id the partial id
     * @return the real id
     */
    public String partialId(String id) {
        String checkId = id;
        int index = id.indexOf(":field");
        if (index > 0) {
            checkId = id.substring(index + 1);
        }
        index = id.indexOf(":desired");
        if (index > 0) {
            checkId = id.substring(index + 1);
        }
        index = id.indexOf(":in_dossier");
        if (index > 0) {
            checkId = id.substring(index + 1);
        }
        if (id.startsWith("partialId=")) {
            checkId = checkId.substring(10);
        }
        Matcher matcher = PATTERN_SELECT.matcher(selenium.getHtmlSource());
        while (matcher.find()) {
            if (matcher.group(1).contains(checkId)) {
                return matcher.group(1);
            }
        }
        matcher = PATTERN_SELECT2.matcher(selenium.getHtmlSource());
        while (matcher.find()) {
            if (matcher.group(1).contains(checkId)) {
                return matcher.group(1);
            }
        }
        matcher = PATTERN_INPUT.matcher(selenium.getHtmlSource());
        while (matcher.find()) {
            if (matcher.group(1).contains(checkId)) {
                return matcher.group(1);
            }
        }
        matcher = PATTERN_INPUT2.matcher(selenium.getHtmlSource());
        while (matcher.find()) {
            if (matcher.group(1).contains(checkId)) {
                return matcher.group(1);
            }
        }
        matcher = PATTERN_TEXTAREA.matcher(selenium.getHtmlSource());
        while (matcher.find()) {
            if (matcher.group(1).contains(checkId)) {
                return matcher.group(1);
            }
        }
        matcher = PATTERN_TEXTAREA2.matcher(selenium.getHtmlSource());
        while (matcher.find()) {
            if (matcher.group(1).contains(checkId)) {
                return matcher.group(1);
            }
        }
        return checkId;
    }
}
