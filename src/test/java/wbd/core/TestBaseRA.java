package wbd.core;

import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

public class TestBaseRA {

    protected static final Logger logger = LoggerFactory.getLogger(TestBaseRA.class);
    protected static String accessToken;
    protected static String refreshToken;

    public SoftAssert softAssert = new SoftAssert();

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        TestBaseRA.accessToken = accessToken;
    }

    public static String getRefreshToken() {
        return refreshToken;
    }

    public static void setRefreshToken(String refreshToken) {
        TestBaseRA.refreshToken = refreshToken;
    }

    @BeforeMethod
    public void init() {
        RestAssured.baseURI = "https://monkfish-app-73239.ondigitalocean.app/";
        RestAssured.basePath = "api";
    }
}
