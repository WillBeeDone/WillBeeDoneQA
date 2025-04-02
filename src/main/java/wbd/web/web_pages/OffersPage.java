package wbd.web.web_pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import wbd.web.core.BasePage;

import java.util.List;

public class OffersPage extends BasePage {

    public OffersPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @FindBy(xpath = "(//select[@class='_dropdown_1sio9_1'])[2]")
    WebElement categoryDropdown;


    public OffersPage scrollAfterCategorySelection() {
        new BasePage(driver, wait).scrollTo(800);
        return this;
    }

}
