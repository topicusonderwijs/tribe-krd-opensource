package nl.topicus.eduarte.web.pages;

import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.test.AbstractHtmlTest;

public class HtmlTest extends AbstractHtmlTest
{
	@Override
	protected List<String> getPackageNames()
	{
		return Arrays.asList("nl.topicus.cobra", "nl.topicus.eduarte");
	}
}
