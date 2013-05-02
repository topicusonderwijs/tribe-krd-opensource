package nl.topicus.eduarte.web.components.panels.kenmerk;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.kenmerk.DeelnemerKenmerk;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class DeelnemerKenmerkenPanel extends TypedPanel<DeelnemerKenmerk>
{

	private static final long serialVersionUID = 1L;

	public DeelnemerKenmerkenPanel(String id, IModel<DeelnemerKenmerk> model)
	{
		super(id, model);

		createAutoFieldSet();
	}

	private void createAutoFieldSet()
	{
		AutoFieldSet<DeelnemerKenmerk> details =
			new AutoFieldSet<DeelnemerKenmerk>("details", getDeelnemerKenmerkModel(), "Details")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && getModelObject() != null;
				}
			};
		details.setRenderMode(RenderMode.DISPLAY);
		details.setPropertyNames("kenmerk.categorieNaam", "kenmerk.code", "kenmerk.naam",
			"toelichting", "begindatum", "einddatum");
		details.setSortAccordingToPropertyNames(true);
		add(details);
		add(new Label("noDetails", "Geen kenmerken gevonden")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible()
					&& DeelnemerKenmerkenPanel.this.getDefaultModelObject() == null;
			}
		});
	}

	public DeelnemerKenmerk getDeelnemerKenmerk()
	{
		return getModelObject();
	}

	public IModel<DeelnemerKenmerk> getDeelnemerKenmerkModel()
	{
		return getModel();
	}
}
