package com.automation.pages;

import com.automation.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Home Page Object Model class
 */
public class HomePage extends BasePage {
    
    // Page Elements
    @FindBy(id = "welcomeMessage")
    private WebElement welcomeMessage;
    
    @FindBy(id = "userProfile")
    private WebElement userProfileDropdown;
    
    @FindBy(id = "logout")
    private WebElement logoutButton;
    
    @FindBy(xpath = "//nav[@class='main-navigation']")
    private WebElement mainNavigation;
    
    @FindBy(linkText = "Dashboard")
    private WebElement dashboardLink;
    
    @FindBy(linkText = "Products")
    private WebElement productsLink;
    
    @FindBy(linkText = "Orders")
    private WebElement ordersLink;
    
    @FindBy(linkText = "Settings")
    private WebElement settingsLink;
    
    @FindBy(id = "searchBox")
    private WebElement searchBox;
    
    @FindBy(id = "searchButton")
    private WebElement searchButton;
    
    // Constructor
    public HomePage(WebDriver driver) {
        super(driver);
    }
    
    // Page Actions
    
    /**
     * Get welcome message text
     */
    public String getWelcomeMessage() {
        waitForElementToBeVisible(welcomeMessage);
        return welcomeMessage.getText();
    }
    
    /**
     * Click user profile dropdown
     */
    public HomePage clickUserProfile() {
        waitForElementToBeClickable(userProfileDropdown);
        userProfileDropdown.click();
        return this;
    }
    
    /**
     * Click logout button
     */
    public void logout() {
        clickUserProfile();
        waitForElementToBeClickable(logoutButton);
        logoutButton.click();
    }
    
    /**
     * Navigate to Dashboard
     */
    public void navigateToDashboard() {
        waitForElementToBeClickable(dashboardLink);
        dashboardLink.click();
    }
    
    /**
     * Navigate to Products
     */
    public void navigateToProducts() {
        waitForElementToBeClickable(productsLink);
        productsLink.click();
    }
    
    /**
     * Navigate to Orders
     */
    public void navigateToOrders() {
        waitForElementToBeClickable(ordersLink);
        ordersLink.click();
    }
    
    /**
     * Navigate to Settings
     */
    public void navigateToSettings() {
        waitForElementToBeClickable(settingsLink);
        settingsLink.click();
    }
    
    /**
     * Perform search
     */
    public void search(String searchTerm) {
        waitForElementToBeVisible(searchBox);
        searchBox.clear();
        searchBox.sendKeys(searchTerm);
        waitForElementToBeClickable(searchButton);
        searchButton.click();
    }
    
    /**
     * Check if user is logged in (welcome message is displayed)
     */
    public boolean isUserLoggedIn() {
        try {
            waitForElementToBeVisible(welcomeMessage);
            return welcomeMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if main navigation is displayed
     */
    public boolean isMainNavigationDisplayed() {
        try {
            return mainNavigation.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
