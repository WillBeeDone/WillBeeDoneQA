package wbd.tests.web_tests;

import org.openqa.selenium.By;
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
        // init pages
        HomePage homePage = new HomePage(app.driver, app.wait);
        HeaderComponent header = new HeaderComponent(app.driver);

        // performing search
        homePage.searchFor("Plumber");

        //waiting till cards contain "Plumber"
        app.wait.until(driver -> {
            try {
                List<WebElement> cards = driver.findElements(By.cssSelector("._offerContainer_1h601_5"));
                if (cards.isEmpty()) {
                    return false;
                }
                WebElement firstCard = cards.get(0);
                String categoryText = firstCard.findElement(By.cssSelector("._category_1h601_87")).getText();
                System.out.println("Checking category: " + categoryText);
                return categoryText.equals("Plumber");
            } catch (Exception e) {
                System.out.println("Exception in wait: " + e.getMessage());
                return false; // Продолжаем ждать, если исключение
            }
        });


        // checking cards
        List<WebElement> adCards = homePage.getAdCards();
        Assert.assertTrue(adCards.size() > 0, "Ad cards should be present after search");

        // checking cards for "Plumber" category
        for (WebElement card : adCards) {
            String categoryText = homePage.getCategoryFromCard(card);
            Assert.assertEquals(categoryText, "Plumber",
                    "Ad card category should be 'Plumber', but found: " + categoryText);
        }
    }
}