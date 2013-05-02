package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.entities.IdObject;

public class LandelijkCodeNaamZoekFilter<T extends IdObject> extends AbstractZoekFilter<T>
		implements ICodeNaamZoekFilter<T>
{
	private static final long serialVersionUID = 1L;

	private String naam;

	private String code;

	private String entityClass;

	public LandelijkCodeNaamZoekFilter()
	{
	}

	public LandelijkCodeNaamZoekFilter(Class<T> entityClass)
	{
		this.entityClass = entityClass.getName();
	}

	@Override
	public String getNaam()
	{
		return naam;
	}

	@Override
	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Override
	public String getCode()
	{
		return code;
	}

	@Override
	public void setCode(String code)
	{
		this.code = code;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getEntityClass()
	{
		try
		{
			return (Class<T>) Class.forName(entityClass);
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void setEntityClass(Class<T> entityClass)
	{
		if (entityClass != null)
		{
			this.entityClass = entityClass.getName();
		}
		else
			this.entityClass = null;
	}

	public static <T extends IdObject> LandelijkCodeNaamZoekFilter<T> of(Class<T> clz)
	{
		return new LandelijkCodeNaamZoekFilter<T>(clz);
	}
}
