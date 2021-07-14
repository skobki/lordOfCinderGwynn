import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;

public class WebDriverFactory {
    private static final Logger logger = LogManager.getLogger(WebDriverFactory.class);
    public static WebDriver getDriver(String browserName, String strategyName) {
        switch (browserName) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions optionsChrome = new ChromeOptions();
                //5.1. Стратегия загрузки страницы: NORMAL/EAGER/NONE
                switch (strategyName) {
                    default:
                        throw new RuntimeException("Неправильное имя стратегии загрузки страницы: используйте NORMAL/EAGER/NONE");
                    case "NORMAL":
                        optionsChrome.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    case "EAGER":
                        optionsChrome.setPageLoadStrategy(PageLoadStrategy.EAGER);
                    case "NONE":
                        optionsChrome.setPageLoadStrategy(PageLoadStrategy.NONE);
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
                    default:
                        throw new RuntimeException("Неправильное имя стратегии загрузки страницы: используйте NORMAL/EAGER/NONE");
                    case "NORMAL":
                        optionsFirefox.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    case "EAGER":
                        optionsFirefox.setPageLoadStrategy(PageLoadStrategy.EAGER);
                    case "NONE":
                        optionsFirefox.setPageLoadStrategy(PageLoadStrategy.NONE);
                }
                //5.2.
                optionsFirefox.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
                //5.3.
                optionsFirefox.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
                //5.4.
                optionsFirefox.addArguments("-kiosk");
                //Запущенный с аргументом -private, Firefox не покажет все куки страницы
                optionsFirefox.addArguments("-private");
                logger.info("Драйвер для Mozilla Firefox");
                return new FirefoxDriver(optionsFirefox);
            default:
                throw new RuntimeException("Неправильное имя браузера: используйте chrome/firefox");
        }

    }
}
