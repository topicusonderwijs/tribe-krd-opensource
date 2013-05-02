package nl.topicus.eduarte.entities.resultaatstructuur;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een tekstuele schaalwaarde van een tekstuele schaal.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"schaal",
	"interneWaarde"})})
public class Schaalwaarde extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	/**
	 * De schaal waarvoor deze waarde geldt.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "schaal")
	@Index(name = "idx_Schaalwaarde_schaal")
	@IgnoreInGebruik
	private Schaal schaal;

	@Column(nullable = false, length = 10)
	@AutoForm(description = "De interne waarde die gebruikt wordt door de onderwijsinstelling om "
		+ "de resultaten in te voeren en te tonen.")
	private String interneWaarde;

	@Column(nullable = false, length = 10)
	@AutoForm(description = "De waarde die gecommuniceerd wordt naar externe instanties, zoals "
		+ "bijvoorbeeld BRON.")
	private String externeWaarde;

	@Column(nullable = false, length = 100)
	private String naam;

	@Column(nullable = false)
	private int volgnummer;

	/**
	 * Is de toets behaald met dit niveau?
	 */
	@Column(nullable = false)
	private boolean behaald;

	@Column(nullable = true, scale = 10, precision = 20)
	@AutoForm(description = "Bij de omrekening van cijfers naar waarden voor een tekstuele schaal "
		+ "wordt de schaalwaarde gekozen als het cijfer tussen 'Vanaf cijfer' en 'T/m cijfer' valt "
		+ "(beiden inclusief). Mochten meerdere schaalwaarden hieraan voldoen, dan wordt de "
		+ "schaalwaarde gekozen met het laagste volgnummer.")
	private BigDecimal vanafCijfer;

	@Column(nullable = true, scale = 10, precision = 20)
	@AutoForm(label = "T/m cijfer")
	private BigDecimal totCijfer;

	@Column(nullable = false, scale = 10, precision = 20)
	@AutoForm(description = "De cijferwaarde waarmee gerekend moet worden bij deze schaalwaarde.")
	private BigDecimal nominaleWaarde;

	public Schaalwaarde()
	{
	}

	public Schaalwaarde(Schaal schaal)
	{
		setSchaal(schaal);
		setVolgnummer(schaal.getNextVolgnummer());
	}

	public Schaal getSchaal()
	{
		return schaal;
	}

	public void setSchaal(Schaal schaal)
	{
		this.schaal = schaal;
	}

	public String getInterneWaarde()
	{
		return interneWaarde;
	}

	public void setInterneWaarde(String interneWaarde)
	{
		this.interneWaarde = interneWaarde;
	}

	public String getExterneWaarde()
	{
		return externeWaarde;
	}

	public void setExterneWaarde(String externeWaarde)
	{
		this.externeWaarde = externeWaarde;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public int getVolgnummer()
	{
		return volgnummer;
	}

	public void setVolgnummer(int volgnummer)
	{
		this.volgnummer = volgnummer;
	}

	public boolean isBehaald()
	{
		return behaald;
	}

	public void setBehaald(boolean behaald)
	{
		this.behaald = behaald;
	}

	public BigDecimal getVanafCijfer()
	{
		return vanafCijfer;
	}

	public void setVanafCijfer(BigDecimal vanafCijfer)
	{
		this.vanafCijfer = vanafCijfer;
	}

	public BigDecimal getTotCijfer()
	{
		return totCijfer;
	}

	public void setTotCijfer(BigDecimal totCijfer)
	{
		this.totCijfer = totCijfer;
	}

	@Override
	public String toString()
	{
		return getInterneWaarde();
	}

	public BigDecimal getNominaleWaarde()
	{
		return nominaleWaarde;
	}

	public void setNominaleWaarde(BigDecimal nominaleWaarde)
	{
		this.nominaleWaarde = nominaleWaarde;
	}

	public boolean bevat(BigDecimal cijfer, int divisor)
	{
		if (getVanafCijfer() == null || getTotCijfer() == null)
			return false;
		BigDecimal vanaf = getVanafCijfer().multiply(BigDecimal.valueOf(divisor));
		BigDecimal tot = getTotCijfer().multiply(BigDecimal.valueOf(divisor));
		return vanaf.compareTo(cijfer) <= 0 && tot.compareTo(cijfer) >= 0;
	}
}
