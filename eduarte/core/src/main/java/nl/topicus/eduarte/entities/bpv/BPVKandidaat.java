package nl.topicus.eduarte.entities.bpv;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BPVKandidaat extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	public static enum MatchingType
	{
		Instelling("Zoek %1 voor %2."),
		Deelnemer("%1", "%1 mag zelf zoeken."),
		Profiel("%1 mag profiel aanmaken, bedrijven kunnen zoeken.");

		private String title;

		private String label;

		private MatchingType(String title)
		{
			this.title = title;
			this.label = name();
		}

		private MatchingType(String label, String title)
		{
			this.label = label;
			this.title = title;
		}

		@Override
		public String toString()
		{
			return StringUtil.convertCamelCase(getLabel());
		}

		private String getLabel()
		{
			return label.replaceFirst("%1", EduArteApp.get().getBPVTerm());
		}

		public String getTitle()
		{
			return title.replaceFirst("%1", EduArteApp.get().getBPVTerm()).replaceFirst("%2",
				EduArteApp.get().getDeelnemerTerm());
		}
	}

	public static enum MatchingStatus
	{
		Kandidaat,
		Gematched,
		MatchAkkoord,
		BPVAangemaakt
		{
			@Override
			public String toString()
			{
				return "BPV aangemaakt";
			}
		},
		Geannuleerd;

		@Override
		public String toString()
		{
			return StringUtil.convertCamelCase(name());
		}

		public MatchingStatus[] getVervolgEnHuidig()
		{
			switch (this)
			{
				case Kandidaat:
					return new MatchingStatus[] {Kandidaat, Geannuleerd};
				case Gematched:
					return new MatchingStatus[] {Gematched, MatchAkkoord, Geannuleerd};
				case MatchAkkoord:
					return new MatchingStatus[] {MatchAkkoord, BPVAangemaakt, Geannuleerd};
				case BPVAangemaakt:
					return new MatchingStatus[] {BPVAangemaakt};
				case Geannuleerd:
					return new MatchingStatus[] {Geannuleerd, Kandidaat};
				default:
			}
			return new MatchingStatus[] {};
		}
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MatchingType matchingType;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MatchingStatus matchingStatus;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bpvKandidaat")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<BPVMatch> bpvMatches = new ArrayList<BPVMatch>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verbintenis", nullable = false)
	@Index(name = "idx_bpvKand_verbintenis")
	@AutoFormEmbedded
	private Verbintenis verbintenis;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bpvInschrijving", nullable = true)
	@Index(name = "idx_bpvKand_bpvInsch")
	private BPVInschrijving bpvInschrijving;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bpvKandidaat")
	private List<BPVCriteriaBPVKandidaat> bpvCriteria = new ArrayList<BPVCriteriaBPVKandidaat>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bpvKandidaat")
	@AutoForm(description = "Kandidaat op basis van een aantal onderwijsproducten")
	private List<BPVKandidaatOnderwijsproduct> onderwijsproducten =
		new ArrayList<BPVKandidaatOnderwijsproduct>();

	public BPVKandidaat()
	{

	}

	public BPVKandidaat(Verbintenis verbintenis, MatchingType matchingType)
	{
		setVerbintenis(verbintenis);
		setMatchingType(matchingType);
		setMatchingStatus(MatchingStatus.Kandidaat);
	}

	public BPVKandidaat(MatchingType matchingType, MatchingStatus matchingStatus,
			List<BPVMatch> bpvMatches, Verbintenis verbintenis, BPVInschrijving bpvInschrijving,
			List<BPVCriteriaBPVKandidaat> bpvCriteria,
			List<BPVKandidaatOnderwijsproduct> onderwijsproducten)
	{
		setMatchingType(matchingType);
		setMatchingStatus(matchingStatus);
		setBpvMatches(bpvMatches);
		setVerbintenis(verbintenis);
		setBpvInschrijving(bpvInschrijving);
		setBpvCriteria(bpvCriteria);
		setOnderwijsproducten(onderwijsproducten);
	}

	public MatchingType getMatchingType()
	{
		return matchingType;
	}

	public void setMatchingType(MatchingType matchingType)
	{
		this.matchingType = matchingType;
	}

	public Verbintenis getVerbintenis()
	{
		return verbintenis;
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
	}

	public BPVInschrijving getBpvInschrijving()
	{
		return bpvInschrijving;
	}

	public void setBpvInschrijving(BPVInschrijving bpvInschrijving)
	{
		this.bpvInschrijving = bpvInschrijving;
	}

	public void setBpvMatches(List<BPVMatch> bpvMatches)
	{
		this.bpvMatches = bpvMatches;
	}

	public List<BPVMatch> getBpvMatches()
	{
		return bpvMatches;
	}

	public void setMatchingStatus(MatchingStatus matchingStatus)
	{
		this.matchingStatus = matchingStatus;
	}

	public MatchingStatus getMatchingStatus()
	{
		return matchingStatus;
	}

	public void verwijder()
	{
		for (BPVMatch match : getBpvMatches())
		{
			match.delete();
		}
		delete();
	}

	public String getDeelnemer()
	{
		return getVerbintenis().getDeelnemer().toString();
	}

	public BPVMatch getEersteNietVervallenMatch()
	{
		BPVMatch ret = null;
		for (BPVMatch match : getBpvMatches())
		{
			if (!match.isKeuzeVervallen())
			{
				if (ret == null || match.getKeuzeVolgnummer() > ret.getKeuzeVolgnummer())
					ret = match;
			}
		}
		return ret;
	}

	public void setBpvCriteria(List<BPVCriteriaBPVKandidaat> bpvCriteria)
	{
		this.bpvCriteria = bpvCriteria;
	}

	public List<BPVCriteriaBPVKandidaat> getBpvCriteria()
	{
		return bpvCriteria;
	}

	public void setOnderwijsproducten(List<BPVKandidaatOnderwijsproduct> onderwijsproducten)
	{
		this.onderwijsproducten = onderwijsproducten;
	}

	public List<BPVKandidaatOnderwijsproduct> getOnderwijsproducten()
	{
		return onderwijsproducten;
	}

	@Override
	public BPVKandidaat clone()
	{
		return new BPVKandidaat(getMatchingType(), getMatchingStatus(), getBpvMatches(),
			getVerbintenis(), getBpvInschrijving(), getBpvCriteria(), getOnderwijsproducten());
	}

	/**
	 * Geeft een samengestelde lijst van bpvCriteria achterhaald uit de aan de kandidaat
	 * gekoppelde criteria, en uit de criteria die voortkomen uit de onderwijsproductenop
	 * basis waarvan deze student kandidaat is gesteld
	 */
	@AutoForm(description = "Samengevoegde criteria van de kandidaat, bestaande uit criteria van deze kandidaat in combinatie met criteria welke georven zijn van de gekoppelde onderwijsproducten.")
	public List<BPVCriteria> getSamengevoegdeCriteria()
	{
		ArrayList<BPVCriteria> returnlist = new ArrayList<BPVCriteria>();
		for (BPVKandidaatOnderwijsproduct koppel : getOnderwijsproducten())
		{
			for (BPVCriteriaOnderwijsproduct criteria : koppel.getOnderwijsproduct()
				.getBpvCriteria())
			{
				if (!returnlist.contains(criteria.getBpvCriteria()))
				{
					returnlist.add(criteria.getBpvCriteria());
				}
			}
		}
		for (BPVCriteriaBPVKandidaat bpvCriteriaBPVKandidaat : getBpvCriteria())
		{
			if (!returnlist.contains(bpvCriteriaBPVKandidaat.getBpvCriteria()))
			{
				returnlist.add(bpvCriteriaBPVKandidaat.getBpvCriteria());
			}
		}
		return returnlist;
	}

}