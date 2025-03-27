package wbd.core;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeMethod;



public class TestBaseRA {
    @BeforeMethod
    public void init() {
        RestAssured.baseURI = "http://localhost:8080/";
        RestAssured.basePath = "api";
    }
}
