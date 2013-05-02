package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.choice.ActiefCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.codenaamactief.ICodeNaamActiefEntiteit;

/**
 * Zoekfilter voor entiteiten met een code en naam en actief kolom
 * 
 * @author vandekamp
 * @param <T>
 */
public abstract class AbstractCodeNaamActiefZoekFilter<T extends ICodeNaamActiefEntiteit> extends
		AbstractCodeNaamZoekFilter<T> implements ICodeNaamActiefZoekFilter<T>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(label = "status", editorClass = ActiefCombobox.class)
	private Boolean actief;

	public AbstractCodeNaamActiefZoekFilter(Class<T> entityClass)
	{
		super(entityClass);
	}

	public Boolean getActief()
	{
		return actief;
	}

	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}
}
