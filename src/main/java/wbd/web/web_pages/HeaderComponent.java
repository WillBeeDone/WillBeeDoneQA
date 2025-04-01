package wbd.web.web_pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class HeaderComponent {
    private WebDriver driver;

    public HeaderComponent(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getCityDropdown() {
        return driver.findElement(By.xpath("(//select[@class='_dropdown_1sio9_1'])[1]"));
    }

    public void selectCity(String city) {
        Select dropdown = new Select(getCityDropdown());
        dropdown.selectByVisibleText(city);
    }

    public List<WebElement> getCityOptions() {
        Select dropdown = new Select(getCityDropdown());
        return dropdown.getOptions();
    }
}