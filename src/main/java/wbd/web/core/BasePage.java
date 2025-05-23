package wbd.web.core;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;

public class BasePage {

    public WebDriver driver;
    public WebDriverWait wait;
    public JavascriptExecutor js;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    public BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver,this);
    }

    public String takeScreenshot() {

        // Capture screenshot
        File tmp = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File screenshot = new File("src/test_screenshots/screen-" + System.currentTimeMillis() + ".png");
        try {
            Files.copy(tmp.toPath(), screenshot.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Screenshot saved to: [" + screenshot.getAbsolutePath() + "]");
        return screenshot.getAbsolutePath();
    }

    public void scrollTo(int y) {
        js.executeScript("window.scrollBy(0," + y + ")");
    }

    public void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }


    protected void shouldHaveText(WebElement element, String text, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(timeout));
        try {
            boolean isTextPresent = wait.until(driver -> {
                String actualText = element.getText();
                // Если текст пустой и элемент является input, берем значение атрибута "value"
                if (actualText.isEmpty() && element.getTagName().equalsIgnoreCase("input")) {
                    actualText = element.getAttribute("value");
                }
                return actualText.contains(text);
            });
            Assert.assertTrue(isTextPresent, "Expected text: [" + text + "], actual text in element: [" + element.getText() + "]");
        } catch (TimeoutException e) {
            throw new AssertionError("Text not found in element: [" + element + "], expected text: [" + text + "]", e);
        }
    }

    public void click(WebElement element,int y) {
        scrollTo(y);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }
    public void clickToElement(WebElement element) {
        scrollToElement(element);
        element.click();
    }
    // получаем текст алерта и закрываем его
    public String getAlertTextAndAccept() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            Alert alert = shortWait.until(ExpectedConditions.alertIsPresent());
            String text = alert.getText();
            logger.info("Alert text: " + text);
            alert.accept();
            return text;
        } catch (TimeoutException | NoAlertPresentException e) {
            logger.warn("Alert not found: " + e.getMessage());
            return null;
        }
    }

    // проверяем, что произошел редирект на главную страницу
    public boolean isRedirectedToMainPage() {
        return driver.getCurrentUrl().contains("#/");  // проверяем, что URL содержит "#/"
    }

    public boolean isRedirectedToLoginPage() {
        return wait.until(ExpectedConditions.urlMatches(".*/#/(login|sign-in-form).*"));

    }

    // проверяем, что отображается ошибка для некорректного email или password
    public boolean isValidationErrorDisplayed(String partialText) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//p[contains(text(),'" + partialText + "')]")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
