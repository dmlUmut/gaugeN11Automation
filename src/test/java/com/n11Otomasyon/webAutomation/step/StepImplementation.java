package com.n11Otomasyon.webAutomation.step;

import com.n11Otomasyon.webAutomation.base.BaseTest;
import com.n11Otomasyon.webAutomation.helper.ElementHelper;
import com.n11Otomasyon.webAutomation.helper.StoreHelper;
import com.n11Otomasyon.webAutomation.model.ElementInfo;
import com.thoughtworks.gauge.Step;
import java.util.List;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.Assertions.assertThat;

public class StepImplementation extends BaseTest {


  private static String orderNumber;

  private void navigate(String url){
    driver.navigate().to(url);
  }

  private WebElement findElement(String key){
    ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
    By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
    WebDriverWait webDriverWait = new WebDriverWait(driver, 30);
    WebElement webElement = webDriverWait
        .until(ExpectedConditions.presenceOfElementLocated(infoParam));
    ((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
        webElement);
    return webElement;
  }

  private List<WebElement> findElements(String key){
    ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
    By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
    return driver.findElements(infoParam);
  }

  private void clickElementBy(String key){
    findElement(key).click();
  }

  private void hoverElementBy(String key){
    WebElement webElement = findElement(key);
    Actions actions = new Actions(driver);
    actions.moveToElement(webElement).build().perform();
  }

  private void sendKeysBy(String key, String text){
    findElement(key).sendKeys(text);
  }

  @Step("<key> elementine tıkla")
  public void clickToElement(String key){
    hoverElementBy(key);
    clickElementBy(key);
  }

  @Step("<key> elementinin üzerine gel")
  public void hoverToElement(String key){
    hoverElementBy(key);
  }

  @Step("<url> adresine git")
  public void navigateToUrl(String url){
    navigate(url);
  }

  @Step("<key> elementine <text> değerini yaz")
  public void sendKeys(String key, String text){
    sendKeysBy(key, text);
  }

  @Step("<key> elementi var mı")
  public void checkElement(String key){
    try {
      findElement(key);
    } catch (Exception e) {
      Assert.fail("Element bulunamadı.");
    }
  }

  @Step("<key> elementinden sipariş numarasını kaydet")
  public void saveOrderNumber(String key){
    String orderText = findElement(key).getText();
    orderNumber = orderText.substring(0, orderText.indexOf("n"));
  }

  @Step("Kaydedilmiş siparişin detaylarını aç")
  public void goToSavedOrder(){
    driver.findElement(By.xpath("//td[contains(text(), '" + orderNumber.trim() + "')]")).click();

  }

  @Step("<int> saniye bekle")
  public void waitSeconds(int seconds){
    try {
      Thread.sleep(seconds * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Step("<int> milisaniye bekle")
  public void waitMilliseconds(int seconds){
    try {
      Thread.sleep(seconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  @Step({"<key> elementinin <attribute> niteliği <expectedValue> değerine eşit mi"})
  public void checkElementAttributeEquals(String key, String attribute, String expectedValue){
    WebElement element = findElement(key);
    String actualValue = element.getAttribute(attribute).trim();
    Assert.assertNotNull("Elementin değeri bulunamadi", actualValue);
    Assert.assertEquals("Elementin değeri eşleşmedi", expectedValue, actualValue);

    boolean isValueCorrect = false;
    int againCount = 0;

    while (!isValueCorrect) {
      waitByMilliSeconds(250);

      if (againCount == 50) {
        if (actualValue.contains(expectedValue)) {
          Assert.fail("Elementin niteliğinin değeri beklenen değere eşit değil");
          System.out.println("Beklenen: " + expectedValue + ", Varolan: " + actualValue);
        }
      }

      actualValue = element.getAttribute(attribute).trim();
      if (actualValue.equals(expectedValue)) {
        isValueCorrect = true;
      }

      againCount++;
    }
  }

  @Step({"<milisaniye> milisaniye bekle",
          "Wait <value> milliseconds"})
  public void waitByMilliSeconds(long milliseconds){
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
