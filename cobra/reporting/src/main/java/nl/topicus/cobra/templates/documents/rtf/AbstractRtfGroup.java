package nl.topicus.cobra.templates.documents.rtf;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.templates.exceptions.InvalidTemplateException;
import nl.topicus.cobra.templates.exceptions.TemplateException;

public abstract class AbstractRtfGroup implements IRtfElement
{
	private AbstractRtfGroup parent;

	private final List<IRtfElement> elements;

	public AbstractRtfGroup()
	{
		this(null);
	}

	public AbstractRtfGroup(AbstractRtfGroup parent)
	{
		this.parent = parent;
		elements = new ArrayList<IRtfElement>();
	}

	/*
	 * (non-Javadoc)
	 * @see nl.topicus.cobra.templates.documents.rtf.IRtfElement#getParent()
	 */
	public AbstractRtfGroup getParent()
	{
		return parent;
	}

	public boolean isKeepMergeFields()
	{
		AbstractRtfGroup currentParent = getParent();
		while (currentParent != null && !(currentParent instanceof RtfDocument))
		{
			currentParent = currentParent.getParent();
		}

		if (currentParent instanceof RtfDocument)
			return ((RtfDocument) currentParent).isKeepMergeFields();

		return RtfDocument.KEEP_MERGE_FIELDS_DEFAULT;
	}

	public void setParent(AbstractRtfGroup parent)
	{
		this.parent = parent;
	}

	public void addElement(IRtfElement element)
	{
		elements.add(element);
	}

	public void insertElementAfter(IRtfElement element, IRtfElement after) throws TemplateException
	{
		int index = getElements().indexOf(after);

		if (index < 0)
			throw new InvalidTemplateException("Element " + after.hashCode() + " not found.");

		if (index < getElements().size())
			getElements().add(++index, element);
		else
			getElements().add(element);
	}

	public void addElements(List<IRtfElement> newElements)
	{
		this.elements.addAll(newElements);
	}

	public List<IRtfElement> getElements()
	{
		return elements;
	}

	public IRtfElement getFirstElementOfType(Class< ? extends IRtfElement> elementClass)
	{
		for (IRtfElement element : elements)
		{
			if (elementClass.isInstance(element))
				return element;
			else if (element instanceof AbstractRtfGroup)
			{
				IRtfElement elementFromGroup =
					((AbstractRtfGroup) element).getFirstElementOfType(elementClass);
				if (elementFromGroup != null)
					return elementFromGroup;
			}
		}

		return null;
	}

	public List<IRtfElement> getAllElementsOfType(Class< ? extends IRtfElement> elementClass)
	{
		List<IRtfElement> allElements = new ArrayList<IRtfElement>();
		for (IRtfElement element : elements)
		{
			if (elementClass.isInstance(element))
				allElements.add(element);
			else if (element instanceof AbstractRtfGroup)
			{
				allElements.addAll(((AbstractRtfGroup) element).getAllElementsOfType(elementClass));
			}
		}

		return allElements;
	}

	public AbstractRtfGroup getFirstGroupWithElementOfType(
			Class< ? extends IRtfElement> elementClass)
	{
		for (IRtfElement element : elements)
		{
			if (elementClass.isInstance(element))
				return this;
			else if (element instanceof AbstractRtfGroup)
			{
				IRtfElement elementFromGroup =
					((AbstractRtfGroup) element).getFirstElementOfType(elementClass);
				if (elementFromGroup != null)
					return (AbstractRtfGroup) element;
			}
		}

		return null;
	}

	public List<RtfField> getFields()
	{
		List<RtfField> result = new ArrayList<RtfField>();

		for (IRtfElement element : elements)
		{
			if (element instanceof RtfField)
			{
				result.add((RtfField) element);
			}
			else if (element instanceof AbstractRtfGroup)
			{
				result.addAll(((AbstractRtfGroup) element).getFields());
			}
		}

		return result;
	}

	@Override
	public void write(BufferedWriter writer) throws TemplateException
	{
		for (IRtfElement element : elements)
		{
			element.write(writer);
		}
	}

	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();

		for (IRtfElement element : elements)
		{
			result.append(element.toString());
		}

		return result.toString();
	}

	@Override
	abstract public AbstractRtfGroup clone() throws CloneNotSupportedException;
}
