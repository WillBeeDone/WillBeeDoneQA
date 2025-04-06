package wbd.web.web_pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import wbd.web.core.BasePage;

public class ForgetPasswordPage extends BasePage {
    public ForgetPasswordPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @FindBy(css = "input[data-testid='MyInputPassRecovery_HdgfY']")
    WebElement emailRecoveryField;

    @FindBy(css = "button[data-testid='MyButtonPassRecovery_PhedgfY']")
    WebElement enterSendButton;

    public ForgetPasswordPage enterEmail(String email) {
        emailRecoveryField.sendKeys(email);
        return this;
    }

    public ForgetPasswordPage enterSendButton() {
        enterSendButton.click();
        return this;
    }
}

