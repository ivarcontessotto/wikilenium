package wikilenium;

import org.junit.*;

public class TestClientTest {

    private final Wikilenium wikilenium;
    private TestClient client;

    public TestClientTest() {
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
    public void test_StartPageSameAsExpectedPage_ClickLimitZero_ReturnTrue() {
        boolean result = client
                .startPage("Philosophie")
                .clickLimit(0)
                .goalPage("Philosophie")
                .run();

        Assert.assertTrue(result);
    }

    @Test
    public void test_StartPageSameAsExpectedPage_ClickLimitGreaterZero_ReturnTrue() {
        boolean result = client
                .startPage("Philosophie")
                .clickLimit(2)
                .goalPage("Philosophie")
                .run();

        Assert.assertTrue(result);
    }

    @Test
    public void test_StartPageOneHopAwayFromExpectedPage_ClickLimitTwo_ReturnTrue() {
        boolean result = client
                .startPage("Begriff (Philosophie)")
                .clickLimit(2)
                .goalPage("Philosophie")
                .run();

        Assert.assertTrue(result);
    }

    @Test
    public void test_StartPageTwoHopsAwayFromExpectedPage_ClickLimitTwo_ReturnTrue() {
        boolean result = client
                .startPage("Wissensgebiet")
                .clickLimit(2)
                .goalPage("Philosophie")
                .run();

        Assert.assertTrue(result);
    }

    @Test
    public void test_StartPageThreeHopsAwayFromExpectedPage_ClickLimitTwo_ReturnFalse() {
        boolean result = client
                .startPage("Fachgebiet")
                .clickLimit(2)
                .goalPage("Philosophie")
                .run();

        Assert.assertFalse(result);
    }

    @Test
    public void test_FirstLinkIsInsideBrackets_ClickFirstLinkOutsideBrackets() {
        boolean result = client
                .startPage("Kredit")
                .clickLimit(1)
                .goalPage("Übereignung")
                .run();

        Assert.assertTrue(result);
    }

    @Test
    public void test_FirstLinkIsInsideBracketsAndHasUnreadableCharacters_ClickFirstLinkOutsideBrackets() {
        boolean result = client
                .startPage("Deutschland")
                .clickLimit(1)
                .goalPage("Bundesstaat (Föderaler Staat)")
                .run();

        Assert.assertTrue(result);
    }
    @Test
    public void test_FirstLinkIsInBoldStyle_ClickBoldStyleLink() {
        boolean result = client
                .startPage("Football League First Division 1903/04")
                .clickLimit(1)
                .goalPage("Football League First Division")
                .run();

        Assert.assertTrue(result);
    }

    @Test
    public void test_LinkInBracketsHasSpecialRegexCharacter_ClickFirstLinkOutsideBrackets() {
        boolean result = client
                .startPage("Idyll")
                .clickLimit(1)
                .goalPage("Gattung (Literatur)")
                .run();

        Assert.assertTrue(result);
    }

    @Test
    public void test_FirstLinkIsItalicStyle_ClickFirstLinkNormalStyle() {
        boolean result = client
                .startPage("Mühl-Schlössl")
                .clickLimit(1)
                .goalPage("Lend (Graz)")
                .run();

        Assert.assertTrue(result);
    }

    @Test
    public void test_ExploratoryTest() {
        boolean result = client
                .startPage("Hermann Heinrich Becker")
                .clickLimit(1)
                .goalPage("Politiker")
                .run();

        Assert.assertTrue(result);
    }
}