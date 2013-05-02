package nl.topicus.eduarte.web.components.factory;

import nl.topicus.cobra.modules.ModuleComponentFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Factory interface voor het aanmaken van componenten die uit de financiele module komen.
 */
public interface FinancieelModuleComponentFactory extends ModuleComponentFactory
{
	public void newContractFactuurregelDefinitiesBewerkenKnop(BottomRowPanel parent,
			IModel<Contract> contractModel);

	public void newContractFactuurregelToevoegenKnop(BottomRowPanel parent,
			IModel<Contract> contractModel);

	public Panel newRestitutiePanel(String id, IModel<Verbintenis> verbintenisModel,
			IModel<Boolean> restitutieModel);

	public SecurePage newRestitutiePage(IModel<Verbintenis> verbintenisModel);
}