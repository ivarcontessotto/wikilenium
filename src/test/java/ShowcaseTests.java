import gameclient.GameClient;
import org.junit.*;
import org.openqa.selenium.chrome.ChromeDriver;

public class ShowcaseTests {

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
    public void test_StartFromBilanz_ClickLimitIsSeven_ShouldReachPhilosophie() {
        boolean result = gameClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Bilanz")
                .clickAtMostNLinks(7)
                .untilWikiPageIs("Philosophie")
                .run();

        Assert.assertTrue(result);
    }

    @Test
    public void test_StartFromCocaCola_ClickLimitIsSeven_ShouldReachPhilosophie() {
        boolean result = gameClient
                .startAtWikiPage("https://de.wikipedia.org/wiki/Coca-Cola")
                .clickAtMostNLinks(7)
                .untilWikiPageIs("Philosophie")
                .run();

        Assert.assertTrue(result);
    }
}