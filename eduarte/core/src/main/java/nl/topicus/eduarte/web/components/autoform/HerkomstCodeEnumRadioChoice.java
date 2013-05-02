package nl.topicus.eduarte.web.components.autoform;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import nl.topicus.cobra.web.components.choice.EnumRadioChoice;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven.BPVCodeHerkomst;
import nl.topicus.eduarte.web.components.factory.BPVModuleComponentFactory;

import org.apache.wicket.model.IObjectClassAwareModel;

/**
 * Radiochoice die "herkomst COLO" verwijdert uit enum radioselectie wanneer BPV module
 * niet afgenomen wordt.
 */
public class HerkomstCodeEnumRadioChoice<T extends Enum<T>> extends EnumRadioChoice<T>
{
	private static final long serialVersionUID = 1L;

	private static <T extends Enum<T>> List<T> findChoices(IObjectClassAwareModel<T> model)
	{
		Class<T> clazz = model.getObjectClass();
		if (clazz == null)
			throw new IllegalArgumentException("Model cannot determine object class");

		ArrayList<T> result = new ArrayList<T>(EnumSet.allOf(clazz));

		if (!isBPVModuleEnabled())
			result.remove(BPVCodeHerkomst.COLO);

		return result;
	}

	private static boolean isBPVModuleEnabled()
	{
		List<BPVModuleComponentFactory> factories =
			EduArteApp.get().getPanelFactories(BPVModuleComponentFactory.class);
		return (factories.size() != 0);
	}

	public HerkomstCodeEnumRadioChoice(String id, IObjectClassAwareModel<T> model)
	{
		super(id, model, findChoices(model));
	}
}
