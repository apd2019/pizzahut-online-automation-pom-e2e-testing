package stepDefinitions;

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
import static org.junit.Assert.*;

public class StepDefinitions {

	public static WebDriver driver;
	WebDriverWait wait;

	String pizzaPrice, restaurantCharges, taxAmount, totalPriceBefore, totalPriceAfter, totalPriceAfter1;

	{
		/* Setting up the project */}

	@Given("User launch Pizzahut application with {string}")
	public void user_launch_pizzahut_application_with(String url) {
		driver = new ChromeDriver();
		WebDriverManager.chromedriver().clearDriverCache().setup();
		driver.manage().window().maximize();
		driver.get(url);

	}

	@When("User wait for auto location black pop up screen")
	public void user_wait_for_auto_location_black_pop_up_screen() throws InterruptedException {
		Thread.sleep(2000);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='pl-10 pr-10']")));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Then("User close the pop up screen")
	public void user_close_the_pop_up_screen() throws InterruptedException {
		try {
			WebElement locationPopUpClsbtn = driver.findElement(
					By.xpath("//button[@class='icon-close--white p-30 absolute top-0 right-0 mr-10 mt-10']"));
			locationPopUpClsbtn.click();
		} catch (Exception e) {

			System.out.println(e);
		}
	}

	@And("User see pop up for delivery asking for enter location")
	public void user_see_pop_up_for_delivery_asking_for_enter_location() throws InterruptedException {
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		locationInput = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@class,'search--hut')]")));

	}

	WebElement locationInput;

	@Then("User type address as {string}")
	public void user_type_address_as(String location) throws InterruptedException {
		locationInput.sendKeys(location);
	}

	@And("User select first auto populate drop down option")
	public void user_select_first_auto_populate_drop_down_option() {

		By locList = By.xpath("//div[@class='pt-5 border-t overflow-scrolling-touch']");
		List<WebElement> options = driver.findElements(locList);
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("(//button[starts-with(@id, 'PlacesAutocomplete')])[1]")));
		wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("(//button[starts-with(@id, 'PlacesAutocomplete')])[1]")));
		if (!options.isEmpty()) {
			options.get(0).click();
			System.out.println("selected the first location in the dropdown");
		} else {
			throw new NoSuchElementException("No options found");
		}

		try {
			WebElement startOrdrTm = wait.until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@class,'text-center')]")));
			startOrdrTm.click();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	@When("User navigate to deails page")
	public void user_navigate_to_deails_page() throws InterruptedException {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//a[@href='/order/deals/'])[2]")));
	}

	@Then("User validate vegetarian radio button flag is off")
	public void user_validate_vegetarian_radio_button_flag_is_off() throws InterruptedException {
		WebElement radioBtnFlag = driver.findElement(
				By.xpath("(//span[contains(@class, 'py-4 px-5 border rounded-full flex items-center')])[1]"));
		assertFalse(radioBtnFlag.isSelected());
		if (!radioBtnFlag.isSelected()) {
			System.out.println("radio button is off");
		} else {
			System.out.println("radio button is enabled");
		}
	}

	@And("User clicks on Pizzas menu bar option")
	public void user_clicks_on_pizzas_menu_bar_option() {
		WebElement pizzaMenu = driver.findElement(By.xpath("(//a[@href='/order/pizzas/'])[2]"));
		pizzaMenu.click();
		System.out.println(pizzaMenu.getText());
	}

	@When("User select add button of any pizza from Recommended")
	public void user_select_add_button_of_any_pizza_from_recommended() {
		String targetPizzaName = "Southern Fiery Chicken";
		wait.until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".sc-fznXWL.sc-fznXWL.product-grid")));
		List<WebElement> pizzaContainer = driver.findElements(By.cssSelector(".sc-fznXWL.sc-fznXWL.product-grid"));
		System.out.println("outer for loop is executed");

		for (WebElement pizza : pizzaContainer) {

			System.out.println("for loop is execd" + ": Pizza Names: " + pizza.getText());
			if (pizza.getText().trim().contains(targetPizzaName)) {
				WebElement addBtn = pizza.findElement(By.xpath("//button/span/span[text()='Add']"));
				wait.until(ExpectedConditions.elementToBeClickable(addBtn)).click();
				System.out.println("this is executed");
				return;
			} else {
				System.out.println("no match found");
			}
		}
	}

	@Then("User see that the pizza is getting added under Your Basket")
	public void user_see_that_the_pizza_is_getting_added_under_your_basket() {
		WebElement yourBasket = driver.findElement(By.id("basket"));
		wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//div[contains(@class, 'basket-item-product-title')]")));
		WebElement basketItem = yourBasket
				.findElement(By.xpath("//div[contains(@class, 'basket-item-product-title')]"));
		System.out.println(basketItem.getText());
	}

	@And("User validate pizza price plus Tax is checkout price")
	public void user_validate_pizza_price_plus_tax_is_checkout_price() {
		pizzaPrice = driver.findElement(By.xpath("//div[contains(@class,'basket-item-product-price')]")).getText();
		restaurantCharges = driver
				.findElement(By.xpath("//div[@class='text-12']/div[contains(@class,'supplement-value')]")).getText();
		taxAmount = driver.findElement(By.xpath("(//div[contains(@class,'items-start')]/span)[4]")).getText();
		totalPriceBefore = driver.findElement(By.xpath("//a/span/span[contains(@data-synth,'basket-value')]"))
				.getText();

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
		WebElement checkoutBtnItemText = driver.findElement(By.xpath("//span/span[contains(@class,'bg-green-dark ')]"));
		String itemText = checkoutBtnItemText.getText();
		assertTrue(itemText.contains("1 item"));
		System.out.println("item count text - passed!");
	}

	@And("User validate checkout button contains total price count")
	public void user_validate_checkout_button_contains_total_price_count() {
		{
			String totalPriceCount = driver.findElement(By.xpath("//a[@href='/order/checkout/']/span[3]/span"))
					.getText();
			System.out.println("Total Price: " + totalPriceCount);
			assertTrue(totalPriceCount.contains(totalPriceBefore));

		}

	}

	@Then("User clicks on Drinks option")
	public void user_clicks_on_drinks_option() {
		driver.findElement(By.xpath("(//a[@href='/order/drinks/'])[2]")).click();
	}

	@And("User select Pepsi option to add into the Basket")
	public void user_select_pepsi_option_to_add_into_the_basket() {
		String targetDrinkName = "Pepsi";
		wait.until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'product-grid')]")));
		List<WebElement> drinkContainer = driver.findElements(By.xpath("//div[contains(@class,'product-grid')]"));
		for (WebElement drink : drinkContainer) {
			if (drink.getText().contains(targetDrinkName)) {
				WebElement addBtn = driver
						.findElement(By.xpath("//button[contains(@class,'button--green')]/span/span[text()='Add']"));
				wait.until(ExpectedConditions.elementToBeClickable(addBtn)).click();
				return;
			} else {
				System.out.println("no drink found!");
			}
		}
	}

	@Then("User see {int} items are showing under checkout button")
	public void user_see_items_are_showing_under_checkout_button(Integer int1) {
		assertTrue(driver.findElement(By.xpath("//span/span[contains(@class,'bg-green-dark')]")).getText()
				.contains(int1 + " items"));
		System.out.println(driver.findElement(By.xpath("//span/span[contains(@class,'bg-green-dark ')]")).getText());
	}

	@And("User see total price is now more than before")
	public void user_see_total_price_is_now_more_than_before() {
		totalPriceAfter = driver.findElement(By.xpath("//a/span/span[contains(@data-synth,'basket-value')]")).getText();
		assertNotEquals(totalPriceBefore, totalPriceAfter);
		System.out.println("total before: " + totalPriceBefore + "total after: " + totalPriceAfter);
		System.out.println("total price is now more than before!");
	}

	@Then("User remove the Pizza item from Basket")
	public void user_remove_the_pizza_item_from_basket() {
		driver.findElement(By.xpath("(//button[contains(@class,'icon-close')])[1]")).click();
	}

	@And("see Price tag got removed from the checkout button")
	public void see_price_tag_got_removed_from_the_checkout_button() {
		wait.until(ExpectedConditions
				.invisibilityOfElementLocated(By.xpath("//a/span/span[contains(@data-synth,'basket-value')]")));
		wait.until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("(//span[contains(@class,'absolute')]/span)[2]")));
		assertFalse(driver.findElement(By.xpath("(//span[contains(@class,'absolute')]/span)[2]")).getText()
				.contains(totalPriceBefore));
	}

	@And("User see {int} item showing in checkout button")
	public void user_see_item_showing_in_checkout_button(Integer int1) {
		assertTrue(driver.findElement(By.xpath("//span/span[contains(@class,'bg-green-dark')]")).getText()
				.contains(int1 + " item"));
		System.out.println(driver.findElement(By.xpath("//span/span[contains(@class,'bg-green-dark ')]")).getText());
	}

	@Then("User Clicks on Checkout button")
	public void user_clicks_on_checkout_button() {
		driver.findElement(By.xpath("(//span[contains(@class,'absolute')]/span)[2]")).click();

	}

	@And("User see minimum order required pop up is getting displayed")
	public void user_see_minimum_order_required_pop_up_is_getting_displayed() {
		WebElement minOrdPopup = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'pt-20 bg-white')]")));
		assertTrue(minOrdPopup.isDisplayed());
		System.out.println(minOrdPopup.isDisplayed());
	}

}
