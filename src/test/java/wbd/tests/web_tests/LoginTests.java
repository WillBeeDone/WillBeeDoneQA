package wbd.tests.web_tests;

import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.utils.DataProviders;
import wbd.web.data.UserData;
import wbd.web.web_pages.LoginPage;

public class LoginTests extends TestBaseUI {

    @Test
    public void loginPositiveTest() {
        LoginPage loginPage = new LoginPage(app.driver, app.wait);
        loginPage.openLoginPage();
        loginPage.enterEmail(UserData.VALID_EMAIL);
        loginPage.enterPassword("Password!123");
        loginPage.submitLogin();

        softAssert.assertTrue(loginPage.isSignOutButtonDisplayed(), "Sign Out button is not displayed!");
        softAssert.assertAll();
    }

    @Test(dataProvider = "invalidPasswords", dataProviderClass = DataProviders.class)
    public void loginWrongPasswordNegativeTest(String invalidPassword, String errorDescription) {
        LoginPage loginPage = new LoginPage(app.driver, app.wait);
        loginPage.openLoginPage();
        loginPage.enterEmail(UserData.VALID_EMAIL);
        loginPage.enterPassword(invalidPassword);
        loginPage.submitLogin();

        softAssert.assertEquals(loginPage.getErrorMessageText(), "Invalid login or password.",
                "Error message mismatch for password: " + invalidPassword + " (" + errorDescription + ")");
        softAssert.assertAll();
    }
}
