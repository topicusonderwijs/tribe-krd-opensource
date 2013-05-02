/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.templates.documents.docx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import nl.topicus.cobra.templates.FieldInfo;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.templates.documents.Office2007Document;
import nl.topicus.cobra.templates.exceptions.TemplateCreationException;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.exceptions.TemplateParseException;
import nl.topicus.cobra.templates.monitors.DocumentTemplateProgressMonitor;
import nl.topicus.cobra.templates.resolvers.FieldResolver;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Word 2007 document template. Is een ZIP met een aantal XML-bestanden. Het bestand
 * "word/document.xml" bevat de inhoud van het document. Met de <code>merge</code> methode
 * worden alle fields vervangen door een eenvoudige tekst-node, die gevuld is met de
 * waarde.
 * 
 * @author Laurens Hop
 */
public class Word2007Document extends Office2007Document
{
	private Document document;

	private final XPath xpath;

	private final XPathExpression fieldExpression;

	private final List<Word2007Field> fields;

	private Node body;

	private List<Node> part;

	private DocumentTemplateProgressMonitor monitor;

	private Iterator<Word2007Field> fieldIterator = null;

	/**
	 * default = true, in sommige gevallen wilt de gebruiker misschien de mergefield info
	 * niet meer hebben.
	 */
	private boolean keepMergeFields = KEEP_MERGE_FIELDS_DEFAULT;

	public static boolean KEEP_MERGE_FIELDS_DEFAULT = true;

	/**
	 * Default constructor
	 * 
	 * @throws XPathExpressionException
	 */
	public Word2007Document() throws XPathExpressionException
	{
		fields = new ArrayList<Word2007Field>();
		XPathFactory factory = XPathFactory.newInstance();
		xpath = factory.newXPath();
		xpath.setNamespaceContext(new WordNamespaceContext());
		fieldExpression = xpath.compile("//w:fldSimple | //w:fldChar[@w:fldCharType=\"begin\"]");
	}

	/**
	 * @see nl.topicus.cobra.templates.documents.Office2007Document#read(java.lang.String,
	 *      int, java.io.InputStream)
	 */
	@Override
	protected void read(String filename, int size, InputStream in) throws IOException
	{
		if ("word/document.xml".equals(filename))
		{
			document = readDOM(in, size);
			try
			{
				fields.addAll(extractFields(document));
			}
			catch (XPathExpressionException e)
			{
				throw new IOException(e);
			}
		}
	}

	/**
	 * Zoekt alle fields uit de DOM tree. Dit zijn alle DOM elementen met tag
	 * "w:fldSimple". Hier wordt vervolgens een <code>Word2007Field</code> van gemaakt.
	 * 
	 * @param node
	 * @throws XPathExpressionException
	 */
	private List<Word2007Field> extractFields(Node node) throws XPathExpressionException
	{
		List<Word2007Field> result = new ArrayList<Word2007Field>();
		NodeList nodeList = (NodeList) fieldExpression.evaluate(node, XPathConstants.NODESET);
		for (int i = 0, n = nodeList.getLength(); i < n; i++)
		{
			Node currentNode = nodeList.item(i);
			Word2007Field field = new Word2007Field(currentNode, xpath);
			result.add(field);
		}
		return result;
	}

	/**
	 * Namespacecontext die wordt gebruikt bij het extraheren van fields.
	 */
	private class WordNamespaceContext implements NamespaceContext
	{
		/**
		 * @see javax.xml.namespace.NamespaceContext#getNamespaceURI(java.lang.String)
		 */
		public String getNamespaceURI(String prefix)
		{
			return "http://schemas.openxmlformats.org/wordprocessingml/2006/main";
		}

		/**
		 * @see javax.xml.namespace.NamespaceContext#getPrefix(java.lang.String)
		 */
		public String getPrefix(String namespaceURI)
		{
			return "w";
		}

