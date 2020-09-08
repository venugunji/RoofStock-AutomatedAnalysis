package org.realestate;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.realestate.model.Analysis;
import org.realestate.utilities.GeneralUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Random;

import static org.realestate.utilities.GeneralUtils.*;

public class MainApplication_Intermediate {
    public static void main(String[] args) throws IOException {
        // System.setProperty("webdriver.chrome.whitelistedIps", "");
        System.setProperty("webdriver.chrome.driver", "/Users/vgunji/GitHubProjects/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=/Users/vgunji/Library/Application\\ Support/Google/Chrome/Default");
        options.addArguments("--start-maximized");
        options.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36"+new Random().nextInt());

        // options.setBinary("/Users/vgunji/GitHubProjects/GoogleChrome.app");

        WebDriver webDriver = new ChromeDriver(options);


        webDriver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(60));
        Analysis analysis = new Analysis();

        try {
            GeneralUtils.loadProperties();
            webDriver.get("https://www.roofstock.com/investment-property-marketplace");
            /*1. Get First Page */
            String fileName = "src/main/resources/Analysis.csv";
                GeneralUtils.importFromCSV(analysis,fileName);

            for (Map.Entry entry :
                    analysis.getPropertyMap().entrySet()) {
                if(!StringUtils.isNotBlank(analysis.getPropertyMap().get(entry.getKey()).getNichePopulation())){
                    analyzeProperty(webDriver, wait, analysis, entry);
                }
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
