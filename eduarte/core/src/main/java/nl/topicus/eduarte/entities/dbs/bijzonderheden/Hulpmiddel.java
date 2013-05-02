package nl.topicus.eduarte.entities.dbs.bijzonderheden;

import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefLandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
public class Hulpmiddel extends CodeNaamActiefLandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	public Hulpmiddel()
	{
	}
}
