package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.SpiceJetPage;
import com.automation.utils.SpiceJetCalendarUtils;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.time.LocalDate;

/**
 * Test class for SpiceJet Calendar functionality using your custom locators
 */
public class SpiceJetCalendarTest extends BaseTest {
    
    private SpiceJetPage spiceJetPage;
    private SpiceJetCalendarUtils calendarUtils;
    
    @BeforeMethod
    public void setupTest() {
        // Navigate to SpiceJet
        driver.get("https://www.spicejet.com/");
        spiceJetPage = new SpiceJetPage(driver);
        calendarUtils = new SpiceJetCalendarUtils(driver);
        
        // Wait for page to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Test(priority = 1, description = "Test calendar opening with your locators")
    public void testCalendarOpening() {
        test.log(Status.INFO, "Testing calendar opening with custom locators");
        
        try {
            // Click departure date field to open calendar
            spiceJetPage.clickDepartureDateField();
            
            // Verify calendar is open
            Assert.assertTrue(calendarUtils.isCalendarOpen(), "Calendar should be open");
            test.log(Status.PASS, "Calendar opened successfully using your locators");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Failed to open calendar: " + e.getMessage());
            Assert.fail("Calendar opening test failed");
        }
    }
    
    @Test(priority = 2, description = "Test date selection using your calendar locators")
    public void testDateSelection() {
        test.log(Status.INFO, "Testing date selection with your calendar locators");
        
        try {
            // Select a specific date (next week)
            LocalDate nextWeek = LocalDate.now().plusDays(7);
            spiceJetPage.selectDepartureDate(nextWeek);
            
            test.log(Status.PASS, "Successfully selected date: " + nextWeek);
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Failed to select date: " + e.getMessage());
            Assert.fail("Date selection test failed");
        }
    }
    
    @Test(priority = 3, description = "Test calendar navigation using your arrows")
    public void testCalendarNavigation() {
        test.log(Status.INFO, "Testing calendar navigation with your arrow locators");
        
        try {
            // Open calendar
            spiceJetPage.clickDepartureDateField();
            
            // Navigate to next month using your locator
            spiceJetPage.navigateToNextMonth();
            test.log(Status.INFO, "Navigated to next month");
            
            // Navigate to previous month using your locator
            spiceJetPage.navigateToPreviousMonth();
            test.log(Status.INFO, "Navigated to previous month");
            
            test.log(Status.PASS, "Calendar navigation working with your locators");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Calendar navigation failed: " + e.getMessage());
            Assert.fail("Calendar navigation test failed");
        }
    }
    
    @Test(priority = 4, description = "Test round trip with calendar selection")
    public void testRoundTripCalendarSelection() {
        test.log(Status.INFO, "Testing round trip booking with calendar");
        
        try {
            // Select round trip
            spiceJetPage.selectRoundTrip();
            
            // Select departure date (1 week from now)
            LocalDate departureDate = LocalDate.now().plusDays(7);
            spiceJetPage.selectDepartureDate(departureDate);
            test.log(Status.INFO, "Selected departure date: " + departureDate);
            
            // Select return date (2 weeks from now)
            LocalDate returnDate = LocalDate.now().plusDays(14);
            spiceJetPage.selectReturnDate(returnDate);
            test.log(Status.INFO, "Selected return date: " + returnDate);
            
            test.log(Status.PASS, "Round trip calendar selection completed");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Round trip calendar selection failed: " + e.getMessage());
            Assert.fail("Round trip test failed");
        }
    }
    
    @Test(priority = 5, description = "Test date selection with day/month/year format")
    public void testSpecificDateSelection() {
        test.log(Status.INFO, "Testing specific date selection (day/month/year)");
        
        try {
            // Select specific date: 15th September 2025
            spiceJetPage.selectDepartureDate(15, "SEPTEMBER", 2025);
            test.log(Status.PASS, "Successfully selected 15th September 2025");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Specific date selection failed: " + e.getMessage());
            Assert.fail("Specific date selection test failed");
        }
    }
    
    @Test(priority = 6, description = "Test date selection with string format")
    public void testStringDateSelection() {
        test.log(Status.INFO, "Testing date selection with string format (dd-MM-yyyy)");
        
        try {
            // Select date using string format
            spiceJetPage.selectDepartureDate("25-12-2025");
            test.log(Status.PASS, "Successfully selected date using string format");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "String date selection failed: " + e.getMessage());
            Assert.fail("String date selection test failed");
        }
    }
    
