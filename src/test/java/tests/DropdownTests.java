package tests;

import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import pages.MainPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.OrderFormPage;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class DropdownTests {
    private WebDriver driver;
    private MainPage mainPage;

    private final int index;
    private final String expectedQuestion;
    private final String expectedAnswer;
    private OrderFormPage orderFormPage;

    public DropdownTests(int index, String expectedQuestion, String expectedAnswer) {
        this.index = index;
        this.expectedQuestion = expectedQuestion;
        this.expectedAnswer = expectedAnswer;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {0, "Сколько это стоит? И как оплатить?", "Сутки — 400 рублей. Оплата курьеру — наличными или картой."},
                {1, "Хочу сразу несколько самокатов! Так можно?", "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим."},
                {2, "Как рассчитывается время аренды?", "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30."},
                {3, "Можно ли заказать самокат прямо на сегодня?", "Только начиная с завтрашнего дня. Но скоро станем расторопнее."},
                {4, "Можно ли продлить заказ или вернуть самокат раньше?", "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."},
                {5, "Вы привозите зарядку вместе с самокатом?", "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится."},
                {6, "Можно ли отменить заказ?", "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои."},
                {7, "Я жизу за МКАДом, привезёте?", "Да, обязательно. Всем самокатов! И Москве, и Московской области."}
        });
    }

    @Before
    public void setUp() {

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, java.util.concurrent.TimeUnit.SECONDS);
        driver.get("https://qa-scooter.praktikum-services.ru/");
        driver.findElement(By.id("rcc-confirm-button")).click();

        WebDriverWait wait = null;

        mainPage = new MainPage(driver);


        // Скроллим до раздела «Вопросы о важном»
        boolean scrolled = mainPage.scrollToQuestionsSectionWithRetry(3);
        if (!scrolled) {
            throw new RuntimeException("Не удалось проскроллить до раздела вопросов после 3 попыток");
        }

        // Ждём загрузки аккордеона перед тестами
        wait = new WebDriverWait(driver, 10);
        wait.until(driver -> {
            try {
                return !mainPage.getQuestionText(0).isEmpty();
            } catch (Exception e) {
                return false;
            }
        });
    }

    @Test
    public void checkAccordionQuestionAndAnswer() {
        // 1. Проверяем текст вопроса перед кликом
        String actualQuestion = mainPage.getQuestionText(index);
        assertEquals("Текст вопроса не совпадает для индекса " + index, expectedQuestion, actualQuestion);

        // 2. Кликаем и проверяем текст ответа
        mainPage.clickQuestion(index);
        // Даём время для анимации раскрытия аккордеона
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String actualAnswer = mainPage.getAnswerText(index);
        assertEquals("Текст ответа не совпадает для индекса " + index, expectedAnswer, actualAnswer);
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}

