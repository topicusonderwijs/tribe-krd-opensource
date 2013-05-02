package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.ICodeNaamEntiteit;

public class AbstractCodeNaamZoekFilter<T extends ICodeNaamEntiteit> extends AbstractZoekFilter<T>
		implements ICodeNaamZoekFilter<T>
{
	private static final long serialVersionUID = 1L;

	private String code;

	private String naam;

	private Class<T> entityClass;

	public AbstractCodeNaamZoekFilter(Class<T> entityClass)
	{
		this.entityClass = entityClass;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public Class<T> getEntityClass()
	{
		return entityClass;
	}

	public void setEntityClass(Class<T> entityClass)
	{
		this.entityClass = entityClass;
	}
}
