package wbd.web.web_pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import wbd.web.core.BasePage;

import java.util.List;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    private WebElement getSearchField() {
        return driver.findElement(By.xpath("//input[@placeholder='Enter keywords to search']"));
    }

    private WebElement getSearchButton() {
        return driver.findElement(By.xpath("//button[contains(text(),'Go →')]"));
    }

    public void enterSearchKeyword(String keyword) {
        getSearchField().clear();
        getSearchField().sendKeys(keyword);
    }

    public void clickSearchButton() {
        getSearchButton().click();
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
}