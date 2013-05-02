package nl.topicus.eduarte.resultaten.web.components.factory;

import java.util.List;

import nl.topicus.cobra.modelsv2.ExtendedModel;
import nl.topicus.cobra.modules.AbstractModuleComponentFactory;
import nl.topicus.cobra.web.components.form.SavableForm;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.resultaten.web.pages.shared.AbstractOnderwijsproductToetsEditPage;
import nl.topicus.eduarte.resultaten.web.pages.shared.OnderwijsproductToetsEditPage;
import nl.topicus.eduarte.resultaten.web.pages.shared.ResultaatstructuurEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.model.IModel;

public class ResultatenDummyComponentFactory extends AbstractModuleComponentFactory implements
		ToetsWizardButtonFactory, ToetscodesBeherenButtonFactory
{
	public ResultatenDummyComponentFactory()
	{
		super(0);
	}

	@Override
	public void addWizardButton(BottomRowPanel panel, SavableForm< ? > form,
			ExtendedModel<Toets> toetsModel, ResultaatstructuurEditPage returnPage)
	{
		// do nothing
	}

	@Override
	public void addToetscodesBeherenButton(BottomRowPanel panel, IModel<List<Toets>> toetsen,
			SecurePage returnPage)
	{
		// do nothing
	}

	@Override
	public AbstractOnderwijsproductToetsEditPage createDefaultEditPage(
			ExtendedModel<Toets> toetsModel, ResultaatstructuurEditPage returnPage)
	{
		return new OnderwijsproductToetsEditPage(toetsModel, returnPage);
	}
}
