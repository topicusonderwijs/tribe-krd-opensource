package nl.topicus.eduarte.entities.settings;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@DiscriminatorValue(value = "LandelijkeExtOrganisaties")
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class GebruikLandelijkeExterneOrganisatiesSetting extends OrganisatieSetting<Boolean>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(label = "Waarde", editorClass = JaNeeCombobox.class)
	@Column(name = "booleanValue", nullable = true)
	private Boolean value;

	@Override
	public String getOmschrijving()
	{
		return "Maak gebruik van landelijk gedefinieerde externe organisaties";
	}

	@Override
	public Boolean getValue()
	{
		return value;
	}

	@Override
	public void setValue(Boolean value)
	{
		this.value = value;
	}

}
