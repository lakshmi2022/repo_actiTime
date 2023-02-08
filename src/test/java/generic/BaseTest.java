package generic;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
	public final String pPath = "base.properties";
	public final String dataPath = "./data/input.xlsx";
	public WebDriver driver;
	public WebDriverWait wait;
	public static ExtentReports extent;
	public ExtentTest test;
	static {
		WebDriverManager.chromedriver().setup();
		WebDriverManager.firefoxdriver().setup();
		
	}
	
	@BeforeSuite
	public void createReport() {
		extent = new ExtentReports("target/Spark.html");		
	}
	
	@Parameters({"config"})
	@BeforeMethod
	public void initialSettings(@Optional(pPath) String path, Method m) throws Exception {
		path="./properties/"+path;
		
		test = extent.startTest(m.getName());
		test.log(LogStatus.INFO, "current method is: "+m.getName());
		String appURL = Utils.getProperty(path, "APPURL");
		String browser = Utils.getProperty(path, "BROWSER");
		String grid = Utils.getProperty(path, "GRID");
		String remoteURL = Utils.getProperty(path, "REMOTEURL");
		test.log(LogStatus.INFO, "browser is: "+browser);
		
		if(grid.equalsIgnoreCase("yes")) {
			
			DesiredCapabilities d = new DesiredCapabilities();
			d.setBrowserName(browser);
			driver = new RemoteWebDriver(new URL(remoteURL), d);
			
		}else {
				if(browser.equalsIgnoreCase("chrome")) {
				driver = new ChromeDriver();
				}else {
					driver = new FirefoxDriver();
				}
		}
		driver.manage().window().maximize();
		
		test.log(LogStatus.INFO, "application url is: "+appURL);
		driver.get(appURL);
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	
	@AfterMethod
	public void closeBrowser(ITestResult result) throws Exception {
		
		int status = result.getStatus();
		String testMethodName = result.getName();
		
		if(status == 2) {
			String failMsg = result.getThrowable().getMessage();
			test.log(LogStatus.FAIL, failMsg);
			
			TakesScreenshot t = (TakesScreenshot) driver;
			File srcImg = t.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(srcImg, new File("./target/"+testMethodName+".png"));
			String html = test.addScreenCapture(testMethodName+".png");
			test.log(LogStatus.FAIL, html);
			
		}
		
	}
	
	@AfterSuite
	public void publishReport() {
		extent.flush();
	}

}
