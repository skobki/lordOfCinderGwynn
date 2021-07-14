import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class LordOfCinderGwynnTest {
    protected static WebDriver driver;
    private Logger logger = LogManager.getLogger(LordOfCinderGwynnTest.class);
    //Параметризированный запуск тестов
    String env = System.getProperty("browser", "chrome");
    String st = System.getProperty("option", "NORMAL");

    @BeforeEach
    public void setUp() {
        logger.info("Браузер = " + env);
        logger.info("Стратегия загрузки страницы = " + st);
        driver = WebDriverFactory.getDriver(env.toLowerCase(), st.toUpperCase());
        logger.info("Драйвер запущен");
    }

    @AfterEach
    public void setDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Драйвер остановлен");
        }
    }

    @Test
    public void openPage() {
        driver.get("https://www.dns-shop.ru/");
        logger.info("Открыта страница DNS - " + "https://www.dns-shop.ru/");
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS); //Не работает
        //Ожидание загрузки страницы
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Вывод title
        String title = driver.getTitle();
        logger.info("Заголовок страницы - " + title.toString());
        //Вывод URL
        String currentUrl = driver.getCurrentUrl();
        logger.info("Текущий URL - " + currentUrl.toString());
        //Убираем перекрывашку
        WebElement searchYes = driver.findElement(By.linkText("Да"));
        searchYes.click();
        //Переходим в категорию "Бытовая техника"
        WebElement searchUrl = driver.findElement(By.partialLinkText("Бытовая"));
        searchUrl.click();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Вывод списка всех подкатегорий в категории "Бытовая техника"
        logger.info("-----------------------");
        logger.info("Вывод категорий:");
        List<WebElement> categories = driver.findElements(By.xpath("//div[@class=\"subcategory\"]/descendant::a"));
        for (WebElement category : categories) {
            logger.info("Категория: " + category.getTagName() + " = " + category.getText());
        }
        logger.info("-----------------------");
        //Добавление куки
        driver.manage().addCookie(new Cookie("Our cookie", "Куки, которое добавили мы"));
        //Вывод всех куки
        logger.info("Вывод всех куки");
        Set<Cookie> cookies = driver.manage().getCookies();
        for(Cookie cookie: cookies) {
            logger.info(String.format("Domain: %s", cookie.getDomain()));
            logger.info(String.format("Expiry: %s", cookie.getExpiry()));
            logger.info(String.format("Name: %s", cookie.getName()));
            logger.info(String.format("Path: %s", cookie.getPath()));
            logger.info(String.format("Value: %s", cookie.getValue()));
            logger.info("-----------------------");
        }
        //Ожидание, для просмотра результата
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
