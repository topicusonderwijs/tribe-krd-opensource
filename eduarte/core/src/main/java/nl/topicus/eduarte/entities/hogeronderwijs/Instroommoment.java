package nl.topicus.eduarte.entities.hogeronderwijs;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
public class Instroommoment extends CodeNaamActiefInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "instroommoment")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<Inschrijvingsverzoek> inschrijvingsverzoeken =
		new ArrayList<Inschrijvingsverzoek>();

	public List<Inschrijvingsverzoek> getInschrijvingsverzoeken()
	{
		return inschrijvingsverzoeken;
	}

	public void setInschrijvingsverzoeken(List<Inschrijvingsverzoek> inschrijvingsverzoeken)
	{
		this.inschrijvingsverzoeken = inschrijvingsverzoeken;
	}
}
