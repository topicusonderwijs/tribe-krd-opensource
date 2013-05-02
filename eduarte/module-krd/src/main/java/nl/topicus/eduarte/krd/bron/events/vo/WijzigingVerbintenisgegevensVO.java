package nl.topicus.eduarte.krd.bron.events.vo;

import static nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie.*;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.BronMeldingOnderdeel;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.bron.BronVerbintenisChanges;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding.VoMeldingSoort;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;

import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;

public class WijzigingVerbintenisgegevensVO extends AbstractVoEvent
{
	private BronVerbintenisChanges changes;

	private Verbintenis verbintenis;

	public WijzigingVerbintenisgegevensVO(BronVerbintenisChanges changes,
			List<BronInschrijvingsgegevensVOMelding> wachtrij, Verbintenis verbintenis)
	{
		super(wachtrij);

		this.changes = changes;
		this.verbintenis = verbintenis;
	}

	@Override
	protected Verbintenis getVerbintenis()
	{
		return verbintenis;
	}

	@Override
	public boolean isToepasselijk()
	{
		if (!verbintenis.isOpleidingBronCommuniceerbaar()
			|| changes.isEnigeWijziging(verbintenis, "redenUitschrijving"))
			return false;
		return verbintenis.isVOVerbintenis()
			&& changes.moetNaarBron(verbintenis)
			&& (verbintenis.isHandmatigVersturenNaarBron() || changes.heeftWijzigingen(verbintenis));
	}

	@Override
	public BronInschrijvingsgegevensVOMelding createMelding()
	{
		if (!isToepasselijk())
			return null;
		SoortMutatie mutatie = changes.getVOSoortMutatie(verbintenis);
		switch (mutatie)
		{
			case Toevoeging:
				return verwerkToevoeging();
			case Verwijdering:
				return verwerkVerwijdering();
			case Aanpassing:
				return verwerkAanpassing();
			case Uitschrijving:
				return verwerkUitschrijving();
		}
		return null;
	}

	private BronInschrijvingsgegevensVOMelding verwerkToevoeging()
	{
		BronInschrijvingsgegevensVOMelding melding =
			findOrNew(VoMeldingSoort.I, verbintenis.getBegindatum());

		// als een bestaande melding in de wachtrij een verwijdering is of een
		// uitschrijving, dan moet deze aangepast worden naar een Aanpassing
		if (melding.getSoortMutatie() == Verwijdering || melding.getSoortMutatie() == Uitschrijving)
		{
			melding.setSoortMutatie(Aanpassing);
			return verwerkAanpassing();
		}
		melding.setSoortMutatie(Toevoeging);
		melding.addReden(changes.toString());
		melding.ververs(null);
		melding.saveOrUpdate();

		verstuurPlaatsingenNaarBron();
		return melding;
	}

	/**
	 * Bij een toevoeging moeten ook alle plaatsingen naar BRON gecommuniceerd worden
	 * (behalve de plaatsing op begindatum van de verbintenis, aangezien die al in de
	 * wachtrij staat)
	 */
	private void verstuurPlaatsingenNaarBron()
	{
		TimeUtil timeutil = TimeUtil.getInstance();
		for (Plaatsing plaatsing : verbintenis.getPlaatsingen())
		{
			if (timeutil.datesEqual(verbintenis.getBegindatum(), plaatsing.getBegindatum()))
				continue;
			WijzigingPlaatsingsgegevensVO plaatsingWijziging =
				new WijzigingPlaatsingsgegevensVO(changes, getWachtrij(), plaatsing);
			plaatsingWijziging.createMelding();
		}
	}

	private BronInschrijvingsgegevensVOMelding verwerkVerwijdering()
	{
		// Bij een wijziging van VOLLEDIG->VOORLOPIG kan direct ook de begindatum
		// gewijzigd worden, daarom als er een begindatum wijziging is de vorige waarde
		// gebruiken.
		Date peildatum = verbintenis.getBegindatum();
		if (changes.heeftWijziging(verbintenis, "begindatum"))
		{
			peildatum = changes.getPreviousValue(verbintenis, "begindatum");
		}
		BronInschrijvingsgegevensVOMelding melding = findOrNew(VoMeldingSoort.I, peildatum);

		// Een toevoeging gevolgd door een verwijdering hoeft helemaal niet naar BRON
		if (melding.getSoortMutatie() == Toevoeging)
		{
			melding.delete();
			melding.setVerwijderd(true);
			verwijderUitWachtrij(melding);
		}
		else
		{
			melding.setSoortMutatie(Verwijdering);
			melding.addReden(changes.toString());
			melding.ververs(null);
			melding.saveOrUpdate();
		}
		// verwijder alle meldingen uit de wachtrij
		List<BronInschrijvingsgegevensVOMelding> meldingenUitWachtrij = getVoMeldingenUitWachtrij();
		for (BronInschrijvingsgegevensVOMelding meldingUitWachtrij : meldingenUitWachtrij)
		{
			if (meldingUitWachtrij.equals(melding))
				continue;
			meldingUitWachtrij.setVerwijderd(true);
			meldingUitWachtrij.delete();
			verwijderUitWachtrij(meldingUitWachtrij);
		}
		return melding;
	}

