package nl.topicus.eduarte.krd.web.components.panels.verbintenis;

import java.math.BigDecimal;
import java.util.Date;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.behaviors.AjaxFormComponentSaveBehaviour;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumBPVValidator;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumValidatorMode;
import nl.topicus.eduarte.web.components.choice.RedenUitschrijvingComboBox;
import nl.topicus.eduarte.zoekfilters.RedenUitschrijvingZoekFilter.SoortRedenUitschrijvingTonen;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

public class BPVBeeindigenPanel extends TypedPanel<BPVInschrijving>
{
	private static final long serialVersionUID = 1L;

	private IModel<Date> verbintenisEinddatumModel;

	public BPVBeeindigenPanel(String id, IModel<BPVInschrijving> bpvModel,
			IModel<Date> verbintenisEinddatumModel)
	{
		super(id, bpvModel);
		this.verbintenisEinddatumModel = verbintenisEinddatumModel;
		createBPVGegevensEditVelden();
	}

	private void createBPVGegevensEditVelden()
	{
		// readonly velden
		add(new Label("BPVBedrijf", getBpvBedrijf().getNaam()));
		add(new Label("totaleOmvang"));
		add(new Label("begindatum"));
		add(new Label("verwachteEinddatum"));

		// edit velden

		RedenUitschrijvingComboBox redenUitschrijvingComboBox =
			new RedenUitschrijvingComboBox("redenUitschrijving", SoortRedenUitschrijvingTonen.BPV);
		redenUitschrijvingComboBox.add(new AjaxFormComponentSaveBehaviour());
		redenUitschrijvingComboBox.setRequired(true);
		redenUitschrijvingComboBox.setLabel(new Model<String>("Reden beëindigen BPV"));
		add(redenUitschrijvingComboBox);

		DatumField einddatum = new DatumField("einddatum");
		einddatum.setRequired(true);
		einddatum.setLabel(new Model<String>("Einddatum BPV"));
		einddatum.add(new AjaxFormComponentUpdatingBehavior("onblur")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				berekenOmvang();
				target.addComponent(get("gerealiseerdeOmvang"));
			}
		});
		einddatum.add(new AbstractValidator<Date>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate(IValidatable<Date> validatable)
			{
				Date datum = validatable.getValue();
				BPVInschrijving inschrijving = getBPV();
				if (datum.before(inschrijving.getBegindatum()))
				{
					ValidationError error = new ValidationError();
					error
						.setMessage("Er is een BPV-inschrijving met een begindatum voor de ingevoerde einddatum");
					validatable.error(error);
				}
			}
		});

		einddatum.add(new AbstractValidator<Date>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate(IValidatable<Date> validatable)
			{
				Date datum = validatable.getValue();
				Date einddatumVerbintenis = verbintenisEinddatumModel.getObject();

				if (datum != null && einddatumVerbintenis != null
					&& datum.after(einddatumVerbintenis))
				{
					ValidationError error = new ValidationError();
					error
						.setMessage("Er is een BPV-inschrijving met een einddatum na de einddatum van de verbintenis.");
					validatable.error(error);
				}
			}
		});

		einddatum.add(new BlokkadedatumBPVValidator<Date>(getModel(),
			BlokkadedatumValidatorMode.Beeindigen));

		TextField<Integer> gerealiseerdeOmvang = new TextField<Integer>("gerealiseerdeOmvang");
		ComponentUtil.fixLength(gerealiseerdeOmvang, BPVInschrijving.class);
		gerealiseerdeOmvang.setOutputMarkupId(true);
		add(gerealiseerdeOmvang);
		add(redenUitschrijvingComboBox);
		add(einddatum);
	}

	private void berekenOmvang()
	{
		Date einddatum = getBPV().getEinddatum();
		Date beginDatum = getBPV().getBegindatum();
		int dagen = TimeUtil.getInstance().getDifferenceInDays(einddatum, beginDatum);
		// +3 ivm afronding
		int weken = (dagen + 3) / 7;

		int gerealiseerdeOmvang = 0;
		BigDecimal urenPerWeek = getBPV().getUrenPerWeek();
		Integer dagenPerWeek = getBPV().getDagenPerWeek();
		if (urenPerWeek != null || dagenPerWeek != null)
		{
			if (urenPerWeek == null)
				urenPerWeek = BigDecimal.valueOf(dagenPerWeek * 8);

			double urenPerWeekDouble = urenPerWeek == null ? 0 : urenPerWeek.doubleValue();
			// 40 weken actief per jaar, +26 ivm afronding
			gerealiseerdeOmvang = (int) (weken * 40 * urenPerWeekDouble + 26) / 52;
			getBPV().setGerealiseerdeOmvang(gerealiseerdeOmvang);
			if (getBPV().getGerealiseerdeOmvang() != null
				&& getBPV().getGerealiseerdeOmvang() > 5120)
			{
				getBPV().setGerealiseerdeOmvang(5120);
			}
		}
		else
			getBPV().setGerealiseerdeOmvang(getBPV().getTotaleOmvang());

	}

	private BPVInschrijving getBPV()
	{
		return getModelObject();
	}

	private ExterneOrganisatie getBpvBedrijf()
	{
		return getBPV().getBpvBedrijf();
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();

		Verbintenis verbintenis = getBPV().getVerbintenis();
		RedenUitschrijving reden = verbintenis.getRedenUitschrijving();

		if (reden != null && reden.isTonenBijBPV())
			getBPV().setRedenUitschrijving(reden);

		getBPV().setEinddatum(verbintenis.getEinddatum());
	}
}