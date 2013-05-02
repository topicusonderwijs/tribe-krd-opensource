/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.home;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.app.signalering.EventAbonnementInstelling;
import nl.topicus.eduarte.app.signalering.EventAbonnementType;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.dao.helpers.EventAbonnementSettingDataAccessHelper;
import nl.topicus.eduarte.entities.signalering.settings.AbstractEventAbonnementConfiguration;
import nl.topicus.eduarte.entities.signalering.settings.GlobaalAbonnementSetting;
import nl.topicus.eduarte.entities.signalering.settings.PersoonlijkAbonnementSetting;
import nl.topicus.eduarte.web.components.menu.HomeMenuItem;
import nl.topicus.eduarte.web.components.panels.signalen.BeheerSignalenPanel;

import org.apache.wicket.model.IModel;

@PageInfo(title = "Signalen beheren", menu = {"Home > Instellingen > Signalen beheren"})
@InPrincipal(Always.class)
public class PersoonlijkBeheerSignalenPage extends AbstractHomePage<Void>
{
	private static final long serialVersionUID = 1L;

	private BeheerSignalenPanel<PersoonlijkAbonnementSetting> signalenpanel;

	private Map<String, IModel<GlobaalAbonnementSetting>> standaardWaarden =
		new HashMap<String, IModel<GlobaalAbonnementSetting>>();

	public PersoonlijkBeheerSignalenPage()
	{
		super(HomeMenuItem.BeheerSignalen);

		add(signalenpanel =
			new BeheerSignalenPanel<PersoonlijkAbonnementSetting>("signalen",
				PersoonlijkAbonnementSetting.class)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public List<EventAbonnementInstelling> getAbonnementInstellingen()
				{
					return Arrays.asList(EventAbonnementInstelling.Aan,
						EventAbonnementInstelling.Uit);
				}

				@Override
				public List<EventAbonnementType> getAbonnementTypes()
				{
					return Arrays.asList(EventAbonnementType.TaakGerelateerd,
						EventAbonnementType.Mentor, EventAbonnementType.Docent,
						EventAbonnementType.Uitvoerende, EventAbonnementType.Verantwoordelijke,
						EventAbonnementType.GeselecteerdeGroepen,
						EventAbonnementType.GeselecteerdeDeelnemers);
				}

				@Override
				protected PersoonlijkAbonnementSetting createSetting()
				{
					return new PersoonlijkAbonnementSetting(getIngelogdeMedewerker().getPersoon());
				}

				@Override
				protected void saveSettings()
				{
					super.saveSettings();
					setResponsePage(PersoonlijkBeheerSignalenPage.class);
				}

				@Override
				protected boolean isEnabled(String key)
				{
					GlobaalAbonnementSetting standaard = getStandaardWaarde(key);
					return standaard == null
						|| !EventAbonnementInstelling.Verplicht.equals(standaard.getWaarde());
				}

				@Override
				public EventAbonnementInstelling getWaarde(String key)
				{
					GlobaalAbonnementSetting standaard = getStandaardWaarde(key);
					EventAbonnementInstelling instelling = getInstellingen().get(key);
					if (standaard == null)
						return instelling == null ? EventAbonnementInstelling.Aan : instelling;

					switch (standaard.getWaarde())
					{
						case Aan:
							return instelling == null ? EventAbonnementInstelling.Aan : instelling;
						case Uit:
							return instelling == null ? EventAbonnementInstelling.Uit : instelling;
						case Verplicht:
							return EventAbonnementInstelling.Aan;
					}
					return EventAbonnementInstelling.Aan;
				}

				@Override
				protected AbstractEventAbonnementConfiguration< ? > getStandaardConfiguratie(
						String key)
				{
					GlobaalAbonnementSetting standaard = getStandaardWaarde(key);
					return standaard == null ? null : standaard.getConfiguratie();
				}
			});
		for (GlobaalAbonnementSetting curSetting : DataAccessRegistry.getHelper(
			EventAbonnementSettingDataAccessHelper.class).list(GlobaalAbonnementSetting.class))
		{
			standaardWaarden.put(signalenpanel.getKey(curSetting), ModelFactory
				.getModel(curSetting));
		}
		createComponents();
	}

	private GlobaalAbonnementSetting getStandaardWaarde(String key)
	{
		IModel<GlobaalAbonnementSetting> ret = standaardWaarden.get(key);
		return ret == null ? null : ret.getObject();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, signalenpanel.getForm()));
		panel.addButton(new AnnulerenButton(panel, PersoonlijkBeheerSignalenPage.class));
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(standaardWaarden);
	}
}
