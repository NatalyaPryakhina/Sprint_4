package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrderFormPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- Шаг 1: Для кого самокат ---
    @FindBy(xpath = ".//input[@placeholder='* Имя']")
    private WebElement nameField;

    @FindBy(xpath = ".//input[@placeholder='* Фамилия']")
    private WebElement surnameField;

    @FindBy(xpath = ".//input[@placeholder='* Адрес: куда привезти заказ']")
    private WebElement addressField;

    @FindBy(xpath = ".//input[@placeholder='* Станция метро']")
    private WebElement metroField;

    @FindBy(xpath = ".//input[@placeholder='* Телефон: на него позвонит курьер']")
    private WebElement phoneField;

    @FindBy(xpath = ".//button[text()='Далее']")
    private WebElement nextButton;

    // --- Шаг 2: Про аренду ---
    @FindBy(xpath = ".//input[@placeholder='* Когда привезти самокат']")
    private WebElement dateField;

    @FindBy(className = "Dropdown-root")
    private WebElement rentalPeriodDropdown;

    @FindBy(id = "black")
    private WebElement blackColor;

    @FindBy(id = "grey")
    private WebElement greyColor;

    @FindBy(xpath = ".//input[@placeholder='Комментарий для курьера']")
    private WebElement commentField;

    @FindBy(xpath = "//div[contains(@class, 'Order_Content')]//button[text()='Заказать']")
    private WebElement orderButton;

    // Кнопка подтверждения в модальном окне "Хотите оформить заказ?"
    @FindBy(xpath = "//button[text()='Да']")
    private WebElement confirmOrderButton;

    public OrderFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
        PageFactory.initElements(driver, this);
    }

    public boolean isOrderFormVisible() {
        return wait.until(ExpectedConditions.visibilityOf(nameField)).isDisplayed();
    }

    // Заполнение первой страницы
    public void fillFirstStep(String name, String surname, String address, String metro, String phone) {
        nameField.sendKeys(name);
        surnameField.sendKeys(surname);
        addressField.sendKeys(address);

        // Выбор метро
        metroField.click();
        String metroOptionXpath = ".//div[text()='" + metro + "']";
        WebElement metroOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(metroOptionXpath)));
        metroOption.click();

        phoneField.sendKeys(phone);
        nextButton.click();
    }

    // Заполнение второй страницы
    public void fillSecondStep(String date, String period, String color, String comment) {
        wait.until(ExpectedConditions.visibilityOf(dateField));

        // Дата (ввод и закрытие календаря через Enter)
        dateField.sendKeys(date);
        dateField.sendKeys(Keys.ENTER);

        // Срок аренды (не Select!)
        rentalPeriodDropdown.click();
        String periodXpath = ".//div[@class='Dropdown-menu']/div[text()='" + period + "']";
        driver.findElement(By.xpath(periodXpath)).click();

        // Цвет
        if ("black".equalsIgnoreCase(color)) blackColor.click();
        if ("grey".equalsIgnoreCase(color)) greyColor.click();

        commentField.sendKeys(comment);
    }

    public void submitOrder() {
        // 1. Ждем и кликаем на основную кнопку "Заказать" в форме
        wait.until(ExpectedConditions.elementToBeClickable(orderButton));
        orderButton.click();

        // 2. Находим кнопку "Да" во всплывающем окне и кликаем
                WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'Order_Modal')]//button[text()='Да']")
        ));

    }


    public boolean isOrderSuccessVisible() {
        try {
            WebElement statusHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(".//div[contains(@class, 'Order_ModalHeader') and text()='Заказ оформлен']")));
            return statusHeader.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

