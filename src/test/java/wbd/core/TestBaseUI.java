package wbd.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;
import wbd.web.core.ApplicationManager;
import wbd.web.core.BasePage;

import java.lang.reflect.Method;

public class TestBaseUI {
    protected final ApplicationManager app = new ApplicationManager();
    protected SoftAssert softAssert;
    public static Logger logger = LoggerFactory.getLogger(TestBaseUI.class);
    String alertText;

    @BeforeMethod
    public void setUp(Method method) {
        logger.info("Test is started: [" + method.getName() + "]");
        app.init();
        softAssert = new SoftAssert(); // добавлено: инициализация softAssert
    }

    @AfterMethod(enabled = false)
    public void tearDown(Method method, ITestResult result) {
        try {
            softAssert.assertAll(); // автоматически проверяем все softAssert-ы
        } catch (AssertionError e) {
            logger.error("Soft assertions failed: " + e.getMessage());
            result.setStatus(ITestResult.FAILURE); // переопределяем статус, если assertAll() упал
        }

        if (result.isSuccess()) {
            logger.info("Test is PASSED: [" + method.getName() + "]");
        } else {
            logger.error("Test is FAILED: [" + method.getName() + "], Screenshot: [" + app.getBasePage().takeScreenshot() + "]");
        }

        app.stop();

    }

    protected void assertRedirectedToLoginPage() {
        softAssert.assertTrue(new BasePage(app.driver, app.wait).isRedirectedToLoginPage(), "There was no redirect to the login page.");
    }

    protected void assertRedirectedToMainPage() {
        softAssert.assertTrue(new BasePage(app.driver, app.wait).isRedirectedToMainPage(), "There was no redirect to the main page.");
    }

    protected void assertAlertTextAndAccept(String expectedPartialText) {
        alertText = new BasePage(app.driver, app.wait).getAlertTextAndAccept();
        softAssert.assertTrue(alertText.contains(expectedPartialText),
        "Expected alert to contain: [" + expectedPartialText + "] but was: [" + alertText + "]");
    }
}


