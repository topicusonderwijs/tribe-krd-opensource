package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.SoortOnderwijsproduct;

/**
 * @author vandekamp
 */
public interface SoortOnderwijsproductDataAccessHelper extends
		BatchDataAccessHelper<SoortOnderwijsproduct>
{

	/**
	 * @return actieve soorten onderwijsproducten bij de gegeven instelling
	 */
	public List<SoortOnderwijsproduct> list();

	/**
	 * @param code
	 * @return Het soort onderwijsproduct met de gegeven code bij de gegeven instelling
	 */
	public SoortOnderwijsproduct get(String code);

}
