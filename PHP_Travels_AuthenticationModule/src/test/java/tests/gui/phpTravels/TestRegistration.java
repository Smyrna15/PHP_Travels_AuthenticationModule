package tests.gui.phpTravels;

import java.util.logging.Level;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.shaft.gui.browser.BrowserActions;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import objectModels.gui.phpTravels.LoginPage;
import objectModels.gui.phpTravels.SignUpFormPage;
import objectModels.gui.phpTravels.UserProfilePage;

public class TestRegistration {

	private WebDriver browser;

	private SignUpFormPage signUpFormPage;
	private UserProfilePage userProfilePage;
	private LoginPage loginPage;

	@Test(description = "TC001 - Check sign up successfully then login")
	@Description("Check that user can register successfully with valid data then login successfully")
	@Severity(SeverityLevel.CRITICAL)
	@Story("As a user I can sign up with valid required fileds")
	public void checkSignUpWithValidDataAndVerifyLogin() {
		signUpFormPage.signUpWithValidData();
		userProfilePage.clickOnLogoutButton();
		loginPage.checkLoginWithValidCredentials();
	}

	@Test(description = "TC002 - First Name format validation")
	@Description("Check that First Name starts with uppercase")
	@Severity(SeverityLevel.NORMAL)
	@Story("As a user when try to register, I have to write first name starts with capital letter")
	public void testFirstNameValidation() {
		signUpFormPage.signUpWithInvalidFirstName();
	}

	@Test(description = "TC003 - Last Name format validation")
	@Description("Check that last name starts with uppercase")
	@Severity(SeverityLevel.NORMAL)
	@Story("As a user when try to register, I have to write last name starts with capital letter")
	public void testLastNameValidation() {
		signUpFormPage.signUpWithInvalidLastName();
	}

	@Test(description = "TC004 - Check if user can signup successfully with matched first and last names")
	@Description("Check if user can register successfuly with matched First Name and Last Name")
	@Severity(SeverityLevel.CRITICAL)
	@Story("As a user when try to register, I have to write first name and last name not matched")
	public void testFirstNameAndLastNameMatching() {
		signUpFormPage.signUpWithMatchedFirstAndLastNames();
	}

	@Test(description = "TC005 - Mobile number format validation")
	@Description("Check if user can register successfuly with invalid mobile number includes letters")
	@Severity(SeverityLevel.BLOCKER)
	@Story("As a user when try to register, I have to write valid mobile number")
	public void testMobileNumberValidation() {
		signUpFormPage.signUpWithInvalidMobileNumber();
	}

	@Test(description = "TC006 - Email format validation")
	@Description("Check if user can register successfuly with invalid Email format (example@mail.com e.g,)")
	@Severity(SeverityLevel.BLOCKER)
	@Story("As a user when try to register, I have to write valid email format")
	public void testEmailFormatValidation() {
		signUpFormPage.signUpWithWithInvalidEmailFormat();
	}

	@Test(description = "TC007 - Existing Email validation")
	@Description("Check if user can register successfuly with Email already exists")
	@Severity(SeverityLevel.BLOCKER)
	@Story("As a user when try to register, I have to write unique and non existing email in database")
	public void testSignUpWithRegisteredEmailValidation() {
		signUpFormPage.signUpWithRegisteredEmail();
	}

	@Test(description = "TC008 - Password length and characters validation")
	@Description("Check if user can register successfuly with Password have more than 8 characters and not alpha numeric")
	@Severity(SeverityLevel.CRITICAL)
	@Story("As a user when try to register, I have to write passowrd that is alphanumeric must have capital letter, small letter, with a limit of 8 characters")
	public void testPasswordMaxLengthAndAlphaNumericValidation() {
		signUpFormPage.signUpWithWithInvalidPasswordMaxLengthAndNumeric();
	}

	@Test(description = "TC009 - Password minimum length validation")
	@Description("Check if user can register successfuly with Password less than 6 characters")
	@Severity(SeverityLevel.CRITICAL)
	@Story("As a user when try to register, I have to write passowrd that is alphanumeric must have capital letter, small letter, with a minimum of 6 characters")
	public void testPasswordMinLengthVlaidation() {
		signUpFormPage.signUpWithWithInvalidPasswordMinLength();
	}

	@Test(description = "TC0010 - Password confirmation validation")
	@Description("Check if user can register successfuly with Password not confirmed correctly")
	@Severity(SeverityLevel.BLOCKER)
	@Story("As a user when try to register, I have to write passowrd and confirm passowrd that matches")
	public void testPasswordConfirmationValidation() {
		signUpFormPage.signUpWithUpWithPasswordAndConfirmPasswordUnmatched();
	}

	@BeforeClass
	public void beforeClass() {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		options.addArguments("--incognito");
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(ChromeOptions.CAPABILITY, options);

		LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
		options.setCapability("goog:loggingPrefs", logPrefs);
		browser = new ChromeDriver(options);
		userProfilePage = new UserProfilePage(browser);
		signUpFormPage = new SignUpFormPage(browser);
		loginPage = new LoginPage(browser);
	}

	@BeforeMethod
	public void beforeMethod() {
		signUpFormPage.navigateToSignUpForm();
	}

	@AfterMethod
	public void afterMehtod() {
		if (BrowserActions.getCurrentURL(browser).equalsIgnoreCase(signUpFormPage.getTestData("AccountUrl"))) {
			userProfilePage.clickOnLogoutButton();
		}
	}

	@AfterClass
	public void afterClass() {
		browser.quit();
	}
}
