package nl.topicus.cobra.templates.documents.rtf;

public class RtfTableRow extends AbstractRtfGroup
{
	public RtfTableRow()
	{
		super();
	}

	public RtfTableRow(AbstractRtfGroup parent)
	{
		super(parent);
	}

	@Override
	public RtfTableRow clone() throws CloneNotSupportedException
	{
		RtfTableRow clone = new RtfTableRow();
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
