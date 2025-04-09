package wbd.tests.web_tests;

import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.utils.DataProviders;
import wbd.web.data.UserData;
import wbd.web.web_pages.ForgotPasswordPage;
import wbd.web.web_pages.LoginPage;
import wbd.web.web_pages.RecoveryPasswordPage;

@Epic("Authorization")
@Feature("Password Recovery (Logged-in Scenarios)")
@Listeners({AllureTestNg.class})
public class RecoveryPasswordWhenLoggedInTests extends TestBaseUI {

    RecoveryPasswordPage recoveryPage;

    @Test(dataProvider = "invalidEmails", dataProviderClass = DataProviders.class, groups = "Negative")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Invalid email in recovery")
    @Description("Verifies that recovery form displays validation errors for incorrect or empty emails")
    @TmsLink("")
    public void recoveryInvalidEmailNegativeTest(String invalidEmail, String reason) {
        logger.info("Trying to recover password with invalid email: {} ({})", invalidEmail, reason);

        ForgotPasswordPage forgotPasswordPage = new LoginPage(app.driver, app.wait)
                .openLoginPage()
                .getForgotPasswordPage();

        forgotPasswordPage.enterEmail(invalidEmail)
               .enterSendButton();

        softAssert.assertTrue(
                forgotPasswordPage.isValidationErrorDisplayed("Incorrect email") ||
                        forgotPasswordPage.isValidationErrorDisplayed("Email is required"),
                "Expected validation error not shown for email: " + invalidEmail
        );

        softAssert.assertAll();
   }

    @Test(dataProvider = "invalidPasswords", dataProviderClass = DataProviders.class, groups = "Negative")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Invalid password format")
    @Description("Checks that password recovery form shows validation errors for passwords that don't meet requirements")
    @TmsLink("")
    public void recoveryInvalidPasswordNegativeTest(String invalidPassword, String reason)  {
        logger.info("Test recovery with invalid password: {} ({})", invalidPassword, reason);

        recoveryPage = new RecoveryPasswordPage(app.driver, app.wait)
                .openRecoveryPage();

        recoveryPage
                .enterNewPassword(invalidPassword)
                .confirmNewPassword(invalidPassword)
                .clickSaveNewPasswordButton();

        // Проверяем наличие сообщения об ошибке валидации
        boolean isValidationErrorVisible = recoveryPage.isValidationErrorDisplayed(UserData.PASSWORD_VALIDATION_MESSAGE);

        softAssert.assertTrue(isValidationErrorVisible,
                "Expected validation error to be displayed for: " + reason);

        softAssert.assertAll();
    }
}
