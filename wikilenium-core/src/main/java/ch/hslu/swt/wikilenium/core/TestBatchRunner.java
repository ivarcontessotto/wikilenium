package ch.hslu.swt.wikilenium.core;

import com.google.common.base.Strings;
import io.qameta.allure.Step;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileStore;

public class TestBatchRunner {

    private String language;
    private int clickLimit;
    private String goalPageName;
    private File inputOutputFile;

    TestBatchRunner(TestRunner testRunner) {
        language = null;
        clickLimit = -1;
        goalPageName = null;
        inputOutputFile = null;
    }

    @Step("Set batch language to {0}")
    public TestBatchRunner language(String language) {
        this.language = language;
        return this;
    }

    @Step("Set batch maximum number of steps to {0}")
    public TestBatchRunner clickLimit(int n) {
        clickLimit = n;
        return this;
    }

    @Step("Set batch goal page to {0}")
    public TestBatchRunner goalPage(String name) {
        goalPageName = name;
        return this;
    }

    @Step("Set batch input output file {0}")
    public TestBatchRunner inputOutputFile(File file) {
        inputOutputFile = file;
        return this;
    }

    public void run() {
        validateSetup();

        try (FileInputStream fileStream = new FileInputStream(inputOutputFile)) {
            XSSFWorkbook workbook = new XSSFWorkbook(fileStream);
        }
        catch (IOException e) {
            throw new IllegalStateException("Error reading input output file.");
        }
    }

    @Step("Validate test batch input")
    private void validateSetup() {
        if (Strings.isNullOrEmpty(language)) {
            throw new IllegalStateException("Batch language is not setup.");
        }
        if (clickLimit < 0) {
            throw new IllegalStateException("Batch click limit is not setup.");
        }
        if (Strings.isNullOrEmpty(goalPageName)) {
            throw new IllegalStateException("Batch goal page is not setup.");
        }
        if (inputOutputFile == null) {
            throw new IllegalStateException("Batch input output file not setup");
        }
    }
}
