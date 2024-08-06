package com.lambdaTestApp.android;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import okhttp3.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AndroidApp {

     String userName = System.getenv("LT_USERNAME") == null ? "Your LT Username" : System.getenv("LT_USERNAME");
     String accessKey = System.getenv("LT_ACCESS_KEY") == null ? "Your LT AccessKey" : System.getenv("LT_ACCESS_KEY");

    String credential = Credentials.basic(userName, accessKey);
    public String gridURL = "@mobile-hub.lambdatest.com/wd/hub";

    AppiumDriver driver;

    @BeforeSuite
    public void beforeSuite() throws IOException, InterruptedException {
        if (isAppPresent()){
            System.out.println("App already present, skipping app upload...");
        }else {
            System.out.println("App not present, uploading app...");
            uploadApp();
            System.out.println("App upload successful");
            System.out.println("Waiting for tests to be initiated. It will take approx. 1 min for app to be configured to run with network logs");
            Thread.sleep(20000);
            System.out.println("Starting the test execution...");
        }
    }

    @Test
    @org.testng.annotations.Parameters(value = {"device", "version", "platform"})
    public void AndroidApp1(String device, @Optional String version, String platform) {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("build","Java TestNG Android");
            capabilities.setCapability("name",platform+" "+device+" "+version);
            capabilities.setCapability("deviceName", device);
            capabilities.setCapability("platformVersion",version);
            capabilities.setCapability("platformName", platform);
            capabilities.setCapability("isRealMobile", true);
            //AppURL (Create from Wikipedia.apk sample in project)
            capabilities.setCapability("app", "proverbial-android"); //Enter your app url
            capabilities.setCapability("deviceOrientation", "PORTRAIT");
            capabilities.setCapability("console", true);
            capabilities.setCapability("network", true);
            capabilities.setCapability("visual", true);
            capabilities.setCapability("devicelog", true);
            capabilities.setCapability("appProfiling", true);
            capabilities.setCapability("appiumVersion", "2.0");
            //capabilities.setCapability("geoLocation", "HK");

            String hub = "https://" + userName + ":" + accessKey + gridURL;
            driver = new AppiumDriver(new URL(hub), capabilities);

            MobileElement color = (MobileElement) driver.findElementById("com.lambdatest.proverbial:id/color");
            //Changes color to pink

            //AndroidDriver androidDriver = <AndroidDriver> driver;

            color.click();
            Thread.sleep(1000);
            //Back to orginal color
            color.click();



            MobileElement text = (MobileElement) driver.findElementById("com.lambdatest.proverbial:id/Text");
            //Changes the text to "Proverbial"
            text.click();

            //toast will be visible
            MobileElement toast = (MobileElement) driver.findElementById("com.lambdatest.proverbial:id/toast");
            toast.click();


            //notification will be visible
            MobileElement notification = (MobileElement) driver.findElementById("com.lambdatest.proverbial:id/notification");
            notification.click();
            Thread.sleep(2000);

            //Opens the geolocation page
            MobileElement geo = (MobileElement) driver.findElementById("com.lambdatest.proverbial:id/geoLocation");
            geo.click();
            Thread.sleep(5000);

            //takes back to home page
            MobileElement home = (MobileElement) driver.findElementByAccessibilityId("Home");
            home.click();

            //Takes to speed test page
            MobileElement speedtest = (MobileElement) driver.findElementById("com.lambdatest.proverbial:id/speedTest");
            speedtest.click();
            Thread.sleep(5000);

            MobileElement Home = (MobileElement) driver.findElementByAccessibilityId("Home");
            Home.click();

            //Opens the browser
            MobileElement browser = (MobileElement) driver.findElementByAccessibilityId("Browser");
            browser.click();

            MobileElement url = (MobileElement) driver.findElementById("com.lambdatest.proverbial:id/url");
            url.sendKeys("https://www.lambdatest.com");

            MobileElement find = (MobileElement) driver.findElementById("com.lambdatest.proverbial:id/find");
            find.click();

            driver.quit();

        } catch (Exception e) {
            e.printStackTrace();
            try{
                driver.quit();
            }catch(Exception e1){
                e.printStackTrace();
            }
        }


    }

    private  void uploadApp() throws IOException, InterruptedException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("appFile","apps/proverbial_android.apk",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File("apps/proverbial_android.apk")))
                .addFormDataPart("custom_id","proverbial-android")
                .addFormDataPart("name","proverbial-android")
                .build();
        Request request = new Request.Builder()
                .url("https://manual-api.lambdatest.com/app/upload/realDevice")
                .method("POST", body)
                .addHeader("Authorization", credential)
                .build();
        Response response = client.newCall(request).execute();
        //System.out.println(response.body().string());
    }

    private boolean isAppPresent() throws IOException {
        String jsonString = getResponseAsJson("android");
        List<String> listOfApps = getAppIdsFromJson(jsonString);
        return listOfApps.contains("proverbial-android");
    }
    private String getResponseAsJson(String platform) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://manual-api.lambdatest.com/app/data?type="+platform+"&level=user")
                .method("GET",null)
                .addHeader("Authorization", credential)
                .build();


        Response response = client.newCall(request).execute();
        assert response.body() != null;
        return response.body().string();
    }

    private List<String> getAppIdsFromJson(String jsonData) {
        List<String> namesList = new ArrayList<String>();
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
            JSONArray dataArray = (JSONArray) jsonObject.get("data");
            for (Object data : dataArray) {
                JSONObject dataObject = (JSONObject) data;
                String name = (String) dataObject.get("name");
               namesList.add(name);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return namesList;
    }

}

