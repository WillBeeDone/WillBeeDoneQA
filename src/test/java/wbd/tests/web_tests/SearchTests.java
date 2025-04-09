package wbd.tests.web_tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.web.web_pages.HomePage;
import java.util.List;

@Epic("Search Functionality")
@Feature("Basic Search")
@Listeners({AllureTestNg.class})

public class SearchTests extends TestBaseUI {

    @Test(groups = "Positive")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Search by keyword")
    @Description("Verify that search returns relevant results for the keyword 'Plumber'")
    @TmsLink("")
    public void testSearchByKeywordPositive() {
        HomePage homePage = new HomePage(app.driver, app.wait);

        // Выполняем поиск
        homePage.searchFor("Plumber");

        // Получаем актуальный список карточек после поиска
        List<WebElement> adCards = homePage.getAdCards();

        // Проверяем, что карточек больше 0
        softAssert.assertTrue(adCards.size() > 0, "Ad cards should be present after search");

        // Проверяем категорию каждой карточки
        for (WebElement card : adCards) {
            try {
                String categoryText = homePage.getCategoryFromCard(card);
                logger.info("Category: {}", categoryText);
                softAssert.assertEquals(categoryText, "Plumber",
                        "Ad card category should be 'Plumber', but found: " + categoryText);
            } catch (Exception e) {
                logger.error("Error when receiving the category: {}", e.getMessage(), e);
            }
        }

        softAssert.assertAll();
    }
}