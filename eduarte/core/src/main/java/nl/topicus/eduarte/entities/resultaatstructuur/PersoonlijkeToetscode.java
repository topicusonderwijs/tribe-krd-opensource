package nl.topicus.eduarte.entities.resultaatstructuur;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"medewerker",
	"toets"})})
public class PersoonlijkeToetscode extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = false)
	@Index(name = "idx_persToetscode_medewerker")
	private Medewerker medewerker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "toets", nullable = false)
	@Index(name = "idx_persToetscode_toets")
	private Toets toets;

	@Column(nullable = false, length = 10)
	private String code;

	public PersoonlijkeToetscode()
	{
	}

	public PersoonlijkeToetscode(Medewerker medewerker, Toets toets)
	{
		setMedewerker(medewerker);
		setToets(toets);
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	public Toets getToets()
	{
		return toets;
	}

	public void setToets(Toets toets)
	{
		this.toets = toets;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}
}
