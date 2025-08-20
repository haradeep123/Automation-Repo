package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.SpiceJetBookingPage;
import com.automation.utils.CalendarUtils;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.time.LocalDate;

/**
 * Calendar Test class for testing date selection functionality
 */
public class CalendarTest extends BaseTest {
    
    private SpiceJetBookingPage bookingPage;
    private CalendarUtils calendarUtils;
    
    @BeforeMethod
    public void setupTest() {
        // Navigate to SpiceJet (update URL in config.properties)
        driver.get("https://www.spicejet.com/");
        bookingPage = new SpiceJetBookingPage(driver);
        calendarUtils = new CalendarUtils(driver);
    }
    
    @Test(priority = 1, description = "Test selecting departure date from calendar")
    public void testSelectDepartureDate() {
        test.log(Status.INFO, "Starting departure date selection test");
        
        try {
            // Select departure date (next week)
            LocalDate departureDate = LocalDate.now().plusDays(7);
            bookingPage.selectDepartureDate(departureDate);
            
            test.log(Status.PASS, "Successfully selected departure date: " + departureDate);
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Failed to select departure date: " + e.getMessage());
            Assert.fail("Departure date selection failed");
        }
    }
    
    @Test(priority = 2, description = "Test selecting return date from calendar")
    public void testSelectReturnDate() {
        test.log(Status.INFO, "Starting return date selection test");
        
        try {
            // First select round trip
            bookingPage.selectRoundTrip();
            
            // Select departure date
            LocalDate departureDate = LocalDate.now().plusDays(7);
            bookingPage.selectDepartureDate(departureDate);
            
            // Select return date (2 weeks from now)
            LocalDate returnDate = LocalDate.now().plusDays(14);
            bookingPage.selectReturnDate(returnDate);
            
            test.log(Status.PASS, "Successfully selected return date: " + returnDate);
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Failed to select return date: " + e.getMessage());
            Assert.fail("Return date selection failed");
        }
    }
    
    @Test(priority = 3, description = "Test selecting specific date with day, month, year")
    public void testSelectSpecificDate() {
        test.log(Status.INFO, "Starting specific date selection test");
        
        try {
            // Select a specific date: 15th September 2025
            bookingPage.selectDepartureDate(15, "SEPTEMBER", 2025);
            
            test.log(Status.PASS, "Successfully selected specific date: 15 September 2025");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Failed to select specific date: " + e.getMessage());
            Assert.fail("Specific date selection failed");
        }
    }
    
    @Test(priority = 4, description = "Test selecting dates in different months")
    public void testSelectDatesInDifferentMonths() {
        test.log(Status.INFO, "Starting different months date selection test");
        
        try {
            bookingPage.selectRoundTrip();
            
            // Select departure date in next month
            LocalDate departureDate = LocalDate.now().plusMonths(1).withDayOfMonth(10);
            bookingPage.selectDepartureDate(departureDate);
            
            // Select return date in the month after that
            LocalDate returnDate = LocalDate.now().plusMonths(2).withDayOfMonth(20);
            bookingPage.selectReturnDate(returnDate);
            
            test.log(Status.PASS, "Successfully selected dates in different months");
            test.log(Status.INFO, "Departure: " + departureDate + ", Return: " + returnDate);
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Failed to select dates in different months: " + e.getMessage());
            Assert.fail("Different months date selection failed");
        }
    }
    
    @Test(priority = 5, description = "Test complete booking flow with calendar")
    public void testCompleteBookingFlow() {
        test.log(Status.INFO, "Starting complete booking flow test");
        
        try {
            // Define travel details
            String origin = "DEL"; // Delhi
            String destination = "BOM"; // Mumbai
            LocalDate departureDate = LocalDate.now().plusDays(10);
            LocalDate returnDate = LocalDate.now().plusDays(17);
            
            // Complete the booking form
            bookingPage.completeBookingForm(origin, destination, departureDate, returnDate);
            
            test.log(Status.PASS, "Successfully completed booking flow");
            test.log(Status.INFO, "Origin: " + origin + ", Destination: " + destination);
            test.log(Status.INFO, "Departure: " + departureDate + ", Return: " + returnDate);
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Failed to complete booking flow: " + e.getMessage());
            Assert.fail("Complete booking flow failed");
        }
    }
    
