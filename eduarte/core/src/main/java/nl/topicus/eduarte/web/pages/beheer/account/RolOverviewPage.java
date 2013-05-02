package nl.topicus.eduarte.web.pages.beheer.account;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.core.principals.beheer.account.RightsRead;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.rechten.RechtenPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;

@PageInfo(title = "Rol", menu = {"Beheer > Accountbeheer > Rollen en rechten",
	"Beheer > Accountbeheer > Rollen en rechten > [Rol]"})
@InPrincipal(RightsRead.class)
public class RolOverviewPage extends AbstractBeheerPage<Rol>
{

	public RolOverviewPage(Rol rol)
	{
		super(BeheerMenuItem.RollenEnRechten);
		setDefaultModel(ModelFactory.getModel(rol));

		AutoFieldSet<Rol> fields = new AutoFieldSet<Rol>("fields", getContextModel(), "Rol");
		fields.setRenderMode(RenderMode.DISPLAY);
		add(fields);
		add(new RechtenPanel("rechten", getContextModel(), true));

		createComponents();
	}

	public Rol getRol()
	{
		return (Rol) getDefaultModelObject();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new BewerkenButton<Void>(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new RolEditPage(getRol());
			}

			@Override
			public Class<RolEditPage> getPageIdentity()
			{
				return RolEditPage.class;
			}
		})
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				AuthorisatieNiveau authNiveau =
					EduArteContext.get().getAccount().getAuthorisatieNiveau();
				return super.isVisible() && authNiveau.implies(getRol().getAuthorisatieNiveau());
			}
		});
		panel.addButton(new TerugButton(panel, RollenEnRechtenPage.class));
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(Rol.class);
		ctorArgValues.add(getDefaultModel());
	}
}
