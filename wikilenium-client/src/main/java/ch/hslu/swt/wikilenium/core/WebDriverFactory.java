package ch.hslu.swt.wikilenium.core;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public interface WebDriverFactory {

    @Step("Open web browser")
    WebDriver getWebDriver();
}
