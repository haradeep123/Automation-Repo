package com.automation.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling dynamic city selection based on visible text
 */
public class CitySelectionUtils {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    public CitySelectionUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    /**
     * Find and click city by visible text using multiple strategies
     * @param cityName - city name to find
     * @param containerXpath - container xpath to search within (optional)
     */
    public void selectCityByText(String cityName, String containerXpath) {
        WebElement cityElement = findCityElementByText(cityName, containerXpath);
        
        if (cityElement != null) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(cityElement));
                cityElement.click();
            } catch (Exception e) {
                throw new RuntimeException("Found city element but could not click: " + cityName, e);
            }
        } else {
            throw new RuntimeException("Could not find city: " + cityName);
        }
    }
    
    /**
     * Find city element by text using multiple strategies
     * @param cityName - city name to find
     * @param containerXpath - container xpath to limit search scope
     * @return WebElement of the city or null if not found
     */
    public WebElement findCityElementByText(String cityName, String containerXpath) {
        String searchContainer = (containerXpath != null && !containerXpath.isEmpty()) ? containerXpath : "";
        
        // Strategy 1: Exact text match
        try {
            String xpath = searchContainer + "//*[text()='" + cityName + "']";
            return driver.findElement(By.xpath(xpath));
        } catch (Exception e) {
            // Continue to next strategy
        }
        
        // Strategy 2: Contains text match
        try {
            String xpath = searchContainer + "//*[contains(text(),'" + cityName + "')]";
            return driver.findElement(By.xpath(xpath));
        } catch (Exception e) {
            // Continue to next strategy
        }
        
        // Strategy 3: Case insensitive match
        try {
            String xpath = searchContainer + "//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + cityName.toLowerCase() + "')]";
            return driver.findElement(By.xpath(xpath));
        } catch (Exception e) {
            // Continue to next strategy
        }
        
        // Strategy 4: Starts with match
        try {
            String xpath = searchContainer + "//*[starts-with(text(),'" + cityName + "')]";
            return driver.findElement(By.xpath(xpath));
        } catch (Exception e) {
            // Continue to next strategy
        }
        
        // Strategy 5: Partial match (first 3 characters)
        if (cityName.length() >= 3) {
            try {
                String partialName = cityName.substring(0, 3);
                String xpath = searchContainer + "//*[contains(text(),'" + partialName + "')]";
                return driver.findElement(By.xpath(xpath));
            } catch (Exception e) {
                // Continue to next strategy
            }
        }
        
        // Strategy 6: Search by aria-label or title attributes
        try {
            String xpath = searchContainer + "//*[@aria-label='" + cityName + "' or @title='" + cityName + "' or contains(@aria-label,'" + cityName + "') or contains(@title,'" + cityName + "')]";
            return driver.findElement(By.xpath(xpath));
        } catch (Exception e) {
            // Continue to next strategy
        }
        
        // Strategy 7: Search by data attributes (common in modern web apps)
        try {
            String xpath = searchContainer + "//*[@data-city='" + cityName + "' or @data-name='" + cityName + "' or contains(@data-city,'" + cityName + "') or contains(@data-name,'" + cityName + "')]";
            return driver.findElement(By.xpath(xpath));
        } catch (Exception e) {
            // Continue to next strategy
        }
        
        // Strategy 8: Search within clickable elements only
        try {
            String xpath = searchContainer + "//a[contains(text(),'" + cityName + "')] | " + searchContainer + "//button[contains(text(),'" + cityName + "')] | " + searchContainer + "//div[@role='option' and contains(text(),'" + cityName + "')]";
            return driver.findElement(By.xpath(xpath));
        } catch (Exception e) {
            // Continue to next strategy
        }
        
        return null; // Return null if no strategy worked
    }
    
    /**
     * Get all available cities from a dropdown
     * @param containerXpath - container xpath to search within
     * @return list of city names
     */
    public List<String> getAllAvailableCities(String containerXpath) {
        List<String> cities = new ArrayList<>();
        
        try {
            // Find all text elements within the container
            String xpath = containerXpath + "//*[text() and string-length(text()) > 2]";
            List<WebElement> elements = driver.findElements(By.xpath(xpath));
            
            for (WebElement element : elements) {
                String text = element.getText().trim();
                if (!text.isEmpty() && text.length() > 2) {
                    // Filter out common UI elements
                    if (!isUIElement(text)) {
                        cities.add(text);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to get available cities: " + e.getMessage());
        }
        
        return cities;
    }
    
    /**
     * Search for cities matching a pattern
     * @param searchPattern - pattern to search for
     * @param containerXpath - container xpath to search within
     * @return list of matching city names
     */
    public List<String> searchCities(String searchPattern, String containerXpath) {
        List<String> matchingCities = new ArrayList<>();
        
        try {
            String xpath = containerXpath + "//*[contains(text(),'" + searchPattern + "')]";
            List<WebElement> elements = driver.findElements(By.xpath(xpath));
            
            for (WebElement element : elements) {
                String text = element.getText().trim();
                if (!text.isEmpty() && !isUIElement(text)) {
                    matchingCities.add(text);
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to search cities: " + e.getMessage());
        }
        
        return matchingCities;
    }
    
    /**
     * Check if a city is available in the dropdown
     * @param cityName - city name to check
     * @param containerXpath - container xpath to search within
     * @return true if city is available
     */
    public boolean isCityAvailable(String cityName, String containerXpath) {
        return findCityElementByText(cityName, containerXpath) != null;
    }
    
    /**
     * Get the closest matching city name
     * @param inputCity - input city name
     * @param containerXpath - container xpath to search within
     * @return closest matching city name or null
     */
    public String getClosestMatchingCity(String inputCity, String containerXpath) {
        List<String> availableCities = getAllAvailableCities(containerXpath);
        
        // Exact match
        for (String city : availableCities) {
            if (city.equalsIgnoreCase(inputCity)) {
                return city;
            }
        }
        
        // Contains match
        for (String city : availableCities) {
            if (city.toLowerCase().contains(inputCity.toLowerCase())) {
                return city;
            }
        }
        
        // Starts with match
        for (String city : availableCities) {
            if (city.toLowerCase().startsWith(inputCity.toLowerCase())) {
                return city;
            }
        }
        
        return null; // No match found
    }
    
    /**
     * Select city with retry mechanism
     * @param cityName - city name to select
     * @param containerXpath - container xpath
     * @param maxRetries - maximum number of retries
     */
    public void selectCityWithRetry(String cityName, String containerXpath, int maxRetries) {
        Exception lastException = null;
        
        for (int i = 0; i < maxRetries; i++) {
            try {
                selectCityByText(cityName, containerXpath);
                return; // Success, exit method
            } catch (Exception e) {
                lastException = e;
                System.out.println("Attempt " + (i + 1) + " failed for city: " + cityName + " - " + e.getMessage());
                
                // Wait before retry
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        
        // All retries failed
        throw new RuntimeException("Failed to select city after " + maxRetries + " attempts: " + cityName, lastException);
    }
    
    /**
     * Check if text represents a UI element rather than a city
     * @param text - text to check
     * @return true if it's a UI element
     */
    private boolean isUIElement(String text) {
        String lowerText = text.toLowerCase();
        
        // Common UI elements to filter out
        String[] uiElements = {
            "select", "choose", "search", "from", "to", "departure", "arrival",
            "date", "calendar", "book", "flight", "passenger", "adult", "child",
            "infant", "class", "economy", "business", "first", "submit", "next",
            "previous", "close", "cancel", "ok", "yes", "no", "apply", "clear"
        };
        
        for (String uiElement : uiElements) {
            if (lowerText.equals(uiElement) || lowerText.contains(uiElement)) {
                return true;
            }
        }
        
        // Check for numbers only
        if (text.matches("\\d+")) {
            return true;
        }
        
        // Check for single characters
        if (text.length() == 1) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Wait for dropdown to be populated with cities
     * @param containerXpath - container xpath
     * @param minCityCount - minimum expected city count
     * @param timeoutSeconds - timeout in seconds
     */
    public void waitForCitiesToLoad(String containerXpath, int minCityCount, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeoutMillis = timeoutSeconds * 1000;
        
        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            List<String> cities = getAllAvailableCities(containerXpath);
            if (cities.size() >= minCityCount) {
                return; // Cities loaded successfully
            }
            
            try {
                Thread.sleep(500); // Wait 500ms before next check
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        throw new RuntimeException("Cities did not load within " + timeoutSeconds + " seconds");
    }
    
    /**
     * Select city by airport code if available, fallback to city name
     * @param cityName - city name
     * @param airportCode - airport code (e.g., "DEL", "BOM")
     * @param containerXpath - container xpath
     */
    public void selectCityByCodeOrName(String cityName, String airportCode, String containerXpath) {
        try {
            // Try airport code first
            selectCityByText(airportCode, containerXpath);
        } catch (Exception e) {
            // Fallback to city name
            selectCityByText(cityName, containerXpath);
        }
    }
}