    @Test(dataProvider = "dateTestData", priority = 6, 
          description = "Test calendar with multiple date combinations")
    public void testMultipleDateCombinations(int daysFromToday, boolean isRoundTrip) {
        test.log(Status.INFO, "Testing date combination: " + daysFromToday + " days from today, Round trip: " + isRoundTrip);
        
        try {
            if (isRoundTrip) {
                bookingPage.selectRoundTrip();
                
                // Select departure date
                LocalDate departureDate = LocalDate.now().plusDays(daysFromToday);
                bookingPage.selectDepartureDate(departureDate);
                
                // Select return date (7 days after departure)
                LocalDate returnDate = departureDate.plusDays(7);
                bookingPage.selectReturnDate(returnDate);
                
                test.log(Status.PASS, "Round trip dates selected successfully");
            } else {
                bookingPage.selectOneWayTrip();
                
                // Select departure date
                LocalDate departureDate = LocalDate.now().plusDays(daysFromToday);
                bookingPage.selectDepartureDate(departureDate);
                
                test.log(Status.PASS, "One way date selected successfully");
            }
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Failed to select date combination: " + e.getMessage());
            Assert.fail("Date combination test failed");
        }
    }
    
    @Test(priority = 7, description = "Test selecting weekend dates")
    public void testSelectWeekendDates() {
        test.log(Status.INFO, "Starting weekend dates selection test");
        
        try {
            // Find next Saturday
            LocalDate nextSaturday = LocalDate.now();
            while (nextSaturday.getDayOfWeek().getValue() != 6) { // 6 = Saturday
                nextSaturday = nextSaturday.plusDays(1);
            }
            
            // Find following Sunday
            LocalDate nextSunday = nextSaturday.plusDays(1);
            
            bookingPage.selectRoundTrip();
            bookingPage.selectDepartureDate(nextSaturday);
            bookingPage.selectReturnDate(nextSunday);
            
            test.log(Status.PASS, "Successfully selected weekend dates");
            test.log(Status.INFO, "Saturday: " + nextSaturday + ", Sunday: " + nextSunday);
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Failed to select weekend dates: " + e.getMessage());
            Assert.fail("Weekend dates selection failed");
        }
    }
    
    @Test(priority = 8, description = "Test selecting dates across year boundary")
    public void testSelectDatesAcrossYear() {
        test.log(Status.INFO, "Starting year boundary dates selection test");
        
        try {
            // Select date in December of current year
            LocalDate decemberDate = LocalDate.now().withMonth(12).withDayOfMonth(25);
            
            // Select date in January of next year
            LocalDate januaryDate = LocalDate.now().plusYears(1).withMonth(1).withDayOfMonth(5);
            
            bookingPage.selectRoundTrip();
            bookingPage.selectDepartureDate(decemberDate);
            bookingPage.selectReturnDate(januaryDate);
            
            test.log(Status.PASS, "Successfully selected dates across year boundary");
            test.log(Status.INFO, "December: " + decemberDate + ", January: " + januaryDate);
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Failed to select dates across year: " + e.getMessage());
            Assert.fail("Year boundary dates selection failed");
        }
    }
    
    @DataProvider(name = "dateTestData")
    public Object[][] getDateTestData() {
        return new Object[][] {
            {3, false},   // 3 days from today, one way
            {7, true},    // 1 week from today, round trip
            {14, true},   // 2 weeks from today, round trip
            {30, false},  // 1 month from today, one way
            {45, true},   // 45 days from today, round trip
        };
    }
}
