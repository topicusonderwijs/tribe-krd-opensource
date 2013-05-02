/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.medewerker;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.medewerker.MedewerkerPersonaliaRead;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.entities.vrijevelden.PersoonVrijVeld;
import nl.topicus.eduarte.providers.MedewerkerProvider;
import nl.topicus.eduarte.web.components.menu.MedewerkerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.VrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;

import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author idserda
 */
@PageInfo(title = "Personalia", menu = {"Medewerker > [Medewerker]", "Groep > [groep]"})
@InPrincipal(MedewerkerPersonaliaRead.class)
public class MedewerkerPersonaliaPage extends AbstractMedewerkerPage
{
	private static final long serialVersionUID = 1L;

	public MedewerkerPersonaliaPage(MedewerkerProvider provider)
	{
		this(provider.getMedewerker());
	}

	public MedewerkerPersonaliaPage(Medewerker medewerker)
	{
		super(MedewerkerMenuItem.Personalia, medewerker);

		AutoFieldSet<Medewerker> fieldsetpersonalia =
			new AutoFieldSet<Medewerker>("inputFieldsPersonalia", getContextMedewerkerModel(),
				"Personalia");
		fieldsetpersonalia.setPropertyNames("persoon.bsn", "afkorting", "persoon.achternaam",
			"persoon.voorvoegsel", "persoon.voornamen", "persoon.voorletters", "persoon.roepnaam",
			"persoon.geslacht", "persoon.geboortedatum");
		fieldsetpersonalia.addFieldModifier(new ConstructorArgModifier("persoon.geboortedatum",
			new PropertyModel<Date>(getDefaultModel(), "persoon.toepassingGeboortedatum")));
		fieldsetpersonalia.setRenderMode(RenderMode.DISPLAY);
		fieldsetpersonalia.setSortAccordingToPropertyNames(true);
		add(fieldsetpersonalia);

		add(new AdressenPanel<PersoonAdres>("adresPanel", new LoadableDetachableModel<Persoon>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Persoon load()
			{
				return getContextMedewerker().getPersoon();
			}
		}));

		add(new ContactgegevenEntiteitPanel<PersoonContactgegeven>("contactGegevensPanel",
			new LoadableDetachableModel<List<PersoonContactgegeven>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected List<PersoonContactgegeven> load()
				{
					return getContextMedewerker().getPersoon().getContactgegevens();
				}
			}, false));

		VrijVeldEntiteitPanel<PersoonVrijVeld, Persoon> vrijVeldenPanel =
			new VrijVeldEntiteitPanel<PersoonVrijVeld, Persoon>("vrijveldenPanel",
				new PropertyModel<Persoon>(getContextMedewerkerModel(), "persoon"));
		vrijVeldenPanel.setDossierScherm(true);
		add(vrijVeldenPanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		ModuleEditPageButton<Medewerker> bewerken =
			new ModuleEditPageButton<Medewerker>(panel, "Bewerken", CobraKeyAction.BEWERKEN,
				Medewerker.class, getSelectedMenuItem(), MedewerkerPersonaliaPage.this,
				getContextMedewerkerModel());
		panel.addButton(bewerken);

	}

}
