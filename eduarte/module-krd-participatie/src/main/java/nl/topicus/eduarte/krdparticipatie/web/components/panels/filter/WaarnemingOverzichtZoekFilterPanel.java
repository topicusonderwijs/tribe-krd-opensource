package nl.topicus.eduarte.krdparticipatie.web.components.panels.filter;

import java.util.List;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.LesuurIndeling;
import nl.topicus.eduarte.entities.participatie.enums.AbsentiePresentieEnum;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingWeergaveEnum;
import nl.topicus.eduarte.krdparticipatie.util.LestijdUtil;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.WaarnemingOverzichtInterface;
import nl.topicus.eduarte.participatie.web.components.choice.combobox.LesUurComboBox;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingOverzichtZoekFilter;
import nl.topicus.eduarte.web.components.quicksearch.contract.ContractSearchEditor;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author vandekamp
 */
public class WaarnemingOverzichtZoekFilterPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private Form<WaarnemingOverzichtZoekFilter> form;

	public WaarnemingOverzichtZoekFilterPanel(String id, WaarnemingOverzichtZoekFilter filter,
			final WaarnemingOverzichtInterface page, boolean toonEinddatum,
			OrganisatieEenheid organisatieEenheid, Locatie locatie)
	{
		super(id);

		List<LesuurIndeling> lestijden = LestijdUtil.getLestijden(organisatieEenheid, locatie);

		if (lestijden != null && lestijden.size() > 0)
		{
			filter.setVanafLesuur(lestijden.get(0));
			filter.setTotLesuur(lestijden.get(lestijden.size() - 1));
		}
		form =
			new Form<WaarnemingOverzichtZoekFilter>("form",
				new CompoundPropertyModel<WaarnemingOverzichtZoekFilter>(filter))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit()
				{
					page.submitFilter();
				}
			};
		add(form);
		if (toonEinddatum)
			form.add(new Label("vanafDatum", "Vanaf datum"));
		else
			form.add(new Label("vanafDatum", "Datum"));
		form.add(new Label("tmDatum", "t/m datum").setVisible(toonEinddatum));
		DatumField beginDatum = new DatumField("beginDatum");
		beginDatum.setRequired(true);
		form.add(beginDatum);
		DatumField eindDatum = new DatumField("eindDatum");
		eindDatum.setRequired(toonEinddatum);
		eindDatum.setVisible(toonEinddatum);
		form.add(eindDatum);
		form.add(new LesUurComboBox("vanafLesuur", null, organisatieEenheid, locatie)
			.setNullValid(true));
		form.add(new LesUurComboBox("totLesuur", null, organisatieEenheid, locatie)
			.setNullValid(true));
		form.add(new EnumCombobox<AbsentiePresentieEnum>("absentieOfPresentie",
			AbsentiePresentieEnum.values()));
		form.add(new EnumCombobox<WaarnemingWeergaveEnum>("waarnemingWeergave",
			WaarnemingWeergaveEnum.values()));
		form.add(new ContractSearchEditor("contract", new PropertyModel<Contract>(form
			.getModelObject(), "contract")));
		form.add(new JaNeeCombobox("toonLegeRegels").setNullValid(true).setRequired(true));
		form.add(new JaNeeCombobox("toonTotalenKolommen").setNullValid(true).setRequired(true));
	}

	public IModel<WaarnemingOverzichtZoekFilter> getFilterModel()
	{
		return form.getModel();
	}

}
