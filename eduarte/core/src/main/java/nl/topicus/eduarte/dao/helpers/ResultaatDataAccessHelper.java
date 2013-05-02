package nl.topicus.eduarte.dao.helpers;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.dao.hibernate.ResultaatHibernateDataAccessHelper.TypeResultaat;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.zoekfilters.ResultaatZoekFilter;

public interface ResultaatDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Resultaat, ResultaatZoekFilter>
{

	public Resultaat getGeldendeResultaat(Toets toets, Deelnemer deelnemer);

	public List<Resultaat> getActueleResultaten(Toets toets, Deelnemer deelnemer);

	/**
	 * Geeft de huidige eindresultaten voor de gegeven collectie van
	 * onderwijsproductafnames. Hierbij worden zowel definitieve als voorlopige
	 * eindresultaten teruggegeven. De aanroeper moet zelf controleren welke resultaten
	 * definitief zijn, en welke nog voorlopig.
	 * 
	 * @param deelnemer
	 * @param onderwijsproductAfnames
	 * @return De eindresultaten van de gegeven onderwijsproductafnames.
	 */
	public Map<OnderwijsproductAfname, Resultaat> getEindresultaten(Deelnemer deelnemer,
			Collection<OnderwijsproductAfname> onderwijsproductAfnames);

	/**
	 * Geeft de definitieve schoolexamenresultaten (SE-resultaten) terug voor de gegeven
	 * collectie van onderwijsproductafnames van de gegeven deelnemer. (Voorlopige
	 * resultaten worden dus niet teruggegeven). Deze methode geeft alleen resultaten
	 * terug als voor de gegeven onderwijsproducten er ook daadwerkelijk een SE-toets is
	 * aangemaakt.
	 * 
	 * @param deelnemer
	 * @param onderwijsproductAfnames
	 * @return De schoolexamenresultaten van de gegeven onderwijsproductafnames
	 */
	public Map<Onderwijsproduct, Resultaat> getDefinitieveSchoolexamenResultaten(
			Deelnemer deelnemer, Collection<OnderwijsproductAfname> onderwijsproductAfnames,
			TypeResultaat typeResultaat);

	/**
	 * Geeft de definitieve centraal examenresultaten (CE-resultaten) terug voor de
	 * gegeven collectie van onderwijsproductafnames van de gegeven deelnemer. (Voorlopige
	 * resultaten worden dus niet teruggegeven). Deze methode geeft alleen resultaten
	 * terug als voor de gegeven onderwijsproducten er ook daadwerkelijk een CE-toets is
	 * aangemaakt. Per onderwijsproduct wordt (potentieel) drie resultaten teruggegeven,
	 * CE1, CE2 en CE3. Elk resultaat heeft een ander herkansingsnummer. Een daarvan is
	 * het geldende resultaat.
	 * 
	 * @param deelnemer
	 * @param onderwijsproductAfnames
	 * @return De centraal examenresultaten van de gegeven onderwijsproductafnames
	 */
	public Map<Onderwijsproduct, List<Resultaat>> getDefinitieveCentraalExamenResultaten(
			Deelnemer deelnemer, Collection<OnderwijsproductAfname> onderwijsproductAfnames);

	public void verifyResultaten(ResultaatZoekFilter filter);

	/**
	 * Geeft aan of de gegeven deelnemer minimaal 1 resultaat heeft voor het gegeven
	 * onderwijsproduct in het gegeven cohort.
	 */
	public boolean heeftResultaten(Deelnemer deelnemer, Onderwijsproduct onderwijsproduct,
			Cohort cohort);
}
