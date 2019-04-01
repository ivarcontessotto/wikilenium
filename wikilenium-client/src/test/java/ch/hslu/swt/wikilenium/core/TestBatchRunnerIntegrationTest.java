package ch.hslu.swt.wikilenium.core;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;

public class TestBatchRunnerIntegrationTest {

    private final String TEST_FILE_NAME = "TestBatch.xlsx";
    private final Wikilenium wikilenium;
    private Path resourceFilePath;
    private File notExistingFile;
    private TestBatchRunner testBatchRunner;

    public TestBatchRunnerIntegrationTest() throws URISyntaxException {

        resourceFilePath = Paths.get(getClass().getResource("/" + TEST_FILE_NAME).toURI());
        wikilenium = new Wikilenium();
    }

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void testSetup() throws IOException {
        Path excelFilePath = Paths.get(temporaryFolder.getRoot().getPath(), "TestBatch.xlsx");
        Files.copy(resourceFilePath, excelFilePath, StandardCopyOption.REPLACE_EXISTING);
        notExistingFile = excelFilePath.toFile();
        testBatchRunner = wikilenium.getTestBatchRunner(Wikilenium.Browser.CHROME);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure language cannot be null")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_LanguageIsNull_ThrowException() {
        testBatchRunner.language(null)
                .clickLimit(0)
                .goalPage("Something")
                .inputOutputFile(notExistingFile)
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure language cannot be empty")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_LanguageIsEmpty_ThrowException() {
        testBatchRunner.language("")
                .clickLimit(0)
                .goalPage("Something")
                .inputOutputFile(notExistingFile)
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure click limit cannot be less than zero")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_ClickLimitIsLessThanZero_ThrowException() {
        testBatchRunner.language("de")
                .clickLimit(-1)
                .goalPage("Philosophie")
                .inputOutputFile(notExistingFile)
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure goal page cannot be null")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_GoalPageIsNull_ThrowException() {
        testBatchRunner.language("de")
                .clickLimit(0)
                .goalPage(null)
                .inputOutputFile(notExistingFile)
                .run();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Make sure goal page cannot be empty")
    @Test(expected = IllegalStateException.class)
    public void test_TestBatchRunner_GoalPageIsEmpty_ThrowException() {
        testBatchRunner.language("de")
                .clickLimit(0)
                .goalPage("")
                .inputOutputFile(notExistingFile)
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
        notExistingFile = Paths.get(temporaryFolder.getRoot().getPath(), "NotExisting.xlsx").toFile();

        testBatchRunner.language("de")
                .clickLimit(0)
                .goalPage("Philosophie")
                .inputOutputFile(notExistingFile)
                .run();
    }
}