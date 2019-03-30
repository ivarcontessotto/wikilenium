package ch.hslu.swt.wikilenium.core;

public class Wikilenium {

    private final static String CHROMEDRIVER_PROPERTY_NAME = "webdriver.chrome.driver";
    private final static String PATH_TO_CHROMEDRIVER = "C:\\Program Files (x86)\\WebDrivers\\chromedriver.exe";

    public Wikilenium() {
        System.setProperty(CHROMEDRIVER_PROPERTY_NAME, PATH_TO_CHROMEDRIVER);
    }

    public TestRunner getTestRunner() {
        return new TestRunner(new WebDriverFactory());
    }
}