package nl.topicus.eduarte.web.components.choice;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.providers.LocatieProvider;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Panel met een secure org.ehd. combobox en een secure locatie combobox. De twee zijn aan
 * elkaar gelinked, en de gebruiker ziet alleen de eigen locaties / organisatie-eenheden.
 * 
 * @author loite
 */
public abstract class SecureOrganisatieEenheidLocatieChoicePanel<T extends OrganisatieEenheidLocatieProvider>
		extends TypedPanel<T>
{
	OrganisatieEenheidCombobox orgEhdCombo;

	LocatieCombobox locatieCombo;

	/**
	 * Geeft aan welke van de componenten required is, gezien de bijbehorende panel nogal
	 * vaak wordt gebruikt is dit het handigste.
	 * 
	 * @author hoeve
	 */
	public enum OrganisatieEenheidLocatieRequired
	{
		OrganisatieEenheid,
		Locatie,
		Beide,
		Geen
	}

	private static final long serialVersionUID = 1L;

	public SecureOrganisatieEenheidLocatieChoicePanel(String id, IModel<T> model,
			OrganisatieEenheidLocatieRequired requiredMode)
	{
		this(id, model, requiredMode, null);
	}

	public SecureOrganisatieEenheidLocatieChoicePanel(String id, IModel<T> model,
			OrganisatieEenheidLocatieRequired requiredMode, IModel<Opleiding> opleidingModel)
	{
		super(id, model);
		setRenderBodyOnly(true);
		LocatieProvider locatieProvider = new LocatieProvider()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Locatie getLocatie()
			{
				return (Locatie) SecureOrganisatieEenheidLocatieChoicePanel.this.get("locatie")
					.getDefaultModelObject();
			}
		};
		orgEhdCombo =
			new OrganisatieEenheidCombobox("organisatieEenheid", null, locatieProvider,
				opleidingModel)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target, OrganisatieEenheid newSelection)
				{
					super.onUpdate(target, newSelection);
					onUpdateOrganisatieEenheid(target, newSelection);
				}
			};
		orgEhdCombo.setNullValid(true);
		orgEhdCombo.setRequired(requiredMode.equals(OrganisatieEenheidLocatieRequired.Beide)
			|| requiredMode.equals(OrganisatieEenheidLocatieRequired.OrganisatieEenheid));
		orgEhdCombo.setLabel(new Model<String>("Organisatie-eenheid"));
		orgEhdCombo.setAddSelectedItemToChoicesWhenNotInList(false);
		add(orgEhdCombo);
		locatieCombo = new LocatieCombobox("locatie", null, orgEhdCombo, opleidingModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target, Locatie newSelection)
			{
				super.onUpdate(target, newSelection);
				onUpdateLocatie(target, newSelection);
			}
		};
		locatieCombo.setNullValid(true);
		locatieCombo.setRequired(requiredMode.equals(OrganisatieEenheidLocatieRequired.Beide)
			|| requiredMode.equals(OrganisatieEenheidLocatieRequired.Locatie));
		locatieCombo.setAddSelectedItemToChoicesWhenNotInList(false);
		add(locatieCombo);
		orgEhdCombo.connectListForAjaxRefresh(locatieCombo);
		locatieCombo.connectListForAjaxRefresh(orgEhdCombo);
	}

	public void update(AjaxRequestTarget target)
	{
		target.addComponent(orgEhdCombo);
		target.addComponent(locatieCombo);
	}

	@SuppressWarnings("unused")
	public void onUpdateOrganisatieEenheid(AjaxRequestTarget target, OrganisatieEenheid newSelection)
	{
	}

	@SuppressWarnings("unused")
	public void onUpdateLocatie(AjaxRequestTarget target, Locatie newSelection)
	{
	}

	public OrganisatieEenheidCombobox getOrganisatieEenheidCombo()
	{
		return orgEhdCombo;
	}

	public LocatieCombobox getLocatieCombo()
	{
		return locatieCombo;
	}
}
