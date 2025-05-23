package wbd.tests.web_tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.utils.DataProviders;
import wbd.web.web_pages.HomePage;
import wbd.web.web_pages.OffersPage;

import java.util.List;

@Epic("Offers Display")
@Feature("Category Filtering")
@Listeners({AllureTestNg.class})
public class OffersTests extends TestBaseUI {


    @Test(groups = "Positive")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Select specific category")
    @Description("Verifies that the 'Pet Care' category can be selected and is displayed correctly")
    @TmsLink("")
    public void testSelectCategory_PetCarePositive() {
        String categoryName = "Pet Care";
        new HomePage(app.driver, app.wait)
                .clickAllCategories()
                .selectCategory(categoryName)
                .scrollAfterCategorySelection()
                .verifySelectedCategory(categoryName);       ;

    }

    @Test(dataProvider = "categories", dataProviderClass = DataProviders.class, groups = "Positive")
    @Severity(SeverityLevel.NORMAL)
    @Story("Category navigation")
    @Description("Verifies category selection using a data provider")
    @TmsLink("")
    public void testCategoryNavigationPositive(String categoryName) {
        new HomePage(app.driver, app.wait)
                .clickAllCategories()
                .selectCategory(categoryName)
                .scrollAfterCategorySelection()
                .verifySelectedCategory(categoryName);
    }

    @Test(groups = "Negative")
    @Severity(SeverityLevel.NORMAL)
    @Story("Invalid category selection")
    @Description("Attempts to verify a fake category is not present in offers")
    @TmsLink("")
    public void testSelectInvalidCategoryNegative() {
        String realCategory = "Pet Care";
        String fakeCategory = "Nonexistent Category";

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
    @Severity(SeverityLevel.NORMAL)
    @Story("Empty category name")
    @Description("Attempts to verify that no empty category is shown in offers")
    @TmsLink("")
    public void testSelectEmptyCategoryNegative() {
        String realCategory = "Pet Care";
        String emptyCategory = "";

        try {
            new HomePage(app.driver, app.wait)
                    .clickAllCategories()
                    .selectCategory(realCategory)
                    .scrollAfterCategorySelection()
                    .verifyOfferText(emptyCategory);

            Assert.fail("It was expected that no empty category would be shown in offers");
        } catch (AssertionError e) {
            System.out.println("Expected behavior: No offers contain an empty category");
        }
    }


    @Test(groups = "Negative")
    @Severity(SeverityLevel.NORMAL)
    @Story("Fake category in cards")
    @Description("Checks that fake category is not found among offer cards after selecting a real category")
    @TmsLink("")
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
    @Severity(SeverityLevel.CRITICAL)
    @Story("Mismatch between dropdown and cards")
    @Description("Ensures that selected category does not mismatch with cards’ content")
    @TmsLink("")
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

        logger.info("Verified that '{}' offers do not contain category '{}'", realCategory, wrongCategory);
    }
}
