package com.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.By;
import java.time.LocalDate;
import java.util.*;

import com.automation.base.BasePage;
import com.automation.utils.SpiceJetCalendarUtils;

public class SpiceJetPage extends BasePage {
    ////div[@data-testid="application-id"]/div[1]/div[3]/div[2]/div[2]/div/div[2]/div[2]/div/div[1]/div[2]
    @FindBy(xpath = "//div[@data-testid='one-way-radio-button']")
    private WebElement onewayRadioButton;
    
    @FindBy(xpath="//div[@data-testid='to-testID-origin']/div/div[2]")
    private WebElement originField;

    @FindBy(xpath="//div[@data-testid='to-testID-origin']/div[2]/div[2]/div[2]/div[2]/div[2]/div[2]")
    private WebElement selectOrigin;

    @FindBy(xpath="//div[@data-testid='to-testID-destination']/div/div[2]")
    private WebElement destinationField;

    @FindBy(xpath="//div[@data-testid='to-testID-destination']/div/div[2]/div[2]/div[2]/div[2]")
    private WebElement listOfDestinationCities;

    @FindBy(xpath="//div[@data-testid= 'undefined-calendar-picker']")
    private WebElement calendarPicker;

    @FindBy(xpath="//div[@data-testid= 'undefined-calendar-picker']/div[1]")
    private WebElement calendarPickerNextArrow;

    @FindBy(xpath="//div[@data-testid= 'undefined-calendar-picker']/div[2]")
    private WebElement calendarPickerPreviousArrow;
    
    // Additional useful locators
    @FindBy(xpath="//div[@data-testid='round-trip-radio-button']")
    private WebElement roundTripRadioButton;
    
    @FindBy(xpath="//div[@data-testid='departure-date-dropdown-label-test-id']")
    private WebElement departureDateField;
    
    @FindBy(xpath="//div[@data-testid='return-date-dropdown-label-test-id']")
    private WebElement returnDateField;
    
    @FindBy(xpath="//div[@data-testid= 'home-page-flight-cta']/div[2]']")
    private WebElement searchFlightsButton;
    
    // Calendar utility instance
    private SpiceJetCalendarUtils calendarUtils;
    
    // Constructor
    public SpiceJetPage(WebDriver driver) {
        super(driver);
        this.calendarUtils = new SpiceJetCalendarUtils(driver);
    }
    
    // Page Actions
    
    /**
     * Select one way trip
     */
    public SpiceJetPage selectOneWay() {
        waitForElementToBeClickable(onewayRadioButton);
        onewayRadioButton.click();
        return this;
    }
    
    /**
     * Select round trip
     */
    public SpiceJetPage selectRoundTrip() {
        waitForElementToBeClickable(roundTripRadioButton);
        roundTripRadioButton.click();
        return this;
    }
    
    /**
     * Click origin field to open origin selection
     */
    public SpiceJetPage clickOriginField() {
        waitForElementToBeClickable(originField);
        originField.click();
        return this;
    }
    
    /**
     * Select origin city by clicking on it
     */
    public SpiceJetPage selectOrigin() {
        waitForElementToBeClickable(selectOrigin);
        selectOrigin.click();
        return this;
    }
    
