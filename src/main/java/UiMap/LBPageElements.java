package UiMap;
import org.openqa.selenium.By;

public class LBPageElements {
	
	//LabelsBase
	public static final By labelName = By.xpath("//ul[contains(@class, 'nav-tabs')]/li/a");
	public static final By demoEmail1 = By.xpath("html/body/div[1]/div/div[1]/div[4]/div[2]/a[2]");
	public static final By demoEmail2 = By.xpath("html/body/div[1]/div/div[1]/div[3]/div[2]/a[2]");
	public static final By genre = By.cssSelector("a[href*='/?g=']");
	public static final By location = By.cssSelector("a[href*='/?c=']");
	public static final By labelArtists = By.className("avatar-block");
	public static final By labelLink = By.cssSelector("a[href*='/label/']");
	public static final By maxPages = By.xpath("/html/body/div[1]/div/div[2]/ul/ul/li[12]");
	public static final By nextButton = By.cssSelector("a[rel='next']");
	public static final By topTracksSection = By.xpath("/html/body/div[1]/div/div[1]/div[2]/div[2]/div[2]/ol/li[1]/span");
	public static final By totalLabelAmount = By.xpath("//*[@id='page-container']/ul/li[1]/span");
	public static final By facebookLink1 = By.xpath("html/body/div[1]/div/div[1]/div[5]/div[2]/div[2]/a");
	public static final By facebookLink2 = By.xpath("html/body/div[1]/div/div[1]/div[5]/div[2]/div[1]/a");
	public static final By facebookLink3 = By.xpath("html/body/div[1]/div/div[1]/div[4]/div[2]/div[2]/a");
	public static final By facebookLikes1 = By.xpath("html/body/div[1]/div/div[1]/div[5]/div[2]/div[2]/span");
	public static final By facebookLikes2 = By.xpath("html/body/div[1]/div/div[1]/div[5]/div[2]/div[1]/span");
	public static final By facebookLikes3 = By.xpath("html/body/div[1]/div/div[1]/div[4]/div[2]/div[2]/span");
													
	
}
