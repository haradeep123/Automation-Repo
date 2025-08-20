# 🚀 SpiceJet E2E Automation Framework

![E2E Tests](https://github.com/haradeep123/Automation-Repo/workflows/SpiceJet%20E2E%20Tests/badge.svg)
![Java](https://img.shields.io/badge/Java-11-orange.svg)
![Selenium](https://img.shields.io/badge/Selenium-4.15.0-green.svg)
![TestNG](https://img.shields.io/badge/TestNG-7.8.0-red.svg)
![Maven](https://img.shields.io/badge/Maven-3.9.5-blue.svg)

A comprehensive end-to-end test automation framework for SpiceJet flight booking using Selenium WebDriver, TestNG, and Maven with CI/CD pipeline integration.

## 📋 **Features**

- ✅ **Complete E2E Test Coverage** - SpiceJet flight booking journey
- ✅ **Page Object Model (POM)** - Maintainable and scalable test structure
- ✅ **Cross-Browser Support** - Chrome, Firefox, Edge, Safari
- ✅ **Headless Execution** - CI/CD compatible browser modes
- ✅ **ExtentReports Integration** - Rich HTML test reports with screenshots
- ✅ **GitHub Actions CI/CD** - Automated test execution on code changes
- ✅ **Maven Profiles** - Separate configurations for local and CI environments
- ✅ **Dynamic Element Handling** - Robust locator strategies for SpiceJet UI
- ✅ **Calendar Automation** - Advanced date selection capabilities
- ✅ **City Selection** - Text-based dynamic city selection

## 🏗️ **Framework Architecture**

```
├── src/
│   ├── main/java/com/automation/
│   │   ├── base/
│   │   │   └── BasePage.java              # Base page class
│   │   ├── pages/
│   │   │   ├── SpiceJetPage.java          # SpiceJet page object
│   │   │   └── SpiceJetBookingPage.java   # Booking page object
│   │   └── utils/
│   │       ├── DriverManager.java         # WebDriver management
│   │       ├── ConfigReader.java          # Configuration reader
│   │       ├── ExtentManager.java         # ExtentReports manager
│   │       ├── CalendarUtils.java         # Calendar utilities
│   │       └── SpiceJetCalendarUtils.java # SpiceJet-specific calendar utils
│   └── test/
│       ├── java/com/automation/
│       │   ├── base/
│       │   │   └── BaseTest.java          # Base test class
│       │   └── tests/
│       │       ├── SpiceJetE2ETest.java   # Original E2E test
│       │       ├── SpiceJetHybridE2ETest.java # Robust hybrid E2E test
│       │       ├── SpiceJetCalendarTest.java  # Calendar specific tests
│       │       └── SpiceJetWorkingCalendarTest.java # Working calendar tests
│       └── resources/
│           ├── config.properties         # Test configuration
│           ├── testng.xml               # TestNG suite for local
│           ├── ci-testng.xml           # TestNG suite for CI/CD
│           ├── log4j2.xml              # Logging configuration
│           └── testdata/               # Test data files
├── .github/workflows/
│   └── e2e-tests.yml                   # GitHub Actions workflow
├── reports/                            # Generated test reports
├── pom.xml                            # Maven configuration
└── CI-CD-SETUP.md                     # CI/CD documentation
```

## 🚀 **Quick Start**

### **Prerequisites**
- Java 11+
- Maven 3.6+
- Chrome/Firefox browser

### **1. Clone Repository**
```bash
git clone https://github.com/haradeep123/Automation-Repo.git
cd Automation-Repo
```

### **2. Install Dependencies**
```bash
mvn clean compile
```

### **3. Run Tests**

**Local execution (with browser UI):**
```bash
# Run all E2E tests
mvn test -Dtest=SpiceJetHybridE2ETest

# Run specific calendar tests
mvn test -Dtest=SpiceJetWorkingCalendarTest

# Run with different browsers
mvn test -Dtest=SpiceJetHybridE2ETest -Dbrowser=firefox
```

**CI mode (headless):**
```bash
# Run in headless mode locally
mvn test -Pci

# Run specific test in CI mode
mvn test -Pci -Dtest=SpiceJetHybridE2ETest
```

## 🎯 **Test Scenarios**

### **SpiceJet E2E Test Journey**
1. **Navigation** - Load SpiceJet homepage
2. **Popup Handling** - Close promotional popups
3. **Trip Selection** - Select One Way trip
4. **Route Selection** - Delhi to Mumbai
5. **Date Selection** - Pick departure date from calendar
6. **Flight Search** - Initiate flight search
7. **Results Verification** - Verify search results page

### **Calendar Automation Features**
- ✅ Dynamic date selection using `data-testid` locators
- ✅ Month navigation (when available)
- ✅ Multiple date selection strategies
- ✅ Fallback mechanisms for date picking

### **City Selection Features**
- ✅ Text-based city selection (Delhi, Mumbai, etc.)
- ✅ Dynamic dropdown handling
- ✅ Multiple locator strategies for reliability
- ✅ Fuzzy matching for city names

## 🔧 **Configuration**

### **Browser Configuration**
```properties
# config.properties
browser=chrome
headless=false
timeout=30
url=https://www.spicejet.com/
```

### **Maven Profiles**

**Local Profile (default):**
```bash
mvn test -Plocal
```

**CI Profile (headless):**
```bash
mvn test -Pci
```

## 📊 **Test Reports**

### **ExtentReports**
- Rich HTML reports with step-by-step execution
- Screenshots on test failures
- Execution timeline and statistics
- Located in: `reports/ExtentReport.html`

### **TestNG Reports**
- Standard TestNG HTML reports
- XML reports for CI/CD integration
- Located in: `target/surefire-reports/`

### **GitHub Actions Reports**
- Automated test execution on every push
- Test results visible in Actions tab
- Downloadable artifacts with detailed reports

## 🚀 **CI/CD Pipeline**

### **GitHub Actions Workflow**
- **Triggers:** Push to main, Pull Requests, Manual trigger
- **Environment:** Ubuntu + Java 11 + Chrome Headless
- **Execution:** Maven CI profile with headless browser
- **Reports:** Automatic upload of test results and ExtentReports

### **Workflow Badge**
Add this to your README to show test status:
```markdown
![E2E Tests](https://github.com/haradeep123/Automation-Repo/workflows/SpiceJet%20E2E%20Tests/badge.svg)
```

## 🛠️ **Advanced Features**

### **Dynamic Element Handling**
```java
// Multiple locator strategies for robust element finding
String[] citySelectors = {
    "//div[contains(text(),'" + cityName + "')]",
    "//*[text()='" + cityName + "']",
    "//li[contains(text(),'" + cityName + "')]"
};
```

### **Calendar Automation**
```java
// Using SpiceJet's actual calendar structure
WebElement dateToSelect = driver.findElement(
    By.xpath("//div[@data-testid='undefined-calendar-day-25']"));
```

### **Headless Browser Detection**
```java
// Automatic CI/CD detection
boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));
if (isHeadless) {
    chromeOptions.addArguments("--headless");
}
```

## 📈 **Best Practices Implemented**

1. **✅ Page Object Model** - Separation of test logic and page elements
2. **✅ Base Classes** - Common functionality in BasePage and BaseTest
3. **✅ Configuration Management** - Externalized configuration in properties files
4. **✅ Reporting Integration** - ExtentReports with detailed logging
5. **✅ Cross-Browser Support** - WebDriverManager for automatic driver management
6. **✅ CI/CD Ready** - Headless execution and GitHub Actions integration
7. **✅ Robust Locators** - Multiple fallback strategies for element location
8. **✅ Error Handling** - Comprehensive try-catch blocks with meaningful logging

## 🐛 **Troubleshooting**

### **Common Issues**

1. **ChromeDriver Version Issues**
   - WebDriverManager handles this automatically
   - Update to latest Selenium version if needed

2. **Element Not Found**
   - SpiceJet UI changes frequently
   - Framework includes multiple locator strategies
   - Check console logs for detailed error information

3. **CI/CD Failures**
   - Verify headless mode is working: `mvn test -Pci`
   - Check GitHub Actions logs for detailed error messages

## 🤝 **Contributing**

1. Fork the repository
2. Create feature branch: `git checkout -b feature-name`
3. Commit changes: `git commit -m 'Add feature'`
4. Push to branch: `git push origin feature-name`
5. Create Pull Request

## 📞 **Contact**

- **Author:** Haradeep
- **Repository:** [https://github.com/haradeep123/Automation-Repo](https://github.com/haradeep123/Automation-Repo)
- **Issues:** [Report Issues](https://github.com/haradeep123/Automation-Repo/issues)

## 📄 **License**

This project is open source and available under the [MIT License](LICENSE).

---

🚀 **Happy Testing with SpiceJet E2E Automation Framework!**