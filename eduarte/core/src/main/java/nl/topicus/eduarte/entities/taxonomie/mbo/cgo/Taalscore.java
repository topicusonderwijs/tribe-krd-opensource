package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author vandenbrink
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Taalscore extends LandelijkOfInstellingEntiteit
{

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "taalvaardigheid", nullable = false)
	@Index(name = "idx_taalscore_taalvaardigheid")
	private Taalvaardigheid taalvaardigheid;

	@ManyToOne(optional = false)
	@JoinColumn(name = "meeteenheidWaarde", nullable = false)
	@Index(name = "idx_taalscore_waarde")
	private MeeteenheidWaarde waarde;

	@ManyToOne(optional = false)
	@JoinColumn(name = "taalbeoordeling", nullable = false)
	@Index(name = "idx_taalscore_taalscoreNV")
	private TaalscoreNiveauVerzameling taalscoreNiveauVerzameling;

	protected Taalscore()
	{
	}

	public Taalscore(EntiteitContext context)
	{
		super(context);
	}

	/**
	 * @return taalscoreNiveauVerzameling
	 */
	public TaalscoreNiveauVerzameling getTaalscoreNiveauVerzameling()
	{
		return taalscoreNiveauVerzameling;
	}

	/**
	 * @param taalscoreNiveauVerzameling
	 */
	public void setTaalscoreNiveauVerzameling(TaalscoreNiveauVerzameling taalscoreNiveauVerzameling)
	{
		this.taalscoreNiveauVerzameling = taalscoreNiveauVerzameling;
	}

	/**
	 * @return taalvaardigheid
	 */
	public Taalvaardigheid getTaalvaardigheid()
	{
		return taalvaardigheid;
	}

	/**
	 * @param taalvaardigheid
	 */
	public void setTaalvaardigheid(Taalvaardigheid taalvaardigheid)
	{
		this.taalvaardigheid = taalvaardigheid;
	}

	/**
	 * @return de meeteenheidwaarde
	 */
	public MeeteenheidWaarde getWaarde()
	{
		return waarde;
	}

	/**
	 * @param waarde
	 */
	public void setWaarde(MeeteenheidWaarde waarde)
	{
		this.waarde = waarde;
	}
}
