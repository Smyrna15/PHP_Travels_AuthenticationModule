package objectModels.gui.phpTravels;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;

import com.shaft.gui.browser.BrowserActions;
import com.shaft.gui.element.ElementActions;
import com.shaft.validation.Assertions;
import com.shaft.validation.Assertions.AssertionComparisonType;
import com.shaft.validation.Assertions.AssertionType;

public class SignUpFormPage {

	private WebDriver browser;

	private Properties properties;

	private static By firstName = By.name("firstname");
	private static By lastName = By.name("lastname");
	private static By mobileNumber = By.name("phone");
	private static By email = By.name("email");
	private static By password = By.name("password");
	private static By confirmPassword = By.name("confirmpassword");
	private static By signUpButton = By.xpath("//button[contains(@class,'signupbtn')]");
	private static By signUpResultAlert = By.className("resultsignup");

	public SignUpFormPage(WebDriver browser) {
		this.browser = browser;
	}

//	In this case used email valid format but not real one, so here we check format validation, if system requirements need to
//	validate if the email is real or not we will change the test data and test case validation.
	public void signUpWithValidData() {
		fillSignUpDataAndSubmit("validFirstName", "validLastName", "validMobileNumber", "validEmail1", "validPassword",
				"validPassword");
	}

// 	Asserting that alert message is displayed as system doesn't validate FirstName characters
	public void signUpWithInvalidFirstName() {
		fillSignUpDataAndSubmit("InvalidFirstName", "validLastName", "validMobileNumber", "validEmail2",
				"validPassword", "validPassword");

		assertAlertMessageTextIfDisplayed(getTestData("FirstNameAlert"), getTestData("FirstNameMessageLogMessage"));
	}

//	Asserting that alert message is displayed as system doesn't validate LastName characters
	public void signUpWithInvalidLastName() {
		fillSignUpDataAndSubmit("validFirstName", "InvalidLastName", "validMobileNumber", "validEmail3",
				"validPassword", "validPassword");

		assertAlertMessageTextIfDisplayed(getTestData("LastNameAlert"), getTestData("LastNameMessageLogMessage"));
	}

//	Asserting that alert message is displayed as system doesn't validate First and LastName matching
	public void signUpWithMatchedFirstAndLastNames() {
		fillSignUpDataAndSubmit("validFirstName", "validFirstName", "validMobileNumber", "validEmail4", "validPassword",
				"validPassword");

		assertAlertMessageTextIfDisplayed(getTestData("FirstAndLastNameMatches"),
				getTestData("FirstName_LastName_MatchedLogMessage"));
	}

//	Asserting that alert message is displayed as system doesn't validate mobile number
	public void signUpWithInvalidMobileNumber() {
		fillSignUpDataAndSubmit("validFirstName", "validLastName", "InvalidMobileNumber", "validEmail5",
				"validPassword", "validPassword");

		assertAlertMessageTextIfDisplayed(getTestData("InvalidMobileNumberAlertMessage"),
				getTestData("InvalidMobileNumberLogMessage"));
	}

//	Asserting that alert message is displayed as system doesn't validate Invalid mail address format
//	If used "emerge@zz.n" for email system will accept it, but if used "emerge@zz" system will reject it and show alert message
//	Mail foramat shoudl be "example@example.nnn", if this bug fixed test is fir to pass the test case assigned for this feature 
	public void signUpWithWithInvalidEmailFormat() {
		fillSignUpDataAndSubmit("validFirstName", "validLastName", "validMobileNumber", "InvalidEmail1",
				"validPassword", "validPassword");

		assertAlertMessageTextIfDisplayed(getTestData("InvalidEmailFormate"),
				getTestData("InvalidEmailFormatLogMessage"));
	}

//	registeredEmail is already existing user using validEmail1 that registered before
	public void signUpWithRegisteredEmail() {
		fillSignUpDataAndSubmit("validFirstName", "validLastName", "validMobileNumber", "validEmail1", "validPassword",
				"validPassword");

		assertAlertMessageTextIfDisplayed(getTestData("EmailAlert_ExistingEmail"),
				getTestData("RegisteredEmailLogMessage"));
	}

//	Register with password only numeric and more than 8 characters.
//	System doesn't validate max length and numeric passwords, therefore assert alert message displaying 
	public void signUpWithWithInvalidPasswordMaxLengthAndNumeric() {
		fillSignUpDataAndSubmit("validFirstName", "validLastName", "validMobileNumber", "validEmail6",
				"InvalidPassword1", "InvalidPassword1");

		assertAlertMessageTextIfDisplayed(getTestData("InvalidPasswordFormat"),
				getTestData("InvalidPasswordFormatLogMessage"));
	}

//	Validate password minimum length
	public void signUpWithWithInvalidPasswordMinLength() {
		fillSignUpDataAndSubmit("validFirstName", "validLastName", "validMobileNumber", "validEmail7",
				"InvalidPassword2", "InvalidPassword2");

		assertAlertMessageTextIfDisplayed(getTestData("InvalidPasswordMinLength"),
				getTestData("InvalidPasswordFormatLogMessage"));
	}

//	Validate password and confirm password matching 
	public void signUpWithUpWithPasswordAndConfirmPasswordUnmatched() {
		fillSignUpDataAndSubmit("validFirstName", "validLastName", "validMobileNumber", "validEmail8",
				"InvalidPassword1", "validPassword");

		assertAlertMessageTextIfDisplayed(getTestData("PasswordConfirmationAlert"),
				getTestData("PasswordConfirmationLogMessage"));
	}

//	Method to assert alert message text if displayed
	private void assertAlertMessageTextIfDisplayed(String expectedMessage, String logMessage) {
		if (checkCurrentUrl() && ElementActions.isElementDisplayed(browser, signUpResultAlert)) {

			Assertions.assertEquals(expectedMessage, ElementActions.getText(browser, signUpResultAlert),
					AssertionComparisonType.CONTAINS, AssertionType.POSITIVE,
					ElementActions.getText(browser, signUpResultAlert));

		} else if (checkCurrentUrl() && !(ElementActions.isElementDisplayed(browser, signUpResultAlert))) {
			Assertions.assertTrue(!checkCurrentUrl(), AssertionType.POSITIVE, getTestData("AlertMessageNotDisplayed"));

		} else {
			assertThatUserRegisteredWithInvalidData(logMessage);
		}
	}

//	Method to Fail test in case user registered with Invalid data and redirected to account url 
	private void assertThatUserRegisteredWithInvalidData(String logMessage) {
		Assertions.assertEquals(getTestData("URL"), BrowserActions.getCurrentURL(browser), logMessage);
	}

//	if user try to sign up with valid data he will be redirected to account url
//	if user try to sign up with Invalid data he will stay on registration page url, so we assert alert message
	private Boolean checkCurrentUrl() {
		if (BrowserActions.getCurrentURL(browser).equalsIgnoreCase(getTestData("URL"))) {
			return true;
		} else {
			return false;
		}
	}

