package wbd.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import wbd.web.core.ApplicationManager;

import java.lang.reflect.Method;

public class TestBaseUI {
    protected final ApplicationManager app = new ApplicationManager();
    Logger logger = LoggerFactory.getLogger(TestBaseUI.class);

    @BeforeMethod
    public void setUp(Method method) {
        logger.info("Test is started: [" + method.getName() + "]");
        app.init();
    }

    @AfterMethod(enabled = false)
    public void tearDown(Method method, ITestResult result) {
        if (result.isSuccess()) {
            logger.info("Test is PASSED: [" + method.getName() + "]");
        } else {
            logger.error("Test is FAILED: [" + method.getName() + "], Screenshot: [" + app.getBasePage().takeScreenshot() + "]");
        }
        app.stop();
    }
}
