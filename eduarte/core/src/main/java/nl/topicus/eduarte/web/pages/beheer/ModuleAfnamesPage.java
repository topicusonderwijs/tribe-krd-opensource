package nl.topicus.eduarte.web.pages.beheer;

import java.util.Comparator;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.eduarte.app.AbstractEduArteModule;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.core.principals.beheer.BeheerHome;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import edu.emory.mathcs.backport.java.util.Collections;

@PageInfo(title = "Modules afnames", menu = "Beheer")
@InPrincipal(BeheerHome.class)
@Actions(Instelling.class)
@Implies(BeheerHome.class)
@Display(parent = "Beheer", label = "Afgenomen modules", module = EduArteModuleKey.BASISFUNCTIONALITEIT)
@Description("Overzicht van afgenomen modules")
public class ModuleAfnamesPage extends AbstractBeheerPage<Void>
{
	public ModuleAfnamesPage()
	{
		super(BeheerMenuItem.ModuleAfnames);

		ListView<AbstractEduArteModule> modules =
			new ListView<AbstractEduArteModule>("moduleAfnames", new ModuleListModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<AbstractEduArteModule> item)
				{
					item.add(ComponentFactory.getDataLabel("name"));
					item.add(ComponentFactory.getDataLabel("version"));
				}

				@Override
				protected IModel<AbstractEduArteModule> getListItemModel(
						IModel< ? extends List<AbstractEduArteModule>> listViewModel, int index)
				{
					return new CompoundPropertyModel<AbstractEduArteModule>(super.getListItemModel(
						listViewModel, index));
				}
			};
		add(modules);

		createComponents();
	}

	private final class ModuleListModel extends
			LoadableDetachableModel<List<AbstractEduArteModule>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<AbstractEduArteModule> load()
		{
			List<AbstractEduArteModule> activeModules =
				EduArteApp.get().getActiveModules(AbstractEduArteModule.class,
					EduArteContext.get().getOrganisatie());
			Collections.sort(activeModules, new Comparator<AbstractEduArteModule>()
			{
				@Override
				public int compare(AbstractEduArteModule arg0, AbstractEduArteModule arg1)
				{
					return arg0.getName().compareTo(arg1.getName());
				}
			});
			return activeModules;
		}
	}

}
