package ch.hslu.swt.wikilenium.core;

import org.openqa.selenium.chrome.ChromeDriver;

public class Wikilenium {

    private final static String CHROMEDRIVER_PROPERTY_NAME = "webdriver.chrome.driver";
    private final static String PATH_TO_CHROMEDRIVER = "C:\\Program Files (x86)\\WebDrivers\\chromedriver.exe";

    public enum Browser {
        CHROME
    }

    public Wikilenium() {
        System.setProperty(CHROMEDRIVER_PROPERTY_NAME, PATH_TO_CHROMEDRIVER);
    }

    public TestRunner getTestRunner(Browser browser) {
        switch (browser) {
            case CHROME: return new TestRunner(ChromeDriver::new);
            default: throw new UnsupportedOperationException("Browser not supported");
        }
    }

    public TestBatchRunner getTestBatchRunner(Browser browser) {
        return new TestBatchRunner(getTestRunner(browser));
    }
}