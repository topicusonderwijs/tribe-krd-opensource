package nl.topicus.eduarte.krd.web.components.panels.examen;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.dao.helpers.ToegestaneExamenstatusOvergangDataAccessHelper;
import nl.topicus.eduarte.entities.examen.ExamenWorkflow;
import nl.topicus.eduarte.entities.examen.ToegestaneExamenstatusOvergang;
import nl.topicus.eduarte.zoekfilters.ToegestaneExamenstatusOvergangZoekFilter;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

public class ExamenWorkflowPanel extends TypedPanel<ExamenWorkflow>
{
	private static final long serialVersionUID = 1L;

	public ExamenWorkflowPanel(String id, ExamenWorkflow examenWorkflow)
	{
		super(id, ModelFactory.getCompoundModel(examenWorkflow));
		add(new Label("naam"));
		RepeatingView examenstatussen = new RepeatingView("examenstatussen");
		for (ToegestaneExamenstatusOvergang toegestaneExOvergang : getStatusOvergangen(examenWorkflow))
		{
			WebMarkupContainer container = new WebMarkupContainer(examenstatussen.newChildId());
			container.add(new OvergangLinkPanel("linkPanel", ModelFactory
				.getModel(toegestaneExOvergang)));
			examenstatussen.add(container);
		}
		for (Panel panel : getExtraPanels("linkPanel"))
		{
			WebMarkupContainer container = new WebMarkupContainer(examenstatussen.newChildId());
			container.add(panel);
			examenstatussen.add(container);
		}
		add(examenstatussen);
	}

	protected List< ? extends Panel> getExtraPanels(@SuppressWarnings("unused") String id)
	{
		return new ArrayList<Panel>();
	}

	public ExamenWorkflow getExamenWorkflow()
	{
		return getModelObject();
	}

	private List<ToegestaneExamenstatusOvergang> getStatusOvergangen(ExamenWorkflow examenWorkflow)
	{
		ToegestaneExamenstatusOvergangZoekFilter filter =
			new ToegestaneExamenstatusOvergangZoekFilter();
		filter.setExamenWorkflow(examenWorkflow);
		filter.addOrderByProperty("volgnummer");
		ToegestaneExamenstatusOvergangDataAccessHelper helper =
			DataAccessRegistry.getHelper(ToegestaneExamenstatusOvergangDataAccessHelper.class);
		List<ToegestaneExamenstatusOvergang> overgangen = helper.list(filter);
		return overgangen;
	}

}