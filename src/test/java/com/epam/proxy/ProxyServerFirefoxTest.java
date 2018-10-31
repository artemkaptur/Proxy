package com.epam.proxy;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarNameVersion;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

public class ProxyServerFirefoxTest extends BaseTest{

    private static final String WEBDRIVER_GECKO_DRIVER = "webdriver.gecko.driver";
    private static final String WEBDRIVER_GECKODRIVER_EXE_PATH = "src/main/resources/webdrivers/mac/geckodriver";

    @Test
    public void testFirefoxDriver() {
        System.setProperty(WEBDRIVER_GECKO_DRIVER, WEBDRIVER_GECKODRIVER_EXE_PATH);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, proxy);
        capabilities.setCapability("acceptInsecureCerts", true);
        driver = new FirefoxDriver(capabilities);
        Har har = mobProxy.getHar();
        har.getLog().setBrowser(new HarNameVersion("Firefox", "61"));

        driver.get("http://google.com");
    }

}
