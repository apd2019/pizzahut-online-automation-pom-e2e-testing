package stepDefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import pizzahutPageObjects.PizzaHutPageObjects;

public class StepDefinitions {

	public static WebDriver driver;
	WebDriverWait wait;
	PizzaHutPageObjects pizzaHutPage;

	String pizzaPrice, restaurantCharges, taxAmount, totalPriceBefore, totalPriceAfter, totalPriceAfter1;

	{
		/* Setting up the project */}

	@Given("User launch Pizzahut application with {string}")
	public void user_launch_pizzahut_application_with(String url) {
		driver = new ChromeDriver();
		WebDriverManager.chromedriver().clearDriverCache().setup();
		driver.manage().window().maximize();
		driver.get(url);
		pizzaHutPage = new PizzaHutPageObjects(driver);
	}

	@When("User wait for auto location black pop up screen")
	public void user_wait_for_auto_location_black_pop_up_screen() throws InterruptedException {
		Thread.sleep(2000);
		try {
			pizzaHutPage.waitForAutoLocationBlackPopUp();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Then("User close the pop up screen")
	public void user_close_the_pop_up_screen() throws InterruptedException {
		try {
			pizzaHutPage.closePopUp();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@And("User see pop up for delivery asking for enter location")
	public void user_see_pop_up_for_delivery_asking_for_enter_location() throws InterruptedException {
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		pizzaHutPage.waitForLocationInput();
	}

	WebElement locationInput;

	@Then("User type address as {string}")
	public void user_type_address_as(String location) throws InterruptedException {
		pizzaHutPage.typeAddress(location);
	}

	@And("User select first auto populate drop down option")
	public void user_select_first_auto_populate_drop_down_option() {
		pizzaHutPage.selectFirstAutoPopulateDropdownOption();
	}

	@When("User navigate to deails page")
	public void user_navigate_to_deails_page() throws InterruptedException {
		pizzaHutPage.waitForDealsPage();
	}

	@Then("User validate vegetarian radio button flag is off")
	public void user_validate_vegetarian_radio_button_flag_is_off() throws InterruptedException {
		assertFalse(pizzaHutPage.vegetarianRadioBtnFlag.isSelected());
		if (!pizzaHutPage.vegetarianRadioBtnFlag.isSelected()) {
			System.out.println("radio button is off");
		} else {
			System.out.println("radio button is enabled");
		}
	}

	@And("User clicks on Pizzas menu bar option")
	public void user_clicks_on_pizzas_menu_bar_option() {
		pizzaHutPage.clickPizzasMenuBarOption();
		System.out.println(pizzaHutPage.pizzasMenuBarOption.getText());
	}

	@When("User select add button of any pizza from Recommended")
	public void user_select_add_button_of_any_pizza_from_recommended() {
		String targetPizzaName = "Southern Fiery Chicken";
		pizzaHutPage.selectAddButtonOfAnyPizzaFromRecommended(targetPizzaName);
	}

	@Then("User see that the pizza is getting added under Your Basket")
	public void user_see_that_the_pizza_is_getting_added_under_your_basket() {
		pizzaHutPage.getBasketItemProductTitle();
		System.out.println(pizzaHutPage.basketItemProductTitle.getText());
	}

	@And("User validate pizza price plus Tax is checkout price")
	public void user_validate_pizza_price_plus_tax_is_checkout_price() {
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
	}

	@Then("User validate checkout button contains Item count")
	public void user_validate_checkout_button_contains_item_count() {
		String itemText = pizzaHutPage.getCheckoutBtnItemText();
		assertTrue(itemText.contains("1 item"));
		System.out.println("item count text - passed!");
	}

	@And("User validate checkout button contains total price count")
	public void user_validate_checkout_button_contains_total_price_count() {
		String totalPriceCount = pizzaHutPage.getTotalPriceCount();
		System.out.println("Total Price: " + totalPriceCount);
		assertTrue(totalPriceCount.contains(totalPriceBefore));
	}

	@Then("User clicks on Drinks option")
	public void user_clicks_on_drinks_option() {
		pizzaHutPage.clickDrinksOption();
	}

	@And("User select Pepsi option to add into the Basket")
	public void user_select_pepsi_option_to_add_into_the_basket() {
		String targetDrinkName = "Pepsi";
		pizzaHutPage.selectPepsiOptionToAddIntoBasket(targetDrinkName);
	}

	@Then("User see {int} items are showing under checkout button")
	public void user_see_items_are_showing_under_checkout_button(Integer int1) {
		assertTrue(pizzaHutPage.checkoutBtnItemText.getText().contains(int1 + " items"));
		System.out.println(pizzaHutPage.checkoutBtnItemText.getText());
	}

	@And("User see total price is now more than before")
	public void user_see_total_price_is_now_more_than_before() {
		totalPriceAfter = pizzaHutPage.getTotalPriceBefore();
		assertNotEquals(totalPriceBefore, totalPriceAfter);
		System.out.println("total before: " + totalPriceBefore + "total after: " + totalPriceAfter);
		System.out.println("total price is now more than before!");
	}

	@Then("User remove the Pizza item from Basket")
	public void user_remove_the_pizza_item_from_basket() {
		pizzaHutPage.removePizzaFromBasket();
	}

	@And("see Price tag got removed from the checkout button")
	public void see_price_tag_got_removed_from_the_checkout_button() {
		pizzaHutPage.waitForPriceTagRemoved();
		assertFalse(pizzaHutPage.priceTagRemoved.getText().contains(totalPriceBefore));
	}

	@And("User see {int} item showing in checkout button")
	public void user_see_item_showing_in_checkout_button(Integer int1) {
		assertTrue(pizzaHutPage.checkoutBtnItemText.getText().contains(int1 + " item"));
		System.out.println(pizzaHutPage.checkoutBtnItemText.getText());
	}

	@Then("User Clicks on Checkout button")
	public void user_clicks_on_checkout_button() {
		pizzaHutPage.clickCheckoutButton();
	}

	@And("User see minimum order required pop up is getting displayed")
	public void user_see_minimum_order_required_pop_up_is_getting_displayed() {
		assertTrue(pizzaHutPage.isMinOrderPopupDisplayed());
		System.out.println(pizzaHutPage.minOrderPopup.isDisplayed());
	}

}
