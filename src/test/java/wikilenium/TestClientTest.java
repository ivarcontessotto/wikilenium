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
                .language("de")
                .startPage("Philosophie")
                .clickLimit(0)
                .goalPage("Philosophie")
                .run();

        Assert.assertTrue(result);
        Assert.assertArrayEquals(new String[] {"Philosophie"}, client.getPathTaken());
    }

    @Test
    public void test_StartPageSameAsExpectedPage_ClickLimitGreaterZero_ReturnTrue() {
        boolean result = client
                .language("de")
                .startPage("Philosophie")
                .clickLimit(2)
                .goalPage("Philosophie")
                .run();

        Assert.assertTrue(result);
        Assert.assertArrayEquals(new String[] {"Philosophie"}, client.getPathTaken());
    }

    @Test
    public void test_StartPageOneHopAwayFromExpectedPage_ClickLimitTwo_ReturnTrue() {
        boolean result = client
                .language("de")
                .startPage("Begriff (Philosophie)")
                .clickLimit(2)
                .goalPage("Philosophie")
                .run();

        Assert.assertTrue(result);
        Assert.assertArrayEquals(
                new String[] {"Begriff (Philosophie)","Philosophie"},
                client.getPathTaken());
    }

    @Test
    public void test_StartPageTwoHopsAwayFromExpectedPage_ClickLimitTwo_ReturnTrue() {
        boolean result = client
                .language("de")
                .startPage("Wissensgebiet")
                .clickLimit(2)
                .goalPage("Philosophie")
                .run();

        Assert.assertTrue(result);
        Assert.assertArrayEquals(
                new String[] {"Wissensgebiet","Begriff (Philosophie)","Philosophie"},
                client.getPathTaken());
    }

    @Test
    public void test_StartPageThreeHopsAwayFromExpectedPage_ClickLimitTwo_ReturnFalse() {
        boolean result = client
                .language("de")
                .startPage("Fachgebiet")
                .clickLimit(2)
                .goalPage("Philosophie")
                .run();

        Assert.assertFalse(result);
        Assert.assertArrayEquals(
                new String[] {"Fachgebiet","Wissensgebiet","Begriff (Philosophie)"},
                client.getPathTaken());
    }

    @Test
    public void test_FirstLinkIsInsideBrackets_ClickFirstLinkOutsideBrackets() {
        boolean result = client
                .language("de")
                .startPage("Kredit")
                .clickLimit(1)
                .goalPage("Übereignung")
                .run();

        Assert.assertTrue(result);
        Assert.assertArrayEquals(new String[] {"Kredit","Übereignung"}, client.getPathTaken());
    }

    @Test
    public void test_FirstLinkIsInsideBracketsAndHasUnreadableCharacters_ClickFirstLinkOutsideBrackets() {
        boolean result = client
                .language("de")
                .startPage("Deutschland")
                .clickLimit(1)
                .goalPage("Bundesstaat (Föderaler Staat)")
                .run();

        Assert.assertTrue(result);
        Assert.assertArrayEquals(
                new String[] {"Deutschland","Bundesstaat (Föderaler Staat)"},
                client.getPathTaken());
    }
    @Test
    public void test_FirstLinkIsInBoldStyle_ClickBoldStyleLink() {
        boolean result = client
                .language("de")
                .startPage("Football League First Division 1903/04")
                .clickLimit(1)
                .goalPage("Football League First Division")
                .run();

        Assert.assertTrue(result);
        Assert.assertArrayEquals(
                new String[] {"Football League First Division 1903/04","Football League First Division"},
                client.getPathTaken());
    }

    @Test
    public void test_LinkInBracketsHasSpecialRegexCharacter_ClickFirstLinkOutsideBrackets() {
        boolean result = client
                .language("de")
                .startPage("Idyll")
                .clickLimit(1)
                .goalPage("Gattung (Literatur)")
                .run();

        Assert.assertTrue(result);
        Assert.assertArrayEquals(new String[] {"Idyll","Gattung (Literatur)"}, client.getPathTaken());
    }

    @Test
    public void test_FirstLinkIsItalicStyle_ClickFirstLinkNormalStyle() {
        boolean result = client
                .language("de")
                .startPage("Mühl-Schlössl")
                .clickLimit(1)
                .goalPage("Lend (Graz)")
                .run();

        Assert.assertTrue(result);
        Assert.assertArrayEquals(new String[] {"Mühl-Schlössl","Lend (Graz)"}, client.getPathTaken());
    }

    @Test
    public void test_ExploratoryTest() {
        boolean result = client
                .language("de")
                .startPage("Hermann Heinrich Becker")
                .clickLimit(1)
                .goalPage("Politiker")
                .run();

        Assert.assertTrue(result);
        Assert.assertArrayEquals(new String[] {"Hermann Heinrich Becker","Politiker"}, client.getPathTaken());
    }
}