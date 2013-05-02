package nl.topicus.eduarte.krd.web.validators;

import java.util.Date;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.krd.bron.BronVerbintenisWijzigingToegestaanCheck;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.IModel;

public class BronVerbintenisMutatieToegestaanValidator extends AbstractFormValidator
{
	private static final long serialVersionUID = 1L;

	private FormComponent< ? > dependentComponent;

	private Date oudeBegindatum;

	private Date oudeEinddatum;

	private VerbintenisStatus oudeStatus;

	private Bekostigd oudeBekostigd;

	private Intensiteit oudeIntensiteit;

	private IModel<Opleiding> oudeOpleidingModel;

	private IModel<Verbintenis> verbintenisModel;

	public BronVerbintenisMutatieToegestaanValidator(FormComponent< ? > dependentComponent,
			Date oudeBegindatum, Date oudeEinddatum, VerbintenisStatus oudeStatus,
			IModel<Opleiding> oudeOpleiding, Intensiteit oudeIntensiteit, Bekostigd oudeBekostigd,
			IModel<Verbintenis> verbintenisModel)
	{
		this.dependentComponent = dependentComponent;
		this.oudeBegindatum = oudeBegindatum;
		this.oudeEinddatum = oudeEinddatum;
		this.oudeStatus = oudeStatus;
		this.oudeOpleidingModel = oudeOpleiding;
		this.oudeIntensiteit = oudeIntensiteit;
		this.oudeBekostigd = oudeBekostigd;
		this.verbintenisModel = verbintenisModel;
	}

	@Override
	public FormComponent< ? >[] getDependentFormComponents()
	{
		return new FormComponent[] {dependentComponent};
	}

	@Override
	public void validate(Form< ? > form)
	{
		Verbintenis verbintenis = verbintenisModel.getObject();

		if ((verbintenis.getStatus() != null && verbintenis.isBronCommuniceerbaar())
			|| (oudeStatus != null && oudeStatus.isBronCommuniceerbaar()))
		{
			BronVerbintenisWijzigingToegestaanCheck check =
				new BronVerbintenisWijzigingToegestaanCheck(oudeBegindatum, oudeEinddatum,
					oudeStatus, oudeOpleidingModel.getObject(), oudeIntensiteit, oudeBekostigd,
					verbintenis);
			if (!check.isWijzigingToegestaan())
			{
				error(dependentComponent);
			}
		}
	}
}