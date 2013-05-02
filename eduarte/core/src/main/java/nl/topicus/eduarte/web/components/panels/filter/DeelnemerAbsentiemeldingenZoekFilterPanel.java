package nl.topicus.eduarte.web.components.panels.filter;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.participatie.web.components.choice.combobox.AbsentieRedenComboBox;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieMeldingZoekFilter;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * @author loite
 */
public class DeelnemerAbsentiemeldingenZoekFilterPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param filter
	 * @param pageable
	 */
	public DeelnemerAbsentiemeldingenZoekFilterPanel(String id, AbsentieMeldingZoekFilter filter,
			final IPageable pageable)
	{
		super(id);
		Form<AbsentieMeldingZoekFilter> form =
			new Form<AbsentieMeldingZoekFilter>("form",
				new CompoundPropertyModel<AbsentieMeldingZoekFilter>(filter))
			{
				private static final long serialVersionUID = 1L;

				/**
				 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
				 */
				@Override
				public void onSubmit()
				{
					AbsentieMeldingZoekFilter absentieMeldingZoekFilter = getModelObject();
					absentieMeldingZoekFilter.setEindDatumTijd(TimeUtil.getInstance()
						.maakEindeVanDagVanDatum(absentieMeldingZoekFilter.getEindDatumTijd()));
					pageable.setCurrentPage(0);
				}
			};
		add(form);
		AbsentieRedenComboBox redenCombo = new AbsentieRedenComboBox("absentieReden", false, false);
		redenCombo.setNullValid(true);
		form.add(redenCombo);
		form.add(new DatumField("beginDatumTijd"));
		form.add(new DatumField("eindDatumTijd"));
		form.add(new JaNeeCombobox("afgehandeld").setNullValid(true));
	}

}
