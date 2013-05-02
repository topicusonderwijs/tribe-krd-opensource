package nl.topicus.eduarte.web.components.panels.criteria;

import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.bpv.BPVCriteriaOnderwijsproduct;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BPVCriteriaOnderwijsproductTable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class BPVCriteriaOnderwijsproductOverzichtPanel extends
		TypedPanel<List<BPVCriteriaOnderwijsproduct>>
{
	private static final long serialVersionUID = 1L;

	WebMarkupContainer data;

	public BPVCriteriaOnderwijsproductOverzichtPanel(String id,
			IModel<List<BPVCriteriaOnderwijsproduct>> model)
	{
		super(id, model);

		EduArteDataPanel<BPVCriteriaOnderwijsproduct> datapanel =
			new EduArteDataPanel<BPVCriteriaOnderwijsproduct>("datapanel",
				new ListModelDataProvider<BPVCriteriaOnderwijsproduct>(model),
				new BPVCriteriaOnderwijsproductTable());

		add(datapanel);
	}
}
