package wbd.web.web_pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import wbd.web.core.BasePage;

import java.util.ArrayList;
import java.util.List;

public class OffersPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(OffersPage.class);

    public OffersPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @FindBy(xpath = "(//select[@class='_dropdown_1sio9_1'])[2]")
    WebElement categoryDropdown;

    public OffersPage scrollAfterCategorySelection() {
        scrollTo(800);
        logger.info("Scrolled down to load offer cards");
        return this;
    }


    @FindBy(xpath = "//p[contains(@class,'_category') and text()]")
    List<WebElement> offerCategoryTitles;
    public OffersPage verifySelectedCategory(String categoryName) {
        boolean found = false;

        for (WebElement categoryTitle : offerCategoryTitles) {
            String actual = categoryTitle.getText().trim();
            logger.info("🔍 Category found in offer card: {}", actual);

            if (actual.equalsIgnoreCase(categoryName)) {
                found = true;
                break;
            }
        }

        Assert.assertTrue(found, "❌ Category '" + categoryName + "' was not found in offer cards");
        logger.info("✅ Category '{}' successfully found among offers", categoryName);
        return this;
    }

    @FindBy(xpath = "//div[contains(@class, '_offerCardImageContainer')]")
    List<WebElement> offerCategoryTexts;

    public void verifyOfferText(String categoryName) {
        boolean found = false;

        for (WebElement webElement : offerCategoryTexts) {
            if (webElement.getText().equalsIgnoreCase(categoryName)) {
                shouldHaveText(webElement, categoryName, 5000);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new AssertionError("Offer with category'" + categoryName + "' not found");
        }

        logger.info("Category was found in the offer card: {}", categoryName);
    }

    public List<String> getAllOfferTexts() {
        List<String> texts = new ArrayList<>();

        for (WebElement element : offerCategoryTexts) {
            texts.add(element.getText().trim());
        }

        logger.info("Categories from cards are collected: {}", texts);
        return texts;
    }
}
