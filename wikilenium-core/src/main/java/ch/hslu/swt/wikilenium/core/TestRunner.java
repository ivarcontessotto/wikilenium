package ch.hslu.swt.wikilenium.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.google.common.base.Strings;

import io.qameta.allure.Step;

public class TestRunner {

    private final WebDriverFactory driverFactory;
    private WebDriver driver;
    private String language;
    private String startPageName;
    private int clickLimit;
    private String goalPageName;
    private List<String> pathTaken;

    TestRunner(WebDriverFactory webDriverFactory) {
        driverFactory = webDriverFactory;
        driver = null;
        language = null;
        startPageName = null;
        clickLimit = -1;
        goalPageName = null;
        pathTaken = new LinkedList<>();
    }

    @Step("Set language to {0}")
    public TestRunner language(String language) {
        this.language = language;
        return this;
    }

    @Step("Set start page to {0}")
    public TestRunner startPage(String name) {
        startPageName = name;
        return this;
    }

    @Step("Set maximum number of steps to {0}")
    public TestRunner clickLimit(int n) {
        clickLimit = n;
        return this;
    }

    @Step("Set goal page to {0}")
    public TestRunner goalPage(String name) {
        goalPageName = name;
        return this;
    }

    public TestResult run() {
        validateSetup();
        driver = driverFactory.getWebDriver();
        boolean isPassed = runTest();
        closeDriver();
        TestResult result = new TestResult(isPassed, pathTaken.size() - 1, pathTaken.toArray(new String[0]));
        pathTaken.clear();
        return result;
    }

    @Step("Validate test input")
    private void validateSetup() {
        if (Strings.isNullOrEmpty(language)) {
            throw new IllegalStateException("Language is not setup.");
        }
        if (Strings.isNullOrEmpty(startPageName)) {
            throw new IllegalStateException("Start page is not setup.");
        }
        if (clickLimit < 0) {
            throw new IllegalStateException("Click limit is not setup.");
        }
        if (Strings.isNullOrEmpty(goalPageName)) {
            throw new IllegalStateException("Goal page is not setup.");
        }
    }

    private boolean runTest() {
        goToStartPage();
        clickLinksUntilPageFoundOrLimitReached();
        return currentPageIsGoal();
    }

    @Step("Go to start page")
    private void goToStartPage() {
        String startUrl = "https://" + language + ".wikipedia.org/wiki/" + startPageName;
        driver.get(startUrl);
        System.out.println("Start page: " + startUrl);
    }

    @Step("Clicking through wikipedia")
    private void clickLinksUntilPageFoundOrLimitReached() {
        pathTaken.add(getCurrentPageName());
        int i = 0;
        while (!currentPageIsGoal() && i < clickLimit) {
            Optional<WebElement> matchingLink = getFirstMatchingLinkInContent();
            if (!matchingLink.isPresent()) {
                System.out.println("No matching links found on page.");
                break;
            }
            System.out.println(String.format("Clicking link: %s", matchingLink.get().getText()));
            matchingLink.get().click();
            String currentPageName = getCurrentPageName();
            if (pathTaken.contains(currentPageName)) {
                pathTaken.add(currentPageName);
                System.out.println("Loop in path detected.");
                break;
            }
            pathTaken.add(currentPageName);
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
                .findFirst().orElse(null);
    }

    private boolean linkIsNotInBrackets(String linkText, String parentTagText) {
        // System.out.println("Inspecting link: " + linkText);
        // System.out.println("Current section: " + parentTagText);
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
                Pattern.compile(
                        String.format("(?<!\\([^\\)]{0,1000})%s(?![^\\(]{0,1000}\\))", Pattern.quote(linkText))),
                        parentTagText);
    }

    private Optional<Integer> findFirstMatch(Pattern regexPattern, String parentTagText) {
        Matcher matcher = regexPattern.matcher(parentTagText);
        if (matcher.find())
            return Optional.of(matcher.start());
        return Optional.empty();
    }

    @Step("Close web browser")
    private void closeDriver() {
        if (driver != null) {
            driver.close();
        }
    }
}