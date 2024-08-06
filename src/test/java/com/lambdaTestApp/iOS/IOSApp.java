package com.lambdaTestApp.iOS;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import okhttp3.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IOSApp {

    String userName = System.getenv("LT_USERNAME") == null ?
            "username" : System.getenv("LT_USERNAME"); //Add username here
    String accessKey = System.getenv("LT_ACCESS_KEY") == null ?
            "accessKey" : System.getenv("LT_ACCESS_KEY"); //Add accessKey here

    public String gridURL = "@mobile-hub.lambdatest.com/wd/hub";

    private final String credential = Credentials.basic(userName, accessKey);

    AppiumDriver driver;

    @BeforeSuite
    public void beforeSuite() throws IOException, InterruptedException {
        if (isAppPresent()){
            System.out.println("App already present, skipping app upload...");
        }else {
            System.out.println("App not present, uploading app...");
            //uploadApp();
            System.out.println("App upload successful");
            System.out.println("Waiting for tests to be initiated. It will take approx. 1 min for app to be configured to run with network logs");
            Thread.sleep(60000);
            System.out.println("Starting the test execution...");
        }
    }

    @Test
    @org.testng.annotations.Parameters(value = {"device", "version", "platform"})
    public void iOSApp1(String device, String version, String platform) {

        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            HashMap<String, Object> ltOptions = new HashMap<>();
            ltOptions.put("w3c", false);
            ltOptions.put("platformName", "ios");
            ltOptions.put("deviceName", "iPhone.*");
            ltOptions.put("platformVersion", "1.*");
            ltOptions.put("isRealMobile", true);
            ltOptions.put("app", "lt://APP1016016161718041499591119");
            capabilities.setCapability("lt:options", ltOptions);

            String hub = "https://mudassars:LoyMEMP9NWiHaeCpfBaGfu7c3hrjYfiksHEe6lSb3zJtqSGp3Z@mobile-hub.lambdatest.com/wd/hub";
            driver = new AppiumDriver<>(new URL(hub), capabilities);

            WebDriverWait Wait = new WebDriverWait(driver,30);

            Thread.sleep(30000);

            //Changes the color of the text
            Wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("color"))).click();
            Thread.sleep(1000);

            //Changes the text to "Proverbial"
            Wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Text"))).click();
            Thread.sleep(1000);

            //Toast will be visible
            Wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("toast"))).click();
            Thread.sleep(1000);

            //Notification will be visible
            Wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("notification"))).click();
            Thread.sleep(4000);

            //Opens the geolocation page
            Wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("geoLocation"))).click();
            Thread.sleep(4000);

            //Takes back
            driver.navigate().back();

            //Takes to speedtest page
            Wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("speedTest"))).click();
            Thread.sleep(4000);

            driver.navigate().back();

            //Opens the browser
            Wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Browser"))).click();
            Thread.sleep(1000);

            MobileElement url = (MobileElement) driver.findElementByAccessibilityId("url");
            url.click();
            url.sendKeys("https://www.lambdatest.com");

            Wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("find"))).click();
            Thread.sleep(1000);

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

    private void uploadApp() throws IOException, InterruptedException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("appFile", "apps/proverbial_ios.ipa",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File("apps/proverbial_ios.ipa")))
                .addFormDataPart("custom_id","proverbial-ios")
                .addFormDataPart("name","proverbial-ios")
                .build();
        Request request = new Request.Builder()
                .url("https://manual-api.lambdatest.com/app/upload/realDevice")
                .method("POST", body)
                .addHeader("Authorization", credential)
                .build();
        Response response = client.newCall(request).execute();
        Thread.sleep(15000);

    }

    private boolean isAppPresent() throws IOException {
        String jsonString = getResponseAsJson("ios");
        List<String> listOfApps = getAppIdsFromJson(jsonString);
        return listOfApps.contains("proverbial-ios");
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


