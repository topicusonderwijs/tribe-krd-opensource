package nl.topicus.eduarte.web.pages.beheer.onderwijs;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.core.principals.beheer.onderwijs.Teams;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.opleiding.Team;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Team", menu = {"Beheer > Onderwijs > Teams > Nieuw team",
	"Beheer > Onderwijs > Teams > [team]"})
@InPrincipal(Teams.class)
public class TeamEditPage extends AbstractBeheerPage<Team> implements IEditPage
{
	private Form<Void> form;

	public TeamEditPage(Team team, SecurePage returnPage)
	{
		super(ModelFactory.getModel(team, new DefaultModelManager(Team.class)),
			BeheerMenuItem.Teams);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<Team> fieldset = new AutoFieldSet<Team>("team", getContextModel(), "Team");
		fieldset.setPropertyNames("code", "naam", "actief");
		fieldset.setRenderMode(RenderMode.EDIT);
		fieldset.setSortAccordingToPropertyNames(true);
		fieldset.addModifier("code", new UniqueConstraintValidator<String>(fieldset, "Team",
			"code", "organisatie"));
		form.add(fieldset);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				Team team = (Team) TeamEditPage.this.getDefaultModelObject();
				team.saveOrUpdate();
				team.commit();
				setResponsePage(TeamZoekenPage.class);
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return TeamEditPage.this.getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return TeamEditPage.this.getReturnPageClass();
			}

		}));
		panel.addButton(new VerwijderButton(panel, "Verwijder")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				Team team = (Team) TeamEditPage.this.getDefaultModelObject();
				team.delete();
				team.commit();
				setResponsePage(TeamZoekenPage.class);
			}

			@Override
			public boolean isVisible()
			{
				Team team = getContextModelObject();
				if (team.isSaved())
				{
					CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper< ? , ? > helper =
						DataAccessRegistry
							.getHelper(CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class);
					return !helper.isTeamInGebruik(team);
				}
				return false;
			}
		});
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
