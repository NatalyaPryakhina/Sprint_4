package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;

public class MainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public static final String URL = "https://qa-scooter.praktikum-services.ru/";

    @FindBy(id = "rcc-confirm-button")
    private WebElement cookieButton;

    @FindBy(xpath = ".//button[@class='Button_Button__ra12g']")
    private WebElement topOrderButton;

    @FindBy(xpath = ".//div[contains(@class, 'Home_FinishButton')]//button[text()='Заказать']")
    private WebElement bottomOrderButton;

    @FindBy(className = "accordion")
    private WebElement questionsSection;

    @FindBy(css = ".accordion__button")
    private List<WebElement> dropdownToggles;

    @FindBy(css = ".accordion__panel")
    private List<WebElement> dropdownContents;

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get(URL);
    }

    public void acceptCookies() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(cookieButton)).click();
        } catch (TimeoutException e) {
            // Кнопка куки не критична для теста
        }
    }

    public void scrollToQuestions() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", questionsSection);
        wait.until(ExpectedConditions.elementToBeClickable(dropdownToggles.get(0)));
    }

    public String getQuestionText(int index) {
        return dropdownToggles.get(index).getText();
    }

    public void clickQuestion(int index) {
        WebElement question = dropdownToggles.get(index);
        wait.until(ExpectedConditions.elementToBeClickable(question)).click();
    }

    public String getAnswerText(int index) {
        WebElement answer = dropdownContents.get(index);
        wait.until(ExpectedConditions.visibilityOf(answer));
        return answer.getText();
    }

    public void clickTopOrderButton() {
        wait.until(ExpectedConditions.elementToBeClickable(topOrderButton)).click();
    }

    public void scrollToBottomOrderButton() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", bottomOrderButton);
        wait.until(ExpectedConditions.elementToBeClickable(bottomOrderButton));
    }

    public void clickBottomOrderButton() {
        bottomOrderButton.click();
    }
}





