package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.MainPage;
import pages.OrderFormPage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderScooterTest {
    private WebDriver driver;
    private MainPage mainPage;
    private OrderFormPage orderFormPage;

    // Параметры для тестов
    private final String name;
    private final String surname;
    private final String address;
    private final String metro;
    private final String phone;
    private final String deliveryDate;
    private final String rentalPeriod;
    private final String scooterColor;
    private final String comment;

    // Конструктор: убрал LocalDate (заменил на String) и лишние запятые
    public OrderScooterTest(String name, String surname, String address,
                            String metro, String phone, String deliveryDate,
                            String rentalPeriod, String scooterColor, String comment) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metro = metro;
        this.phone = phone;
        this.deliveryDate = deliveryDate;
        this.rentalPeriod = rentalPeriod;
        this.scooterColor = scooterColor;
        this.comment = comment;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        String date = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return Arrays.asList(new Object[][] {
                {"Иван", "Петров", "ул. Ленина, 1", "Черкизовская", "+79991234567", date, "сутки", "black", "У подъезда"},
                {"Мария", "Сидорова", "пр. Мира, 25", "Сокольники", "+79167654321", date, "двое суток", "grey", "Не звонить"}
        });
    }

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://qa-scooter.praktikum-services.ru/");

        // Принимаем куки, чтобы не мешали
        try {
            driver.findElement(By.id("rcc-confirm-button")).click();
        } catch (Exception ignored) {}

        mainPage = new MainPage(driver);
        orderFormPage = new OrderFormPage(driver);
    }

    @Test
    public void testOrderWithTopButton() {
        mainPage.clickTopOrderButton();
        orderFormPage.fillFirstStep(name, surname, address, metro, phone);
        orderFormPage.fillSecondStep(deliveryDate, rentalPeriod, scooterColor, comment);
        orderFormPage.submitOrder();
        assertTrue("Заказ не оформился через верхнюю кнопку", orderFormPage.isOrderSuccessVisible());
    }

    @Test
    public void testOrderWithBottomButton() {
        mainPage.scrollToBottomOrderButton();
        mainPage.clickBottomOrderButton();
        orderFormPage.fillFirstStep(name, surname, address, metro, phone);
        orderFormPage.fillSecondStep(deliveryDate, rentalPeriod, scooterColor, comment);
        orderFormPage.submitOrder();
        assertTrue("Заказ не оформился через нижнюю кнопку", orderFormPage.isOrderSuccessVisible());
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
