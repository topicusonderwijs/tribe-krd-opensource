/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.deelnemer.personalia;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AbstractFieldContainer;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemerPersonalia;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.entities.vrijevelden.PersoonVrijVeld;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.VrijVeldEntiteitPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.PageParameters;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author idserda
 */
@PageInfo(title = "Personalia", menu = {"Deelnemer > [deelnemer]", "Groep > [groep] > [deelnemer]"})
@InPrincipal(DeelnemerPersonalia.class)
public class DeelnemerPersonaliaPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	public DeelnemerPersonaliaPage(PageParameters parameters)
	{
		this(getDeelnemerFromPageParameters(DeelnemerPersonaliaPage.class, parameters));
	}

	public DeelnemerPersonaliaPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer(), provider.getDeelnemer()
			.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerPersonaliaPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerPersonaliaPage(Verbintenis inschrijving)
	{
		this(inschrijving.getDeelnemer(), inschrijving);
	}

	public DeelnemerPersonaliaPage(Deelnemer deelnemer, Verbintenis inschrijving)
	{
		super(DeelnemerMenuItem.Personalia, deelnemer, inschrijving);

		AutoFieldSet<Deelnemer> fieldsetpersonalia =
			new PersonaliaFieldset("inputFieldsPersonalia", deelnemerModel, "Personalia");

		fieldsetpersonalia.setPropertyNames("persoon.bsn", "persoon.achternaam",
			"persoon.voorvoegsel", "persoon.officieleAchternaam", "persoon.officieleVoorvoegsel",
			"persoon.voornamen", "persoon.voorletters", "persoon.roepnaam", "persoon.geslacht",
			"persoon.geboortedatum", "persoon.toepassingGeboortedatum", "persoon.geboorteland",
			"persoon.geboorteplaats", "persoon.burgerlijkeStaat", "onderwijsnummer",
			"persoon.datumInNederland", "persoon.cumiCategorie", "persoon.cumiRatio",
			"persoon.nationaliteit1", "persoon.nationaliteit2", "allochtoon", "persoon.nieuwkomer",
			"startkwalificatieplichtigTot", "lgf", "persoon.debiteurennummer",
			"persoon.persoonBetalingsplichtige", "persoon.bankrekeningnummer",
			"persoon.datumOverlijden", "persoon.wachtwoord", "persoon.nietVerstrekkenAanDerden",
			"bronStatus", "bronDatum");

		fieldsetpersonalia.addFieldModifier(new VisibilityModifier(
			new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					return getContextDeelnemer().getPersoon().getCumiCategorie() != null
						|| getContextDeelnemer().getPersoon().getCumiRatio() != null;
				}
			}, "persoon.cumiCategorie", "persoon.cumiRatio"));

		fieldsetpersonalia.addFieldModifier(new VisibilityModifier(new PropertyModel<Boolean>(
			getContextDeelnemerModel(), "persoon.geboortenaamWijktAf"),
			"persoon.officieleAchternaam", "persoon.officieleVoorvoegsel"));

		fieldsetpersonalia.addFieldModifier(new VisibilityModifier(createDatumVanOverlijdenModel(),
			"persoon.datumOverlijden"));

		fieldsetpersonalia.addFieldModifier(new VisibilityModifier(
			new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					return getContextDeelnemer().getPersoon().getToepassingGeboortedatum() != null;
				}
			}, "persoon.toepassingGeboortedatum"));

		fieldsetpersonalia
			.addFieldModifier(new ConstructorArgModifier("persoon.geboortedatum",
				new PropertyModel<Date>(getContextDeelnemerModel(),
					"persoon.toepassingGeboortedatum")));

		fieldsetpersonalia.setRenderMode(RenderMode.DISPLAY);
		fieldsetpersonalia.setSortAccordingToPropertyNames(true);
		add(fieldsetpersonalia);

		add(new AdressenPanel<PersoonAdres>("adresPanel", new PropertyModel<Persoon>(
			getContextDeelnemerModel(), "persoon")));

		add(new ContactgegevenEntiteitPanel<PersoonContactgegeven>("contactGegevensPanel",
			new PropertyModel<List<PersoonContactgegeven>>(getContextDeelnemerModel(),
				"persoon.contactgegevens"), false));

		VrijVeldEntiteitPanel<PersoonVrijVeld, Persoon> vrijVeldenPanel =
			new VrijVeldEntiteitPanel<PersoonVrijVeld, Persoon>("vrijveldenPanel",
				new PropertyModel<Persoon>(getContextDeelnemerModel(), "persoon"));
		vrijVeldenPanel.setDossierScherm(true);
		add(vrijVeldenPanel);

		createComponents();
	}

	public IModel<Boolean> createDatumVanOverlijdenModel()
	{
		return new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return getContextDeelnemer().getPersoon().getDatumOverlijden() != null;
			}
		};
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		ModuleEditPageButton<Deelnemer> bewerken =
			new ModuleEditPageButton<Deelnemer>(panel, "Bewerken", CobraKeyAction.BEWERKEN,
				Deelnemer.class, getSelectedMenuItem(), DeelnemerPersonaliaPage.this,
				deelnemerModel);
		panel.addButton(bewerken);
	}

	private final class PersonaliaFieldset extends AutoFieldSet<Deelnemer>
	{
		private static final long serialVersionUID = 1L;

		private PersonaliaFieldset(String id, IModel<Deelnemer> model, String caption)
		{
			super(id, model, caption);
		}

		@Override
		protected <Y, Z> AbstractFieldContainer<Deelnemer, Y, Z> createContainer(String id,
				FieldProperties<Deelnemer, Y, Z> properties)
		{
			if (properties.getPropertyName().equals("persoon.persoonBetalingsplichtige"))
			{
				if (getContextDeelnemer().heeftBetalingsplichtigBPVBedrijf())
					return new LabelWarningDisplayContainer<Deelnemer, Y, Z>(
						id,
						properties,
						"Er zijn een of meer BPV-bedrijven die voor deze deelnemer de betalingsplicht overnemen voor een verbintenis");
			}
			return super.createContainer(id, properties);
		}
	}
}
