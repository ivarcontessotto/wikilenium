package ch.hslu.swt.wikilenium.core;

import com.google.common.base.Strings;
import io.qameta.allure.Step;

import java.io.File;
import java.io.IOException;

public class TestBatchRunner {

    private final TestRunner testRunner;
    private String language;
    private int clickLimit;
    private String goalPageName;
    private File inputOutputFile;

    TestBatchRunner(TestRunner testRunner) {
        this.testRunner = testRunner;
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
        try {
            String[] startPageColumnEntries = ExcelFileHelper.readColumn(0, inputOutputFile);
            if (startPageColumnEntries.length < 1 ||
                !startPageColumnEntries[0].equals("Start Page")) {

                throw new IllegalStateException("Input output file does not have columns 'Start Page' and 'Test Result'");
            }

            String[] testResults = new String[startPageColumnEntries.length];
            testResults[0] = "Test Result";
            testRunner.language(language).goalPage(goalPageName).clickLimit(clickLimit);
            for (int i = 1; i < startPageColumnEntries.length; i++) {
                System.out.println(startPageColumnEntries[i]);
                TestResult result = testRunner.startPage(startPageColumnEntries[i]).run();
                if (result.isPassed()) {
                    testResults[i] = Integer.toString(result.getClickCount());
                } else {
                    testResults[i] = "Failed";
                }
            }

            ExcelFileHelper.writeColumn(1, testResults, inputOutputFile);

        } catch (IOException e) {
            throw new IllegalStateException("Error reading input output file");
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