    @Test(priority = 7, description = "Test relative date selection")
    public void testRelativeDateSelection() {
        test.log(Status.INFO, "Testing relative date selection");
        
        try {
            // Select date 10 days from today
            spiceJetPage.selectDepartureDateRelativeToToday(10);
            test.log(Status.PASS, "Successfully selected date 10 days from today");
            
            // For round trip, select return date 20 days from today
            spiceJetPage.selectRoundTrip();
            spiceJetPage.selectReturnDateRelativeToToday(20);
            test.log(Status.PASS, "Successfully selected return date 20 days from today");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Relative date selection failed: " + e.getMessage());
            Assert.fail("Relative date selection test failed");
        }
    }
    
    @Test(dataProvider = "dateTestData", priority = 8, 
          description = "Test multiple date combinations with your locators")
    public void testMultipleDateCombinations(int daysFromToday, String tripType) {
        test.log(Status.INFO, "Testing date combination: " + daysFromToday + " days, " + tripType);
        
        try {
            if ("roundtrip".equals(tripType)) {
                spiceJetPage.selectRoundTrip();
                
                LocalDate departureDate = LocalDate.now().plusDays(daysFromToday);
                LocalDate returnDate = departureDate.plusDays(7);
                
                spiceJetPage.selectDepartureDate(departureDate);
                spiceJetPage.selectReturnDate(returnDate);
                
                test.log(Status.PASS, "Round trip dates selected successfully");
            } else {
                spiceJetPage.selectOneWay();
                
                LocalDate departureDate = LocalDate.now().plusDays(daysFromToday);
                spiceJetPage.selectDepartureDate(departureDate);
                
                test.log(Status.PASS, "One way date selected successfully");
            }
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Multiple date combination test failed: " + e.getMessage());
            Assert.fail("Date combination test failed");
        }
    }
    
    @Test(priority = 9, description = "Test complete booking flow with your locators")
    public void testCompleteBookingFlow() {
        test.log(Status.INFO, "Testing complete booking flow with calendar");
        
        try {
            LocalDate departureDate = LocalDate.now().plusDays(5);
            LocalDate returnDate = LocalDate.now().plusDays(12);
            
            // Use the complete booking method
            spiceJetPage.completeBooking("roundtrip", departureDate, returnDate);
            
            test.log(Status.PASS, "Complete booking flow executed successfully");
            test.log(Status.INFO, "Departure: " + departureDate + ", Return: " + returnDate);
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Complete booking flow failed: " + e.getMessage());
            Assert.fail("Complete booking flow test failed");
        }
    }
    
    @Test(priority = 10, description = "Test calendar utilities directly")
    public void testCalendarUtilitiesDirectly() {
        test.log(Status.INFO, "Testing calendar utilities directly with your locators");
        
        try {
            // Open calendar using your locator
            spiceJetPage.openCalendarPicker();
            
            // Use calendar utils directly
            calendarUtils.selectToday();
            test.log(Status.PASS, "Selected today's date using calendar utils");
            
            // Test relative date selection
            spiceJetPage.openCalendarPicker();
            calendarUtils.selectDateRelativeToToday(15);
            test.log(Status.PASS, "Selected date 15 days from today using calendar utils");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Calendar utilities test failed: " + e.getMessage());
            Assert.fail("Calendar utilities test failed");
        }
    }
    
    @DataProvider(name = "dateTestData")
    public Object[][] getDateTestData() {
        return new Object[][] {
            {3, "oneway"},     // 3 days from today, one way
            {7, "roundtrip"},  // 1 week from today, round trip
            {14, "roundtrip"}, // 2 weeks from today, round trip
            {21, "oneway"},    // 3 weeks from today, one way
            {30, "roundtrip"}  // 1 month from today, round trip
        };
    }
}
