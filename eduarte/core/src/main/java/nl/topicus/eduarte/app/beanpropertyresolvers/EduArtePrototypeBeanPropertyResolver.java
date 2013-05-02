package nl.topicus.eduarte.app.beanpropertyresolvers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratieRegistratie;

public class EduArtePrototypeBeanPropertyResolver extends EduArteBeanPropertyResolver
{
	public EduArtePrototypeBeanPropertyResolver(Object context)
	{
		super(context);
	}

	public EduArtePrototypeBeanPropertyResolver(DocumentTemplate template)
	{
		super(createContext(template));
	}

	protected static Map<String, Object> createContext(DocumentTemplate template)
	{
		Map<String, Object> context = new HashMap<String, Object>();
		EduArteBeanPropertyResolver.supplementContext(context);

		if (!template.getContext().getContextClass().isInterface())
		{
			addObjectToContext(context, template, template.getContext().getContextClass(), template
				.getContext().getModelName());

			if (template.getConfiguratiePanel() != null)
			{
				RapportageConfiguratieRegistratie config = template.getConfiguratieRegistratie();
				context.put("instellingen", ReflectionUtil.invokeConstructor(config.factoryType()));
				addObjectToContext(context, template, config.configuratieType(), config.naam());
			}
		}

		return context;
	}

	private static void addObjectToContext(Map<String, Object> context, DocumentTemplate template,
			Class< ? > clazz, String name)
	{
		Object entity = ReflectionUtil.invokeConstructor(clazz);
		if (template.isSectiePerElement())
			context.put(name, entity);
		else
			context.put(name, Arrays.asList(entity));
	}
}
