package nl.topicus.eduarte.web.components.link;

import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.panels.datapanel.selection.IDatabaseSelectionComponent;
import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.security.actions.EduArteActionFactory;
import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateRecht;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.RapportageConfiguratiePage;
import nl.topicus.eduarte.web.pages.shared.SelectieTarget;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.ISecurityCheck;

public class MultiEntityRapportageSelectieTarget<R extends IBijlageKoppelEntiteit< ? extends BijlageEntiteit>, S extends IdObject, ZF extends DetachableZoekFilter<S>>
		implements SelectieTarget<R, S>
{
	private static final long serialVersionUID = 1L;

	protected SecurePage returnPage;

	protected DocumentTemplateType eindType;

	private IModel<DocumentTemplate> templateModel;

	public MultiEntityRapportageSelectieTarget(IModel<DocumentTemplate> templateModel,
			SecurePage page, DocumentTemplateType eindType)
	{
		this.templateModel = templateModel;
		this.returnPage = page;
		this.eindType = eindType;
	}

	@Override
	public Link<DocumentTemplate> createLink(String linkId, ISelectionComponent<R, S> base)
	{
		@SuppressWarnings("unchecked")
		final IDatabaseSelectionComponent<R, S, ZF> dbBase =
			(IDatabaseSelectionComponent<R, S, ZF>) base;
		if (getTemplate().getConfiguratiePanel() != null)
		{
			return new TargetBasedSecurePageLink<DocumentTemplate>(linkId, new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Class< ? extends Page> getPageIdentity()
				{
					return RapportageConfiguratiePage.class;
				}

				@Override
				public Page getPage()
				{
					return new RapportageConfiguratiePage<R, S, ZF>(templateModel, returnPage,
						eindType, dbBase);
				}
			});
		}
		else
		{
			MultiEntityRapportageGenereerLink<R, S, ZF> opslaanlink =
				new MultiEntityRapportageGenereerLink<R, S, ZF>("link", templateModel);
			opslaanlink.setDahClass(dbBase.getDahClass());
			opslaanlink.setSelectionComponent(dbBase);
			opslaanlink.setReturnPage(returnPage);
			opslaanlink.setEindType(eindType);
			return opslaanlink;
		}
	}

	private DocumentTemplate getTemplate()
	{
		return templateModel.getObject();
	}

	@Override
	public String getLinkLabel()
	{
		return getTemplate().getConfiguratiePanel() == null ? "Opslaan" : "Volgende";
	}

	@Override
	public void detach()
	{
		templateModel.detach();
		returnPage.detach();
	}

	@Override
	public ISecurityCheck getSecurityCheck()
	{
		return new ISecurityCheck()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isAuthenticated()
			{
				return true;
			}

			@Override
			public boolean isActionAuthorized(WaspAction action)
			{
				DocumentTemplate template = templateModel.getObject();
				if (template.getCustomSecurityCheck() != null)
					return template.getCustomSecurityCheck().isActionAuthorized(action);

				if (template.isBeperkAutorisatie()
					|| template.getDocumentType().getCategorie().isBeperkAutorisatie())
				{
					EduArteActionFactory actionFactory = EduArteApp.get().getActionFactory();
					List<DocumentTemplateRecht> rechten = template.getRechten();
					rechten.addAll(template.getDocumentType().getCategorie().getRechten());
					for (DocumentTemplateRecht curRecht : rechten)
					{
						if (EduArteContext.get().getAccount().getRollenAsRol().contains(
							curRecht.getRol()))
						{
							if (actionFactory.getAction(curRecht.getActionClass()).implies(action))
								return true;
						}
					}
					return false;
				}
				return returnPage.isActionAuthorized(action);
			}
		};
	}
}
