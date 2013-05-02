package nl.topicus.eduarte.entities.groep;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Een instelling kan verschillende types groepen aanmaken.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
@IsViewWhenOnNoise
public class Groepstype extends CodeNaamActiefInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	/**
	 * Geeft aan of dit een plaatsingsgroep (basisgroep, stamgroep) is. Zo ja, kunnen de
	 * deelnemers van deze groepen alleen via een plaatsing aan de groep gekoppeld worden.
	 * Omgekeerd kan aan groepen die geen plaatsingsgroep zijn, geen plaatsing gekoppeld
	 * worden.
	 */
	@Column(nullable = false)
	private boolean plaatsingsgroep;

	public Groepstype()
	{
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	public boolean isPlaatsingsgroep()
	{
		return plaatsingsgroep;
	}

	public void setPlaatsingsgroep(boolean plaatsingsgroep)
	{
		this.plaatsingsgroep = plaatsingsgroep;
	}
}
