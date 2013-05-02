package nl.topicus.eduarte.entities.inschrijving;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.app.sidebar.datastores.RecenteDeelnemersDataStore;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.entities.vrijevelden.IntakegesprekVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.PersoonVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.PlaatsingVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VerbintenisVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VooropleidingVrijVeld;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.providers.PersoonProvider;
import nl.topicus.eduarte.providers.VerbintenisProvider;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@BatchSize(size = 20)
@Exportable
@Table(appliesTo = "Aanmelding", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
public class Aanmelding extends BeginEinddatumInstellingEntiteit implements DeelnemerProvider,
		PersoonProvider, VerbintenisProvider
{

	public static enum AanmeldingStatus
	{
		Nieuw,
		NaderOnderzoek,
		Goedgekeurd,
		Geparkeerd;

		private String omschrijving;

		private AanmeldingStatus()
		{
			this(null);
		}

		private AanmeldingStatus(String omschrijving)
		{
			this.omschrijving = omschrijving;
		}

		@Override
		public String toString()
		{
			return omschrijving != null ? omschrijving : StringUtil.convertCamelCase(name());
		}

		public AanmeldingStatus[] getVervolgEnHuidig()
		{
			switch (this)
			{
				case Nieuw:
					return new AanmeldingStatus[] {Nieuw, NaderOnderzoek, Goedgekeurd, Geparkeerd};
				case NaderOnderzoek:
					return new AanmeldingStatus[] {NaderOnderzoek, Goedgekeurd, Geparkeerd};
				case Goedgekeurd:
					return new AanmeldingStatus[] {Goedgekeurd};
				case Geparkeerd:
					return new AanmeldingStatus[] {Geparkeerd, NaderOnderzoek, Goedgekeurd};
				default:
			}
			return new AanmeldingStatus[] {};
		}
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verbintenis", nullable = false)
	@Index(name = "idx_aanmelding_verbintenis")
	private Verbintenis verbintenis;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "intakegesprek", nullable = false)
	@Index(name = "idx_aanmelding_intake")
	private Intakegesprek intakegesprek;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AanmeldingStatus status = AanmeldingStatus.Nieuw;

	private static final long serialVersionUID = 1L;

	public Aanmelding()
	{

	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
	}

	@Override
	public Verbintenis getVerbintenis()
	{
		return verbintenis;
	}

	@Override
	public Deelnemer getDeelnemer()
	{
		if (getVerbintenis() != null)
			return getVerbintenis().getDeelnemer();
		return null;
	}

	@Override
	public Persoon getPersoon()
	{
		if (getDeelnemer() != null)
			return getDeelnemer().getPersoon();
		return null;
	}

	public void setStatus(AanmeldingStatus status)
	{
		this.status = status;
	}

	public AanmeldingStatus getStatus()
	{
		return status;
	}

	/**
	 * Let op dit verwijdert de aanmelding met alles wat eraan hangt dus ook persoon
	 * verbintenis e.d. Dit doet geen commit
	 */
	public void verwijderAanmelding()
	{
		DefaultModelManager manager = null;
		// als de status goedgekeurd is, mag alleen de aanmelding verwijderd worden,
		// anders alles.
		if (AanmeldingStatus.Goedgekeurd == getStatus())
		{
			manager = new DefaultModelManager(Aanmelding.class);
			ModelFactory.getCompoundChangeRecordingModel(this, manager).deleteObject();
		}
		else
		{
			RecenteDeelnemersDataStore deelnemerStore =
				EduArteSession.get().getSideBarDataStore(RecenteDeelnemersDataStore.class);
			deelnemerStore.getInschrijvingenModel().getObject().remove(verbintenis);
			deelnemerStore.getInschrijvingenModel().detach();
			manager =
				new DefaultModelManager(PersoonAdres.class, Adres.class,
					PersoonContactgegeven.class, Relatie.class, PersoonExterneOrganisatie.class,
					VooropleidingVrijVeld.class, Vooropleiding.class, IntakegesprekVrijVeld.class,
					PlaatsingVrijVeld.class, Plaatsing.class, VerbintenisContract.class,
					VerbintenisVrijVeld.class, Aanmelding.class, Intakegesprek.class,
					Verbintenis.class, Deelnemer.class, PersoonVrijVeld.class, Persoon.class);

			verbintenis.getDeelnemer().getVerbintenissen().remove(verbintenis);
			ModelFactory.getCompoundChangeRecordingModel(this, manager).deleteObject();
		}
	}

	public void setIntakegesprek(Intakegesprek intakegesprek)
	{
		this.intakegesprek = intakegesprek;
	}

	public Intakegesprek getIntakegesprek()
	{
		return intakegesprek;
	}
}