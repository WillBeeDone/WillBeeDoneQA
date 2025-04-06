package wbd.tests.web_tests;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.web.web_pages.HomePage;

import java.util.ArrayList;
import java.util.List;

public class PaginationTests extends TestBaseUI {
    private HomePage homePage;

    @BeforeMethod
    public void precondition() {
        homePage = new HomePage(app.driver, app.wait);
    }


    @Test
    public void testPaginationNextAndPrevious() {
        // Проверяем первую страницу
        softAssert.assertEquals(homePage.getCurrentPageNumber(), 1, "Should start on page 1");
        softAssert.assertFalse(homePage.isPreviousButtonEnabled(), "Previous button should be disabled on page 1");
        softAssert.assertEquals(homePage.getAdCards().size(), 9, "Should display 9 cards on page 1");

        // Получаем текстовое содержимое карточек с первой страницы
        List<WebElement> firstPageCards = homePage.getAdCards();
        List<String> firstCardsText = new ArrayList<>();
        for (WebElement card : firstPageCards) {
            firstCardsText.add(card.getText());
        }

        // Переходим на следующую страницу
        homePage.clickNextPage();

        // Проверяем вторую страницу
        softAssert.assertEquals(homePage.getCurrentPageNumber(), 2, "Should be on page 2 after clicking Next");
        List<WebElement> secondPageCards = homePage.getAdCards();
        softAssert.assertTrue(secondPageCards.size() <= 9, "Should display 9 or fewer cards on page 2");
        softAssert.assertTrue(homePage.isPreviousButtonEnabled(), "Previous button should be enabled on page 2");

        // Получаем текст карточек со второй страницы
        List<String> secondCardsText = new ArrayList<>();
        for (WebElement card : secondPageCards) {
            secondCardsText.add(card.getText());
        }

        // Сравниваем содержимое карточек первой и второй страниц
        softAssert.assertFalse(firstCardsText.equals(secondCardsText), "Cards on page 2 should differ from page 1");

        // Возвращаемся на первую страницу
        homePage.clickPreviousPage();
        softAssert.assertEquals(homePage.getCurrentPageNumber(), 1, "Should return to page 1");
        softAssert.assertFalse(homePage.isPreviousButtonEnabled(), "Previous button should be disabled on page 1");
        softAssert.assertEquals(homePage.getAdCards().size(), 9, "Should display 9 cards on page 1 again");

        softAssert.assertAll();
    }

    @Test
    public void testPaginationByPageNumber() {
        // Переходим на страницу 3
        homePage.goToPage(3);

        homePage.waitForCurrentPageNumber(3);
        homePage.waitForCardsCountAtLeast(9);

        // Проверяем страницу 3
        softAssert.assertEquals(homePage.getCurrentPageNumber(), 3, "Should be on page 3");
        softAssert.assertTrue(homePage.isPreviousButtonEnabled(), "Previous button should be enabled on page 3");
        softAssert.assertTrue(homePage.isNextButtonEnabled(), "Next button should be enabled on page 3");
        softAssert.assertTrue(homePage.getAdCards().size() <= 9, "Should display 9 or fewer cards on page 3");

        softAssert.assertAll();
    }

    @Test
    public void testLastPage() {
        // Идем до последней страницы
        while (homePage.isNextButtonEnabled()) {
            homePage.clickNextPage();
        }

        // Проверяем последнюю страницу
        softAssert.assertFalse(homePage.isNextButtonEnabled(), "Next button should be disabled on last page");
        softAssert.assertTrue(homePage.isPreviousButtonEnabled(), "Previous button should be enabled on last page");
        softAssert.assertTrue(homePage.getAdCards().size() <= 9, "Should display 9 or fewer cards on last page");

        softAssert.assertAll();
    }
}