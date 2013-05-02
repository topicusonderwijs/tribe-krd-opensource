package nl.topicus.eduarte.entities.contract;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * ContractVerplichting
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(appliesTo = "ContractVerplichting", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
public class ContractVerplichting extends BeginEinddatumInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "contract")
	@Index(name = "idx_ContVerpl_contract")
	private Contract contract;

	@Column(nullable = false, length = 100)
	@AutoForm(htmlClasses = "unit_max")
	private String omschrijving;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = true)
	@Index(name = "idx_ContVerpl_medewerker")
	private Medewerker medewerker;

	@Column(nullable = false)
	private boolean uitgevoerd;

	@Column(nullable = true)
	@AutoForm(label = "Uitgevoerd op")
	private Date datumUitgevoerd;

	public ContractVerplichting()
	{
	}

	public ContractVerplichting(Contract contract)
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

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	public boolean getUitgevoerd()
	{
		return uitgevoerd;
	}

	public void setUitgevoerd(boolean uitgevoerd)
	{
		this.uitgevoerd = uitgevoerd;
	}

	public String getUitgevoerdOmschrijving()
	{
		return getUitgevoerd() ? "Ja" : "Nee";
	}

	@AutoForm(label = "Deadline")
	public Date getDeadline()
	{
		return getEinddatum();
	}

	public void setDeadline(Date deadline)
	{
		setEinddatum(deadline);
	}

	public Date getDatumUitgevoerd()
	{
		return datumUitgevoerd;
	}

	public void setDatumUitgevoerd(Date datumUitgevoerd)
	{
		this.datumUitgevoerd = datumUitgevoerd;
	}

	public String getDatumUitgevoerdFormatted()
	{
		return getDatumUitgevoerd() == null ? "" : TimeUtil.getInstance().formatDate(
			getDatumUitgevoerd());
	}

	@Override
	public String toString()
	{
		return getOmschrijving();
	}

	@Override
	public boolean isActief()
	{
		return !uitgevoerd;
	}

	@Override
	public boolean isActief(Date peildatum)
	{
		return !uitgevoerd;
	}

	public void setNaamContract(String naamContract)
	{
		this.contract.setNaam(naamContract);
	}

	public String getNaamContract()
	{
		return contract.getNaam();
	}
}