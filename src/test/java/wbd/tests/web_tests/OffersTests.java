package wbd.tests.web_tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.utils.DataProviders;
import wbd.web.web_pages.HomePage;
import wbd.web.web_pages.OffersPage;

import java.util.List;
import java.util.NoSuchElementException;

public class OffersTests extends TestBaseUI {


    @Test(groups = "Positive")
    public void testSelectCategory_PetCarePositive() {
        String categoryName = "Pet Care";
        new HomePage(app.driver, app.wait)
                .clickAllCategories()
                .selectCategory(categoryName)
                .scrollAfterCategorySelection()
                .verifySelectedCategory(categoryName)
                .verifyOfferText(categoryName);        ;

    }

    @Test(dataProvider = "categories", dataProviderClass = DataProviders.class, groups = "Positive")
    public void testCategoryNavigationPositive(String categoryName) {
        new HomePage(app.driver, app.wait)
                .clickAllCategories()
                .selectCategory(categoryName)
                .scrollAfterCategorySelection()
                .verifySelectedCategory(categoryName)
                .verifyOfferText(categoryName);       ;

    }

    @Test(groups = "Negative")
    public void testSelectInvalidCategoryNegative() {
        String categoryName = "Nonexistent Category";

        try {
            new HomePage(app.driver, app.wait)
                    .clickAllCategories()
                    .selectCategory(categoryName);

            Assert.fail("It was expected that the category" + categoryName +"' would not be found, but the exception did not occur");
        } catch (NoSuchElementException e) {
            System.out.println("Expected behavior: category not found — " + e.getMessage());
        }
    }

    @Test(groups = "Negative")
    public void testSelectEmptyCategoryNegative() {
        String categoryName = "";

        try {
            new HomePage(app.driver, app.wait)
                    .clickAllCategories()
                    .selectCategory(categoryName);

            Assert.fail("Exclusion was expected when choosing an empty category");
        } catch (NoSuchElementException e) {
            System.out.println("An empty category is not chosen, as expected");
        }
    }

    @Test(groups = "Negative")
    public void testFakeCategoryInOffersNegative() {
        String realCategory = "Pet Care";
        String fakeCategory = "Fake Category";

        try {
            new HomePage(app.driver, app.wait)
                    .clickAllCategories()
                    .selectCategory(realCategory)
                    .scrollAfterCategorySelection()
                    .verifyOfferText(fakeCategory);

            Assert.fail("It was expected that there will be no category in offers '" + fakeCategory + "'");
        } catch (AssertionError e) {
            System.out.println("Expected behavior: Offer with a '" + fakeCategory + "' was not found");
        }
    }

    @Test(groups = "Negative")
    public void testMismatchBetweenDropdownAndCardsNegative() {
        String realCategory = "Pet Care";
        String wrongCategory = "Beauty & Care";

        new HomePage(app.driver, app.wait)
                .clickAllCategories()
                .selectCategory(realCategory)
                .scrollAfterCategorySelection()
                .verifySelectedCategory(realCategory);

        List<String> cardTexts = new OffersPage(app.driver, app.wait)
                .getAllOfferTexts();
        for (String text : cardTexts) {
            Assert.assertFalse(text.contains(wrongCategory), "Unexpected category '" + wrongCategory + "' found in offer card: " + text);
        }

        logger.info("✅ Verified that '{}' offers do not contain category '{}'", realCategory, wrongCategory);
    }

}
