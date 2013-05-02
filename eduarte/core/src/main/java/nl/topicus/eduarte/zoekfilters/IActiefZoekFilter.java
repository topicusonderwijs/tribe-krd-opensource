package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.choice.ActiefCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;

public interface IActiefZoekFilter<T> extends DetachableZoekFilter<T>
{
	@AutoForm(editorClass = ActiefCombobox.class)
	public Boolean getActief();

	public void setActief(Boolean actief);
}
