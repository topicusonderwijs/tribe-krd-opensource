package nl.topicus.eduarte.krd.web.validators;

import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.krd.bron.BronVerbintenisWijzigingToegestaanCheck;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.IModel;

public class BronPlaatsingValidator extends AbstractFormValidator
{
	private static final long serialVersionUID = 1L;

	private final DatumField datumField;

	private IModel<Plaatsing> plaatsingModel;

	public BronPlaatsingValidator(DatumField datumField, IModel<Plaatsing> plaatsingModel)
	{
		this.datumField = datumField;
		this.plaatsingModel = plaatsingModel;
	}

	@Override
	public FormComponent< ? >[] getDependentFormComponents()
	{
		return new FormComponent[] {datumField};
	}

	@Override
	public void validate(Form< ? > form)
	{
		if (datumField.getDatum() != null)
		{
			Plaatsing plaatsing = plaatsingModel.getObject();
			BronVerbintenisWijzigingToegestaanCheck check =
				new BronVerbintenisWijzigingToegestaanCheck(datumField.getDatum(), plaatsing
					.getVerbintenis());
			if (!plaatsing.getVerbintenis().isBOVerbintenis() && !check.isWijzigingToegestaan())
				error(datumField);
		}

	}
}