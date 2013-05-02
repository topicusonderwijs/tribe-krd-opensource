package nl.topicus.eduarte.krdparticipatie.web.components.panels.filter;

import java.math.BigDecimal;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.zoekfilters.DeelnemerVerzuimloketZoekfilter;
import nl.topicus.onderwijs.ibgverzuimloket.model.IbgEnums;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

public class DeelnemerVerzuimloketZoekfilterPanel extends
		TypedPanel<DeelnemerVerzuimloketZoekfilter>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerVerzuimloketZoekfilterPanel(String id, DeelnemerVerzuimloketZoekfilter filter)
	{
		super(id, new CompoundPropertyModel<DeelnemerVerzuimloketZoekfilter>(filter));
		setRenderBodyOnly(true);
		Form<Void> form = new Form<Void>("form")
		{
			private static final long serialVersionUID = 1L;

		};
		add(form);

		form.add(new TextField<BigDecimal>("meldingsnummer"));
		form.add(new DatumField("vanafDatum"));
		form.add(new DatumField("tmDatum"));
		form.add(new EnumCombobox<IbgEnums.StatusMeldingRelatiefVerzuim>("status",
			IbgEnums.StatusMeldingRelatiefVerzuim.values()));
		form.add(new EnumCombobox<IbgEnums.Verzuimsoort>("verzuimsoort", IbgEnums.Verzuimsoort
			.values()));

	}
}
