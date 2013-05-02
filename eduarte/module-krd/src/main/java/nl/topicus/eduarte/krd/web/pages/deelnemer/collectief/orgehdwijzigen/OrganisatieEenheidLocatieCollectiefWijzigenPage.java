package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.orgehdwijzigen;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenWrite;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;
import nl.topicus.eduarte.web.components.choice.SecureOrganisatieEenheidLocatieFormChoicePanel;
import nl.topicus.eduarte.web.components.choice.SecureOrganisatieEenheidLocatieChoicePanel.OrganisatieEenheidLocatieRequired;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;

/**
 * @author idserda
 */
@PageInfo(title = "Organisatie-eenheid en locatie collectief wijzigen", menu = {"Deelnemer > Collectief > Organisatie-eenheid bewerken"})
@InPrincipal(DeelnemerVerbintenissenWrite.class)
public class OrganisatieEenheidLocatieCollectiefWijzigenPage extends SecurePage
{
	private Form<Void> form;

	public OrganisatieEenheidLocatieCollectiefWijzigenPage()
	{
		this(new OrganisatieEenheidLocatieCollectiefWijzigenModel());
	}

	public OrganisatieEenheidLocatieCollectiefWijzigenPage(
			OrganisatieEenheidLocatieCollectiefWijzigenModel orgEhdLocatieWijzigenModel)
	{
		super(orgEhdLocatieWijzigenModel, CoreMainMenuItem.Deelnemer);

		form = new Form<Void>("form");

		SecureOrganisatieEenheidLocatieFormChoicePanel<OrganisatieEenheidLocatieProvider> organisatieEenheidLocatieChoice =
			new SecureOrganisatieEenheidLocatieFormChoicePanel<OrganisatieEenheidLocatieProvider>(
				"organisatieEenheidLocatie", orgEhdLocatieWijzigenModel,
				OrganisatieEenheidLocatieRequired.Beide);

		organisatieEenheidLocatieChoice.getOrganisatieEenheidCombo().setModel(
			new PropertyModel<OrganisatieEenheid>(getDefaultModel(), "organisatieEenheid"));
		organisatieEenheidLocatieChoice.getLocatieCombo().setModel(
			new PropertyModel<Locatie>(getDefaultModel(), "locatie"));

		form.add(organisatieEenheidLocatieChoice);

		add(form);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				setResponsePage(new OrganisatieEenheidLocatieCollectiefWijzigenSelectiePage(
					(OrganisatieEenheidLocatieCollectiefWijzigenModel) OrganisatieEenheidLocatieCollectiefWijzigenPage.this
						.getDefaultModel(), OrganisatieEenheidLocatieCollectiefWijzigenPage.this));
			}
		}.setLabel("Volgende"));
		panel.addButton(new TerugButton(panel,
			OrganisatieEenheidLocatieCollectiefWijzigenOverzichtPage.class));
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.OrgEhdLocatieWijzigen);
	}
}
