import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class hw03test {
    protected static WebDriver driver;
    private Logger logger = LogManager.getLogger(hw03test.class);
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
    public void hahaProgrammirovanie() {
        driver.get("https://www.dns-shop.ru/");
        logger.info("Открыта страница DNS - https://www.dns-shop.ru/");
        //Файрфокс - второй по аутентичности браузер после IE. В тесте часто будет встречаться if
        //Кратко будет описано: "Потому что файрфокс", поскольку в хроме все работает замечательно и без добавления
        //кода в блоках if
        if (env.toLowerCase().equals("firefox")) {
            //Убрал из фактории kiosk для файрфокса, потому что как-то криво он работает
            //(надеюсь не срежется балл за то, что перевод в фулскрин идет после того, как открывается dns)
            driver.manage().window().maximize();
        }
        //Ожидание, которое в дальнейшем будет часто использоваться
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        //Закрываем плашку с подтверждением города
        WebElement searchYes = driver.findElement(By.linkText("Да"));
        searchYes.click();
        //Потому что файрфокс
        if (env.toLowerCase().equals("firefox")) {
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Смартфоны и гаджеты")));
        }
        //Переходим через выпадашку на страницу со смартфонами
        //
        //Пытался перейти через отображение скрытых элементов с помощью js, но не вышло
        //В итоге узнал, что некоторые сайты удаляют из DOM те элементы, которые не отображаются на странице
        //пока не сделать какое-либо действие. Подумал, что dns - один из таких сайтов и сдался с попытками провернуть
        //хитрость с помощью js.
        //
        WebElement step1 = driver.findElement(By.linkText("Смартфоны и гаджеты"));
        Actions actions = new Actions(driver);
        actions
                .moveToElement(step1)
                .perform();
        //Список открывается не моментально, поэтому ожидание
        By visibilityOfList = (By.xpath("//body/div[@class='container']/div[@class='homepage-top-grid']/div[@id='homepage-desktop-menu-wrap']/div[@class='menu-desktop']/div[2]/div[2]"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(visibilityOfList));
        WebElement step2 = driver.findElement(By.xpath("//a[@class='ui-link menu-desktop__second-level'][contains(text(),'Смартфоны')]"));
        step2.click();
        //Потому что файрфокс (цепляюсь за выпадающий список сортировки)
        //(за что-то другое зацепиться не получалось)
        if (env.toLowerCase().equals("firefox")) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='top-filter__selected']")));
        }
        //Скриншот страницы со смартфонами
        //
        //В файрфоксе, если не выставить ожидание,
        //то страница не успевает загрузиться перед скрином и скринится не до конца загруженная страница.
        try {
            Screenshot screenshot = new AShot().takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\pageScreenShot1.png"));
            logger.info("Скриншот сохранен в файле [temp\\pageScreenShot1.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Исполнитель javascript, в дальнейшем часто используется
        JavascriptExecutor js = (JavascriptExecutor) driver;
        //Например для прокрутки страницы (собственно только для прокрутки он и используется в тесте)
        String script1 = "window.scrollBy(0,1200);";
        js.executeScript(script1);
        WebElement step3 = driver.findElement(By.xpath("//label[21]"));
        step3.click();
        WebElement step4 = driver.findElement(By.linkText("Объем оперативной памяти"));
        step4.click();
        WebElement step5 = driver.findElement(By.xpath("//div[@class='ui-collapse__content ui-collapse__content_list ui-collapse__content_in']//label[6]"));
        step5.click();
        //script2 для прокрутки страницы часто будет использоваться ниже
        String script2 = "window.scrollBy(0, 1000);";
        js.executeScript(script2);
        WebElement step6 = driver.findElement(By.xpath("//button[contains(text(),'Применить')]"));
        step6.click();
        //Потому что файрфокс
        if (env.toLowerCase().equals("firefox")) {
            js.executeScript("window.scrollBy(0, -2500);");
        }
        WebElement step7 = driver.findElement(By.xpath("//span[@class='top-filter__selected']"));
        actions
                .moveToElement(step7)
                .click()
                .perform();
        //Потому что файрфокс
        if (env.toLowerCase().equals("firefox")) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='popover-block popover-block_show']")));
        }
        WebElement step8 = driver.findElement(By.xpath("//span[contains(text(),'Сначала дорогие')]"));
        step8.click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='products-page__list']//div[2]//div[1]//div[4]//div[1]//div[1]")));
        //Скриншот страницы с товарами после сортировки
        try {
            Screenshot screenshot = new AShot().takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\pageScreenShot2.png"));
            logger.info("Скриншот сохранен в файле [temp\\pageScreenShot2.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String xpathOfFirstElement = "//body/div[@class='container category-child']/div[@class='products-page']/div[@class='products-page__content']/div[@class='products-page__list']/div[@class='products-list']/div[@class='products-list__content']/div[2]/div[1]/a[1]";
        WebElement step9 = driver.findElement(By.xpath(xpathOfFirstElement));
        //Записываю текст ссылки в строку, чтобы потом проверить находится ли часть этой строки
        //с названием товара в title
        //
        //Не смог придумать что-то лучше
        //
        String str = step9.getText();
        int startIndex = str.indexOf("[");
        int endIndex = str.indexOf("]");
        String rep = "";
        String check = str.substring(startIndex, endIndex + 1);
        String goal = str.replace(check, rep);
        //С помощью shift открываю новое окно браузера
        actions
                .keyDown(Keys.SHIFT)
                .click(step9)
                .keyUp(Keys.SHIFT)
                .perform();
        //Перевожу webdriver на новое окно, ибо если не перевести,
        //то дальнейшие действия будут выполняться в старом окне
        for(String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
        //Потому что файрфокс
        if (env.toLowerCase().equals("firefox")) {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='product-buy product-buy_one-line']//div[@class='product-buy__price product-buy__price_active']")));
        }
        //Скриншот первого товара из списка
        try {
            Screenshot screenshot = new AShot().takeScreenshot(driver);
            ImageIO.write(screenshot.getImage(), "png", new File("temp\\pageScreenShot3.png"));
            logger.info("Скриншот сохранен в файле [temp\\pageScreenShot3.png]");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Получаю title страницы с товаром
        String title = driver.getTitle();
        //Проверяю текущий title с ожидаемым
        if (title.contains(goal)) {
            logger.info("Заголовок страницы соответствует ожидаемому");
            logger.info("Заголовок страницы: " + title);
        } else {
            logger.info("Заголовок страницы не соответствует ожидаемому");
            logger.info("Ожидаемый заголовок страницы должен содержать: " + goal);
            logger.info("Текущий заголовок страницы: " + title);
        }
        js.executeScript(script2);
        WebElement step10 = driver.findElement(By.linkText("Характеристики"));
        step10.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-tab='characteristics']")));
        js.executeScript(script2);
        WebElement step11 = driver.findElement(By.xpath("//tbody/tr[contains(.,'Объем оперативной памяти ')]/td[2]/div"));
        //Запись в строку значения оперативной памяти выбранного товара
        //для дальнейшего сравнения с ожидаемым
        //
        //Можно было бы, скорее всего, в ожидаемое значение пихнуть оперативную память, которую
        //выбираем во время фильтрации по товарам, но я этого не сделал
        String checkOp = step11.getText();
        String expectedOp = "8 Гб";
        if (checkOp.contains(expectedOp)) {
            logger.info("Объем оперативной памяти соответствует ожидаемому");
            logger.info("Объем оперативной памяти: " + checkOp);
        } else {
            logger.info("Объем оперативной памяти не соответствует ожидаемому");
            logger.info("Ожидаемый объем оперативный памяти: " + expectedOp);
            logger.info("Текущий объем оперативной памяти: " + checkOp);
        }
        //Единственный разрешенный слипер :)
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
