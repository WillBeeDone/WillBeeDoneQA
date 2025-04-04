package wbd.tests.web_tests;

import org.testng.annotations.BeforeMethod;
import wbd.core.TestBaseUI;
import wbd.web.web_pages.HomePage;
import wbd.web.web_pages.LoginPage;

public class RecoveryPasswordTests extends TestBaseUI {

    private final String newPassword = "NewPass123!";

    @BeforeMethod
    public void precondition() {
        new HomePage(app.driver, app.wait).getLoginPage();
      //  new LoginPage(app.driver, app.wait).getForgetPasswordPage();
    }


}




