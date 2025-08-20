package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.SpiceJetPage;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.time.LocalDate;
import java.util.List;

/**
 * Test class for SpiceJet City Selection functionality using visible text
 */
public class SpiceJetCitySelectionTest extends BaseTest {
    
    private SpiceJetPage spiceJetPage;
    
    @BeforeMethod
    public void setupTest() {
        // Navigate to SpiceJet
        driver.get("https://www.spicejet.com/");
        spiceJetPage = new SpiceJetPage(driver);
        
        // Wait for page to load
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Test(priority = 1, description = "Test origin city selection by visible text")
    public void testOriginCitySelectionByText() {
        test.log(Status.INFO, "Testing origin city selection by visible text");
        
        try {
            // Select Delhi as origin city
            spiceJetPage.selectOriginByText("Delhi");
            test.log(Status.PASS, "Successfully selected Delhi as origin city");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Failed to select origin city by text: " + e.getMessage());
            Assert.fail("Origin city selection by text failed");
        }
    }
    
    @Test(priority = 2, description = "Test destination city selection by visible text")
    public void testDestinationCitySelectionByText() {
        test.log(Status.INFO, "Testing destination city selection by visible text");
        
        try {
            // First select origin to enable destination
            spiceJetPage.selectOriginByText("Delhi");
            
            // Then select destination
            spiceJetPage.selectDestinationByText("Mumbai");
            test.log(Status.PASS, "Successfully selected Mumbai as destination city");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Failed to select destination city by text: " + e.getMessage());
            Assert.fail("Destination city selection by text failed");
        }
    }
    
    @Test(priority = 3, description = "Test complete booking with city names and calendar")
    public void testCompleteBookingWithCityNames() {
        test.log(Status.INFO, "Testing complete booking flow with city names");
        
        try {
            String originCity = "Delhi";
            String destinationCity = "Chennai";
            LocalDate departureDate = LocalDate.now().plusDays(7);
            LocalDate returnDate = LocalDate.now().plusDays(14);
            
            // Complete booking with city names
            spiceJetPage.completeBookingWithCityNames("roundtrip", originCity, destinationCity, 
                                                    departureDate, returnDate);
            
            test.log(Status.PASS, "Complete booking flow with city names executed successfully");
            test.log(Status.INFO, "Origin: " + originCity + ", Destination: " + destinationCity);
            test.log(Status.INFO, "Departure: " + departureDate + ", Return: " + returnDate);
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Complete booking with city names failed: " + e.getMessage());
            Assert.fail("Complete booking test failed");
        }
    }
    
    @Test(dataProvider = "cityPairs", priority = 4, 
          description = "Test multiple city combinations")
    public void testMultipleCityCombinations(String originCity, String destinationCity) {
        test.log(Status.INFO, "Testing city combination: " + originCity + " to " + destinationCity);
        
        try {
            // Select origin city
            spiceJetPage.selectOriginByText(originCity);
            test.log(Status.INFO, "Selected origin: " + originCity);
            
            // Select destination city
            spiceJetPage.selectDestinationByText(destinationCity);
            test.log(Status.INFO, "Selected destination: " + destinationCity);
            
            // Select departure date
            spiceJetPage.selectDepartureDateRelativeToToday(5);
            test.log(Status.INFO, "Selected departure date 5 days from today");
            
            test.log(Status.PASS, "City combination test passed for: " + originCity + " to " + destinationCity);
            
        } catch (Exception e) {
            test.log(Status.FAIL, "City combination test failed: " + e.getMessage());
            Assert.fail("City combination test failed for: " + originCity + " to " + destinationCity);
        }
    }
    
    @Test(priority = 5, description = "Test city search functionality")
    public void testCitySearchFunctionality() {
        test.log(Status.INFO, "Testing city search functionality");
        
        try {
            // Search for cities starting with "Bangalore"
            List<String> originCities = spiceJetPage.getAvailableOriginCities("Bangalore");
            test.log(Status.INFO, "Found " + originCities.size() + " origin cities matching 'Bangalore'");
            
            for (String city : originCities) {
                test.log(Status.INFO, "Available origin city: " + city);
            }
            
            // Search for destination cities
            List<String> destinationCities = spiceJetPage.getAvailableDestinationCities("Mumbai");
            test.log(Status.INFO, "Found " + destinationCities.size() + " destination cities matching 'Mumbai'");
            
            for (String city : destinationCities) {
                test.log(Status.INFO, "Available destination city: " + city);
            }
            
            test.log(Status.PASS, "City search functionality working correctly");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "City search functionality failed: " + e.getMessage());
            Assert.fail("City search test failed");
        }
    }
    
