package wbd.web.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
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
    @Getter
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
            default:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
        }

        driver.manage().window().setPosition(new Point(2500, 0)); // Размещение окна браузера
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.get("https://monkfish-app-73239.ondigitalocean.app/");
        } catch (TimeoutException e) {
            System.out.println("⏳ Page load timed out, restarting the driver...");
            stop(); // Закрываем текущий браузер
            init(); // Перезапускаем
        }

        basePage = new BasePage(driver, wait);
    }

    public void stop() {
        if (driver != null) {
            driver.quit();
        }
    }
}
