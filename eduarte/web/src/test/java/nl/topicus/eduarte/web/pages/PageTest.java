package nl.topicus.eduarte.web.pages;

import java.io.File;

import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.test.AbstractPageTest;

import org.junit.Test;

public class PageTest extends AbstractPageTest
{
	/**
	 * Test alle Pages uit Eduarte.
	 */
	@Test
	public void testEduArte()
	{
		doTest("nl" + File.separator + "topicus" + File.separator + "eduarte");
	}

	@Override
	protected boolean testClass(Class< ? > clazz)
	{
		boolean ret = super.testClass(clazz);
		// check aanwezigheid annotatie
		if (!clazz.isAnnotationPresent(InPrincipal.class))
		{
			errorBuilder.append(clazz.getName() + " heeft geen InPrincipal annotatie.\n");
			ret = true;
		}
		return ret;
	}
}
