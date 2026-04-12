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

    // Локаторы для финальных шагов
    private final By orderSuccessHeader = By.xpath(".//div[contains(@class, 'Order_ModalHeader') and contains(text(), 'Заказ оформлен')]");
    private final By confirmButton = By.xpath("//div[contains(@class, 'Order_Modal')]//button[text()='Да']");

    public OrderFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
        PageFactory.initElements(driver, this);
    }

    // Заполнение первой страницы формы
    public void fillFirstStep(String name, String surname, String address, String metro, String phone) {
        wait.until(ExpectedConditions.visibilityOf(nameField));
        nameField.sendKeys(name);
        surnameField.sendKeys(surname);
        addressField.sendKeys(address);
        selectMetro(metro);
        phoneField.sendKeys(phone);
        nextButton.click();
    }

    // Метод для выбора станции метро из выпадающего списка
    private void selectMetro(String metro) {
        metroField.click();
        By metroOption = By.xpath(".//div[text()='" + metro + "']");
        wait.until(ExpectedConditions.elementToBeClickable(metroOption)).click();
    }

    // Заполнение второй страницы формы
    public void fillSecondStep(String date, String period, String color, String comment) {
        wait.until(ExpectedConditions.visibilityOf(dateField));
        dateField.sendKeys(date, Keys.ENTER);

        rentalPeriodDropdown.click();
        By periodOption = By.xpath(".//div[@class='Dropdown-menu']/div[text()='" + period + "']");
        wait.until(ExpectedConditions.elementToBeClickable(periodOption)).click();

        if ("black".equalsIgnoreCase(color)) {
            wait.until(ExpectedConditions.elementToBeClickable(blackColor)).click();
        } else if ("grey".equalsIgnoreCase(color)) {
            wait.until(ExpectedConditions.elementToBeClickable(greyColor)).click();
        }

        commentField.sendKeys(comment);
    }

    // Завершение оформления заказа
    public void submitOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(orderButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton)).click();
    }

    // Проверка отображения подтверждения заказа
    public boolean isOrderSuccessVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(orderSuccessHeader)).isDisplayed();
    }
}

