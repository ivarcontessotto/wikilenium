package gameclient;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameClient {

    private WebDriver driver;
    private String startUrl;
    private int clickLinksLimit;
    private String expectedWikiPageName;

    public GameClient(WebDriver driver) {
        this.driver = driver;
        startUrl = null;
        clickLinksLimit = -1;
        expectedWikiPageName = null;
    }

    public void close() {
        driver.close();
    }

    public GameClient startAtWikiPage(String url) {
        startUrl = url;
        return this;
    }

    public GameClient clickAtMostNLinks(int limit) {
        clickLinksLimit = limit;
        return this;
    }

    public GameClient untilWikiPageIs(String expectedPageName) {
        expectedWikiPageName = expectedPageName;
        return this;
    }

    public boolean run() {
        validateTestSetup();
        return runTest();
    }

    private void validateTestSetup() {
        if (startUrl == null) {
            throw new IllegalStateException("Test start url is not setup properly.");
        }
        if (clickLinksLimit < 0) {
            throw new IllegalStateException("Click links limit is not setup properly.");
        }
        if (expectedWikiPageName == null) {
            throw new IllegalStateException("Expected wiki page name is not setup properly.");
        }
    }

    private boolean runTest() {
        driver.get(startUrl);
        System.out.println(String.format("Start url: %s", startUrl));
        clickLinksUntilPageFoundOrLimitReached();
        if (!currentPageHasExpectedName()) {
            System.out.println(String.format("Expected wiki page to be <%s>' but found <%s>", expectedWikiPageName, getCurrentWikiPageName()));
            return false;
        }
        return true;
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
        return getCurrentWikiPageName().contentEquals(expectedWikiPageName);
    }

    private String getCurrentWikiPageName() {
        return this.driver.findElement(By.id("firstHeading")).getText();
    }

    private Optional<WebElement> getFirstMatchingLinkInContent() {
        return driver.findElements(By.xpath("//div[@class='mw-parser-output']/p")).stream()
                .map(this::getFirstMatchingLinkInSection)
                .filter(a -> a != null)
                .findFirst();
    }

    private WebElement getFirstMatchingLinkInSection(WebElement pTag) {
        return pTag.findElements(By.xpath("a")).stream()
                .filter(a -> linkIsNotInBrackets(pTag.getText(), a.getText()))
                .findFirst()
                .orElse(null);
    }

    private boolean linkIsNotInBrackets(String sectionText, String linkText) {
        List<Integer> allMatches = findAllMatches(sectionText, linkText);
        List<Integer>  matchesOutsideBrackets = findMatchesOutsideBrackets(sectionText, linkText);
        return !allMatches.isEmpty() &&
                !matchesOutsideBrackets.isEmpty() &&
                allMatches.get(0).equals(matchesOutsideBrackets.get(0));
    }

    private List<Integer> findAllMatches(String sectionText, String linkText) {
        return findMatches(sectionText, linkText);
    }

    private List<Integer> findMatchesOutsideBrackets(String sectionText, String linkText) {
        return findMatches(sectionText, String.format("(?<!\\([^\\)]{0,100})%s(?![^\\(]{0,100}\\))", linkText));
    }

    private List<Integer> findMatches(String sectionText, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sectionText);
        List<Integer> occurrences = new LinkedList<>();
        while (matcher.find()) {
            occurrences.add(matcher.start());
        }
        return occurrences;
    }
}
