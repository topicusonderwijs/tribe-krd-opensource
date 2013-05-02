package nl.topicus.eduarte.entities.inschrijving;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.ContractOnderdeel;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * Koppeling tussen {@link Verbintenis} en {@link Contract}/{@link ContractOnderdeel}.
 * 
 * @author hoeve
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(appliesTo = "VerbintenisContract", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
@Exportable
public class VerbintenisContract extends BeginEinddatumInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "verbintenis")
	@Index(name = "idx_VerbCont_verbintenis")
	private Verbintenis verbintenis;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "contract")
	@Index(name = "idx_VerbCont_contract")
	private Contract contract;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "onderdeel")
	@Index(name = "idx_VerbCont_onderdeel")
	private ContractOnderdeel onderdeel;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date datumBeschikking;

	@Column(nullable = true)
	private String externNummer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "extOrgContactPersoon")
	@Index(name = "idx_VerbCont_extOrgCP")
	private ExterneOrganisatieContactPersoon externeOrganisatieContactPersoon;

	public VerbintenisContract()
	{
	}

	public VerbintenisContract(Verbintenis verbintenis)
	{
		setVerbintenis(verbintenis);
	}

	public Verbintenis getVerbintenis()
	{
		return verbintenis;
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
	}

	@Exportable
	public Contract getContract()
	{
		return contract;
	}

	public void setContract(Contract contract)
	{
		this.contract = contract;
	}

	public ContractOnderdeel getOnderdeel()
	{
		return onderdeel;
	}

	public void setOnderdeel(ContractOnderdeel onderdeel)
	{
		this.onderdeel = onderdeel;
	}

	public String getExternNummer()
	{
		return externNummer;
	}

	public void setExternNummer(String externNummer)
	{
		this.externNummer = externNummer;
	}

	public ExterneOrganisatie getExterneOrganisatie()
	{
		return contract.getExterneOrganisatie();
	}

	public void setDatumBeschikking(Date datumBeschikking)
	{
		this.datumBeschikking = datumBeschikking;
	}

	public Date getDatumBeschikking()
	{
		return datumBeschikking;
	}

	@Exportable
	public ExterneOrganisatieContactPersoon getExterneOrganisatieContactPersoon()
	{
		return externeOrganisatieContactPersoon;
	}

	public void setExterneOrganisatieContactPersoon(
			ExterneOrganisatieContactPersoon externeOrganisatieContactPersoon)
	{
		this.externeOrganisatieContactPersoon = externeOrganisatieContactPersoon;
	}

}