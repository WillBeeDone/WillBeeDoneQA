package wbd.web.web_pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import wbd.web.core.BasePage;



public class RecoveryPasswordPage extends BasePage {

    @FindBy(css = "[data-testid='MyInputPasswordRecovery_KjfyhH']")
    WebElement newPasswordField;

    @FindBy(css = "[data-testid='MyInputPasswordRecovery_HyftdTytyH']")
    WebElement confirmNewPasswordField;

    @FindBy(css = "[data-testid='MyButtonPasswordRecovery_HgftFdgtd']")
    WebElement saveNewPasswordButton;

    @FindBy(css = "[data-testid='MyButtonPasswordRecovery_mnbhGfytd']")
    WebElement cancelButton;

    public RecoveryPasswordPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public RecoveryPasswordPage enterNewPassword(String newPassword) {
        newPasswordField.clear();
        newPasswordField.sendKeys(newPassword);
        return this;
    }

    public RecoveryPasswordPage confirmNewPassword(String confirmPassword) {
        confirmNewPasswordField.clear();
        confirmNewPasswordField.sendKeys(confirmPassword);
        return this;
    }

    public RecoveryPasswordPage clickSaveNewPasswordButton() {
        wait.until(ExpectedConditions.elementToBeClickable(saveNewPasswordButton));
        click(saveNewPasswordButton);
        return this;
    }

    public RecoveryPasswordPage clickCancelButton() {
        cancelButton.click();
        return this;
    }
}