	private List<BronInschrijvingsgegevensVOMelding> getVoMeldingenUitWachtrij()
	{
		BronMeldingZoekFilter filter = new BronMeldingZoekFilter();
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));
		filter.setVerbintenis(verbintenis);
		filter.setDeelnemer(verbintenis.getDeelnemer());
		filter.setBronMeldingOnderdeel(BronMeldingOnderdeel.VOInschrijving);
		filter.setMeldingStatus(BronMeldingStatus.WACHTRIJ);
		filter.setBronOnderwijssoort(BronOnderwijssoort.VOORTGEZETONDERWIJS);

		return getVoMeldingen(filter);
	}

	private List<BronInschrijvingsgegevensVOMelding> getVoMeldingen(BronMeldingZoekFilter filter)
	{
		BronDataAccessHelper wachtrij = DataAccessRegistry.getHelper(BronDataAccessHelper.class);
		List<BronInschrijvingsgegevensVOMelding> result =
			wachtrij.getBronVoMeldingen(filter, BronInschrijvingsgegevensVOMelding.class);

		return result;
	}

	private BronInschrijvingsgegevensVOMelding verwerkAanpassing()
	{
		if (changes.heeftWijziging(verbintenis, "begindatum"))
		{
			return verwerkBegindatumWijziging();
		}
		BronInschrijvingsgegevensVOMelding melding =
			findOrNew(VoMeldingSoort.I, verbintenis.getBegindatum());
		if (melding.getSoortMutatie() == null)
		{
			melding.setSoortMutatie(Aanpassing);
		}
		melding.ververs(null);
		melding.addReden(changes.toString());
		melding.saveOrUpdate();
		return melding;
	}

	private BronInschrijvingsgegevensVOMelding verwerkBegindatumWijziging()
	{
		Date vorigeBegindatum = changes.getPreviousValue(verbintenis, "begindatum");

		BronInschrijvingsgegevensVOMelding melding = findOrNew(VoMeldingSoort.I, vorigeBegindatum);
		if (melding.getSoortMutatie() == Toevoeging)
		{
			// vervang de ingangsdatum met de nieuwe datum voor een toevoeging die al in
			// de wachtrij staat
			melding.setIngangsDatum(verbintenis.getBegindatum());
			melding.ververs(null);
			melding.addReden(changes.getReden(verbintenis));
			melding.saveOrUpdate();
			return melding;
		}
		else if (melding.getSoortMutatie() == Verwijdering)
		{
			// doe niets... melding wordt al uit bron verwijderd
			return null;
		}
		else if (melding.getSoortMutatie() == Uitschrijving)
		{
			// doe niets... onbekend wat er moet gebeuren met een uitschrijving
			// waarvan de ingangsdatum van de verbintenis wordt aangepast.
			// Waarschijnlijk is dat een enorme applicatie fout
			return null;
		}
		// pas de huidige (of nieuwe) melding aan zodat dit een verwijdering wordt van
		// de verbintenis met de vorige ingangsdatum
		melding.setSoortMutatie(Verwijdering);
		melding.ververs(null);
		melding.addReden(changes.getReden(verbintenis));
		melding.saveOrUpdate();

		// maak een nieuwe toevoeging voor de verbintenis
		melding = findOrNew(VoMeldingSoort.I, verbintenis.getBegindatum());
		melding.setSoortMutatie(Toevoeging);
		melding.ververs(null);
		melding.addReden(changes.getReden(verbintenis));
		melding.saveOrUpdate();
		return melding;
	}

	private BronInschrijvingsgegevensVOMelding verwerkUitschrijving()
	{
		BronInschrijvingsgegevensVOMelding melding = null;

		// Zoek naar een eventuele vorige melding in de wachtrij als de einddatum
		// gewijzigd is van datum1 naar datum2. Dan moet er gezocht worden naar een
		// melding met ingangsdatum datum1, in het andere geval kan er gewoon gezocht
		// worden op de gezette einddatum (maar dan zal er geen melding gevonden worden,
		// en dus een nieuwe uitschrijfmelding aangemaakt worden)
		Date vorigeEinddatum = changes.getPreviousValue(verbintenis, "einddatum");
		if (vorigeEinddatum != null)
			melding = findOrNew(VoMeldingSoort.I, vorigeEinddatum);
		else
			melding = findOrNew(VoMeldingSoort.I, verbintenis.getEinddatum());

		melding.setSoortMutatie(Uitschrijving);
		melding.ververs(null);
		melding.addReden(changes.toString());

		// bij een uitschrijving moet de ingangsdatum de datum uitschrijving zijn.
		melding.setIngangsDatum(verbintenis.getEinddatum());
		melding.saveOrUpdate();
		return melding;
	}
}
