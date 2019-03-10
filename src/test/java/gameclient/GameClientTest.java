package gameclient;

import org.junit.*;
import org.openqa.selenium.chrome.ChromeDriver;

public class GameClientTest {

    private GameClient gameClient;

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\WebDrivers\\chromedriver.exe");
    }

    @Before
    public void beforeTest() {
        this.gameClient = new GameClient(new ChromeDriver());
    }

    @After
    public void afterTest() {
        this.gameClient.close();
    }

    @Test
    public void test_StartPageSameAsExpectedPage_ClickLimitZero_ShouldReturnTrue() {
        boolean result = gameClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Philosophie")
                .clickAtMostNLinks(0)
                .untilWikiPageIs("Philosophie")
                .run();

        Assert.assertTrue(result);
    }

    @Test
    public void test_StartPageSameAsExpectedPage_ClickLimitGreaterZero_ShouldReturnTrue() {
        boolean result = gameClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Philosophie")
                .clickAtMostNLinks(2)
                .untilWikiPageIs("Philosophie")
                .run();

        Assert.assertTrue(result);
    }

    @Test
    public void test_StartPageOneHopAwayFromExpectedPage_ClickLimitTwo_ShouldReturnTrue() {
        boolean result = gameClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Begriff_(Philosophie)")
                .clickAtMostNLinks(2)
                .untilWikiPageIs("Philosophie")
                .run();

        Assert.assertTrue(result);
    }

    @Test
    public void test_StartPageTwoHopsAwayFromExpectedPage_ClickLimitTwo_ShouldReturnTrue() {
        boolean result = gameClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Wissensgebiet")
                .clickAtMostNLinks(2)
                .untilWikiPageIs("Philosophie")
                .run();

        Assert.assertTrue(result);
    }

    @Test
    public void test_StartPageThreeHopsAwayFromExpectedPage_ClickLimitTwo_ShouldReturnFalse() {
        boolean result = gameClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Fachgebiet")
                .clickAtMostNLinks(2)
                .untilWikiPageIs("Philosophie")
                .run();
        Assert.assertFalse(result);
    }

    @Test
    public void test_FirstLinkIsInsideBrackets_ShouldClickFirstLinkOutsideBrackets() {
        boolean result = gameClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Kredit")
                .clickAtMostNLinks(1)
                .untilWikiPageIs("Übereignung")
                .run();

        Assert.assertTrue(result);
    }
}