package br.com.eniovi.tasks.functional;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TasksTest {

	private WebDriver driver;

	@Before
	public void setup() throws URISyntaxException, MalformedURLException {
		final DesiredCapabilities cap = DesiredCapabilities.chrome();
		driver = new RemoteWebDriver(new URL("http://172.29.0.7:4444/wd/hub"), cap);
		driver.navigate().to("http://172.29.0.3:8080/tasks");
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	@Test
	public void shouldNotAddTasksWithoutDate() throws MalformedURLException {
		try {
			driver.findElement(By.id("addTodo")).click();

			driver.findElement(By.id("task")).sendKeys("Teste via Selenium");

			driver.findElement(By.id("saveButton")).click();

			final String message = driver.findElement(By.id("message")).getText();
			assertEquals("Fill the due date", message);
		} finally {
			driver.quit();
		}
	}

	@Test
	public void shouldNotAddTasksWithoutDescription() throws MalformedURLException {
		try {
			driver.findElement(By.id("addTodo")).click();

			final LocalDate today = LocalDate.now();
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			final String todayFormatter = today.format(formatter);
			driver.findElement(By.id("dueDate")).sendKeys(todayFormatter);

			driver.findElement(By.id("saveButton")).click();

			final String message = driver.findElement(By.id("message")).getText();
			assertEquals("Fill the task description", message);
		} finally {
			driver.quit();
		}
	}

	@Test
	public void shouldNotAddTasksWithPastDate() throws MalformedURLException {
		try {
			driver.findElement(By.id("addTodo")).click();

			final LocalDate today = LocalDate.now().minusDays(1);
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			final String todayFormatter = today.format(formatter);
			driver.findElement(By.id("dueDate")).sendKeys(todayFormatter);

			driver.findElement(By.id("saveButton")).click();

			final String message = driver.findElement(By.id("message")).getText();
			assertEquals("Fill the task description", message);
		} finally {
			driver.quit();
		}
	}

	@Test
	public void shouldSaveTaskWithSuccess() throws MalformedURLException {
		try {
			driver.findElement(By.id("addTodo")).click();

			driver.findElement(By.id("task")).sendKeys("Teste via Selenium");

			final LocalDate tomorrow = LocalDate.now().plusDays(1);
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			final String tomorrowFormatter = tomorrow.format(formatter);
			driver.findElement(By.id("dueDate")).sendKeys(tomorrowFormatter);

			driver.findElement(By.id("saveButton")).click();

			final String message = driver.findElement(By.id("message")).getText();
			assertEquals("Success!", message);
		} finally {
			driver.quit();
		}
	}
	
	@Test
	public void shouldRemoveTaskWithSuccess() throws MalformedURLException {
		try {
			driver.findElement(By.id("addTodo")).click();

			driver.findElement(By.id("task")).sendKeys("Teste via Selenium");

			final LocalDate tomorrow = LocalDate.now().plusDays(1);
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			final String tomorrowFormatter = tomorrow.format(formatter);
			driver.findElement(By.id("dueDate")).sendKeys(tomorrowFormatter);

			driver.findElement(By.id("saveButton")).click();

			final String messageAdd = driver.findElement(By.id("message")).getText();
			assertEquals("Success!", messageAdd);
			
			driver.findElement(By.xpath("//a[@class='btn btn-outline-danger btn-sm']")).click();
			final String messageRemove = driver.findElement(By.id("message")).getText();
			assertEquals("Success!", messageRemove);
		} finally {
			driver.quit();
		}
	}

}
