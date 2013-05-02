package nl.topicus.eduarte.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.core.principals.beheer.BeheerHome;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;

@PageInfo(title = "Beheer", menu = "Beheer")
@InPrincipal(BeheerHome.class)
@RechtenSoorten( {RechtenSoort.INSTELLING, RechtenSoort.BEHEER})
public class BeheerHomePage extends AbstractBeheerPage<Void>
{

	public BeheerHomePage()
	{
		super(BeheerMenuItem.Beheer);
		Organisatie organisatie =
			(Organisatie) EduArteRequestCycle.get().getOrganisatie().doUnproxy();
		setDefaultModel(ModelFactory.getCompoundModel(organisatie));
		add(ComponentFactory.getDataLabel("naam"));
		add(ComponentFactory.getDataLabel("wikiUser").setVisible(organisatie instanceof Instelling));
		add(ComponentFactory.getDataLabel("wikiPassword", StringUtil.repeatString("*", 8))
			.setVisible(organisatie instanceof Instelling));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new BewerkenButton<Void>(panel, BeheerHomeEditPage.class));
	}

}
