package nl.topicus.eduarte.entities.hogeronderwijs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class SpecifiekeVraagAntwoord extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 15)
	private String antwoordcode;

	@Column(nullable = false, length = 350)
	private String antwoord;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "specifiekeVraag")
	@Index(name = "idx_SpecVraagAntw_SpecVraag")
	private SpecifiekeVraag specifiekeVraag;

	public String getAntwoordcode()
	{
		return antwoordcode;
	}

	public void setAntwoordcode(String antwoordcode)
	{
		this.antwoordcode = antwoordcode;
	}

	public String getAntwoord()
	{
		return antwoord;
	}

	public void setAntwoord(String antwoord)
	{
		this.antwoord = antwoord;
	}

	public SpecifiekeVraag getSpecifiekeVraag()
	{
		return specifiekeVraag;
	}

	public void setSpecifiekeVraag(SpecifiekeVraag specifiekeVraag)
	{
		this.specifiekeVraag = specifiekeVraag;
	}
}
