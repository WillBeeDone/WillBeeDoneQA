package wbd.tests.web_tests;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.web.data.UserData;
import wbd.web.web_pages.LoginPage;


public class LoginTests extends TestBaseUI {
    @Test
    public void loginPositiveTest() {
        // Создаем экземпляр LoginPage
        LoginPage loginPage = new LoginPage(app.driver, app.wait);

        // click Sign In button
        app.driver.findElement(By.xpath("//button[contains(text(),'Sign In')]")).click();

        // enter login and password
        loginPage.enterEmail(UserData.VALID_EMAIL);
        loginPage.enterPassword("Password!123");

        // click Sign in button
        app.driver.findElement(By.xpath("//button[contains(text(),'Sign in')]")).click();

        //Проверяем наличие Sign Out
        Assert.assertTrue(app.driver.findElement(By.xpath("//button[contains(text(),'SignOut')]")).isDisplayed());
    }

    @Test
    public void loginWrongPasswordNegativeTest() {
        LoginPage loginPage = new LoginPage(app.driver, app.wait);

        app.driver.findElement(By.xpath("//button[contains(text(),'Sign In')]")).click();

        loginPage.enterEmail(UserData.VALID_EMAIL);
        loginPage.enterPassword("WrongPassword123");

        app.driver.findElement(By.xpath("//button[contains(text(),'Sign in')]")).click();

        Assert.assertEquals(loginPage.getErrorMessage().getText(), "Invalid login or password.");
    }
}
