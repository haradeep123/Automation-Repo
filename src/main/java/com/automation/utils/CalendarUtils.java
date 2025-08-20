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
 * Calendar Utilities for automating date selection in various calendar widgets
 */
public class CalendarUtils {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    public CalendarUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    /**
     * Select date from a standard calendar widget
     * @param day - day to select (1-31)
     * @param month - month name (January, February, etc.)
     * @param year - year (2024, 2025, etc.)
     */
    public void selectDate(int day, String month, int year) {
        // Navigate to correct year
        navigateToYear(year);
        
        // Navigate to correct month
        navigateToMonth(month);
        
        // Select the day
        selectDay(day);
    }
    
    /**
     * Select date using LocalDate object
     * @param date - LocalDate object
     */
    public void selectDate(LocalDate date) {
        int day = date.getDayOfMonth();
        String month = date.getMonth().name();
        int year = date.getYear();
        
        selectDate(day, month, year);
    }
    
    /**
     * Select date from string format (dd-MM-yyyy or dd/MM/yyyy)
     * @param dateString - date in string format
     * @param format - date format pattern
     */
    public void selectDate(String dateString, String format) {
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(format));
        selectDate(date);
    }
    
    /**
     * Navigate to specific year in calendar
     * @param targetYear - year to navigate to
     */
    private void navigateToYear(int targetYear) {
        try {
            // Common year navigation patterns
            WebElement yearElement = null;
            
            // Try different selectors for year
            try {
                yearElement = driver.findElement(By.xpath("//select[@class='ui-datepicker-year']"));
                selectFromDropdown(yearElement, String.valueOf(targetYear));
                return;
            } catch (Exception e) {
                // Continue to next approach
            }
            
            try {
                yearElement = driver.findElement(By.xpath("//*[@class='year' or contains(@class,'year')]"));
                if (yearElement.getTagName().equals("select")) {
                    selectFromDropdown(yearElement, String.valueOf(targetYear));
                    return;
                }
            } catch (Exception e) {
                // Continue to next approach
            }
            
            // Navigate using year buttons
            navigateUsingYearButtons(targetYear);
            
        } catch (Exception e) {
            System.out.println("Could not navigate to year: " + targetYear + ". Error: " + e.getMessage());
        }
    }
    
    /**
     * Navigate to specific month in calendar
     * @param targetMonth - month name to navigate to
     */
    private void navigateToMonth(String targetMonth) {
        try {
            // Common month navigation patterns
            WebElement monthElement = null;
            
            // Try month dropdown
            try {
                monthElement = driver.findElement(By.xpath("//select[@class='ui-datepicker-month']"));
                selectFromDropdown(monthElement, targetMonth);
                return;
            } catch (Exception e) {
                // Continue to next approach
            }
            
            try {
                monthElement = driver.findElement(By.xpath("//*[@class='month' or contains(@class,'month')]"));
                if (monthElement.getTagName().equals("select")) {
                    selectFromDropdown(monthElement, targetMonth);
                    return;
                }
            } catch (Exception e) {
                // Continue to next approach
            }
            
            // Navigate using month buttons
            navigateUsingMonthButtons(targetMonth);
            
        } catch (Exception e) {
            System.out.println("Could not navigate to month: " + targetMonth + ". Error: " + e.getMessage());
        }
    }
    
    /**
     * Select specific day from calendar
     * @param day - day number to select
     */
    private void selectDay(int day) {
        try {
            // Common day selection patterns
            List<WebElement> dayElements = null;
            
            // Try different day selectors
            String[] daySelectors = {
                "//td[@data-day='" + day + "']",
                "//td[text()='" + day + "']",
                "//span[text()='" + day + "']",
                "//a[text()='" + day + "']",
                "//div[text()='" + day + "']",
                "//*[@class='day' or contains(@class,'day')][text()='" + day + "']",
                "//*[@role='gridcell'][text()='" + day + "']"
            };
            
            for (String selector : daySelectors) {
                try {
                    WebElement dayElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(selector)));
                    dayElement.click();
                    return;
                } catch (Exception e) {
                    // Continue to next selector
                }
            }
            
            // If specific selectors fail, try finding all day elements
            dayElements = driver.findElements(By.xpath("//td[not(@class) or not(contains(@class,'disabled'))]"));
            for (WebElement element : dayElements) {
                if (element.getText().trim().equals(String.valueOf(day))) {
                    wait.until(ExpectedConditions.elementToBeClickable(element));
                    element.click();
                    return;
                }
            }
            
        } catch (Exception e) {
            System.out.println("Could not select day: " + day + ". Error: " + e.getMessage());
        }
    }
    
    /**
     * Navigate using year navigation buttons
     * @param targetYear - target year
     */
    private void navigateUsingYearButtons(int targetYear) {
        try {
            // Get current year from calendar
            int currentYear = getCurrentYear();
            
            while (currentYear != targetYear) {
                if (currentYear < targetYear) {
                    // Click next year button
                    clickNextYear();
                    currentYear++;
                } else {
                    // Click previous year button
                    clickPreviousYear();
                    currentYear--;
                }
                
                // Add small delay to avoid too fast navigation
                Thread.sleep(200);
            }
        } catch (Exception e) {
            System.out.println("Error navigating years with buttons: " + e.getMessage());
        }
    }
    
    /**
     * Navigate using month navigation buttons
     * @param targetMonth - target month
     */
    private void navigateUsingMonthButtons(String targetMonth) {
        try {
            int maxAttempts = 12; // Prevent infinite loop
            int attempts = 0;
            
            while (!getCurrentMonth().equalsIgnoreCase(targetMonth) && attempts < maxAttempts) {
                if (isTargetMonthAfterCurrent(targetMonth)) {
                    clickNextMonth();
                } else {
                    clickPreviousMonth();
                }
                attempts++;
                Thread.sleep(200);
            }
        } catch (Exception e) {
            System.out.println("Error navigating months with buttons: " + e.getMessage());
        }
    }
    
    /**
     * Get current year from calendar
     * @return current year
     */
    private int getCurrentYear() {
        try {
            String[] yearSelectors = {
                "//*[@class='ui-datepicker-year']",
                "//*[contains(@class,'year')]",
                "//*[@class='year']"
            };
            
            for (String selector : yearSelectors) {
                try {
                    WebElement yearElement = driver.findElement(By.xpath(selector));
                    String yearText = yearElement.getText();
                    return Integer.parseInt(yearText.replaceAll("[^0-9]", ""));
                } catch (Exception e) {
                    // Continue to next selector
                }
            }
        } catch (Exception e) {
            System.out.println("Could not get current year: " + e.getMessage());
        }
        return LocalDate.now().getYear(); // Return current year as fallback
    }
    
    /**
     * Get current month from calendar
     * @return current month name
     */
    private String getCurrentMonth() {
        try {
            String[] monthSelectors = {
                "//*[@class='ui-datepicker-month']",
                "//*[contains(@class,'month')]",
                "//*[@class='month']"
            };
            
            for (String selector : monthSelectors) {
                try {
                    WebElement monthElement = driver.findElement(By.xpath(selector));
                    return monthElement.getText().trim();
                } catch (Exception e) {
                    // Continue to next selector
                }
            }
        } catch (Exception e) {
            System.out.println("Could not get current month: " + e.getMessage());
        }
        return LocalDate.now().getMonth().name(); // Return current month as fallback
    }
    
    /**
     * Click next month button
     */
    private void clickNextMonth() {
        String[] nextSelectors = {
            "//a[@class='ui-datepicker-next']",
            "//*[contains(@class,'next')]",
            "//*[@aria-label='Next']",
            "//*[text()='Next']",
            "//*[text()='>']"
        };
        
        clickNavigationButton(nextSelectors, "next month");
    }
    
    /**
     * Click previous month button
     */
    private void clickPreviousMonth() {
        String[] prevSelectors = {
            "//a[@class='ui-datepicker-prev']",
            "//*[contains(@class,'prev')]",
            "//*[@aria-label='Previous']",
            "//*[text()='Previous']",
            "//*[text()='<']"
        };
        
        clickNavigationButton(prevSelectors, "previous month");
    }
    
    /**
     * Click next year button
     */
    private void clickNextYear() {
        String[] nextYearSelectors = {
            "//*[contains(@class,'next-year')]",
            "//*[@aria-label='Next Year']",
            "//*[text()='>>']"
        };
        
        clickNavigationButton(nextYearSelectors, "next year");
    }
    
    /**
     * Click previous year button
     */
    private void clickPreviousYear() {
        String[] prevYearSelectors = {
            "//*[contains(@class,'prev-year')]",
            "//*[@aria-label='Previous Year']",
            "//*[text()='<<']"
        };
        
        clickNavigationButton(prevYearSelectors, "previous year");
    }
    
    /**
     * Generic method to click navigation buttons
     * @param selectors - array of selectors to try
     * @param buttonType - type of button for logging
     */
    private void clickNavigationButton(String[] selectors, String buttonType) {
        for (String selector : selectors) {
            try {
                WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(selector)));
                button.click();
                return;
            } catch (Exception e) {
                // Continue to next selector
            }
        }
        System.out.println("Could not find " + buttonType + " button");
    }
    
    /**
     * Select option from dropdown
     * @param dropdown - dropdown element
     * @param value - value to select
     */
    private void selectFromDropdown(WebElement dropdown, String value) {
        List<WebElement> options = dropdown.findElements(By.tagName("option"));
        for (WebElement option : options) {
            if (option.getText().equals(value) || option.getAttribute("value").equals(value)) {
                option.click();
                return;
            }
        }
    }
    
    /**
     * Check if target month comes after current month
     * @param targetMonth - target month name
     * @return true if target month is after current month
     */
    private boolean isTargetMonthAfterCurrent(String targetMonth) {
        String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
                          "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
        
        String currentMonth = getCurrentMonth().toUpperCase();
        targetMonth = targetMonth.toUpperCase();
        
        int currentIndex = -1, targetIndex = -1;
        
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(currentMonth)) currentIndex = i;
            if (months[i].equals(targetMonth)) targetIndex = i;
        }
        
        return targetIndex > currentIndex;
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
}
