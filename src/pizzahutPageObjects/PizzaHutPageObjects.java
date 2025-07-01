package pizzahutPageObjects;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PizzaHutPageObjects {
	WebDriver driver;
	WebDriverWait wait;

	// Pop up black screen
	@FindBy(xpath = "//div[@class='pl-10 pr-10']")
	public WebElement autoLocationBlackPopUp;

	// Pop up close button
	@FindBy(xpath = "//button[@class='icon-close--white p-30 absolute top-0 right-0 mr-10 mt-10']")
	public WebElement locationPopUpClsbtn;

	// Location input
	@FindBy(xpath = "//input[contains(@class,'search--hut')]")
	public WebElement locationInput;

	// First auto-populate dropdown option
	@FindBy(xpath = "//div[@class='pt-5 border-t overflow-scrolling-touch']")
	public List<WebElement> locationDropdownOptions;

	@FindBy(xpath = "(//button[starts-with(@id, 'PlacesAutocomplete')])[1]")
	public WebElement firstAutoPopulateOption;

	@FindBy(xpath = "//button[contains(@class,'text-center')]")
	public WebElement startOrderTimeBtn;

	// Deals page navigation
	@FindBy(xpath = "(//a[@href='/order/deals/'])[2]")
	public WebElement dealsPageLink;

	// Vegetarian radio button
	@FindBy(xpath = "(//span[contains(@class, 'py-4 px-5 border rounded-full flex items-center')])[1]")
	public WebElement vegetarianRadioBtnFlag;

	// Pizzas menu bar option
	@FindBy(xpath = "(//a[@href='/order/pizzas/'])[2]")
	public WebElement pizzasMenuBarOption;

	// Pizza containers (Recommended)
	@FindBy(css = ".sc-fznXWL.sc-fznXWL.product-grid")
	public List<WebElement> pizzaContainers;

	// Add button inside pizza (relative, so handled in method)
	// Basket
	@FindBy(id = "basket")
	public WebElement yourBasket;

	@FindBy(xpath = "//div[contains(@class, 'basket-item-product-title')]")
	public WebElement basketItemProductTitle;

	@FindBy(xpath = "//div[contains(@class,'basket-item-product-price')]")
	public WebElement basketItemProductPrice;

	@FindBy(xpath = "//div[@class='text-12']/div[contains(@class,'supplement-value')]")
	public WebElement restaurantCharges;

	@FindBy(xpath = "(//div[contains(@class,'items-start')]/span)[4]")
	public WebElement taxAmount;

	@FindBy(xpath = "//a/span/span[contains(@data-synth,'basket-value')]")
	public WebElement basketValue;

	@FindBy(xpath = "//span/span[contains(@class,'bg-green-dark ')]")
	public WebElement checkoutBtnItemText;

	@FindBy(xpath = "//a[@href='/order/checkout/']/span[3]/span")
	public WebElement totalPriceCount;

	// Drinks option
	@FindBy(xpath = "(//a[@href='/order/drinks/'])[2]")
	public WebElement drinksOption;

	// Drink containers
	@FindBy(xpath = "//div[contains(@class,'product-grid')]")
	public List<WebElement> drinkContainers;

	// Add button for drink (handled in method)
	@FindBy(xpath = "//button[contains(@class,'button--green')]/span/span[text()='Add']")
	public WebElement addDrinkBtn;

	// Remove pizza from basket
	@FindBy(xpath = "(//button[contains(@class,'icon-close')])[1]")
	public WebElement removePizzaBtn;

	// Price tag removed from checkout
	@FindBy(xpath = "(//span[contains(@class,'absolute')]/span)[2]")
	public WebElement priceTagRemoved;

	// Checkout button
	@FindBy(xpath = "(//span[contains(@class,'absolute')]/span)[2]")
	public WebElement checkoutButton;

	// Minimum order required popup
	@FindBy(xpath = "//div[contains(@class,'pt-20 bg-white')]")
	public WebElement minOrderPopup;

	public PizzaHutPageObjects(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}

	// Wait for auto location black pop up
	public void waitForAutoLocationBlackPopUp() {
		wait.until(ExpectedConditions.visibilityOf(autoLocationBlackPopUp));
	}

	// Close pop up
	public void closePopUp() {
		locationPopUpClsbtn.click();
	}

	// Wait for location input
	public void waitForLocationInput() {
		wait.until(ExpectedConditions.visibilityOf(locationInput));
	}

	// Type address
	public void typeAddress(String address) {
		locationInput.sendKeys(address);
	}

	// Select first auto populate dropdown option
	public void selectFirstAutoPopulateDropdownOption() {
		wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("(//button[starts-with(@id, 'PlacesAutocomplete')])[1]")));
		wait.until(ExpectedConditions.elementToBeClickable(firstAutoPopulateOption));
		if (!locationDropdownOptions.isEmpty()) {
			locationDropdownOptions.get(0).click();
		} else {
			throw new RuntimeException("No options found");
		}
		try {
			wait.until(
					ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(@class,'text-center')]")));
			startOrderTimeBtn.click();
		} catch (Exception e) {
			// ignore
		}
	}

	// Wait for deals page
	public void waitForDealsPage() {
		wait.until(ExpectedConditions.visibilityOf(dealsPageLink));
	}

	// Validate vegetarian radio button flag is off
	public boolean isVegetarianRadioBtnFlagOff() {
		return !vegetarianRadioBtnFlag.isSelected();
	}

	// Click pizzas menu bar option
	public void clickPizzasMenuBarOption() {
		pizzasMenuBarOption.click();
	}

	// Select add button of any pizza from Recommended
	public void selectAddButtonOfAnyPizzaFromRecommended(String targetPizzaName) {
		wait.until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".sc-fznXWL.sc-fznXWL.product-grid")));
		for (WebElement pizza : pizzaContainers) {
			if (pizza.getText().trim().contains(targetPizzaName)) {
				WebElement addBtn = pizza.findElement(By.xpath(".//button/span/span[text()='Add']"));
				wait.until(ExpectedConditions.elementToBeClickable(addBtn)).click();
				return;
			}
		}
		throw new RuntimeException("No matching pizza found");
	}

	// Get basket item product title
	public String getBasketItemProductTitle() {
		wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//div[contains(@class, 'basket-item-product-title')]")));
		return basketItemProductTitle.getText();
	}

	// Get pizza price, restaurant charges, tax, total
	public String getPizzaPrice() {
		return basketItemProductPrice.getText();
	}

	public String getRestaurantCharges() {
		return restaurantCharges.getText();
	}

	public String getTaxAmount() {
		return taxAmount.getText();
	}

	public String getTotalPriceBefore() {
		return basketValue.getText();
	}

	// Get checkout button item count
	public String getCheckoutBtnItemText() {
		return checkoutBtnItemText.getText();
	}

	// Get total price count
	public String getTotalPriceCount() {
		return totalPriceCount.getText();
	}

	// Click drinks option
	public void clickDrinksOption() {
		drinksOption.click();
	}

	// Select Pepsi option to add into the Basket
	public void selectPepsiOptionToAddIntoBasket(String targetDrinkName) {
		wait.until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'product-grid')]")));
		for (WebElement drink : drinkContainers) {
			if (drink.getText().contains(targetDrinkName)) {
				wait.until(ExpectedConditions.elementToBeClickable(addDrinkBtn)).click();
				return;
			}
		}
		throw new RuntimeException("No drink found");
	}

	// Remove pizza from basket
	public void removePizzaFromBasket() {
		removePizzaBtn.click();
	}

	// Wait for price tag to be removed from checkout button
	public void waitForPriceTagRemoved() {
		wait.until(ExpectedConditions
				.invisibilityOfElementLocated(By.xpath("//a/span/span[contains(@data-synth,'basket-value')]")));
		wait.until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("(//span[contains(@class,'absolute')]/span)[2]")));
	}

	// Get price tag removed text
	public String getPriceTagRemovedText() {
		return priceTagRemoved.getText();
	}

	// Click checkout button
	public void clickCheckoutButton() {
		checkoutButton.click();
	}

	// Wait for minimum order required popup
	public boolean isMinOrderPopupDisplayed() {
		wait.until(ExpectedConditions.visibilityOf(minOrderPopup));
		return minOrderPopup.isDisplayed();
	}
}
