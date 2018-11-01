package com.epam.appium;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AndroidSimpleTest {

    @Test
    public void androidSimpleTest() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName","Android");
        capabilities.setCapability("platformVersion","4.2.2");
        capabilities.setCapability("udid", "5a07273f");
        capabilities.setCapability("deviceName", "Galaxy s4");
        capabilities.setCapability("skipUnlock", "true");
        capabilities.setCapability("noReset", "false");
        capabilities.setCapability("appPackage", "com.android.chrome");
        capabilities.setCapability("appActivity", "org.chromium.chrome.browser.ChromeTabbedActivity");

        AndroidDriver driver = new AndroidDriver(
                new URL("http://0.0.0.0:4446/wd/hub"), capabilities);

        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.navigate().to("https://www.onliner.by/");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.quit();
    }

}
