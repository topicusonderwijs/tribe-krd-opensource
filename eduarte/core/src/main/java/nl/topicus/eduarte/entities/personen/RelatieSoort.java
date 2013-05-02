package nl.topicus.eduarte.entities.personen;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * 
 * @author vanharen
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
@IsViewWhenOnNoise
public class RelatieSoort extends CodeNaamActiefInstellingEntiteit
{

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	@AutoForm(description = "Selecteer dit wanneer  de Relatiesoort gebruikt kan worden bij een persoon")
	private boolean persoonOpname;

	@Column(nullable = false)
	@AutoForm(description = "Selecteer dit wanneer de Relatiesoort gebruikt kan worden bij een organisatie")
	private boolean organisatieOpname;

	public RelatieSoort()
	{
	}

	public boolean isPersoonOpname()
	{
		return persoonOpname;
	}

	public void setPersoonOpname(boolean persoonOpname)
	{
		this.persoonOpname = persoonOpname;
	}

	public boolean isOrganisatieOpname()
	{
		return organisatieOpname;
	}

	public void setOrganisatieOpname(boolean organisatieOpname)
	{
		this.organisatieOpname = organisatieOpname;
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

}
