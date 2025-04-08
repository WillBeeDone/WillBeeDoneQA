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


    @FindBy(xpath = "//button[@type='button' and text()='Sign Up']")
    WebElement signUpButton;
  
    @FindBy(xpath = "//a[contains(text(),'Sign In')]")
    WebElement signInLink;
  
    @FindBy(xpath = "(//select[@class='_dropdown_pua4g_1'])[2]")
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
        wait.until(ExpectedConditions.elementToBeClickable(signInLink));
        signInLink.click();
        return new LoginPage(driver, wait);
    }


    public RegistrationPage getRegistrationPage() {
        wait.until(ExpectedConditions.elementToBeClickable(signUpButton)).click();
        return new RegistrationPage(driver, wait);
    }

    public HomePage clickAllCategories() {
        scrollToElement(categoryDropdown);
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
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div._firstPartOfferCard_jqbzu_53")));
    }

    public List<WebElement> getAdCards() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("div._firstPartOfferCard_jqbzu_53")));
        return driver.findElements(By.cssSelector("div._firstPartOfferCard_jqbzu_53"));
    }


    public String getCityFromCard(WebElement card) {
        List<WebElement> cityElements = card.findElements(By.cssSelector("._location_jqbzu_143"));
        return cityElements.isEmpty() ? "Город не найден" : cityElements.get(0).getText();
    }

    public String getCategoryFromCard(WebElement card) {
        return card.findElement(By.cssSelector("._category_jbegn_130")).getText();
    }

    public void waitForAdCardsWithCategory(String expectedCategory) {
        wait.until(driver -> {
            // Проверяем наличие iframe и переключаемся, если нужно
            List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            if (!iframes.isEmpty()) {
                driver.switchTo().frame(iframes.get(0)); // Переключаемся на первый iframe (уточните селектор)
            }

            List<WebElement> cards = driver.findElements(By.cssSelector("div._firstPartOfferCard_jqbzu_53"));
            if (cards.isEmpty()) {
                driver.switchTo().defaultContent(); // Возвращаемся в основной контекст
                return false;
            }

            for (WebElement card : cards) {
                try {
                    String actualCategory = card.findElement(By.cssSelector("._category_jbegn_130")).getText();
                    if (!expectedCategory.equals(actualCategory)) {
                        driver.switchTo().defaultContent();
                        return false;
                    }
                } catch (Exception e) {
                    driver.switchTo().defaultContent();
                    return false;
                }
            }
            driver.switchTo().defaultContent();
            return true;
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

    public void waitForCardsToUpdate() {
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

