package nl.topicus.eduarte.app;

import java.io.Serializable;

import nl.topicus.cobra.web.components.menu.MenuItemKey;

/**
 * @author Arjan
 * @param <T>
 */
public class EduArteModuleEditPageKey<T> implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Class<T> entity;

	private MenuItemKey menuItemKey;

	/**
	 * @param entity
	 */
	public EduArteModuleEditPageKey(Class<T> entity)
	{
		this.entity = entity;
	}

	/**
	 * @param entity
	 * @param menuItemKey
	 */
	public EduArteModuleEditPageKey(Class<T> entity, MenuItemKey menuItemKey)
	{
		this.entity = entity;
		this.menuItemKey = menuItemKey;
	}

	/**
	 * @return Returns the entity.
	 */
	public Class<T> getEntity()
	{
		return entity;
	}

	/**
	 * @param entity
	 *            The entity to set.
	 */
	public void setEntity(Class<T> entity)
	{
		this.entity = entity;
	}

	/**
	 * @return Returns the menuItem.
	 */
	public MenuItemKey getMenuItemKey()
	{
		return menuItemKey;
	}

	public void setMenuItemKey(MenuItemKey menuItemKey)
	{
		this.menuItemKey = menuItemKey;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object arg0)
	{
		if (arg0 instanceof EduArteModuleEditPageKey)
		{
			EduArteModuleEditPageKey<T> key = (EduArteModuleEditPageKey<T>) arg0;
			if (key.getEntity().equals(entity))
			{
				if (key.getMenuItemKey() != null && getMenuItemKey() != null
					&& key.getMenuItemKey().equals(getMenuItemKey()))
					return true;
				else if (key.getMenuItemKey() == null && getMenuItemKey() == null)
					return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		if (getMenuItemKey() != null)
			return getEntity().hashCode() ^ getMenuItemKey().hashCode();
		return getEntity().hashCode();
	}
}
