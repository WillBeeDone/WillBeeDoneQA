package wbd.tests.web_tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.utils.DataProviders;
import wbd.web.data.UserData;
import wbd.web.helpers.HelperMailTm;
import wbd.web.web_pages.RegistrationPage;

import java.io.IOException;

import static wbd.web.helpers.HelperMailTm.getConfirmationLink;

public class RegistrationTests extends TestBaseUI {
    RegistrationPage registrationPage;

    // инициализация страницы регистрации перед выполнением тестов
    @BeforeMethod
    public void precondition() {
        registrationPage = new RegistrationPage(app.driver, app.wait);
    }

    // позитивный тест регистрации нового пользователя
    @Test
    public void Registration_PositiveTest() {
        String randomEmail = "test" + System.currentTimeMillis() + "@gmail.com";

        registrationPage
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
            softAssert.assertTrue(registrationPage.isValidationErrorDisplayed("Incorrect email"), "No error for: " + email);
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
            softAssert.assertTrue(registrationPage.isValidationErrorDisplayed("Must contains upper&lower"), "No error for: " + invalidPassword);
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
        boolean isErrorDisplayed = registrationPage.isValidationErrorDisplayed("Incorrect email");

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
        boolean isErrorDisplayed = registrationPage.isValidationErrorDisplayed("Must contains upper&lower");

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
        String email = HelperMailTm.generateEmail(); // генерируем временный email
        String password = UserData.VALID_PASSWORD;

        registrationPage
                .openRegistrationPage()
                .enterEmail(email)
                .enterPassword(password)
                .confirmPassword(password)
                .checkAgreeBox()
                .submitRegistration();

        // проверяем появление алерта и его текст
        String alertText = registrationPage.getAlertTextAndAccept();
        softAssert.assertEquals(alertText, "Please, check your email.", "Alert text mismatch!");

        long startTime = System.currentTimeMillis(); // засекаем время
        String confirmationLink = getConfirmationLink(email); // получаем ссылку для подтверждения регистрации
        long duration = System.currentTimeMillis() - startTime;
        logger.info("⏱️ Time to get confirmation link: " + duration + "ms");

        softAssert.assertNotNull(confirmationLink, "No confirmation link found!");

        logger.info("⏳ Ждём перед переходом по ссылке...");

        // переходим по ссылке подтверждения
        confirmationLink = confirmationLink.replace("[", "").replace("]", "");
        app.driver.get(confirmationLink);

        WebElement welcomeMsg = app.wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'Welcome!')]")
        ));

        logger.info(welcomeMsg.getText());

        softAssert.assertTrue(welcomeMsg.isDisplayed(), "Welcome text! Not found");

        // проверяем наличие текста подтверждения
        WebElement successText = app.driver.findElement(
                By.xpath("//*[contains(text(), 'Email confirmed successfully!')]")
        );

        softAssert.assertTrue(successText.isDisplayed(), "Success message not displayed");

        // проверяем наличие ссылки "Sign In"
        WebElement signInLink = app.wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(), 'Sign In')]")));
        softAssert.assertTrue(signInLink.isDisplayed(), "Link 'Sign in' not found");

        // кликаем по ссылке "Sign In"
        registrationPage.clickOnRegistrationLink();

        // ждём перехода и подстановки значений

        WebElement emailField = app.wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@type='email' and @name='email']")
        ));
        WebElement passwordField = app.wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@type='password' and @name='password']")
        ));

        // проверяем, что поля не пустые, если что, заполняем вручную
        if (emailField.getAttribute("value").isEmpty()) {
            emailField.sendKeys(email);
        }
        if (passwordField.getAttribute("value").isEmpty()) {
            passwordField.sendKeys(password);
        }
        // жмем кнопку входа
        registrationPage.clickSingInButton();

        softAssert.assertAll();
    }
}