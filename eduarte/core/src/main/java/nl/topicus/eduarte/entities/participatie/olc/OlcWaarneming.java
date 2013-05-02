package nl.topicus.eduarte.entities.participatie.olc;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.participatie.AfspraakType;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.web.components.choice.participatie.olc.OlcLocatieCombobox;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
// @Table(appliesTo = "OlcWaarneming", indexes = {@Index(name =
// "idx_OlcWaarneming_organisatie", columnNames = {"organisatie"})})
public class OlcWaarneming extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "olcLocatie", nullable = false)
	@Index(name = "idx_OlcWaarneming_organisatieE")
	@AutoForm(label = "OLC Locatie", editorClass = OlcLocatieCombobox.class, htmlClasses = {"unit_max"})
	private OlcLocatie olcLocatie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	@Index(name = "idx_OlcWaarneming_deelnemer")
	private Deelnemer deelnemer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = true)
	@Index(name = "idx_OlcWaarneming_medewerker")
	private Medewerker medewerker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "afspraakType", nullable = false)
	@Index(name = "idx_OlcWaarneming_afspraakType")
	private AfspraakType afspraakType;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = false)
	private Date datum;

	@Temporal(value = TemporalType.TIME)
	@Column(nullable = false)
	private Date beginTijd;

	@Temporal(value = TemporalType.TIME)
	@Column(nullable = true)
	private Date eindTijd;

	@Column(nullable = true)
	private boolean verwerkt;

	public OlcWaarneming()
	{
	}

	public boolean isVerwerkt()
	{
		return verwerkt;
	}

	public void setVerwerkt(boolean verwerkt)
	{
		this.verwerkt = verwerkt;
	}

	public void setOlcLocatie(OlcLocatie olcLocatie)
	{
		this.olcLocatie = olcLocatie;
	}

	public OlcLocatie getOlcLocatie()
	{
		return olcLocatie;
	}

	public void setBeginTijd(Date beginTijd)
	{
		this.beginTijd = beginTijd;
	}

	public Date getBeginTijd()
	{
		return beginTijd;
	}

	public void setEindTijd(Date eindTijd)
	{
		this.eindTijd = eindTijd;
	}

	public Date getEindTijd()
	{
		return eindTijd;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setDatum(Date datum)
	{
		this.datum = datum;
	}

	public Date getDatum()
	{
		return datum;
	}

	public void setAfspraakType(AfspraakType afspraakType)
	{
		this.afspraakType = afspraakType;
	}

	public AfspraakType getAfspraakType()
	{
		return afspraakType;
	}
}
