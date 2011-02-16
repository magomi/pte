package de.intecsoft;

import junit.framework.TestCase;
import org.apache.wicket.util.tester.WicketTester;

/**
 * Simple test using the WicketTester
 */
public class TestHomePage extends TestCase
{
	private WicketTester tester;

	@Override
	public void setUp()
	{
		tester = new WicketTester(new PteApplication());
	}

	public void testRenderMyPage()
	{
		//start and render the test page
		tester.startPage(PtePage.class);

		//assert rendered page class
		tester.assertRenderedPage(PtePage.class);

		//assert rendered label component
		tester.assertLabel("message", "If you see this message wicket is properly configured and running");
	}
}
