package nl.topicus.eduarte.krd.web.pages.intake;

import org.apache.wicket.IClusterable;
import org.apache.wicket.Session;
import org.apache.wicket.util.value.ValueMap;

/**
 * Lijst van deelnemer intakes die opgeslagen wordt in de
 * {@link Session#getMetaData(org.apache.wicket.MetaDataKey)}. Daar een gebruiker mogelijk
 * met meerdere intakes kan werken (back button en dergelijke), houden we een map bij van
 * de lopende intakes met als sleutel het deelnemernummer.
 */
public class IntakeWizardMetaDataValues implements IClusterable
{
	private static final long serialVersionUID = 1L;

	private ValueMap deelnemerIntakes = new ValueMap();

	public IntakeWizardMetaDataValues()
	{
	}

	public IntakeWizardModel getWizardModel(int deelnemernummer)
	{
		return (IntakeWizardModel) deelnemerIntakes.get(Integer.toString(deelnemernummer));
	}

	public void putWizardModel(IntakeWizardModel model)
	{
		deelnemerIntakes.put(Integer.toString(model.getDeelnemernummer()), model);
	}
}
