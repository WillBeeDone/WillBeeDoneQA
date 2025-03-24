package wbd.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ApplicationManager {
    public WebDriver driver;
    public WebDriverWait wait;
    public BasePage basePage;
    public void init() {

        String browser = System.getProperty("browser", "chrome");

        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            default: // Это резервный сценарий на случай, если значение browser не совпадает ни с одним из указанных случаев
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
        }

        driver.get("");
        driver.manage().window().setPosition(new Point(2500, 0));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get("");
        } catch (TimeoutException e) {
            System.out.println("⏳ Page load timed out, restarting the driver...");
            driver.quit();
            driver = new ChromeDriver(); // Перезапуск браузера
            driver.get("");
        }
        basePage = new BasePage(driver,wait);
    }

    public BasePage getBasePage() {
        return basePage;
    }

    public void stop() {
        driver.quit();
    }
}
