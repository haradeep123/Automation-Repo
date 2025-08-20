package com.automation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Extent Reports Manager for test reporting
 */
public class ExtentManager {
    
    private static ExtentReports extent;
    private static ExtentSparkReporter sparkReporter;
    private static String reportPath;
    
    /**
     * Initialize Extent Reports
     */
    public static void initializeReport() {
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport_" + timestamp + ".html";
            
            sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("Automation Test Report");
            sparkReporter.config().setReportName("Test Execution Report");
            sparkReporter.config().setTheme(Theme.STANDARD);
            
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User Name", System.getProperty("user.name"));
        }
    }
    
    /**
     * Create a new test in the report
     */
    public static ExtentTest createTest(String testName) {
        return extent.createTest(testName);
    }
    
    /**
     * Create a new test with description
     */
    public static ExtentTest createTest(String testName, String description) {
        return extent.createTest(testName, description);
    }
    
    /**
     * Flush the report
     */
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
    
    /**
     * Get extent reports instance
     */
    public static ExtentReports getExtentReports() {
        return extent;
    }
    
    /**
     * Get report path
     */
    public static String getReportPath() {
        return reportPath;
    }
}
