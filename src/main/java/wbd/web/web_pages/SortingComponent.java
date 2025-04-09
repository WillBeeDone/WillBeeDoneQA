package wbd.web.web_pages;

import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wbd.web.core.BasePage;

import java.util.ArrayList;
import java.util.List;

public class SortingComponent extends BasePage {
    @Getter
    private HomePage homePage;

    private static final Logger logger = LoggerFactory.getLogger(SortingComponent.class);

    @FindBy(css = "[data-testid='MyButtonHomePageSort_JnHb']")
    private WebElement sortButton;

    public SortingComponent(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        this.homePage = new HomePage(driver, wait);
    }

    public void clickSortToggle() {
        click(sortButton, 0);
    }

    public void waitForSortUpdate(List<Integer> initialPrices) {
        logger.info("Initial prices in waitForSortUpdate: {}", initialPrices);

        wait.until(driver -> {
            List<Integer> newPrices = extractPrices();
            if (newPrices.isEmpty()) {
                logger.warn("New prices not found!");
                return false;
            }
            logger.info("Prices after sort in waitForSortUpdate: {}", newPrices);
            boolean isDifferent = !newPrices.equals(initialPrices);
            logger.info("Have prices changed? {}", isDifferent);
            return isDifferent;
        });
    }

    private List<Integer> extractPrices() {
        List<Integer> prices = new ArrayList<>();
        List<WebElement> cards = getHomePage().getAdCards();
        for (WebElement card : cards) {
            try {
                WebElement priceElement = card.findElement(By.cssSelector("._euro_jqbzu_166"));
                String priceText = priceElement.getText().replaceAll("[^0-9]", "").trim();
                if (!priceText.isEmpty()) {
                    prices.add(Integer.parseInt(priceText));
                }
            } catch (Exception e) {
                logger.error("Failed to extract price from card: {}", e.getMessage());
            }
        }
        return prices;
    }
    public static int parsePrice(String priceText) {
        try {
            String cleaned = priceText.replaceAll("[^\\d]", "");
            return Integer.parseInt(cleaned);
        } catch (Exception e) {
            logger.error("Failed to parse price from text: '{}'", priceText, e);
            return 0;
        }
    }

}
