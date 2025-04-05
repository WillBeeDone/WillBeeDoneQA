package wbd.tests.web_tests;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.web.web_pages.HeaderComponent;
import wbd.web.web_pages.HomePage;

import java.util.List;

public class HeaderComponentTests extends TestBaseUI {


    @Test
    public void testCityDropdownOptions() {

        HeaderComponent header = new HeaderComponent(app.driver,app.wait);
        
        List<WebElement> cities = header.getCityOptions();

        softAssert.assertTrue(cities.size() > 0, "Dropdown should contain cities");
        softAssert.assertEquals(cities.get(3).getText(), "Berlin");
        softAssert.assertAll();
    }

    @Test
    public void testSelectCity() {
        HeaderComponent header = new HeaderComponent(app.driver,app.wait);

        header.selectCity("Berlin");
        String selectedCity = header.getSelectedCity();
        softAssert.assertEquals(selectedCity, "Berlin");
        softAssert.assertAll();
    }

    @Test
    public void testAdCardsAfterCitySelection() {
        HeaderComponent header = new HeaderComponent(app.driver,app.wait);
        HomePage homePage = new HomePage(app.driver, app.wait);

        // Выбор города
        header.selectCity("Berlin");

        // Получение всех карточек объявлений
        List<WebElement> adCards = homePage.getAdCards();
        softAssert.assertTrue(adCards.size() > 0, "Ad cards should be present after city selection");

        // Проверка, что каждая карточка содержит "Berlin"
        for (WebElement card : adCards) {
            String cityText = homePage.getCityFromCard(card);
            softAssert.assertEquals(cityText, "Berlin", "Each ad card should display 'Berlin'");
        }
        softAssert.assertAll();
    }
}
