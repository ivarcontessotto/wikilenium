package wikilenium.testclient;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestClient {

    private WebDriver driver;
    private String startUrl;
    private int clickLinksLimit;
    private String goalWikiPageName;

    public TestClient(WebDriver driver) {
        this.driver = driver;
        startUrl = null;
        clickLinksLimit = -1;
        goalWikiPageName = null;
    }

    public void close() {
        driver.close();
    }

    public TestClient startAtWikiPage(String url) {
        startUrl = url;
        return this;
    }

    public TestClient clickAtMostNLinks(int limit) {
        clickLinksLimit = limit;
        return this;
    }

    public TestClient untilWikiPageIs(String goalWikiPageName) {
        this.goalWikiPageName = goalWikiPageName;
        return this;
    }

    public boolean run() {
        validateSetup();
        return runTest();
    }

    private void validateSetup() {
        if (startUrl == null) {
            throw new IllegalStateException("Test start url is not setup properly.");
        }
        if (clickLinksLimit < 0) {
            throw new IllegalStateException("Click links limit is not setup properly.");
        }
        if (goalWikiPageName == null) {
            throw new IllegalStateException("Goal wiki page name is not setup properly.");
        }
    }

    private boolean runTest() {
        driver.get(startUrl);
        System.out.println("Start url: " + startUrl);
        clickLinksUntilPageFoundOrLimitReached();
        return currentPageHasExpectedName();
    }

    private void clickLinksUntilPageFoundOrLimitReached() {
        int i = 0;
        while (!currentPageHasExpectedName() && i < clickLinksLimit) {
            Optional<WebElement> matchingLink = getFirstMatchingLinkInContent();
            if (!matchingLink.isPresent()) {
                System.out.println("No matching links found on page.");
                break;
            }
            System.out.println(String.format("Clicking link: %s", matchingLink.get().getText()));
            matchingLink.get().click();
            i++;
        }
    }

    private boolean currentPageHasExpectedName() {
        return getCurrentWikiPageName().contentEquals(goalWikiPageName);
    }

    private String getCurrentWikiPageName() {
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
