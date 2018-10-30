package com.epam.proxy;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.ProxyServer;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileOutputStream;

public class ProxyServerTest {

    public static final String WEBDRIVER_CHROME_DRIVER = "webdriver.chrome.driver";
    private static final String WEBDRIVER_CHROMDRIVER_EXE_PATH  = "src/main/resources/webdrivers/mac/chromedriver";
    private static final String WEBDRIVER_GECKO_DRIVER = "webdriver.gecko.driver";
    private static final String WEBDRIVER_GECKODRIVER_EXE_PATH = "src/main/resources/webdrivers/mac/geckodriver";

    private static ProxyServer server;
    private static Proxy proxy;

    @BeforeClass
    public static void setUpProxy() throws Exception {
        server = new ProxyServer(4444);
        server.start();
        proxy = server.seleniumProxy();
    }

    @BeforeMethod
    public void createNewHar() {
        server.newHar("google.com");
    }

    @Test
    public void testFirefoxDriver() throws Exception {
        System.setProperty(WEBDRIVER_GECKO_DRIVER, WEBDRIVER_GECKODRIVER_EXE_PATH);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, proxy);
        capabilities.setCapability("acceptInsecureCerts", true);

        WebDriver driver = new FirefoxDriver(capabilities);

        driver.get("http://google.com");
        driver.quit();
    }

    @Test
    public void testChromeDriver() throws Exception {
        System.setProperty(WEBDRIVER_CHROME_DRIVER, WEBDRIVER_CHROMDRIVER_EXE_PATH);
        ChromeOptions option = new ChromeOptions();
        option.addArguments("--proxy-server=localhost:"
                + server.getPort());
        WebDriver driver = new ChromeDriver(option);

        driver.get("http://google.com");
        driver.quit();
    }

    @AfterMethod
    public void saveHAR() throws Exception {
        Har har = server.getHar();

        for (HarEntry entry : har.getLog().getEntries()) {
            System.out.println(entry.getRequest().getUrl());
            // время ожидания ответа от сервера в миллисекундах
            System.out.println(entry.getTimings().getWait());
            // время чтения ответа от сервера в миллисекундах
            System.out.println(entry.getTimings().getReceive());
        }

        File file = new File("results/"
                + har.getLog().getBrowser().getName() + ".har");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        try {
            har.writeTo(fos);
        }
        finally {
            fos.close();
        }
    }

    @AfterClass
    public static void stopProxyServer() throws Exception {
        server.stop();
    }
}
