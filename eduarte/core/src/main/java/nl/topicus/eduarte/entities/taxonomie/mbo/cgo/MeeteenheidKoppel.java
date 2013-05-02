package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.web.components.choice.MeeteenheidCombobox;
import nl.topicus.eduarte.web.components.choice.MeeteenheidKoppelTypeCombobox;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppelt een meeteenheid aan een organisatie-eenheid of opleiding, per cohort
 * 
 * @author vanharen
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class MeeteenheidKoppel extends InstellingEntiteit
{

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "cohort")
	@Index(name = "idx_meeteenheidK_cohort")
	private Cohort cohort;

	@ManyToOne(optional = false)
	@JoinColumn(name = "meeteenheid", nullable = false)
	@Index(name = "idx_meeteenheidK_meeteenheid")
	@AutoForm(editorClass = MeeteenheidCombobox.class)
	private Meeteenheid meeteenheid;

	@ManyToOne(optional = true)
	@JoinColumn(name = "organisatieEenheid", nullable = true)
	@Index(name = "idx_meeteenheidK_organisatieE")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(optional = true)
	@JoinColumn(name = "opleiding", nullable = true)
	@Index(name = "idx_meeteenheidK_opleidingA")
	private Opleiding opleiding;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@AutoForm(editorClass = MeeteenheidKoppelTypeCombobox.class)
	private MeeteenheidKoppelType type;

	@Column(nullable = false)
	private Boolean vastgezet = false;

	@Column(nullable = false)
	private Boolean automatischAangemaakt = false;

	public Cohort getCohort()
	{
		return cohort;
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = cohort;
	}

	public Meeteenheid getMeeteenheid()
	{
		return meeteenheid;
	}

	public void setMeeteenheid(Meeteenheid meeteenheid)
	{
		Asserts.assertNotNull("meeteenheid", meeteenheid);
		this.meeteenheid = meeteenheid;
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		Asserts.assertNotNull("organisatieEenheid", organisatieEenheid);
		this.organisatieEenheid = organisatieEenheid;
	}

	public Opleiding getOpleiding()
	{
		return opleiding;
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = opleiding;
	}

	public MeeteenheidKoppelType getType()
	{
		return type;
	}

	public void setType(MeeteenheidKoppelType type)
	{
		this.type = type;
	}

	public Boolean getVastgezet()
	{
		return vastgezet;
	}

	public void setVastgezet(Boolean vastgezet)
	{
		this.vastgezet = vastgezet;
	}

	public Boolean getAutomatischAangemaakt()
	{
		return automatischAangemaakt;
	}

	public void setAutomatischAangemaakt(Boolean automatischAangemaakt)
	{
		this.automatischAangemaakt = automatischAangemaakt;
	}

}
