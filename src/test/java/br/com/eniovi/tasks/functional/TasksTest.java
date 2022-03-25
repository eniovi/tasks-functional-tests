package br.com.eniovi.tasks.functional;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TasksTest {

	private WebDriver driver;

	@Before
	public void setup() throws URISyntaxException {
		final Path chromeDriverPath = Paths
				.get(ClassLoader.getSystemResource("drivers/chrome-v99/linux-chromedriver").toURI());
		System.setProperty("webdriver.chrome.driver", chromeDriverPath.toString());
		driver = new ChromeDriver();
		driver.navigate().to("http://172.29.0.3:8080/tasks");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test
	public void shouldNotAddTasksWithoutDate() {
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
	public void shouldNotAddTasksWithoutDescription() {
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
	public void shouldNotAddTasksWithPastDate() {
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
	public void shouldSaveTaskWithSuccess() {
		try {
			driver.findElement(By.id("addTodo")).click();

			driver.findElement(By.id("task")).sendKeys("Teste via Selenium");

			final LocalDate today = LocalDate.now();
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			final String todayFormatter = today.format(formatter);
			driver.findElement(By.id("dueDate")).sendKeys(todayFormatter);

			driver.findElement(By.id("saveButton")).click();

			final String message = driver.findElement(By.id("message")).getText();
			assertEquals("Success!", message);
		} finally {
			driver.quit();
		}
	}

}
