package nl.topicus.eduarte.dao.helpers;

import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.zoekfilters.ProductregelZoekFilter;

public interface ProductregelDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Productregel, ProductregelZoekFilter>
{

	public boolean isUnique(IdObject object, Map<String, Object> properties, Opleiding opleiding);

	public Productregel getProductRegel(String afkorting, Opleiding opleiding, Cohort cohort);

	public List<Productregel> getProductRegels(Opleiding opleiding);
}
