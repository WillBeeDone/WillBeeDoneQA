package wbd.tests.web_tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import wbd.core.TestBaseUI;
import wbd.utils.DataProviders;
import wbd.web.data.UserData;
import wbd.web.web_pages.LoginPage;
import wbd.web.web_pages.RegistrationPage;

import java.io.IOException;

public class RegistrationTests extends TestBaseUI {
    private RegistrationPage registrationPage;
    SoftAssert softAssert = new SoftAssert();

    // инициализация страницы регистрации перед выполнением тестов
    @BeforeMethod
    public void precondition() {
        registrationPage = new RegistrationPage(app.driver, app.wait);
    }

    // позитивный тест регистрации нового пользователя
    @Test
    public void Registration_PositiveTest() {
        String randomEmail = "test" + System.currentTimeMillis() + "@gmail.com";

        new RegistrationPage(app.driver, app.wait)
                .openRegistrationPage() // открываем страницу регистрации
                .enterEmail(randomEmail) // вводим email
                .enterPassword(UserData.VALID_PASSWORD) // вводим пароль
                .confirmPassword(UserData.VALID_PASSWORD) // подтверждаем пароль
                .checkAgreeBox() // ставим галочку согласия
                .submitRegistration(); // отправляем форму регистрации

        // проверяем появление алерта и его текст
        String alertText = registrationPage.getAlertTextAndAccept();
        softAssert.assertEquals(alertText, "Please, check your email.", "Alert text mismatch!");
        // проверяем, что произошел редирект на главную страницу
        softAssert.assertTrue(registrationPage.isRedirectedToMainPage(), "Redirect failed!");

        softAssert.assertAll();
    }

    // негативный тест на ввод некорректного email с массивом данных
    @Test
    public void Registration_InvalidEmail_NegativeTest() {
        registrationPage.openRegistrationPage();
        // массив 12 невалидных имейлов  (пока нет реакции сайта на русские буквы)
        String[] invalidEmails = {"test", "test@", "test@gmail", "test@gmail.", "test@.com", "test@@gmail.com", "test@gmail..com", "test@ gmail.com", "test @gmail.com", "test@gmail .com", "test@gmail. com"};
        for (String email : invalidEmails) {
            registrationPage.enterEmail(email);
            // проверяем, что отображается сообщение об ошибке
            softAssert.assertTrue(registrationPage.isEmailValidationErrorDisplayed(), "No error for: " + email);
        }
        softAssert.assertAll();
    }

    // негативный тест валидации пароля c массивом данных
    @Test
    public void Password_Validation_NegativeTest() {
        // массив 11 невалидных паролей
        String[] invalidPasswords = {"1111111", "ffffffff", "Pa123!", " ", " password1!", " password1! ", " pass word1!", "password1!", "Password!", "Password1", "Пароль123!"};
        registrationPage.openRegistrationPage();

        for (String invalidPassword : invalidPasswords) {
            registrationPage.enterPassword(invalidPassword);
            registrationPage.confirmPassword(invalidPassword);
            // проверяем, что появляется сообщение об ошибке
            softAssert.assertTrue(registrationPage.isPasswordValidationErrorDisplayed(), "No error for: " + invalidPassword);
        }
        softAssert.assertAll();
    }

    //======================= Data Provider ====================================

    // негативный тест валидации имейла c DataProvider
    @Test(dataProvider = "invalidEmails", dataProviderClass = DataProviders.class)
    public void Email_Validation_With_Provider_NegativeTest(String invalidEmail, String errorDescription) {
        registrationPage.openRegistrationPage()
                .enterEmail(invalidEmail);

        // фиксируем, что вернет метод проверки появилось сообщение об ошибке или нет
        boolean isErrorDisplayed = registrationPage.isEmailValidationErrorDisplayed();

        logger.info(isErrorDisplayed
                ? "✅ The error is displayed: " + errorDescription
                : "❌ The error is not displayed: " + errorDescription);

        softAssert.assertTrue(isErrorDisplayed, "No error for: " + invalidEmail);
        softAssert.assertAll();
    }

    // негативный тест валидации пароля c DataProvider
    @Test(dataProvider = "invalidPasswords", dataProviderClass = DataProviders.class)
    public void Password_Validation_With_Provider_NegativeTest(String invalidPassword, String errorDescription) {
        registrationPage.openRegistrationPage()
                .enterPassword(invalidPassword);

        // фиксируем, что вернет метод проверки появилось сообщение об ошибке или нет
        boolean isErrorDisplayed = registrationPage.isPasswordValidationErrorDisplayed();

        logger.info(isErrorDisplayed
                ? "✅ The error is displayed: " + errorDescription
                : "❌ The error is not displayed: " + errorDescription);

        softAssert.assertTrue(isErrorDisplayed, "No error for: " + invalidPassword);
        softAssert.assertAll();
    }

    //============================= Сервис mail.tm ==========================================

    // тест регистрации с временным email через mail.tm
    @Test
    public void RegistrationWithMailTmPositiveTest() throws IOException, InterruptedException {
        String email = RegistrationPage.generateEmail(); // генерируем временный email
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
        String confirmationLink = RegistrationPage.getConfirmationLink(email);
        softAssert.assertNotNull(confirmationLink, "No confirmation link found!");

        // переходим по ссылке подтверждения
        app.driver.get(confirmationLink);
        softAssert.assertTrue(registrationPage.isRedirectedToMainPage(), "Redirect failed!");

        softAssert.assertAll();
    }
}
