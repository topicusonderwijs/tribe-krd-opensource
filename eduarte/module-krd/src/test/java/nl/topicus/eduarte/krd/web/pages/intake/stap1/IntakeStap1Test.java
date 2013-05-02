package nl.topicus.eduarte.krd.web.pages.intake.stap1;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.quicksearch.AbstractBaseSearchEditor;
import nl.topicus.eduarte.entities.adres.TypeContactgegeven;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.krd.web.pages.KRDPagesTestCase;
import nl.topicus.eduarte.krd.web.pages.intake.stap0.IntakeStap0Zoeken;
import nl.topicus.eduarte.krd.web.pages.intake.stap2.IntakeStap2Achtergrond;
import nl.topicus.eduarte.tester.matchers.AndMatcher;
import nl.topicus.eduarte.tester.matchers.ComponentMatcher;
import nl.topicus.eduarte.tester.matchers.ComponentTypeMatcher;
import nl.topicus.eduarte.tester.matchers.OrMatcher;
import nl.topicus.eduarte.tester.matchers.PropertyExpressionMatcher;
import nl.topicus.eduarte.web.components.quicksearch.voorvoegsel.VoorvoegselSearchEditor;
import nl.topicus.eduarte.web.components.text.GeboortedatumField;
import nl.topicus.eduarte.web.pages.EduArteErrorPage;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerZoekenPage;

