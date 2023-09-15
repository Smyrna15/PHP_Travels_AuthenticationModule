package objectModels.gui.phpTravels;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.shaft.gui.browser.BrowserActions;
import com.shaft.gui.element.ElementActions;
import com.shaft.validation.Assertions;
import com.shaft.validation.Assertions.AssertionType;

public class LoginPage {

	private WebDriver browser;

	private Properties properties;

	private static By userName = By.name("username");
	private static By password = By.name("password");
	private static By rememberMeCheckBox = By.xpath("//label[contains(.,'Remember Me')]");
	private static By loginButton = By.xpath("//button[contains(.,'Login')]");

	public LoginPage(WebDriver browser) {
		this.browser = browser;
	}

	public void checkLoginWithValidCredentials() {
		fillCredetialsAndLogin(getTestData("validEmail1"), getTestData("validPassword"));

		assertCredentials(getTestData("SuccessfulLogin"),
				getTestData("FailedLoginWithCorrectCredentials"));
	}

//	Assertion of login credentials
	private void assertCredentials(String customLogSuccess, String customLogFailure) {
		if (checkCurrentUrl()) {
			Assertions.assertTrue(checkCurrentUrl(), AssertionType.POSITIVE, customLogSuccess);
		} else {
			Assertions.assertTrue(checkCurrentUrl(), AssertionType.POSITIVE, customLogFailure);
		}
	}

//	To type in each field of the signUp form
	private void elementActionsTyping(By elementLocator, String value) {
		ElementActions.type(browser, elementLocator, value);
	}

	private void fillCredetialsAndLogin(String email, String userPassword) {
		elementActionsTyping(userName, email);
		elementActionsTyping(password, userPassword);
		checkBoxAndClickOnLoginButton();
	}

	private void checkBoxAndClickOnLoginButton() {
		ElementActions.click(browser, rememberMeCheckBox);
		ElementActions.click(browser, loginButton);
	}

	private Boolean checkCurrentUrl() {
		if (BrowserActions.getCurrentURL(browser).equalsIgnoreCase(getTestData("AccountUrl"))) {
			return true;
		} else {
			return false;
		}
	}

	public String getTestData(String valueName) {
		properties = new Properties();

		try {
			properties.load(new FileInputStream("src/test/resources/TestDataFiles/LoginTestData.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return properties.getProperty(valueName);
	}
}
