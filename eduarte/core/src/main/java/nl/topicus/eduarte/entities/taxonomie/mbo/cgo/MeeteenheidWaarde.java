package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Index;

/**
 * Legt een mogelijke waarde van een Meeteenheid vast. Deze bestaat uit een label (String)
 * en een bijbehorende numerieke waarde.
 * 
 * @author vandenbrink
 */
@Entity
@javax.persistence.Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = {"meeteenheid", "waarde"}),
	@UniqueConstraint(columnNames = {"meeteenheid", "label"})})
public class MeeteenheidWaarde extends LandelijkOfInstellingEntiteit
{

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private Integer waarde;

	/**
	 * Het label dat getoond wordt in een matrixcel (leerpunt e.d.). Mag maximaal 2
	 * karakters zijn.
	 */
	@Column(length = 2, nullable = false)
	private String label;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "meeteenheid", nullable = false)
	@Index(name = "idx_meeteenheid_waarde")
	private Meeteenheid meeteenheid;

	protected MeeteenheidWaarde()
	{
	}

	public MeeteenheidWaarde(EntiteitContext context)
	{
		super(context);
	}

	/**
	 * @return de numerieke waarde van een MeeteenheidWaarde, correspondeert met de
	 *         scores/niveaus die in andere objecten , bv. CompetentieNiveau worden
	 *         opgeslagen
	 */
	public Integer getWaarde()
	{
		return waarde;
	}

	/**
	 * @param waarde
	 */
	public void setWaarde(Integer waarde)
	{
		this.waarde = waarde;
	}

	/**
	 * @return het tekstuele label bij een MeeteenheidWaarde
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

	/**
	 * @return de bijbehorende meeteenheid
	 */
	public Meeteenheid getMeeteenheid()
	{
		return meeteenheid;
	}

	/**
	 * @param meeteenheid
	 */
	public void setMeeteenheid(Meeteenheid meeteenheid)
	{
		this.meeteenheid = meeteenheid;
	}

	@Override
	public String toString()
	{
		return getLabel();
	}

}
