package nl.topicus.eduarte.entities.vrijevelden;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(name = "VrijVeldKeuzeOptie")
public class VrijVeldKeuzeOptie extends InstellingEntiteit implements IActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@Column(nullable = false)
	private boolean actief;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vrijveld", nullable = false)
	@Index(name = "idx_VVKOptie_VrijVeld")
	@IgnoreInGebruik
	private VrijVeld vrijVeld;

	public VrijVeldKeuzeOptie()
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

	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	public VrijVeld getVrijVeld()
	{
		return vrijVeld;
	}

	public void setVrijVeld(VrijVeld vrijVeld)
	{
		this.vrijVeld = vrijVeld;
	}
}
