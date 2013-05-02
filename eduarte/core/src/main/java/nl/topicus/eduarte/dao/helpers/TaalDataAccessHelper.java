/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.helpers;

import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.taxonomie.MBONiveau;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.ModerneTaal;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.TaalType;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Taalvaardigheid;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Taalvaardigheidseis;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.TaalvaardigheidseisLlb;
import nl.topicus.eduarte.zoekfilters.TaalZoekFilter;

/**
 * @author vandenbrink
 */
public interface TaalDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<ModerneTaal, TaalZoekFilter>
{

	/**
	 * De lijst van gedefinieerde taalvaardigheidseisen.
	 * 
	 * @return taalbeheersingseisen
	 */
	public List<Taalvaardigheidseis> getTaalvaardigheidseisen();

	/**
	 * De lijst van taalvaardigheden (lezen, schrijven, e.d.)
	 * 
	 * @return taalvaardigheden
	 */
	public List<Taalvaardigheid> getTaalvaardigheden();

	/**
	 * Vind een taalvaardigheid voor de gegegeven titel
	 * 
	 * @param titel
	 * @return taalvaardigheid
	 */
	public Taalvaardigheid getTaalvaardigheid(String titel);

	/**
	 * Eisen bij de uitstroom
	 * 
	 * @return taalvaardigheidseisen
	 */
	public List<Taalvaardigheidseis> getTaalvaardigheidsEisen(CompetentieMatrix matrix);

	/**
	 * De mogelijke taaltypes, bijvoorbeeld Nederlands of Moderne Vreemde Taal.
	 * 
	 * @return taaltypes
	 */
	public List<TaalType> getTaalTypes();

	/**
	 * Taaltype met de gegeven titel (of null indien niet bestaand)
	 * 
	 * @param titel
	 * @return taaltype
	 */
	public TaalType getTaalType(String titel);

	/**
	 * Vind de LLB taalvaardigheidseis voor gegeven taaltype en niveau v.d. uitstroom
	 * 
	 * @param taalType
	 * @param niveau
	 * @return Taalvaardigheidseis
	 */
	public TaalvaardigheidseisLlb getTaalvaardigheidseisLlb(TaalType taalType, MBONiveau niveau);

	/**
	 * Clustert de taalvaardigheidseisen bij een uitstroom per taaltype (de LLB eisen van
	 * het niveau van de uitstroom worden ook meegenomen)
	 * 
	 * @return eisen geclusterd per taaltype (ook llb)
	 */
	public Map<TaalType, List<Taalvaardigheidseis>> getTaalvaardigheidseisenClustered(
			CompetentieMatrix matrix);

	public Boolean isTaalInGebruik(ModerneTaal taal);

	public List<ModerneTaal> getTalen(Verbintenis verbintenis);
}
