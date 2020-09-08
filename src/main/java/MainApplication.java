import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.http.AddSeleniumUserAgent;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.realestate.model.Analysis;
import org.realestate.utilities.GeneralUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import static org.realestate.utilities.GeneralUtils.*;

public class MainApplication {

    public static void main(String[] args) throws IOException {
       // System.setProperty("webdriver.chrome.whitelistedIps", "");
        System.setProperty("webdriver.chrome.driver", "/Users/vgunji/GitHubProjects/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=/Users/vgunji/Library/Application\\ Support/Google/Chrome/Default");
        options.addArguments("--start-maximized");
        options.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36"+new Random().nextInt());

        //options.setBinary("/Applications/Google\\ Chrome.app");

        WebDriver webDriver = new ChromeDriver(options);


        webDriver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(60));
        Analysis analysis = new Analysis();
        Analysis previousAnalysis = new Analysis();

        try {
            GeneralUtils.loadProperties();
            String fileName ="src/main/resources/Analysis_2020-08-23_13_25_39_632.csv";
            GeneralUtils.importFromCSV(previousAnalysis,fileName);

            /*1. Get First Page */
            fetchAllUrls(webDriver, wait, analysis, previousAnalysis);

            for (Map.Entry entry :
                    analysis.getPropertyMap().entrySet()) {

                if(!StringUtils.isNotBlank(analysis.getPropertyMap().get(entry.getKey()).getSchoolRatings())) {
                    analyzeProperty(webDriver, wait, analysis, entry);
                }

                //break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(analysis);
            System.out.println(analysis.getPropertyMap().size());
            exportToCSV(analysis);

            webDriver.close();
            webDriver.quit();
        }
    }

}
