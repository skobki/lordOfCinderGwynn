import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.*;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverFactory {
    private static final Logger logger = LogManager.getLogger(WebDriverFactory.class);
    public static WebDriver getDriver(String browserName, String strategyName) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions op = new EdgeOptions();
        op.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        switch (browserName) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions optionsChrome = new ChromeOptions();
                //5.1. Стратегия загрузки страницы: NORMAL/EAGER/NONE
                switch (strategyName) {
                    case "NORMAL":
                        optionsChrome.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                        break;
                    case "EAGER":
                        optionsChrome.setPageLoadStrategy(PageLoadStrategy.EAGER);
                        break;
                    case "NONE":
                        optionsChrome.setPageLoadStrategy(PageLoadStrategy.NONE);
                        break;
                    default:
                        throw new RuntimeException("Неправильное имя стратегии загрузки страницы: используйте NORMAL/EAGER/NONE");
                }
                //5.2. Настройка поведения алертов: DISMISS/ACCEPT/ACCEPT_AND_NOTIFY/DISMISS_AND_NOTIFY/IGNORE
                optionsChrome.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
                //5.3. Поддержка JS: true/false
                optionsChrome.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
                //5.4. Открытие браузера в режиме инкогнито + фулскрин
                optionsChrome.addArguments("--start-maximized");
                optionsChrome.addArguments("--incognito");
                logger.info("Драйвер для Google Chrome");
                return new ChromeDriver(optionsChrome);
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions optionsFirefox = new FirefoxOptions();
                //5.1.
                switch (strategyName) {
                    case "NORMAL":
                        optionsFirefox.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                        break;
                    case "EAGER":
                        optionsFirefox.setPageLoadStrategy(PageLoadStrategy.EAGER);
                        break;
                    case "NONE":
                        optionsFirefox.setPageLoadStrategy(PageLoadStrategy.NONE);
                        break;
                    default:
                        throw new RuntimeException("Неправильное имя стратегии загрузки страницы: используйте NORMAL/EAGER/NONE");
                }
                //5.2.
                optionsFirefox.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
                //5.3.
                optionsFirefox.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
                //5.4.
                //optionsFirefox.addArguments("-kiosk");
                //Запущенный с аргументом -private, Firefox не покажет все куки страницы
                optionsFirefox.addArguments("-private");
                logger.info("Драйвер для Mozilla Firefox");
                return new FirefoxDriver(optionsFirefox);
            default:
                throw new RuntimeException("Неправильное имя браузера: используйте chrome/firefox");
        }

    }
}
