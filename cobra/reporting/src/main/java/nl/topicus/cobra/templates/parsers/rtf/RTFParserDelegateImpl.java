package nl.topicus.cobra.templates.parsers.rtf;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.templates.documents.rtf.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etranslate.tm.processing.rtf.RTFParser;
import com.etranslate.tm.processing.rtf.RTFParserDelegate;

public class RTFParserDelegateImpl implements RTFParserDelegate
{
	public static final Logger log = LoggerFactory.getLogger(RTFParserDelegateImpl.class);

	private RtfDocument document;

	private AbstractRtfGroup currentGroup;

	private List<IRtfElement> stack;

	private boolean isDocumentStart = false;

	private boolean isDocumentHeader = false;

	private boolean mustOpenGroup = false;

	private boolean inTable = false;

	private boolean mustCloseTableRow = false;

	public RTFParserDelegateImpl()
	{
	}

	public RTFParserDelegateImpl(RtfDocument document)
	{
		this.document = document;
	}

	public RtfDocument getDocument()
	{
		return document;
	}

	@Override
	public void startDocument()
	{
		stack = new ArrayList<IRtfElement>(3);

		if (document == null)
			document = new RtfDocument();

		currentGroup = document;

		isDocumentStart = true;
		isDocumentHeader = true;
	}

	@Override
	public void endDocument()
	{
	}

	@Override
	public void openGroup(int depth)
	{
		if (isDocumentStart)
		{
			mustOpenGroup = false;
			isDocumentStart = false;
		}
		else
		{
			mustOpenGroup = true;
		}
	}

	@Override
	public void closeGroup(int depth)
	{
		if (currentGroup == null)
			log.error("whellep?");

		currentGroup = currentGroup.getParent();

		if (mustCloseTableRow)
		{
			currentGroup = currentGroup.getParent();
			mustCloseTableRow = false;
		}
	}

	@Override
	public void controlSymbol(String controlSymbol, int context)
	{
		controlWord(controlSymbol, RTFParser.VALUE_NOT_SPECIFIED, context);
	}

	@Override
	public void controlWord(String controlWord, int value, int context)
	{
		RtfControlWord word;
		if (value == RTFParser.VALUE_NOT_SPECIFIED)
			word = new RtfControlWord(controlWord);
		else
			word = new RtfControlWord(controlWord, value);

		if (controlWord.equals("\\*"))
		{
			stack.add(word);
			return;
		}

		if (mustOpenGroup)
		{
			AbstractRtfGroup group = null;

			if (controlWord.equals("\\field"))
			{
				group = new RtfField(currentGroup);
			}
			else if (controlWord.equals("\\fldinst"))
			{
				group = new RtfFieldInstruction(currentGroup);
			}
			else if (controlWord.equals("\\fldrslt"))
			{
				group = new RtfFieldResult(currentGroup);

			}
			else
			{
				group = new RtfControlGroup(currentGroup);
			}

			if (!stack.isEmpty())
			{
				group.addElements(stack);
				stack.clear();
			}

			group.addElement(word);
			currentGroup.addElement(group);
			currentGroup = group;

			mustOpenGroup = false;
		}
		else if (isDocumentHeader && currentGroup instanceof RtfDocument)
		{
			isDocumentHeader = false;

			RtfSection section = new RtfSection(currentGroup);
			((RtfDocument) currentGroup).setSection(section);
			currentGroup = section;

			if (controlWord.equals("\\trowd"))
			{
				RtfTableRow row = new RtfTableRow(currentGroup);
				row.addElement(word);
				currentGroup.addElement(row);
				currentGroup = row;
				inTable = true;
			}
			else
			{
				currentGroup.addElement(word);
			}

		}
		else if (controlWord.equals("\\trowd")
			&& !(currentGroup.getParent() instanceof RtfTableRow))
		{
			RtfTableRow row = new RtfTableRow(currentGroup);
			row.addElement(word);
			currentGroup.addElement(row);
			currentGroup = row;
			inTable = true;
		}
		else if (controlWord.equals("\\pard") && inTable && !(currentGroup instanceof RtfTableRow))
		{
			RtfTableRow row = new RtfTableRow(currentGroup);
			row.addElement(word);
			currentGroup.addElement(row);
			currentGroup = row;
		}
		else if (controlWord.equals("\\row"))
		{
			currentGroup.addElement(word);
			mustCloseTableRow = true;
		}
		else if (controlWord.equals("\\lastrow"))
		{
			inTable = false;
			currentGroup.addElement(word);
		}
		else
			currentGroup.addElement(word);
	}

	@Override
	public void delimiter(String delimiter, int context)
	{
		if (mustOpenGroup)
		{
			stack.add(new RtfDelimiter(delimiter));
			return;
		}

		currentGroup.addElement(new RtfDelimiter(delimiter));
	}

	@Override
	public void text(String text, String style, int context)
	{
		if (mustOpenGroup)
		{
			RtfControlGroup group = new RtfControlGroup(currentGroup);

			currentGroup.addElement(group);
			currentGroup = group;
		}

		if (!currentGroup.getElements().isEmpty())
		{
			IRtfElement lastElement =
				currentGroup.getElements().get(currentGroup.getElements().size() - 1);
			if (lastElement instanceof RtfText)
			{
				((RtfText) lastElement).setText(((RtfText) lastElement).getText().concat(text));
				return;
			}
		}

		currentGroup.addElement(new RtfText(text));
	}

	@Override
	public OutputStream getNextOutputStream(int context)
	{
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void styleList(List styles)
	{
	}
}
