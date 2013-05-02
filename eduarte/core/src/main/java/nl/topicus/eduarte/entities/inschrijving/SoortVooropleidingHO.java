package nl.topicus.eduarte.entities.inschrijving;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.LandelijkEntiteit;
import nl.topicus.eduarte.entities.codenaamactief.ICodeNaamActiefEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author hop
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code"})})
public class SoortVooropleidingHO extends LandelijkEntiteit implements ICodeNaamActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 10, nullable = false)
	private String code;

	@Column(length = 100, nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@Column(nullable = false)
	private boolean actief = true;

	@Override
	public String toString()
	{
		return getCode() + " " + getNaam();
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

	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

}
