package com.n11Otomasyon.webAutomation.base;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BaseTest {


  public static WebDriver driver;
  //public static String environment = System.getProperty("environment","PROD");

  @BeforeScenario
  public void setUp() throws MalformedURLException, Exception{

    String baseUrl = "https://www.n11.com/";
    DesiredCapabilities capabilities;

    if (StringUtils.isEmpty(System.getenv("key"))) {
      capabilities = DesiredCapabilities.chrome();
      //capabilities = DesiredCapabilities.safari();
      Map<String, Object> prefs = new HashMap<String, Object>();
      prefs.put("profile.default_content_setting_values.notifications", 2);

      ChromeOptions options = new ChromeOptions();
      //SafariOptions options = new SafariOptions();
      //options.setUseCleanSession(true);
      //options.setUseTechnologyPreview(true);
      options.setExperimentalOption("prefs", prefs);
      //options.addArguments("--kiosk");
      capabilities.setCapability(ChromeOptions.CAPABILITY, options);
      System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");
      driver = new ChromeDriver(capabilities);
      driver.manage().timeouts().pageLoadTimeout(45, TimeUnit.SECONDS);
      //driver = new SafariDriver(capabilities);
    } else {
      capabilities = DesiredCapabilities.firefox();
      capabilities.setCapability("key", System.getenv("key"));
      driver = new RemoteWebDriver(new URL("http://hub.testinium.io/wd/hub"), capabilities);
      ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
    }

    driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS)
        .implicitlyWait(10, TimeUnit.SECONDS);
    //driver.manage().window().fullscreen();
    driver.get(baseUrl);
  }

  @AfterScenario
  public void tearDown() throws Exception{
    driver.quit();
  }


}