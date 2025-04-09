package wbd.tests.web_tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.web.web_pages.HeaderComponent;
import wbd.web.web_pages.HomePage;

import java.util.List;

@Epic("HeaderComponent")
@Feature("City selection functionality")

@Listeners({AllureTestNg.class})

public class HeaderComponentTests extends TestBaseUI {

    @BeforeMethod
    public void resetPage() {
        app.driver.get("https://monkfish-app-73239.ondigitalocean.app");
        app.wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("select[data-testid='DropDownLocationHeader_HfZydgG']")));
    }

    @Test
    @TmsLink("")
    @Severity(SeverityLevel.NORMAL)
    @Story("Verify available cities in dropdown")
    @Description("Test verifies that the city dropdown contains options and 'Berlin' is present")
    public void testCityDropdownOptions() {
        HeaderComponent header = new HeaderComponent(app.driver, app.wait);

        List<WebElement> cities = header.getCityOptions();

        softAssert.assertTrue(cities.size() > 0, "Dropdown should contain cities");
        softAssert.assertEquals(cities.get(3).getText(), "Berlin");
        softAssert.assertAll();
    }

    @Test
    @TmsLink("")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Select city from dropdown")
    @Description("Test verifies that a city can be selected and the selection is applied")
    public void testSelectCity() {
        HeaderComponent header = new HeaderComponent(app.driver, app.wait);

        header.selectCity("Berlin");
        String selectedCity = header.getSelectedCity();
        softAssert.assertEquals(selectedCity, "Berlin");
        softAssert.assertAll();
    }

    @Test
    @TmsLink("")
    @Severity(SeverityLevel.NORMAL)
    @Story("Ads filtering by selected city")
    @Description("Test checks if ad cards display the selected city (Berlin) after selection")
    public void testAdCardsAfterCitySelection() {
        HeaderComponent header = new HeaderComponent(app.driver, app.wait);
        HomePage homePage = new HomePage(app.driver, app.wait);

        header.selectCity("Berlin");
        logger.info("Selected city in header: {}", header.getSelectedCity());

        List<WebElement> adCards = homePage.getAdCards();
        logger.info("Number of ad cards: {}", adCards.size());
        softAssert.assertTrue(adCards.size() > 0, "Ad cards should be present after city selection");

        for (WebElement card : adCards) {
            String cityText = homePage.getCityFromCard(card);
            logger.info("City text in card: '{}'", cityText);
            softAssert.assertEquals(cityText, "Berlin", "Each ad card should display 'Berlin'");
        }
        softAssert.assertAll();
    }
}
