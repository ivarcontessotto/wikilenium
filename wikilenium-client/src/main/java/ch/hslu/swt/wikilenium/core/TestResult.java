package ch.hslu.swt.wikilenium.core;

public class TestResult {

    private final boolean isPassed;
    private final int clickCount;
    private final String[] pathTaken;

    TestResult(boolean isPassed, int clickCount, String[] pathTaken) {
        this.isPassed = isPassed;
        this.clickCount = clickCount;
        this.pathTaken = pathTaken;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public int getClickCount() {
        return clickCount;
    }

    public String[] getPathTaken() {
        return pathTaken;
    }
}
