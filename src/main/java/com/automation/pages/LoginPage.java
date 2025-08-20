package com.automation.pages;

import com.automation.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Login Page Object Model class
 */
public class LoginPage extends BasePage {
    
    // Page Elements
    @FindBy(id = "username")
    private WebElement usernameField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(id = "loginButton")
    private WebElement loginButton;
    
    @FindBy(xpath = "//div[@class='error-message']")
    private WebElement errorMessage;
    
    @FindBy(linkText = "Forgot Password?")
    private WebElement forgotPasswordLink;
    
    @FindBy(id = "rememberMe")
    private WebElement rememberMeCheckbox;
    
    // Constructor
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    // Page Actions
    
    /**
     * Enter username
     */
    public LoginPage enterUsername(String username) {
        waitForElementToBeVisible(usernameField);
        usernameField.clear();
        usernameField.sendKeys(username);
        return this;
    }
    
    /**
     * Enter password
     */
    public LoginPage enterPassword(String password) {
        waitForElementToBeVisible(passwordField);
        passwordField.clear();
        passwordField.sendKeys(password);
        return this;
    }
    
    /**
     * Click login button
     */
    public void clickLoginButton() {
        waitForElementToBeClickable(loginButton);
        loginButton.click();
    }
    
    /**
     * Login with credentials
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }
    
    /**
     * Click forgot password link
     */
    public void clickForgotPasswordLink() {
        waitForElementToBeClickable(forgotPasswordLink);
        forgotPasswordLink.click();
    }
    
    /**
     * Check remember me checkbox
     */
    public LoginPage checkRememberMe() {
        if (!rememberMeCheckbox.isSelected()) {
            rememberMeCheckbox.click();
        }
        return this;
    }
    
    /**
     * Get error message text
     */
    public String getErrorMessage() {
        waitForElementToBeVisible(errorMessage);
        return errorMessage.getText();
    }
    
    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if login button is enabled
     */
    public boolean isLoginButtonEnabled() {
        return loginButton.isEnabled();
    }
}
