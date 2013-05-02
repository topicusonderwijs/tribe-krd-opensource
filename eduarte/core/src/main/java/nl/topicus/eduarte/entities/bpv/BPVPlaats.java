package nl.topicus.eduarte.entities.bpv;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.wiquery.MoneyInputField;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.web.components.panels.ExterneOrganisatieContactPersoonPanel;

import org.apache.wicket.markup.html.form.TextArea;
import org.hibernate.annotations.Index;

@Entity
public class BPVPlaats extends InstellingEntiteit
{

	private static final long serialVersionUID = 1L;

	public enum BPVType
	{
		AfstudeerStage,
		Tussenstage
	}

	@Enumerated(EnumType.STRING)
	private BPVType type;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bpvPlaats")
	private List<BPVCriteriaBPVPlaats> bpvCriteria = new ArrayList<BPVCriteriaBPVPlaats>();

	private int aantalPlaatsen;

	private int aantalStudenten;

	private int begeleidingsUren;

	/**
	 * Deze moet gekoppeld zijn aan de externe organisatie bpvBedrijf
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contactPersoonBPVBedrijf", nullable = true)
	@Index(name = "idx_bpvPlaats_contPersBPV")
	@AutoForm(label = "Contactpersoon", displayClass = ExterneOrganisatieContactPersoonPanel.class, htmlClasses = "unit_max")
	private ExterneOrganisatieContactPersoon contactPersoonBPVBedrijf;

	private Date begindatum;

	private Date einddatum;

	private boolean matchingDoorInstelling = true;

	private boolean matchingDoorStudenten = true;

	@AutoForm(editorClass = TextArea.class)
	private String opdrachtOmschrijving;

	@AutoForm(editorClass = MoneyInputField.class)
	@Column(scale = 10, precision = 20)
	private BigDecimal vergoeding;

	@Column(nullable = true, scale = 2, precision = 12)
	private BigDecimal urenPerWeek;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "externeOrganisatie", nullable = false)
	@Index(name = "idx_bpvPl_exto")
	private ExterneOrganisatie bedrijf;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bpvPlaats")
	private List<BPVPlaatsOpleiding> geschikteOpleidingen = new ArrayList<BPVPlaatsOpleiding>();

	@Column(nullable = true, length = 4)
	private Integer dagenPerWeek;

	public BPVPlaats()
	{

	}

	public BPVPlaats(ExterneOrganisatie externeOrganisatie)
	{
		this.bedrijf = externeOrganisatie;
	}

	public void setBpvCriteria(List<BPVCriteriaBPVPlaats> bpvCriteria)
	{
		this.bpvCriteria = bpvCriteria;
	}

	public List<BPVCriteriaBPVPlaats> getBpvCriteria()
	{
		return bpvCriteria;
	}

	public void setAantalPlaatsen(int aantalPlaatsen)
	{
		this.aantalPlaatsen = aantalPlaatsen;
	}

	public int getAantalPlaatsen()
	{
		return aantalPlaatsen;
	}

	public void setAantalStudenten(int aantalStudenten)
	{
		this.aantalStudenten = aantalStudenten;
	}

	public int getAantalStudenten()
	{
		return aantalStudenten;
	}

	public void setBegeleidingsUren(int begeleidingsUren)
	{
		this.begeleidingsUren = begeleidingsUren;
	}

	public int getBegeleidingsUren()
	{
		return begeleidingsUren;
	}

	public void setBegindatum(Date begindatum)
	{
		this.begindatum = begindatum;
	}

	public Date getBegindatum()
	{
		return begindatum;
	}

	public void setMatchingDoorInstelling(boolean matchingDoorInstelling)
	{
		this.matchingDoorInstelling = matchingDoorInstelling;
	}

	public boolean isMatchingDoorInstelling()
	{
		return matchingDoorInstelling;
	}

	public void setEinddatum(Date einddatum)
	{
		this.einddatum = einddatum;
	}

	public Date getEinddatum()
	{
		return einddatum;
	}

	public void setMatchingDoorStudenten(boolean matchingDoorStudenten)
	{
		this.matchingDoorStudenten = matchingDoorStudenten;
	}

	public boolean isMatchingDoorStudenten()
	{
		return matchingDoorStudenten;
	}

	public void setOpdrachtOmschrijving(String opdrachtOmschrijving)
	{
		this.opdrachtOmschrijving = opdrachtOmschrijving;
	}

	public String getOpdrachtOmschrijving()
	{
		return opdrachtOmschrijving;
	}

	public void setVergoeding(BigDecimal vergoeding)
	{
		this.vergoeding = vergoeding;
	}

	public BigDecimal getVergoeding()
	{
		return vergoeding;
	}

	public void setUrenPerWeek(BigDecimal urenPerWeek)
	{
		this.urenPerWeek = urenPerWeek;
	}

	public BigDecimal getUrenPerWeek()
	{
		return urenPerWeek;
	}

	public void setGeschikteOpleidingen(List<BPVPlaatsOpleiding> geschikteOpleidingen)
	{
		this.geschikteOpleidingen = geschikteOpleidingen;
	}

	public List<BPVPlaatsOpleiding> getGeschikteOpleidingen()
	{
		return geschikteOpleidingen;
	}

	public void setType(BPVType type)
	{
		this.type = type;
	}

	public BPVType getType()
	{
		return type;
	}

	public void setBedrijf(ExterneOrganisatie bedrijf)
	{
		this.bedrijf = bedrijf;
	}

	public ExterneOrganisatie getBedrijf()
	{
		return bedrijf;
	}

	/**
	 * Geeft een samengestelde lijst van bpvCriteria achterhaald uit de aan de plaats
	 * gekoppelde externe organisatie, en uit de criteria die voortkomen uit de plaats
	 * zelf
	 */
	public List<BPVCriteria> getSamengevoegdeCriteria()
	{
		ArrayList<BPVCriteria> returnlist = new ArrayList<BPVCriteria>();
		for (BPVCriteriaExterneOrganisatie criteria : getBedrijf().getBpvCriteria())
		{
			if (!returnlist.contains(criteria.getBpvCriteria()))
			{
				returnlist.add(criteria.getBpvCriteria());
			}
		}

		for (BPVCriteriaBPVPlaats bpvCriteriaBPVKandidaat : getBpvCriteria())
		{
			if (!returnlist.contains(bpvCriteriaBPVKandidaat.getBpvCriteria()))
			{
				returnlist.add(bpvCriteriaBPVKandidaat.getBpvCriteria());
			}
		}
		return returnlist;
	}

	public void setContactPersoonBPVBedrijf(
			ExterneOrganisatieContactPersoon contactPersoonBPVBedrijf)
	{
		this.contactPersoonBPVBedrijf = contactPersoonBPVBedrijf;
	}

	public ExterneOrganisatieContactPersoon getContactPersoonBPVBedrijf()
	{
		return contactPersoonBPVBedrijf;
	}

	public void setDagenPerWeek(Integer dagenPerWeek)
	{
		this.dagenPerWeek = dagenPerWeek;
	}

	public Integer getDagenPerWeek()
	{
		return dagenPerWeek;
	}
}
