package nl.topicus.eduarte.entities.hogeronderwijs;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class SpecifiekeVraag extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 15)
	private String vraagcode;

	@Column(nullable = false, length = 350)
	private String vraag;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "specifiekeVraag")
	@BatchSize(size = 20)
	private List<SpecifiekeVraagAntwoord> antwoorden = new ArrayList<SpecifiekeVraagAntwoord>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inschrijvingsverzoek")
	@Index(name = "idx_SpecVraag_Inschrverzoek")
	private Inschrijvingsverzoek inschrijvingsverzoek;

	public String getVraagcode()
	{
		return vraagcode;
	}

	public void setVraagcode(String vraagcode)
	{
		this.vraagcode = vraagcode;
	}

	public String getVraag()
	{
		return vraag;
	}

	public void setVraag(String vraag)
	{
		this.vraag = vraag;
	}

	public List<SpecifiekeVraagAntwoord> getAntwoorden()
	{
		return antwoorden;
	}

	public void setAntwoorden(List<SpecifiekeVraagAntwoord> antwoorden)
	{
		this.antwoorden = antwoorden;
	}

	public Inschrijvingsverzoek getInschrijvingsverzoek()
	{
		return inschrijvingsverzoek;
	}

	public void setInschrijvingsverzoek(Inschrijvingsverzoek inschrijvingsverzoek)
	{
		this.inschrijvingsverzoek = inschrijvingsverzoek;
	}

}
