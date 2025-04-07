package wbd.tests.web_tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.web.data.UserData;
import wbd.web.helpers.HelperMailTm;
import wbd.web.web_pages.ForgetPasswordPage;
import wbd.web.web_pages.HomePage;
import wbd.web.web_pages.RecoveryPasswordPage;
import java.lang.reflect.Method;
import java.io.IOException;


public class RecoveryPasswordTests extends TestBaseUI {

    RecoveryPasswordPage recoveryPage;
    private String email;
    String password = UserData.VALID_PASSWORD;
    String newPassword = "NewPass123!";
    String oldPassword = UserData.VALID_PASSWORD;

    private String confirmationLink;
    private String resetLink;

    @BeforeMethod
    public void precondition(Method method) throws IOException, InterruptedException {
        email = HelperMailTm.generateEmail();
        new HomePage(app.driver, app.wait)
                .getRegistrationPage()
                .enterEmail(email)
                .enterPassword(password)
                .confirmPassword(password)
                .checkAgreeBox()
                .submitRegistration();

        assertAlertTextAndAccept("Please, check your email");

        confirmationLink = HelperMailTm.getConfirmationLink(email)
                .replace("[", "")
                .replace("]", "")
                .trim();
        app.driver.get(confirmationLink);

    }

    @Test
    public void recoveryPasswordConfirmPositiveTest() throws IOException, InterruptedException {
        logger.info("Open Forget Password Page");
        ForgetPasswordPage forgetPasswordPage = new HomePage(app.driver, app.wait)
                .getLoginPage()
                .getForgetPasswordPage();

        logger.info("Send reset request");
        forgetPasswordPage
                .enterEmail(email)
                .enterSendButton();

        assertAlertTextAndAccept("Check your email please");
        assertRedirectedToMainPage();

        logger.info("Get reset link and open it");
        resetLink = HelperMailTm.getConfirmationLink(email)
                .replace("[", "")
                .replace("]", "")
                .trim();
        // Очистка мусорных окончаний, например "to" после UUID
        resetLink = resetLink.replaceAll("(to\\s?.*)?$", "").trim();

        logger.info("Final cleaned link: {}", resetLink);
        app.driver.get(resetLink);

        recoveryPage = new RecoveryPasswordPage(app.driver, app.wait);

        logger.info("Enter a new password, but do not click 'Save'");
        recoveryPage = new RecoveryPasswordPage(app.driver, app.wait)
                .enterNewPassword(newPassword)
                .confirmNewPassword(newPassword);

        logger.info("Click Save button");
        recoveryPage.clickSaveNewPasswordButton();

        logger.info("Check alert and redirect");
        String alertText = recoveryPage.getAlertTextAndAccept();
        logger.info("Captured alert text: " + alertText);

        // Проверка наличия alert
        softAssert.assertNotNull(alertText, "Expected alert text but got null");

        // Проверка содержимого alert
        softAssert.assertTrue(alertText.contains("Password successfully changed"),
                "Expected alert to contain: [Password successfully changed] but was: [" + alertText + "]");

        // Логируем текущий URL для отладки
        logger.info("Before waiting for redirect, current URL: " + app.driver.getCurrentUrl());

        // Проверка редиректа
        softAssert.assertTrue(recoveryPage.isRedirectedToLoginPage(),
                "There was no redirect to the login page.");

        softAssert.assertAll();

    }

    @Test
    public void recoveryPasswordCancelPositiveTest() throws IOException, InterruptedException {
        logger.info("Open Forget Password Page");
        ForgetPasswordPage forgetPasswordPage = new HomePage(app.driver, app.wait)
                .getLoginPage()
                .getForgetPasswordPage();

        logger.info("Send reset request");
        forgetPasswordPage
                .enterEmail(email)
                .enterSendButton();

        assertAlertTextAndAccept("Check your email please");
        assertRedirectedToMainPage();

        logger.info("Get reset link and open it");
        resetLink = HelperMailTm.getConfirmationLink(email)
                .replace("[", "")
                .replace("]", "")
                .trim();
        app.driver.get(resetLink);

        recoveryPage = new RecoveryPasswordPage(app.driver, app.wait);

        logger.info("Enter new password (do not save)");
        recoveryPage = new RecoveryPasswordPage(app.driver, app.wait)
                .enterNewPassword(newPassword)
                .confirmNewPassword(newPassword);

        logger.info("Click Cancel button");
        recoveryPage.clickCancelButton();

        assertRedirectedToMainPage();
        softAssert.assertAll();
    }


    @Test
    public void recoveryPasswordOldPasswordNegativeTest() throws IOException, InterruptedException {
        logger.info("Open Forget Password Page");
        ForgetPasswordPage forgetPasswordPage = new HomePage(app.driver, app.wait)
                .getLoginPage()
                .getForgetPasswordPage();

        logger.info("Send reset request");
        forgetPasswordPage
                .enterEmail(email)
                .enterSendButton();

        assertAlertTextAndAccept("Check your email please");
        assertRedirectedToMainPage();

        logger.info("Get reset link and open it");
        resetLink = HelperMailTm.getConfirmationLink(email)
                .replace("[", "")
                .replace("]", "")
                .trim();
        // Очистка мусорных окончаний, например "to" после UUID
        resetLink = resetLink.replaceAll("(to\\s?.*)?$", "").trim();

        logger.info("Final cleaned link: {}", resetLink);
        app.driver.get(resetLink);

        recoveryPage = new RecoveryPasswordPage(app.driver, app.wait);

        logger.info("Enter OLD password instead of new one");
        recoveryPage = new RecoveryPasswordPage(app.driver, app.wait)
                .enterNewPassword(oldPassword)
                .confirmNewPassword(oldPassword);

        logger.info("Click Save button");
        recoveryPage.clickSaveNewPasswordButton();

        logger.info("Check alert message and absence of redirect");
        String alertText = recoveryPage.getAlertTextAndAccept();
        logger.info("Captured alert text: " + alertText);

        // Баг: пароль успешно изменён, хотя он совпадает со старым
        if (alertText != null && alertText.toLowerCase().contains("successfully")) {
            logger.error("BUG: Old password was accepted and changed successfully — this should be disallowed!");
        }

        // Проверка, что alert НЕ содержит сообщение об успешной смене
        softAssert.assertFalse(alertText.toLowerCase().contains("successfully"),
                "BUG: Alert text should not confirm password change when reusing old password");

        // Проверка, что НЕ произошло редиректа на страницу логина
        boolean redirected = recoveryPage.isRedirectedToLoginPage();
        if (redirected) {
            logger.error("BUG: Redirected to login page even though old password was reused");
        }
        softAssert.assertFalse(redirected, "BUG: Should not redirect to login page when old password is reused");

        softAssert.assertAll();
    }
}




