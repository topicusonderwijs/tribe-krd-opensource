package nl.topicus.cobra.templates.documents.rtf;

import junit.framework.Assert;
import nl.topicus.cobra.templates.FieldInfo;
import nl.topicus.cobra.templates.mocks.AutoModel;
import nl.topicus.cobra.templates.mocks.Versnellingsbak;
import nl.topicus.cobra.templates.resolvers.BeanPropertyResolver;

import org.junit.Test;

public class RtfAnnotationTest
{
	@Test
	public void TestAnnotation()
	{
		AutoModel model = new AutoModel("Golf", "Zilver", Versnellingsbak.HANDMATIG);
		BeanPropertyResolver resolver = new BeanPropertyResolver(model);
		FieldInfo naamInfo = resolver.getInfo("naam");
		Assert.assertNotNull(naamInfo);
		Assert.assertEquals(String.class, naamInfo.getFieldClass());

		FieldInfo kleurInfo = resolver.getInfo("kleur");
		Assert.assertNotNull(kleurInfo.getFieldClass());

		FieldInfo transmissieInfo = resolver.getInfo("versnellingsbak");
		Assert.assertEquals(Versnellingsbak.class, transmissieInfo.getFieldClass());

		FieldInfo afkortingInfo = resolver.getInfo("versnellingsbak.afkorting");
		Assert.assertEquals(String.class, afkortingInfo.getFieldClass());
	}
}
