package nl.topicus.cobra.templates.documents.rtf;

import java.io.BufferedWriter;

import nl.topicus.cobra.templates.exceptions.TemplateException;

public class RtfFieldResult extends RtfControlGroup
{
	private boolean isMerged = false;

	private transient RtfText resultText;

	public RtfFieldResult()
	{
		super();
	}

	public RtfFieldResult(AbstractRtfGroup parent)
	{
		super(parent);
	}

	public boolean isMerged()
	{
		return isMerged;
	}

	private RtfText getResultText()
	{
		if (resultText == null)
			resultText = (RtfText) getFirstElementOfType(RtfText.class);

		return resultText;
	}

	public void setText(String text)
	{
		AbstractRtfGroup group = getFirstGroupWithElementOfType(RtfText.class);
		if (group == null)
		{
			isMerged = true;
			return;
		}

		int index = group.getElements().indexOf(getResultText());
		index++;

		while (index < group.getElements().size())
		{
			if (group.getElements().get(index) instanceof RtfText
				|| group.getElements().get(index) instanceof RtfDelimiter)
			{
				group.getElements().remove(index);
			}

			index++;
		}

		getResultText().setText(text);
		isMerged = true;
	}

	@Override
	public RtfFieldResult clone() throws CloneNotSupportedException
	{
		RtfFieldResult clone = new RtfFieldResult();
		for (IRtfElement element : getElements())
		{
			IRtfElement cloneElement = element.clone();
			if (cloneElement instanceof AbstractRtfGroup)
				((AbstractRtfGroup) cloneElement).setParent(clone);

			clone.addElement(cloneElement);
		}
		return clone;
	}

	/**
	 * @see nl.topicus.cobra.templates.documents.rtf.RtfControlGroup#write(java.io.BufferedWriter)
	 *      Schrijf de body van dit element weg. Tenzij men
	 *      {@link RtfDocument#setKeepMergeFields(boolean)} = false heeft geset, dan slaan
	 *      we dit element over en gaan we meteen naar de text waarde.
	 */
	@Override
	public void write(BufferedWriter writer) throws TemplateException
	{
		if (isKeepMergeFields())
			super.write(writer);
		else
		{
			AbstractRtfGroup group = getFirstGroupWithElementOfType(RtfText.class);
			if (group != null)
			{
				group.write(writer);
			}
		}
	}
}
