package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.web.components.choice.renderer.VerbintenisRenderer;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author Henzen
 */
public class VerbintenisCombobox extends AbstractAjaxDropDownChoice<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Verbintenis>>
	{
		private static final long serialVersionUID = 1L;

		private IModel<Deelnemer> deelnemerModel;

		public ListModel(IModel<Deelnemer> deelnemerModel)
		{
			this.deelnemerModel = deelnemerModel;
		}

		@Override
		protected List<Verbintenis> load()
		{
			Deelnemer deelnemer = deelnemerModel.getObject();
			return DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class)
				.getVerbintenissenByDeelnemer(deelnemer);
		}

		@Override
		protected void onDetach()
		{
			ComponentUtil.detachQuietly(deelnemerModel);
			super.onDetach();
		}

	}

	public VerbintenisCombobox(String id, IModel<Verbintenis> model,
			IModel<Deelnemer> deelnemerModel)
	{
		super(id, model, new ListModel(deelnemerModel), new VerbintenisRenderer());
		setNullValid(true);
	}
}
