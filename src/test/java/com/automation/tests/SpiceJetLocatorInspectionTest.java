package com.automation.tests;

import com.automation.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import java.util.List;

/**
 * Test class to inspect SpiceJet calendar structure and find correct locators
 */
public class SpiceJetLocatorInspectionTest extends BaseTest {
    
    @Test(description = "Inspect SpiceJet calendar structure")
    public void inspectCalendarStructure() {
        try {
            // Navigate to SpiceJet
            driver.get("https://www.spicejet.com/");
            Thread.sleep(3000);
            
            System.out.println("=== SPICEJET CALENDAR INSPECTION ===");
            
            // First, let's find and click the departure date field
            try {
                WebElement departureDateField = driver.findElement(
                    By.xpath("//div[contains(@data-testid,'departure') or contains(@class,'departure')]"));
                System.out.println("Found departure date field: " + departureDateField.getAttribute("outerHTML"));
                departureDateField.click();
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("Could not find departure date field with standard selectors, trying alternatives...");
                
                // Try finding any clickable date-related elements
                List<WebElement> dateElements = driver.findElements(By.xpath("//*[contains(text(),'Departure') or contains(@placeholder,'departure') or contains(@data-testid,'date')]"));
                for (int i = 0; i < Math.min(5, dateElements.size()); i++) {
                    WebElement element = dateElements.get(i);
                    System.out.println("Date element " + i + ": " + element.getTagName() + " - " + element.getAttribute("outerHTML"));
                }
                
                if (!dateElements.isEmpty()) {
                    dateElements.get(0).click();
                    Thread.sleep(2000);
                }
            }
            
            // Now let's look for calendar elements
            System.out.println("\n=== CALENDAR ELEMENTS ===");
            
            // Look for any calendar-related elements
            List<WebElement> calendarElements = driver.findElements(By.xpath("//*[contains(@class,'calendar') or contains(@class,'datepicker') or contains(@data-testid,'calendar')]"));
            System.out.println("Found " + calendarElements.size() + " calendar elements:");
            
            for (int i = 0; i < Math.min(10, calendarElements.size()); i++) {
                WebElement element = calendarElements.get(i);
                System.out.println("Calendar element " + i + ": " + element.getTagName() + " - " + element.getAttribute("class") + " - " + element.getAttribute("data-testid"));
            }
            
            // Look for navigation arrows
            System.out.println("\n=== NAVIGATION ARROWS ===");
            List<WebElement> arrows = driver.findElements(By.xpath("//*[contains(@class,'arrow') or contains(@class,'next') or contains(@class,'prev') or contains(text(),'❮') or contains(text(),'❯') or contains(text(),'<') or contains(text(),'>')]"));
            System.out.println("Found " + arrows.size() + " potential navigation elements:");
            
            for (int i = 0; i < Math.min(10, arrows.size()); i++) {
                WebElement element = arrows.get(i);
                System.out.println("Arrow element " + i + ": " + element.getTagName() + " - " + element.getAttribute("class") + " - Text: " + element.getText());
            }
            
            // Look for month/year displays
            System.out.println("\n=== MONTH/YEAR DISPLAYS ===");
            List<WebElement> monthYear = driver.findElements(By.xpath("//*[contains(text(),'2024') or contains(text(),'2025') or contains(text(),'January') or contains(text(),'February') or contains(text(),'March') or contains(text(),'April') or contains(text(),'May') or contains(text(),'June') or contains(text(),'July') or contains(text(),'August') or contains(text(),'September') or contains(text(),'October') or contains(text(),'November') or contains(text(),'December')]"));
            System.out.println("Found " + monthYear.size() + " month/year elements:");
            
            for (int i = 0; i < Math.min(5, monthYear.size()); i++) {
                WebElement element = monthYear.get(i);
                System.out.println("Month/Year element " + i + ": " + element.getTagName() + " - " + element.getAttribute("class") + " - Text: " + element.getText());
            }
            
            // Look for day elements
            System.out.println("\n=== DAY ELEMENTS ===");
            List<WebElement> days = driver.findElements(By.xpath("//*[text()='1' or text()='2' or text()='3' or text()='15' or text()='25']"));
            System.out.println("Found " + days.size() + " potential day elements:");
            
            for (int i = 0; i < Math.min(5, days.size()); i++) {
                WebElement element = days.get(i);
                System.out.println("Day element " + i + ": " + element.getTagName() + " - " + element.getAttribute("class") + " - Text: " + element.getText() + " - Parent: " + element.findElement(By.xpath("..")).getAttribute("class"));
            }
            
            // Check for your specific locator
            System.out.println("\n=== CHECKING YOUR LOCATORS ===");
            try {
                WebElement yourCalendar = driver.findElement(By.xpath("//div[@data-testid= 'undefined-calendar-picker']"));
                System.out.println("Your calendar locator found: " + yourCalendar.getAttribute("outerHTML"));
            } catch (Exception e) {
                System.out.println("Your calendar locator NOT found: " + e.getMessage());
            }
            
            try {
                List<WebElement> undefinedElements = driver.findElements(By.xpath("//*[contains(@data-testid,'undefined')]"));
                System.out.println("Found " + undefinedElements.size() + " elements with 'undefined' in data-testid");
                for (WebElement element : undefinedElements) {
                    System.out.println("Undefined element: " + element.getTagName() + " - " + element.getAttribute("data-testid"));
                }
            } catch (Exception e) {
                System.out.println("No undefined elements found");
            }
            
        } catch (Exception e) {
            System.out.println("Error during inspection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test(description = "Try to find correct departure date locator")
    public void findDepartureDateLocator() {
        try {
            driver.get("https://www.spicejet.com/");
            Thread.sleep(3000);
            
            System.out.println("\n=== FINDING DEPARTURE DATE FIELD ===");
            
            // Multiple strategies to find departure date field
            String[] departureDateSelectors = {
                "//input[contains(@placeholder,'Departure') or contains(@placeholder,'departure')]",
                "//div[contains(@data-testid,'departure')]",
                "//div[contains(@class,'departure')]",
                "//div[contains(text(),'Departure')]/..//input",
                "//div[contains(text(),'Departure')]",
                "//*[@id and contains(@id,'departure')]",
                "//*[@name and contains(@name,'departure')]"
            };
            
            for (String selector : departureDateSelectors) {
                try {
                    List<WebElement> elements = driver.findElements(By.xpath(selector));
                    if (!elements.isEmpty()) {
                        System.out.println("Selector: " + selector + " found " + elements.size() + " elements:");
                        for (int i = 0; i < Math.min(3, elements.size()); i++) {
                            WebElement element = elements.get(i);
                            System.out.println("  Element " + i + ": " + element.getTagName() + " - " + element.getAttribute("outerHTML"));
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Selector failed: " + selector + " - " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error finding departure date: " + e.getMessage());
        }
    }
}
