package nl.topicus.eduarte.web.pages.medewerker;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.eduarte.core.principals.medewerker.MedewerkerInzien;
import nl.topicus.eduarte.dao.helpers.RolDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.OrganisatieMedewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.providers.MedewerkerProvider;
import nl.topicus.eduarte.web.components.image.PersoonImage;
import nl.topicus.eduarte.web.components.menu.MedewerkerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OrganisatieLocatieTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.RolTable;
import nl.topicus.eduarte.zoekfilters.RolZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.PropertyModel;

/**
 * Medewekrerkaart
 * 
 * @author loite
 */
@PageInfo(title = "Medewerkerkaart", menu = {"Medewerker > [medewerker]"})
@InPrincipal(MedewerkerInzien.class)
public class MedewerkerkaartPage extends AbstractMedewerkerPage
{
	private static final long serialVersionUID = 1L;

	public MedewerkerkaartPage(MedewerkerProvider provider)
	{
		this(provider.getMedewerker());
	}

	public MedewerkerkaartPage(Medewerker medewerker)
	{
		super(MedewerkerMenuItem.Medewerkerkaart, medewerker);
		add(new PersoonImage("foto", new PropertyModel<Persoon>(getContextMedewerkerModel(),
			"persoon")).add(new SimpleAttributeModifier("alt", "Foto van medewerker")));

		add(createFieldsetPersonalia());
		createFieldsetAccount();
		add(createFieldsetAanstelling());
		add(createFieldsetOrganisatieEenheid());
		add(createDatapanelRollen());

		createComponents();
	}

	private AutoFieldSet<Medewerker> createFieldsetPersonalia()
	{
		AutoFieldSet<Medewerker> fieldset =
			new AutoFieldSet<Medewerker>("fieldSetPersonalia", getContextMedewerkerModel(),
				"Personalia");
		fieldset.setRenderMode(RenderMode.DISPLAY);
		fieldset.setSortAccordingToPropertyNames(true);

		fieldset.setPropertyNames("afkorting", "persoon.achternaam", "persoon.voorvoegsel",
			"persoon.roepnaam", "persoon.geboortedatum", "persoon.geslacht",
			"persoon.fysiekAdres.adres.straatHuisnummer",
			"persoon.fysiekAdres.adres.postcodePlaats", "persoon.eersteEmailAdres.contactgegeven",
			"persoon.eersteTelefoon.contactgegeven");

		fieldset
			.addFieldModifier(new ConstructorArgModifier("persoon.geboortedatum",
				new PropertyModel<Date>(getContextMedewerkerModel(),
					"persoon.toepassingGeboortedatum")));

		fieldset.addFieldModifier(new LabelModifier("persoon.eersteEmailAdres.contactgegeven",
			"Emailadres"));
		fieldset.addFieldModifier(new LabelModifier("persoon.eersteTelefoon.contactgegeven",
			"Telefoonnummer"));

		return fieldset;
	}

	private void createFieldsetAccount()
	{
		Medewerker medewerker = getContextMedewerker();

		add(new WebMarkupContainer("panelEmptyAccount").setVisible(medewerker.getAccount() == null));
		add(createPanelAccount("panelAccount").setVisible(medewerker.getAccount() != null));
	}

	private Component createPanelAccount(String id)
	{
		AutoFieldSet<Account> accountFieldSet =
			new AutoFieldSet<Account>(id,
				ModelFactory.getModel(getContextMedewerker().getAccount()), "Account");
		accountFieldSet.setPropertyNames("gebruikersnaam", "actiefOmschrijving");
		accountFieldSet.setSortAccordingToPropertyNames(true);
		return accountFieldSet;
	}

	private EduArteDataPanel<Rol> createDatapanelRollen()
	{
		Account account = getContextMedewerker().getAccount();

		RolZoekFilter filter = new RolZoekFilter();
		filter.setAccount(account);
		IDataProvider<Rol> dataProvider =
			GeneralFilteredSortableDataProvider.of(filter, RolDataAccessHelper.class);
		EduArteDataPanel<Rol> rollen =
			new EduArteDataPanel<Rol>("dataPanelRollen", dataProvider, new RolTable());
		rollen.setButtonsVisible(false);
		rollen.setVisible(account != null);

		return rollen;
	}

	private AutoFieldSet<Medewerker> createFieldsetAanstelling()
	{
		AutoFieldSet<Medewerker> fieldset =
			new AutoFieldSet<Medewerker>("fieldSetAanstelling", getContextMedewerkerModel(),
				"Aanstelling");
		fieldset.setRenderMode(RenderMode.DISPLAY);
		fieldset.setSortAccordingToPropertyNames(true);

		fieldset.setPropertyNames("functie", "datumInDienst", "datumUitDienst");

		return fieldset;
	}

	private WebMarkupContainer createFieldsetOrganisatieEenheid()
	{
		final WebMarkupContainer datapanelContainer =
			new WebMarkupContainer("panelOrganisatieMedewerker");
		CustomDataPanel<OrganisatieMedewerker> datapanel =
			new EduArteDataPanel<OrganisatieMedewerker>("datapanelOrganisatieMedewerker",
				new ListModelDataProvider<OrganisatieMedewerker>(
					new PropertyModel<List<OrganisatieMedewerker>>(getDefaultModel(),
						"organisatieMedewerkers")),
				new OrganisatieLocatieTable<OrganisatieMedewerker>(false));

		datapanelContainer.setOutputMarkupId(true);
		datapanelContainer.add(datapanel);

		return datapanelContainer;
	}
}
