package nl.topicus.eduarte.web.components.choice;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.CohortDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox met alle landelijk gedefinieerde cohorten.
 * 
 * @author loite
 */
public class CohortCombobox extends AbstractAjaxDropDownChoice<Cohort>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Cohort>>
	{
		private static final long serialVersionUID = 1L;

		private Date beginDatum;

		private Date eindDatum;

		public ListModel(Date beginDatum, Date eindDatum)
		{
			this.beginDatum = beginDatum;
			this.eindDatum = eindDatum;
		}

		@Override
		protected List<Cohort> load()
		{
			Date peildatum = EduArteContext.get().getPeildatum();
			Date showBeginDatum = beginDatum;
			if (beginDatum == null && peildatum != null)
				showBeginDatum = TimeUtil.getInstance().addYears(peildatum, -10);

			Date showEindDatum = eindDatum;
			if (eindDatum == null && peildatum != null)
				showEindDatum = TimeUtil.getInstance().addYears(peildatum, 5);

			return DataAccessRegistry.getHelper(CohortDataAccessHelper.class).list(showBeginDatum,
				showEindDatum);
		}
	}

	public CohortCombobox(String id)
	{
		this(id, null);
	}

	public CohortCombobox(String id, IModel<Cohort> model)
	{
		this(id, model, null, null);
	}

	public CohortCombobox(String id, IModel<Cohort> model, Date beginDatum, Date eindDatum)
	{
		super(id, model, new ListModel(beginDatum, eindDatum), new ToStringRenderer());
	}

	public Cohort getCohort()
	{
		return getModelObject();
	}
}
