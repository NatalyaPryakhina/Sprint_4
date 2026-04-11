package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;


public class MainPage {
    private WebDriver driver;
    private WebDriverWait wait;


    @FindBy(xpath = ".//button[@class='Button_Button__ra12g']")
    private WebElement topOrderButton;

    // Локатор нижней кнопки «Заказать» (в футере или перед ним)
    @FindBy(xpath = ".//div[contains(@class, 'Home_FinishButton')]//button[text()='Заказать']")
    private WebElement bottomOrderButton;


    // Секция «Вопросы о важном» (для скролла)
    @FindBy(className = "accordion")
    private WebElement questionsSection;

    // Все элементы-вопросы (стрелочки)
    @FindBy(css = ".accordion__button")
    private List<WebElement> dropdownToggles;

    // Все элементы с ответами
    @FindBy(css = ".accordion__panel")
    private List<WebElement> dropdownContents;

    public MainPage(WebDriver driver) {
        this.driver = driver;
        // Сначала инициализируем wait
        this.wait = new WebDriverWait(driver, 10);
        // Затем инициализируем элементы через PageFactory
        PageFactory.initElements(driver, this);
    }


    //Скролл до раздела «Вопросы о важном»
    public boolean scrollToQuestionsSectionWithRetry(int maxRetries) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                WebDriverWait localWait = new WebDriverWait(driver, 15);
                localWait.until(ExpectedConditions.visibilityOf(questionsSection));

                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                        questionsSection);

                Thread.sleep(1000);
                return true;
            } catch (Exception e) {
                System.out.println("Попытка " + attempt + " скролла не удалась: " + e.getMessage());
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        return false;
    }

    /**
     * Текст вопроса по индексу
     */
    public String getQuestionText(int index) {
        wait.until(driver -> !dropdownToggles.isEmpty());
        return dropdownToggles.get(index).getText().trim();
    }

    /**
     * Клик на вопрос по индексу
     */
    public void clickQuestion(int index) {
        wait.until(driver -> !dropdownToggles.isEmpty());
        dropdownToggles.get(index).click();
    }

    /**
     * Текст ответа по индексу
     */
    public String getAnswerText(int index) {
        wait.until(driver -> !dropdownContents.isEmpty());
        return dropdownContents.get(index).getText().trim();
    }

    //клик по верхней кнопке заказать
    public void clickTopOrderButton() {
        wait.until(ExpectedConditions.elementToBeClickable(topOrderButton));
        topOrderButton.click();
    }

    // Скролл до нижней кнопки «Заказать»
    public void scrollToBottomOrderButton() {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                bottomOrderButton
        );
        wait.until(ExpectedConditions.elementToBeClickable(bottomOrderButton));
    }

    // Клик по нижней кнопке
    public void clickBottomOrderButton() {
        bottomOrderButton.click();
    }
}



