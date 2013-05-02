package nl.topicus.eduarte.web.components.factory;

import nl.topicus.cobra.modules.ModuleComponentFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.providers.BPVInschrijvingProvider;

/**
 * Factory interface voor het aanmaken van componenten die uit de BPV module komen.
 */
public interface BPVModuleComponentFactory extends ModuleComponentFactory
{
	public StagemarktServiceAdapter getStagemarktServiceAdapter();

	public void createControleerAccreditatieButton(BottomRowPanel parent,
			final BPVInschrijvingProvider provider);
}