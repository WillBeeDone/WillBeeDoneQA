package wbd.tests.web_tests;

import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.utils.DataProviders;
import wbd.web.web_pages.HomePage;

public class CategorySelectionTests extends TestBaseUI {


    @Test
    public void testSelectCategory_PetCare() {
        String categoryName = "Pet Care";
        new HomePage(app.driver, app.wait)
                .clickAllCategories()
                .selectCategory(categoryName)
                .scrollAfterCategorySelection()
        ;
    }

    @Test(dataProvider = "categories", dataProviderClass = DataProviders.class)
    public void testCategoryNavigation(String categoryName) {
        new HomePage(app.driver, app.wait)
                .clickAllCategories()
                .selectCategory(categoryName)
                .scrollAfterCategorySelection()
        ;
    }
}
