package nl.topicus.eduarte.resultaten.web.components.factory;

import nl.topicus.cobra.modelsv2.ExtendedModel;
import nl.topicus.cobra.modules.ModuleComponentFactory;
import nl.topicus.cobra.web.components.form.SavableForm;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.resultaten.web.pages.shared.AbstractOnderwijsproductToetsEditPage;
import nl.topicus.eduarte.resultaten.web.pages.shared.ResultaatstructuurEditPage;

public interface ToetsWizardButtonFactory extends ModuleComponentFactory
{
	public void addWizardButton(BottomRowPanel panel, SavableForm< ? > form,
			ExtendedModel<Toets> toetsModel, ResultaatstructuurEditPage returnPage);

	public AbstractOnderwijsproductToetsEditPage createDefaultEditPage(
			ExtendedModel<Toets> toetsModel, ResultaatstructuurEditPage returnPage);
}
