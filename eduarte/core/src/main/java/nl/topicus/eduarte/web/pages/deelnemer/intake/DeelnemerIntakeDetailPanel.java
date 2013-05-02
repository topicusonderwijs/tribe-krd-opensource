package nl.topicus.eduarte.web.pages.deelnemer.intake;

import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.eduarte.web.components.panels.verbintenis.DeelnemerVerbintenisPanel;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class DeelnemerIntakeDetailPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private SecurePage returnPage;

	public DeelnemerIntakeDetailPanel(String id, IModel< ? > model, SecurePage returnPage)
	{
		super(id, model);
		this.returnPage = returnPage;
	}

	@Override
	protected void onBeforeRender()
	{
		setVisible(getDefaultModelObject() != null);

		if (getDefaultModelObject() instanceof Verbintenis)
			addOrReplace(new DeelnemerVerbintenisPanel("inner", getModelAsVerbintenis()));
		else if (getDefaultModelObject() instanceof Intakegesprek)
			addOrReplace(new DeelnemerIntakegesprekPanel("inner", getModelAsIntakegesprek(),
				returnPage));
		else
			addOrReplace(new EmptyPanel("inner"));

		super.onBeforeRender();
	}

	@SuppressWarnings("unchecked")
	public IModel<VerbintenisProvider> getVerbintenisOfIntakegesprekModel()
	{
		return (IModel<VerbintenisProvider>) getDefaultModel();
	}

	@SuppressWarnings("unchecked")
	public IModel<Intakegesprek> getModelAsIntakegesprek()
	{
		return (IModel<Intakegesprek>) getDefaultModel();
	}

	@SuppressWarnings("unchecked")
	public IModel<Verbintenis> getModelAsVerbintenis()
	{
		return (IModel<Verbintenis>) getDefaultModel();
	}
}
