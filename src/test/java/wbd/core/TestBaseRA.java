package wbd.core;

import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;

public class TestBaseRA {

    protected static final Logger logger = LoggerFactory.getLogger(TestBaseRA.class);

    @BeforeMethod
    public void init() {
        RestAssured.baseURI = "http://localhost:8080/";
        RestAssured.basePath = "api";
    }
}
