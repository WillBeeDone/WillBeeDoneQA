package wbd.web.web_pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import wbd.web.core.BasePage;

public class LoginPage extends BasePage {

    @FindBy(xpath = "//button[@type='button' and contains(text(),'Sign In')]")
    private WebElement signInButton;

    @FindBy(xpath = "//input[@type='email' and @name='email']")
    private WebElement emailField;

    @FindBy(xpath = "//input[@type='password' and @name='password']")
    private WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit' and contains(text(),'Sign in')]")
    private WebElement submitButton;

    @FindBy(xpath = "//button[contains(text(),'SignOut')]")
    private WebElement signOutButton;

    @FindBy(xpath = "//*[contains(text(), 'Invalid login or password.')]")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void openLoginPage() {
        signInButton.click();
    }

    public void enterEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
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

}