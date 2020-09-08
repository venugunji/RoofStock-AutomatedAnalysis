package org.realestate.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.realestate.model.Analysis;
import org.realestate.model.Property;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class GeneralUtils {
    public static Properties properties;

    public static void loadProperties() {
        try (InputStream input = GeneralUtils.class.getClassLoader().getResourceAsStream("roofstock_xpaths.properties")) {
            properties =new Properties();
            properties.load(input);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void fetchAllUrls(WebDriver webDriver, WebDriverWait wait, Analysis analysis,Analysis previousAnalysis) throws InterruptedException {
        webDriver.get("https://www.roofstock.com/investment-property-marketplace");

        /*2. Sort By CashOnCash */
        clickByXPath(webDriver,wait, properties.getProperty("sortByButton"));
        clickByXPath(webDriver,wait,properties.getProperty("sortByCashOnCash"));


        /*4.Find how many pages are present*/
        Integer totalNoOfPages = getTotalNoOfPages(webDriver, wait);
        System.out.println(totalNoOfPages);

        /*5. Extract elements from all  pages and save urls in to object */
        do {
            totalNoOfPages--;

            try {
                extractUrlsFromAPage(webDriver, wait, analysis,previousAnalysis);
            } catch (StaleElementReferenceException e) {
                extractUrlsFromAPage(webDriver, wait, analysis,previousAnalysis);
            }
            clickOnNextPageButton(webDriver, wait);
        } while (totalNoOfPages > 0);
    }




    public static Integer getTotalNoOfPages(WebDriver webDriver, WebDriverWait wait) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("Listings__PaginationContainer-e8eat8-0")));
        List<WebElement> webElements =  webDriver.findElement(By.className("Listings__PaginationContainer-e8eat8-0")).findElement(By.tagName("ul")).findElements(By.tagName("li"));
        return Integer.parseInt(webElements.get(webElements.size()-2).findElement(By.tagName("span")).getText());
    }

    public static void clickOnNextPageButton(WebDriver webDriver, WebDriverWait wait) throws InterruptedException {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("Listings__PaginationContainer-e8eat8-0")));
       WebElement webElement = webDriver.findElement(By.className("Listings__PaginationContainer-e8eat8-0")).findElement(By.className("fa-chevron-right"));
       try{
           webElement.click();
       }catch(Exception e ){
           e.printStackTrace();
          // webElement.click();
       }

        Thread.sleep(2000);
    }

    public static void extractUrlsFromAPage(WebDriver webDriver, WebDriverWait wait, Analysis analysis, Analysis previousAnalysis) {

        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("bOyxdi")));

        List<WebElement> elementList = webDriver.findElements(By.className("bOyxdi"));
        for (WebElement webElement:
                elementList) {
            String url =  webElement.findElement(By.tagName("a")).getAttribute("href");
            String address = webElement.findElement(By.className("MuiTypography-subtitle1")).getAttribute("title");
            String price = webElement.findElement(By.className("hukPZu")).findElement(By.className("jVQRaZ")).getText();

            if(previousAnalysis.getPropertyMap().containsKey(url) && price.equals(previousAnalysis.getPropertyMap().get(url).getPropertyCost().trim())){
                System.out.println("url is present already, so copying "+url);
                analysis.getPropertyMap().put(url, previousAnalysis.getPropertyMap().get(url));
            }else{
                if(previousAnalysis.getPropertyMap().containsKey(url))
                System.out.println("previous price is: "+previousAnalysis.getPropertyMap().get(url).getPropertyCost()+ ", current price is : "+price);
                System.out.println("url not present, adding as new property "+url);
                Property property = new Property();
                property.setUrl(url);
                property.setAddress(address);
                analysis.getPropertyMap().put(url, property);
            }

        }
    }

    public static void clickByXPath(WebDriver webDriver, WebDriverWait wait, String s) {
        String elementXpath = s;
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementXpath)));
        WebElement button = webDriver.findElement(By.xpath(elementXpath));
        button.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void exportToCSV(Analysis analysis) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(Property.class).withHeader();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss_SSS");
       String time =  sdf.format(cal.getTime());

        mapper.writer(schema).writeValue(new File("src/main/resources/Analysis_"+time+".csv"),analysis.getPropertyMap().values());
    }

    public static void importFromCSV(Analysis analysis,String fileName) throws IOException {

        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();

        ObjectReader oReader = csvMapper.reader(Property.class).with(schema);

        Map<String, Property> propertyMap = analysis.getPropertyMap();

        try (Reader reader = new FileReader(fileName)) {
            MappingIterator<Property> mi = oReader.readValues(reader);
            while (mi.hasNext()) {
                Property current = mi.next();
                propertyMap.put(current.getUrl(),current);
               // System.out.println(current);
            }
        }
        System.out.println("number of properties in Excel sheet: " + propertyMap.size());
    }



    public static void analyzeProperty(WebDriver webDriver, WebDriverWait wait, Analysis analysis, Map.Entry entry) {

        webDriver.switchTo().newWindow(WindowType.TAB);

        webDriver.get(entry.getKey().toString());
        try {
            fetchPropertyDetails(webDriver, wait, analysis, entry);

        } catch(NoSuchElementException e){
            System.out.println("no such element exception occurred for "+entry.getKey());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("drag and drop got an exception");
            WebElement frame = webDriver.findElement(By.className("intercom-tour-frame"));

            WebDriver.TargetLocator locator =  webDriver.switchTo();
            WebDriver frameDriver = locator.frame(frame);
            WebElement active = locator.activeElement();
            frameDriver.findElement(By.className("intercom-1o29jst")).click();

            fetchPropertyDetails(webDriver, wait, analysis, entry);
        }

        ArrayList<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(1));
        webDriver.close();
        webDriver.switchTo().window(tabs.get(0));
    }

    public static void fetchPropertyDetails(WebDriver webDriver, WebDriverWait wait, Analysis analysis, Map.Entry entry) {
        //zipCode
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(properties.getProperty("addressLine2"))));
        String addressLine2 = webDriver.findElement(By.xpath(properties.getProperty("addressLine2"))).getText();
        String zipcode = addressLine2.replaceAll("[^0-9]", "");
        System.out.println(zipcode);
        analysis.getPropertyMap().get(entry.getKey()).setZipCode(zipcode);

        //SchoolRatings
        String schoolRatings = webDriver.findElement(By.xpath(properties.getProperty("schoolRatings"))).getText();
        analysis.getPropertyMap().get(entry.getKey()).setSchoolRatings(schoolRatings);

        System.out.println(schoolRatings);
        //RoofStockRatingStars
        WebElement ratingsDiv = webDriver.findElement(By.xpath(properties.getProperty("roofstockRatingStars")));

        double stars = 0.0;
        List<WebElement> labelsWithFullStars = webDriver.findElements(By.className("dv-star-rating-full-star"));
        stars = stars+labelsWithFullStars.size();
        List<WebElement> labelsWithEmptyStars = webDriver.findElements(By.className("dv-star-rating-empty-star"));
        for (WebElement webElement:
             labelsWithEmptyStars) {
            try {
                webElement.findElement(By.cssSelector("svg[data-icon='star-half']"));
                stars = stars + 0.5;
            }catch(NoSuchElementException noSuchElementException){

            }
        }
        System.out.println(stars);
        analysis.getPropertyMap().get(entry.getKey()).setRoofstockRatingStars(String.valueOf(stars));



        //forSale or salePending
        String forSale = "---";
        try {
             forSale = webDriver.findElement(By.xpath((properties.getProperty("forSale")))).getText();
        }catch(Exception e){
            System.out.println("Exception while fetching forSale");
            e.printStackTrace();
        }
        analysis.getPropertyMap().get(entry.getKey()).setForSale(forSale);


        //PropertyCost
        String propertyCost = webDriver.findElement(By.xpath(properties.getProperty("propertyCost"))).getText();
        analysis.getPropertyMap().get(entry.getKey()).setPropertyCost(propertyCost);


        //LeaseEndsOn
        String occupancy = webDriver.findElement(By.xpath(properties.getProperty("occupancy"))).getText();
        if("Vacant".equals(occupancy)){
            analysis.getPropertyMap().get(entry.getKey()).setLeaseEndsOn("Vacant");
        }else if("Occupied".equals(occupancy)){
            String leaseEndsOn = webDriver.findElement(By.xpath(properties.getProperty("leaseEndsOn"))).getText();
            analysis.getPropertyMap().get(entry.getKey()).setLeaseEndsOn(leaseEndsOn);
        }else {
            analysis.getPropertyMap().get(entry.getKey()).setLeaseEndsOn("M/M");
        }

        //Lot Size
        String lotSize = webDriver.findElement(By.xpath(properties.getProperty("lotSize"))).getText();
        analysis.getPropertyMap().get(entry.getKey()).setLotSize(lotSize);
        System.out.println("lotSize:"+lotSize);


        //DragAndDropSlider
        WebElement targetElement = webDriver.findElements(By.className("rc-slider")).get(1).findElement(By.className("rc-slider-rail"));
        WebElement sliderHandle = webDriver.findElements(By.className("rc-slider-handle")).get(1);//.findElement(By.className("rc-slider-rail"));
        Actions actions = new Actions(webDriver);
        actions.dragAndDropBy(sliderHandle,-500,0).build().perform();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //CashOnCash
        webDriver.findElement(By.xpath(properties.getProperty("financialsButton"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(properties.getProperty("cashOnCash"))));
        String cashOnCash = webDriver.findElement(By.xpath(properties.getProperty("cashOnCash"))).getText();
        analysis.getPropertyMap().get(entry.getKey()).setCashOnCash(cashOnCash);

        //initialInvestment
        String initialInvestment = webDriver.findElement(By.xpath(properties.getProperty("initialInvestment"))).getText();
        analysis.getPropertyMap().get(entry.getKey()).setInitialInvestment(initialInvestment);

        //Year
        String description = webDriver.findElement(By.xpath(properties.getProperty("description"))).getText();
        System.out.println(description);
        String year = description.split(Pattern.quote("|"))[2].replaceAll("[^0-9]", "");
        System.out.println(year);
        analysis.getPropertyMap().get(entry.getKey()).setYear(year);

        //downPayment
        String downPayment = webDriver.findElement(By.xpath(properties.getProperty("downPayment"))).getText();
        analysis.getPropertyMap().get(entry.getKey()).setDownPayment(downPayment);

        //annualizedReturnIn5years
        String annualizedReturnIn5Years = webDriver.findElement(By.xpath(properties.getProperty("annualizedReturnIn5Years"))).getText();
        analysis.getPropertyMap().get(entry.getKey()).setAnnualizedReturnIn5Years(annualizedReturnIn5Years);

        //Analyze Zillow

        //analyzeZillow(webDriver,wait,analysis,entry);

        //Analize Niche
        if(!StringUtils.isNotBlank(analysis.getPropertyMap().get(entry.getKey()).getNichePopulation())) {
            analyzeNiche(webDriver, wait, analysis, entry, zipcode);
        }

    }

    public static void analyzeZillow(WebDriver webDriver, WebDriverWait wait, Analysis analysis, Map.Entry entry) {
        String address = analysis.getPropertyMap().get(entry.getKey()).getAddress();

        webDriver.switchTo().newWindow(WindowType.TAB);
        webDriver.get("https://www.zillow.com/");

        try{


            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"search-box-input\"]")));
            webDriver.findElement(By.xpath("//*[@id=\"search-box-input\"]")).sendKeys(address);
            // wait.until(ExpectedConditions.textToBe(By.xpath("//*[@id=\"search-box-input\"]"),address));
            webDriver.findElement(By.xpath("//*[@id=\"search-icon\"]")).submit();

            //Zillow Estimated Value
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("zestimate-value")));
            String estimatedValue = webDriver.findElement(By.className("zestimate-value")).getText();
            System.out.println("Zestimate Value :"+estimatedValue);
            analysis.getPropertyMap().get(entry.getKey()).setZillowEstimatedValue(estimatedValue);

            //Zillow Estimated Rent per month
            String  zillowRentEstimate  = webDriver.findElement(By.xpath("//*[@id=\"ds-data-view\"]/ul/li[3]/div[2]/div[1]/div[1]/div[3]/p[1]/span")).getText();
            analysis.getPropertyMap().get(entry.getKey()).setZillowRentEstimatePerMonth(zillowRentEstimate);

            //Zillow description
            String zillowOwnerDescription = webDriver.findElement(By.xpath("//*[@id=\"ds-data-view\"]/ul/li[4]/div[1]/div/div[1]/div[2]/div")).getText();
            analysis.getPropertyMap().get(entry.getKey()).setZillowOwnerDescription(zillowOwnerDescription);

            //Zillow Median NeighborHood Home value
            String zillowMedianNeighborHoodHomeValue = webDriver.findElement(By.xpath("//*[@id=\"ds-data-view\"]/ul/li[5]/div[1]/div[1]/ul/li[4]/p")).getText();
            analysis.getPropertyMap().get(entry.getKey()).setZillowMedianNeighborHoodHomeValue(zillowMedianNeighborHoodHomeValue);

            //Zillow last sold for
            String zillowLastSoldFor = webDriver.findElement(By.xpath("//*[@id=\"ds-container\"]/div[3]/div[1]/div/p/span[1]/span[2]")).getText();
            analysis.getPropertyMap().get(entry.getKey()).setZillowLastSoldFor(zillowLastSoldFor);

            //Zillow last sold on
            String zillowLastSoldOn = webDriver.findElement(By.xpath("//*[@id=\"ds-container\"]/div[3]/div[1]/div/p/span[2]")).getText();
            analysis.getPropertyMap().get(entry.getKey()).setZillowLastSoldOn(zillowLastSoldOn);


        }catch (Exception e){

        }
        ArrayList<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(2));
         webDriver.close();
        webDriver.switchTo().window(tabs.get(1));
    }

    public static void analyzeNiche(WebDriver webDriver, WebDriverWait wait, Analysis analysis,Map.Entry entry, String zipcode) {
//       Map.Entry<String, Property> existingEntry  =  analysis.getPropertyMap().entrySet().stream()
//                .filter(stringPropertyEntry -> zipcode.equals(analysis.getPropertyMap().get(stringPropertyEntry).getZipCode()))
//                .findAny()
//                .orElse(null);
//
//               if( null != existingEntry){
//                   System.out.println("zip code is present already so updating with it"+zipcode);
//                   Property existingProperty = analysis.getPropertyMap().get(existingEntry);
//
//                   analysis.getPropertyMap().get(entry.getKey()).setNicheRating(existingProperty.getNicheRating());
//                   analysis.getPropertyMap().get(entry.getKey()).setNicheAreaFeel(existingProperty.getNicheAreaFeel());
//                   analysis.getPropertyMap().get(entry.getKey()).setNicheRentPercent(existingProperty.getNicheRentPercent());
//                   analysis.getPropertyMap().get(entry.getKey()).setNicheOwnPercent(existingProperty.getNicheOwnPercent());
//                   analysis.getPropertyMap().get(entry.getKey()).setNicheMedianRent(existingProperty.getNicheMedianRent());
//                   analysis.getPropertyMap().get(entry.getKey()).setNicheMedianHomeValue(existingProperty.getNicheMedianHomeValue());
//                   analysis.getPropertyMap().get(entry.getKey()).setNichePopulation(existingProperty.getNichePopulation());
//               }else{

                   webDriver.switchTo().newWindow(WindowType.TAB);
                   webDriver.get("https://www.niche.com/places-to-live/z/"+zipcode);

                   try{
                       //Niche Rating
                       wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"header\"]/div/div[2]/div[1]/ul[1]/li[1]/span/div")));
                       String rating = webDriver.findElement(By.xpath("//*[@id=\"header\"]/div/div[2]/div[1]/ul[1]/li[1]/span/div")).getText();
                       analysis.getPropertyMap().get(entry.getKey()).setNicheRating(rating);

                       //nicheAreaFeel
                       String areaFeel = webDriver.findElement(By.xpath("//*[@id=\"real-estate\"]/div[2]/div[2]/div/div[1]/div[2]/span")).getText();
                       analysis.getPropertyMap().get(entry.getKey()).setNicheAreaFeel(areaFeel);

                       //nicheRentPercent
                       String rentPercent = webDriver.findElement(By.xpath("//*[@id=\"real-estate\"]/div[2]/div[2]/div/div[2]/div[2]/ul/li[1]/div[3]")).getText();
                       analysis.getPropertyMap().get(entry.getKey()).setNicheRentPercent(rentPercent);

                       //nicheOwnPercent
                       String ownPercent = webDriver.findElement(By.xpath("//*[@id=\"real-estate\"]/div[2]/div[2]/div/div[2]/div[2]/ul/li[2]/div[3]")).getText();
                       analysis.getPropertyMap().get(entry.getKey()).setNicheOwnPercent(ownPercent);

                       //nicheMedianRent
                       String medianRent = webDriver.findElement(By.xpath("//*[@id=\"real-estate\"]/div[2]/div[1]/div/div[2]/div[2]/span")).getText();
                       analysis.getPropertyMap().get(entry.getKey()).setNicheMedianRent(medianRent);

                       //nicheMedianHomeValue
                       String medianHomeValue = webDriver.findElement(By.xpath("//*[@id=\"real-estate\"]/div[2]/div[1]/div/div[1]/div[2]/span")).getText();
                       analysis.getPropertyMap().get(entry.getKey()).setNicheMedianHomeValue(medianHomeValue);

                       //nichePopulation
                       String population = webDriver.findElement(By.xpath("//*[@id=\"about\"]/div[2]/div[1]/div/div/div[2]/span")).getText();
                       analysis.getPropertyMap().get(entry.getKey()).setNichePopulation(population);


                   }catch(Exception e) {
                       System.out.println("Exception while analyzing Niche for zip code: "+zipcode);
                       e.printStackTrace();
                   }
                   ArrayList<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
                   webDriver.switchTo().window(tabs.get(2));
                   webDriver.close();
                   webDriver.switchTo().window(tabs.get(1));
//               }

    }
}
