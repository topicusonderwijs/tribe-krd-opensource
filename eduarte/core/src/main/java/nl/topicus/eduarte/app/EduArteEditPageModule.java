package nl.topicus.eduarte.app;

import nl.topicus.cobra.modules.CobraModule;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.web.pages.IModuleEditPage;

/**
 * @author Arjan
 */
public interface EduArteEditPageModule extends CobraModule
{
	public <T> Class< ? extends IModuleEditPage<T>> getModuleEditPage(Class<T> entityClass,
			MenuItemKey menuItemKey);
}
