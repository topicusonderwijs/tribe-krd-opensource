package nl.topicus.eduarte.web.components.factory;

import java.util.List;

import nl.topicus.cobra.modules.ModuleComponentFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

public interface InleverenLinkFactory extends ModuleComponentFactory
{
	public <T> void postProcessDatapanel(EduArteDataPanel<T> datapanel);

	public void addInleverenColumn(CustomDataPanelContentDescription<Toets> table,
			List<Deelnemer> deelnemers);
}
