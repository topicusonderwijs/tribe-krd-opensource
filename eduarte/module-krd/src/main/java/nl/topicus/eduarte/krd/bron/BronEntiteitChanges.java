package nl.topicus.eduarte.krd.bron;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumUtil;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.examen.Examenstatus;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.BronCommuniceerbaar;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaalwaarde;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.SoortToets;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie;

import org.hibernate.Hibernate;

@SuppressWarnings("hiding")
public class BronEntiteitChanges<T extends Entiteit> implements Iterable<BronStateChange>
{
	private final List<BronStateChange> changes;

	private final Date peilDatum = TimeUtil.getInstance().currentDate();

	private T entiteit;

	public BronEntiteitChanges(T entiteit, List<BronStateChange> changes)
	{
		this.entiteit = entiteit;
		this.changes = changes;
	}

	public T getEntiteit()
	{
		return entiteit;
	}

	@Override
	public Iterator<BronStateChange> iterator()
	{
		return changes.iterator();
	}

	List<BronStateChange> getChanges()
	{
		return Collections.unmodifiableList(changes);
	}

	public boolean bevatSleutelgegevenWijziging()
	{
		for (BronStateChange change : changes)
		{
			if (BronWatchList.isSleutelWaarde(change.getEntity(), change.getPropertyName()))
			{
				return true;
			}
		}
		return false;
	}

	private List<BronStateChange> getChanges(Object obj, String... properties)
	{
		List<BronStateChange> result = new ArrayList<BronStateChange>();
		for (BronStateChange change : changes)
		{
			if (change.getEntity().equals(obj))
			{
				for (String property : properties)
				{
					if (change.getPropertyName().equals(property))
						result.add(change);
				}
			}
		}
		return result;
	}

