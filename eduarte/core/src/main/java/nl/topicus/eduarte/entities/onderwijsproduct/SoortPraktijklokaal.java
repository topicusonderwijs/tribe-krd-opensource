package nl.topicus.eduarte.entities.onderwijsproduct;

import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Soort praktijklokaal dat nodig is voor een onderwijsproduct.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
public class SoortPraktijklokaal extends CodeNaamActiefInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	public SoortPraktijklokaal()
	{
	}
}
