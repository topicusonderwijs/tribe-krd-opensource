package nl.topicus.cobra.templates.documents.rtf;

import java.util.Collection;

import nl.topicus.cobra.templates.exceptions.TemplateCreationException;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.resolvers.FieldResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RtfSection extends AbstractRtfGroup
{
	private static final Logger log = LoggerFactory.getLogger(RtfSection.class);

	public RtfSection()
	{
		super();
	}

	public RtfSection(AbstractRtfGroup parent)
	{
		super(parent);
	}

	@SuppressWarnings("unchecked")
	public void expandPropertyLists(FieldResolver resolver) throws TemplateException
	{
		for (RtfField field : getFields())
		{
			if (!field.getResult().isMerged() && field.getInstruction().getName().contains("[]"))
			{
				log.debug("Property list >> " + field.getInstruction().getName());

				String prop =
					field.getInstruction().getName().substring(0,
						field.getInstruction().getName().indexOf("[]"));

				Object v = resolver.resolve(prop);

				if (v != null && v instanceof Collection)
				{
					log.debug("\texpanding: " + prop + ", size: " + ((Collection) v).size());

					// n-1 extra merge field blokken aanmaken
					for (int i = ((Collection) v).size() - 1; i > 0; i--)
					{
						AbstractRtfGroup group = null;
						try
						{
							if (field.getParent() instanceof RtfDocument)
							{
								group = field.clone();
								group.setParent(field.getParent());
								field.getParent().insertElementAfter(group, field);
							}
							else if (field.getParent() instanceof RtfTableRow)
							{
								group = field.getParent().clone();
								group.setParent(field.getParent().getParent());
								field.getParent().getParent().insertElementAfter(group,
									field.getParent());
							}
							else
							{
								throw new TemplateCreationException("Unsupported RTF format");
							}
						}
						catch (CloneNotSupportedException e)
						{
							throw new TemplateCreationException("Failed to clone "
								+ field.getClass().getName() + ".", e);
						}

						for (RtfField clonedField : group.getFields())
						{
							clonedField.getInstruction()
								.setName(
									clonedField.getInstruction().getName().replace("[]",
										"[" + i + "]"));
							log.debug("\t" + i + ": " + clonedField.getInstruction().getName());
						}
					}

					for (RtfField parentField : field.getParent().getFields())
					{
						parentField.getInstruction().setName(
							parentField.getInstruction().getName().replace("[]", "[" + 0 + "]"));

						log.debug("\t" + 0 + ": " + parentField.getInstruction().getName());
					}
				}
			}
		}
	}

	@Override
	public RtfSection clone() throws CloneNotSupportedException
	{
		RtfSection clone = new RtfSection();
		for (IRtfElement element : getElements())
		{
			IRtfElement cloneElement = element.clone();
			if (cloneElement instanceof AbstractRtfGroup)
				((AbstractRtfGroup) cloneElement).setParent(clone);

			clone.addElement(cloneElement);
		}
		return clone;
	}
}
