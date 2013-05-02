package nl.topicus.eduarte.web.components.link;

import java.util.Map;

import nl.topicus.cobra.dao.helpers.ZoekFilterDataAccessHelper;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.resolvers.FieldResolver;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.jobs.rapportage.RapportageJobDataMap;
import nl.topicus.eduarte.jobs.rapportage.RapportageJobUtil;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.model.IModel;
import org.quartz.SchedulerException;

/**
 * Link welke een achtergrondtaak start.
 * 
 * @author hoeve
 */
public class MultiEntityRapportageGenereerLink<R extends IdObject, S extends IdObject, ZF extends DetachableZoekFilter<S>>
		extends AbstractRapportageGenereerLink
{
	private static final long serialVersionUID = 1L;

	protected ISelectionComponent<R, S> selectionComponent;

	protected Class< ? extends ZoekFilterDataAccessHelper<S, ZF>> dahClass;

	protected SecurePage returnPage;

	protected DocumentTemplateType eindType;

	public MultiEntityRapportageGenereerLink(String id, IModel<DocumentTemplate> templateModel)
	{
		super(id, templateModel);
	}

	public void setSelectionComponent(ISelectionComponent<R, S> selectionComponent)
	{
		this.selectionComponent = selectionComponent;
	}

	public void setDahClass(Class< ? extends ZoekFilterDataAccessHelper<S, ZF>> dahClass)
	{
		this.dahClass = dahClass;
	}

	public void setReturnPage(SecurePage returnPage)
	{
		this.returnPage = returnPage;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onClick()
	{
		DocumentTemplate template = getModelObject();

		/**
		 * dont do {@link super#onClick()}. this subclass does not generate a document, it
		 * only makes the quartz job which makes the document.
		 */
		try
		{
			RapportageJobDataMap<R, S> datamap = new RapportageJobDataMap<R, S>();

			updateContext(datamap, null);
			updateDatamap(datamap);

			datamap.setDahClass(dahClass);
			datamap.setSelection(selectionComponent.getSelectedElements());
			datamap.setDocumentTemplate(template);
			datamap.setEindType(eindType);

			RapportageJobUtil.startRapportageJob(template, datamap);
		}
		catch (SchedulerException e)
		{
			log.error(e.toString(), e);
			error("Taak kon niet opgestart worden.");
			error(e.getLocalizedMessage());
		}

		if (returnPage instanceof IRapportageReturnPage)
			((IRapportageReturnPage<R, S, ZF>) returnPage).rapportageInvoked();

		setResponsePage(returnPage);
	}

	@SuppressWarnings("unused")
	protected void updateDatamap(RapportageJobDataMap<R, S> datamap)
	{
	}

	@Override
	protected Map<String, Object> updateContext(Map<String, Object> context,
			nl.topicus.cobra.templates.documents.DocumentTemplate document)
	{
		return context;
	}

	@SuppressWarnings("unused")
	@Override
	protected void writeSections(nl.topicus.cobra.templates.documents.DocumentTemplate document,
			FieldResolver resolver) throws TemplateException
	{
		// dit wordt hier nooit gecalled.
	}

	public void setEindType(DocumentTemplateType eindType)
	{
		this.eindType = eindType;
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		returnPage.detach();
	}
}
