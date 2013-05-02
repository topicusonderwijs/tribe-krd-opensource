package nl.topicus.eduarte.web.pages.deelnemerportaal.home;

import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.signalering.EventAbonnementType;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.signalering.settings.PersoonlijkAbonnementSetting;
import nl.topicus.eduarte.web.components.menu.deelnemerPortaal.DeelnemerportaalHomeMenuItem;
import nl.topicus.eduarte.web.components.panels.signalen.BeheerSignalenPanel;
import nl.topicus.eduarte.web.components.panels.signalen.PersoonlijkBeheerSignalenPanel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;

/**
 * Pagina om per medewerker de settings voor het versturen van signalen te beheren.
 * 
 * @author marrink
 */
@PageInfo(title = "Eigen signaal profielen beheren", menu = {"Home > Signalen > Beheer signalen"})
@InPrincipal(Always.class)
public class DeelnemerportaalBeheerSignalenPage extends AbstractDeelnemerportaalHomePage
{
	private static final long serialVersionUID = 1L;

	private BeheerSignalenPanel<PersoonlijkAbonnementSetting> signalenpanel;

	public DeelnemerportaalBeheerSignalenPage()
	{
		this(getDefaultVerbintenis());
	}

	public DeelnemerportaalBeheerSignalenPage(Verbintenis verbintenis)
	{
		super(DeelnemerportaalHomeMenuItem.Home, verbintenis);

		add(signalenpanel =
			new PersoonlijkBeheerSignalenPanel("taakSignalen", PersoonlijkAbonnementSetting.class)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public List<EventAbonnementType> getAbonnementTypes()
				{
					return Arrays.asList(EventAbonnementType.TaakGerelateerd);
				}

				@Override
				protected void saveSettings()
				{
					super.saveSettings();
				}
			});

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, signalenpanel.getForm()));
		panel.addButton(new TerugButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			public Page getPage()
			{
				return new DeelnemerportaalHomePage(getContextVerbintenis());
			}

			public Class< ? extends Page> getPageIdentity()
			{
				return DeelnemerportaalHomePage.class;
			}
		}));
	}
}
