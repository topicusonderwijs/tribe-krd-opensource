package nl.topicus.eduarte.entities.opleiding;

import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Een team geeft aan wie verantwoordelijk is voor een opleiding bij een bepaalde
 * organisatie-eenheid/locatie.
 * 
 * @author loite
 */
@Entity()
@Exportable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
public class Team extends CodeNaamActiefInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	public Team()
	{
	}

}
