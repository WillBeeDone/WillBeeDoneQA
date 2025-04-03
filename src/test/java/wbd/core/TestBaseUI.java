package wbd.core;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import wbd.web.core.ApplicationManager;
import wbd.web.data.UserData;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestBaseUI {
    protected final ApplicationManager app = new ApplicationManager();
    public static Logger logger = LoggerFactory.getLogger(TestBaseUI.class);

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


