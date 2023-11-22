package flight.system.Destination;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;

public class Scraper {
    String chromeDriverPath;
    ChromeOptions options;
    WebDriver driver;
    JavascriptExecutor js;

public void setUpDriver(String url, boolean headless){
    chromeDriverPath = "src/main/java/flight/system/Destination/chromedriver.exe";
    System.setProperty("webdriver.chrome.driver", chromeDriverPath);
    options = new ChromeOptions();
    if(headless){
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors");
    }
    driver = new ChromeDriver(options);
    js = (JavascriptExecutor) driver;
    driver.get(url);
}

    public void delay(int del){
        try {
            Thread.sleep(del);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getCityUrl(String city){
        setUpDriver("https://www.tripadvisor.de/Search?q=" + city, true);
        while (true){
            try{
                driver.findElement(By.xpath("//*[@id=\"onetrust-accept-btn-handler\"]")).click();
                break;
            }catch (Exception e){
                delay(100);
            }
        }
        ArrayList<String> elements = new ArrayList<>();
        while (true){
            try{
                for (int i = 0; i < 10; i++) {
                    if(i != 5){
                        elements.add(driver.findElement(By.xpath("//*[@id=\"BODY_BLOCK_JQUERY_REFLOW\"]/div[2]/div/div[2]/div/div/div/div/div[1]/div/div[1]/div/div[3]/div/div[1]/div/div[2]/div/div/div[" + (i + 1) + "]" /*+ "/div/div/div/div[2]/div[1]/div[1]"*/)).getAttribute("innerHTML"));
                    }
                }
                break;
            }catch (Exception e){
                if(!elements.isEmpty()){
                    break;
                }
                delay(100);
            }
        }
        elements.removeIf(s -> !s.contains("geo"));
        String url = elements.get(0);
        url = url.substring(url.indexOf("/"),url.indexOf("html") + 4);
        url = "https://www.tripadvisor.de" + url;
        //System.out.println(url);
        driver.close();
        return url;
    }

    public POI[][] getPOI(String city){
        setUpDriver(getCityUrl(city), true);
        while (true){
            try{
                driver.findElement(By.xpath("//*[@id=\"onetrust-accept-btn-handler\"]")).click();
                break;
            }catch (Exception e){
                delay(100);
            }
        }
        ArrayList<POI[]> elements = new ArrayList<>();
        ArrayList<Attraction> attractions = new ArrayList<>();
        ArrayList<Hotel> hotels = new ArrayList<>();
        ArrayList<Restaurant> restaurants = new ArrayList<>();

        int index = 0;
        while (true){
            try{
                String element = driver.findElement(By.xpath("//*[@id=\"lithium-root\"]/main/div[7]/div[1]/div[2]/div[1]/div/div[2]/div/div[2]/div/ul/li[" + (index + 1) + "]/div[1]")).getAttribute("innerHTML");;
                String imageLink = element.substring(element.indexOf("srcset=\"") + 8, element.indexOf("1x,https://"));
                String name = element.substring(element.indexOf("ukgoS") + 7, element.indexOf("bzahk u") - 24);
                String link = "https://www.tripadvisor.de" + element.substring(9, element.indexOf(".html") + 5);
                attractions.add(new Attraction(imageLink,link,name));
            }catch (Exception e){

                if(!attractions.isEmpty()){
                    break;
                }
                delay(100);
            }
            index++;
        }
        index = 0;
        while (true){
            try{
                String element = driver.findElement(By.xpath("//*[@id=\"lithium-root\"]/main/div[7]/div[1]/div[2]/div[2]/div/div[2]/div/div[2]/div/ul/li[" + (index + 1) + "]/div[1]")).getAttribute("innerHTML");;
                String imageLink = element.substring(element.indexOf("srcset=\"") + 8, element.indexOf("1x,https://"));
                String name = element.substring(element.indexOf("ukgoS") + 7, element.indexOf("bzahk u") - 24);
                String link = "https://www.tripadvisor.de" + element.substring(9, element.indexOf(".html") + 5);
                hotels.add(new Hotel(imageLink,link,name));
            }catch (Exception e){

                if(!hotels.isEmpty()){
                    break;
                }
                delay(100);
            }
            index++;
        }
        index = 0;
        while (true){
            try{
                String element = driver.findElement(By.xpath("//*[@id=\"lithium-root\"]/main/div[7]/div[1]/div[2]/div[3]/div/div[2]/div/div[2]/div/ul/li[" + (index + 1) + "]/div[1]")).getAttribute("innerHTML");;
                String imageLink = element.substring(element.indexOf("srcset=\"") + 8, element.indexOf("1x,https://"));
                String name = element.substring(element.indexOf("ukgoS") + 7, element.indexOf("bzahk u") - 24);
                String link = "https://www.tripadvisor.de" + element.substring(9, element.indexOf(".html") + 5);
                restaurants.add(new Restaurant(imageLink,link,name));
            }catch (Exception e){

                if(!restaurants.isEmpty()){
                    break;
                }
                delay(100);
            }
            index++;
        }
        //elements.forEach(System.out::println);
        driver.close();
        elements.add(attractions.toArray(new Attraction[0]));
        elements.add(hotels.toArray(new Hotel[0]));
        elements.add(restaurants.toArray(new Restaurant[0]));
        return elements.toArray(POI[][]::new);
    }

    public static void main(String[] args){
        Scraper s = new Scraper();
        s.getPOI("Berlin");
    }
}
