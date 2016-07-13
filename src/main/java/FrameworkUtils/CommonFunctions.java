package FrameworkUtils;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import UiMap.JDPageElements;

public class CommonFunctions {

	public CommonFunctions() {
		
	}
	
	public static void clickButton(WebDriver driver, By element) {
		if (driver.findElements(element).size() > 0) {
			// System.out.println(element + " Found");
			driver.findElement(element).click();
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//			}
		} else {
			 System.err.println(element + " Not Found");
		}
	}
	
	public static void inputToField(WebDriver driver, By element, String input) {
		if (driver.findElements(element).size() > 0) {
			// System.out.println(element + " Found");
			driver.findElement(element).sendKeys(input);
		} else {
			 System.err.println(element + " Not Found");
		}
	}
	
	public static String createStringFromArrayList(ArrayList<String> genreNames) {
		StringBuilder sb = new StringBuilder();
		
		for (String s : genreNames) {
			sb.append(s);
			sb.append(", ");
		}
		
		sb.deleteCharAt(sb.length()-1);
		sb.deleteCharAt(sb.length()-1);
		
		return sb.toString();
	}
	
	public static void wait(int seconds) {
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
		}
	}
	
	public static String getElementText(WebDriver driver, By element) {
		String st = null;
		if (driver.findElements(element).size() > 0) {
//			 System.out.println(element + " Found");
			st = driver.findElement(element).getText();
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//			}
		} else {
			 System.err.println(element + " Not Found");
		}
		return st;
	}
	
	public static void pressEnter(WebDriver driver, By element) {
		if (driver.findElements(JDPageElements.searchBox).size() > 0) {
			driver.findElement(JDPageElements.searchBox).sendKeys(Keys.ENTER);
		} else {
			System.err.println(element + " Not Found");
		}
	}
	
	public static void pressTab(WebDriver driver, By element) {
		if (driver.findElements(JDPageElements.searchBox).size() > 0) {
			driver.findElement(JDPageElements.searchBox).sendKeys(Keys.TAB);
		} else {
			System.err.println(element + " Not Found");
		}
	}
	
	public static void clearField(WebDriver driver, By element) {
		if (driver.findElements(element).size() > 0) {
			driver.findElement(element).clear();
		}
	}
	
	public static List<WebElement> findElements(WebDriver driver, By element) {
		List<WebElement> elements = new ArrayList<WebElement>();
		if (driver.findElements(element).size() > 0) {
			elements = driver.findElements(element);
		}else {
			System.err.println(element + " Not Found");
		}
		return elements;
	}
	
	public static int getListSize(WebDriver driver, By element) {
		int listSize = 0;
		if (driver.findElements(element).size() > 0) {
			listSize = driver.findElements(element).size();
		}else {
			System.err.println(element + " Not Found");
		}
		return listSize;
	}
	
	public static boolean elementExists(WebDriver driver, By element) {
		boolean bool = false;
		
		if (driver.findElements(element).size() > 0) {
			bool = true;
		}
		return bool;
	}
}
