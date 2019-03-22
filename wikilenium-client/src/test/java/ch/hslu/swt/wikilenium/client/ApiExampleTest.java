package ch.hslu.swt.wikilenium.client;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

public class ApiExampleTest {

	private final Wikilenium wikilenium;
	private TestClient client;

	public ApiExampleTest() {
		wikilenium = new Wikilenium();
	}

	@Before
	public void testSetup() {
		client = wikilenium.getChromeClient();
	}

	@After
	public void testCleanup() {
		client.close();
	}

	@Severity(SeverityLevel.NORMAL)
	@Description("Start at Page \"Bilanz\" and reach page \"Philosophie\" after 7 steps")
	@Test
	public void test_StartPageBilanz_ClickLimit7_PageIsPhilosophie() {
		boolean result = client.language("de").startPage("Bilanz").clickLimit(7).goalPage("Philosophie").run();

		Assert.assertTrue(result);
	}

	@Severity(SeverityLevel.NORMAL)
	@Description("Start at Page \"Coca-Cola\" and reach page \"Philosophie\" after 7 steps")
	@Test
	public void test_StartPageCocaCola_ClickLimit7_PageIsPhilosophie() {
		boolean result = client.language("de").startPage("Coca-Cola").clickLimit(7).goalPage("Philosophie").run();

		Assert.assertTrue(result);
	}

	@Severity(SeverityLevel.NORMAL)
	@Description("Start at Page \"Tee\" and reach page \"Philosophie\" after 7 steps")
	@Test
	public void test_Tee_ClickLimit7_PageIsPhilosophie() {
		boolean result = client.language("de").startPage("Tee").clickLimit(7).goalPage("Philosophie").run();

		Assert.assertTrue(result);
	}

	@Severity(SeverityLevel.NORMAL)
	@Description("Start at Page \"Coca-Cola\" and reach page \"Philosophie\" after 7 steps")
	@Test(expected = LoopException.class)
	public void test_StartPageCocaCola_ClickLimit7_ExpectLoopException() {
		boolean result = client.language("de").startPage("Coca-Cola").clickLimit(7).goalPage("Philosophie").run();

		Assert.assertTrue(result);
	}

	@Ignore
	@Severity(SeverityLevel.TRIVIAL)
	@Description("Start at Page \"Softwaretest\" and reach page \"Philosophie\" after 7 steps")
	@Test(expected = LoopException.class)
	public void test_StartSoftwaretest_ClickLimit7_PageIsPhilosophie() {
		boolean result = client.language("de").startPage("Softwaretest").clickLimit(7).goalPage("Philosophie").run();

		Assert.assertTrue(result);
	}
}