package nl.topicus.eduarte.web.pages.beheer.organisatie;

import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BPVBedrijfsgegevenTable;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class BPVBedrijfsgegevenOverzichtPanel extends TypedPanel<List<BPVBedrijfsgegeven>>
{
	private static final long serialVersionUID = 1L;

	WebMarkupContainer data;

	public BPVBedrijfsgegevenOverzichtPanel(String id, IModel<List<BPVBedrijfsgegeven>> model)
	{
		super(id, model);

		CustomDataPanel<BPVBedrijfsgegeven> datapanel =
			new EduArteDataPanel<BPVBedrijfsgegeven>("datapanel",
				new ListModelDataProvider<BPVBedrijfsgegeven>(model), new BPVBedrijfsgegevenTable(
					false));

		add(datapanel);
	}

}
