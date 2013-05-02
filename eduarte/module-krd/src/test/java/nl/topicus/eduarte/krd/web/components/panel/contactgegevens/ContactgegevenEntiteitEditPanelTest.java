package nl.topicus.eduarte.krd.web.components.panel.contactgegevens;

import junit.framework.Assert;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.eduarte.entities.IContactgegevenEntiteit;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.adres.TypeContactgegeven;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.tester.EduArteTestCase;
import nl.topicus.eduarte.tester.TestFormComponentPanelSource;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitEditPanel;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;

public class ContactgegevenEntiteitEditPanelTest extends EduArteTestCase
{
	private IModel<Persoon> testPersoonModel;

	@Before
	public void setup()
	{
		testPersoonModel =
			ModelFactory.getCompoundChangeRecordingModel(new Persoon(), new DefaultModelManager(
				SoortContactgegeven.class, PersoonContactgegeven.class, Persoon.class));
	}

	@Test
	public void submitLeegContactgegevenMet1Gegeven()
	{
		addEenContactgegeven();

		renderContactgegevenEntiteitEditPanel();

		FormTester formTester = tester.newFormTester("form");
		formTester.submit();

		Assert.assertEquals(0, getTestPersoon().getContactgegevens().size());
	}

	@Test
	public void submitLeegContactgegevenMet2Gegevens()
	{
		addEenContactgegeven();
		renderContactgegevenEntiteitEditPanel();

		getEersteContactgegeven().setContactgegeven("1");
		addTweeContactgegeven();

		ListView< ? > list = tester.getListView("form:panel:gegevens:contactgegevenList");
		list.renderComponent();

		FormTester formTester = tester.newFormTester("form");
		formTester.submit();

		Assert.assertEquals(1, getTestPersoon().getContactgegevens().size());
	}

	@Test
	public void submitLeegContactgegevenMet3Gegevens()
	{
		addEenContactgegeven();
		renderContactgegevenEntiteitEditPanel();

		getEersteContactgegeven().setContactgegeven("1");
		addTweeContactgegeven();
		addDrieContactgegeven();
		getDerdeContactgegeven().setContactgegeven("3");

		ListView< ? > list = tester.getListView("form:panel:gegevens:contactgegevenList");
		list.renderComponent();

		FormTester formTester = tester.newFormTester("form");
		formTester.submit();

		Assert.assertEquals(2, getTestPersoon().getContactgegevens().size());
	}

	private void renderContactgegevenEntiteitEditPanel()
	{
		tester.startFormPanel(new TestFormComponentPanelSource()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public ContactgegevenEntiteitEditPanel<PersoonContactgegeven, Persoon> getTestPanel(
					String panelId)
			{
				ContactgegevenEntiteitEditPanel<PersoonContactgegeven, Persoon> panel =
					new ContactgegevenEntiteitEditPanel<PersoonContactgegeven, Persoon>(panelId,
						getTestPersoonModel());

				return panel;
			}
		});

		ListView< ? > list = tester.getListView("form:panel:gegevens:contactgegevenList");
		Assert.assertEquals(getTestPersoon().getContactgegevens().size(), list.size());
	}

	private void addEenContactgegeven()
	{
		SoortContactgegeven soort = new SoortContactgegeven();
		soort.setNaam("1");
		soort.setCode("1");
		soort.setTypeContactgegeven(TypeContactgegeven.Telefoon);
		PersoonContactgegeven gegeven = getTestPersoon().newContactgegeven();
		gegeven.setSoortContactgegeven(soort);
		gegeven.setPersoon(getTestPersoon());

		getTestPersoon().getContactgegevens().add(gegeven);
	}

	private void addTweeContactgegeven()
	{
		SoortContactgegeven soort = new SoortContactgegeven();
		soort.setNaam("2");
		soort.setCode("2");
		soort.setTypeContactgegeven(TypeContactgegeven.Telefoon);
		PersoonContactgegeven gegeven = getTestPersoon().newContactgegeven();
		gegeven.setSoortContactgegeven(soort);
		gegeven.setPersoon(getTestPersoon());

		getTestPersoon().getContactgegevens().add(gegeven);
	}

	private void addDrieContactgegeven()
	{
		SoortContactgegeven soort = new SoortContactgegeven();
		soort.setNaam("3");
		soort.setCode("3");
		soort.setTypeContactgegeven(TypeContactgegeven.Telefoon);
		PersoonContactgegeven gegeven = getTestPersoon().newContactgegeven();
		gegeven.setSoortContactgegeven(soort);
		gegeven.setPersoon(getTestPersoon());

		getTestPersoon().getContactgegevens().add(gegeven);
	}

	private IContactgegevenEntiteit getEersteContactgegeven()
	{
		if (getTestPersoon().getContactgegevens().size() > 0)
			return getTestPersoon().getContactgegevens().get(0);

		return null;
	}

	@SuppressWarnings("unused")
	private IContactgegevenEntiteit getTweedeContactgegeven()
	{
		if (getTestPersoon().getContactgegevens().size() > 1)
			return getTestPersoon().getContactgegevens().get(1);

		return null;
	}

	private IContactgegevenEntiteit getDerdeContactgegeven()
	{
		if (getTestPersoon().getContactgegevens().size() > 2)
			return getTestPersoon().getContactgegevens().get(2);

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
