package objectModels.gui.phpTravels;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.shaft.gui.element.ElementActions;

public class UserProfilePage {

	private WebDriver browser;

	private static By userNameDropDown = By.xpath("//li[contains(@class, 'd-none')]");
	private static By logoutButton = By.xpath("//a[contains(., 'Logout')]");

	public UserProfilePage(WebDriver browser) {
		this.browser = browser;
	}

	public void clickOnLogoutButton() {
		ElementActions.click(browser, userNameDropDown);
		ElementActions.click(browser, logoutButton);
	}

}
