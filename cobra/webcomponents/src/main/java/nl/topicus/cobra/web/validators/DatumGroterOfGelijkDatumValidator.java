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
 * Validator die controleert dat een gegeven datum na of gelijk is aan een vaste gegeven
 * datum of een ander datumveld ligt.
 * 
 * @author loite
 */
public class DatumGroterOfGelijkDatumValidator extends AbstractFormValidator
{
	private static final long serialVersionUID = 1L;

	private final DatumField datumField;

	private final Date vasteDatum;

	private final DatumField anderDatumField;

	private final String veldNaam;

	/**
	 * Constructor
	 * 
	 * @param datumField
	 * @param datum
	 */
	public DatumGroterOfGelijkDatumValidator(String veldNaam, DatumField datumField, Date datum)
	{
		this.veldNaam = veldNaam;
		this.datumField = datumField;
		this.vasteDatum = TimeUtil.getInstance().asDate(datum);
		this.anderDatumField = null;
	}

	/**
	 * Constructor
	 * 
	 * @param datumField
	 * @param datum
	 */
	public DatumGroterOfGelijkDatumValidator(DatumField datumField, Date datum)
	{
		this("datum", datumField, datum);
	}

	/**
	 * Constructor om twee datumfields met elkaar te vergelijken
	 * 
	 * @param datumField
	 *            veld dat groter dan of gelijk moet zijn aan anderDatumField
	 * @param anderDatumField
	 */
	public DatumGroterOfGelijkDatumValidator(DatumField datumField, DatumField anderDatumField)
	{
		if (datumField.getLabel() != null)
			this.veldNaam = datumField.getLabel().getObject();
		else
			this.veldNaam = "datum";

		this.vasteDatum = null;
		this.datumField = datumField;
		this.anderDatumField = anderDatumField;
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

		Date datum = getVasteDatum();
		if (datum == null && anderDatumField != null)
			datum = anderDatumField.getDatum();

		params.put("datum", TimeUtil.getInstance().formatDate(datum));

		if (datum == null || (!datumField.isRequired() && datumField.getDatum() == null))
			return;
		else if (datumField.isRequired() && datumField.getDatum() == null)
			error(datumField, "DatumGroterOfGelijkDatumValidator.error", params);
		else if (datumField.getDatum().before(datum))
			error(datumField, "DatumGroterOfGelijkDatumValidator.error", params);
	}

	protected Date getVasteDatum()
	{
		return vasteDatum;
	}
}