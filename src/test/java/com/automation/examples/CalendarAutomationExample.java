package com.automation.examples;

import com.automation.utils.CalendarUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.time.LocalDate;

/**
 * Example class demonstrating different approaches to calendar automation
 */
public class CalendarAutomationExample {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private CalendarUtils calendarUtils;
    
    public CalendarAutomationExample(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.calendarUtils = new CalendarUtils(driver);
    }
    
    /**
     * Example 1: Basic date picker automation
     * Works with most standard jQuery UI datepickers
     */
    public void basicDatePickerExample(int day, String month, int year) {
        try {
            // Click on date input field to open calendar
            WebElement dateInput = driver.findElement(By.id("datepicker"));
            dateInput.click();
            
            // Wait for calendar to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("ui-datepicker")));
            
            // Use the calendar utility
            calendarUtils.selectDate(day, month, year);
            
        } catch (Exception e) {
            System.out.println("Basic date picker automation failed: " + e.getMessage());
        }
    }
    
    /**
     * Example 2: Custom calendar automation with manual navigation
     * For calendars with custom HTML structure
     */
    public void customCalendarExample(LocalDate targetDate) {
        try {
            // Open calendar
            WebElement calendarButton = driver.findElement(By.className("calendar-trigger"));
            calendarButton.click();
            
            // Get current displayed month/year
            WebElement monthYearElement = driver.findElement(By.className("calendar-header"));
            String currentMonthYear = monthYearElement.getText();
            
            // Navigate to target month/year
            navigateToTargetMonth(targetDate);
            
            // Select the day
            String dayXpath = "//td[@data-date='" + targetDate.getDayOfMonth() + "']";
            WebElement dayElement = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath(dayXpath)));
            dayElement.click();
            
        } catch (Exception e) {
            System.out.println("Custom calendar automation failed: " + e.getMessage());
        }
    }
    
    /**
     * Example 3: Bootstrap datepicker automation
     */
    public void bootstrapDatePickerExample(LocalDate date) {
        try {
            // Click input to open datepicker
            WebElement input = driver.findElement(By.className("form-control"));
            input.click();
            
            // Wait for Bootstrap datepicker to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("datepicker")));
            
            // Navigate to correct month/year
            navigateBootstrapCalendar(date);
            
            // Select day
            String daySelector = "//td[@class='day' and text()='" + date.getDayOfMonth() + "']";
            WebElement dayElement = driver.findElement(By.xpath(daySelector));
            dayElement.click();
            
        } catch (Exception e) {
            System.out.println("Bootstrap datepicker automation failed: " + e.getMessage());
        }
    }
    
    /**
     * Example 4: React/Angular date picker automation
     */
    public void modernDatePickerExample(LocalDate date) {
        try {
            // Click on modern date picker trigger
            WebElement trigger = driver.findElement(By.cssSelector("[data-testid='date-picker']"));
            trigger.click();
            
            // Wait for React/Angular calendar component
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".react-datepicker, .mat-calendar")));
            
            // Navigate using modern calendar controls
            navigateModernCalendar(date);
            
            // Select date
            String dateSelector = String.format(
                "[aria-label*='%s %d, %d'], [data-date='%s']",
                date.getMonth().name().toLowerCase(),
                date.getDayOfMonth(),
                date.getYear(),
                date.toString()
            );
            
            WebElement dateElement = driver.findElement(By.cssSelector(dateSelector));
            dateElement.click();
            
        } catch (Exception e) {
            System.out.println("Modern datepicker automation failed: " + e.getMessage());
        }
    }
    
    /**
     * Example 5: Date range picker automation
     */
    public void dateRangePickerExample(LocalDate startDate, LocalDate endDate) {
        try {
            // Click on date range input
            WebElement rangeInput = driver.findElement(By.className("daterange-input"));
            rangeInput.click();
            
            // Wait for range picker to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("daterangepicker")));
            
            // Select start date
            selectDateInRangePicker(startDate, "start");
            
            // Select end date
            selectDateInRangePicker(endDate, "end");
            
            // Apply the selection
            WebElement applyButton = driver.findElement(
                By.xpath("//button[text()='Apply']"));
            applyButton.click();
            
        } catch (Exception e) {
            System.out.println("Date range picker automation failed: " + e.getMessage());
        }
    }
    
    /**
     * Example 6: Mobile-responsive calendar automation
     */
    public void mobileCalendarExample(LocalDate date) {
        try {
            // For mobile calendars, might need different approach
            WebElement mobileInput = driver.findElement(By.cssSelector("input[type='date']"));
            
            // Check if it's a native HTML5 date input
            if (isNativeDateInput(mobileInput)) {
                // Use direct value setting for HTML5 date inputs
                String dateString = date.toString(); // Format: YYYY-MM-DD
                mobileInput.clear();
                mobileInput.sendKeys(dateString);
            } else {
                // Handle custom mobile calendar
                mobileInput.click();
                
                // Wait for mobile calendar overlay
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.className("mobile-calendar")));
                
                // Use swipe gestures or touch interactions if needed
                navigateMobileCalendar(date);
            }
            
        } catch (Exception e) {
            System.out.println("Mobile calendar automation failed: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to navigate to target month in custom calendar
     */
    private void navigateToTargetMonth(LocalDate targetDate) {
        try {
            // Implementation depends on specific calendar structure
            // This is a generic example
            
            while (!isTargetMonthDisplayed(targetDate)) {
                if (shouldNavigateForward(targetDate)) {
                    driver.findElement(By.className("next-month")).click();
                } else {
                    driver.findElement(By.className("prev-month")).click();
                }
                
                // Small delay to allow calendar to update
                Thread.sleep(200);
            }
        } catch (Exception e) {
            System.out.println("Month navigation failed: " + e.getMessage());
        }
    }
    
    /**
     * Helper method for Bootstrap calendar navigation
     */
    private void navigateBootstrapCalendar(LocalDate date) {
        // Bootstrap-specific navigation logic
        // This would include year/month dropdown handling
        // or prev/next button clicking
    }
    
    /**
     * Helper method for modern calendar navigation
     */
    private void navigateModernCalendar(LocalDate date) {
        // React/Angular calendar navigation
        // Often includes year/month selection dropdowns
        // or sophisticated navigation controls
    }
    
    /**
     * Helper method for date range picker date selection
     */
    private void selectDateInRangePicker(LocalDate date, String dateType) {
        try {
            String calendarClass = dateType.equals("start") ? "left" : "right";
            String dayXpath = String.format(
                "//div[contains(@class,'%s')]//td[text()='%d' and not(contains(@class,'disabled'))]",
                calendarClass, date.getDayOfMonth()
            );
            
            WebElement dayElement = driver.findElement(By.xpath(dayXpath));
            dayElement.click();
            
        } catch (Exception e) {
            System.out.println("Date range selection failed: " + e.getMessage());
        }
    }
    
    /**
     * Helper method for mobile calendar navigation
     */
    private void navigateMobileCalendar(LocalDate date) {
        // Mobile-specific navigation
        // Might involve swipe gestures, touch events, etc.
    }
    
    /**
     * Check if input is native HTML5 date input
     */
    private boolean isNativeDateInput(WebElement input) {
        return "date".equals(input.getAttribute("type"));
    }
    
    /**
     * Check if target month is currently displayed
     */
    private boolean isTargetMonthDisplayed(LocalDate targetDate) {
        try {
            WebElement monthDisplay = driver.findElement(By.className("current-month"));
            String displayedMonth = monthDisplay.getText().toLowerCase();
            String targetMonth = targetDate.getMonth().name().toLowerCase();
            
            return displayedMonth.contains(targetMonth);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Determine navigation direction
     */
    private boolean shouldNavigateForward(LocalDate targetDate) {
        // Compare target date with currently displayed date
        // Return true if need to go forward, false for backward
        return targetDate.isAfter(LocalDate.now());
    }
    
    /**
     * Complete example: SpiceJet-like booking with date selection
     */
    public void spiceJetBookingExample() {
        try {
            // Navigate to SpiceJet
            driver.get("https://www.spicejet.com/");
            
            // Select trip type
            WebElement roundTrip = driver.findElement(By.id("ctl00_mainContent_rbtnl_Trip_1"));
            roundTrip.click();
            
            // Select origin
            WebElement fromCity = driver.findElement(By.id("ctl00_mainContent_ddl_originStation1_CTXT"));
            fromCity.click();
            driver.findElement(By.xpath("//a[@value='DEL']")).click();
            
            // Select destination
            WebElement toCity = driver.findElement(By.id("ctl00_mainContent_ddl_destinationStation1_CTXT"));
            toCity.click();
            driver.findElement(By.xpath("//a[@value='BOM' and @tabindex='-1']")).click();
            
            // Select departure date
            WebElement departureDate = driver.findElement(By.id("ctl00_mainContent_view_date1"));
            departureDate.click();
            
            // Select a date 7 days from now
            LocalDate departure = LocalDate.now().plusDays(7);
            selectSpiceJetDate(departure);
            
            // Select return date
            LocalDate returnDate = departure.plusDays(7);
            selectSpiceJetDate(returnDate);
            
            // Search flights
            WebElement searchButton = driver.findElement(By.id("ctl00_mainContent_btn_FindFlights"));
            searchButton.click();
            
        } catch (Exception e) {
            System.out.println("SpiceJet booking example failed: " + e.getMessage());
        }
    }
    
    /**
     * Helper method specifically for SpiceJet date selection
     */
    private void selectSpiceJetDate(LocalDate date) {
        try {
            // Navigate to correct month/year
            while (!isCorrectMonthDisplayed(date)) {
                if (date.isAfter(getCurrentDisplayedDate())) {
                    driver.findElement(By.xpath("//span[@class='ui-icon ui-icon-circle-triangle-e']")).click();
                } else {
                    driver.findElement(By.xpath("//span[@class='ui-icon ui-icon-circle-triangle-w']")).click();
                }
                Thread.sleep(300);
            }
            
            // Select the day
            String dayXpath = "//a[@class='ui-state-default' and text()='" + date.getDayOfMonth() + "']";
            WebElement dayElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dayXpath)));
            dayElement.click();
            
        } catch (Exception e) {
            System.out.println("SpiceJet date selection failed: " + e.getMessage());
        }
    }
    
    /**
     * Check if correct month is displayed in SpiceJet calendar
     */
    private boolean isCorrectMonthDisplayed(LocalDate targetDate) {
        try {
            WebElement monthYear = driver.findElement(By.xpath("//div[@class='ui-datepicker-title']"));
            String displayedText = monthYear.getText().toUpperCase();
            
            String targetMonth = targetDate.getMonth().name();
            String targetYear = String.valueOf(targetDate.getYear());
            
            return displayedText.contains(targetMonth) && displayedText.contains(targetYear);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get currently displayed date in calendar
     */
    private LocalDate getCurrentDisplayedDate() {
        try {
            WebElement monthYear = driver.findElement(By.xpath("//div[@class='ui-datepicker-title']"));
            String displayedText = monthYear.getText();
            // Parse and return as LocalDate
            // This is a simplified version - actual implementation would be more robust
            return LocalDate.now(); // Placeholder
        } catch (Exception e) {
            return LocalDate.now();
        }
    }
}
