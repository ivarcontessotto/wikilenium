package ch.hslu.swt.wikilenium.client;

import org.openqa.selenium.chrome.ChromeDriver;

import io.qameta.allure.Step;

public class Wikilenium {

	private final static String CHROMEDRIVER_PROPERTY_NAME = "webdriver.chrome.driver";
	private final static String PATH_TO_CHROMEDRIVER = "C:\\Program Files (x86)\\WebDrivers\\chromedriver.exe";

	public Wikilenium() {
		System.setProperty(CHROMEDRIVER_PROPERTY_NAME, PATH_TO_CHROMEDRIVER);
	}

	@Step("Setup ChromeDriver")
	public TestClient getChromeClient() {
		return new TestClient(new ChromeDriver());
	}

	public TestClient getFirefoxClient() {
		return null;
	}
}