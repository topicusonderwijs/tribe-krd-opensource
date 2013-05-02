package nl.topicus.eduarte.web.components.resultaat;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.components.factory.StructuurLinkFactory;
import nl.topicus.eduarte.web.components.modalwindow.DummyLink;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class ResultaatColumnHeaderPanel extends TypedPanel<Toets>
{
	private static final long serialVersionUID = 1L;

	public ResultaatColumnHeaderPanel(String id, final IModel<String> model,
			final IModel<Toets> toetsModel, boolean editMode)
	{
		super(id, toetsModel);

		setRenderBodyOnly(true);

		Link< ? > structuurLink;
		if (editMode || toetsModel.getObject() == null)
		{
			structuurLink = new DummyLink("structuurLink");
			structuurLink.setEnabled(false);
		}
		else
		{
			structuurLink =
				EduArteApp.get().getFirstPanelFactory(StructuurLinkFactory.class,
					EduArteContext.get().getOrganisatie()).createStructuurLink("structuurLink",
					getModelObject().getResultaatstructuur(), this);
		}
		structuurLink.setBeforeDisabledLink("");
		structuurLink.setAfterDisabledLink("");
		add(structuurLink);

		Label label = new Label("label", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				Toets toets = toetsModel.getObject();
				if (toets == null || toets.getPersoonlijkeToetscode() == null)
					return model.getObject();

				return toets.getPersoonlijkeToetscode();
			}
		});
		label.add(new AttributeModifier("title", true, new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				Toets toets = toetsModel.getObject();
				if (toets == null || toets.getPersoonlijkeToetscode() == null)
					return model.getObject();

				return toets.getPersoonlijkeToetscode() + " (" + model.getObject() + ")";
			}
		}));
		structuurLink.add(label);

	}
}