	public void navigateToSignUpForm() {
		BrowserActions.navigateToURL(browser, getTestData("URL"));
	}

//	To type in each field of the signUp form
	private void elementActionsTyping(By elementLocator, String value) {
		ElementActions.type(browser, elementLocator, value);
	}

//	Click on sign up to submit registration form
	private void clickOnSignUpButton() {
		ElementActions.click(browser, signUpButton);
	}

//	To pass test data for each use case
	private void fillSignUpDataAndSubmit(String first_Name, String last_Name, String mobile_Number, String mail_Address,
			String pass, String confirmPass) {
		elementActionsTyping(firstName, getTestData(first_Name));
		elementActionsTyping(lastName, getTestData(last_Name));
		elementActionsTyping(mobileNumber, getTestData(mobile_Number));
		elementActionsTyping(email, getTestData(mail_Address));
		elementActionsTyping(password, getTestData(pass));
		elementActionsTyping(confirmPassword, getTestData(confirmPass));

		clickOnSignUpButton();
		getRequestResponse();
	}

//	To get test data from properties file
	public String getTestData(String valueName) {
		properties = new Properties();

		try {
			properties.load(new FileInputStream("src/test/resources/TestDataFiles/RegistrationTestData.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return properties.getProperty(valueName);
	}

	private void getRequestResponse() {
		String currentURL = browser.getCurrentUrl();
		LogEntries logs = browser.manage().logs().get("performance");
		int status = -1;

		for (Iterator<LogEntry> it = logs.iterator(); it.hasNext();) {
			LogEntry entry = it.next();
			try {
				JSONObject json = new JSONObject(entry.getMessage());
				JSONObject message = json.getJSONObject("message");
				String method = message.getString("method");
				if (method != null && "Network.responseReceived".equals(method)) {
					JSONObject params = message.getJSONObject("params");
					JSONObject response = params.getJSONObject("response");
					String messageUrl = response.getString("url");
					if (currentURL.equals(messageUrl)) {
						status = response.getInt("status");
						PrintStream apiResponse;
						try {
							apiResponse = new PrintStream(new FileOutputStream("API_RESPONSE_JSON.txt"));
							apiResponse.println("returned response for " + messageUrl + ": " + status);
							apiResponse.println("\nstatus code: " + status);
							apiResponse.println("headers: " + response.get("headers"));
							apiResponse.println(json.toString());
							apiResponse.println("");
							System.setOut(apiResponse);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
