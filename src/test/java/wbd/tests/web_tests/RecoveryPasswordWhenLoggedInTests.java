package wbd.tests.web_tests;

import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.utils.DataProviders;
import wbd.web.data.UserData;
import wbd.web.web_pages.ForgetPasswordPage;
import wbd.web.web_pages.LoginPage;
import wbd.web.web_pages.RecoveryPasswordPage;




public class RecoveryPasswordWhenLoggedInTests extends TestBaseUI {

    RecoveryPasswordPage recoveryPage;

    @Test(dataProvider = "invalidEmails", dataProviderClass  = DataProviders.class)
    public void recoveryInvalidEmailNegativeTest(String invalidEmail, String reason) {
        logger.info("Trying to recover password with invalid email: {} ({})", invalidEmail, reason);

        ForgetPasswordPage forgetPasswordPage = new LoginPage(app.driver, app.wait)
                .openLoginPage()
                .getForgetPasswordPage();

        forgetPasswordPage.enterEmail(invalidEmail)
               .enterSendButton();

        softAssert.assertTrue(
                forgetPasswordPage.isValidationErrorDisplayed("Incorrect email") ||
                        forgetPasswordPage.isValidationErrorDisplayed("Email is required"),
                "Expected validation error not shown for email: " + invalidEmail
        );

        softAssert.assertAll();
   }

    @Test(dataProvider = "invalidPasswords", dataProviderClass = DataProviders.class)
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
