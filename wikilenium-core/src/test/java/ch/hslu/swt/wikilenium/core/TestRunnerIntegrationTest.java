package ch.hslu.swt.wikilenium.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

public class TestRunnerIntegrationTest {

    private static final String EMPTY_STRING = "";

    private final Wikilenium wikilenium;
    private TestRunner testRunner;

    public TestRunnerIntegrationTest() {
        wikilenium = new Wikilenium();
    }

    @Before
    public void testSetup() {
        testRunner = wikilenium.getTestRunner(Wikilenium.Browser.CHROME);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure language cannot be null")
    @Test(expected = IllegalStateException.class)
    public void test_TestRunner_LanguageIsNull_ThrowException() {
        testRunner.language(null)
                .startPage("Something")
                .clickLimit(0)
                .goalPage("Something")
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure language cannot be empty")
    @Test(expected = IllegalStateException.class)
    public void test_TestRunner_LanguageIsEmpty_ThrowException() {
        testRunner.language("")
                .startPage("Something")
                .clickLimit(0)
                .goalPage("Something")
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure start page cannot be null")
    @Test(expected = IllegalStateException.class)
    public void test_TestRunner_StartPageIsNull_ThrowException() {
        testRunner.language("de")
                .startPage(null)
                .clickLimit(0)
                .goalPage("Something")
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure start page cannot be empty")
    @Test(expected = IllegalStateException.class)
    public void test_TestRunner_StartPageIsEmpty_ThrowException() {
        testRunner.language("de")
                .startPage("")
                .clickLimit(0)
                .goalPage("Something")
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure click limit cannot be less than zero")
    @Test(expected = IllegalStateException.class)
    public void test_TestRunner_ClickLimitIsLessThanZero_ThrowException() {
        testRunner.language("de")
                .startPage("Something")
                .clickLimit(-1)
                .goalPage("Something")
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure goal page cannot be null")
    @Test(expected = IllegalStateException.class)
    public void test_TestRunner_GoalPageIsNull_ThrowException() {
        testRunner.language("de")
                .startPage("Something")
                .clickLimit(0)
                .goalPage(null)
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure goal page cannot be empty")
    @Test(expected = IllegalStateException.class)
    public void test_TestRunner_GoalPageIsEmpty_ThrowException() {
        testRunner.language("de")
                .startPage("Something")
                .clickLimit(0)
                .goalPage("")
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure no clicks are needed to reach the end page, if it is same as the start page")
    @Test
    public void test_TestRunner_StartPageSameAsExpectedPage_ClickLimitZero_ReturnTrue() {
        TestResult result = testRunner
                .language("de")
                .startPage("Philosophie")
                .clickLimit(0)
                .goalPage("Philosophie")
                .run();

        Assert.assertTrue(result.isPassed());
        Assert.assertArrayEquals(new String[] { "Philosophie" }, result.getPathTaken());
        Assert.assertEquals(0, result.getClickCount());
        Assert.assertEquals(EMPTY_STRING, result.getFailReason());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure the end page is reached, if it is same as the start page and click limit is 2")
    @Test
    public void test_TestRunner_StartPageSameAsExpectedPage_ClickLimitGreaterZero_ReturnTrue() {
        TestResult result = testRunner
                .language("de")
                .startPage("Philosophie")
                .clickLimit(2)
                .goalPage("Philosophie")
                .run();

        Assert.assertTrue(result.isPassed());
        Assert.assertArrayEquals(new String[] { "Philosophie" }, result.getPathTaken());
        Assert.assertEquals(0, result.getClickCount());
        Assert.assertEquals(EMPTY_STRING, result.getFailReason());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure \"Philosophie\" is reached from \"Begriff (Philosophie)\" after 2 steps")
    @Test
    public void test_TestRunner_StartPageOneHopAwayFromExpectedPage_ClickLimitTwo_ReturnTrue() {
        TestResult result = testRunner
                .language("de")
                .startPage("Begriff (Philosophie)")
                .clickLimit(2)
                .goalPage("Philosophie")
                .run();

        Assert.assertTrue(result.isPassed());
        Assert.assertArrayEquals(new String[] { "Begriff (Philosophie)", "Philosophie" }, result.getPathTaken());
        Assert.assertEquals(1, result.getClickCount());
        Assert.assertEquals(EMPTY_STRING, result.getFailReason());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure \"Philosophie\" is reached from \"Wissensgebiet\" after 2 steps")
    @Test
    public void test_TestRunner_StartPageTwoHopsAwayFromExpectedPage_ClickLimitTwo_ReturnTrue() {
        TestResult result = testRunner
                .language("de")
                .startPage("Wissensgebiet")
                .clickLimit(2)
                .goalPage("Philosophie")
                .run();

        Assert.assertTrue(result.isPassed());
        Assert.assertArrayEquals(
                new String[] { "Wissensgebiet", "Begriff (Philosophie)", "Philosophie" },
                result.getPathTaken());
        Assert.assertEquals(2, result.getClickCount());
        Assert.assertEquals(EMPTY_STRING, result.getFailReason());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure \"Philosophie\" is reached from \"Fachgebiet\" after 2 steps")
    @Test
    public void test_TestRunner_StartPageThreeHopsAwayFromExpectedPage_ClickLimitTwo_ReturnFalse() {
        TestResult result = testRunner
                .language("de")
                .startPage("Fachgebiet")
                .clickLimit(2)
                .goalPage("Philosophie")
                .run();

        Assert.assertFalse(result.isPassed());
        Assert.assertArrayEquals(
                new String[] { "Fachgebiet", "Wissensgebiet", "Begriff (Philosophie)" },
                result.getPathTaken());
        Assert.assertEquals(2, result.getClickCount());
        Assert.assertEquals("Did not reach page 'Philosophie' after 2 clicks.", result.getFailReason());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure \"Übereignung\" is reached from \"Kredit\" after 1 step")
    @Test
    public void test_TestRunner_FirstLinkIsInsideBrackets_ClickFirstLinkOutsideBrackets() {
        TestResult result = testRunner
                .language("de")
                .startPage("Kredit")
                .clickLimit(1)
                .goalPage("Übereignung")
                .run();

        Assert.assertTrue(result.isPassed());
        Assert.assertArrayEquals(new String[] { "Kredit", "Übereignung" }, result.getPathTaken());
        Assert.assertEquals(1, result.getClickCount());
        Assert.assertEquals(EMPTY_STRING, result.getFailReason());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure \"Bundesstaat (Föderaler Staat)\" is reached from \"Deutschland\" after 1 step")
    @Test
    public void test_TestRunner_FirstLinkIsInsideBracketsAndHasUnreadableCharacters_ClickFirstLinkOutsideBrackets() {
        TestResult result = testRunner
                .language("de")
                .startPage("Deutschland")
                .clickLimit(1)
                .goalPage("Bundesstaat (Föderaler Staat)")
                .run();

        Assert.assertTrue(result.isPassed());
        Assert.assertArrayEquals(
                new String[] { "Deutschland", "Bundesstaat (Föderaler Staat)" },
                result.getPathTaken());
        Assert.assertEquals(1, result.getClickCount());
        Assert.assertEquals(EMPTY_STRING, result.getFailReason());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure \"Football League First Division\" is reached from \"Football League First Division 1903/04\" after 1 step")
    @Test
    public void test_TestRunner_FirstLinkIsInBoldStyle_ClickBoldStyleLink() {
        TestResult result = testRunner
                .language("de")
                .startPage("Football League First Division 1903/04")
                .clickLimit(1)
                .goalPage("Football League First Division")
                .run();

        Assert.assertTrue(result.isPassed());
        Assert.assertArrayEquals(
                new String[] { "Football League First Division 1903/04", "Football League First Division" },
                result.getPathTaken());
        Assert.assertEquals(1, result.getClickCount());
        Assert.assertEquals(EMPTY_STRING, result.getFailReason());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure \"Gattung (Literatur)\" is reached from \"Idyll\" after 1 step")
    @Test
    public void test_TestRunner_LinkInBracketsHasSpecialRegexCharacter_ClickFirstLinkOutsideBrackets() {
        TestResult result = testRunner
                .language("de")
                .startPage("Idyll")
                .clickLimit(1)
                .goalPage("Gattung (Literatur)")
                .run();

        Assert.assertTrue(result.isPassed());
        Assert.assertArrayEquals(new String[] { "Idyll", "Gattung (Literatur)" }, result.getPathTaken());
        Assert.assertEquals(1, result.getClickCount());
        Assert.assertEquals(EMPTY_STRING, result.getFailReason());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure \"Lend (Graz)\" is reached from \"Mühl-Schlössl\" after 1 step")
    @Test
    public void test_TestRunner_FirstLinkIsItalicStyle_ClickFirstLinkNormalStyle() {
        TestResult result = testRunner
                .language("de")
                .startPage("Mühl-Schlössl")
                .clickLimit(1)
                .goalPage("Lend (Graz)")
                .run();

        Assert.assertTrue(result.isPassed());
        Assert.assertArrayEquals(new String[] { "Mühl-Schlössl", "Lend (Graz)" }, result.getPathTaken());
        Assert.assertEquals(1, result.getClickCount());
        Assert.assertEquals(EMPTY_STRING, result.getFailReason());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure loop in path is detected and LoopException is thrown")
    @Test
    public void test_TestRunner_PathContainsLoop_DetectLoopEarlyAndReturnFalse() {
        TestResult result = testRunner
                .language("de")
                .startPage("Coca-Cola")
                .clickLimit(100)
                .goalPage("Philosophie")
                .run();

        Assert.assertFalse(result.isPassed());
        Assert.assertArrayEquals(
                new String[] { "Coca-Cola", "Marke (Marketing)", "Marketing", "Absatzwirtschaft", "Marketing" },
                result.getPathTaken());
        Assert.assertEquals(4, result.getClickCount());
        Assert.assertEquals("Endless loop detected between 'Absatzwirtschaft' and 'Marketing'.", result.getFailReason());
    }

    @Test
    public void test_TestRunner_PageDoesNotContainMatchingLink_DetectDeadEndAndReturnFalse() {
        TestResult result = testRunner
                .language("de")
                .startPage("auä")
                .clickLimit(100)
                .goalPage("Philosophie")
                .run();

        Assert.assertFalse(result.isPassed());
        Assert.assertArrayEquals(
                new String[] { "Auä" }, result.getPathTaken());
        Assert.assertEquals(0, result.getClickCount());
        Assert.assertEquals("Dead end on page 'Auä'.", result.getFailReason());
    }
}