package ch.hslu.swt.wikilenium.core;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class TestBatchRunnerIntegrationTest {

    private final Wikilenium wikilenium;
    private TestBatchRunner testBatchRunner;
    private File excelFile;

    public TestBatchRunnerIntegrationTest() {
        wikilenium = new Wikilenium();
    }

    @Before
    public void testSetup() {
        excelFile = new File("");
        testBatchRunner = wikilenium.getTestBatchRunner(Wikilenium.Browser.CHROME);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure language cannot be null")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_LanguageIsNull_ThrowException() {
        testBatchRunner.language(null)
                .clickLimit(0)
                .goalPage("Something")
                .inputOutputFile(excelFile)
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure language cannot be empty")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_LanguageIsEmpty_ThrowException() {
        testBatchRunner.language("")
                .clickLimit(0)
                .goalPage("Something")
                .inputOutputFile(excelFile)
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure click limit cannot be less than zero")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_ClickLimitIsLessThanZero_ThrowException() {
        testBatchRunner.language("de")
                .clickLimit(-1)
                .goalPage("Philosophie")
                .inputOutputFile(excelFile)
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure goal page cannot be null")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_GoalPageIsNull_ThrowException() {
        testBatchRunner.language("de")
                .clickLimit(0)
                .goalPage(null)
                .inputOutputFile(excelFile)
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure goal page cannot be empty")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_GoalPageIsEmpty_ThrowException() {
        testBatchRunner.language("de")
                .clickLimit(0)
                .goalPage("")
                .inputOutputFile(excelFile)
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure input output file cannot be null")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_InputOutputFileIsNull_ThrowException() {
        testBatchRunner.language("de")
                .clickLimit(0)
                .goalPage("Philosophie")
                .inputOutputFile(null)
                .run();
    }
}