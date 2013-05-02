package nl.topicus.eduarte.web.pages.beheer.organisatie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.core.principals.beheer.organisatie.OrganisatiemodelPrincipal;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheidAdres;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheidContactgegeven;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheidLocatie;
import nl.topicus.eduarte.entities.personen.OrganisatieEenheidContactPersoon;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OrganisatieEenheidLocatieTable;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Organisatie-eenheid", menu = "Beheer > Organisatie-eenheid > [OrganisatieEenheid]")
@InPrincipal(OrganisatiemodelPrincipal.class)
public class OrganisatieEenheidKaartPage extends AbstractBeheerPage<OrganisatieEenheid>
{
	public OrganisatieEenheidKaartPage(OrganisatieEenheid organisatieEenheid)
	{
		this(ModelFactory.getModel(organisatieEenheid), new OrganisatieEenheidZoekenPage());
	}

	public OrganisatieEenheidKaartPage(IModel<OrganisatieEenheid> organisatieEenheid,
			SecurePage returnPage)
	{
		super(organisatieEenheid, BeheerMenuItem.Organisatie_eenheden);
		setReturnPage(returnPage);
		createForm();
		add(new OrganisatieEenheidContactPersoonOverzichtPanel("contactpersonenPanel",
			new PropertyModel<List<OrganisatieEenheidContactPersoon>>(getContextModel(),
				"contactpersonen")));
	}

	private void createForm()
	{
		addAlgemeenFieldSet();
		addLocatieTable();
		addAdresTable();
		addFieldsetContactGegevens();
		createComponents();
	}

	private void addAlgemeenFieldSet()
	{
		AutoFieldSet<OrganisatieEenheid> algemeenFieldSet =
			new AutoFieldSet<OrganisatieEenheid>("organisatieEenheid", getContextModel(),
				"Algemeen");
		List<String> fields = new ArrayList<String>();
		fields.addAll(Arrays.asList("afkorting", "naam", "soortOrganisatieEenheid",
			"officieleNaam", "parent", "instellingBrincode", "bankrekeningnummer", "begindatum",
			"einddatum", "intakeWizardStap3Overslaan"));
		if (EduArteApp.get().isModuleActive(EduArteModuleKey.DIGITAALAANMELDEN))
			fields.addAll(Arrays.asList("tonenBijDigitaalAanmelden"));
		algemeenFieldSet.setPropertyNames(fields);
		algemeenFieldSet.setSortAccordingToPropertyNames(true);
		add(algemeenFieldSet);
	}

	private void addLocatieTable()
	{
		CustomDataPanel<OrganisatieEenheidLocatie> datapanel =
			new EduArteDataPanel<OrganisatieEenheidLocatie>("datapanelLocatie",
				new ListModelDataProvider<OrganisatieEenheidLocatie>(
					new PropertyModel<List<OrganisatieEenheidLocatie>>(getContextModel(),
						"locaties")), new OrganisatieEenheidLocatieTable());
		datapanel.setButtonsVisible(false);
		add(datapanel);
	}

	private void addAdresTable()
	{
		add(new AdressenPanel<OrganisatieEenheidAdres>("inputFieldsAdres",
			getOrganisatieEenheidModel()));
	}

	@SuppressWarnings("unchecked")
	private IModel<OrganisatieEenheid> getOrganisatieEenheidModel()
	{
		return (IModel<OrganisatieEenheid>) getDefaultModel();
	}

	private void addFieldsetContactGegevens()
	{
		add(new ContactgegevenEntiteitPanel<OrganisatieEenheidContactgegeven>(
			"inputFieldsContactgegevens",
			new LoadableDetachableModel<List<OrganisatieEenheidContactgegeven>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected List<OrganisatieEenheidContactgegeven> load()
				{
					return getContextModelObject().getContactgegevens();
				}
			}, false));
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new BewerkenButton<OrganisatieEenheid>(panel,
			OrganisatieEenheidEditPage.class, getContextModel(), OrganisatieEenheidKaartPage.this));

		panel.addButton(new TerugButton(panel, OrganisatieEenheidKaartPage.this.getReturnPage()));
	}

	/**
	 * @see nl.topicus.eduarte.web.pages.SecurePage#getBookmarkConstructorArguments(java.util.List,
	 *      java.util.List)
	 */
	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(OrganisatieEenheid.class);
		ctorArgValues.add(getDefaultModel());
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
