package wbd.web.web_pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import wbd.web.core.BasePage;

public class LoginPage extends BasePage {

    @FindBy(xpath = "//input[@type='email' and @name='email']")
    WebElement emailField;

    @FindBy(xpath = "//input[@type='password' and @name='password']")
    WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit' and contains(text(),'Sign in')]")
    WebElement submitButton;

    @FindBy(xpath = "//button[@type='button' and contains(text(),'Sign In')]")
    WebElement signInButton;

    @FindBy(css = "a[data-testid='sign-out-link']")
    WebElement signOutButton;

    @FindBy(xpath = "//*[contains(text(), 'Invalid login or password.')]")
    WebElement errorMessage;

    @FindBy(css = "a[data-testid='LinkToPasswordRecovery_HgFtg']")
    WebElement forgetPasswordButton;

    @FindBy(xpath = "//img[@alt='Hamburger icon']")
    WebElement hamburgerMenu;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public LoginPage openLoginPage() {
        signInButton.click();
        return this;
    }

    public void enterEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void clickHamburgerMenu() {
        hamburgerMenu.click();
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void submitLogin() {
        submitButton.click();
    }

    public boolean isSignOutButtonDisplayed() {
        return signOutButton.isDisplayed();
    }

    public String getErrorMessageText() {
        return errorMessage.getText();
    }

    public ForgetPasswordPage getForgetPasswordPage() {
        wait.until(ExpectedConditions.visibilityOf(forgetPasswordButton));
        forgetPasswordButton.click();
        return new ForgetPasswordPage(driver, wait);
    }
}