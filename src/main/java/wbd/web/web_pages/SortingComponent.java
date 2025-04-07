package wbd.web.web_pages;

import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import wbd.web.core.BasePage;

import java.util.ArrayList;
import java.util.List;

public class SortingComponent extends BasePage {
    @Getter
    private HomePage homePage;

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
        System.out.println("Изначальные цены в waitForSortUpdate: " + initialPrices);

        wait.until(driver -> {
            List<Integer> newPrices = extractPrices();
            if (newPrices.isEmpty()) {
                System.out.println("Новые цены не найдены!");
                return false;
            }
            System.out.println("Цены после сортировки в waitForSortUpdate: " + newPrices);
            boolean isDifferent = !newPrices.equals(initialPrices);
            System.out.println("Цены изменились? " + isDifferent);
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
                System.out.println("Ошибка извлечения цены: " + e.getMessage());
            }
        }
        return prices;
    }
}