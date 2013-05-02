package nl.topicus.eduarte.web.pages.groep;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.core.principals.groep.GroepenMijn;
import nl.topicus.eduarte.entities.groep.GroepDocent;
import nl.topicus.eduarte.entities.groep.GroepMentor;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;

import org.apache.wicket.Page;

@PageInfo(title = "Mijn groepen", menu = "Groep")
@InPrincipal(GroepenMijn.class)
public class MijnGroepenPage extends GroepZoekenPage
{
	public MijnGroepenPage()
	{
		this(createFilter());
	}

	private static GroepZoekFilter createFilter()
	{
		GroepZoekFilter filter = GroepZoekenPage.getDefaultFilter();
		filter.setMentorOrDocent(EduArteContext.get().getMedewerker());
		filter.setMentorOrDocentRequired(true);
		return filter;
	}

	public MijnGroepenPage(GroepZoekFilter filter)
	{
		super(filter);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new ModuleEditPageButton<GroepDocent>(panel, "Als docent (ont)koppelen",
			CobraKeyAction.GEEN, GroepDocent.class, this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Page createTargetPage(
					Class< ? extends IModuleEditPage<GroepDocent>> pageClass, SecurePage returnPage)
			{
				return (Page) ReflectionUtil.invokeConstructor(pageClass);
			}
		});
		panel.addButton(new ModuleEditPageButton<GroepMentor>(panel, "Als mentor (ont)koppelen",
			CobraKeyAction.GEEN, GroepMentor.class, this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Page createTargetPage(
					Class< ? extends IModuleEditPage<GroepMentor>> pageClass, SecurePage returnPage)
			{
				return (Page) ReflectionUtil.invokeConstructor(pageClass);
			}
		});
	}
}
