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
    private String goalWikiPageName;

    public GameClient(WebDriver driver) {
        this.driver = driver;
        startUrl = null;
        clickLinksLimit = -1;
        goalWikiPageName = null;
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

    public GameClient untilWikiPageIs(String goalWikiPageName) {
        this.goalWikiPageName = goalWikiPageName;
        return this;
    }

    public boolean run() {
        validateSetup();
        return playGame();
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

    private boolean playGame() {
        driver.get(startUrl);
        System.out.println("Start url: " + startUrl);
        clickLinksUntilPageFoundOrLimitReached();
        if (!currentPageHasExpectedName()) {
            System.out.println(String.format("Expected wiki page to be <%s>' but found <%s>", goalWikiPageName, getCurrentWikiPageName()));
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
        return getCurrentWikiPageName().contentEquals(goalWikiPageName);
    }

    private String getCurrentWikiPageName() {
        return this.driver.findElement(By.id("firstHeading")).getText();
    }

    private Optional<WebElement> getFirstMatchingLinkInContent() {
        return driver.findElements(By.xpath("//div[@class='mw-parser-output']/p")).stream()
                .map(this::findFirstMatchingLinkInTag)
                .filter(a -> a != null)
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
        System.out.println("Inspecting link: " + linkText);
        System.out.println("Current section: " + parentTagText);
        List<Integer> allOccurrences = findAllOccurrences(linkText, parentTagText);
        List<Integer>  occurrencesNotInBrackets = findOccurrencesNotInBrackets(linkText, parentTagText);
        return !allOccurrences.isEmpty() &&
                !occurrencesNotInBrackets.isEmpty() &&
                allOccurrences.get(0).equals(occurrencesNotInBrackets.get(0));
    }

    private List<Integer> findAllOccurrences(String linkText, String parentTagtext) {
        return findOccurrences(Pattern.compile(Pattern.quote(linkText)), parentTagtext);
    }

    private List<Integer> findOccurrencesNotInBrackets(String linkText, String parentTagText) {
        return findOccurrences(
                Pattern.compile(String.format("(?<!\\([^\\)]{0,1000})%s(?![^\\(]{0,1000}\\))", Pattern.quote(linkText))),
                parentTagText);
    }

    private List<Integer> findOccurrences(Pattern regexPattern, String parentTagText) {
        Matcher matcher = regexPattern.matcher(parentTagText);
        List<Integer> occurrences = new LinkedList<>();
        while (matcher.find()) {
            occurrences.add(matcher.start());
        }
        return occurrences;
    }
}
