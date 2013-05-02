package nl.topicus.cobra.web.components.datapanel;

public class DefaultCustomDataPanelId implements CustomDataPanelId
{
	private static final long serialVersionUID = 1L;

	private Class< ? > baseClass;

	private String panelId;

	private boolean accountSpecific = true;

	public DefaultCustomDataPanelId(Class< ? > baseClass, String panelId)
	{
		this.baseClass = baseClass;
		this.panelId = panelId;
	}

	public DefaultCustomDataPanelId(Class< ? > baseClass, String panelId, boolean accountSpecific)
	{
		this(baseClass, panelId);
		this.accountSpecific = accountSpecific;
	}

	public Class< ? > getBaseClass()
	{
		return baseClass;
	}

	public String getPanelId()
	{
		return panelId;
	}

	@Override
	public boolean isAccountSpecific()
	{
		return accountSpecific;
	}

	@Override
	public int hashCode()
	{
		return baseClass.hashCode() ^ panelId.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof CustomDataPanelId)
		{
			CustomDataPanelId other = (CustomDataPanelId) obj;
			return other.generateId().equals(generateId());
		}
		return false;
	}

	@Override
	public String generateId()
	{
		return getBaseClass().getName() + ":" + getPanelId();
	}
}
