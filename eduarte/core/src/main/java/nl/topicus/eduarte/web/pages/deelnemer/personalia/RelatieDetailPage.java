/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.deelnemer.personalia;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemerRelaties;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.entities.vrijevelden.RelatieVrijVeld;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.VrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author idserda
 */
@PageInfo(title = "Personalia", menu = {"Deelnemer > [deelnemer]", "Groep > [groep] > [deelnemer]"})
@InPrincipal(DeelnemerRelaties.class)
public class RelatieDetailPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	IModel<Relatie> relatieModel;

	public RelatieDetailPage(Deelnemer deelnemer, Verbintenis verbintenis, Relatie relatie)
	{
		super(DeelnemerMenuItem.Relaties, deelnemer, verbintenis);

		relatieModel = ModelFactory.getModel(relatie);

		AutoFieldSet<Relatie> fieldsetpersonalia =
			new AutoFieldSet<Relatie>("personaliaFieldSet", relatieModel, "Personalia");
		fieldsetpersonalia.setPropertyNames("relatie.achternaam", "relatie.voorvoegsel",
			"relatie.voorletters", "relatie.roepnaam", "relatie.voornamen", "relatie.geslacht",
			"relatie.bsn", "relatie.geboorteland", "relatie.nationaliteit1",
			"relatie.debiteurennummer", "relatie.bankrekeningnummer");
		fieldsetpersonalia.setRenderMode(RenderMode.DISPLAY);
		fieldsetpersonalia.setSortAccordingToPropertyNames(true);
		add(fieldsetpersonalia);

		AutoFieldSet<Relatie> relatiedetails =
			new AutoFieldSet<Relatie>("relatieDetailsFieldSet", relatieModel,
				"Relatie tot de deelnemer");

		relatiedetails.setPropertyNames("relatieSoort", "wettelijkeVertegenwoordiger",
			"betalingsplichtige");
		relatiedetails.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return getContextDeelnemer().getPersoon().isMeerderjarig();
			}
		}, "wettelijkeVertegenwoordiger"));

		relatiedetails.setRenderMode(RenderMode.DISPLAY);
		relatiedetails.setSortAccordingToPropertyNames(true);
		add(relatiedetails);

		add(new AdressenPanel<PersoonAdres>("adresPanel", new PropertyModel<Persoon>(relatieModel,
			"relatie")));

		add(new ContactgegevenEntiteitPanel<PersoonContactgegeven>(
			"contactGegevensPanel",
			new PropertyModel<List<PersoonContactgegeven>>(relatieModel, "relatie.contactgegevens"),
			false));

		VrijVeldEntiteitPanel<RelatieVrijVeld, Relatie> vrijVeldenPanel =
			new VrijVeldEntiteitPanel<RelatieVrijVeld, Relatie>("vrijVelden", relatieModel);

		vrijVeldenPanel.setDossierScherm(true);
		add(vrijVeldenPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		ModuleEditPageButton<Relatie> bewerken =
			new ModuleEditPageButton<Relatie>(panel, "Bewerken", CobraKeyAction.BEWERKEN,
				Relatie.class, getSelectedMenuItem(), RelatieDetailPage.this, relatieModel);
		panel.addButton(bewerken);

		panel.addButton(new TerugButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new DeelnemerRelatiesOverzichtPage(getContextDeelnemer(),
					getContextVerbintenis());
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return RelatieDetailPage.class;
			}

		}));
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(Relatie.class);
		ctorArgValues.add(getRelatieModel());
	}

	private IModel<Relatie> getRelatieModel()
	{
		return relatieModel;
	}
}