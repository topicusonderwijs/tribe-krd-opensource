package nl.topicus.eduarte.web.components.panels;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.web.components.label.PostcodeWoonplaatsLabel;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.ExterneOrganisatieOverzichtPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

public class ExterneOrganisatiePanel extends TypedPanel<ExterneOrganisatie>
{
	private static final long serialVersionUID = 1L;

	public ExterneOrganisatiePanel(String id,
			final IModel<ExterneOrganisatie> externeOrganisatieModel)
	{
		this(id, externeOrganisatieModel, null);
	}

	public ExterneOrganisatiePanel(String id,
			final IModel<ExterneOrganisatie> externeOrganisatieModel, final SecurePage returnPage)
	{
		super(id, ModelFactory.getCompoundModelForModel(externeOrganisatieModel));
		setRenderBodyOnly(true);

		WebMarkupContainer link = null;

		if (returnPage != null)
		{
			link = new TargetBasedSecurePageLink<Void>("link", new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Page getPage()
				{
					return new ExterneOrganisatieOverzichtPage(externeOrganisatieModel.getObject(),
						returnPage);
				}

				@Override
				public Class<ExterneOrganisatieOverzichtPage> getPageIdentity()
				{
					return ExterneOrganisatieOverzichtPage.class;
				}
			});
		}
		else
		{
			link = new WebMarkupContainer("link");
			link.setRenderBodyOnly(true);
		}

		link.add(new Label("naam"));
		link.add(new Label("postAdres.adres.straatHuisnummer"));
		link.add(new PostcodeWoonplaatsLabel("postAdres.adres.postcodePlaats"));

		add(link);

	}
}