package nl.topicus.eduarte.dao.participatie.helpers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.dao.participatie.hibernate.AfspraakHibernateDataAccessHelper.IIVOTijd;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.AfspraakDeelnemer;
import nl.topicus.eduarte.entities.participatie.Basisrooster;
import nl.topicus.eduarte.entities.participatie.ExternSysteem;
import nl.topicus.eduarte.entities.participatie.ParticipatieMaandOverzicht;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.IIVOTijdZoekFilter;
import nl.topicus.eduarte.participatie.zoekfilters.ParticipatieAanwezigheidMaandZoekFilter;

public interface AfspraakDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Afspraak, AfspraakZoekFilter>
{
	public Afspraak getById(Long id);

	/**
	 * Zoekt de afspraken met het gegeven externe id. Als het goed is geeft de methode max
	 * 1 resultaat terug, maar in theorie zouden er meer gevonden kunnen worden.
	 * 
	 * @param externId
	 * @param externSysteem
	 * @param basisrooster
	 * @return de afspraken
	 */
	public List<Afspraak> getByExternId(String externId, ExternSysteem externSysteem,
			Basisrooster basisrooster);

	public void calculateRecurrence(Afspraak base);

	public void getMaandOverzicht(ParticipatieAanwezigheidMaandZoekFilter filter,
			ParticipatieMaandOverzicht overzicht);

	public Map<IdObject, List<Afspraak>> getAfspraken(AfspraakZoekFilter filter,
			List<IdObject> participanten);

	/**
	 * Geeft de afspraken voor de gegeven groep op het gegeven tijdstip. De gegeven begin-
	 * en eindtijd moet volledig binnen de afspraken vallen.
	 * 
	 * @param groep
	 * @param begintijd
	 * @param eindtijd
	 * @return De afspraken van de gegeven groep
	 */
	public List<Afspraak> getAfspraken(Groep groep, Date begintijd, Date eindtijd);

	/**
	 * Verwijdert de gegeven afspraken.
	 */
	public void verwijderAfspraken(List<Afspraak> afspraken, boolean removeWaarnemingen);

	/**
	 * Geeft de afspraak na de gegeven datum die niet prive is
	 */
	public Afspraak getVolgendeAfspraak(Deelnemer deelnemer, Date after);

	/**
	 * Geeft de afspraken voor de gegeven deelnemer op het gegeven tijdstip. De gegeven
	 * begin- en eindtijd moet volledig binnen de afspraken vallen.
	 * 
	 * @param deelnemer
	 * @param begintijd
	 * @param eindtijd
	 * @return De afspraken van de gegeven deelnemer
	 */
	public List<Afspraak> getAfspraken(Deelnemer deelnemer, Date begintijd, Date eindtijd);

	/**
	 * @param basisrooster
	 * @param beginDatumTijd
	 * @param eindDatumTijd
	 * @return Een lijst met de afspraken in het gegeven basisrooster tussen de gegeven
	 *         data (inclusief)
	 */
	public List<Afspraak> getAfspraken(Basisrooster basisrooster, Date beginDatumTijd,
			Date eindDatumTijd);

	/**
	 * @param filter
	 * @return Een lijst met de totale iivo-tijd per deelnemer.
	 */
	public List<IIVOTijd> getIIVOTijd(IIVOTijdZoekFilter filter);

	/**
	 * @param afspraak
	 * @param deelnemer
	 * @return de afspraakDeelnemer
	 */
	public AfspraakDeelnemer getAfspraakDeelnemer(Afspraak afspraak, Deelnemer deelnemer);

	public List<Afspraak> getOverlappendeAfspraken(Deelnemer deelnemer, Date begintijd,
			Date eindtijd);

	public List<Afspraak> getRoosterAfsprakenGekoppeldAanGroepGesorteerdOpBeginTijd(
			OrganisatieEenheid organisatieEenheid, Locatie locatie, Date peilDatum);

	/**
	 * @param peilDatum
	 * @param beginLesuur
	 * @param eindLesuur
	 * @param docentId
	 * 
	 * @return Geeft een afspraak terug die een docent op een bepaalde dag en lesuur zou
	 *         hebben. Locatie wordt hier genegeerd.
	 */
	public Afspraak getRoosterAfspraakBijDocent(OrganisatieEenheid organisatieEenheid,
			Date peilDatum, int beginLesuur, int eindLesuur, Long docentId);

}
