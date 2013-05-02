package nl.topicus.eduarte.web.components.choice;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Drop down voor bpvBedrijfsgegevens.
 * 
 * @author vandekamp
 */
public class BPVBedrijfsgegevenCombobox extends AbstractAjaxDropDownChoice<BPVBedrijfsgegeven>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<BPVBedrijfsgegeven>>
	{
		private static final long serialVersionUID = 1L;

		private final IModel<ExterneOrganisatie> externeOrganisatieModel;

		private ListModel(IModel<ExterneOrganisatie> externeOrganisatieModel)
		{
			this.externeOrganisatieModel = externeOrganisatieModel;
		}

		@Override
		protected List<BPVBedrijfsgegeven> load()
		{
			if (getExterneOrganisatie() != null)
				return getExterneOrganisatie().getBpvBedrijfsgegevens();
			return new ArrayList<BPVBedrijfsgegeven>();
		}

		private ExterneOrganisatie getExterneOrganisatie()
		{
			return externeOrganisatieModel.getObject();
		}

		@Override
		protected void onDetach()
		{
			ComponentUtil.detachQuietly(externeOrganisatieModel);
		}

	}

	public BPVBedrijfsgegevenCombobox(String id, IModel<BPVBedrijfsgegeven> model,
			IModel<ExterneOrganisatie> externeOrganisatieModel)
	{
		super(id, model, new ListModel(externeOrganisatieModel), new EntiteitPropertyRenderer(
			"kenniscentrum.naam"));
	}
}
