package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.SoortOnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.SoortOnderwijsproduct;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 */
public class SoortOnderwijsproductCombobox extends
		AbstractAjaxDropDownChoice<SoortOnderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends
			LoadableDetachableModel<List<SoortOnderwijsproduct>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<SoortOnderwijsproduct> load()
		{
			return DataAccessRegistry.getHelper(SoortOnderwijsproductDataAccessHelper.class).list();
		}
	}

	public SoortOnderwijsproductCombobox(String id)
	{
		super(id, new ListModel(), new ChoiceRenderer<SoortOnderwijsproduct>("naam", "id"));
	}

	public SoortOnderwijsproductCombobox(String id, IModel<SoortOnderwijsproduct> model)
	{
		super(id, model, new ListModel(), new ChoiceRenderer<SoortOnderwijsproduct>("naam", "id"));
	}
}
