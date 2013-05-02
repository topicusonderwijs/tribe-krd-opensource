package nl.topicus.eduarte.resultaten.web.pages.shared;

import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.resultaten.principals.onderwijs.ToetsenBevriezen;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.SubpageContext;

import org.apache.wicket.model.IModel;

@PageInfo(title = "Toetsen bevriezen", menu = "Onderwijs > Onderwijsproducten > [onderwijsproduct] > Resultaten > Bevriezen")
@InPrincipal(ToetsenBevriezen.class)
public class ToetsenBevriezenPage extends AbstractToetsenBevriezenPage
{

	private SecurePage returnPage;

	public ToetsenBevriezenPage(Resultaatstructuur structuur, SecurePage returnPage)
	{
		super(new SubpageContext(returnPage), ModelFactory.getCompoundChangeRecordingModel(
			structuur, new DefaultModelManager(Toets.class, Resultaatstructuur.class)));

		this.returnPage = returnPage;
		createComponents();
	}

	@Override
	protected IModel<Toets> getToetsModel(Toets toets)
	{
		return getResultaatstructuurModel().getManager().getModel(toets, null);
	}

	@Override
	public List<Toets> getBevriezingen(Toets toets)
	{
		return Arrays.asList(toets);
	}

	@Override
	public boolean getDisableBevrorenToetsen()
	{
		return false;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, null)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean isFormSubmittingButton()
			{
				return false;
			}

			@Override
			protected void onSubmit()
			{
				IChangeRecordingModel<Resultaatstructuur> model = getResultaatstructuurModel();
				model.saveObject();
				model.getObject().commit();
				setResponsePage(returnPage);
			}
		});
		panel.addButton(new AnnulerenButton(panel, returnPage));
	}

	@SuppressWarnings("unchecked")
	private IChangeRecordingModel<Resultaatstructuur> getResultaatstructuurModel()
	{
		return (IChangeRecordingModel<Resultaatstructuur>) getDefaultModel();
	}
}
