/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.templates.documents;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nl.topicus.cobra.templates.FieldInfo;
import nl.topicus.cobra.templates.resolvers.FieldResolver;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author hop
 */
public abstract class Office2007Document implements DocumentTemplate
{
	protected byte[] originalArchive;

	private OutputStream outStream;

	/**
	 * @see nl.topicus.cobra.templates.documents.DocumentTemplate#getContentType()
	 */
	@Override
	public abstract String getContentType();

	/**
	 * @see nl.topicus.cobra.templates.documents.DocumentTemplate#getFieldInfo(nl.topicus.cobra.templates.resolvers.FieldResolver)
	 */
	@Override
	public abstract List<FieldInfo> getFieldInfo(FieldResolver resolver);

	/**
	 * @see nl.topicus.cobra.templates.documents.DocumentTemplate#mergeDocumentFooter(nl.topicus.cobra.templates.resolvers.FieldResolver)
	 */
	@Override
	public abstract void mergeDocumentFooter(FieldResolver resolver);

	/**
	 * Leest Word document in uit input stream. Het oorspronkelijke ZIP-archief blijft
	 * behouden. De <code>read()</code> methode verwerkt vervolgens de inhoud.
	 */
	public void read(InputStream stream) throws IOException
	{
		int estimatedSize = stream.available();
		if (estimatedSize < 1024)
			estimatedSize = 1024;
		ByteArrayOutputStream out = new ByteArrayOutputStream(estimatedSize);
		int b;
		while ((b = stream.read()) >= 0)
			out.write(b);

		originalArchive = out.toByteArray();
		read();
	}

	/**
	 * Schrijft het document weer als een Word2007-bestand weg. Alle bestanden uit het
	 * oorspronkelijke archief worden overgezet, alleen "word/document.xml" wordt
	 * vervangen door de nieuwe DOM tree.
	 */
	public void write(OutputStream stream) throws IOException
	{
		ZipOutputStream zipOut = new ZipOutputStream(stream);
		ZipInputStream zipIn = new ZipInputStream(new ByteArrayInputStream(originalArchive));
		ZipEntry entry = zipIn.getNextEntry();
		while (entry != null)
		{
			zipOut.putNextEntry(new ZipEntry(entry.getName()));
			if (!write(entry.getName(), zipOut))
			{
				byte[] buffer = new byte[4096];
				int n = 0;
				while ((n = zipIn.read(buffer, 0, 4096)) > 0)
					zipOut.write(buffer, 0, n);
			}

			zipIn.closeEntry();
			zipOut.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
		zipOut.close();
	}

	@Override
	public void setOutputStream(OutputStream outStream)
	{
		this.outStream = outStream;
	}

	@Override
	public OutputStream getOutputStream()
	{
		return outStream;
	}

	protected abstract void read(String filename, int size, InputStream in) throws IOException;

	protected abstract boolean write(String filename, OutputStream out) throws IOException;

	/**
	 * Verwerkt het ZIP archief. Het bestand "word/document.xml" wordt als DOM tree apart
	 * gezet. Vervolgens worden de fields uit dat document apart bijgehouden.
	 * 
	 * @throws IOException
	 */
	public void read() throws IOException
	{
		ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(originalArchive));
		ZipEntry entry = zip.getNextEntry();
		while (entry != null)
		{
			read(entry.getName(), (int) entry.getSize(), zip);
			zip.closeEntry();
			entry = zip.getNextEntry();
		}
	}

	/**
	 * Hulpfunctie voor het wegschrijven van DOM documenten naar een stream.
	 * 
	 * @param document
	 * @param out
	 * @throws IOException
	 */
	protected void writeDOM(Document document, OutputStream out) throws IOException
	{
		try
		{
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			OutputStreamWriter writer = new OutputStreamWriter(out, Charset.forName("UTF-8"));
			// StringWriter w2 = new StringWriter();
			StreamResult result = new StreamResult(writer);
			transformer.transform(new DOMSource(document), result);
			// System.out.println(w2.toString());
		}
		catch (TransformerException e)
		{
			throw new IOException(e);
		}
	}

	/**
	 * Hulpfunctie voor het lezen van een DOM XML-document
	 * 
	 * @throws IOException
	 */
	protected Document readDOM(InputStream in, int size) throws IOException
	{
		byte[] buffer = new byte[size];
		int index = 0;
		int n = 0;
		do
		{
			n = in.read(buffer, index, size - index);
			index += n;
		}
		while (n > 0);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		try
		{
			return factory.newDocumentBuilder().parse(new ByteArrayInputStream(buffer));
		}
		catch (SAXException e)
		{
			throw new IOException(e);
		}
		catch (ParserConfigurationException e)
		{
			throw new IOException(e);
		}

	}

	@Override
	public boolean hasSections()
	{
		return true;
	}
}
