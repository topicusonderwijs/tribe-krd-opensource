package nl.topicus.cobra.templates.documents.rtf;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.topicus.cobra.util.StringUtil;

public class RtfFieldInstruction extends RtfControlGroup
{
	// reguliere expressie om de elementen van de field instructie te parsen. Let op dat
	// we \@ en \# doorkrijgen als \\@ en \\#. Bij een spatie in de veldnaam voegt Word
	// "-tekens toe.
	Pattern INSTRUCTION_PATTERN =
		Pattern
			.compile("\\s*(\\S*)\\s*\\\"?([^\\(\\)\\s]*(?:\\([^\\(\\)]*\\)[^\\s\\\"]*)*)\\\"?\\s*(\\\\\\\\@\\s*\"(.*)\")?(\\\\\\\\#\\s*\"(.*)\")?.*");

	private transient String instructionText;

	public RtfFieldInstruction()
	{
		super();
	}

	public RtfFieldInstruction(AbstractRtfGroup parent)
	{
		super(parent);
	}

	private String getInstructionText()
	{
		if (instructionText == null)
		{
			StringBuilder builder = new StringBuilder();
			List<IRtfElement> allText = getAllElementsOfType(RtfText.class);
			for (IRtfElement element : allText)
			{
				builder.append(((RtfText) element).getTextAsUnicode().replace("\r\n", ""));
			}
			instructionText = builder.toString();
		}

		return instructionText;
	}

	private void setInstructionText(String text)
	{
		instructionText = text;
	}

	public String getType()
	{
		if (getInstructionText() != null)
		{
			String text = getInstructionText();

			Matcher matcher = INSTRUCTION_PATTERN.matcher(text);
			if (matcher.matches())
			{
				return matcher.group(1);
			}
		}

		throw new RuntimeException("Incorrecte field instruction, kan geen type bepalen!");
	}

	public String getName()
	{
		if (getInstructionText() != null)
		{
			String text = getInstructionText();

			Matcher matcher = INSTRUCTION_PATTERN.matcher(text);
			if (matcher.matches())
			{
				return matcher.group(2);
			}
		}

		throw new RuntimeException("Incorrecte field instruction, kan geen naam bepalen!");
	}

	public String getFormat()
	{
		String format = "";
		if (getInstructionText() != null)
		{
			String text = getInstructionText();

			Matcher matcher = INSTRUCTION_PATTERN.matcher(text);
			if (matcher.matches() && matcher.groupCount() >= 4)
			{
				// datumnotatie via \@
				format = matcher.group(4);
			}
			if (StringUtil.isEmpty(format) && matcher.matches() && matcher.groupCount() >= 6)
			{
				// getalnotatie via \#
				format = matcher.group(6);
			}

			return format;
		}

		throw new RuntimeException("Incorrecte field instruction, kan geen format bepalen!");
	}

	public void setName(String name)
	{
		setInstructionText(getInstructionText().replace(getName(), name));
	}

	@Override
	public RtfFieldInstruction clone() throws CloneNotSupportedException
	{
		RtfFieldInstruction clone = new RtfFieldInstruction();
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
