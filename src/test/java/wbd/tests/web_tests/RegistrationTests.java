package wbd.tests.web_tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.core.TestBaseUI;
import wbd.web.data.UserData;
import wbd.web.web_pages.RegistrationPage;

import java.io.IOException;

public class RegistrationTests extends TestBaseUI {
    private RegistrationPage registrationPage;

    // инициализация страницы регистрации перед выполнением тестов
    @BeforeMethod
    public void precondition() {
        registrationPage = new RegistrationPage(app.driver, app.wait);
    }

    // позитивный тест регистрации нового пользователя
    @Test
    public void Registration_PositiveTest() {
        SoftAssert softAssert = new SoftAssert();
        String randomEmail = "test" + System.currentTimeMillis() + "@gmail.com";

        registrationPage.openRegistrationPage(); // открываем страницу регистрации
        registrationPage.enterEmail(randomEmail); // вводим email
        registrationPage.enterPassword(UserData.VALID_PASSWORD); // вводим пароль
        registrationPage.confirmPassword(UserData.VALID_PASSWORD); // подтверждаем пароль
        registrationPage.checkAgreeBox(); // ставим галочку согласия
        registrationPage.submitRegistration(); // отправляем форму регистрации

        // проверяем появление алерта и его текст
        String alertText = registrationPage.getAlertTextAndAccept();
        softAssert.assertEquals(alertText, "Please, check your email.", "Alert text mismatch!");
        // проверяем, что произошел редирект на главную страницу
        softAssert.assertTrue(registrationPage.isRedirectedToMainPage(), "Redirect failed!");

        softAssert.assertAll();
    }

    // негативный тест на ввод некорректного email
    @Test
    public void Registration_InvalidEmail_NegativeTest() {
        SoftAssert softAssert = new SoftAssert();
        registrationPage.openRegistrationPage();

        String[] invalidEmails = {"invalidemail", "invalid@", "invalid.com", "user@domain", "@domain.com"};
        for (String email : invalidEmails) {
            registrationPage.enterEmail(email);
            // проверяем, что отображается сообщение об ошибке
            softAssert.assertTrue(registrationPage.isEmailValidationErrorDisplayed(), "No error for: " + email);
        }
        softAssert.assertAll();
    }

    // негативный тест валидации пароля
    @Test
    public void Password_Validation_NegativeTest() {
        SoftAssert softAssert = new SoftAssert();
        String[] invalidPasswords = {"short", "password1!", "Password!", "Password1", "Пароль123!"};
        registrationPage.openRegistrationPage();

        for (String invalidPassword : invalidPasswords) {
            registrationPage.enterPassword(invalidPassword);
            registrationPage.confirmPassword(invalidPassword);
            // проверяем, что появляется сообщение об ошибке
            softAssert.assertTrue(registrationPage.isPasswordValidationErrorDisplayed(), "No error for: " + invalidPassword);
        }
        softAssert.assertAll();
    }

    // тест регистрации с временным email через mail.tm
    @Test
    public void RegistrationWithMailTmPositiveTest() throws IOException, InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        String email = TestBaseUI.generateEmail(); // генерируем временный email
        String password = UserData.VALID_PASSWORD;

        registrationPage.openRegistrationPage();
        registrationPage.enterEmail(email);
        registrationPage.enterPassword(password);
        registrationPage.confirmPassword(password);
        registrationPage.checkAgreeBox();
        registrationPage.submitRegistration();

        // проверяем появление алерта и его текст
        String alertText = registrationPage.getAlertTextAndAccept();
        softAssert.assertEquals(alertText, "Please, check your email.", "Alert text mismatch!");

        // получаем ссылку для подтверждения регистрации
        String confirmationLink = TestBaseUI.getConfirmationLink(email);
        softAssert.assertNotNull(confirmationLink, "No confirmation link found!");

        // переходим по ссылке подтверждения
        app.driver.get(confirmationLink);
        softAssert.assertTrue(registrationPage.isRedirectedToMainPage(), "Redirect failed!");

        softAssert.assertAll();
    }
}
