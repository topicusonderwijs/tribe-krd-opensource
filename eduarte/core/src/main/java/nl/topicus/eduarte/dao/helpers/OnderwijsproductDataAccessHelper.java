package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.taxonomie.Deelgebied;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;

public interface OnderwijsproductDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Onderwijsproduct, OnderwijsproductZoekFilter>
{
	/**
	 * @param onderwijsproduct
	 * @return true indien onderwijsproductafnames of resultaatstructuur naar dit
	 *         onderwijsproduct verwijzen
	 */
	public boolean isInGebruik(Onderwijsproduct onderwijsproduct);

	/**
	 * Een lijst van onderwijsproducten die hierarchisch gezien lager staan dan de
	 * opgegeven {@link Onderwijsproduct}. De lijst bevat tevens de opgegeven
	 * {@link Onderwijsproduct}.
	 * 
	 * @param onderwijsproduct
	 * @return lijst (volgorde niet gedefinieerd)
	 */
	public List<Onderwijsproduct> getChildren(Onderwijsproduct onderwijsproduct);

	/**
	 * @param code
	 *            Onderwijsproductcode
	 * @return lijst (volgorde niet gedefinieerd)
	 */
	public List<Onderwijsproduct> getOnderwijsproductByCode(String code);

	/**
	 * Een lijst van onderwijsproducten die hierarchisch gezien lager staan dan de
	 * opgegeven {@link Onderwijsproduct}. De lijst bevat tevens de opgegeven
	 * {@link Onderwijsproduct}.
	 * 
	 * @param onderwijsproduct
	 * @return lijst (volgorde niet gedefinieerd)
	 */
	public List<Onderwijsproduct> getVoorwaarden(Onderwijsproduct onderwijsproduct);

	/**
	 * @param deelgebied
	 * @param aanbod
	 * @return Een lijst met de onderwijsproducten die gekoppeld zijn aan het gegeven
	 *         deelgebied, en die aangeboden worden op dezelfde organisatie-eenheid en
	 *         locaties als het gegeven aanbod
	 */
	public <U extends IOrganisatieEenheidLocatieKoppelEntiteit<U>> List<Onderwijsproduct> getGekoppeldeOnderwijsproducten(
			Deelgebied deelgebied, List<U> aanbod);

	/**
	 * @return Lijst met alle codes die al gebruikt worden bij de organisatie-eenheid
	 */
	public List<String> getCodes();

	/**
	 * 
	 * @param code
	 * @return het onderwijsproduct met de meegegeven code;
	 */
	public Onderwijsproduct get(String code);

	public List<Onderwijsproduct> getOnderwijsproductByTaxCode(String taxonomiecode);

}
