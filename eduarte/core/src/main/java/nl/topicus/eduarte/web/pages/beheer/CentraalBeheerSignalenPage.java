/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.beheer;

import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.app.signalering.EventAbonnementInstelling;
import nl.topicus.eduarte.app.signalering.EventAbonnementType;
import nl.topicus.eduarte.core.principals.beheer.systeem.SignalenBeheren;
import nl.topicus.eduarte.entities.signalering.settings.GlobaalAbonnementSetting;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.signalen.BeheerSignalenPanel;

@PageInfo(title = "Signalen beheren", menu = {"Beheer > Systeem > Signalen"})
@InPrincipal(SignalenBeheren.class)
public class CentraalBeheerSignalenPage extends AbstractBeheerPage<Void>
{
	private static final long serialVersionUID = 1L;

	private BeheerSignalenPanel<GlobaalAbonnementSetting> signalenpanel;

	public CentraalBeheerSignalenPage()
	{
		super(BeheerMenuItem.Signalen);

		add(signalenpanel =
			new BeheerSignalenPanel<GlobaalAbonnementSetting>("signalen",
				GlobaalAbonnementSetting.class)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public List<EventAbonnementInstelling> getAbonnementInstellingen()
				{
					return Arrays.asList(EventAbonnementInstelling.values());
				}

				@Override
				public List<EventAbonnementType> getAbonnementTypes()
				{
					return Arrays.asList(EventAbonnementType.Mentor, EventAbonnementType.Docent,
						EventAbonnementType.Uitvoerende, EventAbonnementType.Verantwoordelijke,
						EventAbonnementType.TaakGerelateerd);
				}

				@Override
				protected GlobaalAbonnementSetting createSetting()
				{
					return new GlobaalAbonnementSetting();
				}

				@Override
				protected void saveSettings()
				{
					super.saveSettings();
					setResponsePage(CentraalBeheerSignalenPage.class);
				}
			});
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, signalenpanel.getForm()));
		panel.addButton(new AnnulerenButton(panel, CentraalBeheerSignalenPage.class));
	}
}
