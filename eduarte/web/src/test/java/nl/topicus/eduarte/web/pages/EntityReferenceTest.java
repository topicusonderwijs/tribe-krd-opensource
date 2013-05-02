package nl.topicus.eduarte.web.pages;

import java.io.File;

import nl.topicus.cobra.test.AbstractEntityReferenceTest;
import nl.topicus.eduarte.entities.Entiteit;

import org.junit.Test;

public class EntityReferenceTest extends AbstractEntityReferenceTest
{
	/**
	 * Test alle Pages uit eduarte.
	 */
	@Test
	public void testEduArte()
	{
		doTest("nl" + File.separator + "topicus" + File.separator + "eduarte", Entiteit.class);
	}
}
