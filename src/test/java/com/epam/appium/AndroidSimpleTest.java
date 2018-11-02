package com.epam.appium;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static io.appium.java_client.touch.offset.PointOption.point;

public class AndroidSimpleTest {

    private static AndroidDriver driver;

    @BeforeClass
    public void driverSetUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName","Android");
        capabilities.setCapability("platformVersion","6.0.1");
        capabilities.setCapability("udid", "4d0005c0d4ff91f5");
        capabilities.setCapability("deviceName", "Galaxy s5");
        capabilities.setCapability("skipUnlock", "true");
        capabilities.setCapability("noReset", "false");
        capabilities.setCapability("appPackage", "com.android.contacts");
        capabilities.setCapability("appActivity", "com.android.contacts.activities.PeopleActivity");

       driver = new AndroidDriver(new URL("http://0.0.0.0:4446/wd/hub"), capabilities);
    }

    @Test
    public void androidSimpleTest() throws MalformedURLException {
        new TouchAction(driver)
                .press(point(462, 1541))
                .moveTo(point(462, 462))
                .release()
                .perform();

        new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(driver
                        .findElement(By.xpath("//android.widget.TextView[@text='Nastassia Suhovei']")))).click();

        Assert.assertTrue(driver.findElement(By.xpath("//android.widget.TextView[@text='+375 29 372-07-93']")).isDisplayed());
    }

    @AfterClass
    public void quitDriver()
    {
        driver.quit();
    }

}
