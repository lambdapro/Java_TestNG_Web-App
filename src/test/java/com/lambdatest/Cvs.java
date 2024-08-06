package com.lambdatest;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.lambdatest.*;


import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

public class Cvs {

    private RemoteWebDriver driver;
    private String Status = "failed";

    @BeforeMethod
    public void setup(Method m, ITestContext ctx) throws MalformedURLException {
        String username = System.getenv("LT_USERNAME") == null ? "Your LT Username" : System.getenv("LT_USERNAME");
        String authkey = System.getenv("LT_ACCESS_KEY") == null ? "Your LT AccessKey" : System.getenv("LT_ACCESS_KEY");
        ;
        String hub = "@hub.lambdatest.com/wd/hub";

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browserName", "Chrome");
        caps.setCapability("version", "latest");
        caps.setCapability("build", "TestNG With Java");
        caps.setCapability("name", m.getName() + this.getClass().getName());
        caps.setCapability("plugin", "git-testng");
        caps.setCapability("performance",true);
        caps.setCapability("network", true);
        caps.setCapability("console", true);
        caps.setCapability("video", true);
        caps.setCapability("selenium_version", "3.13.0");
        caps.setCapability("w3c", false);
        caps.setCapability("geoLocation", "CA");



        String[] Tags = new String[] { "Feature", "Tag", "Moderate" };
        caps.setCapability("tags", Tags);

        driver = new RemoteWebDriver(new URL("http://" + username + ":" + authkey + hub), caps);
    }


    @Test
    public void basicTest() throws Exception {
        String spanText;
        System.out.println("Loading Url");

        driver.get("http://cvs.com/minuteclinic/");
        Thread.sleep(5000);

        SmartUISnapshot.smartuiSnapshot(driver, "home");

        Thread.sleep(2000);




        System.out.println("TestFinished");

    }

    @AfterMethod
    public void tearDown() {
        driver.executeScript("lambda-status=" + Status);
        driver.quit();
    }

}
