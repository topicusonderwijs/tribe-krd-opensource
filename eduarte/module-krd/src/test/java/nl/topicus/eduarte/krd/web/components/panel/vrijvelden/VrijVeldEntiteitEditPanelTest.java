package nl.topicus.eduarte.krd.web.components.panel.vrijvelden;

import junit.framework.Assert;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.vrijevelden.PersoonVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldEntiteit;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldType;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.tester.EduArteTestCase;
import nl.topicus.eduarte.tester.TestFormComponentPanelSource;

import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;

public class VrijVeldEntiteitEditPanelTest extends EduArteTestCase
{
	private IModel<Persoon> testPersoonModel;

	@Before
	public void setup()
	{
		testPersoonModel =
			ModelFactory.getCompoundChangeRecordingModel(new Persoon(), new DefaultModelManager(
				VrijVeld.class, PersoonVrijVeld.class, Persoon.class));
	}

	@Test
	public void submitLeegVrijVeldMet1Gegeven()
	{
		addEenVrijVeld();

		renderVrijVeldEntiteitEditPanel();

		FormTester formTester = tester.newFormTester("form");
		formTester.submit();

		Assert.assertEquals(0, getTestPersoon().getVrijVelden().size());
	}

	@Test
	public void submitLeegVrijVeldMet2Gegevens()
	{
		addEenVrijVeld();
		renderVrijVeldEntiteitEditPanel();

		getEersteVrijVeld().setTextWaarde("1");
		addTweeVrijVeld();

		ListView< ? > list = tester.getListView("form:panel:gegevens:vrijVeldenList");
		list.renderComponent();

		FormTester formTester = tester.newFormTester("form");
		formTester.submit();

		Assert.assertEquals(1, getTestPersoon().getVrijVelden().size());
	}

	@Test
	public void submitLeegVrijVeldMet3Gegevens()
	{
		addEenVrijVeld();
		renderVrijVeldEntiteitEditPanel();

		getEersteVrijVeld().setTextWaarde("1");
		addTweeVrijVeld();
		addDrieVrijVeld();
		getDerdeVrijVeld().setTextWaarde("3");

		ListView< ? > list = tester.getListView("form:panel:gegevens:vrijVeldenList");
		list.renderComponent();

		FormTester formTester = tester.newFormTester("form");
		formTester.submit();

		Assert.assertEquals(2, getTestPersoon().getVrijVelden().size());
	}

	private void renderVrijVeldEntiteitEditPanel()
	{
		tester.startFormPanel(new TestFormComponentPanelSource()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public FormComponentPanel< ? > getTestPanel(String panelId)
			{
				VrijVeldEntiteitEditPanel<Persoon> panel =
					new VrijVeldEntiteitEditPanel<Persoon>(panelId, getTestPersoonModel());

				return panel;
			}
		});

		ListView< ? > list = tester.getListView("form:panel:gegevens:vrijVeldenList");
		Assert.assertEquals(getTestPersoon().getVrijVelden().size(), list.size());
	}

	private void addEenVrijVeld()
	{
		VrijVeld veld = new VrijVeld();
		veld.setNaam("1");
		veld.setCategorie(VrijVeldCategorie.DEELNEMERPERSONALIA);
		veld.setType(VrijVeldType.TEKST);
		PersoonVrijVeld pveld = getTestPersoon().newVrijVeld();
		pveld.setVrijVeld(veld);
		pveld.setPersoon(getTestPersoon());
		getTestPersoon().getVrijVelden().add(pveld);
	}

	private void addTweeVrijVeld()
	{
		VrijVeld veld = new VrijVeld();
		veld.setNaam("2");
		veld.setCategorie(VrijVeldCategorie.DEELNEMERPERSONALIA);
		veld.setType(VrijVeldType.TEKST);
		PersoonVrijVeld pveld = getTestPersoon().newVrijVeld();
		pveld.setVrijVeld(veld);
		pveld.setPersoon(getTestPersoon());
		getTestPersoon().getVrijVelden().add(pveld);
	}

	private void addDrieVrijVeld()
	{
		VrijVeld veld = new VrijVeld();
		veld.setNaam("3");
		veld.setCategorie(VrijVeldCategorie.DEELNEMERPERSONALIA);
		veld.setType(VrijVeldType.TEKST);
		PersoonVrijVeld pveld = getTestPersoon().newVrijVeld();
		pveld.setVrijVeld(veld);
		pveld.setPersoon(getTestPersoon());
		getTestPersoon().getVrijVelden().add(pveld);
	}

	private VrijVeldEntiteit getEersteVrijVeld()
	{
		if (getTestPersoon().getVrijVelden().size() > 0)
			return getTestPersoon().getVrijVelden().get(0);

		return null;
	}

	@SuppressWarnings("unused")
	private VrijVeldEntiteit getTweedeVrijVeld()
	{
		if (getTestPersoon().getVrijVelden().size() > 1)
			return getTestPersoon().getVrijVelden().get(1);

		return null;
	}

	private VrijVeldEntiteit getDerdeVrijVeld()
	{
		if (getTestPersoon().getVrijVelden().size() > 2)
			return getTestPersoon().getVrijVelden().get(2);

		return null;
	}

	public Persoon getTestPersoon()
	{
		return getTestPersoonModel().getObject();
	}

	public IModel<Persoon> getTestPersoonModel()
	{
		return testPersoonModel;
	}
}
