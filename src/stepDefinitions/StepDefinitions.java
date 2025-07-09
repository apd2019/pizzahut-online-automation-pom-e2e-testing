package stepDefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import java.time.Duration;
import java.util.NoSuchElementException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import main.java.utils.ExtentReportManager;
import pizzahutPageObjects.PizzaHutPageObjects;

public class StepDefinitions {
	private ExtentReports extent;
	ExtentTest test = ExtentReportManager.createTest("Pizzahut Automation Test");

	public static WebDriver driver;
	WebDriverWait wait;
	PizzaHutPageObjects pizzaHutPage;

	String pizzaPrice, restaurantCharges, taxAmount, totalPriceBefore, totalPriceAfter, totalPriceAfter1;
	
	@Before
	public void setUpExtentReport(io.cucumber.java.Scenario scenario) {
		extent = ExtentReportManager.getInstance();
		test = extent.createTest(scenario.getName());
	}
	
	@After
	public void tearDownExtentReport(io.cucumber.java.Scenario scenario){
		if (scenario.isFailed()) {
			test.log(Status.FAIL, "Scenario failed: " + scenario.getName());
		} else {
			test.log(Status.PASS, "Scenario passed: " + scenario.getName());
		}
		if(driver != null) {
			driver.quit();
		}
		if(extent != null) {
			extent.flush();
		}
	}
	

	{
		/* Setting up the project */}

	@Given("User launch Pizzahut application with {string}")
	public void user_launch_pizzahut_application_with(String url) {
		driver = new ChromeDriver();
		WebDriverManager.chromedriver().clearDriverCache().setup();
		driver.manage().window().maximize();

		pizzaHutPage = new PizzaHutPageObjects(driver);

		try {
			driver.get(url);
			test.log(Status.PASS, "Launched Application with URL: " + url);
		} catch (Exception e) {
			test.log(Status.FAIL, "Failed to launch application with URL: " + url);
			throw e;

		}
	}

	@When("User wait for auto location black pop up screen")
	public void user_wait_for_auto_location_black_pop_up_screen() throws InterruptedException {
		
		try {
			pizzaHutPage.waitForAutoLocationBlackPopUp();
			test.log(Status.PASS, "Auto location black pop up is displayed");
		} catch(NoSuchElementException e) {
			test.log(Status.INFO, "Location pop up did not appear, continuing");
		}catch (Exception e) {
			System.out.println(e);
			test.log(Status.INFO, "Unexpected error: "+ e.getMessage());
		}

	}

	@Then("User close the pop up screen")
	public void user_close_the_pop_up_screen() throws InterruptedException {
		try {
			pizzaHutPage.closePopUp();
			test.log(Status.PASS, "Closed the pop up screen");
		}catch(NoSuchElementException e) {
			test.log(Status.INFO, "Pop up screen was not found to close, continuing");
		}catch (Exception e) {
			System.out.println(e);
			test.log(Status.INFO, "Failed to close the pop up screen: " + e.getMessage());
		}
	}

	@And("User see pop up for delivery asking for enter location")
	public void user_see_pop_up_for_delivery_asking_for_enter_location() throws InterruptedException {
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		try {
			pizzaHutPage.waitForLocationInput();
			test.log(Status.PASS, "Location input field is displayed");
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Location input field is not displayed");
			throw e;
		}
	}

	WebElement locationInput;

	@Then("User type address as {string}")
	public void user_type_address_as(String location) throws InterruptedException {
		try {
			pizzaHutPage.typeAddress(location);
			test.log(Status.PASS, "Typed address: " + location);
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Failed to type address: " + location);
			throw e;
		}

	}

	@And("User select first auto populate drop down option")
	public void user_select_first_auto_populate_drop_down_option() {
		try {
			pizzaHutPage.selectFirstAutoPopulateDropdownOption();
			test.log(Status.PASS, "Selected first auto populate drop down option");
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Failed to select first auto populate drop down option");
			throw e;
		}

	}

	@When("User navigate to deails page")
	public void user_navigate_to_deails_page() throws InterruptedException {
		try {
			pizzaHutPage.waitForDealsPage();
			test.log(Status.PASS, "Deals page is displayed");
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Deals page is not displayed");
			throw e;
		}

	}

