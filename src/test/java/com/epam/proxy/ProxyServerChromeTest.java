package com.epam.proxy;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarNameVersion;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;

public class ProxyServerChromeTest extends BaseTest{

    private static final String WEBDRIVER_CHROME_DRIVER = "webdriver.chrome.driver";
    private static final String WEBDRIVER_CHROMDRIVER_EXE_PATH = "src/main/resources/webdrivers/mac/chromedriver";

    @Test
    public void testChromeDriver() {
        System.setProperty(WEBDRIVER_CHROME_DRIVER, WEBDRIVER_CHROMDRIVER_EXE_PATH);
        ChromeOptions option = new ChromeOptions();
        option.addArguments("--proxy-server=localhost:"
                + mobProxy.getPort());
        driver = new ChromeDriver(option);
        Har har = mobProxy.getHar();
        har.getLog().setBrowser(new HarNameVersion("Chrome", "70"));

        driver.get("http://google.com");
    }

}
