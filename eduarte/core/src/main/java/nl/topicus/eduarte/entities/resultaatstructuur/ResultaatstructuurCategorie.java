package nl.topicus.eduarte.entities.resultaatstructuur;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ResultaatstructuurCategorie extends InstellingEntiteit implements IActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 100)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@Column(nullable = false)
	private boolean actief = true;

	public ResultaatstructuurCategorie()
	{
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Override
	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	@Override
	public String toString()
	{
		return getNaam();
	}
}
