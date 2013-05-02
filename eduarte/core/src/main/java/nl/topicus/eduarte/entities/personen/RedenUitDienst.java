package nl.topicus.eduarte.entities.personen;

import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author hoeve
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
@Exportable
public class RedenUitDienst extends CodeNaamActiefInstellingEntiteit implements
		Comparable<RedenUitDienst>
{
	private static final long serialVersionUID = 1L;

	public RedenUitDienst()
	{
	}

	@Override
	public int compareTo(RedenUitDienst arg0)
	{
		if (arg0 == null)
			throw new NullPointerException("Kan niet vergelijken met null.");
		return getNaam().compareTo(arg0.getNaam());
	}

	@Override
	public String toString()
	{
		return getNaam();
	}
}
