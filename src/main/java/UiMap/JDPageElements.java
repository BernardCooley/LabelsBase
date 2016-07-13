package UiMap;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;

public class JDPageElements {
	
	//LabelsBase
	public static final By searchBox = By.xpath("//*[@id='query-start']");
	public static final By dropdown = By.xpath("//*[@id='search-in-start']");
	public static final By labelLink = By.xpath("//*[@id='lhs-facets']/table/tbody/tr[2]/td[2]/ul[6]/li[2]/a/b/strong/div/abbr");
	public static final By releaseAmount = By.xpath("//*[@id='lhs-facets']/table/tbody/tr[2]/td[2]/ul[6]/li[2]/a/b/strong/span");
	public static final By labelSeeAll = By.xpath("//*[@id='lhs-facets']/table/tbody/tr[2]/td[2]/ul[6]/lh/div[2]/a");
	public static final By labelsList = By.xpath("//*[@id='facet-options-list']/ul[5]/li");
	public static final By filterBtn = By.xpath("//*[@id='dialog-filter']");
	
	public static List<By> getLabelsList(int listSize) {
		List<By> labelsList = new ArrayList<By>();
		
		for (int i = 0; i < listSize; i++) {
			labelsList.add(By.xpath("//*[@id='facet-options-list']/ul[5]/li[" + (i+1) + "]//label"));
		}
		return labelsList;
	}
	
	public static List<By> getreleaseAmtList(int listSize) {
		List<By> labelsList = new ArrayList<By>();
		
		for (int i = 0; i < listSize; i++) {
			labelsList.add(By.xpath("//*[@id='facet-options-list']/ul[5]/li[" + (i+1) + "]//span"));
		}
		return labelsList;
	}
													
//	public static final By  = By.xpath("");
}
