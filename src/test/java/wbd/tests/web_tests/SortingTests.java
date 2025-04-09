package wbd.tests.web_tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.web.web_pages.HomePage;
import wbd.web.web_pages.SortingComponent;

import java.util.ArrayList;
import java.util.List;

@Epic("Content Display")
@Feature("Sorting Functionality")
@Listeners({AllureTestNg.class})

public class SortingTests extends TestBaseUI {
    private HomePage homePage;
    private SortingComponent sortingComponent;

    @BeforeMethod
    public void preconditions() {
        homePage = new HomePage(app.driver, app.wait);
        sortingComponent = new SortingComponent(app.driver, app.wait);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Default Sorting")
    @Description("Test verifies that ads are sorted in ascending price order by default")
    @TmsLink("")
    public void testDefaultSortOrderIsAscending() {
        // Get cards with ascending prices by default
        List<WebElement> cards = homePage.getAdCards();
        List<Integer> prices = new ArrayList<>();
        for (WebElement card : cards) {
            String priceText = card.findElement(By.cssSelector("._euro_jqbzu_166")).getText();
            int price = parsePrice(priceText);
            prices.add(price);
        }

        // Assert that prices are in ascending order
        for (int i = 1; i < prices.size(); i++) {
            softAssert.assertTrue(prices.get(i - 1) <= prices.get(i),
                    "Expected ascending order: " + prices.get(i - 1) + " <= " + prices.get(i));
        }

        softAssert.assertAll();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Sorting Toggle")
    @Description("Test verifies that clicking sort toggle changes order to descending by price")
    @TmsLink("")
    public void testSortOrderAfterClickingToggleIsDescending() {
        // 1. Capture initial ascending order
        List<WebElement> initialCards = homePage.getAdCards();
        List<Integer> initialPrices = new ArrayList<>();
        for (WebElement card : initialCards) {
            String priceText = card.findElement(By.cssSelector("._euro_jqbzu_166")).getText();
            int price = parsePrice(priceText);
            initialPrices.add(price);
        }
        logger.info("Prices before sorting: {}", initialPrices);

        for (int i = 1; i < initialPrices.size(); i++) {
            softAssert.assertTrue(initialPrices.get(i - 1) <= initialPrices.get(i),
                    "Expected initial ascending order: " + initialPrices.get(i - 1) + " <= " + initialPrices.get(i));
        }

        // 2. Click sort toggle and wait for update
        sortingComponent.clickSortToggle();
        sortingComponent.waitForSortUpdate(initialPrices); // Pass original prices

        // 3. Verify descending order
        List<WebElement> cards = homePage.getAdCards();
        List<Integer> prices = new ArrayList<>();
        for (WebElement card : cards) {
            String priceText = card.findElement(By.cssSelector("._euro_jqbzu_166")).getText();
            int price = parsePrice(priceText);
            prices.add(price);
        }
        logger.info("Prices after sorting: {}", prices);

        for (int i = 1; i < prices.size(); i++) {
            softAssert.assertTrue(prices.get(i - 1) >= prices.get(i),
                    "Expected descending order: " + prices.get(i - 1) + " >= " + prices.get(i));
        }

        softAssert.assertAll();
    }

    private int parsePrice(String priceText) {
        // Remove all non-digit characters to extract a clean integer price
        try {
            String cleaned = priceText.replaceAll("[^\\d]", "");
            return Integer.parseInt(cleaned);
        } catch (Exception e) {
            logger.error("Failed to parse price from text: '{}'", priceText, e);
            return 0;
        }
    }
}
