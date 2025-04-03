package wbd.tests.web_tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.web.web_pages.HeaderComponent;
import wbd.web.web_pages.HomePage;

import java.util.List;

import org.openqa.selenium.WebElement;

public class HomePageTests extends TestBaseUI {
    @Test
    public void testSearchByKeyword() {
        // Инициализация страниц
        HomePage homePage = new HomePage(app.driver, app.wait);
        HeaderComponent header = new HeaderComponent(app.driver);

        // Выполнение поиска
        homePage.searchFor("Plumber");

        // Ожидание загрузки карточек с категорией "Plumber"
        homePage.waitForAdCardsWithCategory("Plumber");

        // Проверка карточек
        List<WebElement> adCards = homePage.getAdCards();
        Assert.assertTrue(adCards.size() > 0, "Ad cards should be present after search");

        // Проверка категории каждой карточки
        for (WebElement card : adCards) {
            String categoryText = homePage.getCategoryFromCard(card);
            Assert.assertEquals(categoryText, "Plumber",
                    "Ad card category should be 'Plumber', but found: " + categoryText);
        }
    }
}