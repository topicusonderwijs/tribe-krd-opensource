package nl.topicus.eduarte.krd.web.components.panel.adresedit;

import nl.topicus.cobra.modelsv2.HibernateModel;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.tester.EduArteTestCase;
import nl.topicus.eduarte.web.components.panels.adresedit.AdresEditPanel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AdresEditPanelTest extends EduArteTestCase
{
	private IModel<Persoon> persoon;

	@Before
	public void init()
	{
		persoon = new HibernateModel<Persoon>(new Persoon());
		PersoonAdres persoonAdres = getPersoon().newAdres();
		persoonAdres.setFysiekadres(true);
		getPersoon().getAdressen().add(persoonAdres);

		Adres adres = persoonAdres.getAdres();
		adres.setGeheim(true);
		adres.setPostcode("7411HR");
		adres.setHuisnummer("11");
		adres.setStraat("Teststraat");
		adres.setPlaats("Deventer");
		adres.setLand(Land.getNederland());
	}

	@Test
	public void submitWerktMetBestaandNederlandsAdres()
	{
		tester.setupRequestAndResponse();
		renderAdresEditPanel();

		FormTester formTester = tester.newFormTester("panel:adresform");
		formTester.submit();

		tester.assertNoErrorMessage();
	}

	@Test
	public void submitWerktMetBestaandBuitenlandsAdres()
	{
		tester.setupRequestAndResponse();

		Adres adres = getPersoon().getFysiekAdres().getAdres();
		adres.setGeheim(true);
		adres.setPostcode("123456789012");
		adres.setHuisnummer("1002");
		adres.setPlaats("Hannover");
		Land land = new Land();
		land.setNaam("Duitsland");
		land.setCode("6040");
		adres.setLand(land);

		renderAdresEditPanel();

		FormTester formTester = tester.newFormTester("panel:adresform");
		formTester.submit();

		tester.assertNoErrorMessage();
	}

	@Test
	public void submitFaaltMetGekkeNederlandsePostcode()
	{
		tester.setupRequestAndResponse();
		renderAdresEditPanel();

		FormTester formTester = tester.newFormTester("panel:adresform");
		formTester.setValue("adres.postcode", "aaaaaa");
		formTester.submit();

		Assert.assertTrue("Het form geeft geen error, dit moet wel.", formTester.getForm()
			.hasError());
		tester
			.assertErrorMessages(new String[] {"Postcode van woonadres voldoet niet aan het postcodeformaat (1234AA)"});
	}

	@Test
	public void submitFaaltMetTeLangeNederlandsePostcode()
	{
		tester.setupRequestAndResponse();
		renderAdresEditPanel();

		FormTester formTester = tester.newFormTester("panel:adresform");
		formTester.setValue("adres.postcode", "7411HR11");
		formTester.submit();

		Assert.assertTrue(formTester.getForm().hasError());
		tester
			.assertErrorMessages(new String[] {"Postcode van woonadres voldoet niet aan het postcodeformaat (1234AA)"});
	}

	@Test
	public void submitFaaltMetTeLangeBuitenlandsePostcode()
	{
		tester.setupRequestAndResponse();

		Adres adres = getPersoon().getFysiekAdres().getAdres();
		adres.setPlaats("Onbekend");
		Land land = new Land();
		land.setNaam("Duitsland");
		land.setCode("6040");
		adres.setLand(land);

		renderAdresEditPanel();

		FormTester formTester = tester.newFormTester("panel:adresform");
		formTester.setValue("adres.postcode", "123456789012345");
		formTester.submit();

		Assert.assertTrue(formTester.getForm().hasError());
		tester
			.assertErrorMessages(new String[] {"'Postcode van woonadres' mag maximaal 12 karakters hebben."});
	}

	private void renderAdresEditPanel()
	{
		tester.startEduArtePanel(new TestPanelSource()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getTestPanel(String panelId)
			{
				return new AdresEditPanel<PersoonAdres>(panelId, new PropertyModel<PersoonAdres>(
					persoon, "fysiekAdressen[0]"), AdresEditPanel.Mode.POPUP);
			}
		});
	}

	protected Persoon getPersoon()
	{
		return persoon.getObject();
	}
}
