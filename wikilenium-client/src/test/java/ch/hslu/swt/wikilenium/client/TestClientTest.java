package ch.hslu.swt.wikilenium.client;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

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

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure language cannot be null")
    @Test(expected = IllegalStateException.class)
    public void test_LanguageIsNull_ThrowException() {
        client.language(null)
                .startPage("Something")
                .clickLimit(0)
                .goalPage("Something")
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure language cannot be empty")
    @Test(expected = IllegalStateException.class)
    public void test_LanguageIsEmpty_ThrowException() {
        client.language("")
                .startPage("Something")
                .clickLimit(0)
                .goalPage("Something")
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure start page cannot be null")
    @Test(expected = IllegalStateException.class)
    public void test_StartPageIsNull_ThrowException() {
        client.language("de")
                .startPage(null)
                .clickLimit(0)
                .goalPage("Something")
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure start page cannot be empty")
    @Test(expected = IllegalStateException.class)
    public void test_StartPageIsEmpty_ThrowException() {
        client.language("de")
                .startPage("")
                .clickLimit(0)
                .goalPage("Something")
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure click limit cannot be less than zero")
    @Test(expected = IllegalStateException.class)
    public void test_ClickLimitIsLessThanZero_ThrowException() {
        client.language("de")
                .startPage("Something")
                .clickLimit(-1)
                .goalPage("Something")
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure goal page cannot be null")
    @Test(expected = IllegalStateException.class)
    public void test_GoalPageIsNull_ThrowException() {
        client.language("de")
                .startPage("Something")
                .clickLimit(0)
                .goalPage(null)
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure goal page cannot be empty")
    @Test(expected = IllegalStateException.class)
    public void test_GoalPageIsEmpty_ThrowException() {
        client.language("de")
                .startPage("Something")
                .clickLimit(0)
                .goalPage("")
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure no clicks are needed to reach the end page, if it is same as the start page")
    @Test
    public void test_StartPageSameAsExpectedPage_ClickLimitZero_ReturnTrue() {
        boolean result = client
                .language("de")
                .startPage("Philosophie")
                .clickLimit(0)
                .goalPage("Philosophie")
                .run();

        Assert.assertTrue(result);
        Assert.assertArrayEquals(new String[] { "Philosophie" }, client.getPathTaken());
        Assert.assertEquals(0, client.getClickCount());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure the end page is reached, if it is same as the start page and click limit is 2")
    @Test
    public void test_StartPageSameAsExpectedPage_ClickLimitGreaterZero_ReturnTrue() {
        boolean result = client
                .language("de")
                .startPage("Philosophie")
                .clickLimit(2)
                .goalPage("Philosophie")
                .run();

        Assert.assertTrue(result);
        Assert.assertArrayEquals(new String[] { "Philosophie" }, client.getPathTaken());
        Assert.assertEquals(0, client.getClickCount());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure \"Philosophie\" is reached from \"Begriff (Philosophie)\" after 2 steps")
    @Test
    public void test_StartPageOneHopAwayFromExpectedPage_ClickLimitTwo_ReturnTrue() {
        boolean result = client
                .language("de")
                .startPage("Begriff (Philosophie)")
                .clickLimit(2)
                .goalPage("Philosophie")
                .run();

        Assert.assertTrue(result);
        Assert.assertArrayEquals(new String[] { "Begriff (Philosophie)", "Philosophie" }, client.getPathTaken());
        Assert.assertEquals(1, client.getClickCount());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure \"Philosophie\" is reached from \"Wissensgebiet\" after 2 steps")
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
                new String[] { "Wissensgebiet", "Begriff (Philosophie)", "Philosophie" },
                client.getPathTaken());
        Assert.assertEquals(2, client.getClickCount());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure \"Philosophie\" is reached from \"Fachgebiet\" after 2 steps")
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
                new String[] { "Fachgebiet", "Wissensgebiet", "Begriff (Philosophie)" },
                client.getPathTaken());
        Assert.assertEquals(2, client.getClickCount());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure \"Übereignung\" is reached from \"Kredit\" after 1 step")
    @Test
    public void test_FirstLinkIsInsideBrackets_ClickFirstLinkOutsideBrackets() {
        boolean result = client
                .language("de")
                .startPage("Kredit")
                .clickLimit(1)
                .goalPage("Übereignung")
                .run();

        Assert.assertTrue(result);
        Assert.assertArrayEquals(new String[] { "Kredit", "Übereignung" }, client.getPathTaken());
        Assert.assertEquals(1, client.getClickCount());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure \"Bundesstaat (Föderaler Staat)\" is reached from \"Deutschland\" after 1 step")
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
                new String[] { "Deutschland", "Bundesstaat (Föderaler Staat)" },
                client.getPathTaken());
        Assert.assertEquals(1, client.getClickCount());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure \"Football League First Division\" is reached from \"Football League First Division 1903/04\" after 1 step")
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
                new String[] { "Football League First Division 1903/04", "Football League First Division" },
                client.getPathTaken());
        Assert.assertEquals(1, client.getClickCount());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure \"Gattung (Literatur)\" is reached from \"Idyll\" after 1 step")
    @Test
    public void test_LinkInBracketsHasSpecialRegexCharacter_ClickFirstLinkOutsideBrackets() {
        boolean result = client
                .language("de")
                .startPage("Idyll")
                .clickLimit(1)
                .goalPage("Gattung (Literatur)")
                .run();

        Assert.assertTrue(result);
        Assert.assertArrayEquals(new String[] { "Idyll", "Gattung (Literatur)" }, client.getPathTaken());
        Assert.assertEquals(1, client.getClickCount());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure \"Lend (Graz)\" is reached from \"Mühl-Schlössl\" after 1 step")
    @Test
    public void test_FirstLinkIsItalicStyle_ClickFirstLinkNormalStyle() {
        boolean result = client
                .language("de")
                .startPage("Mühl-Schlössl")
                .clickLimit(1)
                .goalPage("Lend (Graz)")
                .run();

        Assert.assertTrue(result);
        Assert.assertArrayEquals(new String[] { "Mühl-Schlössl", "Lend (Graz)" }, client.getPathTaken());
        Assert.assertEquals(1, client.getClickCount());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure loop in path is detected and LoopException is thrown")
    @Test
    public void test_PathContainsLoop_DetectLoopEarlyAndReturnFalse() {
        boolean result = client
                .language("de")
                .startPage("Coca-Cola")
                .clickLimit(100)
                .goalPage("Philosophie")
                .run();

        Assert.assertFalse(result);
        Assert.assertArrayEquals(
                new String[] { "Coca-Cola", "Marke (Marketing)", "Marketing", "Absatzwirtschaft", "Marketing" },
                client.getPathTaken());
        Assert.assertEquals(4, client.getClickCount());
    }
}