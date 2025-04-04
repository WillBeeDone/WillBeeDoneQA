package wbd.web.web_pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import wbd.web.core.BasePage;

import java.util.List;

public class HeaderComponent extends BasePage {

    @FindBy(xpath = "(//select[@class='_dropdown_1sio9_1'])[1]")
    private WebElement cityDropdown;

    public HeaderComponent(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // Returns the Select object for the city dropdown.
    public Select getCitySelect() {
        return new Select(cityDropdown);
    }

    //Selects a city from the dropdown by visible text.
    public void selectCity(String city) {
        getCitySelect().selectByVisibleText(city);
    }

    // Returns the list of available city options in the dropdown.
    public List<WebElement> getCityOptions() {
        return getCitySelect().getOptions();
    }

    // Returns the text of the currently selected city.
    public String getSelectedCity() {
        return getCitySelect().getFirstSelectedOption().getText();
    }
}