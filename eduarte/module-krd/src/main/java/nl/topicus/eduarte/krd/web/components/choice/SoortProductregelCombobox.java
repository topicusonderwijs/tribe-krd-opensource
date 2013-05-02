package nl.topicus.eduarte.krd.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.helpers.SoortProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.productregel.SoortProductregel;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Drop down voor soorten productregels.
 * 
 * @author loite
 */
public class SoortProductregelCombobox extends AbstractAjaxDropDownChoice<SoortProductregel>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<SoortProductregel>>
	{
		private static final long serialVersionUID = 1L;

		private final IModel<Taxonomie> taxonomieModel;

		private ListModel(IModel<Taxonomie> taxonomieModel)
		{
			this.taxonomieModel = taxonomieModel;
		}

		@Override
		protected List<SoortProductregel> load()
		{
			return DataAccessRegistry.getHelper(SoortProductregelDataAccessHelper.class).list(
				getTaxonomie());
		}

		private Taxonomie getTaxonomie()
		{
			return taxonomieModel.getObject();
		}

		@Override
		protected void onDetach()
		{
			ComponentUtil.detachQuietly(taxonomieModel);
		}

	}

	public SoortProductregelCombobox(String id, IModel<SoortProductregel> model,
			IModel<Taxonomie> taxonomieModel)
	{
		super(id, model, new ListModel(taxonomieModel), new ToStringRenderer());
	}
}
