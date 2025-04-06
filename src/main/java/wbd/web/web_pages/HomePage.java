package wbd.web.web_pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import wbd.web.core.BasePage;

import java.util.ArrayList;
import java.util.List;


public class HomePage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    @FindBy(xpath = "//button[@type='button' and contains(text(),'Sign In')]")
    WebElement signInButton;

    @FindBy(xpath = "//button[@type='button' and text()='Sign Up']")
    WebElement signUpButton;

    @FindBy(xpath = "(//select[@class='_dropdown_1sio9_1'])[2]")
    WebElement categoryDropdown;

    @FindBy(xpath = "//input[@placeholder='Enter keywords to search']")
    WebElement searchField;

    @FindBy(xpath = "//button[contains(text(),'Go →')]")
    WebElement searchButton;

    @FindBy(xpath = "//button[contains(text(),'>')]")
    WebElement nextButton;

    @FindBy(xpath = "//button[contains(.,'<')]")
    WebElement prevButton;

    @FindBy(xpath = "//div[@class='_totalPage_dqn2o_19']/following-sibling::div[1]//button[not(contains(., '>')) and not(contains(., '<'))]")
    List<WebElement> pageNumbers;

    public HomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public LoginPage getLoginPage() {
        wait.until(ExpectedConditions.elementToBeClickable(signInButton)).click();
        return new LoginPage(driver, wait);
    }

    public HomePage clickAllCategories() {
        wait.until(ExpectedConditions.elementToBeClickable(categoryDropdown)).click();
        logger.info("Clicked on the 'All Categories' dropdown");
        return this;
    }

    public OffersPage selectCategory(String categoryName) {
        Select select = new Select(categoryDropdown);
        select.selectByVisibleText(categoryName);

        String selected = select.getFirstSelectedOption().getText().trim();
        logger.info("The category is chosen: {}", selected);
        Assert.assertEquals(selected, categoryName, "The selected category does not match");

        return new OffersPage(driver, wait);
    }

    public void enterSearchKeyword(String keyword) {
        searchField.clear();
        searchField.sendKeys(keyword);
    }

    public void clickSearchButton() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    public void searchFor(String keyword) {
        enterSearchKeyword(keyword);
        clickSearchButton();
    }

    public List<WebElement> getAdCards() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("._offerContainer_jbegn_11 > div")));
        return driver.findElements(By.cssSelector("._offerContainer_jbegn_11 > div"));
    }

    public String getCityFromCard(WebElement card) {
        return card.findElement(By.cssSelector("._location_jbegn_143")).getText();
    }

    public String getCategoryFromCard(WebElement card) {
        return card.findElement(By.cssSelector("._category_jbegn_130")).getText();
    }

    public void waitForAdCardsWithCategory(String category) {
        wait.until(driver -> {
            List<WebElement> cards = getAdCards();
            if (cards.isEmpty()) {
                return false;
            }
            String categoryText = getCategoryFromCard(cards.get(0));
            System.out.println("Checking category: " + categoryText);
            return categoryText.equals(category);
        });
    }

    public void clickNextPage() {
        click(nextButton, 0);
        waitForCardsToUpdate();
    }

    public void clickPreviousPage() {
        click(prevButton, 0);
        waitForCardsToUpdate();
    }

    public void goToPage(int pageNumber) {
        wait.until(driver -> pageNumbers.size() >= pageNumber);
        WebElement page = pageNumbers.get(pageNumber - 1);
        click(page, 0);
        waitForCardsToUpdate();
    }

    public int getCurrentPageNumber() {
        for (WebElement page : pageNumbers) {
            if (page.getAttribute("class").contains("_active_dqn2o_16")) {
                return Integer.parseInt(page.getText());
            }
        }
        throw new RuntimeException("Current page not found");
    }

    public boolean isNextButtonEnabled() {
        return nextButton.isEnabled();
    }

    public boolean isPreviousButtonEnabled() {
        return prevButton.isEnabled();
    }

    private void waitForCardsToUpdate() {
        List<WebElement> oldCards = getAdCards();
        List<String> oldCardsText = extractTextsFromElements(oldCards);

        wait.until(driver -> {
            List<WebElement> newCards = getAdCards();
            if (newCards.isEmpty()) return false;

            List<String> newCardsText = extractTextsFromElements(newCards);
            boolean nextEnabled = isNextButtonEnabled();

            return !newCardsText.equals(oldCardsText) || !nextEnabled;
        });
    }

    private List<String> extractTextsFromElements(List<WebElement> elements) {
        List<String> texts = new ArrayList<>();
        for (WebElement element : elements) {
            try {
                texts.add(element.getText().trim());
            } catch (StaleElementReferenceException e) {
                return new ArrayList<>();
            }
        }
        return texts;
    }

    public void waitForCurrentPageNumber(int expectedPage) {
        wait.until(driver -> {
            try {
                return getCurrentPageNumber() == expectedPage;
            } catch (Exception e) {
                return false;
            }
        });
    }

    public void waitForCardsCountAtLeast(int expectedCount) {
        wait.until(driver -> getAdCards().size() >= expectedCount);
    }
}