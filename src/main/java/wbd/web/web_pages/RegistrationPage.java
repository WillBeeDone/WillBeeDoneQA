package wbd.web.web_pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import wbd.web.core.BasePage;


public class RegistrationPage extends BasePage {

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
        PageFactory.initElements(driver, this);  // инициализация элементов на странице
    }

    // открыть страницу регистрации
    public void openRegistrationPage() {
        signUpButton.click();  // кликаем на кнопку "Sign Up"
    }

    // вводим email
    public void enterEmail(String email) {
        emailField.clear();  // очищаем поле email
        emailField.sendKeys(email);  // вводим email
    }

    // вводим пароль
    public void enterPassword(String password) {
        passwordField.clear();  // очищаем поле пароля
        passwordField.sendKeys(password);  // вводим пароль
    }

    // подтверждаем пароль
    public void confirmPassword(String password) {
        confirmPasswordField.clear();  // очищаем поле подтверждения пароля
        confirmPasswordField.sendKeys(password);  // вводим подтверждение пароля
    }

    // ставим галочку согласия с условиями
    public void checkAgreeBox() {
        if (!agreeCheckbox.isSelected()) {
            agreeCheckbox.click();  // если не выбрана галочка, ставим ее
        }
    }

    // отправляем форму регистрации
    public void submitRegistration() {
        submitButton.click();  // кликаем на кнопку "Sign up"
    }

    // получаем текст алерта и закрываем его
    public String getAlertTextAndAccept() {
        wait.until(ExpectedConditions.alertIsPresent());  // ждем появления алерта
        Alert alert = driver.switchTo().alert();  // переключаемся на алерт
        String alertText = alert.getText();  // получаем текст алерта
        alert.accept();  // принимаем алерт
        return alertText;
    }

    // проверяем, что произошел редирект на главную страницу
    public boolean isRedirectedToMainPage() {
        return driver.getCurrentUrl().contains("#/");  // проверяем, что URL содержит "#/"
    }

    // проверяем, что отображается ошибка для некорректного email
    public boolean isEmailValidationErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(text(),'Incorrect email')]")));
            return true;
        } catch (Exception e) {
            return false;  // если ошибка не появилась, возвращаем false
        }
    }

    // проверяем, что отображается ошибка для некорректного пароля
    public boolean isPasswordValidationErrorDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//p[contains(text(),'Must contains upper&lower case, number, special ch')]")
            ));
             return true;  // ошибка отображается, возвращаем true
        } catch (Exception e) {
            return false;  // ошибка не отображается, возвращаем false
        }
    }
}

