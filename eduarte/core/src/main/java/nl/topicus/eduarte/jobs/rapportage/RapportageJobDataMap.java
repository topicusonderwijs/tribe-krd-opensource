package nl.topicus.eduarte.jobs.rapportage;

import java.io.Serializable;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessHelper;
import nl.topicus.cobra.dao.helpers.ZoekFilterDataAccessHelper;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratieFactory;

import org.quartz.JobDataMap;

public class RapportageJobDataMap<R extends IdObject, S extends IdObject> extends
		DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public static final String KEY_DAH_CLASS = "dahClass";

	public static final String KEY_SELECTION = "selection";

	public static final String KEY_CONFIGURATION = "configuration";

	public static final String KEY_DOCUMENTTEMPLATE = "documentTemplate";

	public static final String KEY_EINDTYPE = "eindType";

	public static final String KEY_BIJLAGE_CONTEXT_ID_PROVIDER = "bijlageContextIDProvider";

	public RapportageJobDataMap()
	{
	}

	@SuppressWarnings("unchecked")
	public RapportageJobDataMap(JobDataMap basicMap)
	{
		this
			.setDahClass((Class< ? extends ZoekFilterDataAccessHelper<S, DetachableZoekFilter<S>>>) basicMap
				.get(KEY_DAH_CLASS));
		this.setDocumentTemplate((DocumentTemplate) basicMap.get(KEY_DOCUMENTTEMPLATE));
		this.setEindType((DocumentTemplateType) basicMap.get(KEY_EINDTYPE));
		this.setSelection((List<R>) basicMap.get(KEY_SELECTION));

		this.getWrappedMap().putAll(basicMap.getWrappedMap());
	}

	@SuppressWarnings("unchecked")
	public List<R> getSelection()
	{
		return (List<R>) get(KEY_SELECTION);
	}

	public void setSelection(List<R> selection)
	{
		put(KEY_SELECTION, selection);
	}

	@SuppressWarnings("unchecked")
	public RapportageConfiguratieFactory<R> getConfiguration()
	{
		return (RapportageConfiguratieFactory<R>) get(KEY_CONFIGURATION);
	}

	public void setConfiguration(RapportageConfiguratieFactory<R> configuration)
	{
		put(KEY_CONFIGURATION, configuration);
	}

	@SuppressWarnings("unchecked")
	// Class< ? extends ZoekFilterDataAccessHelper<S, DetachableZoekFilter<S>>>
	public Class< ? extends ZoekFilterDataAccessHelper<S, DetachableZoekFilter<S>>> getDahClass()
	{
		return (Class< ? extends ZoekFilterDataAccessHelper<S, DetachableZoekFilter<S>>>) get(KEY_DAH_CLASS);
	}

	public void setDahClass(Class< ? extends DataAccessHelper< ? >> dahClass)
	{
		put(KEY_DAH_CLASS, dahClass);
	}

	public DocumentTemplate getDocumentTemplate()
	{
		return (DocumentTemplate) get(KEY_DOCUMENTTEMPLATE);
	}

	public void setDocumentTemplate(DocumentTemplate templateModel)
	{
		put(KEY_DOCUMENTTEMPLATE, templateModel);
	}

	public DocumentTemplateType getStartType()
	{
		return getDocumentTemplate() != null ? getDocumentTemplate().getType() : null;
	}

	/**
	 * Als hier een andere waarde voor staat betekent dit dat het gegenereerde document
	 * nog door een converter moet, bv Open Office.
	 * 
	 * @return het type waar het bestand aan moet voldoen na generatie.
	 */
	public DocumentTemplateType getEindType()
	{
		DocumentTemplateType eindtype = (DocumentTemplateType) get(KEY_EINDTYPE);
		if (eindtype == null)
			eindtype = getStartType();

		return eindtype;
	}

	public void setEindType(DocumentTemplateType eindType)
	{
		put(KEY_EINDTYPE, eindType);
	}

	public static class BijlageContextIDProvider
	{
		public Serializable getBijlageContextID(IdObject rapportageContextObject)
		{
			return rapportageContextObject.getIdAsSerializable();
		}
	}

	public void setBijlageContextIDProvider(BijlageContextIDProvider provider)
	{
		put(KEY_BIJLAGE_CONTEXT_ID_PROVIDER, provider);
	}

	public BijlageContextIDProvider getBijlageContextIDProvider()
	{
		BijlageContextIDProvider provider =
			(BijlageContextIDProvider) get(KEY_BIJLAGE_CONTEXT_ID_PROVIDER);
		if (provider == null)
			return new BijlageContextIDProvider();

		return provider;
	}

}
