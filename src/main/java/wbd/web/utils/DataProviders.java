package wbd.web.utils;

import org.testng.annotations.DataProvider;

public class DataProviders {
    // payload'ами для SQL-инъекций
    @DataProvider(name = "sqlInjectionPayloads")
    public static Object[][] sqlInjectionPayloads() {
        return new Object[][]{
                {"' OR '1'='1"},
                {"\" OR \"1\"=\"1"},
                {"admin' --"},
                {"' UNION SELECT null,null --"},
                {"1; DROP TABLE users"},
                {"' OR '' = '"}
        };
    }
}
