package nl.topicus.eduarte.entities.hogeronderwijs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class StudielinkBericht extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 50, nullable = false)
	private String type;

	@Lob
	@Column(nullable = true)
	private String xmlResponse;

	@Column(length = 4, nullable = false)
	private String verzender;

	@Column(length = 4, nullable = false)
	private String ontvanger;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inschrijvingsverzoek", nullable = true)
	@Index(name = "idx_SlBericht_Inschrverzoek")
	private Inschrijvingsverzoek inschrijvingsverzoek;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	@Index(name = "idx_SLBericht_Deelnemer")
	private Deelnemer deelnemer;

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getXmlResponse()
	{
		return xmlResponse;
	}

	public void setXmlResponse(String xmlResponse)
	{
		this.xmlResponse = xmlResponse;
	}

	public String getVerzender()
	{
		return verzender;
	}

	public void setVerzender(String verzender)
	{
		this.verzender = verzender;
	}

	public String getOntvanger()
	{
		return ontvanger;
	}

	public void setOntvanger(String ontvanger)
	{
		this.ontvanger = ontvanger;
	}

	public Inschrijvingsverzoek getInschrijvingsverzoek()
	{
		return inschrijvingsverzoek;
	}

	public void setInschrijvingsverzoek(Inschrijvingsverzoek inschrijvingsverzoek)
	{
		this.inschrijvingsverzoek = inschrijvingsverzoek;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}
}
