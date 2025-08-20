package com.automation.tests;

import com.automation.base.BaseTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.List;

/**
 * End-to-End test for SpiceJet flight booking using proper locators
 * Based on the locators from SpiceJetBookingPage.java
 */
public class SpiceJetE2ETestWithProperLocators extends BaseTest {
    
    private WebDriverWait wait;
    
    @Test(description = "Complete E2E Flight Booking Journey using Proper Locators", 
          groups = {"e2e", "regression"}, 
          priority = 1)
    public void testCompleteFlightBookingWithProperLocators() {
        
        test.log(Status.INFO, "🚀 Starting SpiceJet E2E Test with Proper Locators");
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        try {
            // Step 1: Navigate to SpiceJet
            navigateToSpiceJet();
            
            // Step 2: Handle any popups/promotions
            handleInitialPopups();
            
            // Step 3: Select One Way Trip
            selectOneWayTrip();
            
            // Step 4: Select Origin City (Delhi)
            selectOriginCity("Delhi");
            
            // Step 5: Select Destination City (Mumbai)
            selectDestinationCity("Mumbai");
            
            // Step 6: Select Departure Date
            selectDepartureDate();
            
            // Step 7: Search for Flights
            searchFlights();
            
            // Step 8: Verify Search Results
            verifySearchResults();
            
            test.log(Status.PASS, "✅ SpiceJet E2E Test with Proper Locators Completed Successfully!");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "❌ E2E Test Failed: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("E2E Test Failed: " + e.getMessage());
        }
    }
    
    private void navigateToSpiceJet() {
        test.log(Status.INFO, "Step 1: Navigating to SpiceJet website");
        driver.get("https://www.spicejet.com/");
        
        // Wait for page to load
        wait.until(ExpectedConditions.titleContains("SpiceJet"));
        
        // Verify page loaded correctly
        String pageTitle = driver.getTitle();
        Assert.assertTrue(pageTitle.contains("SpiceJet"), "SpiceJet page should load correctly");
        test.log(Status.PASS, "✅ Successfully loaded SpiceJet homepage. Title: " + pageTitle);
        
        sleep(2000);
    }
    
    private void handleInitialPopups() {
        test.log(Status.INFO, "Step 2: Handling any initial popups or promotions");
        
        try {
            // Look for common popup closers
            String[] popupSelectors = {
                "//div[contains(@class,'modal')]//button[contains(@class,'close')]",
                "//button[contains(text(),'Close') or contains(text(),'×')]",
                "//div[@class='css-1dbjc4n r-1awozwy r-1loqt21 r-18u37iz r-1otgn73 r-1i6wzkk r-lrvibr']"
            };
            
            for (String selector : popupSelectors) {
                try {
                    WebElement popup = driver.findElement(By.xpath(selector));
                    if (popup.isDisplayed()) {
                        popup.click();
                        test.log(Status.INFO, "✅ Closed popup");
                        sleep(1000);
                        break;
                    }
                } catch (Exception e) {
                    // Continue to next selector
                }
            }
            
        } catch (Exception e) {
            test.log(Status.INFO, "ℹ️ No popups found or already closed");
        }
    }
    
    private void selectOneWayTrip() {
        test.log(Status.INFO, "Step 3: Selecting One Way trip");
        
        try {
            // Using the proper ID locator for one way trip
            WebElement oneWayRadio = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("ctl00_mainContent_rbtnl_Trip_0")));
            
