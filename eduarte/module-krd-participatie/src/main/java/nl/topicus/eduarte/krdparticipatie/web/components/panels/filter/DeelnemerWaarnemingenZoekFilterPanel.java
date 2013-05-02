package nl.topicus.eduarte.krdparticipatie.web.components.panels.filter;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.participatie.web.components.choice.combobox.WaarnemingSoortComboBox;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingZoekFilter;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * @author vandekamp
 */
public class DeelnemerWaarnemingenZoekFilterPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public DeelnemerWaarnemingenZoekFilterPanel(String id, WaarnemingZoekFilter filter,
			final IPageable pageable, OrganisatieEenheid organisatieEenheid)
	{
		super(id);
		Form<WaarnemingZoekFilter> form =
			new Form<WaarnemingZoekFilter>("form", new CompoundPropertyModel<WaarnemingZoekFilter>(
				filter))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit()
				{
					WaarnemingZoekFilter waarnemingZoekFilter = getModelObject();
					waarnemingZoekFilter.setEindDatumTijd(TimeUtil.getInstance()
						.maakEindeVanDagVanDatum(waarnemingZoekFilter.getEindDatumTijd()));
					pageable.setCurrentPage(0);
				}
			};
		add(form);

		WaarnemingSoortComboBox waarnemingSoortComboBox =
			new WaarnemingSoortComboBox("waarnemingSoort", null, false, organisatieEenheid, false);
		waarnemingSoortComboBox.setNullValid(true);
		form.add(waarnemingSoortComboBox);
		form.add(new DatumField("beginDatumTijd"));
		form.add(new DatumField("eindDatumTijd"));
		form.add(new JaNeeCombobox("afgehandeld").setNullValid(true));
	}

}
