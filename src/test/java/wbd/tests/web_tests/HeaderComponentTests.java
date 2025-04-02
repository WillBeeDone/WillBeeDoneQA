package wbd.tests.web_tests;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.web.web_pages.HeaderComponent;
import wbd.web.web_pages.HomePage;

import java.util.List;

public class HeaderComponentTests extends TestBaseUI {
    @Test
    public void testCityDropdownOptions() {
        HeaderComponent header = new HeaderComponent(app.driver);

        List<WebElement> cities = header.getCityOptions();
        Assert.assertTrue(cities.size() > 0, "Dropdown should contain cities");
        Assert.assertEquals(cities.get(3).getText(), "Berlin");
    }

    @Test
    public void testSelectCity() {
        HeaderComponent header = new HeaderComponent(app.driver);

        header.selectCity("Berlin");
        String selectedCity = new Select(header.getCityDropdown()).getFirstSelectedOption().getText();
        Assert.assertEquals(selectedCity, "Berlin");
    }

    @Test
    public void testAdCardsAfterCitySelection() {
        HeaderComponent header = new HeaderComponent(app.driver);
        HomePage homePage = new HomePage(app.driver,app.wait);
        // Выбираем город
        header.selectCity("Berlin");

        // Получаем все карточки
        List<WebElement> adCards = homePage.getAdCards();
        Assert.assertTrue(adCards.size() > 0, "Ad cards should be present after city selection");

        // Проверяем, что каждая карточка содержит "Berlin"
        for (WebElement card : adCards) {
            String cityText = homePage.getCityFromCard(card);
            Assert.assertEquals(cityText, "Berlin", "Each ad card should display 'Berlin'");
        }
    }
}