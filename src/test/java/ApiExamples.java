import org.junit.*;
import wikilenium.TestClient;
import wikilenium.Wikilenium;

public class ApiExamples {

    private final Wikilenium wikilenium;
    private TestClient client;

    public ApiExamples() {
        wikilenium = new Wikilenium();
    }

    @Before
    public void testSetup() {
        client = wikilenium.getChromeClient();
    }

    @After
    public void testCleanup() {
        client.close();
    }

    @Test
    public void test_StartPageBilanz_ClickLimit7_PageIsPhilosophie() {
        boolean result = client
                .startPage("https://de.wikipedia.org/wiki/Bilanz")
                .clickLimit(7)
                .goalPage("Philosophie")
                .run();

        Assert.assertTrue(result);
    }

    @Test
    public void test_StartPageCocaCola_ClickLimit7_PageIsPhilosophie() {
        boolean result = client
                .startPage("https://de.wikipedia.org/wiki/Coca-Cola")
                .clickLimit(7)
                .goalPage("Philosophie")
                .run();

        Assert.assertTrue(result);
    }
}