	@Then("User validate vegetarian radio button flag is off")
	public void user_validate_vegetarian_radio_button_flag_is_off() throws InterruptedException {
		try {
			assertFalse(pizzaHutPage.vegetarianRadioBtnFlag.isSelected());
			if (!pizzaHutPage.vegetarianRadioBtnFlag.isSelected()) {
				System.out.println("radio button is off");
			} else {
				System.out.println("radio button is enabled");
			}
			test.log(Status.PASS, "Vegetarian radio button flag is off");
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Vegetarian radio button flag is not off");
			throw e;
		}

	}

	@And("User clicks on Pizzas menu bar option")
	public void user_clicks_on_pizzas_menu_bar_option() {
		try {
			pizzaHutPage.clickPizzasMenuBarOption();
			System.out.println(pizzaHutPage.pizzasMenuBarOption.getText());
			test.log(Status.PASS, "Clicked on Pizzas menu bar option");
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Failed to click on Pizzas menu bar option");
			throw e;
		}

	}

	@When("User select add button of any pizza from Recommended")
	public void user_select_add_button_of_any_pizza_from_recommended() {
		String targetPizzaName = "Southern Fiery Chicken";
		try {
			pizzaHutPage.selectAddButtonOfAnyPizzaFromRecommended(targetPizzaName);
			test.log(Status.PASS, "Selected pizza: " + targetPizzaName);
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Failed to select pizza: " + targetPizzaName);
			throw e;
		}

	}

	@Then("User see that the pizza is getting added under Your Basket")
	public void user_see_that_the_pizza_is_getting_added_under_your_basket() {

		try {
			pizzaHutPage.getBasketItemProductTitle();
			System.out.println(pizzaHutPage.basketItemProductTitle.getText());
			test.log(Status.PASS, "Pizza is added to Your Basket: " + pizzaHutPage.basketItemProductTitle.getText());
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Pizza is not added to Your Basket");
			throw e;
		}

	}

	@And("User validate pizza price plus Tax is checkout price")
	public void user_validate_pizza_price_plus_tax_is_checkout_price() {

		try {
			pizzaPrice = pizzaHutPage.getPizzaPrice();
			restaurantCharges = pizzaHutPage.getRestaurantCharges();
			taxAmount = pizzaHutPage.getTaxAmount();
			totalPriceBefore = pizzaHutPage.getTotalPriceBefore();

			System.out.println("PizzaPrice: " + pizzaPrice);
			System.out.println("Restaurant Handling Charges: " + restaurantCharges);
			System.out.println("taxAmount: " + taxAmount);
			System.out.println("totalPayment: " + totalPriceBefore);

			String cleanedPizzaPrice = pizzaPrice.replaceAll("[^\\d.]", "").trim();
			String cleanedRestaurantCharges = restaurantCharges.replaceAll("[^\\d.]", "").trim();
			String cleanedTaxAmount = taxAmount.replaceAll("[^\\d.]", "").trim();
			String cleanedTotalPriceBefore = totalPriceBefore.replaceAll("[^\\d.]", "").trim();

			double price = Double.parseDouble(cleanedPizzaPrice);
			double restCharge = Double.parseDouble(cleanedRestaurantCharges);
			double tax = Double.parseDouble(cleanedTaxAmount);
			double total = Double.parseDouble(cleanedTotalPriceBefore);

			System.out.println("Price + Tax should match Total: " + (price + restCharge + tax));
			assertEquals("Price + Restaurant Charges + Tax should match Total", price + restCharge + tax, total, 0.01);
			System.out.println(
					"Price: " + price + ", Restaurant Charges: " + restCharge + ", Tax: " + tax + ", total: " + total);

			test.log(Status.PASS, "Pizza price plus restaurant charges and tax matches the total price");
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Pizza price plus restaurant charges and tax does not match the total price");
			throw e;
		}

	}

	@Then("User validate checkout button contains Item count")
	public void user_validate_checkout_button_contains_item_count() {
		try {
			String itemText = pizzaHutPage.getCheckoutBtnItemText();
			assertTrue(itemText.contains("1 item"));
			System.out.println("item count text - passed!");
			test.log(Status.PASS, "Checkout button contains item count: " + itemText);
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Checkout button does not contain item count");
			throw e;
		}

	}

