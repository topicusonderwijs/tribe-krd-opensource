package nl.topicus.eduarte.web.components.panels.verbintenis;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class DeelnemerVerbintenisDetailPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public DeelnemerVerbintenisDetailPanel(String id, IModel< ? > model)
	{
		super(id, model);

		Panel verbintenisPanel =
			new DeelnemerVerbintenisPanel("detailsVerbintenis", getModelAsVerbintenis())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && getDefaultModelObject() instanceof Verbintenis;
				}
			};
		Panel plaatsingPanel =
			new DeelnemerPlaatsingPanel("detailsPlaatsing", getModelAsPlaatsing())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && getDefaultModelObject() instanceof Plaatsing;
				}
			};

		add(verbintenisPanel);
		add(plaatsingPanel);

	}

	@Override
	public boolean isVisible()
	{
		return getDefaultModelObject() != null;
	}

	@SuppressWarnings("unchecked")
	public IModel<IdObject> getPlaatsingOfVerbintenisModel()
	{
		return (IModel<IdObject>) getDefaultModel();
	}

	@SuppressWarnings("unchecked")
	public IModel<Verbintenis> getModelAsVerbintenis()
	{
		return (IModel<Verbintenis>) getDefaultModel();
	}

	@SuppressWarnings("unchecked")
	public IModel<Plaatsing> getModelAsPlaatsing()
	{
		return (IModel<Plaatsing>) getDefaultModel();
	}
}
