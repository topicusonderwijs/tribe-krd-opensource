package nl.topicus.eduarte.web.components.panels.verbintenis;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.vrijevelden.PlaatsingVrijVeld;
import nl.topicus.eduarte.web.components.panels.VrijVeldEntiteitPanel;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class DeelnemerPlaatsingPanel extends TypedPanel<Plaatsing>
{

	private static final long serialVersionUID = 1L;

	public DeelnemerPlaatsingPanel(String id, IModel<Plaatsing> model)
	{
		super(id, model);

		AutoFieldSet<Plaatsing> detailsLinks =
			new AutoFieldSet<Plaatsing>("detailsLinks", getPlaatsingModel());
		detailsLinks.setPropertyNames("verbintenis.opleiding.naam", "leerjaar",
			"jarenPraktijkonderwijs", "groep.code", "begindatum", "einddatum", "lwoo");
		detailsLinks.addFieldModifier(new LabelModifier("jarenPraktijkonderwijs", "Praktijkjaar"));
		detailsLinks.setSortAccordingToPropertyNames(true);
		detailsLinks.setRenderMode(RenderMode.DISPLAY);
		detailsLinks.addFieldModifier(new VisibilityModifier("jarenPraktijkonderwijs",
			new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					boolean praktijkonderwijs = false;
					Plaatsing plaatsing = (Plaatsing) getDefaultModelObject();
					if (plaatsing.getVerbintenis() != null)
					{
						Opleiding opleiding = plaatsing.getVerbintenis().getOpleiding();
						if (opleiding != null)
							praktijkonderwijs =
								opleiding.getVerbintenisgebied().getTaxonomie().isVO()
									&& "0090".equals(opleiding.getVerbintenisgebied()
										.getExterneCode());
					}
					return praktijkonderwijs;
				}
			}));
		detailsLinks.addFieldModifier(new VisibilityModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return heeftLwoo();
			}

		}, "lwoo"));
		add(detailsLinks);

		add(new VrijVeldEntiteitPanel<PlaatsingVrijVeld, Plaatsing>("vrijveldenPanel",
			getPlaatsingModel()));
	}

	private boolean heeftLwoo()
	{
		if (getPlaatsingModel().getObject().getVerbintenis().getOpleiding() != null)
		{
			return getPlaatsingModel().getObject().getVerbintenis().getOpleiding().heeftLwoo();
		}
		return false;
	}

	public IModel<Plaatsing> getPlaatsingModel()
	{
		return getModel();
	}
}