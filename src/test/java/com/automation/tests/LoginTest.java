package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.HomePage;
import com.automation.pages.LoginPage;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Login Test class containing login related test cases
 */
public class LoginTest extends BaseTest {
    
    private LoginPage loginPage;
    private HomePage homePage;
    
    @Test(priority = 1, description = "Verify successful login with valid credentials")
    public void testValidLogin() {
        test.log(Status.INFO, "Starting valid login test");
        
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        
        // Perform login
        loginPage.login("testuser@example.com", "password123");
        test.log(Status.INFO, "Entered valid credentials and clicked login");
        
        // Verify successful login
        Assert.assertTrue(homePage.isUserLoggedIn(), "User should be logged in successfully");
        test.log(Status.PASS, "Login successful - user is logged in");
        
        // Verify welcome message
        String welcomeMessage = homePage.getWelcomeMessage();
        Assert.assertTrue(welcomeMessage.contains("Welcome"), "Welcome message should be displayed");
        test.log(Status.PASS, "Welcome message verified: " + welcomeMessage);
    }
    
    @Test(priority = 2, description = "Verify login failure with invalid credentials")
    public void testInvalidLogin() {
        test.log(Status.INFO, "Starting invalid login test");
        
        loginPage = new LoginPage(driver);
        
        // Perform login with invalid credentials
        loginPage.login("invalid@example.com", "wrongpassword");
        test.log(Status.INFO, "Entered invalid credentials and clicked login");
        
        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        test.log(Status.PASS, "Error message is displayed for invalid credentials");
        
        // Verify error message text
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Invalid"), "Error message should contain 'Invalid'");
        test.log(Status.PASS, "Error message verified: " + errorMessage);
    }
    
    @Test(priority = 3, description = "Verify login with empty credentials")
    public void testEmptyCredentials() {
        test.log(Status.INFO, "Starting empty credentials test");
        
        loginPage = new LoginPage(driver);
        
        // Try to login with empty credentials
        loginPage.login("", "");
        test.log(Status.INFO, "Attempted login with empty credentials");
        
        // Verify login button state or error message
        Assert.assertTrue(loginPage.isErrorMessageDisplayed() || !loginPage.isLoginButtonEnabled(), 
                         "Error should be shown or login button should be disabled");
        test.log(Status.PASS, "Empty credentials validation working correctly");
    }
    
    @Test(dataProvider = "loginData", priority = 4, description = "Verify login with multiple test data")
    public void testLoginWithMultipleData(String username, String password, String expectedResult) {
        test.log(Status.INFO, "Testing login with username: " + username);
        
        loginPage = new LoginPage(driver);
        loginPage.login(username, password);
        
        if (expectedResult.equals("success")) {
            homePage = new HomePage(driver);
            Assert.assertTrue(homePage.isUserLoggedIn(), "Login should be successful");
            test.log(Status.PASS, "Login successful for: " + username);
        } else {
            Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Login should fail");
            test.log(Status.PASS, "Login failed as expected for: " + username);
        }
    }
    
    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return new Object[][] {
            {"valid@example.com", "validpassword", "success"},
            {"invalid@example.com", "invalidpassword", "failure"},
            {"", "", "failure"},
            {"valid@example.com", "", "failure"},
            {"", "validpassword", "failure"}
        };
    }
}
