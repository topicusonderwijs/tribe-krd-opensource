package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.Productregel.TypeProductregel;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;

import org.apache.wicket.model.IModel;

/**
 * Zoekfilter voor productregels.
 * 
 * @author loite
 */
public class ProductregelZoekFilter extends AbstractLandelijkOfInstellingZoekFilter<Productregel>
{
	private static final long serialVersionUID = 1L;

	private IModel<Opleiding> opleiding;

	private IModel<Verbintenisgebied> verbintenisgebied;

	private IModel<Cohort> cohort;

	private TypeProductregel typeProductregel;

	/**
	 * @param opleiding
	 *            Mag niet null zijn.
	 * @param cohort
	 *            Mag niet null zijn.
	 */
	public ProductregelZoekFilter(Opleiding opleiding, Cohort cohort)
	{
		Asserts.assertNotNull("opleiding", opleiding);
		Asserts.assertNotNull("cohort", cohort);
		setOpleiding(opleiding);
		setCohort(cohort);
		setDefaultSortOrder();
	}

	/**
	 * 
	 * @param opleiding
	 *            Mag niet null zijn
	 * @param cohortModel
	 *            Mag niet null zijn
	 */
	public ProductregelZoekFilter(Opleiding opleiding, IModel<Cohort> cohortModel)
	{
		Asserts.assertNotNull("opleiding", opleiding);
		Asserts.assertNotNull("cohortModel", cohortModel);
		Asserts.assertNotNull("cohortModel.object", cohortModel.getObject());
		setOpleiding(opleiding);
		this.cohort = cohortModel;
		setDefaultSortOrder();
	}

	/**
	 * @param verbintenisgebied
	 *            Mag niet null zijn
	 * @param cohort
	 *            Mag niet null zijn
	 */
	public ProductregelZoekFilter(Verbintenisgebied verbintenisgebied, Cohort cohort)
	{
		Asserts.assertNotNull("verbintenisgebied", verbintenisgebied);
		Asserts.assertNotNull("cohort", cohort);
		setVerbintenisgebied(verbintenisgebied);
		setCohort(cohort);
		setDefaultSortOrder();
	}

	/**
	 * @param verbintenisgebied
	 *            Mag niet null zijn
	 * @param cohortModel
	 *            Mag niet null zijn
	 */
	public ProductregelZoekFilter(Verbintenisgebied verbintenisgebied, IModel<Cohort> cohortModel)
	{
		Asserts.assertNotNull("verbintenisgebied", verbintenisgebied);
		Asserts.assertNotNull("cohortModel", cohortModel);
		Asserts.assertNotNull("cohortModel.object", cohortModel.getObject());
		setVerbintenisgebied(verbintenisgebied);
		this.cohort = cohortModel;
		setDefaultSortOrder();
	}

	private void setDefaultSortOrder()
	{
		addOrderByProperty("volgnummer");
		addOrderByProperty("soortProductregel.volgnummer");
	}

	public Opleiding getOpleiding()
	{
		return getModelObject(opleiding);
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}

	public Verbintenisgebied getVerbintenisgebied()
	{
		return getModelObject(verbintenisgebied);
	}

	public void setVerbintenisgebied(Verbintenisgebied verbintenisgebied)
	{
		this.verbintenisgebied = makeModelFor(verbintenisgebied);
	}

	public Cohort getCohort()
	{
		return getModelObject(cohort);
	}

	public void setCohort(Cohort cohort)
	{
		if (this.cohort != null)
			this.cohort.setObject(cohort);
		else
			this.cohort = makeModelFor(cohort);
	}

	public TypeProductregel getTypeProductregel()
	{
		return typeProductregel;
	}

	public void setTypeProductregel(TypeProductregel typeProductregel)
	{
		this.typeProductregel = typeProductregel;
	}
}
