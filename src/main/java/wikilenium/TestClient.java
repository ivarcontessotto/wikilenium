package wikilenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestClient {

    private final WebDriver driver;
    private String language;
    private String startPageName;
    private int clickLimit;
    private String goalPageName;
    private List<String> pathTaken;

    TestClient(WebDriver webDriver) {
        driver = webDriver;
        language = null;
        startPageName = null;
        clickLimit = -1;
        goalPageName = null;
        pathTaken = new LinkedList<>();
    }

    public void close() {
        driver.close();
    }

    public TestClient language(String language) {
        this.language = language;
        return this;
    }

    public TestClient startPage(String name) {
        startPageName = name;
        return this;
    }

    public TestClient clickLimit(int n) {
        clickLimit = n;
        return this;
    }

    public TestClient goalPage(String name) {
        goalPageName = name;
        return this;
    }

    String[] getPathTaken() {
        return pathTaken.toArray(new String[0]);
    }

    public boolean run() {
        validateSetup();
        return runTest();
    }

    private void validateSetup() {
        if (language == null) {
            throw new IllegalStateException("Language is not setup.");
        }
        if (startPageName == null) {
            throw new IllegalStateException("Start page is not setup.");
        }
        if (clickLimit < 0) {
            throw new IllegalStateException("Click limit is not setup.");
        }
        if (goalPageName == null) {
            throw new IllegalStateException("Goal page is not setup.");
        }
    }

    private boolean runTest() {
        openStartPage();
        clickLinksUntilPageFoundOrLimitReached();
        return currentPageIsGoal();
    }

    private void openStartPage() {
        String startUrl = "https://" + language + ".wikipedia.org/wiki/" + startPageName;
        driver.get(startUrl);
        System.out.println("Start page: " + startUrl);
    }

    private void clickLinksUntilPageFoundOrLimitReached() {
        int i = 0;
        pathTaken.add(getCurrentPageName());
        while (!currentPageIsGoal() && i < clickLimit) {
            Optional<WebElement> matchingLink = getFirstMatchingLinkInContent();
            if (!matchingLink.isPresent()) {
                System.out.println("No matching links found on page.");
                break;
            }
            System.out.println(String.format("Clicking link: %s", matchingLink.get().getText()));
            matchingLink.get().click();
            pathTaken.add(getCurrentPageName());
            i++;
        }
    }

    private boolean currentPageIsGoal() {
        return getCurrentPageName().contentEquals(goalPageName);
    }

    private String getCurrentPageName() {
        return this.driver.findElement(By.id("firstHeading")).getText();
    }

    private Optional<WebElement> getFirstMatchingLinkInContent() {
        return driver.findElements(By.xpath("//div[@class='mw-parser-output']/p")).stream()
                .map(this::findFirstMatchingLinkInTag)
                .filter(Objects::nonNull)
                .findFirst();
    }

    private WebElement findFirstMatchingLinkInTag(WebElement tag) {
        return tag.findElements(By.xpath("a | b/a")).stream()
                .filter(link -> !link.getText().isEmpty())
                .filter(link -> linkIsNotInBrackets(link.getText(), tag.getText()))
                .findFirst()
                .orElse(null);
    }

    private boolean linkIsNotInBrackets(String linkText, String parentTagText) {
        //System.out.println("Inspecting link: " + linkText);
        //System.out.println("Current section: " + parentTagText);
        Optional<Integer> firstMatch = findFirstMatch(linkText, parentTagText);
        Optional<Integer> firstMatchNotInBrackets = findFirstMatchNotInBrackets(linkText, parentTagText);
        return firstMatch.isPresent() &&
                firstMatchNotInBrackets.isPresent() &&
                firstMatch.get().equals(firstMatchNotInBrackets.get());
    }

    private Optional<Integer> findFirstMatch(String linkText, String parentTagText) {
        return findFirstMatch(Pattern.compile(Pattern.quote(linkText)), parentTagText);
    }

    private Optional<Integer> findFirstMatchNotInBrackets(String linkText, String parentTagText) {
        return findFirstMatch(
                Pattern.compile(String.format("(?<!\\([^\\)]{0,1000})%s(?![^\\(]{0,1000}\\))", Pattern.quote(linkText))),
                parentTagText);
    }

    private Optional<Integer> findFirstMatch(Pattern regexPattern, String parentTagText) {
        Matcher matcher = regexPattern.matcher(parentTagText);
        if (matcher.find()) return Optional.of(matcher.start());
        return Optional.empty();
    }
}