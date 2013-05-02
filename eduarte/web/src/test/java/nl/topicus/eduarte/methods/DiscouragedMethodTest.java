package nl.topicus.eduarte.methods;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.test.AbstractDiscouragedMethodTest;
import nl.topicus.cobra.web.components.form.modifier.AjaxRefreshModifier;
import nl.topicus.cobra.web.components.form.modifier.ExtendedAjaxRefreshModifier;

import org.junit.Test;

public class DiscouragedMethodTest extends AbstractDiscouragedMethodTest
{
	@Override
	protected List<String> getPackageNames()
	{
		return Arrays.asList("nl.topicus");
	}

	@Test
	public void ajaxRefreshModifier()
	{
		doCustomTest("Gebruik EduArteAjaxRefreshModifier", findConstructor(
			AjaxRefreshModifier.class, String.class, Serializable[].class), findConstructor(
			ExtendedAjaxRefreshModifier.class, String.class, Serializable[].class));
	}
}
