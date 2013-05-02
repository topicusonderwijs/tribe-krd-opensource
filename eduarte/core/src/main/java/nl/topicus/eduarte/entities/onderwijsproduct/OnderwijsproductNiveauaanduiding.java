package nl.topicus.eduarte.entities.onderwijsproduct;

import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(name = "ONDERWIJSPRODUCTNIVEAU", uniqueConstraints = {@UniqueConstraint(columnNames = {
	"code", "organisatie"})})
public class OnderwijsproductNiveauaanduiding extends CodeNaamActiefInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	public OnderwijsproductNiveauaanduiding()
	{
	}
}