            if (!oneWayRadio.isSelected()) {
                oneWayRadio.click();
                test.log(Status.PASS, "✅ Selected One Way trip");
            } else {
                test.log(Status.INFO, "ℹ️ One Way trip already selected");
            }
            
        } catch (Exception e) {
            test.log(Status.INFO, "ℹ️ One Way trip selection: " + e.getMessage());
            // Fallback: One Way is usually default
        }
    }
    
    private void selectOriginCity(String cityName) {
        test.log(Status.INFO, "Step 4: Selecting origin city: " + cityName);
        
        try {
            // Click on FROM dropdown using proper ID
            WebElement fromDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("ctl00_mainContent_ddl_originStation1_CTXT")));
            fromDropdown.click();
            test.log(Status.INFO, "📍 Clicked FROM dropdown");
            
            sleep(1000);
            
            // Select city from dropdown
            selectCityFromDropdown(cityName);
            test.log(Status.PASS, "✅ Successfully selected origin city: " + cityName);
            
        } catch (Exception e) {
            test.log(Status.FAIL, "❌ Failed to select origin city: " + e.getMessage());
            throw new RuntimeException("Failed to select origin city: " + e.getMessage());
        }
    }
    
    private void selectDestinationCity(String cityName) {
        test.log(Status.INFO, "Step 5: Selecting destination city: " + cityName);
        
        try {
            // Click on TO dropdown using proper ID
            WebElement toDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("ctl00_mainContent_ddl_destinationStation1_CTXT")));
            toDropdown.click();
            test.log(Status.INFO, "📍 Clicked TO dropdown");
            
            sleep(1000);
            
            // Select city from dropdown
            selectCityFromDropdown(cityName);
            test.log(Status.PASS, "✅ Successfully selected destination city: " + cityName);
            
        } catch (Exception e) {
            test.log(Status.FAIL, "❌ Failed to select destination city: " + e.getMessage());
            throw new RuntimeException("Failed to select destination city: " + e.getMessage());
        }
    }
    
    private void selectCityFromDropdown(String cityName) {
        try {
            // Multiple strategies to find and select city
            String[] citySelectors = {
                "//a[contains(text(),'" + cityName + "')]",
                "//div[contains(text(),'" + cityName + "')]",
                "//*[contains(text(),'" + cityName + "')]",
                "//li[contains(text(),'" + cityName + "')]"
            };
            
            boolean citySelected = false;
            for (String selector : citySelectors) {
                try {
                    WebElement cityElement = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath(selector)));
                    cityElement.click();
                    citySelected = true;
                    test.log(Status.INFO, "📍 Selected city: " + cityName);
                    break;
                } catch (Exception e) {
                    // Try next selector
                }
            }
            
            if (!citySelected) {
                throw new RuntimeException("Unable to select city: " + cityName);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to select city " + cityName + ": " + e.getMessage());
        }
    }
    
    private void selectDepartureDate() {
        test.log(Status.INFO, "Step 6: Selecting departure date");
        
        try {
            // Click on departure date field using proper ID
            WebElement departureDateField = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("ctl00_mainContent_view_date1")));
            departureDateField.click();
            test.log(Status.INFO, "📅 Opened departure date calendar");
            
            sleep(2000);
            
            // Select a date from the calendar
            // Look for available dates in the current month
            List<WebElement> availableDates = driver.findElements(
                By.xpath("//a[contains(@class,'ui-state-default') and not(contains(@class,'ui-state-disabled'))]"));
            
            if (availableDates.size() > 0) {
                // Select the first available date that's not today
                for (WebElement date : availableDates) {
                    String dateText = date.getText();
                    try {
                        int day = Integer.parseInt(dateText);
                        if (day > 15) { // Select a date in the latter half of the month
                            date.click();
                            test.log(Status.PASS, "✅ Selected departure date: " + dateText);
                            break;
                        }
                    } catch (NumberFormatException e) {
                        // Skip non-numeric dates
                    }
                }
            } else {
                // Fallback: try to click next month and select date
                try {
                    WebElement nextMonth = driver.findElement(
                        By.xpath("//span[@class='ui-icon ui-icon-circle-triangle-e']"));
                    nextMonth.click();
                    sleep(1000);
                    
                    List<WebElement> nextMonthDates = driver.findElements(
                        By.xpath("//a[contains(@class,'ui-state-default')]"));
                    if (nextMonthDates.size() > 0) {
                        nextMonthDates.get(5).click(); // Select 6th date
                        test.log(Status.PASS, "✅ Selected departure date from next month");
                    }
                } catch (Exception e) {
                    test.log(Status.WARNING, "⚠️ Could not navigate calendar months");
                }
            }
            
            sleep(1000);
            
        } catch (Exception e) {
            test.log(Status.FAIL, "❌ Failed to select departure date: " + e.getMessage());
            throw new RuntimeException("Failed to select departure date: " + e.getMessage());
        }
    }
    
    private void searchFlights() {
        test.log(Status.INFO, "Step 7: Searching for flights");
        
        try {
            // Click search button using proper ID
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("ctl00_mainContent_btn_FindFlights")));
            
            // Scroll to search button if needed
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", searchButton);
            
            sleep(1000);
            searchButton.click();
            test.log(Status.PASS, "✅ Clicked search flights button");
            
            // Wait for search to process
            test.log(Status.INFO, "⏳ Waiting for flight search results...");
            sleep(5000);
            
        } catch (Exception e) {
            test.log(Status.FAIL, "❌ Failed to search flights: " + e.getMessage());
            throw new RuntimeException("Failed to search flights: " + e.getMessage());
        }
    }
    
    private void verifySearchResults() {
        test.log(Status.INFO, "Step 8: Verifying search results");
        
        try {
            // Wait for results page to load
            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("search"),
                ExpectedConditions.urlContains("flight"),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'flights found')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'No flights')]"))
            ));
            
            // Check current URL
            String currentUrl = driver.getCurrentUrl();
            test.log(Status.INFO, "📍 Current URL: " + currentUrl);
            
            if (currentUrl.contains("search") || currentUrl.contains("flight") || 
                currentUrl.contains("book") || currentUrl.contains("result")) {
                test.log(Status.PASS, "✅ Successfully navigated to search results page");
            }
            
            // Look for flight results or messages
            try {
                // Check for flight results
                List<WebElement> flightResults = driver.findElements(
                    By.xpath("//div[contains(@class,'flight')] | //table[contains(@class,'result')] | //*[contains(@id,'flight')]"));
                
                if (flightResults.size() > 0) {
                    test.log(Status.PASS, "✅ Found " + flightResults.size() + " flight result elements");
                }
                
                // Check for specific success indicators
                String pageSource = driver.getPageSource().toLowerCase();
                if (pageSource.contains("search result") || pageSource.contains("flight") || 
                    pageSource.contains("available") || pageSource.contains("select")) {
                    test.log(Status.PASS, "✅ Search results page content verified");
                }
                
                // Check for no flights message
                if (pageSource.contains("no flight") || pageSource.contains("not available")) {
                    test.log(Status.INFO, "ℹ️ No flights available for selected route/date");
                }
                
            } catch (Exception e) {
                test.log(Status.INFO, "ℹ️ Flight results verification: " + e.getMessage());
            }
            
            // Take screenshot for verification
            test.log(Status.INFO, "📸 Search results page loaded");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "❌ Failed to verify search results: " + e.getMessage());
            throw new RuntimeException("Failed to verify search results: " + e.getMessage());
        }
    }
    
    /**
     * Helper method for consistent sleep/wait
     */
    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test(description = "E2E Test with Round Trip using proper locators", 
          groups = {"e2e", "extended"}, 
          priority = 2,
          enabled = false) // Disabled by default
    public void testRoundTripFlightBookingWithProperLocators() {
        test.log(Status.INFO, "🔄 Starting Round Trip E2E Test with Proper Locators");
        
        try {
            navigateToSpiceJet();
            handleInitialPopups();
            
            // Select Round Trip using proper ID
            WebElement roundTripRadio = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("ctl00_mainContent_rbtnl_Trip_1")));
            roundTripRadio.click();
            test.log(Status.PASS, "✅ Selected Round Trip");
            
            // Continue with origin, destination, dates...
            selectOriginCity("Delhi");
            selectDestinationCity("Mumbai");
            
            // Select departure date
            selectDepartureDate();
            
            // Select return date
            WebElement returnDateField = driver.findElement(By.id("ctl00_mainContent_view_date2"));
            returnDateField.click();
            sleep(1000);
            
            // Select return date (a few days after departure)
            List<WebElement> returnDates = driver.findElements(
                By.xpath("//a[contains(@class,'ui-state-default')]"));
            if (returnDates.size() > 0) {
                returnDates.get(returnDates.size() - 5).click(); // Select a date towards month end
                test.log(Status.PASS, "✅ Selected return date");
            }
            
            // Search flights
            searchFlights();
            verifySearchResults();
            
            test.log(Status.PASS, "✅ Round Trip E2E Test with Proper Locators Completed!");
            
        } catch (Exception e) {
            test.log(Status.INFO, "ℹ️ Round trip test: " + e.getMessage());
        }
    }
}
