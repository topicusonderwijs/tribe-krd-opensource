package nl.topicus.eduarte.web.components.panels.criteria;

import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.bpv.BPVCriteriaExterneOrganisatie;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BPVCriteriaExterneOrganisatieTable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class BPVCriteriaExterneOrganisatieOverzichtPanel extends
		TypedPanel<List<BPVCriteriaExterneOrganisatie>>
{
	private static final long serialVersionUID = 1L;

	WebMarkupContainer data;

	public BPVCriteriaExterneOrganisatieOverzichtPanel(String id,
			IModel<List<BPVCriteriaExterneOrganisatie>> model)
	{
		super(id, model);

		EduArteDataPanel<BPVCriteriaExterneOrganisatie> datapanel =
			new EduArteDataPanel<BPVCriteriaExterneOrganisatie>("datapanel",
				new ListModelDataProvider<BPVCriteriaExterneOrganisatie>(model),
				new BPVCriteriaExterneOrganisatieTable());

		add(datapanel);
	}
}
