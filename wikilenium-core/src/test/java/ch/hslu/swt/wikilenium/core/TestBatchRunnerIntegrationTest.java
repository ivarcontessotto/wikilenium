package ch.hslu.swt.wikilenium.core;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;

public class TestBatchRunnerIntegrationTest {

    private final String VALID_FILE_NAME = "ValidTestBatch.xlsx";
    private final Wikilenium wikilenium;
    private Path validResourceFilePath;
    private File validTempFile;
    private TestBatchRunner testBatchRunner;

    public TestBatchRunnerIntegrationTest() throws URISyntaxException {
        validResourceFilePath = Paths.get(getClass().getResource("/" + VALID_FILE_NAME).toURI());
        wikilenium = new Wikilenium();
    }

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void testSetup() throws IOException {
        Path validTempFilePath = Paths.get(temporaryFolder.getRoot().getPath(), VALID_FILE_NAME);
        Files.copy(validResourceFilePath, validTempFilePath, StandardCopyOption.REPLACE_EXISTING);
        validTempFile = validTempFilePath.toFile();
        testBatchRunner = wikilenium.getTestBatchRunner(Wikilenium.Browser.CHROME);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure language cannot be null")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_LanguageIsNull_ThrowException() {
        testBatchRunner.language(null)
                .clickLimit(0)
                .goalPage("Something")
                .inputOutputFile(validTempFile)
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure language cannot be empty")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_LanguageIsEmpty_ThrowException() {
        testBatchRunner.language("")
                .clickLimit(0)
                .goalPage("Something")
                .inputOutputFile(validTempFile)
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure click limit cannot be less than zero")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_ClickLimitIsLessThanZero_ThrowException() {
        testBatchRunner.language("de")
                .clickLimit(-1)
                .goalPage("Philosophie")
                .inputOutputFile(validTempFile)
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure goal page cannot be null")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_GoalPageIsNull_ThrowException() {
        testBatchRunner.language("de")
                .clickLimit(0)
                .goalPage(null)
                .inputOutputFile(validTempFile)
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure goal page cannot be empty")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_GoalPageIsEmpty_ThrowException() {
        testBatchRunner.language("de")
                .clickLimit(0)
                .goalPage("")
                .inputOutputFile(validTempFile)
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

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure input output file exists")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_InputOutputFileDoesNotExist_ThrowException() {
        File notExistingTempFile = Paths.get(temporaryFolder.getRoot().getPath(), "NotExisting.xlsx").toFile();

        testBatchRunner.language("de")
                .clickLimit(0)
                .goalPage("Philosophie")
                .inputOutputFile(notExistingTempFile)
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure input output file has expected two columns")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_InputOutputFileHasWrongColumns_ThrowException() throws URISyntaxException, IOException {
        String invalidFileName = "InvalidTestBatch.xlsx";
        Path invalidResourceFilePath = Paths.get(getClass().getResource("/" + invalidFileName).toURI());
        Path invalidTempFilePath = Paths.get(temporaryFolder.getRoot().getPath(), invalidFileName);
        Files.copy(invalidResourceFilePath, invalidTempFilePath, StandardCopyOption.REPLACE_EXISTING);
        File invalidTempFile = invalidTempFilePath.toFile();

        testBatchRunner.language("de")
                .clickLimit(2)
                .goalPage("Philosophie")
                .inputOutputFile(invalidTempFile)
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure the test results from a batch run are stored to the input output file.")
    @Test
    public void test_TestBatchRunner_TestRunFinished_InputOutputFileContainsResults() throws IOException {
        testBatchRunner.language("de")
                .clickLimit(1)
                .goalPage("Philosophie")
                .inputOutputFile(validTempFile)
                .run();

        String[] testResultColumnEntries = ExcelFileHelper.readColumn(1, true, validTempFile);
        Assert.assertEquals(5, testResultColumnEntries.length);
        Assert.assertArrayEquals(new String[] { "Test Result", "0", "1", "Failed", "Failed"},
                testResultColumnEntries);
    }
}