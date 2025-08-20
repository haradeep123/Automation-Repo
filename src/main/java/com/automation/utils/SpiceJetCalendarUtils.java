package com.automation.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * SpiceJet specific Calendar Utilities using your custom locators
 */
public class SpiceJetCalendarUtils {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    // Your specific locators
    private final String CALENDAR_PICKER = "//div[@data-testid= 'undefined-calendar-picker']";
    private final String CALENDAR_NEXT_ARROW = "//div[@data-testid= 'undefined-calendar-picker']/div[1]";
    private final String CALENDAR_PREV_ARROW = "//div[@data-testid= 'undefined-calendar-picker']/div[2]";
    
    public SpiceJetCalendarUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    /**
     * Open calendar picker
     */
    public void openCalendar() {
        try {
            WebElement calendarPicker = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath(CALENDAR_PICKER)));
            calendarPicker.click();
            
            // Wait for calendar to be fully loaded
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Failed to open calendar: " + e.getMessage());
        }
    }
    
    /**
     * Select specific date from calendar
     * @param day - day to select (1-31)
     * @param month - month name (JANUARY, FEBRUARY, etc.)
     * @param year - year (2024, 2025, etc.)
     */
    public void selectDate(int day, String month, int year) {
        try {
            // Navigate to correct month and year
            navigateToMonthYear(month, year);
            
            // Select the specific day
            selectDay(day);
            
        } catch (Exception e) {
            System.out.println("Failed to select date: " + day + "/" + month + "/" + year + " - " + e.getMessage());
        }
    }
    
    /**
     * Select date using LocalDate
     * @param date - LocalDate object
     */
    public void selectDate(LocalDate date) {
        selectDate(date.getDayOfMonth(), date.getMonth().name(), date.getYear());
    }
    
    /**
     * Select date from string format
     * @param dateString - date string in dd-MM-yyyy format
     */
    public void selectDate(String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            selectDate(date);
        } catch (Exception e) {
            System.out.println("Invalid date format. Use dd-MM-yyyy format: " + e.getMessage());
        }
    }
    
    /**
     * Navigate to specific month and year
     * @param targetMonth - target month name
     * @param targetYear - target year
     */
    private void navigateToMonthYear(String targetMonth, int targetYear) {
        try {
            int maxAttempts = 24; // Prevent infinite loop
            int attempts = 0;
            
            while (attempts < maxAttempts) {
                String currentMonthYear = getCurrentMonthYear();
                
                if (isTargetMonthYear(currentMonthYear, targetMonth, targetYear)) {
                    break; // We've reached the target
                }
                
                if (shouldNavigateForward(currentMonthYear, targetMonth, targetYear)) {
                    clickNextMonth();
                } else {
                    clickPreviousMonth();
                }
                
                attempts++;
                Thread.sleep(500); // Wait for calendar to update
            }
        } catch (Exception e) {
            System.out.println("Failed to navigate to month/year: " + e.getMessage());
        }
    }
    
    /**
     * Get current month and year displayed in calendar
     * @return current month year string
     */
    private String getCurrentMonthYear() {
        try {
            // Try multiple selectors for month/year display
            String[] monthYearSelectors = {
                "//div[@data-testid= 'undefined-calendar-picker']//div[contains(@class,'month-year')]",
                "//div[@data-testid= 'undefined-calendar-picker']//span[contains(@class,'month')]",
                "//div[@data-testid= 'undefined-calendar-picker']//*[contains(text(),'2024') or contains(text(),'2025')]",
                "//div[contains(@class,'css-1dbjc4n')]//div[contains(text(),'January') or contains(text(),'February') or contains(text(),'March')]"
            };
            
            for (String selector : monthYearSelectors) {
                try {
                    WebElement monthYearElement = driver.findElement(By.xpath(selector));
                    String text = monthYearElement.getText().trim();
                    if (!text.isEmpty()) {
                        return text.toUpperCase();
                    }
                } catch (Exception e) {
                    // Continue to next selector
                }
            }
            
            // Fallback: try to find any text that looks like month/year
            List<WebElement> allElements = driver.findElements(
                By.xpath("//div[@data-testid= 'undefined-calendar-picker']//*[text()]"));
            
            for (WebElement element : allElements) {
                String text = element.getText().trim();
                if (containsMonthYear(text)) {
                    return text.toUpperCase();
                }
            }
            
        } catch (Exception e) {
            System.out.println("Could not get current month/year: " + e.getMessage());
        }
        
        return ""; // Return empty if not found
    }
    
    /**
     * Check if text contains month and year information
     * @param text - text to check
     * @return true if contains month/year
     */
    private boolean containsMonthYear(String text) {
        String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
                          "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
        
        text = text.toUpperCase();
        
        // Check if text contains a month name and a year (2020-2030)
        boolean hasMonth = false;
        boolean hasYear = false;
        
        for (String month : months) {
            if (text.contains(month)) {
                hasMonth = true;
                break;
            }
        }
        
        for (int year = 2020; year <= 2030; year++) {
            if (text.contains(String.valueOf(year))) {
                hasYear = true;
                break;
            }
        }
        
        return hasMonth || hasYear;
    }
    
    /**
     * Check if current display matches target month/year
     * @param currentMonthYear - current display
     * @param targetMonth - target month
     * @param targetYear - target year
     * @return true if matches
     */
    private boolean isTargetMonthYear(String currentMonthYear, String targetMonth, int targetYear) {
        return currentMonthYear.contains(targetMonth.toUpperCase()) && 
               currentMonthYear.contains(String.valueOf(targetYear));
    }
    
    /**
     * Determine if we should navigate forward
     * @param currentMonthYear - current display
     * @param targetMonth - target month
     * @param targetYear - target year
     * @return true if should go forward
     */
    private boolean shouldNavigateForward(String currentMonthYear, String targetMonth, int targetYear) {
        try {
            // Extract current year and month
            int currentYear = extractYear(currentMonthYear);
            String currentMonth = extractMonth(currentMonthYear);
            
            if (currentYear < targetYear) {
                return true; // Go forward to reach future year
            } else if (currentYear > targetYear) {
                return false; // Go backward to reach past year
            } else {
                // Same year, compare months
                return isMonthBefore(currentMonth, targetMonth);
            }
        } catch (Exception e) {
            System.out.println("Error determining navigation direction: " + e.getMessage());
            return true; // Default to forward
        }
    }
    
    /**
     * Extract year from month/year string
     * @param monthYear - month year string
     * @return year as integer
     */
    private int extractYear(String monthYear) {
        for (int year = 2020; year <= 2030; year++) {
            if (monthYear.contains(String.valueOf(year))) {
                return year;
            }
        }
        return LocalDate.now().getYear(); // Default to current year
    }
    
    /**
     * Extract month from month/year string
     * @param monthYear - month year string
     * @return month name
     */
    private String extractMonth(String monthYear) {
        String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
                          "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
        
        for (String month : months) {
            if (monthYear.contains(month)) {
                return month;
            }
        }
        return LocalDate.now().getMonth().name(); // Default to current month
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
            if (months[i].equals(month1.toUpperCase())) index1 = i;
            if (months[i].equals(month2.toUpperCase())) index2 = i;
        }
        
        return index1 < index2;
    }
    
    /**
     * Click next month arrow using your locator
     */
    private void clickNextMonth() {
        try {
            WebElement nextArrow = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath(CALENDAR_NEXT_ARROW)));
            nextArrow.click();
            Thread.sleep(300); // Wait for animation
        } catch (Exception e) {
            System.out.println("Failed to click next month: " + e.getMessage());
        }
    }
    
    /**
     * Click previous month arrow using your locator
     */
    private void clickPreviousMonth() {
        try {
            WebElement prevArrow = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath(CALENDAR_PREV_ARROW)));
            prevArrow.click();
            Thread.sleep(300); // Wait for animation
        } catch (Exception e) {
            System.out.println("Failed to click previous month: " + e.getMessage());
        }
    }
    
    /**
     * Select specific day from calendar
     * @param day - day to select (1-31)
     */
    private void selectDay(int day) {
        try {
            // Multiple strategies to find and click the day
            String[] daySelectors = {
                // Strategy 1: Direct text match
                "//div[@data-testid= 'undefined-calendar-picker']//*[text()='" + day + "']",
                
                // Strategy 2: Clickable day elements
                "//div[@data-testid= 'undefined-calendar-picker']//div[contains(@class,'day') and text()='" + day + "']",
                
                // Strategy 3: Calendar cell with day number
                "//div[@data-testid= 'undefined-calendar-picker']//td[text()='" + day + "']",
                
                // Strategy 4: Any clickable element with day number
                "//div[@data-testid= 'undefined-calendar-picker']//*[@role='button' and text()='" + day + "']",
                
                // Strategy 5: Span or div with day number
                "//div[@data-testid= 'undefined-calendar-picker']//span[text()='" + day + "']",
                
                // Strategy 6: Generic approach - any element with day number that's clickable
                "//*[contains(@class,'css-') and text()='" + day + "' and not(contains(@class,'disabled'))]"
            };
            
            for (String selector : daySelectors) {
                try {
                    WebElement dayElement = wait.until(
                        ExpectedConditions.elementToBeClickable(By.xpath(selector)));
                    dayElement.click();
                    return; // Success, exit method
                } catch (Exception e) {
                    // Continue to next strategy
                }
            }
            
            // If all strategies fail, try to find all clickable elements and filter by text
            List<WebElement> clickableElements = driver.findElements(
                By.xpath("//div[@data-testid= 'undefined-calendar-picker']//*"));
            
            for (WebElement element : clickableElements) {
                try {
                    if (element.getText().trim().equals(String.valueOf(day)) && element.isEnabled()) {
                        element.click();
                        return;
                    }
                } catch (Exception e) {
                    // Continue to next element
                }
            }
            
            throw new Exception("Could not find day: " + day + " in calendar");
            
        } catch (Exception e) {
            System.out.println("Failed to select day " + day + ": " + e.getMessage());
        }
    }
    
    /**
     * Select today's date
     */
    public void selectToday() {
        selectDate(LocalDate.now());
    }
    
    /**
     * Select date relative to today
     * @param daysFromToday - number of days from today (positive for future, negative for past)
     */
    public void selectDateRelativeToToday(int daysFromToday) {
        LocalDate targetDate = LocalDate.now().plusDays(daysFromToday);
        selectDate(targetDate);
    }
    
    /**
     * Select first available date (usually tomorrow or today if available)
     */
    public void selectFirstAvailableDate() {
        try {
            // Try to select today first
            LocalDate today = LocalDate.now();
            selectDate(today);
        } catch (Exception e) {
            // If today fails, try tomorrow
            try {
                LocalDate tomorrow = LocalDate.now().plusDays(1);
                selectDate(tomorrow);
            } catch (Exception e2) {
                System.out.println("Could not select any available date: " + e2.getMessage());
            }
        }
    }
    
    /**
     * Check if calendar is currently open
     * @return true if calendar is visible
     */
    public boolean isCalendarOpen() {
        try {
            WebElement calendar = driver.findElement(By.xpath(CALENDAR_PICKER));
            return calendar.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Close calendar if it's open
     */
    public void closeCalendar() {
        try {
            if (isCalendarOpen()) {
                // Click outside the calendar or press escape
                WebElement body = driver.findElement(By.tagName("body"));
                body.click();
            }
        } catch (Exception e) {
            System.out.println("Failed to close calendar: " + e.getMessage());
        }
    }
}
