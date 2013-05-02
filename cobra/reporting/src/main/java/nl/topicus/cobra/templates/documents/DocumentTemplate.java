package nl.topicus.cobra.templates.documents;

import java.io.OutputStream;
import java.util.List;

import nl.topicus.cobra.templates.FieldInfo;
import nl.topicus.cobra.templates.TemplateManager;
import nl.topicus.cobra.templates.documents.jrxml.JasperReportsTemplate;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.cobra.templates.monitors.DocumentTemplateProgressMonitor;
import nl.topicus.cobra.templates.resolvers.FieldResolver;

/**
 * Generieke document template interface. Bevat een aantal simpele functies om een
 * template te genereren. De volgorde voor genereren is:
 * <ul>
 * <li>
 * {@link TemplateManager#createDocumentTemplateByFileExt(String, java.io.InputStream)}</li>
 * <li>{@link DocumentTemplate#setOutputStream(OutputStream)}</li>
 * <li>{@link DocumentTemplate#mergeDocumentHeader(FieldResolver)}</li>
 * <li>{@link DocumentTemplate#mergeDocumentFooter(FieldResolver)}</li>
 * <li>{@link DocumentTemplate#writeDocumentHeader()}</li>
 * <li>{@link DocumentTemplate#writeSection(FieldResolver)} <-- per entiteit dat in de
 * rapportage moet verschijnen</li>
 * <li>{@link DocumentTemplate#writeDocumentFooter()}</li>
 * </ul>
 * 
 * @author Laurens Hop
 * @author hoeve
 */
public interface DocumentTemplate
{
	/**
	 * variabele welke de keyname in {@link FieldResolver} bevat voor de waarde waar de
	 * context over gaat.
	 */
	public static final String CONTEXT_OBJECT_REF_NAME = "CONTEXT_OBJECT_REF_NAME";

	/**
	 * Mergt de template met een externe data context. Het gaat hier alleen om de header
	 * van het document, niet van een pagina.
	 * 
	 * @param resolver
	 *            FieldResolver die de velden gaat opzoeken
	 */
	public void mergeDocumentHeader(FieldResolver resolver) throws TemplateException;

	/**
	 * Mergt de template met een externe data context. Het gaat hier alleen om de footer
	 * van het document, niet van een pagina.
	 * 
	 * @param resolver
	 *            FieldResolver die de velden gaat opzoeken
	 */
	public void mergeDocumentFooter(FieldResolver resolver) throws TemplateException;

	/**
	 * Schrijft het body deel van de template. Meestal is dit voor 1 entiteit.
	 * 
	 * @param resolver
	 * @throws TemplateException
	 */
	public void writeSection(FieldResolver resolver) throws TemplateException;

	/**
	 * Schrijf een nieuwe header voor het document.
	 */
	public void writeDocumentHeader() throws TemplateException;

	/**
	 * Schrijf een nieuwe footer voor het document.
	 */
	public void writeDocumentFooter() throws TemplateException;

	/**
	 * Schrijf een nieuwe header voor volgende sectie.
	 */
	public void writePageHeader() throws TemplateException;

	/**
	 * Schrijf een nieuwe footer voor de huidige sectie.
	 */
	public void writePageFooter() throws TemplateException;

	/**
	 * Geeft een lijst van alle velden die gebruikt worden in deze template.
	 * 
	 * @param resolver
	 *            FieldResolver die de info over de velden gaat opzoeken
	 * @return een lijst van alle velden + info die in het document voorkomen
	 */
	public List<FieldInfo> getFieldInfo(FieldResolver resolver);

	/**
	 * @return MIME type van het output bestand
	 */
	public String getContentType();

	/**
	 * @return mooie enum van het output bestand, welke de mime type en type omschrijving
	 *         bevat.
	 */
	public DocumentTemplateType getType();

	public void setOutputStream(OutputStream outStream);

	public OutputStream getOutputStream();

	/**
	 * @param monitor
	 *            je gewenste monitor in om zo eventueel een progress aan je background te
	 *            koppelen
	 */
	public void setProgressMonitor(DocumentTemplateProgressMonitor monitor);

	/**
	 * @return Geeft aan of het resultaat document de merge velden zal houden. Standaard
	 *         zou dit true moeten zijn.
	 */
	public boolean isKeepMergeFields();

	/**
	 * @see DocumentTemplate#isKeepMergeFields()
	 * @param keepMergeFields
	 */
	public void setKeepMergeFields(boolean keepMergeFields);

	/**
	 * Geeft aan of de functie {@link #writeSection(FieldResolver)} wel nut heeft. Bij de
	 * {@link JasperReportsTemplate} wordt het hele document in 1 keer gemaakt, het is dus
	 * niet mogelijk om per sectie iets te schrijven.
	 */
	public boolean hasSections();
}
