package com.epam.proxy;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarRequest;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseTest {

    static BrowserMobProxy mobProxy;
    static Proxy proxy;

    static WebDriver driver;

    @BeforeClass
    public static void setUpProxy() {
        mobProxy = new BrowserMobProxyServer();
        mobProxy.start(0);
        mobProxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

        mobProxy.addResponseFilter((response, contents, messageInfo) -> {
            if (contents.isText()) {
                contents.setTextContents("Hello");
            }
        });

        proxy = ClientUtil.createSeleniumProxy(mobProxy);
    }

    @BeforeMethod
    public void createNewHar() {
        mobProxy.newHar("google.com");
    }

    @AfterMethod
    public void saveHAR() throws IOException {
        Har har = mobProxy.getHar();

        for (HarEntry entry : har.getLog().getEntries()) {
            System.out.println(entry.getRequest().getUrl());
            System.out.println(entry.getTimings().getWait());
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
        } finally {
            fos.close();
        }
        driver.quit();

        getRequests("*.google.com.*")
                .stream()
                .map(HarRequest::getUrl)
                .forEach(System.out::println);
    }

    @AfterClass
    public static void stopProxyServer() {
        mobProxy.stop();
    }

    private List<HarRequest> getRequests(String urlPattern) {
        Har har = mobProxy.getHar();

        return har.getLog().getEntries()
                .stream()
                .map(HarEntry::getRequest)
                .filter(harRequest -> harRequest.getUrl().matches(urlPattern)).collect(Collectors.toList());
    }

}
