package nl.topicus.eduarte.entities.bpv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"bpvKandidaat",
	"keuzeVolgnummer"})})
public class BPVMatch extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bpvKandidaat", nullable = false)
	@Index(name = "idx_bpvMatch_bpvKand")
	private BPVKandidaat bpvKandidaat;

	// nullable moet niet false zijn, als je met colo ga matchen hang je hier nog geen
	// stageplaats aan.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bpvPlaats", nullable = true)
	@Index(name = "idx_bpvMatch_plaats")
	private BPVPlaats bpvPlaats;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bpvColoPlaats", nullable = true)
	@Index(name = "idx_bpvMatch_coloplaats")
	private BPVColoPlaats bpvColoPlaats;

	@Column(nullable = false)
	private boolean matchAkkoord;

	/**
	 * Het hoogste keuzevolgnummer is de eerstekeuze, behalve als de keuze is vervallen
	 */
	@Column(nullable = false)
	private int keuzeVolgnummer;

	@Column(nullable = false)
	private boolean keuzeVervallen;

	public BPVMatch()
	{
	}

	public BPVMatch(BPVKandidaat kandidaat, BPVPlaats plaats)
	{
		setBpvKandidaat(kandidaat);
		setBpvPlaats(plaats);
	}

	public BPVKandidaat getBpvKandidaat()
	{
		return bpvKandidaat;
	}

	public void setBpvKandidaat(BPVKandidaat bpvKandidaat)
	{
		this.bpvKandidaat = bpvKandidaat;
	}

	public void setKeuzeVervallen(boolean keuzeVervallen)
	{
		this.keuzeVervallen = keuzeVervallen;
	}

	public boolean isKeuzeVervallen()
	{
		return keuzeVervallen;
	}

	public void setKeuzeVolgnummer(int keuzeVolgnummer)
	{
		this.keuzeVolgnummer = keuzeVolgnummer;
	}

	public int getKeuzeVolgnummer()
	{
		return keuzeVolgnummer;
	}

	public void setMatchAkkoord(boolean matchAkkoord)
	{
		this.matchAkkoord = matchAkkoord;
	}

	public boolean isMatchAkkoord()
	{
		return matchAkkoord;
	}

	/**
	 * Methode die aangeroep wordt om bij het drag en drop van dit object de gegevens van
	 * de een naar de ander te zetten
	 */
	public void wissel(BPVMatch droppedOn)
	{
		BPVColoPlaats bpvColoPlaatsCp = getBpvColoPlaats();
		setBpvColoPlaats(droppedOn.getBpvColoPlaats());
		droppedOn.setBpvColoPlaats(bpvColoPlaatsCp);
	}

	public void setBpvPlaats(BPVPlaats bpvPlaats)
	{
		this.bpvPlaats = bpvPlaats;
	}

	public BPVPlaats getBpvPlaats()
	{
		return bpvPlaats;
	}

	public String getDragDropLabelBoven()
	{
		if (getBpvColoPlaats() == null)
			return "";
		return getBpvColoPlaats().getDragDropLabelBoven();
	}

	public String getDragDropLabelOnder()
	{
		if (getBpvColoPlaats() == null)
			return "";
		return getBpvColoPlaats().getDragDropLabelOnder();
	}

	public void setBpvColoPlaats(BPVColoPlaats bpvColoPlaats)
	{
		this.bpvColoPlaats = bpvColoPlaats;
	}

	public BPVColoPlaats getBpvColoPlaats()
	{
		return bpvColoPlaats;
	}

	public String getNaamLeerbedrijf()
	{
		if (getBpvColoPlaats() != null)
			return getBpvColoPlaats().getLeerbedrijfNaam();
		if (getBpvPlaats() != null)
			return getBpvPlaats().getBedrijf().getVerkorteNaam();
		return "";
	}
}