    /**
     * Select origin city by visible text
     * @param cityName - visible text of the city (e.g., "Delhi", "Mumbai", "Chennai")
     */
    public SpiceJetPage selectOriginByText(String cityName) {
        clickOriginField();
        try {
            // Wait a moment for dropdown to appear
            Thread.sleep(1000);
            
            // Find city by visible text
            String cityXpath = "//*[contains(text(),'" + cityName + "')]";
            WebElement cityElement = driver.findElement(By.xpath(cityXpath));
            waitForElementToBeClickable(cityElement);
            cityElement.click();
            
        } catch (Exception e) {
            System.out.println("Failed to select origin city: " + cityName + " - " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Click destination field to open destination selection
     */
    public SpiceJetPage clickDestinationField() {
        waitForElementToBeClickable(destinationField);
        destinationField.click();
        return this;
    }
    
    /**
     * Select destination from the list
     */
    public SpiceJetPage selectDestination() {
        waitForElementToBeClickable(listOfDestinationCities);
        listOfDestinationCities.click();
        return this;
    }
    
    /**
     * Select destination city by visible text
     * @param cityName - visible text of the city (e.g., "Delhi", "Mumbai", "Chennai")
     */
    public SpiceJetPage selectDestinationByText(String cityName) {
        clickDestinationField();
        try {
            // Wait a moment for dropdown to appear
            Thread.sleep(1000);
            
            // Find city by visible text with multiple strategies
            WebElement cityElement = findCityByText(cityName);
            waitForElementToBeClickable(cityElement);
            cityElement.click();
            
        } catch (Exception e) {
            System.out.println("Failed to select destination city: " + cityName + " - " + e.getMessage());
        }
        return this;
    }
    
    /**
     * Find city element by visible text using multiple strategies
     * @param cityName - city name to find
     * @return WebElement of the city
     */
    private WebElement findCityByText(String cityName) {
        // Strategy 1: Exact text match
        try {
            String exactTextXpath = "//*[text()='" + cityName + "']";
            return driver.findElement(By.xpath(exactTextXpath));
        } catch (Exception e) {
            // Continue to next strategy
        }
        
        // Strategy 2: Contains text match
        try {
            String containsTextXpath = "//*[contains(text(),'" + cityName + "')]";
            return driver.findElement(By.xpath(containsTextXpath));
        } catch (Exception e) {
            // Continue to next strategy
        }
        
        // Strategy 3: Case insensitive match
        try {
            String caseInsensitiveXpath = "//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + cityName.toLowerCase() + "')]";
            return driver.findElement(By.xpath(caseInsensitiveXpath));
        } catch (Exception e) {
            // Continue to next strategy
        }
        
        // Strategy 4: Partial match (for cities with airport codes)
        try {
            String partialXpath = "//*[contains(text(),'" + cityName.substring(0, Math.min(3, cityName.length())) + "')]";
            return driver.findElement(By.xpath(partialXpath));
        } catch (Exception e) {
            // Continue to next strategy
        }
        
        // Strategy 5: Search within specific dropdown areas
        try {
            String dropdownXpath = "//div[@data-testid='to-testID-destination']//descendant::*[contains(text(),'" + cityName + "')]";
            return driver.findElement(By.xpath(dropdownXpath));
        } catch (Exception e) {
            // Continue to next strategy
        }
        
        // Strategy 6: Find by aria-label or title attributes
        try {
            String attributeXpath = "//*[@aria-label='" + cityName + "' or @title='" + cityName + "']";
            return driver.findElement(By.xpath(attributeXpath));
        } catch (Exception e) {
            throw new RuntimeException("Could not find city: " + cityName + " using any strategy");
        }
    }
    
    /**
     * Click departure date field to open calendar
     */
    public SpiceJetPage clickDepartureDateField() {
        waitForElementToBeClickable(departureDateField);
        departureDateField.click();
        return this;
    }
    
    /**
     * Click return date field to open calendar
     */
    public SpiceJetPage clickReturnDateField() {
        waitForElementToBeClickable(returnDateField);
        returnDateField.click();
        return this;
    }
    
    /**
     * Select departure date using your calendar locators
     * @param day - day to select (1-31)
     * @param month - month name (JANUARY, FEBRUARY, etc.)
     * @param year - year (2024, 2025, etc.)
     */
    public SpiceJetPage selectDepartureDate(int day, String month, int year) {
        clickDepartureDateField();
        calendarUtils.selectDate(day, month, year);
        return this;
    }
    
    /**
     * Select departure date using LocalDate
     * @param date - LocalDate object
     */
    public SpiceJetPage selectDepartureDate(LocalDate date) {
        clickDepartureDateField();
        calendarUtils.selectDate(date);
        return this;
    }
    
    /**
     * Select departure date using string format (dd-MM-yyyy)
     * @param dateString - date in dd-MM-yyyy format
     */
    public SpiceJetPage selectDepartureDate(String dateString) {
        clickDepartureDateField();
        calendarUtils.selectDate(dateString);
        return this;
    }
    
    /**
     * Select return date using your calendar locators
     * @param day - day to select (1-31)
     * @param month - month name (JANUARY, FEBRUARY, etc.)
     * @param year - year (2024, 2025, etc.)
     */
    public SpiceJetPage selectReturnDate(int day, String month, int year) {
        clickReturnDateField();
        calendarUtils.selectDate(day, month, year);
        return this;
    }
    
    /**
     * Select return date using LocalDate
     * @param date - LocalDate object
     */
    public SpiceJetPage selectReturnDate(LocalDate date) {
        clickReturnDateField();
        calendarUtils.selectDate(date);
        return this;
    }
    
    /**
     * Select return date using string format (dd-MM-yyyy)
     * @param dateString - date in dd-MM-yyyy format
     */
    public SpiceJetPage selectReturnDate(String dateString) {
        clickReturnDateField();
        calendarUtils.selectDate(dateString);
        return this;
    }
    
    /**
     * Select departure date relative to today
     * @param daysFromToday - number of days from today
     */
    public SpiceJetPage selectDepartureDateRelativeToToday(int daysFromToday) {
        clickDepartureDateField();
        calendarUtils.selectDateRelativeToToday(daysFromToday);
        return this;
    }
    
    /**
     * Select return date relative to today
     * @param daysFromToday - number of days from today
     */
    public SpiceJetPage selectReturnDateRelativeToToday(int daysFromToday) {
        clickReturnDateField();
        calendarUtils.selectDateRelativeToToday(daysFromToday);
        return this;
    }
    
    /**
     * Navigate to next month in calendar using your locator
     */
    public SpiceJetPage navigateToNextMonth() {
        waitForElementToBeClickable(calendarPickerNextArrow);
        calendarPickerNextArrow.click();
        return this;
    }
    
    /**
     * Navigate to previous month in calendar using your locator
     */
    public SpiceJetPage navigateToPreviousMonth() {
        waitForElementToBeClickable(calendarPickerPreviousArrow);
        calendarPickerPreviousArrow.click();
        return this;
    }
    
    /**
     * Open calendar picker directly using your locator
     */
    public SpiceJetPage openCalendarPicker() {
        waitForElementToBeClickable(calendarPicker);
        calendarPicker.click();
        return this;
    }
    
    /**
     * Search for flights
     */
    public void searchFlights() {
        waitForElementToBeClickable(searchFlightsButton);
        searchFlightsButton.click();
    }
    
    /**
     * Complete booking flow with calendar selection
     * @param tripType - "oneway" or "roundtrip"
     * @param departureDate - departure date
     * @param returnDate - return date (null for one way)
     */
    public void completeBooking(String tripType, LocalDate departureDate, LocalDate returnDate) {
        // Select trip type
        if ("roundtrip".equalsIgnoreCase(tripType)) {
            selectRoundTrip();
        } else {
            selectOneWay();
        }
        
        // Select origin and destination
        clickOriginField();
        selectOrigin();
        
        clickDestinationField();
        selectDestination();
        
        // Select dates using your calendar
        selectDepartureDate(departureDate);
        
        if (returnDate != null && "roundtrip".equalsIgnoreCase(tripType)) {
            selectReturnDate(returnDate);
        }
        
        // Search flights
        searchFlights();
    }
    
    /**
     * Complete booking flow with text-based city selection and calendar
     * @param tripType - "oneway" or "roundtrip"
     * @param originCity - origin city name (e.g., "Delhi", "Mumbai")
     * @param destinationCity - destination city name (e.g., "Chennai", "Bangalore")
     * @param departureDate - departure date
     * @param returnDate - return date (null for one way)
     */
    public void completeBookingWithCityNames(String tripType, String originCity, String destinationCity, 
                                           LocalDate departureDate, LocalDate returnDate) {
        // Select trip type
        if ("roundtrip".equalsIgnoreCase(tripType)) {
            selectRoundTrip();
        } else {
            selectOneWay();
        }
        
        // Select origin and destination by text
        selectOriginByText(originCity);
        selectDestinationByText(destinationCity);
        
        // Select dates using your calendar
        selectDepartureDate(departureDate);
        
        if (returnDate != null && "roundtrip".equalsIgnoreCase(tripType)) {
            selectReturnDate(returnDate);
        }
        
        // Search flights
        searchFlights();
    }
    
    /**
     * Search for available cities in origin dropdown
     * @param searchText - text to search for
     * @return list of available city names
     */
    public List<String> getAvailableOriginCities(String searchText) {
        List<String> cities = new ArrayList<>();
        
        try {
            clickOriginField();
            Thread.sleep(1000);
            
            // Find all city elements
            List<WebElement> cityElements = driver.findElements(
                By.xpath("//div[@data-testid='to-testID-origin']//descendant::*[contains(text(),'" + searchText + "')]"));
            
            for (WebElement element : cityElements) {
                String cityText = element.getText().trim();
                if (!cityText.isEmpty()) {
                    cities.add(cityText);
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to get available cities: " + e.getMessage());
        }
        
        return cities;
    }
    
    /**
     * Search for available cities in destination dropdown
     * @param searchText - text to search for
     * @return list of available city names
     */
    public List<String> getAvailableDestinationCities(String searchText) {
        List<String> cities = new ArrayList<>();
        
        try {
            clickDestinationField();
            Thread.sleep(1000);
            
            // Find all city elements
            List<WebElement> cityElements = driver.findElements(
                By.xpath("//div[@data-testid='to-testID-destination']//descendant::*[contains(text(),'" + searchText + "')]"));
            
            for (WebElement element : cityElements) {
                String cityText = element.getText().trim();
                if (!cityText.isEmpty()) {
                    cities.add(cityText);
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to get available cities: " + e.getMessage());
        }
        
        return cities;
    }
}
