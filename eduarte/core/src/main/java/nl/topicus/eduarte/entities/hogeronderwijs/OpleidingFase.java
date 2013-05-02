package nl.topicus.eduarte.entities.hogeronderwijs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Legt vast in welke hoofdfases de opleiding wordt aangeboden en voor hoeveel credits
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class OpleidingFase extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "opleiding")
	@Index(name = "idx_OplFase_opleiding")
	private Opleiding opleiding;

	@Enumerated(EnumType.STRING)
	@AutoForm(htmlClasses = "unit_max")
	@Column(nullable = false)
	private OpleidingsVorm opleidingsvorm;

	@Enumerated(EnumType.STRING)
	@AutoForm(htmlClasses = "unit_max")
	@Column(nullable = false)
	private Hoofdfase hoofdfase;

	@AutoForm(htmlClasses = "unit_120")
	@Column(nullable = true)
	private Integer credits;

	public OpleidingFase()
	{
	}

	public OpleidingFase(Opleiding opleiding, OpleidingsVorm opleidingsvorm, Hoofdfase hoofdfase)
	{
		this.opleiding = opleiding;
		this.opleidingsvorm = opleidingsvorm;
		this.hoofdfase = hoofdfase;
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = opleiding;
	}

	public Opleiding getOpleiding()
	{
		return opleiding;
	}

	public void setHoofdfase(Hoofdfase hoofdfase)
	{
		this.hoofdfase = hoofdfase;
	}

	public Hoofdfase getHoofdfase()
	{
		return hoofdfase;
	}

	public void setCredits(Integer credits)
	{
		this.credits = credits;
	}

	public Integer getCredits()
	{
		return credits;
	}

	public void setOpleidingsvorm(OpleidingsVorm opleidingsvorm)
	{
		this.opleidingsvorm = opleidingsvorm;
	}

	public OpleidingsVorm getOpleidingsvorm()
	{
		return opleidingsvorm;
	}
}
