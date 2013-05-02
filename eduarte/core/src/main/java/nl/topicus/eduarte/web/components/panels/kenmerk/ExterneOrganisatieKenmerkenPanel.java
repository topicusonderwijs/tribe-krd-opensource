package nl.topicus.eduarte.web.components.panels.kenmerk;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.kenmerk.ExterneOrganisatieKenmerk;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class ExterneOrganisatieKenmerkenPanel extends TypedPanel<ExterneOrganisatieKenmerk>
{

	private static final long serialVersionUID = 1L;

	public ExterneOrganisatieKenmerkenPanel(String id, IModel<ExterneOrganisatieKenmerk> model)
	{
		super(id, model);

		createAutoFieldSet();
	}

	private void createAutoFieldSet()
	{
		AutoFieldSet<ExterneOrganisatieKenmerk> details =
			new AutoFieldSet<ExterneOrganisatieKenmerk>("details",
				getExterneOrganisatieKenmerkModel(), "Details")
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
					&& ExterneOrganisatieKenmerkenPanel.this.getDefaultModelObject() == null;
			}
		});
	}

	public ExterneOrganisatieKenmerk getExterneOrganisatieKenmerk()
	{
		return getModelObject();
	}

	public IModel<ExterneOrganisatieKenmerk> getExterneOrganisatieKenmerkModel()
	{
		return getModel();
	}
}
