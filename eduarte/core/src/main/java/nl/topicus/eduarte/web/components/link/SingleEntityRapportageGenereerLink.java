package nl.topicus.eduarte.web.components.link;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.templates.documents.DocumentTemplate;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.resolvers.FieldResolver;

import org.apache.wicket.model.IModel;

public class SingleEntityRapportageGenereerLink<T> extends AbstractRapportageGenereerLink
{
	private static final long serialVersionUID = 1L;

	protected IModel<T> objectModel;

	protected String modelName;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            wicket-id
	 * @param templateModel
	 *            Document template
	 * @param modelName
	 *            naam welke in de context zal worden gebruikt voor velden in de template
	 * @param objectModel
	 *            Huidige object
	 */
	public SingleEntityRapportageGenereerLink(String id,
			IModel<nl.topicus.eduarte.entities.rapportage.DocumentTemplate> templateModel,
			String modelName, IModel<T> objectModel)
	{
		super(id, templateModel);
		this.modelName = modelName;
		this.objectModel = objectModel;
	}

	@Override
	protected Map<String, Object> updateContext(Map<String, Object> context,
			DocumentTemplate document)
	{
		if (document.hasSections())
			context.put(modelName, objectModel.getObject());
		else
		{
			List<T> list = new ArrayList<T>();
			list.add(objectModel.getObject());
			context.put(modelName, list);
		}
		context.put(nl.topicus.cobra.templates.documents.DocumentTemplate.CONTEXT_OBJECT_REF_NAME,
			modelName);
		return context;
	}

	@Override
	protected void writeSections(DocumentTemplate document, FieldResolver resolver)
			throws TemplateException
	{
		document.writeDocumentHeader();
		document.writeSection(resolver);
		document.writeDocumentFooter();
	}

	@Override
	public void detachModels()
	{
		super.detachModels();

		objectModel.detach();
	}
}
