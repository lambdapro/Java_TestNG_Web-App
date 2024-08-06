
```
To run single test (Web Browser Automation) use below command
     mvn test -D suite=single.xml

To run single test (Mobile Browser Automation) use below command
     mvn test -D suite=mobile.xml
     
To run accesiility test (Web Browser Automation) use below command
     mvn test -D suite=accesibility.xml

To run parallel test (Web Browser Automation) use below command
     mvn test -D suite=parallel.xml

To run single test (Android App Automation) use below command
     mvn test -D suite=android_single.xml
    
To run single test (Android App Automation) use below command
     mvn test -D suite=android_parallel.xml    
 
To run single test (iOS App Automation) use below command
     mvn test -D suite=iOS_single.xml
    
To run single test (iOS App Automation) use below command
     mvn test -D suite=iOS_parallel.xml 

To run Cypress Test use below command
     npm test    
     
To run playwright Test - Single Thread    
     node playwright_single.js 
     
To run playwright Test - Parallel 3 Thread's
     node playwright_parallel.js   

```

### Visual UI Tests with Selenium

Navigate to https://smartui.lambdatest.com/projects

1. Run command ```   mvn test -D suite=visual_baseline.xml -Denv.project=<NAME_OF_THE_CUSTOMER> ``` 
2. run command ```  mvn test -D suite=visualchangebuild.xml -Denv.project=<NAME_OF_THE_CUSTOMER> ```

Runs will be reflected in the smartUI dashboard with the same project name as passed in maven commands((NAME_OF_THE_CUSTOMER) ) above 

### Run test with Hyperexecute


1. Run command ```   ./hyperexecute --config testng_hyperexecute_autosplit_sample.yaml --download-artifacts --force-clean-artifacts   ```
```./hyperexecute --config hyperexecute.yaml --download-artifacts --force-clean-artifacts -v```

Runs will be reflected in the Hyperexecute dashboard here https://hyperexecute.lambdatest.com/hyperexecute/jobs