		/**
		 * @see javax.xml.namespace.NamespaceContext#getPrefixes(java.lang.String)
		 */
		@SuppressWarnings( {"unchecked"})
		public Iterator getPrefixes(String namespaceURI)
		{
			return null;
		}
	}

	/**
	 * Gebruikt de fieldresolver om field informatie van alle gebruikte fields terug te
	 * geven.
	 * 
	 * @see nl.topicus.cobra.templates.documents.DocumentTemplate#getFieldInfo(nl.topicus.cobra.templates.resolvers.FieldResolver)
	 */
	@Override
	public List<FieldInfo> getFieldInfo(FieldResolver resolver)
	{
		List<FieldInfo> result = new ArrayList<FieldInfo>();
		for (Word2007Field field : fields)
		{
			if (field.getType().equals("MERGEFIELD"))
			{
				FieldInfo fieldInfo = field.getFieldInfo(resolver);
				if (fieldInfo != null)
					result.add(fieldInfo);
			}
		}

		return result;
	}

	@Override
	public void mergeDocumentFooter(FieldResolver resolver)
	{
	}

	/**
	 * @see nl.topicus.cobra.templates.documents.Office2007Document#write(java.lang.String,
	 *      java.io.OutputStream)
	 */
	@Override
	protected boolean write(String filename, OutputStream out) throws IOException
	{
		if ("word/document.xml".equals(filename))
		{
			writeDOM(document, out);
			return true;
		}
		return false;
	}

	/**
	 * Geeft het output MIME type terug
	 * 
	 * @see nl.topicus.cobra.templates.documents.DocumentTemplate#getContentType()
	 */
	@Override
	public String getContentType()
	{
		return MIME_TYPE;
	}

	@Override
	public DocumentTemplateType getType()
	{
		return DocumentTemplateType.DOCX;
	}

	/**
	 * Het Word2007 MIME type
	 */
	public static String MIME_TYPE =
		"application/vnd.openxmlformats-officedocument.wordprocessingml.document";

	@Override
	public void writeSection(FieldResolver resolver) throws TemplateException
	{
		try
		{
			// indien fieldIterator != null, dan hadden we de vorige keer afgebroken
			// wegens een {NEXT} veld.
			if (fieldIterator == null)
			{
				if (part == null)
				{
					XPathExpression exp = xpath.compile("//w:body");
					body = (Node) exp.evaluate(document, XPathConstants.NODE);

					NodeList nodes = body.getChildNodes();
					part = new ArrayList<Node>();
					for (int i = 0; i < nodes.getLength(); i++)
						part.add(nodes.item(i));
					for (Node n : part)
						body.removeChild(n);
				}
				else
				{
					// insert page break
					// <w:p>
					// <w:r>
					// <w:br w:type="page"/>
					// </w:r>
					// </w:p>
					Node p = document.createElement("w:p");
					Node r = document.createElement("w:r");
					Node br = document.createElement("w:br");
					Attr a = document.createAttribute("w:type");
					a.setTextContent("page");
					br.getAttributes().setNamedItem(a);
					// Node text =
					// document.createTextNode(field.getMergedResult().toString());
					r.appendChild(br);
					p.appendChild(r);
					body.appendChild(p);

				}

				fields.clear();
				for (Node n : part)
				{
					Node n2 = n.cloneNode(true);
					// Expand property lists (e.g. deelnemer.inschrijvingen[].status)
					expandPropertyLists(resolver, n2);
					fields.addAll(extractFields(n2));
					body.appendChild(n2);
				}
				// Vul de waarden in.
				fieldIterator = fields.iterator();
			}

			boolean aborted = false;
			while (fieldIterator.hasNext() && !aborted)
			{
				Word2007Field field = fieldIterator.next();
				if ("NEXT".equals(field.getType()))
				{
					aborted = true;
					field.clear();
				}
				else
				{
					field.merge(resolver);
				}
				replaceFieldValue(field);
			}

			if (!aborted)
				fieldIterator = null;

			if (monitor != null)
				monitor.afterWriteSection();
		}
		catch (XPathExpressionException e)
		{
			throw new TemplateParseException(e.getLocalizedMessage(), e);
		}
	}

