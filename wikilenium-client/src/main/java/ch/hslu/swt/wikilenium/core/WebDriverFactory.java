package ch.hslu.swt.wikilenium.core;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

class WebDriverFactory {

    @Step("Open chrome browser")
    WebDriver getChromeDriver() {
        return new ChromeDriver();
    }

    @Step("Open firefox browser")
    WebDriver getFirefoxDriver() {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
