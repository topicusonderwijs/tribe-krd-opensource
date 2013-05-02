package nl.topicus.eduarte.krd.zoekfilters;

import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;

public class BereikbareDiplomasZoekFilter extends OpleidingZoekFilter
{
	private static final long serialVersionUID = 1L;

	@AutoForm(editorClass = JaNeeCombobox.class)
	private Boolean geslaagd;

	public Boolean getGeslaagd()
	{
		return geslaagd;
	}

	public void setGeslaagd(Boolean geslaagd)
	{
		this.geslaagd = geslaagd;
	}
}
