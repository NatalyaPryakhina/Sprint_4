package tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pages.MainPage;
import pages.OrderFormPage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderScooterTest extends BaseTest {
    private MainPage mainPage;
    private OrderFormPage orderFormPage;

    private final String name;
    private final String surname;
    private final String address;
    private final String metro;
    private final String phone;
    private final String deliveryDate;
    private final String rentalPeriod;
    private final String scooterColor;
    private final String comment;

    public OrderScooterTest(String name, String surname, String address, String metro,
                            String phone, String deliveryDate, String rentalPeriod,
                            String scooterColor, String comment) {
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

    @Parameterized.Parameters(name = "Заказ: {0} {1}, метро: {3}")
    public static Collection<Object[]> data() {
        String date = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return Arrays.asList(new Object[][] {
                {"Иван", "Петров", "ул. Ленина, 1", "Черкизовская", "+79991234567", date, "сутки", "black", "У подъезда"},
                {"Мария", "Сидорова", "пр. Мира, 25", "Сокольники", "+79167654321", date, "двое суток", "grey", "Не звонить"}
        });
    }

    @Before
    @Override
    public void setUp() {
        super.setUp();
        mainPage = new MainPage(driver);
        orderFormPage = new OrderFormPage(driver);
        mainPage.open();
        mainPage.acceptCookies();
    }

    @Test
    public void testOrderWithTopButton() {
        mainPage.clickTopOrderButton();
        orderFormPage.fillFirstStep(name, surname, address, metro, phone);
        orderFormPage.fillSecondStep(deliveryDate, rentalPeriod, scooterColor, comment);
        orderFormPage.submitOrder();
        assertTrue("Окно 'Заказ оформлен' не появилось", orderFormPage.isOrderSuccessVisible());
    }

    @Test
    public void testOrderWithBottomButton() {
        mainPage.scrollToBottomOrderButton();
        mainPage.clickBottomOrderButton();
        orderFormPage.fillFirstStep(name, surname, address, metro, phone);
        orderFormPage.fillSecondStep(deliveryDate, rentalPeriod, scooterColor, comment);
        orderFormPage.submitOrder();
        assertTrue("Окно 'Заказ оформлен' не появилось", orderFormPage.isOrderSuccessVisible());
    }
}


