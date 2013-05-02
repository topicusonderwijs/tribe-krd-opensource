package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;
import nl.topicus.eduarte.entities.codenaamactief.ICodeNaamActiefEntiteit;
import nl.topicus.eduarte.entities.onderwijsproduct.*;
import nl.topicus.eduarte.entities.opleiding.Team;
import nl.topicus.eduarte.entities.organisatie.SoortExterneOrganisatie;
import nl.topicus.eduarte.zoekfilters.ICodeNaamActiefZoekFilter;

/**
 * @author vandekamp
 * @param <T>
 */
public interface CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper<T extends ICodeNaamActiefEntiteit, ZF extends ICodeNaamActiefZoekFilter<T>>
		extends BatchZoekFilterDataAccessHelper<T, ZF>
{
	/**
	 * @param code
	 * @return Het {@link CodeNaamActiefInstellingEntiteit} met de gegeven code bij de
	 *         gegeven instelling
	 */
	public <R extends T> R get(String code, Class<R> entityClass);

	/**
	 * @param leerstijl
	 * @return of de leerstijl in gebruik is
	 */
	public boolean isLeerstijlInGebruik(Leerstijl leerstijl);

	/**
	 * @param soortPraktijklokaal
	 * @return of het soortpraktijklokaal in gebruik is.
	 */
	public boolean isSoortPraktijklokaalInGebruik(SoortPraktijklokaal soortPraktijklokaal);

	/**
	 * @param typeLocatie
	 * @return of het TypeLocatie in gebruik is.
	 */
	public boolean isTypeLocatieInGebruik(TypeLocatie typeLocatie);

	/**
	 * @param typeToets
	 * @return of het TypeToets in gebruik is.
	 */
	public boolean isTypeToetsInGebruik(TypeToets typeToets);

	public boolean isGebruiksmiddelInGebruik(Gebruiksmiddel gebruiksmiddel);

	public boolean isVerbruiksmiddelInGebruik(Verbruiksmiddel verbruiksmiddel);

	public boolean isAggregatieniveauInGebruik(Aggregatieniveau aggregatieniveau);

	public boolean isOnderwijsproductNiveauInGebruik(OnderwijsproductNiveauaanduiding niveau);

	public boolean isTeamInGebruik(Team team);

	/**
	 * 
	 * @param niveau
	 * @return Het aggregatieniveau met het gegeven niveau.
	 */
	public Aggregatieniveau getAggregatieniveau(int niveau);

	/**
	 * @param soortOnderwijsproduct
	 * @return of het soort onderwijsproduct in gebruik is
	 */
	public boolean isSoortOnderwijsproductInGebruik(SoortOnderwijsproduct soortOnderwijsproduct);

	/**
	 * @param soortExterneOrganisatie
	 * @return of het soort externe organisatie in gebruik is
	 */
	public boolean isSoortExterneOrganisatieInGebruik(
			SoortExterneOrganisatie soortExterneOrganisatie);

}
