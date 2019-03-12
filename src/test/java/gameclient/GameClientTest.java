package gameclient;

import org.junit.*;
import org.openqa.selenium.chrome.ChromeDriver;

/***
 * Tests that the game client is working properly.
 */
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

    @Test
    public void test_FirstLinkIsInsideBracketsAndHasUnreadableCharacters_ShouldClickFirstLinkOutsideBrackets() {
        boolean result = gameClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Deutschland")
                .clickAtMostNLinks(1)
                .untilWikiPageIs("Bundesstaat (Föderaler Staat)")
                .run();

        Assert.assertTrue(result);
    }
    @Test
    public void test_FirstLinkIsInBoldStyle_ShouldClickFirstLinkRegardless() {
        boolean result = gameClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Football_League_First_Division_1903/04")
                .clickAtMostNLinks(1)
                .untilWikiPageIs("Football League First Division")
                .run();

        Assert.assertTrue(result);

//        boolean result = gameClient
//                .startAtWikiPage("https://de.wikipedia.org/wiki/Scottish_FA_Cup_1896/97")
//                .clickAtMostNLinks(1)
//                .untilWikiPageIs("Scottish FA Cup")
//                .run();
//
//        Assert.assertTrue(result);
    }

    @Test
    public void test_LinkInBracketsHasRegexSpecialCharacter_ShouldClickFirstLinkOutsideBrackets() {
        boolean result = gameClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Idyll")
                .clickAtMostNLinks(1)
                .untilWikiPageIs("Gattung (Literatur)")
                .run();

        Assert.assertTrue(result);
    }

    @Test
    public void test_FirstLinkIsItalicStyle_ShouldClickFirstLinkNormalStyle() {
        boolean result = gameClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/M%C3%BChl-Schl%C3%B6ssl")
                .clickAtMostNLinks(1)
                .untilWikiPageIs("Lend (Graz)")
                .run();

        Assert.assertTrue(result);
    }

    @Test
    public void test_ExplorationTest() {
        boolean result = gameClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Hermann_Heinrich_Becker")
                .clickAtMostNLinks(1)
                .untilWikiPageIs("Politiker")
                .run();

        Assert.assertTrue(result);
    }
}