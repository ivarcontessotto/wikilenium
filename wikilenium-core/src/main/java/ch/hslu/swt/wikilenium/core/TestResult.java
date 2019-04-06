package ch.hslu.swt.wikilenium.core;

import io.qameta.allure.Step;

public class TestResult {

    private final boolean isPassed;
    private final String failReason;
    private final int clickCount;
    private final String[] pathTaken;

    TestResult(boolean isPassed, int clickCount, String[] pathTaken) {
        this(isPassed, "", clickCount, pathTaken);
    }

    TestResult(boolean isPassed, String failReason, int clickCount, String[] pathTaken) {
        this.isPassed = isPassed;
        this.failReason = failReason;
        this.clickCount = clickCount;
        this.pathTaken = pathTaken;
    }

    @Step("Get test result")
    public boolean isPassed() {
        return isPassed;
    }

    /**
     * @return The fail reason, if the test failed. Otherwise an empty string.
     */
    public String getFailReason() {
        return this.failReason;
    }

    public int getClickCount() {
        return clickCount;
    }

    public String[] getPathTaken() {
        return pathTaken;
    }
}
