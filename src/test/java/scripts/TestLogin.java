package scripts;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;
import generic.BaseTest;
import generic.Utils;

public class TestLogin extends BaseTest{
	@Test
	public void testMethod() {
		test.log(LogStatus.INFO, "testMethod");
		String un = Utils.getXLData(dataPath, "Sheet1", 1,0);
		test.log(LogStatus.INFO, "un: "+un);
	}
}
