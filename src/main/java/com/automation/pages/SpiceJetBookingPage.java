package com.automation.pages;

import com.automation.base.BasePage;
import com.automation.utils.CalendarUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import java.time.LocalDate;

/**
 * SpiceJet Booking Page Object Model class
 */
public class SpiceJetBookingPage extends BasePage {
    
    private CalendarUtils calendarUtils;
    
    // Page Elements
    @FindBy(id = "ctl00_mainContent_ddl_originStation1_CTXT")
    private WebElement fromDropdown;
    
    @FindBy(id = "ctl00_mainContent_ddl_destinationStation1_CTXT")
    private WebElement toDropdown;
    
    @FindBy(id = "ctl00_mainContent_view_date1")
    private WebElement departureDate;
    
    @FindBy(id = "ctl00_mainContent_view_date2")
    private WebElement returnDate;
    
    @FindBy(id = "ctl00_mainContent_btn_FindFlights")
    private WebElement searchButton;
    
    @FindBy(id = "ctl00_mainContent_rbtnl_Trip_0")
    private WebElement oneWayTrip;
    
    @FindBy(id = "ctl00_mainContent_rbtnl_Trip_1")
    private WebElement roundTrip;
    
    // Calendar related elements
    @FindBy(xpath = "//div[@class='ui-datepicker-group ui-datepicker-group-first']")
    private WebElement leftCalendar;
    
    @FindBy(xpath = "//div[@class='ui-datepicker-group ui-datepicker-group-last']")
    private WebElement rightCalendar;
    
    @FindBy(xpath = "//span[@class='ui-icon ui-icon-circle-triangle-w']")
    private WebElement previousMonthButton;
    
    @FindBy(xpath = "//span[@class='ui-icon ui-icon-circle-triangle-e']")
    private WebElement nextMonthButton;
    
    // Constructor
    public SpiceJetBookingPage(WebDriver driver) {
        super(driver);
        this.calendarUtils = new CalendarUtils(driver);
    }
    
    // Page Actions
    
    /**
     * Select origin city
     * @param city - origin city name
     */
    public SpiceJetBookingPage selectOrigin(String city) {
        waitForElementToBeClickable(fromDropdown);
        fromDropdown.click();
        
        WebElement cityOption = driver.findElement(By.xpath("//a[@value='" + city + "']"));
        waitForElementToBeClickable(cityOption);
        cityOption.click();
        
        return this;
    }
    
    /**
     * Select destination city
     * @param city - destination city name
     */
    public SpiceJetBookingPage selectDestination(String city) {
        waitForElementToBeClickable(toDropdown);
        toDropdown.click();
        
        WebElement cityOption = driver.findElement(By.xpath("//a[@value='" + city + "' and @tabindex='-1']"));
        waitForElementToBeClickable(cityOption);
        cityOption.click();
        
        return this;
    }
    
    /**
     * Select departure date using calendar
     * @param day - day of month
     * @param month - month name
     * @param year - year
     */
    public SpiceJetBookingPage selectDepartureDate(int day, String month, int year) {
        // Click on departure date field to open calendar
        waitForElementToBeClickable(departureDate);
        departureDate.click();
        
        // Wait for calendar to be visible
        waitForElementToBeVisible(leftCalendar);
        
        // Select the date using SpiceJet specific logic
        selectDateInSpiceJetCalendar(day, month, year);
        
        return this;
    }
    
    /**
     * Select departure date using LocalDate
     * @param date - LocalDate object
     */
    public SpiceJetBookingPage selectDepartureDate(LocalDate date) {
        return selectDepartureDate(date.getDayOfMonth(), 
                                 date.getMonth().name(), 
                                 date.getYear());
    }
    
    /**
     * Select return date using calendar
     * @param day - day of month
     * @param month - month name
     * @param year - year
     */
    public SpiceJetBookingPage selectReturnDate(int day, String month, int year) {
        // Click on return date field to open calendar
        waitForElementToBeClickable(returnDate);
        returnDate.click();
        
        // Wait for calendar to be visible
        waitForElementToBeVisible(rightCalendar);
        
        // Select the date using SpiceJet specific logic
        selectDateInSpiceJetCalendar(day, month, year);
        
        return this;
    }
    
    /**
     * Select return date using LocalDate
     * @param date - LocalDate object
     */
    public SpiceJetBookingPage selectReturnDate(LocalDate date) {
        return selectReturnDate(date.getDayOfMonth(), 
                               date.getMonth().name(), 
                               date.getYear());
    }
    
    /**
     * Select one way trip
     */
    public SpiceJetBookingPage selectOneWayTrip() {
        if (!oneWayTrip.isSelected()) {
            waitForElementToBeClickable(oneWayTrip);
            oneWayTrip.click();
        }
        return this;
    }
    
    /**
     * Select round trip
     */
    public SpiceJetBookingPage selectRoundTrip() {
        if (!roundTrip.isSelected()) {
            waitForElementToBeClickable(roundTrip);
            roundTrip.click();
        }
        return this;
    }
    
    /**
     * Click search flights button
     */
    public void searchFlights() {
        waitForElementToBeClickable(searchButton);
        searchButton.click();
    }
    
