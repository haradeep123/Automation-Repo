package com.automation.base;

import com.automation.utils.ConfigReader;
import com.automation.utils.DriverManager;
import com.automation.utils.ExtentManager;
import com.aventstack.extentreports.ExtentTest;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;

/**
 * Base Test class containing common test setup and teardown
 * All test classes should extend this class
 */
public abstract class BaseTest {
    
    protected WebDriver driver;
    protected ExtentTest test;
    
    @BeforeClass
    public void classSetup() {
        ConfigReader.loadProperties();
        ExtentManager.initializeReport();
    }
    
    @BeforeMethod
    public void setUp() {
        String browser = ConfigReader.getProperty("browser");
        driver = DriverManager.getDriver(browser);
        
        String url = ConfigReader.getProperty("url");
        driver.get(url);
        
        // Initialize extent test
        test = ExtentManager.createTest(this.getClass().getSimpleName());
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            DriverManager.quitDriver();
        }
    }
    
    @AfterClass
    public void classTearDown() {
        ExtentManager.flushReport();
    }
    
    /**
     * Get current driver instance
     */
    public WebDriver getDriver() {
        return driver;
    }
    
    /**
     * Get current extent test instance
     */
    public ExtentTest getTest() {
        return test;
    }
}
