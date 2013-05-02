package nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.annotation.Record;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.MeldingEnSleutelgegevens;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.Signaal;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.StatusMelding;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "BRON_BVE_TERUGKOPPELMELDINGEN")
public class BronBveTerugkoppelMelding extends InstellingEntiteit implements
		MeldingEnSleutelgegevens, IBronTerugkMelding
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batchgegevens")
	@Index(name = "idx_BRON_BVE_TER_batchgegeven")
	private BronBveBatchgegevens batchgegevens;

	@Column(nullable = true, name = "batchnummer")
	private Integer batchnummerAanleverbestand;

	@Column(nullable = true)
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	private Datum geboortedatum;

	@Enumerated(EnumType.STRING)
	private Geslacht geslacht;

	@Column(length = 4, nullable = true)
	private String land;

	@Column(length = 15, nullable = true, name = "deelnemernummer")
	@AutoForm(label = "Deelnemernummer")
	private String leerlingnummer;

	@Column(nullable = true)
	private Integer meldingnummer;

	@Column(length = 9, nullable = true)
	private String onderwijsnummer;

	@Column(length = 6, nullable = true, name = "postcode")
	private String postcodeVolgensInstelling;

	@Column(length = 9, nullable = true)
	@AutoForm(label = "BSN")
	private String sofinummer;

	@OneToMany(mappedBy = "melding", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@AutoForm(include = false)
	private List<BronBveTerugkoppelRecord> records = new ArrayList<BronBveTerugkoppelRecord>();

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private StatusMelding statusMelding;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "aanlevermelding")
	@Index(name = "idx_BRON_BVE_TER_aanlevermeld")
	private BronAanleverMelding aanlevermelding;

	@OneToMany(mappedBy = "melding", targetEntity = BronBveTerugkoppelRecord.class, fetch = FetchType.LAZY)
	@Where(clause = "type=499")
	@AutoForm(include = false)
	private List<Signaal> signalen = new ArrayList<Signaal>();

	public BronBveBatchgegevens getBatchgegevens()
	{
		return batchgegevens;
	}

	public void setBatchgegevens(BronBveBatchgegevens batchgegevens)
	{
		this.batchgegevens = batchgegevens;
	}

	@Override
	public Datum getGeboortedatum()
	{
		return geboortedatum;
	}

	public void setGeboortedatum(Datum geboortedatum)
	{
		this.geboortedatum = geboortedatum;
	}

	@Override
	public Geslacht getGeslacht()
	{
		return geslacht;
	}

	public void setGeslacht(Geslacht geslacht)
	{
		this.geslacht = geslacht;
	}

	@Override
	public String getLand()
	{
		return land;
	}

	public void setLand(String land)
	{
		this.land = land;
	}

	@Override
	public String getLeerlingnummer()
	{
		return leerlingnummer;
	}

	public void setLeerlingnummer(String leerlingnummer)
	{
		this.leerlingnummer = leerlingnummer;
	}

	@Override
	public Integer getMeldingnummer()
	{
		return meldingnummer;
	}

	public void setMeldingnummer(Integer meldingnummer)
	{
		this.meldingnummer = meldingnummer;
	}

	@Override
	public String getOnderwijsnummer()
	{
		return onderwijsnummer;
	}

	public void setOnderwijsnummer(String onderwijsnummer)
	{
		this.onderwijsnummer = onderwijsnummer;
	}

	@Override
	public String getPostcodeVolgensInstelling()
	{
		return postcodeVolgensInstelling;
	}

	public void setPostcodeVolgensInstelling(String postcodeVolgensInstelling)
	{
		this.postcodeVolgensInstelling = postcodeVolgensInstelling;
	}

	@Override
	public List<BronBveTerugkoppelRecord> getRecords()
	{
		return records;
	}

	public void setRecords(List<BronBveTerugkoppelRecord> records)
	{
		this.records = records;
	}

	@Override
	public String getSofinummer()
	{
		return sofinummer;
	}

	public void setSofinummer(String sofinummer)
	{
		this.sofinummer = sofinummer;
	}

	@Override
	public StatusMelding getStatusMelding()
	{
		return statusMelding;
	}

	public void setStatusMelding(StatusMelding statusMelding)
	{
		this.statusMelding = statusMelding;
	}

	@Override
	public Integer getBatchnummerAanleverbestand()
	{
		return batchnummerAanleverbestand;
	}

	public void setBatchnummerAanleverbestand(Integer batchnummerAanleverbestand)
	{
		this.batchnummerAanleverbestand = batchnummerAanleverbestand;
	}

	@Override
	public int getRecordType()
	{
		return MeldingEnSleutelgegevens.class.getAnnotation(Record.class).type();
	}

	public BronAanleverMelding getAanlevermelding()
	{
		return aanlevermelding;
	}

	public void setAanlevermelding(BronAanleverMelding aanlevermelding)
	{
		this.aanlevermelding = aanlevermelding;
	}

	@Override
	public Integer getTerugkoppelNummer()
	{
		return getMeldingnummer();
	}

	public List<Signaal> getSignalen()
	{
		return signalen;
	}

	public void setSignalen(List<Signaal> signalen)
	{
		this.signalen = signalen;
	}

	@Override
	public Integer getBatchNummer()
	{
		if (getBatchgegevens() != null && getBatchgegevens().getTerugkoppelbestand() != null)
			return getBatchgegevens().getTerugkoppelbestand().getBRONBatchNummer();
		return null;
	}

	@Override
	public String getBestandsnaam()
	{
		if (getBatchgegevens() != null && getBatchgegevens().getTerugkoppelbestand() != null)
			return getBatchgegevens().getTerugkoppelbestand().getBestandsnaam();
		return null;
	}
}
