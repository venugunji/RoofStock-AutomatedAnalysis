import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.realestate.model.Analysis;
import org.realestate.utilities.GeneralUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static org.realestate.utilities.GeneralUtils.importFromCSV;
import static org.realestate.utilities.GeneralUtils.properties;

public class MainApplicationTest {

@Test
    void loadPropertiesTest(){
    GeneralUtils.loadProperties();
    System.out.println(GeneralUtils.properties.getProperty("mainurl"));
}

@Test
    void getTotalNoOfPagesTest(){
    System.setProperty("webdriver.chrome.driver", "/Users/vgunji/GitHubProjects/chromedriver");
    WebDriver webDriver = new ChromeDriver();
    webDriver.get("https://www.roofstock.com/investment-property-marketplace");
    WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(200));

    wait.until(ExpectedConditions.presenceOfElementLocated(By.className("Listings__PaginationContainer-e8eat8-0")));
    List<WebElement> webElements =  webDriver.findElement(By.className("Listings__PaginationContainer-e8eat8-0")).findElement(By.tagName("ul")).findElements(By.tagName("li"));
    WebElement lastButLi = webElements.get(webElements.size()-2);
    WebElement span = lastButLi.findElement(By.className("kFAgaU"));
    System.out.println(lastButLi.getText());

}

    @Test
    void clickOnNextPageButtonTest() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/Users/vgunji/GitHubProjects/chromedriver");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://www.roofstock.com/investment-property-marketplace");
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(200));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("Listings__PaginationContainer-e8eat8-0")));
        List<WebElement> webElements =  webDriver.findElement(By.className("Listings__PaginationContainer-e8eat8-0")).findElement(By.tagName("ul")).findElements(By.tagName("li"));
        webElements.get(webElements.size()-1).click();

        Thread.sleep(5000);
    }


    @Test
    void importFromCSVTest() throws IOException {
        Analysis analysis = new Analysis();
        String fileName ="src/main/resources/Analysis.csv";

        importFromCSV(analysis,fileName);
        System.out.println(analysis.getPropertyMap().size());
    }


}
