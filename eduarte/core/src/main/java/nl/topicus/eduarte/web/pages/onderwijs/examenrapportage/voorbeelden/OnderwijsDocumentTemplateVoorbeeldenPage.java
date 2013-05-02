package nl.topicus.eduarte.web.pages.onderwijs.examenrapportage.voorbeelden;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijsDocumentTemplateRead;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.PackageResourceStream;

/**
 * Pagina met een aantal voorbeelden van onderwijs samenvoegdocumenten (diploma's,
 * cijferlijsten etc)
 * 
 * @author loite
 */
@PageInfo(title = "Voorbeeldsjablonen", menu = {"Onderwijs > Rapportage > Voorbeelden"})
@InPrincipal(OnderwijsDocumentTemplateRead.class)
public class OnderwijsDocumentTemplateVoorbeeldenPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	private abstract static class FileResource extends WebResource
	{
		private static final long serialVersionUID = 1L;

		private final String bestand;

		public FileResource(String bestand)
		{
			this.bestand = bestand;
		}

		public abstract String getContentType();

		@Override
		public IResourceStream getResourceStream()
		{
			return new PackageResourceStream(OnderwijsDocumentTemplateVoorbeeldenPage.class,
				bestand)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public String getContentType()
				{
					return FileResource.this.getContentType();
				}

			};
		}

		@Override
		protected void setHeaders(WebResponse response)
		{
			super.setHeaders(response);
			response.setHeader("Content-Disposition", "attachment; filename=\"" + bestand + "\"");
		}
	}

	private static final class Word2007Resource extends FileResource
	{
		private static final long serialVersionUID = 1L;

		public Word2007Resource(String bestand)
		{
			super(bestand);
		}

		@Override
		public String getContentType()
		{
			return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		}
	}

	private static final class JasperReportsResource extends FileResource
	{
		private static final long serialVersionUID = 1L;

		public JasperReportsResource(String bestand)
		{
			super(bestand);
		}

		@Override
		public String getContentType()
		{
			return "application/pdf";
		}
	}

	private static final class RTFResource extends FileResource
	{
		private static final long serialVersionUID = 1L;

		public RTFResource(String bestand)
		{
			super(bestand);
		}

		@Override
		public String getContentType()
		{
			return "text/rtf";
		}
	}

	private static final class CSVResource extends FileResource
	{
		private static final long serialVersionUID = 1L;

		public CSVResource(String bestand)
		{
			super(bestand);
		}

		@Override
		public String getContentType()
		{
			return "text/csv";
		}
	}

	private static final class ExcelResource extends FileResource
	{
		private static final long serialVersionUID = 1L;

		public ExcelResource(String bestand)
		{
			super(bestand);
		}

		@Override
		public String getContentType()
		{
			return "application/vnd.ms-excel";
		}
	}

	public OnderwijsDocumentTemplateVoorbeeldenPage()
	{
		super(CoreMainMenuItem.Onderwijs);
		add(new ResourceLink<Void>("vwoDiploma", new Word2007Resource("VWO Diploma.docx")));
		add(new ResourceLink<Void>("havoDiploma", new Word2007Resource("Havo Diploma.docx")));
		add(new ResourceLink<Void>("vmboDiploma", new Word2007Resource("VMBO Diploma.docx")));
		add(new ResourceLink<Void>("vwoCijferlijst", new Word2007Resource("VWO Cijferlijst.docx")));
		add(new ResourceLink<Void>("vavoHavoCijferlijst", new Word2007Resource(
			"VAVO_cijf_cert.docx")));
		add(new ResourceLink<Void>("tlGlCijferlijst", new Word2007Resource(
			"VMBO Cijferlijst_GL_TL.docx")));
		add(new ResourceLink<Void>("bblKblCijferlijst", new Word2007Resource(
			"VMBO Cijferlijst_BL-KL-LWT.docx")));
		add(new ResourceLink<Void>("mboOudCijferlijst", new RTFResource("cijf_diploma_blanco.rtf")));
		add(new ResourceLink<Void>("certificaatNT2", new Word2007Resource("NT2_CERT.docx")));
		add(new ResourceLink<Void>("cgoAchterzijde", new Word2007Resource(
			"Sjabloon_achterzijde diploma-certificaat.docx")));
		add(new ResourceLink<Void>("cgoVoorzijde", new Word2007Resource(
			"Sjabloon_voorzijde diploma.docx")));
		add(new ResourceLink<Void>("cgoCertificaat", new Word2007Resource(
			"Sjabloon_certificaat.docx")));
		add(new ResourceLink<Void>("cgoDiploma", new Word2007Resource("Sjabloon_diploma.docx")));
		add(new ResourceLink<Void>("cgoVerklaring",
			new Word2007Resource("Sjabloon_verklaring.docx")));
		add(new ResourceLink<Void>("mboDiplomaExCijfers", new RTFResource(
			"MBO Diploma excl cijfers.rtf")));
		add(new ResourceLink<Void>("mboDiplomaIncCijfers", new RTFResource(
			"MBO Diploma incl cijfers.rtf")));
		add(new ResourceLink<Void>("onderwijsovereenkomst", new Word2007Resource(
			"onderwijsovereenkomst.docx")));
		add(new ResourceLink<Void>("praktijkovereenkomst", new Word2007Resource(
			"praktijkovereenkomst.docx")));
		add(new ResourceLink<Void>("vergaderlijstexamenuitslag", new JasperReportsResource(
			"voorbeeld_vergaderlijst.jrxml")));
		add(new ResourceLink<Void>("voorbeeldFactuur", new RTFResource("Voorbeeld factuur.rtf")));
		add(new ResourceLink<Void>("voorbeeldFactuurDocx", new Word2007Resource(
			"Voorbeeld factuur.docx")));
		add(new ResourceLink<Void>("debiteuren", new CSVResource("debiteuren.csv")));
		add(new ResourceLink<Void>("boekingen", new CSVResource("boekingen.csv")));
		add(new ResourceLink<Void>("verkoopfacturen", new CSVResource("verkoopfacturen.csv")));
		add(new ResourceLink<Void>("vergaderlijst_v2", new JasperReportsResource(
			"voorbeeld_vergaderlijst_v2.jrxml")));
		add(new ResourceLink<Void>("volledigecijferlijst", new ExcelResource(
			"volledigecijferlijst.xls")));
		add(new ResourceLink<Void>("resultatenvergaderlijst", new ExcelResource(
			"resultatenvergaderlijst.xls")));

		createComponents();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.Voorbeelden);
	}

}
