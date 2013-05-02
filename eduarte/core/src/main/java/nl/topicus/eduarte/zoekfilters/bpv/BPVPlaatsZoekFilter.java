package nl.topicus.eduarte.zoekfilters.bpv;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.wiquery.MoneyInputField;
import nl.topicus.eduarte.entities.bpv.BPVCriteriaBPVPlaats;
import nl.topicus.eduarte.entities.bpv.BPVPlaats;
import nl.topicus.eduarte.entities.bpv.BPVPlaatsOpleiding;
import nl.topicus.eduarte.entities.bpv.BPVPlaats.BPVType;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

/**
 * @author schimmel
 */
public class BPVPlaatsZoekFilter extends AbstractZoekFilter<BPVPlaats>
{
	private static final long serialVersionUID = 1L;

	public static enum BPVPlaatsStatus
	{
		NIET_GEACCORDEERD("Niet geaccordeerd"),
		NIET_GEMATCHED_DOOR_INSTELLING("Niet gematched door instelling"),
		NIET_GEMATCHED("Niet gematched"),
		ALLES("Maakt niet uit");

		private String name;

		BPVPlaatsStatus(String name)
		{
			this.setName(name);
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}

		@Override
		public String toString()
		{
			return getName();
		}
	}

	private BPVType type;

	private List<BPVCriteriaBPVPlaats> bpvCriteria = new ArrayList<BPVCriteriaBPVPlaats>();

	@AutoForm(description = "Minimaal aantal plaatsen", required = false)
	private Integer aantalPlaatsen;

	@AutoForm(description = "Minimaal aantal studenten per plaats", required = false)
	private Integer aantalStudenten;

	@AutoForm(description = "Maximaal aantal begeleidingsuren", required = false)
	private Integer begeleidingsUren;

	private Persoon bedrijfsContactPersoon;

	@AutoForm(label = "Datum vanaf")
	private Date begindatum;

	@AutoForm(label = "Datum uiterlijk tot")
	private Date einddatum;

	private boolean matchingDoorInstelling = true;

	private boolean matchingDoorStudenten = true;

	@AutoForm(label = "Omschrijving bevat")
	private String opdrachtOmschrijving;

	@AutoForm(editorClass = MoneyInputField.class, label = "Vergoeding vanaf")
	private BigDecimal vergoeding;

	@AutoForm(label = "Minimale uren per week", required = false)
	private BigDecimal urenPerWeek;

	@AutoForm(label = "Minimale dagen per week", required = false)
	private Integer dagenPerWeek;

	private ExterneOrganisatie bedrijf;

	private List<BPVPlaatsOpleiding> geschikteOpleidingen = new ArrayList<BPVPlaatsOpleiding>();

	@AutoForm(label = "Status van stageplaats", required = true)
	private BPVPlaatsStatus bpvPlaatsStatus;

	public BPVPlaatsZoekFilter()
	{
		setBpvPlaatsStatus(BPVPlaatsStatus.NIET_GEACCORDEERD);
	}

	public void setType(BPVType type)
	{
		this.type = type;
	}

	public BPVType getType()
	{
		return type;
	}

	public void setBpvCriteria(List<BPVCriteriaBPVPlaats> bpvCriteria)
	{
		this.bpvCriteria = bpvCriteria;
	}

	public List<BPVCriteriaBPVPlaats> getBpvCriteria()
	{
		return bpvCriteria;
	}

	public void setAantalPlaatsen(Integer aantalPlaatsen)
	{
		this.aantalPlaatsen = aantalPlaatsen;
	}

	public Integer getAantalPlaatsen()
	{
		return aantalPlaatsen;
	}

	public void setAantalStudenten(Integer aantalStudenten)
	{
		this.aantalStudenten = aantalStudenten;
	}

	public Integer getAantalStudenten()
	{
		return aantalStudenten;
	}

	public void setBegeleidingsUren(Integer begeleidingsUren)
	{
		this.begeleidingsUren = begeleidingsUren;
	}

	public Integer getBegeleidingsUren()
	{
		return begeleidingsUren;
	}

	public void setBedrijfsContactPersoon(Persoon bedrijfsContactPersoon)
	{
		this.bedrijfsContactPersoon = bedrijfsContactPersoon;
	}

	public Persoon getBedrijfsContactPersoon()
	{
		return bedrijfsContactPersoon;
	}

	public void setBegindatum(Date begindatum)
	{
		this.begindatum = begindatum;
	}

	public Date getBegindatum()
	{
		return begindatum;
	}

	public void setEinddatum(Date einddatum)
	{
		this.einddatum = einddatum;
	}

	public Date getEinddatum()
	{
		return einddatum;
	}

	public void setMatchingDoorInstelling(boolean matchingDoorInstelling)
	{
		this.matchingDoorInstelling = matchingDoorInstelling;
	}

	public boolean isMatchingDoorInstelling()
	{
		return matchingDoorInstelling;
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

	public void setBedrijf(ExterneOrganisatie bedrijf)
	{
		this.bedrijf = bedrijf;
	}

	public ExterneOrganisatie getBedrijf()
	{
		return bedrijf;
	}

	public void setGeschikteOpleidingen(List<BPVPlaatsOpleiding> geschikteOpleidingen)
	{
		this.geschikteOpleidingen = geschikteOpleidingen;
	}

	public List<BPVPlaatsOpleiding> getGeschikteOpleidingen()
	{
		return geschikteOpleidingen;
	}

	public void setBpvPlaatsStatus(BPVPlaatsStatus bpvPlaatsStatus)
	{
		this.bpvPlaatsStatus = bpvPlaatsStatus;
	}

	public BPVPlaatsStatus getBpvPlaatsStatus()
	{
		return bpvPlaatsStatus;
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