	@And("User validate checkout button contains total price count")
	public void user_validate_checkout_button_contains_total_price_count() {
		try {
			String totalPriceCount = pizzaHutPage.getTotalPriceCount();
			System.out.println("Total Price: " + totalPriceCount);
			assertTrue(totalPriceCount.contains(totalPriceBefore));
			test.log(Status.PASS, "Checkout button contains total price count: " + totalPriceCount);
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Checkout button does not contain total price count");
			throw e;
		}

	}

	@Then("User clicks on Drinks option")
	public void user_clicks_on_drinks_option() {
		try {
			pizzaHutPage.clickDrinksOption();
			test.log(Status.PASS, "Clicked on Drinks option");
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Failed to click on Drinks option");
			throw e;
		}

	}

	@And("User select Pepsi option to add into the Basket")
	public void user_select_pepsi_option_to_add_into_the_basket() {
		String targetDrinkName = "Pepsi";
		try {
			pizzaHutPage.selectPepsiOptionToAddIntoBasket(targetDrinkName);
			test.log(Status.PASS, "Selected drink: " + targetDrinkName);
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Failed to select drink: " + targetDrinkName);
			throw e;
		}

	}

	@Then("User see {int} items are showing under checkout button")
	public void user_see_items_are_showing_under_checkout_button(Integer int1) {
		try {
			assertTrue(pizzaHutPage.checkoutBtnItemText.getText().contains(int1 + " items"));
			System.out.println(pizzaHutPage.checkoutBtnItemText.getText());
			test.log(Status.PASS, "Checkout button shows " + int1 + " items");
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Checkout button does not show " + int1 + " items");
			throw e;
		}

	}

	@And("User see total price is now more than before")
	public void user_see_total_price_is_now_more_than_before() {
		try {
			totalPriceAfter = pizzaHutPage.getTotalPriceBefore();
			assertNotEquals(totalPriceBefore, totalPriceAfter);
			System.out.println("total before: " + totalPriceBefore + "total after: " + totalPriceAfter);
			System.out.println("total price is now more than before!");
			test.log(Status.PASS, "Total price is now more than before: " + totalPriceAfter);
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Total price is not more than before");
			throw e;
		}

	}

	@Then("User remove the Pizza item from Basket")
	public void user_remove_the_pizza_item_from_basket() {
		try {
			pizzaHutPage.removePizzaFromBasket();
			test.log(Status.PASS, "Removed pizza from basket");
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Failed to remove pizza from basket");
			throw e;
		}

	}

	@And("see Price tag got removed from the checkout button")
	public void see_price_tag_got_removed_from_the_checkout_button() {
		try {
			pizzaHutPage.waitForPriceTagRemoved();
			assertFalse(pizzaHutPage.priceTagRemoved.getText().contains(totalPriceBefore));
			test.log(Status.PASS, "Price tag got removed from the checkout button");
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Price tag did not get removed from the checkout button");
			throw e;
		}

	}

	@And("User see {int} item showing in checkout button")
	public void user_see_item_showing_in_checkout_button(Integer int1) {
		try {
			assertTrue(pizzaHutPage.checkoutBtnItemText.getText().contains(int1 + " item"));
			System.out.println(pizzaHutPage.checkoutBtnItemText.getText());
			test.log(Status.PASS, "Checkout button shows " + int1 + " item");
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Checkout button does not show " + int1 + " item");
			throw e;
		}

	}

	@Then("User Clicks on Checkout button")
	public void user_clicks_on_checkout_button() {
		try {
			pizzaHutPage.clickCheckoutButton();
			test.log(Status.PASS, "Clicked on Checkout button");
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Failed to click on Checkout button");
			throw e;
		}

	}

	@And("User see minimum order required pop up is getting displayed")
	public void user_see_minimum_order_required_pop_up_is_getting_displayed() {
		try {
			assertTrue(pizzaHutPage.isMinOrderPopupDisplayed());
			System.out.println(pizzaHutPage.minOrderPopup.isDisplayed());
			test.log(Status.PASS, "Minimum order required popup is displayed");
		} catch (Exception e) {
			System.out.println(e);
			test.log(Status.FAIL, "Minimum order required popup is not displayed");
			throw e;
		}

	}

}
