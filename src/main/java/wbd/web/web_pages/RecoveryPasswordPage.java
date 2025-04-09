package wbd.web.web_pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import wbd.web.core.BasePage;

import java.time.Duration;


public class RecoveryPasswordPage extends BasePage {
    private static final String RECOVERY_PAGE_URL = "https://monkfish-app-73239.ondigitalocean.app/#/password-recovery-form/6e0a4ee9-7111-49ec-992f-d8b15fdb168a";
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
        this.wait.withTimeout(Duration.ofSeconds(5));
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
        clickToElement(saveNewPasswordButton);
        return this;
    }

    public RecoveryPasswordPage clickCancelButton() {
        cancelButton.click();
        return this;
    }

    public RecoveryPasswordPage openRecoveryPage() {
        driver.get(RECOVERY_PAGE_URL);
        return this;
    }
}