import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.Result;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class IntakeStap1Test extends KRDPagesTestCase
{
	private static final String FORM_ID = "personaliaForm";

	@Test
	public void navigeerNaarIntakeWizard()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(DeelnemerZoekenPage.class);

		tester.clickLink("layLeft:bottom:rightButtons:0:link");
		tester.assertRenderedPage(IntakeStap0Zoeken.class);
		tester.clickLink("layLeft:bottom:rightButtons:0:link");
		tester.assertRenderedPage(IntakeStap1Personalia.class);
	}

	@Test
	public void intakeMagGestartWordenAlsApplicatieBeheerder()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(IntakeStap1Personalia.class);
		tester.assertRenderedPage(IntakeStap1Personalia.class);
	}

	@Test
	public void intakeMagNietGestartWordenAlsMedewerker()
	{
		tester.voerTestUitMetMedewerker();
		tester.setupRequestAndResponse();

		tester.startPage(IntakeStap1Personalia.class);
		tester.assertRenderedPage(EduArteErrorPage.class);
		EduArteErrorPage renderedPage = (EduArteErrorPage) tester.getLastRenderedPage();
		assertTrue(renderedPage.getThrownError() instanceof UnauthorizedInstantiationException);
	}

	@Test
	public void navigeerVanStap1NaarStap2ZonderGegevensFaalt()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(IntakeStap1Personalia.class);
		tester.assertRenderedPage(IntakeStap1Personalia.class);

		volgende();

		tester.assertRenderedPage(IntakeStap1Personalia.class);
	}

	/**
	 * @mantis 37628
	 */
	@Test
	@Ignore
	public void navigeerVanStap1naarStap2()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(IntakeStap1Personalia.class);
		tester.assertRenderedPage(IntakeStap1Personalia.class);

		IntakeStap1Personalia page = (IntakeStap1Personalia) tester.getLastRenderedPage();
		Deelnemer deelnemer = page.getWizard().getDeelnemer();
		Persoon persoon = deelnemer.getPersoon();
		persoon.setGeslacht(Geslacht.Onbekend);
		persoon.setOfficieleAchternaam("Puk");

		List<PersoonAdres> adressen = persoon.getFysiekAdressen();
		for (PersoonAdres adres : adressen)
		{
			adres.getAdres().setPostcode("1234AA");
			adres.getAdres().setHuisnummer("123");
		}
		adressen = persoon.getPostAdressen();
		for (PersoonAdres adres : adressen)
		{
			adres.getAdres().setPostcode("1234AA");
			adres.getAdres().setHuisnummer("123");
		}

		List<PersoonContactgegeven> contactgegevens = persoon.getContactgegevens();
		for (PersoonContactgegeven contactgegeven : contactgegevens)
		{
			if (contactgegeven.getSoortContactgegeven().getTypeContactgegeven().equals(
				TypeContactgegeven.Email))
				contactgegeven.setContactgegeven("1@1.nl");
			else if (contactgegeven.getSoortContactgegeven().getTypeContactgegeven().equals(
				TypeContactgegeven.Homepage))
				contactgegeven.setContactgegeven("www.1.nl");
			else
				contactgegeven.setContactgegeven("1");
		}

		volgende();

		tester.assertErrorMessages(new String[] {});
		Result result = tester.isRenderedPage(IntakeStap2Achtergrond.class);
		if (result.wasFailed() && tester.getLastRenderedPage() instanceof EduArteErrorPage)
		{
			((EduArteErrorPage) tester.getLastRenderedPage()).getThrownError().printStackTrace(
				System.err);
		}
		assertFalse(result.getMessage(), result.wasFailed());
	}

	@Test
	@Ignore
	public void navigeerVanStap1naarStap2naarStap1naarStap2()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(IntakeStap1Personalia.class);
		tester.assertRenderedPage(IntakeStap1Personalia.class);

		IntakeStap1Personalia stap1 = (IntakeStap1Personalia) tester.getLastRenderedPage();
		Deelnemer deelnemer = stap1.getWizard().getDeelnemer();
		Persoon persoon = deelnemer.getPersoon();
		persoon.setGeslacht(Geslacht.Onbekend);
		persoon.setOfficieleAchternaam("Puk");

		List<PersoonAdres> adressen = persoon.getFysiekAdressen();
		for (PersoonAdres adres : adressen)
		{
			adres.getAdres().setPostcode("1234AA");
			adres.getAdres().setHuisnummer("123");
		}
		adressen = persoon.getPostAdressen();
		for (PersoonAdres adres : adressen)
		{
			adres.getAdres().setPostcode("1234AA");
			adres.getAdres().setHuisnummer("123");
		}

		List<PersoonContactgegeven> contactgegevens = persoon.getContactgegevens();
		for (PersoonContactgegeven contactgegeven : contactgegevens)
		{
			if (contactgegeven.getSoortContactgegeven().getTypeContactgegeven().equals(
				TypeContactgegeven.Email))
				contactgegeven.setContactgegeven("1@1.nl");
			else if (contactgegeven.getSoortContactgegeven().getTypeContactgegeven().equals(
				TypeContactgegeven.Homepage))
				contactgegeven.setContactgegeven("www.1.nl");
			else
				contactgegeven.setContactgegeven("1");
		}

		volgende();

		tester.assertErrorMessages(new String[] {});
		tester.assertRenderedPage(IntakeStap2Achtergrond.class);

		// back button
		tester.startPage(stap1);
		volgende();
		tester.assertRenderedPage(IntakeStap2Achtergrond.class);
	}

	/**
	 * @mantis 37634
	 */
	@Test
	public void officieleNaamEnAanspreekNaamGelijk()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(IntakeStap1Personalia.class);
		tester.assertRenderedPage(IntakeStap1Personalia.class);

		IntakeStap1Personalia page = (IntakeStap1Personalia) tester.getLastRenderedPage();

		AutoFieldSet< ? > personalia = page.getPersonalia();

		CheckBox checkbox = (CheckBox) personalia.findFieldComponent("aanspreeknaamWijktAf");
		TextField< ? > officieleAchternaam =
			(TextField< ? >) personalia.findFieldComponent("persoon.officieleAchternaam");
		VoorvoegselSearchEditor officieleVoorvoegsel =
			(VoorvoegselSearchEditor) personalia.findFieldComponent("persoon.officieleVoorvoegsel");
		TextField< ? > achternaam =
			(TextField< ? >) personalia.findFieldComponent("persoon.achternaam");
		VoorvoegselSearchEditor voorvoegsel =
			(VoorvoegselSearchEditor) personalia.findFieldComponent("persoon.voorvoegsel");

		assertEquals("achternamen", officieleAchternaam.getDefaultModelObjectAsString(), achternaam
			.getDefaultModelObjectAsString());
		assertEquals("voorvoegsel", officieleVoorvoegsel.getDefaultModelObjectAsString(),
			voorvoegsel.getDefaultModelObjectAsString());

		tester.assertModelValue(checkbox.getPageRelativePath(), false);

		voerAjaxUpdateUit("persoon.officieleAchternaam", "vries", "Vries");
		tester.assertModelValue(achternaam.getPageRelativePath(), "Vries");

		voerAjaxUpdateUit("persoon.officieleVoorvoegsel", "de", "de");
		tester.assertModelValue(voorvoegsel.getPageRelativePath(), "de");
	}

	@Test
	public void achternaamKrijgtHoofdletter()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(IntakeStap1Personalia.class);
		tester.assertRenderedPage(IntakeStap1Personalia.class);

		voerAjaxUpdateUit("persoon.officieleAchternaam", "naam", "Naam");
	}

	@Test
	public void voorlettersInHoofdletters()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(IntakeStap1Personalia.class);
		tester.assertRenderedPage(IntakeStap1Personalia.class);

		voerAjaxUpdateUit("persoon.voorletters", "jtfjm", "J.T.F.J.M.");
	}

	@Test
	public void voornamenInHoofdletters()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(IntakeStap1Personalia.class);
		tester.assertRenderedPage(IntakeStap1Personalia.class);

		voerAjaxUpdateUit("persoon.voornamen", "jan-willem", "Jan-Willem");
		voerAjaxUpdateUit("persoon.voornamen", "Jan peter", "Jan Peter");
	}

	@Test
	public void registratiedatumVerplicht()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(IntakeStap1Personalia.class);
		FormTester formTester = tester.newFormTester(FORM_ID);

		ComponentMatcher matcher =
			new AndMatcher(new ComponentTypeMatcher(FormComponent.class),
				new PropertyExpressionMatcher("registratieDatum"));

		FormComponent<Date> registratieDatum = tester.first(formTester.getForm(), matcher);
		Assert.assertTrue("required", registratieDatum.isRequired());
	}

	/**
	 * @mantis 37579
	 */
	@Test
	public void geboortedatumNietVerplicht()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(IntakeStap1Personalia.class);
		FormTester formTester = tester.newFormTester(FORM_ID);

		ComponentMatcher matcher =
			new AndMatcher(new ComponentTypeMatcher(GeboortedatumField.class),
				new PropertyExpressionMatcher("persoon.geboortedatum"));

		GeboortedatumField geboortedatum = tester.first(formTester.getForm(), matcher);
		Assert.assertFalse("required", geboortedatum.isRequired());
	}

	/**
	 * @mantis 37615
	 */
	@Test
	public void vulVoorlettersMetLettersVanVoornamen()
	{
		tester.voerTestUitMetApplicatieBeheerder();
		tester.setupRequestAndResponse();

		tester.startPage(IntakeStap1Personalia.class);
		tester.assertRenderedPage(IntakeStap1Personalia.class);

		voerAjaxUpdateUit("persoon.voornamen", "voor namen", "Voor Namen");
		TextField<String> voorletters =
			tester.first(tester.getLastRenderedPage(), new PropertyExpressionMatcher(
				"persoon.voorletters"));
		Assert.assertEquals("V.N.", voorletters.getDefaultModelObjectAsString());
	}

	private void volgende()
	{
		// dankzij bug WICKET-2203 is het niet mogelijk om de submitlink van de volgende
		// knop te gebruiken voor het submitten. Dus nu maar via het form doen.
		FormTester formTester = tester.newFormTester(FORM_ID);
		SubmitLink link =
			(SubmitLink) tester.getLastRenderedPage().get("layLeft:bottom:rightButtons:1:link");
		link.onSubmit();
		formTester.submit();
	}

	private void voerAjaxUpdateUit(String property, String naam, String verwachteNaam)
	{
		tester.setupRequestAndResponse();

		tester.startPage(IntakeStap1Personalia.class);

		FormTester formTester = tester.newFormTester(FORM_ID);

		ComponentMatcher matcher =
			new AndMatcher(new OrMatcher(new ComponentTypeMatcher(TextField.class),
				new ComponentTypeMatcher(AbstractBaseSearchEditor.class)),
				new PropertyExpressionMatcher(property));

		FormComponent< ? > field = tester.first(formTester.getForm(), matcher);
		if (field instanceof AbstractBaseSearchEditor< ? , ? >)
		{
			tester.executeQuickSearchSelect((AbstractBaseSearchEditor< ? , ? >) field, naam);
		}
		else
		{
			tester.setValue(field, naam);
			tester.executeAjaxEvent(field, "onblur");
		}
		Assert.assertEquals(verwachteNaam, field.getDefaultModelObjectAsString());
	}
}