	private void replaceFieldValue(Word2007Field field) throws XPathExpressionException
	{
		if (field.getMergedResult() != null)
		{
			if (field.getNode().getNodeName().equals("w:fldSimple"))
			{
				Node node = field.getNode();
				Node parent = node.getParentNode(); // paragraph node
				Node grandparent = parent.getParentNode();// bijv. body node

				String textContent = field.getMergedResult().toString();
				Matcher match = Pattern.compile("[\r\n\t]").matcher(textContent);
				int groupStart = 0;
				int groupEnd = 0;

				if (match.find())
				{
					Node insertBeforeNode = parent.getNextSibling();
					Node currentParagraph = parent;
					parent.removeChild(node);

					do
					{
						groupEnd = match.start();
						String partialText = textContent.substring(groupStart, groupEnd);
						String group = match.group();
						groupStart = match.end();

						if (!"".equals(partialText))
						{
							Node textR = createTextNode(partialText, node);
							currentParagraph.appendChild(textR);
						}

						if (group.contains("\n"))
						{
							Node newCurrentParagraph = currentParagraph.cloneNode(false);

							XPathExpression exp = xpath.compile("./w:pPr");
							Node textPPr =
								(Node) exp.evaluate(currentParagraph, XPathConstants.NODE);
							if (textPPr != null)
								newCurrentParagraph.appendChild(textPPr);

							currentParagraph = newCurrentParagraph;
							grandparent.insertBefore(currentParagraph, insertBeforeNode);
							insertBeforeNode = currentParagraph.getNextSibling();
						}
						else if (group.equals("\t"))
						{
							currentParagraph.appendChild(createTabNode(node));
						}
					}
					while (match.find());

					String lastPart = textContent.substring(groupStart, textContent.length());
					if (!lastPart.isEmpty())
					{
						Node textR = createTextNode(lastPart, node);
						currentParagraph.appendChild(textR);
					}
				}
				else
				{
					Node textR = createTextNode(textContent, node);
					parent.replaceChild(textR, node);
				}

			}
			else if (field.getNode().getNodeName().equals("w:fldChar")
				&& !"FORMCHECKBOX".equals(field.getType()))
			{
				Node current = field.getNode().getParentNode().getNextSibling();
				boolean endFound = false;
				XPathExpression exp = xpath.compile("./w:fldChar[@w:fldCharType=\"end\"]");
				while (!endFound && current.getNextSibling() != null)
				{
					Node end = (Node) exp.evaluate(current, XPathConstants.NODE);
					endFound = end != null;
					Node next = current.getNextSibling();
					current.getParentNode().removeChild(current);
					current = next;
				}
				Node r = document.createElement("w:r");
				Node t = document.createElement("w:t");
				Node text = document.createTextNode(field.getMergedResult().toString());
				t.appendChild(text);
				r.appendChild(t);
				field.getNode().getParentNode().replaceChild(r, field.getNode());
			}
		}
	}

	private Node createTextNode(String partialText, Node originalNode)
			throws XPathExpressionException
	{
		Node textR = originalNode.cloneNode(true).getFirstChild();
		XPathExpression exp = xpath.compile("./w:t");
		Node textT = (Node) exp.evaluate(textR, XPathConstants.NODE);
		if (textT != null)
			textT.setTextContent(partialText);

		return textR;
	}

	private Node createTabNode(Node originalNode) throws XPathExpressionException
	{
		Node textR = originalNode.cloneNode(true).getFirstChild();
		XPathExpression exp = xpath.compile("./w:t");
		Node textT = (Node) exp.evaluate(textR, XPathConstants.NODE);
		if (textT != null)
			textR.replaceChild(this.document.createElement("w:tab"), textT);

		return textR;
	}

