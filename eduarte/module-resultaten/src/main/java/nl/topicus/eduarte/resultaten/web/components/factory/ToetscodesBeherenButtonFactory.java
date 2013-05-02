package nl.topicus.eduarte.resultaten.web.components.factory;

import java.util.List;

import nl.topicus.cobra.modules.ModuleComponentFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.model.IModel;

public interface ToetscodesBeherenButtonFactory extends ModuleComponentFactory
{
	public void addToetscodesBeherenButton(BottomRowPanel panel, IModel<List<Toets>> toetsen,
			SecurePage returnPage);
}
