package nl.topicus.eduarte.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.core.principals.beheer.BeheerHomeWrite;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;

@PageInfo(title = "Beheer", menu = "Beheer")
@InPrincipal(BeheerHomeWrite.class)
@RechtenSoorten( {RechtenSoort.INSTELLING, RechtenSoort.BEHEER})
public class BeheerHomeEditPage extends AbstractBeheerPage<Void>
{

	private final Form<Organisatie> form;

	public BeheerHomeEditPage()
	{
		super(BeheerMenuItem.Beheer);
		Organisatie organisatie =
			(Organisatie) EduArteRequestCycle.get().getOrganisatie().doUnproxy();
		form = new Form<Organisatie>("form", ModelFactory.getCompoundModel(organisatie))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				Organisatie org = getModelObject();
				org.saveOrUpdate();
				org.commit();
				setResponsePage(BeheerHomePage.class);
			}
		};
		add(form);
		form.setModel(ModelFactory.getCompoundModel(organisatie));
		form.add(ComponentFactory.getDataLabel("naam"));
		form.add(new TextField<String>("wikiUser").setVisible(organisatie instanceof Instelling));
		form.add(new TextField<String>("wikiPassword")
			.setVisible(organisatie instanceof Instelling));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form));
		panel.addButton(new AnnulerenButton(panel, BeheerHomePage.class));
	}

}