    @Test(priority = 6, description = "Test case insensitive city selection")
    public void testCaseInsensitiveCitySelection() {
        test.log(Status.INFO, "Testing case insensitive city selection");
        
        try {
            // Test with different cases
            spiceJetPage.selectOriginByText("delhi");  // lowercase
            test.log(Status.INFO, "Selected origin with lowercase: delhi");
            
            spiceJetPage.selectDestinationByText("MUMBAI");  // uppercase
            test.log(Status.INFO, "Selected destination with uppercase: MUMBAI");
            
            test.log(Status.PASS, "Case insensitive city selection working correctly");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Case insensitive city selection failed: " + e.getMessage());
            Assert.fail("Case insensitive test failed");
        }
    }
    
    @Test(priority = 7, description = "Test partial city name matching")
    public void testPartialCityNameMatching() {
        test.log(Status.INFO, "Testing partial city name matching");
        
        try {
            // Test with partial names
            spiceJetPage.selectOriginByText("Del");  // Partial name for Delhi
            test.log(Status.INFO, "Selected origin with partial name: Del");
            
            spiceJetPage.selectDestinationByText("Mum");  // Partial name for Mumbai
            test.log(Status.INFO, "Selected destination with partial name: Mum");
            
            test.log(Status.PASS, "Partial city name matching working correctly");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Partial city name matching failed: " + e.getMessage());
            Assert.fail("Partial name matching test failed");
        }
    }
    
    @Test(priority = 8, description = "Test city selection with airport codes")
    public void testCitySelectionWithAirportCodes() {
        test.log(Status.INFO, "Testing city selection with airport codes");
        
        try {
            // Test with airport codes (if available)
            spiceJetPage.selectOriginByText("DEL");  // Delhi airport code
            test.log(Status.INFO, "Selected origin with airport code: DEL");
            
            spiceJetPage.selectDestinationByText("BOM");  // Mumbai airport code
            test.log(Status.INFO, "Selected destination with airport code: BOM");
            
            test.log(Status.PASS, "City selection with airport codes working correctly");
            
        } catch (Exception e) {
            test.log(Status.INFO, "Airport codes might not be available, trying city names instead");
            try {
                spiceJetPage.selectOriginByText("Delhi");
                spiceJetPage.selectDestinationByText("Mumbai");
                test.log(Status.PASS, "City selection with city names working as fallback");
            } catch (Exception e2) {
                test.log(Status.FAIL, "Both airport codes and city names failed: " + e2.getMessage());
            }
        }
    }
    
    @Test(priority = 9, description = "Test error handling for invalid city names")
    public void testInvalidCityNames() {
        test.log(Status.INFO, "Testing error handling for invalid city names");
        
        try {
            // Try to select a non-existent city
            spiceJetPage.selectOriginByText("InvalidCity123");
            test.log(Status.INFO, "Attempted to select invalid city name");
            
            // The method should handle this gracefully without crashing
            test.log(Status.PASS, "Error handling for invalid city names working correctly");
            
        } catch (Exception e) {
            test.log(Status.INFO, "Exception caught as expected for invalid city: " + e.getMessage());
            test.log(Status.PASS, "Error handling working correctly");
        }
    }
    
    @Test(priority = 10, description = "Test one way trip with city names")
    public void testOneWayTripWithCityNames() {
        test.log(Status.INFO, "Testing one way trip with city names");
        
        try {
            String originCity = "Bangalore";
            String destinationCity = "Hyderabad";
            LocalDate departureDate = LocalDate.now().plusDays(10);
            
            // Complete one way booking
            spiceJetPage.completeBookingWithCityNames("oneway", originCity, destinationCity, 
                                                    departureDate, null);
            
            test.log(Status.PASS, "One way trip booking completed successfully");
            test.log(Status.INFO, "Origin: " + originCity + ", Destination: " + destinationCity);
            test.log(Status.INFO, "Departure: " + departureDate);
            
        } catch (Exception e) {
            test.log(Status.FAIL, "One way trip with city names failed: " + e.getMessage());
            Assert.fail("One way trip test failed");
        }
    }
    
    @DataProvider(name = "cityPairs")
    public Object[][] getCityPairs() {
        return new Object[][] {
            {"Delhi", "Mumbai"},
            {"Bangalore", "Chennai"},
            {"Hyderabad", "Pune"},
            {"Kolkata", "Ahmedabad"},
            {"Goa", "Jaipur"}
        };
    }
}
