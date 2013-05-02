package nl.topicus.eduarte.web.components.choice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * 
 * @author vandekamp
 */
public class BPVStatusCombobox extends AbstractAjaxDropDownChoice<BPVStatus>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<BPVStatus>>
	{
		private static final long serialVersionUID = 1L;

		private IModel<BPVStatus> bpvStatusModel;

		private ListModel(IModel<BPVStatus> bpvStatusModel)
		{
			this.bpvStatusModel = bpvStatusModel;
		}

		@Override
		protected List<BPVStatus> load()
		{
			if (bpvStatusModel.getObject() != null)
			{
				BPVStatus status = bpvStatusModel.getObject();
				return new ArrayList<BPVStatus>(Arrays.asList(status.getVervolgNormaal()));
			}
			return new ArrayList<BPVStatus>();
		}

		@Override
		protected void onDetach()
		{
			ComponentUtil.detachQuietly(bpvStatusModel);
			super.onDetach();
		}
	}

	public BPVStatusCombobox(String id)
	{
		this(id, null);
	}

	public BPVStatusCombobox(String id, IModel<BPVStatus> model)
	{
		super(id, model, new ListModel(model));
	}
}
