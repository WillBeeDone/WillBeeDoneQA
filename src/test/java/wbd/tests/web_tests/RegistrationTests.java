package wbd.tests.web_tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import wbd.core.TestBaseUI;
import wbd.web.data.UserData;
import org.openqa.selenium.Alert;

import java.time.Duration;

public class RegistrationTests extends TestBaseUI {

    @Test
    public void RegistrationPositiveTest() {

        String randomEmail = "test" + System.currentTimeMillis() + "@gmail.com";

        // click on Sing UP button
        app.driver.findElement(By.xpath("//button[@type='button' and text()='Sign Up']")).click();

        // enter 'email'
        WebElement emailField = app.driver.findElement(By.xpath("//input[@type='email' and @name='email']"));
        emailField.click();
        emailField.clear();
        emailField.sendKeys(randomEmail);

        // enter 'password'
        WebElement passwordField = app.driver.findElement(By.xpath("//input[@type='password' and @name='password']"));
        passwordField.click();
        passwordField.clear();
        passwordField.sendKeys(UserData.VALID_PASSWORD);

        // enter 'confirm password'
        WebElement confirmPasswordField = app.driver.findElement(By.xpath("//input[@type='password' and @name='confirmPassword']"));
        confirmPasswordField.click();
        confirmPasswordField.clear();
        confirmPasswordField.sendKeys(UserData.VALID_PASSWORD);

        // click on 'checkbox'
        WebElement agreeCheckbox = app.driver.findElement(By.xpath("//input[@type='checkbox' and @name='agree']"));
        if (!agreeCheckbox.isSelected()) {
            agreeCheckbox.click();
        }

        // click on Sing UP button
        app.driver.findElement(By.xpath("//button[@type='submit' and text()='Sign up']")).click();

        // ждем появления алерта
        WebDriverWait wait = new WebDriverWait(app.driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.alertIsPresent());

        // переключаемся на алерт
        Alert alert = app.driver.switchTo().alert();

        // берем текст алерта
        String alertText = alert.getText();

        // проверяем текст алерта
        Assert.assertEquals(alertText, "Please, check your email.", "Alert text does not match!");

        // закрываем алерт
        alert.accept();

        // ждем редирект на главную страницу
        wait.until(ExpectedConditions.urlContains("#/"));

        // проверяем, что URL сменился
        Assert.assertTrue(app.driver.getCurrentUrl().contains("#/"),
                "Redirect to main page failed! Current URL: " + app.driver.getCurrentUrl());
    }
}