package nl.topicus.eduarte.web.components.panels.criteria;

import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.eduarte.entities.bpv.BPVCriteria;
import nl.topicus.eduarte.zoekfilters.bpv.BPVCriteriaZoekFilter;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * @author schimmel
 */
public class BPVCriteriaZoekFilterPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public BPVCriteriaZoekFilterPanel(String id, BPVCriteriaZoekFilter filter,
			final CustomDataPanel<BPVCriteria> datapanel)
	{
		super(id, new CompoundPropertyModel<BPVCriteriaZoekFilter>(filter));
		setRenderBodyOnly(true);
		Form<BPVCriteriaZoekFilter> form = new Form<BPVCriteriaZoekFilter>("form")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				datapanel.setCurrentPage(0);
			}
		};
		form.add(new TextField<String>("naam"));
		form.add(new JaNeeCombobox("actief"));
		add(form);
	}
}