    /**
     * SpiceJet specific calendar date selection logic
     * @param day - day to select
     * @param month - month name
     * @param year - year
     */
    private void selectDateInSpiceJetCalendar(int day, String month, int year) {
        try {
            // Navigate to correct month and year
            navigateToMonthYear(month, year);
            
            // Select the specific day
            String dayXpath = "//a[@class='ui-state-default' and text()='" + day + "']";
            WebElement dayElement = driver.findElement(By.xpath(dayXpath));
            waitForElementToBeClickable(dayElement);
            dayElement.click();
            
        } catch (Exception e) {
            System.out.println("Error selecting date in SpiceJet calendar: " + e.getMessage());
            
            // Fallback: try alternative day selection
            try {
                String alternativeDayXpath = "//td[@data-month and @data-year]//a[text()='" + day + "']";
                WebElement dayElement = driver.findElement(By.xpath(alternativeDayXpath));
                waitForElementToBeClickable(dayElement);
                dayElement.click();
            } catch (Exception fallbackError) {
                System.out.println("Fallback date selection also failed: " + fallbackError.getMessage());
            }
        }
    }
    
    /**
     * Navigate to specific month and year in SpiceJet calendar
     * @param targetMonth - target month name
     * @param targetYear - target year
     */
    private void navigateToMonthYear(String targetMonth, int targetYear) {
        try {
            int maxAttempts = 24; // Prevent infinite loop (2 years worth of navigation)
            int attempts = 0;
            
            while (attempts < maxAttempts) {
                // Get current month and year from calendar header
                String currentMonthYear = getCurrentMonthYear();
                
                if (currentMonthYear.contains(targetMonth.toUpperCase()) && 
                    currentMonthYear.contains(String.valueOf(targetYear))) {
                    break; // We've reached the target month/year
                }
                
                // Determine navigation direction
                if (needToGoForward(currentMonthYear, targetMonth, targetYear)) {
                    clickNextMonth();
                } else {
                    clickPreviousMonth();
                }
                
                attempts++;
                Thread.sleep(300); // Small delay to allow calendar to update
            }
        } catch (Exception e) {
            System.out.println("Error navigating to month/year: " + e.getMessage());
        }
    }
    
    /**
     * Get current month and year from calendar header
     * @return current month and year as string
     */
    private String getCurrentMonthYear() {
        try {
            WebElement monthYearElement = driver.findElement(
                By.xpath("//div[@class='ui-datepicker-title']"));
            return monthYearElement.getText().toUpperCase();
        } catch (Exception e) {
            // Try alternative selector
            try {
                WebElement monthElement = driver.findElement(
                    By.xpath("//span[@class='ui-datepicker-month']"));
                WebElement yearElement = driver.findElement(
                    By.xpath("//span[@class='ui-datepicker-year']"));
                return (monthElement.getText() + " " + yearElement.getText()).toUpperCase();
            } catch (Exception e2) {
                System.out.println("Could not get current month/year: " + e2.getMessage());
                return "";
            }
        }
    }
    
    /**
     * Determine if we need to navigate forward in the calendar
     * @param currentMonthYear - current month/year string
     * @param targetMonth - target month
     * @param targetYear - target year
     * @return true if we need to go forward
     */
    private boolean needToGoForward(String currentMonthYear, String targetMonth, int targetYear) {
        // Simple logic - in a real implementation, you'd want more sophisticated date comparison
        try {
            // Extract current year (assuming format like "AUGUST 2025")
            String[] parts = currentMonthYear.split(" ");
            if (parts.length >= 2) {
                int currentYear = Integer.parseInt(parts[1]);
                
                if (currentYear < targetYear) {
                    return true; // Go forward to reach target year
                } else if (currentYear > targetYear) {
                    return false; // Go backward to reach target year
                } else {
                    // Same year, compare months
                    String currentMonth = parts[0];
                    return isMonthBefore(currentMonth, targetMonth.toUpperCase());
                }
            }
        } catch (Exception e) {
            System.out.println("Error determining navigation direction: " + e.getMessage());
        }
        
        return true; // Default to forward
    }
    
    /**
     * Check if month1 comes before month2
     * @param month1 - first month
     * @param month2 - second month
     * @return true if month1 is before month2
     */
    private boolean isMonthBefore(String month1, String month2) {
        String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
                          "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
        
        int index1 = -1, index2 = -1;
        
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(month1)) index1 = i;
            if (months[i].equals(month2)) index2 = i;
        }
        
        return index1 < index2;
    }
    
    /**
     * Click next month button
     */
    private void clickNextMonth() {
        try {
            waitForElementToBeClickable(nextMonthButton);
            nextMonthButton.click();
        } catch (Exception e) {
            System.out.println("Could not click next month button: " + e.getMessage());
        }
    }
    
    /**
     * Click previous month button
     */
    private void clickPreviousMonth() {
        try {
            waitForElementToBeClickable(previousMonthButton);
            previousMonthButton.click();
        } catch (Exception e) {
            System.out.println("Could not click previous month button: " + e.getMessage());
        }
    }
    
    /**
     * Select date relative to today
     * @param daysFromToday - number of days from today (positive for future, negative for past)
     * @param isReturnDate - true if selecting return date, false for departure date
     */
    public SpiceJetBookingPage selectDateRelativeToToday(int daysFromToday, boolean isReturnDate) {
        LocalDate targetDate = LocalDate.now().plusDays(daysFromToday);
        
        if (isReturnDate) {
            return selectReturnDate(targetDate);
        } else {
            return selectDepartureDate(targetDate);
        }
    }
    
    /**
     * Complete booking form with all details
     * @param origin - origin city
     * @param destination - destination city
     * @param departureDate - departure date
     * @param returnDate - return date (null for one way)
     */
    public void completeBookingForm(String origin, String destination, 
                                  LocalDate departureDate, LocalDate returnDate) {
        // Select trip type
        if (returnDate != null) {
            selectRoundTrip();
        } else {
            selectOneWayTrip();
        }
        
        // Select cities
        selectOrigin(origin);
        selectDestination(destination);
        
        // Select dates
        selectDepartureDate(departureDate);
        if (returnDate != null) {
            selectReturnDate(returnDate);
        }
        
        // Search for flights
        searchFlights();
    }
}
