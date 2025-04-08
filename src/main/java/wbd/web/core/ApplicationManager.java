package wbd.web.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", String.valueOf(isJenkins())));

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
            case "chrome":
                WebDriverManager.chromedriver().setup();

                ChromeOptions options = new ChromeOptions();
                System.out.println("=== INIT ChromeOptions ===");
                System.out.println("System.getProperty(browser): " + browser);
                System.out.println("System.getProperty(chrome.options): " + System.getProperty("chrome.options"));
                System.out.println("System.getenv(JENKINS_HOME): " + System.getenv("JENKINS_HOME"));

                if (headless) {
                    options.addArguments("--headless=new", "--disable-gpu", "--window-size=1920,1080", "--no-sandbox", "--disable-dev-shm-usage", "--remote-allow-origins=*");
                }

                String chromeArgs = System.getProperty("chrome.options");
                if (chromeArgs != null) {
                    options.addArguments(chromeArgs);
                }

                driver = new ChromeDriver(options);
        }


        driver.manage().window().setPosition(new Point(2500, 0)); // Размещение окна браузера
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            String baseUrl = System.getProperty("wbd.url", "https://monkfish-app-73239.ondigitalocean.app/#/");
            driver.get(baseUrl);

        } catch (TimeoutException e) {
            System.out.println("⏳ Page load timed out, restarting the driver...");
            stop();
            init();
        }

        basePage = new BasePage(driver, wait);
    }

    private boolean isJenkins() {
        return System.getenv("JENKINS_HOME") != null ||
                System.getenv("BUILD_ID") != null ||
                System.getenv("JENKINS_URL") != null;
    }

    public void stop() {
        if (driver != null) {
            driver.quit();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }
}
