package com.automation.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;

/**
 * Driver Manager class to handle WebDriver initialization and management
 */
public class DriverManager {
    
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    
    /**
     * Get WebDriver instance for specified browser
     */
    public static WebDriver getDriver(String browserName) {
        if (driver.get() == null) {
            createDriver(browserName);
        }
        return driver.get();
    }
    
    /**
     * Create WebDriver instance based on browser name
     */
    private static void createDriver(String browserName) {
        WebDriver webDriver = null;
        
        // Check if running in headless mode (for CI/CD)
        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        
        switch (browserName.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                
                // Basic Chrome options
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--remote-allow-origins=*");
                
                if (isHeadless) {
                    // CI/CD headless mode
                    chromeOptions.addArguments("--headless");
                    chromeOptions.addArguments("--window-size=1920,1080");
                    chromeOptions.addArguments("--disable-extensions");
                    chromeOptions.addArguments("--disable-web-security");
                    chromeOptions.addArguments("--allow-running-insecure-content");
                    System.out.println("Running Chrome in headless mode for CI/CD");
                } else {
                    // Local development mode
                    chromeOptions.addArguments("--start-maximized");
                    System.out.println("Running Chrome in normal mode");
                }
                
                webDriver = new ChromeDriver(chromeOptions);
                break;
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                
                if (isHeadless) {
                    // CI/CD headless mode
                    firefoxOptions.addArguments("--headless");
                    firefoxOptions.addArguments("--width=1920");
                    firefoxOptions.addArguments("--height=1080");
                    System.out.println("Running Firefox in headless mode for CI/CD");
                } else {
                    // Local development mode
                    firefoxOptions.addArguments("--start-maximized");
                    System.out.println("Running Firefox in normal mode");
                }
                
                webDriver = new FirefoxDriver(firefoxOptions);
                break;
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                webDriver = new EdgeDriver(edgeOptions);
                break;
                
            case "safari":
                webDriver = new SafariDriver();
                break;
                
            default:
                throw new IllegalArgumentException("Browser not supported: " + browserName);
        }
        
        driver.set(webDriver);
    }
    
    /**
     * Quit the current WebDriver instance
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
    
    /**
     * Get current WebDriver instance
     */
    public static WebDriver getCurrentDriver() {
        return driver.get();
    }
}
