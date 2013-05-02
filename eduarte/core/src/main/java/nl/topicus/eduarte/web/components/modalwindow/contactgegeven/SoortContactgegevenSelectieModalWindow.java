package nl.topicus.eduarte.web.components.modalwindow.contactgegeven;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.panels.filter.CodeNaamActiefZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.SoortContactgegevenZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

public class SoortContactgegevenSelectieModalWindow extends
		AbstractZoekenModalWindow<SoortContactgegeven>
{
	private static final long serialVersionUID = 1L;

	private SoortContactgegevenZoekFilter filter;

	public SoortContactgegevenSelectieModalWindow(String id, IModel<SoortContactgegeven> model,
			SoortContactgegevenZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Soort contactgegeven selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<SoortContactgegeven> createContents(String id)
	{
		return new SoortContactgegevenSelectiePanel(id, this, filter)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void fillBottomRow(BottomRowPanel panel)
			{
				panel.addButton(new AjaxAnnulerenButton(panel)
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onClick(AjaxRequestTarget target)
					{
						getModalWindow().setDefaultModelObject(null);
						getModalWindow().close(target);
					}
				});
			}

			@Override
			protected Component createFilterPanel(String filterId,
					SoortContactgegevenZoekFilter zoekfilter,
					CustomDataPanel<SoortContactgegeven> datapanel)
			{

				CodeNaamActiefZoekFilterPanel panel =
					new CodeNaamActiefZoekFilterPanel(filterId, zoekfilter, datapanel);

				panel.setVisibilityAllowed(false);
				return panel;
			}

		};
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
