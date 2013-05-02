package nl.topicus.eduarte.krd.web.validators;

import java.util.Date;

import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.krd.bron.BronBpvWijzigingToegestaanCheck;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.IModel;

public class BronBPVMutatieToegestaanValidator extends AbstractFormValidator
{
	private static final long serialVersionUID = 1L;

	private DatumField afsluitDatumField;

	private Date oudeBegindatum;

	private Date oudeAfsluitdatum;

	private Long oudeBpvBedrijfId;

	private BPVStatus status;

	private IModel<BPVInschrijving> bpvModel;

	public BronBPVMutatieToegestaanValidator(DatumField afsluitDatumField, Date oudeBegindatum,
			Date oudeAfsluitdatum, Long oudeBpvBedrijfId, BPVStatus status,
			IModel<BPVInschrijving> bpvModel)
	{
		this.afsluitDatumField = afsluitDatumField;
		this.oudeBegindatum = oudeBegindatum;
		this.oudeAfsluitdatum = oudeAfsluitdatum;
		this.oudeBpvBedrijfId = oudeBpvBedrijfId;
		this.status = status;
		this.bpvModel = bpvModel;
	}

	@Override
	public FormComponent< ? >[] getDependentFormComponents()
	{
		return new FormComponent[] {afsluitDatumField};
	}

	@Override
	public void validate(Form< ? > form)
	{
		BPVInschrijving bpv = bpvModel.getObject();
		if (BPVStatus.Voorlopig != bpv.getStatus()
			|| (status != null && status.isBronCommuniceerbaar()))
		{
			BronBpvWijzigingToegestaanCheck check =
				new BronBpvWijzigingToegestaanCheck(oudeBegindatum, oudeAfsluitdatum,
					oudeBpvBedrijfId, status, bpv, bpv.getVerbintenis());
			if (!check.isWijzigingToegestaan())
				error(afsluitDatumField);
		}
	}
}