	private List<BronStateChange> getChanges(Object obj)
	{
		List<BronStateChange> result = new ArrayList<BronStateChange>();
		for (BronStateChange change : changes)
		{
			if (change.getEntity().equals(obj))
			{
				result.add(change);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private List<BronStateChange> getChanges(Class< ? >... classes)
	{
		List<BronStateChange> result = new ArrayList<BronStateChange>();
		for (BronStateChange change : changes)
		{
			for (Class< ? > clazz : classes)
			{
				if (Hibernate.getClass(change.getEntity()).isAssignableFrom(clazz))
				{
					result.add(change);
				}
			}
		}
		return result;
	}

	public boolean isGeslaagd(Examendeelname deelname)
	{
		List<BronStateChange> list = getChanges(deelname, "examenstatus");
		for (BronStateChange change : list)
		{
			Examenstatus nieuweStatus = (Examenstatus) change.getCurrentValue();
			return nieuweStatus != null && nieuweStatus.isGeslaagd();
		}
		return false;
	}

	public boolean heeftWijzigingen(Object entiteit)
	{
		return !getChanges(entiteit).isEmpty();
	}

	public boolean heeftWijzigingenVoor(Class< ? >... classes)
	{
		return !getChanges(classes).isEmpty();
	}

	@SuppressWarnings("unchecked")
	public <Y extends IdObject> List<Y> getGewijzigde(Class<Y> entiteitClass)
	{
		ArrayList<Y> result = new ArrayList<Y>();
		List<BronStateChange> wijzigingenVoorEntiteitClass =
			getChanges(new Class< ? >[] {entiteitClass});

		for (BronStateChange change : wijzigingenVoorEntiteitClass)
		{
			IdObject entiteit = change.getEntity();
			if (entiteitClass.isAssignableFrom(Hibernate.getClass(entiteit))
				&& !result.contains(entiteit))
			{
				result.add((Y) entiteit);
			}
		}
		return result;
	}

	public boolean heeftWijziging(Object obj, String property)
	{
		return !getChanges(obj, property).isEmpty();
	}

	@SuppressWarnings("unchecked")
	public <X> X getPreviousValue(Object obj, String property)
	{
		List<BronStateChange> list = getChanges(obj, property);
		for (BronStateChange change : list)
		{
			return (X) change.getPreviousValue();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <X> X getCurrentValue(Object obj, String property)
	{
		List<BronStateChange> list = getChanges(obj, property);
		for (BronStateChange change : list)
		{
			return (X) change.getCurrentValue();
		}
		return null;
	}

	public boolean isWijzigingPersoonsgegevensVo(Verbintenis verbintenis)
	{
		if (heeftWijzigingen(verbintenis.getPersoon()))
		{
			Persoon persoon = verbintenis.getPersoon();
			if (heeftWijziging(persoon, "bsn") || heeftWijziging(persoon, "geboortedatum")
				|| heeftWijziging(persoon, "toepassingGeboortedatum")
				|| heeftWijziging(persoon, "geslacht"))
				return true;
			if (heeftWijziging(persoon, "fysiekeAdressen"))
			{
				// nullcheck toevoegen, misschien eruithalen
				List<PersoonAdres> oudeAdressen = getPreviousValue(persoon, "fysiekAdressen");
				PersoonAdres oudeAdres =
					BeginEinddatumUtil.getElementOpPeildatum(oudeAdressen, peilDatum);
				PersoonAdres nieuwAdres = persoon.getFysiekAdres(peilDatum);
				if (!oudeAdres.getAdres().getPostcode().equals(nieuwAdres.getAdres().getPostcode()))
					return true;
				if (!oudeAdres.getAdres().getLand().equals(nieuwAdres.getAdres().getLand()))
					return true;
			}
		}
		if (verbintenis.getPersoon().getFysiekAdres() != null
			&& heeftWijzigingen(verbintenis.getPersoon().getFysiekAdres().getAdres()))
		{
			Adres adres = verbintenis.getPersoon().getFysiekAdres().getAdres();

			if (heeftWijziging(adres, "postcode") || heeftWijziging(adres, "land"))
				return true;
		}
		return false;
	}

	public boolean isWijzigingCumiVO(Verbintenis verbintenis)
	{
		if (heeftWijziging(verbintenis.getPersoon(), "cumiRatio")
			|| heeftWijziging(verbintenis.getPersoon(), "cumiCategorie"))
			return true;
		return false;
	}

	public boolean isWijzigingInschrijvingsgegevensVO(Verbintenis verbintenis)
	{
		if (!verbintenis.isBronCommuniceerbaar())
			return false;
		List<Plaatsing> gewijzigdePlaatsingen = getGewijzigde(Plaatsing.class);
		if (!gewijzigdePlaatsingen.isEmpty())
		{
			Plaatsing gewijzigdePlaatsing = gewijzigdePlaatsingen.get(0);

			if (heeftAlleenEinddatumWijzigingen(gewijzigdePlaatsing))
				return false;

			// Voor wijzigingen aan alleen leerjaar bij praktijk verbintenis geen melding
			// aanmaken.
			else if (isEnigeWijziging(gewijzigdePlaatsing, "leerjaar"))
			{
				String externeCode = gewijzigdePlaatsing.getVerbintenis().getExterneCode();
				if (externeCode != null && externeCode.equals("0090"))
					return false;
			}
		}
		if (heeftWijziging(verbintenis, "begindatum"))
		{
			return true;
		}
		if (heeftWijziging(verbintenis.getPersoon(), "cumiRatio")
			|| heeftWijziging(verbintenis.getPersoon(), "cumiCategorie"))
			return true;
		if (heeftWijziging(verbintenis, "opleiding") || heeftWijzigingenVoor(Plaatsing.class))
		{
			return true;
		}
		return false;
	}

	public boolean heeftVOExamenresultaatWijzigingenVoor(Verbintenis verbintenis)
	{
		if (heeftWijzigingenVoor(Examendeelname.class))
		{
			for (Examendeelname examendeelname : getGewijzigde(Examendeelname.class))
			{
				if (examendeelname.getVerbintenis().equals(verbintenis))
					return true;
			}
		}
		if (heeftWijzigingenVoor(OnderwijsproductAfnameContext.class))
		{

			for (OnderwijsproductAfnameContext context : getGewijzigde(OnderwijsproductAfnameContext.class))
			{
				if (context.getVerbintenis().equals(verbintenis))
					return true;
			}
		}
		if (heeftWijzigingenVoor(OnderwijsproductAfname.class))
		{
			for (OnderwijsproductAfname afname : getGewijzigde(OnderwijsproductAfname.class))
			{
				for (OnderwijsproductAfnameContext context : afname.getAfnameContexten())
				{
					if (context.getVerbintenis().equals(verbintenis))
						return true;
				}
			}
		}
		if (heeftWijzigingenVoor(Resultaat.class))
		{
			for (Resultaat resultaat : getGewijzigde(Resultaat.class))
			{
				List<OnderwijsproductAfname> afnames =
					resultaat.getToets().getResultaatstructuur().getOnderwijsproduct().getAfnames();
				for (OnderwijsproductAfname afname : afnames)
				{
					for (OnderwijsproductAfnameContext context : afname.getAfnameContexten())
					{
						if (context.getVerbintenis().equals(verbintenis))
							return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public String toString()
	{
		return toString(changes);
	}

	private String toString(List<BronStateChange> listOfChanges)
	{
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sb.append(sdf.format(new Date()));
		String komma = " ";
		for (BronStateChange change : listOfChanges)
		{
			sb.append(komma);
			sb.append(change.toString());
			komma = ", ";
		}
		return sb.toString();
	}

	public String getReden(IdObject value)
	{
		List<BronStateChange> changesForValue = getChanges(value);
		return toString(changesForValue);
	}

	public boolean heeftOnderwijsproductAfnameWijziging(OnderwijsproductAfnameContext context)
	{
		for (OnderwijsproductAfname onderwijsproductAfname : getGewijzigde(OnderwijsproductAfname.class))
		{
			if (context.getOnderwijsproductAfname().equals(onderwijsproductAfname))
				return true;
		}
		return false;
	}

	public boolean isEnigeWijziging(Object gewijzigdeObject, String property)
	{
		List<BronStateChange> changesGewijzigdeObject = getChanges(gewijzigdeObject);
		if (changesGewijzigdeObject != null && changesGewijzigdeObject.size() == 1)
		{
			if (changesGewijzigdeObject.get(0).getPropertyName().equals(property))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Bepaalt of de verbintenis gegevens naar BRON toe moeten, op basis van de status van
	 * de verbintenis. Als er een status wijziging plaats heeft, en &eacute;&eacute;n van
	 * de statussen (oude/nieuwe waarde) een status is die naar BRON moet, dan moet de
	 * wijziging naar BRON gestuurd worden. Als er geen wijziging in de status is, dan
	 * bepaalt de actuele status van de verbintenis of er een melding naar BRON gestuurd
	 * moet worden.
	 */
	public boolean moetNaarBron(Verbintenis verbintenis)
	{
		List<BronStateChange> verbintenisStatusChanges = getChanges(verbintenis, "status");
		if (!verbintenisStatusChanges.isEmpty())
		{
			BronStateChange change = verbintenisStatusChanges.get(0);
			return moetNaarBron(change);
		}
		return verbintenis.isBronCommuniceerbaar()
			|| heeftWijziging(verbintenis, "handmatigVersturenNaarBronMutatie");
	}

	public boolean moetNaarBron(BPVInschrijving inschrijving)
	{
		List<BronStateChange> verbintenisStatusChanges = getChanges(inschrijving, "status");
		if (!verbintenisStatusChanges.isEmpty())
		{
			BronStateChange change = verbintenisStatusChanges.get(0);
			return moetNaarBron(change);
		}
		return isBronCommuniceerbaar(inschrijving.getStatus());
	}

	private boolean moetNaarBron(BronStateChange change)
	{
		BronCommuniceerbaar vorigeStatus = (BronCommuniceerbaar) change.getPreviousValue();
		BronCommuniceerbaar nieuweStatus = (BronCommuniceerbaar) change.getCurrentValue();

		return isBronCommuniceerbaar(vorigeStatus) || isBronCommuniceerbaar(nieuweStatus);
	}

	/**
	 * Null-safe utility methode voor het bepalen of een verbintenis status BRON
	 * communiceerbaar is.
	 */
	private boolean isBronCommuniceerbaar(BronCommuniceerbaar status)
	{
		return status != null && status.isBronCommuniceerbaar();
	}

	public SoortMutatie getBveSoortMutatie(Verbintenis verbintenis)
	{
		if (verbintenis.isHandmatigVersturenNaarBron())
		{
			return verbintenis.getHandmatigeBronBveSoortMutatie();
		}
		else if (isNieuwVoorBron(verbintenis))
		{
			return SoortMutatie.Toevoeging;
		}
		else if (moetUitBronVerwijderdWorden(verbintenis))
		{
			return SoortMutatie.Verwijdering;
		}
		else
		{
			return SoortMutatie.Aanpassing;
		}
	}

	/**
	 * Bepaalt of de verbintenis nieuw is voor BRON door te kijken of de status van een
	 * niet-BRON status naar een wel-BRON status gaat.
	 */
	public boolean isNieuwVoorBron(Verbintenis verbintenis)
	{
		// return !verbintenis.getBronStatus().isGemeldAanBron();

		List<BronStateChange> verbintenisStatusChanges = getChanges(verbintenis, "status");
		if (!verbintenisStatusChanges.isEmpty())
		{
			VerbintenisStatus oudeStatus =
				(VerbintenisStatus) verbintenisStatusChanges.get(0).getPreviousValue();
			VerbintenisStatus nieuweStatus =
				(VerbintenisStatus) verbintenisStatusChanges.get(0).getCurrentValue();

			if (oudeStatus == null)
				return nieuweStatus.isBronCommuniceerbaar();

			if (nieuweStatus == null)
				return false;

			return !oudeStatus.isBronCommuniceerbaar() && nieuweStatus.isBronCommuniceerbaar();
		}
		return false;
	}

	public boolean isNieuwVoorBron(Resultaat resultaat)
	{
		Toets toets = resultaat.getToets();

		// Voor NT2 toets
		if (toets.getParent() != null && toets.getParent().getSoort().isNT2VaardigheidToets())
		{
			if (toets.getSoort() == SoortToets.Instroomniveau)
			{
				return resultaat.getOverschrijft() == null
					|| resultaat.getOverschrijft().getCijfer() == null;
			}
			else
			{
				// Behaald niveau altijd als aanpassing sturen, omdat het onmogelijk is om
				// alleen het behaald niveau te sturen. Als er al een toevoeging in de
				// wachtrij staat, wordt de melding automatisch 'verdicht' met die
				// toevoeging.
				return false;
			}
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean isNieuwVoorBron(OnderwijsproductAfnameContext context)
	{
		Verbintenis verbintenis = context.getVerbintenis();
		if (this.isNieuwVoorBron(verbintenis))
			return true;

		List<BronStateChange> afnameChanges = getChanges(verbintenis, "afnameContexten");

		for (BronStateChange bronStateChange : afnameChanges)
		{
			List<OnderwijsproductAfnameContext> afnameContextVorige =
				(List<OnderwijsproductAfnameContext>) bronStateChange.getPreviousValue();

			List<OnderwijsproductAfnameContext> afnameContextCurrent =
				(List<OnderwijsproductAfnameContext>) bronStateChange.getCurrentValue();

			if (afnameContextCurrent != null)
			{
				for (OnderwijsproductAfnameContext onderwijsproductAfnameContext : afnameContextCurrent)
				{
					if (afnameContextVorige == null
						|| (!afnameContextVorige.contains(onderwijsproductAfnameContext) && onderwijsproductAfnameContext
							.equals(context)))
						return true;
				}
			}
		}

		for (BronStateChange onderwijsproductAfnameChange : getChanges(context,
			"onderwijsproductAfname"))
		{
			if (onderwijsproductAfnameChange.getPreviousValue() == null
				&& onderwijsproductAfnameChange.getCurrentValue() != null)
				return true;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean moetUitBronVerwijderdWorden(OnderwijsproductAfnameContext context)
	{
		Verbintenis verbintenis = context.getVerbintenis();

		List<BronStateChange> afnameChanges = getChanges(verbintenis, "afnameContexten");

		for (BronStateChange bronStateChange : afnameChanges)
		{
			List<OnderwijsproductAfnameContext> afnameContextVorige =
				(List<OnderwijsproductAfnameContext>) bronStateChange.getPreviousValue();

			List<OnderwijsproductAfnameContext> afnameContextCurrent =
				(List<OnderwijsproductAfnameContext>) bronStateChange.getCurrentValue();

			if (afnameContextVorige != null)
			{
				for (OnderwijsproductAfnameContext onderwijsproductAfnameContext : afnameContextVorige)
				{
					if (afnameContextCurrent == null
						|| (!afnameContextCurrent.contains(onderwijsproductAfnameContext))
						&& onderwijsproductAfnameContext.equals(context))

						return true;
				}
			}
		}

		for (BronStateChange onderwijsproductAfnameChange : getChanges(context,
			"onderwijsproductAfname"))
		{
			if (onderwijsproductAfnameChange.getPreviousValue() != null
				&& onderwijsproductAfnameChange.getCurrentValue() == null)
				return true;
		}

		return false;
	}

	public boolean moetUitBronVerwijderdWorden(Resultaat resultaat)
	{
		Toets toets = resultaat.getToets();

		if (toets.getParent() != null && toets.getParent().getSoort().isNT2VaardigheidToets())
		{
			List<BronStateChange> resultaatWaardenWijzigingen = getChanges(resultaat, "waarde");
			if (!resultaatWaardenWijzigingen.isEmpty())
			{
				Schaalwaarde huidigeWaarde =
					(Schaalwaarde) resultaatWaardenWijzigingen.get(0).getCurrentValue();

				if (huidigeWaarde == null && toets.getWeging() == 0)
					return true;
			}
		}

		return false;
	}

	/**
	 * Bepaalt of er een Verwijder bericht naar BRON gestuurd moet worden. Dit is niet
	 * hetzelfde als een Verbintenis beeindigen.
	 */
	protected boolean moetUitBronVerwijderdWorden(Verbintenis verbintenis)
	{
		List<BronStateChange> verbintenisStatusChanges = getChanges(verbintenis, "status");
		if (!verbintenisStatusChanges.isEmpty())
		{
			VerbintenisStatus vorigeStatus =
				(VerbintenisStatus) verbintenisStatusChanges.get(0).getPreviousValue();

			VerbintenisStatus nieuweStatus =
				(VerbintenisStatus) verbintenisStatusChanges.get(0).getCurrentValue();

			if (vorigeStatus == null || !vorigeStatus.isBronCommuniceerbaar())
			{
				// als de vorige status null is, is de verbintenis niet bekend in BRON
				return false;
			}
			if (nieuweStatus == null || nieuweStatus.isBronCommuniceerbaar())
			{
				// als de vorige status niet bron communiceerbaar was, is de verbintenis
				// niet bekend in BRON
				return false;
			}
			return vorigeStatus.isBronCommuniceerbaar() && !nieuweStatus.isBronCommuniceerbaar();
		}
		return false;
	}

	public SoortMutatie getBveSoortMutatie(Examendeelname deelname)
	{
		if (isNieuwVoorBron(deelname))
			return SoortMutatie.Toevoeging;
		if (moetUitBronVerwijderdWorden(deelname))
			return SoortMutatie.Verwijdering;
		return SoortMutatie.Aanpassing;
	}

	private boolean isNieuwVoorBron(Examendeelname deelname)
	{
		List<BronStateChange> deelnameChanges = getChanges(deelname, "examenstatus");
		for (BronStateChange change : deelnameChanges)
		{
			Examenstatus vorige = (Examenstatus) change.getPreviousValue();
			Examenstatus huidige = (Examenstatus) change.getCurrentValue();
			return Examenstatus.isNieuwVoorBronBo(vorige, huidige);
		}
		return false;
	}

	private boolean moetUitBronVerwijderdWorden(Examendeelname deelname)
	{
		List<BronStateChange> statusWijzigingen = getChanges(deelname, "examenstatus");
		if (!statusWijzigingen.isEmpty())
		{
			BronStateChange wijziging = statusWijzigingen.get(0);
			Examenstatus vorigeStatus = (Examenstatus) wijziging.getPreviousValue();
			Examenstatus nieuweStatus = (Examenstatus) wijziging.getCurrentValue();

			return Examenstatus.moetUitBronVerwijderdWordenBo(vorigeStatus, nieuweStatus);
		}
		return false;
	}

	public nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie getVOSoortMutatie(
			Verbintenis verbintenis)
	{
		if (verbintenis.isHandmatigVersturenNaarBron())
		{
			return verbintenis.getHandmatigeBronVoSoortMutatie();
		}
		else if (isUitschrijvingVoorBron(verbintenis))
		{
			return nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie.Uitschrijving;
		}
		else if (isNieuwVoorBron(verbintenis))
		{
			return nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie.Toevoeging;
		}
		else if (moetUitBronVerwijderdWorden(verbintenis))
		{
			return nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie.Verwijdering;
		}
		return nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie.Aanpassing;
	}

	public boolean isNieuwVoorBron(Bekostigingsperiode periode)
	{
		Verbintenis verbintenis = periode.getVerbintenis();
		List<BronStateChange> periodeChanges = getChanges(periode, "begindatum");
		if (!periodeChanges.isEmpty())
		{
			BronStateChange change = periodeChanges.get(0);

			Date previous = (Date) change.getPreviousValue();
			Date current = (Date) change.getCurrentValue();

			if (previous == null && current.equals(verbintenis.getBegindatum())
				&& isNieuwVoorBron(verbintenis))
				return true;
			if (previous == null && current.equals(verbintenis.getBegindatum()))
				return false;
			if (previous == null)
				return true;
			return false;
		}
		return false;
	}

	public boolean isGewijzigd(Bekostigingsperiode periode)
	{
		if (periode == null)
			return false;

		Verbintenis verbintenis = periode.getVerbintenis();
		if (heeftWijzigingen(periode))
		{
			if (periode.getBegindatum().equals(verbintenis.getBegindatum()))
			{
				// als de verbintenis van Ja/Nee naar Gedeeltelijk bekostigd gaat,
				// telt de gewijzigde periode met de begindatum die gelijk is aan die
				// van de verbintenis niet mee als de bekostiging niet verandert.
				Bekostigd wasBekostigd = getPreviousValue(verbintenis, "bekostigd");

				boolean bekostigingGewijzigd =
					(wasBekostigd == Bekostigd.Ja || wasBekostigd == Bekostigd.Nee);

				if (bekostigingGewijzigd)
					return (wasBekostigd == Bekostigd.Ja) != periode.isBekostigd();

				// bekostiging stond al op gedeeltelijk dus wijziging heeft betrekking op
				// bekostiging
				return true;
			}
			// periode heeft andere begindatum dan verbintenis dus wijziging heeft
			// betrekking op bekostiging
			return true;
		}
		// geen wijzigingen
		return false;

	}

	public boolean isNieuwVoorBron(BPVInschrijving inschrijving)
	{
		List<BronStateChange> deelnameChanges = getChanges(inschrijving);
		for (BronStateChange change : deelnameChanges)
		{
			if ("einddatum".equals(change.getPropertyName()))
				continue;
			if ("status".equals(change.getPropertyName()))
			{
				BPVStatus origineel = (BPVStatus) change.getPreviousValue();
				BPVStatus huidig = (BPVStatus) change.getCurrentValue();
				if ((origineel == null || !origineel.isBronCommuniceerbaar())
					&& huidig.isBronCommuniceerbaar())
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean isUitschrijvingVoorBron(Verbintenis verbintenis)
	{
		return heeftWijziging(verbintenis, "einddatum");
	}

	public boolean isUitschrijvingVoorBron(BPVInschrijving bpv)
	{
		return heeftWijziging(bpv, "einddatum");
	}

	public boolean moetUitBronVerwijderdWorden(BPVInschrijving inschrijving)
	{
		List<BronStateChange> statusWijzigingen = getChanges(inschrijving, "status");
		if (!statusWijzigingen.isEmpty())
		{
			BPVStatus vorigeStatus = (BPVStatus) statusWijzigingen.get(0).getPreviousValue();
			BPVStatus nieuweStatus = (BPVStatus) statusWijzigingen.get(0).getCurrentValue();

			if (vorigeStatus == null)
				return false;
			if (nieuweStatus == null)
				return true;
			return vorigeStatus.isBronCommuniceerbaar() && !nieuweStatus.isBronCommuniceerbaar();
		}
		return false;
	}

	public boolean heeftBekostigingsWijzigingen(Verbintenis verbintenis)
	{
		List<BronStateChange> verbintenisBekostigingChanges =
			getChanges(verbintenis, "bekostigd", "begindatum");
		List<BronStateChange> bekostigingChanges = getChanges(Bekostigingsperiode.class);

		boolean verbintenisBekostigingGewijzigd = !verbintenisBekostigingChanges.isEmpty();
		for (BronStateChange bekostigingChange : bekostigingChanges)
		{
			if (((Bekostigingsperiode) bekostigingChange.getEntity()).getVerbintenis().equals(
				verbintenis))
			{
				verbintenisBekostigingGewijzigd = true;
				break;
			}
		}
		return verbintenisBekostigingGewijzigd;
	}

	public boolean heeftAlleenEinddatumWijzigingen(Object obj)
	{
		return isEnigeWijziging(obj, "einddatum");
	}

	public boolean heeftAlleenBekostigingsWijzigingen(Verbintenis verbintenis)
	{
		return isEnigeWijziging(verbintenis, "bekostigd");
	}

	public boolean heeftAlleenVakWijzigingenED(Verbintenis verbintenis)
	{
		return isEnigeWijziging(verbintenis, "afnameContexten");
	}

	public boolean heeftAlleenVolgnummerWijzigingen(OnderwijsproductAfnameContext context)
	{
		return isEnigeWijziging(context, "volgnummer");
	}

	public SoortMutatie getBveSoortMutatie(BPVInschrijving bpv)
	{
		if (bpv.isHandmatigVersturenNaarBron())
		{
			return bpv.getHandmatigeBronBveSoortMutatie();
		}
		if (moetUitBronVerwijderdWorden(bpv))
		{
			return SoortMutatie.Verwijdering;
		}
		if (isNieuwVoorBron(bpv))
		{
			return SoortMutatie.Toevoeging;
		}

		return SoortMutatie.Aanpassing;
	}
}
