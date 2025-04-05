package wbd.tests.web_tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.web.web_pages.ForgetPasswordPage;
import wbd.web.web_pages.HomePage;
import wbd.web.web_pages.LoginPage;

public class RecoveryPasswordTests extends TestBaseUI {

    LoginPage loginPage;
    ForgetPasswordPage forgetPasswordPage;
    private final String newPassword = "NewPass123!";

    @BeforeMethod
    public void precondition() {
        loginPage = new HomePage(app.driver, app.wait).getLoginPage();
        forgetPasswordPage = loginPage.getForgetPasswordPage();
    }

    @Test
    public void recoveryPasswordPositiveTest() {


    }
}




