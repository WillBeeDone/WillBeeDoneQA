package wbd.tests.web_tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.web.web_pages.HomePage;
import wbd.web.web_pages.SortingComponent;

import java.util.ArrayList;
import java.util.List;

public class SortingTests extends TestBaseUI {
    private HomePage homePage;
    private SortingComponent sortingComponent;

    @BeforeMethod
    public void preconditions() {
        homePage = new HomePage(app.driver, app.wait);
        sortingComponent = new SortingComponent(app.driver, app.wait);
    }

    @Test
    public void testDefaultSortOrderIsAscending() {
        // Получаем карточки на странице (по умолчанию они должны быть отсортированы по возрастанию цены)
        List<WebElement> cards = homePage.getAdCards();
        List<Integer> prices = new ArrayList<>();
        for (WebElement card : cards) {
            String priceText = card.findElement(By.cssSelector("._euro_jqbzu_166")).getText();
            int price = parsePrice(priceText);
            prices.add(price);
        }

        // Проверяем, что цены идут по возрастанию
        for (int i = 1; i < prices.size(); i++) {
            softAssert.assertTrue(prices.get(i - 1) <= prices.get(i),
                    "Ожидается, что цена " + prices.get(i - 1) + " будет меньше или равна " + prices.get(i));
        }
        softAssert.assertAll();
    }

    @Test
    public void testSortOrderAfterClickingToggleIsDescending() {
        // 1. Фиксируем изначальный возрастающий порядок
        List<WebElement> initialCards = homePage.getAdCards();
        List<Integer> initialPrices = new ArrayList<>();
        for (WebElement card : initialCards) {
            String priceText = card.findElement(By.cssSelector("._euro_jqbzu_166")).getText();
            int price = parsePrice(priceText);
            initialPrices.add(price);
        }
        System.out.println("Цены до сортировки: " + initialPrices);
        for (int i = 1; i < initialPrices.size(); i++) {
            softAssert.assertTrue(initialPrices.get(i - 1) <= initialPrices.get(i),
                    "Изначально ожидается возрастающий порядок: " + initialPrices.get(i - 1) + " <= " + initialPrices.get(i));
        }

        // 2. Нажимаем на кнопку сортировки и ждем изменения
        sortingComponent.clickSortToggle();
        sortingComponent.waitForSortUpdate(initialPrices); // Передаем изначальные цены

        // 3. Проверяем убывающий порядок
        List<WebElement> cards = homePage.getAdCards();
        List<Integer> prices = new ArrayList<>();
        for (WebElement card : cards) {
            String priceText = card.findElement(By.cssSelector("._euro_jqbzu_166")).getText();
            int price = parsePrice(priceText);
            prices.add(price);
        }
        System.out.println("Цены после сортировки: " + prices);
        for (int i = 1; i < prices.size(); i++) {
            softAssert.assertTrue(prices.get(i - 1) >= prices.get(i),
                    "Ожидается убывающий порядок: " + prices.get(i - 1) + " >= " + prices.get(i));
        }
        softAssert.assertAll();
    }

    private int parsePrice(String priceText) {
        // Убираем знак валюты и пробелы (если они есть) и оставляем только число
        String cleaned = priceText.replace(" €", "").trim();
        return Integer.parseInt(cleaned);
    }
}
