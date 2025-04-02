package wbd.web.web_pages;

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
    public HomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @FindBy (xpath = "(//select[@class='_dropdown_1sio9_1'])[2]")
    WebElement categoryDropdown;

    public HomePage clickAllCategories() {
        wait.until(ExpectedConditions.elementToBeClickable(categoryDropdown)).click();
        System.out.println("The click on the All Categories is executed");
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
}
