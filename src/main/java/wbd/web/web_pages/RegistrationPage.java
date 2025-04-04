package wbd.web.web_pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wbd.web.core.BasePage;


public class RegistrationPage extends BasePage {

    public static Logger logger = LoggerFactory.getLogger(RegistrationPage.class);

    // инициализация элементов страницы с помощью PageFactory
    @FindBy(xpath = "//button[@type='button' and text()='Sign Up']")
    private WebElement signUpButton;

    @FindBy(xpath = "//input[@type='email' and @name='email']")
    private WebElement emailField;

    @FindBy(xpath = "//input[@type='password' and @name='password']")
    private WebElement passwordField;

    @FindBy(xpath = "//input[@type='password' and @name='confirmPassword']")
    private WebElement confirmPasswordField;

    @FindBy(xpath = "//input[@type='checkbox' and @name='agree']")
    private WebElement agreeCheckbox;

    @FindBy(xpath = "//button[@type='submit' and text()='Sign up']")
    private WebElement submitButton;

    //  =====================================
    public RegistrationPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);

    }

    // открыть страницу регистрации
    public RegistrationPage openRegistrationPage() {
        signUpButton.click(); // кликаем на кнопку "Sign Up"
        return this;
    }

    // вводим email
    public RegistrationPage enterEmail(String email) {
        emailField.clear();  // очищаем поле email
        emailField.sendKeys(email); // вводим email
        return this;
    }

    // вводим пароль
    public RegistrationPage enterPassword(String password) {
        passwordField.clear();  // очищаем поле пароля
        passwordField.sendKeys(password); // вводим пароль
        return this;
    }

    // подтверждаем пароль
    public RegistrationPage confirmPassword(String password) {
        confirmPasswordField.clear();  // очищаем поле подтверждения пароля
        confirmPasswordField.sendKeys(password); // вводим подтверждение пароля
        return this;
    }

    // ставим галочку согласия с условиями
    public RegistrationPage checkAgreeBox() {
        if (!agreeCheckbox.isSelected()) {
            agreeCheckbox.click();  // если не выбрана галочка, ставим ее
        }
        return this;
    }

    // отправляем форму регистрации
    public RegistrationPage submitRegistration() {
        submitButton.click();  // кликаем на кнопку "Sign up"
        return this;
    }

    // получаем текст алерта и закрываем его -> в BasePage

    // проверяем, что произошел редирект на главную страницу -> в BasePage

    // проверяем, что отображается ошибка для некорректного email
    // - isEmailValidationErrorDisplayed(), isPasswordValidationErrorDisplayed() → теперь один универсальный метод в BasePage

    //======================= EmailHelper ==========================  -> в EmailHelper

    // создаем временный e-mail перед регистрацией, используя сервис "Guerrilla Mail",
    // который дает временные e-mail через API
}

