package wbd.tests.web_tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.utils.DataProviders;
import wbd.web.data.UserData;
import wbd.web.web_pages.LoginPage;

@Epic("Authorization")
@Feature("Login functionality")
@Listeners({AllureTestNg.class})
public class LoginTests extends TestBaseUI {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Story("User authentication")
    @Description("Verify that user can login with valid email and password")
    @TmsLink("")
    public void loginPositiveTest() {
        LoginPage loginPage = new LoginPage(app.driver, app.wait);
        loginPage.openLoginPage();
        loginPage.enterEmail(UserData.VALID_EMAIL);
        loginPage.enterPassword(UserData.VALID_PASSWORD);
        loginPage.submitLogin();
        loginPage.clickHamburgerMenu();

        softAssert.assertTrue(loginPage.isSignOutButtonDisplayed(), "Sign Out button is not displayed!");
        softAssert.assertAll();
    }

    @Test(dataProvider = "invalidPasswords", dataProviderClass = DataProviders.class)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Negative login scenarios")
    @Description("Verify system behavior when using invalid passwords")
    @TmsLink("")
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
