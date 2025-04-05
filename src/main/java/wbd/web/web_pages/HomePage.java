package wbd.web.web_pages;

import org.openqa.selenium.By;
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
        return driver.findElements(By.cssSelector("._offerContainer_1h601_5"));
    }

    public String getCityFromCard(WebElement card) {
        return card.findElement(By.cssSelector("._location_1h601_100")).getText();
    }

    public String getCategoryFromCard(WebElement card) {
        return card.findElement(By.cssSelector("._category_1h601_87")).getText();
    }

    // Новый метод для ожидания загрузки карточек с нужной категорией
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

}
