package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.choice.ActiefCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

/**
 * 
 * 
 * @author vanharen
 */
public class AbstractNaamActiefZoekFilter<T extends InstellingEntiteit> extends
		AbstractZoekFilter<T> implements INaamActiefZoekFilter<T>
{

	private static final long serialVersionUID = 1L;

	@AutoForm(htmlClasses = "unit_160")
	private String naam;

	@AutoForm(editorClass = ActiefCombobox.class)
	private Boolean actief;

	private Class<T> entityClass;

	public AbstractNaamActiefZoekFilter(Class<T> entityClass)
	{
		this.entityClass = entityClass;
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
	public Boolean getActief()
	{
		return actief;
	}

	@Override
	public void setActief(Boolean actief)
	{
		this.actief = actief;

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
