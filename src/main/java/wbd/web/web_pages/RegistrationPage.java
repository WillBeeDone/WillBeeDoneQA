package wbd.web.web_pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import wbd.web.core.BasePage;


public class RegistrationPage extends BasePage {

    @FindBy(xpath = "//button[@type='button' and text()='Sign Up']")
    WebElement signUpButton;

    @FindBy(xpath = "//input[@type='email' and @name='email']")
    WebElement emailField;

    @FindBy(xpath = "//input[@type='password' and @name='password']")
    WebElement passwordField;

    @FindBy(xpath = "//input[@type='password' and @name='confirmPassword']")
    WebElement confirmPasswordField;

    @FindBy(xpath = "//input[@type='checkbox' and @name='agree']")
    WebElement agreeCheckbox;

    @FindBy(xpath = "//button[@type='submit' and text()='Sign up']")
    WebElement submitButton;

    @FindBy(xpath = "//a[contains(@href, 'sign-in-form') and contains(normalize-space(), 'Sign In')]")
    WebElement signInLink;

    @FindBy(xpath = "//button[@data-testid='MyButtonSignIn_JuhYt']")
    WebElement signInButton;

    @FindBy(xpath = "//*[contains(text(), 'Welcome!')]")
    WebElement welcomeMessage;

    @FindBy(xpath = "//*[contains(text(), 'Email confirmed successfully!')]")
    WebElement confirmationSuccessText;

    @FindBy(xpath = "//a[contains(text(), 'Sign In')]")
    WebElement signInAfterConfirmationLink;


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

    // кликаем на ссылку "Sign In"
    public RegistrationPage clickOnRegistrationLink() {
        signInLink.click();
        return this;
    }

    public RegistrationPage clickSignInButton() {
        wait.until(ExpectedConditions.elementToBeClickable(signInButton));
        signInButton.click(); // теперь кликаем по настоящей кнопке входа
        return this;
    }

    public RegistrationPage waitForEmailAndPasswordFields() {
        wait.until(ExpectedConditions.visibilityOf(emailField));
        wait.until(ExpectedConditions.visibilityOf(passwordField));
        return this;
    }

    public boolean isEmailConfirmedSuccessfully() {
        wait.until(ExpectedConditions.visibilityOf(welcomeMessage));
        logger.info(welcomeMessage.getText());

        return welcomeMessage.isDisplayed() && confirmationSuccessText.isDisplayed();
    }

    public RegistrationPage clickSignInLinkAfterConfirmation() {
        wait.until(ExpectedConditions.elementToBeClickable(signInAfterConfirmationLink)).click();
        return this;
    }


    public RegistrationPage fillEmailAndPasswordIfEmpty(String email, String password) {
        if (emailField.getAttribute("value").isEmpty()) {
            logger.info("Email field is empty. Filling with: " + email);
            emailField.sendKeys(email);
        }
        if (passwordField.getAttribute("value").isEmpty()) {
            logger.info("Password field is empty. Filling with: " + password);
            passwordField.sendKeys(password);
        }
        return this;
    }

}

