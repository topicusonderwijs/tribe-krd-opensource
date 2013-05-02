package nl.topicus.eduarte.web.pages.deelnemerportaal.home;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.settings.DeelnemerportaalWelkomsttekstSetting;
import nl.topicus.eduarte.web.components.factory.ParticipatieModuleComponentFactory;
import nl.topicus.eduarte.web.components.menu.deelnemerPortaal.DeelnemerportaalHomeMenuItem;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.IPageLink;

/**
 * @author ambrosius
 */
@PageInfo(title = "Home", menu = "")
@InPrincipal(Always.class)
@RechtenSoorten( {RechtenSoort.DEELNEMER})
public class DeelnemerportaalHomePage extends AbstractDeelnemerportaalHomePage
{
	private static final long serialVersionUID = 1L;

	public DeelnemerportaalHomePage()
	{
		this(getDefaultVerbintenis());
	}

	public DeelnemerportaalHomePage(Verbintenis verbintenis)
	{
		super(DeelnemerportaalHomeMenuItem.Home, verbintenis);

		WebMarkupContainer welkomstContainer = new WebMarkupContainer("welkomsttekstcontainer");
		String welkomsttekstString =
			DataAccessRegistry.getHelper(SettingsDataAccessHelper.class).getSetting(
				DeelnemerportaalWelkomsttekstSetting.class).getValue();
		welkomstContainer.add(new Label("welkomsttekst", welkomsttekstString));
		welkomstContainer.setVisible(welkomsttekstString != null && !welkomsttekstString.isEmpty());
		add(welkomstContainer);
		DeelnemerportaalOverzichtSignalenPanel signalenPanel =
			new DeelnemerportaalOverzichtSignalenPanel("signalen", getIngelogdeDeelnemer()
				.getPersoon(), 20);
		add(signalenPanel);

		add(EduArteApp.get().getFirstPanelFactory(ParticipatieModuleComponentFactory.class,
			EduArteRequestCycle.get().getOrganisatie()).newInloopCollegesHomePanel(
			"inloopCollegesPanel", verbintenis.getDeelnemer()));

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new PageLinkButton(panel, "Beheer signalen", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new DeelnemerportaalBeheerSignalenPage(getContextVerbintenis());
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return DeelnemerportaalBeheerSignalenPage.class;
			}
		}));
	}
}
