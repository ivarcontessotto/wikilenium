package wikilenium.testclient;

import org.junit.*;
import org.openqa.selenium.chrome.ChromeDriver;

/***
 * Contains tests for the test-client to ensure it works as expected.
 */
public class TestClientTest {

    public static final String WEBDRIVER_PROPERTY_NAME = "webdriver.chrome.driver";
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
    public void test_StartPageSameAsExpectedPage_ClickLimitZero_ReturnTrue() {
        boolean result = testClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Philosophie")
                .clickAtMostNLinks(0)
                .untilWikiPageIs("Philosophie")
                .run();
        Assert.assertTrue(result);
    }

    @Test
    public void test_StartPageSameAsExpectedPage_ClickLimitGreaterZero_ReturnTrue() {
        boolean result = testClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Philosophie")
                .clickAtMostNLinks(2)
                .untilWikiPageIs("Philosophie")
                .run();
        Assert.assertTrue(result);
    }

    @Test
    public void test_StartPageOneHopAwayFromExpectedPage_ClickLimitTwo_ReturnTrue() {
        boolean result = testClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Begriff_(Philosophie)")
                .clickAtMostNLinks(2)
                .untilWikiPageIs("Philosophie")
                .run();
        Assert.assertTrue(result);
    }

    @Test
    public void test_StartPageTwoHopsAwayFromExpectedPage_ClickLimitTwo_ReturnTrue() {
        boolean result = testClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Wissensgebiet")
                .clickAtMostNLinks(2)
                .untilWikiPageIs("Philosophie")
                .run();
        Assert.assertTrue(result);
    }

    @Test
    public void test_StartPageThreeHopsAwayFromExpectedPage_ClickLimitTwo_ReturnFalse() {
        boolean result = testClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Fachgebiet")
                .clickAtMostNLinks(2)
                .untilWikiPageIs("Philosophie")
                .run();
        Assert.assertFalse(result);
    }

    @Test
    public void test_FirstLinkIsInsideBrackets_ClickFirstLinkOutsideBrackets() {
        boolean result = testClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Kredit")
                .clickAtMostNLinks(1)
                .untilWikiPageIs("Übereignung")
                .run();
        Assert.assertTrue(result);
    }

    @Test
    public void test_FirstLinkIsInsideBracketsAndHasUnreadableCharacters_ClickFirstLinkOutsideBrackets() {
        boolean result = testClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Deutschland")
                .clickAtMostNLinks(1)
                .untilWikiPageIs("Bundesstaat (Föderaler Staat)")
                .run();
        Assert.assertTrue(result);
    }
    @Test
    public void test_FirstLinkIsInBoldStyle_ClickBoldStyleLink() {
        boolean result = testClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Football_League_First_Division_1903/04")
                .clickAtMostNLinks(1)
                .untilWikiPageIs("Football League First Division")
                .run();
        Assert.assertTrue(result);
    }

    @Test
    public void test_LinkInBracketsHasSpecialRegexCharacter_ClickFirstLinkOutsideBrackets() {
        boolean result = testClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Idyll")
                .clickAtMostNLinks(1)
                .untilWikiPageIs("Gattung (Literatur)")
                .run();
        Assert.assertTrue(result);
    }

    @Test
    public void test_FirstLinkIsItalicStyle_ClickFirstLinkNormalStyle() {
        boolean result = testClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/M%C3%BChl-Schl%C3%B6ssl")
                .clickAtMostNLinks(1)
                .untilWikiPageIs("Lend (Graz)")
                .run();
        Assert.assertTrue(result);
    }

    @Test
    public void test_ExploratoryTest() {
        boolean result = testClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Hermann_Heinrich_Becker")
                .clickAtMostNLinks(1)
                .untilWikiPageIs("Politiker")
                .run();
        Assert.assertTrue(result);
    }
}