	@SuppressWarnings("unused")
	@Override
	public void mergeDocumentHeader(FieldResolver resolver) throws TemplateException
	{
	}

	@Override
	public void writeDocumentFooter() throws TemplateException
	{
		try
		{
			if (fieldIterator != null)
			{
				while (fieldIterator.hasNext())
				{
					Word2007Field field = fieldIterator.next();
					field.clear();
					replaceFieldValue(field);
				}
			}
			write(getOutputStream());
		}
		catch (IOException e)
		{
			throw new TemplateCreationException(e.getLocalizedMessage(), e);
		}
		catch (XPathExpressionException e)
		{
			throw new TemplateParseException(e.getLocalizedMessage(), e);
		}

	}

	@SuppressWarnings("unused")
	@Override
	public void writeDocumentHeader() throws TemplateException
	{
	}

	public static Word2007Document createDocument(InputStream inStream) throws TemplateException
	{
		Word2007Document worddoc;

		try
		{
			worddoc = new Word2007Document();
			worddoc.read(inStream);
		}
		catch (IOException e)
		{
			throw new TemplateCreationException("Word template kon niet worden gemaakt.", e);
		}
		catch (XPathExpressionException e)
		{
			throw new TemplateCreationException("Word template kon niet worden gemaakt.", e);
		}

		return worddoc;
	}

	@SuppressWarnings("unused")
	@Override
	public void writePageFooter() throws TemplateException
	{
	}

	@SuppressWarnings("unused")
	@Override
	public void writePageHeader() throws TemplateException
	{
	}

	@Override
	public void setProgressMonitor(DocumentTemplateProgressMonitor monitor)
	{
		this.monitor = monitor;
	}

	/**
	 * @see nl.topicus.cobra.templates.documents.DocumentTemplate#isKeepMergeFields()
	 */
	public boolean isKeepMergeFields()
	{
		return keepMergeFields;
	}

	public void setKeepMergeFields(boolean keepMergeFields)
	{
		this.keepMergeFields = keepMergeFields;
	}

	private void expandPropertyLists(FieldResolver resolver, Node nodeToExpand)
			throws XPathExpressionException
	{
		List<Word2007Field> extractedFields = extractFields(nodeToExpand);
		for (Word2007Field field : extractedFields)
		{
			Node currentNode = field.getNode();
			if (field.getName().contains("[]"))
			{
				String prop = field.getName().substring(0, field.getName().indexOf("[]"));
				Object v = resolver.resolve(prop);

				if (v != null && v instanceof Collection< ? >)
				{
					// Zoek een parent van het type <w:tr>
					Node rowNode = null;
					Node parentNode = currentNode;
					while (rowNode == null && parentNode.getParentNode() != null)
					{
						if (parentNode.getLocalName().equals("tr"))
						{
							rowNode = parentNode;
						}
						else
						{
							parentNode = parentNode.getParentNode();
						}
					}
					if (rowNode != null)
					{
						// n merge field blokken aanmaken
						for (int i = 0; i < ((Collection< ? >) v).size(); i++)
						{
							Node newRow = rowNode.cloneNode(true);
							fillInListIndex(newRow, i);
							rowNode.getParentNode().insertBefore(newRow, rowNode);
						}
						// Verwijder de oorspronkelijke regel.
						rowNode.getParentNode().removeChild(rowNode);
					}
				}
			}
		}
	}

	private void fillInListIndex(Node rowNode, int index) throws XPathExpressionException
	{
		List<Word2007Field> extractedFields = extractFields(rowNode);
		for (Word2007Field field : extractedFields)
		{
			String name = field.getName();
			if (name.contains("[]"))
			{
				field.setName(name.replace("[]", "[" + index + "]"));
			}
		}
	}

	@Override
	public boolean hasSections()
	{
		return true;
	}
}
