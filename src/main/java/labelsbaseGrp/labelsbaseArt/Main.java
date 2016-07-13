package labelsbaseGrp.labelsbaseArt;
import org.openqa.selenium.WebDriver;

import FrameworkUtils.BrowserLauncher;

public class Main {

	public static void main(String[] args) {
		
		String browser = "Firefox";
		String genre = "Electronica";
		String releaseName = "Slipstream EP";
//		String soundCloudLink = "https://soundcloud.com/bernard-cooley/sets/slipstream-demo/s-eTc4o";
		
		BrowserLauncher bL = new BrowserLauncher();
    	WebDriver driver = bL.launchBrowser(browser);
		
    	// Scrape labelsbase for new labels
		UpdateDatabase.updateDatabase(driver);
		
		// Email all labels from the above genre
//		EmailLabels.emailLabels(driver, genre, releaseName);
    	
//    	UpdateLikesAndReleases.updateLikesAndReleases(driver);
		
	}

}
