package nl.topicus.eduarte.web.components.panels.kenmerk;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.kenmerk.MedewerkerKenmerk;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class MedewerkerKenmerkenPanel extends TypedPanel<MedewerkerKenmerk>
{

	private static final long serialVersionUID = 1L;

	public MedewerkerKenmerkenPanel(String id, IModel<MedewerkerKenmerk> model)
	{
		super(id, model);

		createAutoFieldSet();
	}

	private void createAutoFieldSet()
	{
		AutoFieldSet<MedewerkerKenmerk> details =
			new AutoFieldSet<MedewerkerKenmerk>("details", getMedewerkerKenmerkModel(), "Details")
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
					&& MedewerkerKenmerkenPanel.this.getDefaultModelObject() == null;
			}
		});
	}

	public MedewerkerKenmerk getMedewerkerKenmerk()
	{
		return getModelObject();
	}

	public IModel<MedewerkerKenmerk> getMedewerkerKenmerkModel()
	{
		return getModel();
	}

}
