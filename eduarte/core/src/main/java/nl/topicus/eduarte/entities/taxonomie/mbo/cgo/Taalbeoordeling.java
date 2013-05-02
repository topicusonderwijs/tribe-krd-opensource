package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Index;

/**
 * @author vandenbrink
 */
@Entity
public class Taalbeoordeling extends TaalscoreNiveauVerzameling

{
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = true)
	@JoinColumn(name = "deelnemer", nullable = true)
	@Index(name = "idx_taalBeoord_deelnemer")
	private Deelnemer deelnemer;

	@ManyToOne
	@JoinColumn(name = "medewerker")
	@Index(name = "idx_taalBeoord_medewerker")
	private Medewerker medewerker;

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}
}
