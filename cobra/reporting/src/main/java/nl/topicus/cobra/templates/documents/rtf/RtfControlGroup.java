package nl.topicus.cobra.templates.documents.rtf;

import java.io.BufferedWriter;
import java.io.IOException;

import nl.topicus.cobra.templates.exceptions.TemplateCreationException;
import nl.topicus.cobra.templates.exceptions.TemplateException;

public class RtfControlGroup extends AbstractRtfGroup
{
	public RtfControlGroup()
	{
		super();
	}

	public RtfControlGroup(AbstractRtfGroup parent)
	{
		super(parent);
	}

	@Override
	public void write(BufferedWriter writer) throws TemplateException
	{
		try
		{
			writer.append("{");

			super.write(writer);

			writer.append("}");
		}
		catch (IOException e)
		{
			throw new TemplateCreationException("Cannot write document", e);
		}
	}

	@Override
	public String toString()
	{
		return "{" + super.toString() + "}";
	}

	@Override
	public RtfControlGroup clone() throws CloneNotSupportedException
	{
		RtfControlGroup clone = new RtfControlGroup();
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
