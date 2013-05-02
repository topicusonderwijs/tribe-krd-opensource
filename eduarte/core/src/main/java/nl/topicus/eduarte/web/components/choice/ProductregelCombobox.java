package nl.topicus.eduarte.web.components.choice;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.productregel.Productregel;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Drop down voor productregels van een opleiding.
 * 
 * @author loite
 */
public class ProductregelCombobox extends AbstractAjaxDropDownChoice<Productregel>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Productregel>>
	{
		private static final long serialVersionUID = 1L;

		private final IModel<Opleiding> opleidingModel;

		private final IModel<Cohort> cohortModel;

		private final IModel< ? extends List<Productregel>> excludeModel;

		private ListModel(Opleiding opleiding, Cohort cohort,
				IModel< ? extends List<Productregel>> excludeModel)
		{
			this.opleidingModel = ModelFactory.getModel(opleiding);
			this.cohortModel = ModelFactory.getModel(cohort);
			this.excludeModel = excludeModel;
		}

		@Override
		protected List<Productregel> load()
		{
			List<Productregel> res = getOpleiding().getLandelijkeEnLokaleProductregels(getCohort());
			for (Productregel regel : getExcludedProductregels())
			{
				res.remove(regel);
			}
			return res;
		}

		private Opleiding getOpleiding()
		{
			return opleidingModel.getObject();
		}

		private Cohort getCohort()
		{
			return cohortModel.getObject();
		}

		private List<Productregel> getExcludedProductregels()
		{
			if (excludeModel != null)
				return excludeModel.getObject();
			return Collections.emptyList();
		}

		@Override
		protected void onDetach()
		{
			super.onDetach();
			ComponentUtil.detachQuietly(opleidingModel);
			ComponentUtil.detachQuietly(cohortModel);
			ComponentUtil.detachQuietly(excludeModel);
		}

	}

	public ProductregelCombobox(String id, Opleiding opleiding, Cohort cohort)
	{
		this(id, null, opleiding, cohort);
	}

	public ProductregelCombobox(String id, IModel<Productregel> model, Opleiding opleiding,
			Cohort cohort)
	{
		this(id, model, opleiding, cohort, null);
	}

	/**
	 * @param excludeModel
	 *            Model die een lijst van productregels oplevert die NIET getoond moeten
	 *            worden in de combobox.
	 */
	public ProductregelCombobox(String id, IModel<Productregel> model, Opleiding opleiding,
			Cohort cohort, IModel< ? extends List<Productregel>> excludeModel)
	{
		super(id, model, new ListModel(opleiding, cohort, excludeModel), new ToStringRenderer());
	}
}
