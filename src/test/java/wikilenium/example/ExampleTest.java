package wikilenium.example;

import org.junit.*;
import org.openqa.selenium.chrome.ChromeDriver;
import wikilenium.testclient.TestClient;

/***
 * Contains example tests that show how to use the test-client.
 */
public class ExampleTest {

    private final static String WEBDRIVER_PROPERTY_NAME = "webdriver.chrome.driver";
    public static final String PATH_TO_WEBDRIVER = "C:\\Program Files (x86)\\WebDrivers\\chromedriver.exe";

    private TestClient testClient;

    @BeforeClass
    public static void classSetup() {
        System.setProperty(WEBDRIVER_PROPERTY_NAME, PATH_TO_WEBDRIVER);
    }

    @Before
    public void testSetup() {
        this.testClient = new TestClient(new ChromeDriver());
    }

    @After
    public void testCleanup() {
        this.testClient.close();
    }

    @Test
    public void test_StartPageBilanz_ClickAtMost7Links_PageIsPhilosophie() {
        boolean result = testClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Bilanz")
                .clickAtMostNLinks(7)
                .untilWikiPageIs("Philosophie")
                .run();
        Assert.assertTrue(result);
    }

    @Test
    public void test_StartPageCocaCola_ClickAtMost7Links_PageIsPhilosophie() {
        boolean result = testClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Coca-Cola")
                .clickAtMostNLinks(7)
                .untilWikiPageIs("Philosophie")
                .run();
        Assert.assertTrue(result);
    }
}