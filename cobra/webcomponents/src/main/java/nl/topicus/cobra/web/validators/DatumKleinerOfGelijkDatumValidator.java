package nl.topicus.cobra.web.validators;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.text.DatumField;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

/**
 * Validator die controleert dat een gegeven datum voor of gelijk is aan een vaste gegeven
 * datum ligt.
 * 
 * @author loite
 */
public class DatumKleinerOfGelijkDatumValidator extends AbstractFormValidator
{
	private static final long serialVersionUID = 1L;

	private final DatumField datumField;

	private final Date vasteDatum;

	private final String veldNaam;

	/**
	 * Constructor
	 * 
	 * @param datumField
	 * @param datum
	 */
	public DatumKleinerOfGelijkDatumValidator(String veldNaam, DatumField datumField, Date datum)
	{
		this.veldNaam = veldNaam;
		this.datumField = datumField;
		this.vasteDatum = TimeUtil.getInstance().asDate(datum);
	}

	/**
	 * Constructor
	 * 
	 * @param datumField
	 * @param datum
	 */
	public DatumKleinerOfGelijkDatumValidator(DatumField datumField, Date datum)
	{
		this("datum", datumField, datum);
	}

	@Override
	public FormComponent< ? >[] getDependentFormComponents()
	{
		FormComponent< ? >[] formComponents = {datumField};
		return formComponents;
	}

	@Override
	public void validate(Form< ? > form)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("veldnaam", veldNaam);
		params.put("datum", TimeUtil.getInstance().formatDate(getVasteDatum()));

		if (!datumField.isRequired() && datumField.getDatum() == null)
			return;
		else if (datumField.isRequired() && datumField.getDatum() == null)
			error(datumField, "DatumKleinerOfGelijkDatumValidator.error", params);
		else if (getVasteDatum() != null && datumField.getDatum().after(getVasteDatum()))
			error(datumField, "DatumKleinerOfGelijkDatumValidator.error", params);
	}

	public DatumField getDatumField()
	{
		return datumField;
	}

	public Date getVasteDatum()
	{
		return vasteDatum;
	}

	public String getVeldNaam()
	{
		return veldNaam;
	}

}