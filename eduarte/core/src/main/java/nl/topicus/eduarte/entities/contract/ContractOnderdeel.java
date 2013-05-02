package nl.topicus.eduarte.entities.contract;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * ContractOnderdeel
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(appliesTo = "ContractOnderdeel", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
@Exportable
public class ContractOnderdeel extends BeginEinddatumInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "contract")
	@Index(name = "idx_ContOnd_contract")
	private Contract contract;

	@Column(nullable = false, length = 100)
	@Index(name = "idx_contOnd_naam")
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@Column(nullable = true)
	@AutoForm(label = "Min. aantal deelnemers")
	private Integer minimumAantalDeelnemers;

	@Column(nullable = true)
	@AutoForm(label = "Max. aantal deelnemers")
	private Integer maximumAantalDeelnemers;

	@Column(nullable = true, scale = 2, precision = 19)
	private BigDecimal prijs;

	@Column(nullable = true, length = 25)
	@AutoForm(htmlClasses = "unit_max")
	private String frequentieAanwezigheid;

	@Column(nullable = true, length = 25)
	@AutoForm(htmlClasses = "unit_max")
	private String groepsgrootte;

	@Column(nullable = true, length = 25)
	@AutoForm(htmlClasses = "unit_max")
	private String begeleidingsintensiteit;

	@Column(nullable = true, length = 25)
	@AutoForm(htmlClasses = "unit_max")
	private String studiebelasting;

	public ContractOnderdeel()
	{

	}

	public ContractOnderdeel(Contract contract)
	{
		setContract(contract);
	}

	public Contract getContract()
	{
		return contract;
	}

	public void setContract(Contract contract)
	{
		this.contract = contract;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Exportable
	public Integer getMinimumAantalDeelnemers()
	{
		return minimumAantalDeelnemers;
	}

	public void setMinimumAantalDeelnemers(Integer minimumAantalDeelnemers)
	{
		this.minimumAantalDeelnemers = minimumAantalDeelnemers;
	}

	@Exportable
	public Integer getMaximumAantalDeelnemers()
	{
		return maximumAantalDeelnemers;
	}

	public void setMaximumAantalDeelnemers(Integer maximumAantalDeelnemers)
	{
		this.maximumAantalDeelnemers = maximumAantalDeelnemers;
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	@Exportable
	public String getPrijsEuro()
	{
		String currency = "";
		if (getPrijs() != null)
		{
			NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("nl", "NL"));
			currency = format.format(getPrijs());
		}
		return currency;
	}

	public void setPrijs(BigDecimal prijs)
	{
		this.prijs = prijs;
	}

	public BigDecimal getPrijs()
	{
		return prijs;
	}

	@Exportable
	public String getFrequentieAanwezigheid()
	{
		return frequentieAanwezigheid;
	}

	public void setFrequentieAanwezigheid(String frequentieAanwezigheid)
	{
		this.frequentieAanwezigheid = frequentieAanwezigheid;
	}

	@Exportable
	public String getGroepsgrootte()
	{
		return groepsgrootte;
	}

	public void setGroepsgrootte(String groepsgrootte)
	{
		this.groepsgrootte = groepsgrootte;
	}

	@Exportable
	public String getBegeleidingsintensiteit()
	{
		return begeleidingsintensiteit;
	}

	public void setBegeleidingsintensiteit(String begeleidingsintensiteit)
	{
		this.begeleidingsintensiteit = begeleidingsintensiteit;
	}

	@Exportable
	public String getStudiebelasting()
	{
		return studiebelasting;
	}

	public void setStudiebelasting(String studiebelasting)
	{
		this.studiebelasting = studiebelasting;
	}

	@Exportable
	@Override
	public Date getEinddatum()
	{
		return super.getEinddatum();
	}

	@Exportable
	@Override
	public Date getBegindatum()
	{
		return super.getBegindatum();
	}

}