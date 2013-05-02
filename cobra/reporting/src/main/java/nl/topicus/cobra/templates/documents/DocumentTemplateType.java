package nl.topicus.cobra.templates.documents;

import nl.topicus.cobra.templates.documents.docx.Word2007Document;
import nl.topicus.cobra.templates.documents.docx.Word2007MacroDocument;
import nl.topicus.cobra.templates.documents.docx.Word2007MacroTemplate;
import nl.topicus.cobra.templates.documents.docx.Word2007Template;
import nl.topicus.cobra.templates.documents.jrxml.JasperReportsTemplate;
import nl.topicus.cobra.templates.documents.rtf.RtfDocument;
import nl.topicus.cobra.templates.documents.xls.ExcelWorkbook;
import nl.topicus.cobra.templates.documents.xlsx.Excel2007Workbook;
import nl.topicus.cobra.templates.documents.zip.ZipFileDocument;

/**
 * Enum met wat meer informatie over het object, met een enum is dit makkelijker te
 * vergelijken over alle applicaties heen.
 * 
 * @author hoeve
 */
public enum DocumentTemplateType
{

	CSV
	{
		@Override
		public String getOmschrijving()
		{
			return "CSV bestand";
		}

		@Override
		public String getMimeType()
		{
			return "text/csv";
		}

		@Override
		public String getFileExtension()
		{
			return "csv";
		}
	},
	RTF
	{
		@Override
		public String getOmschrijving()
		{
			return "RTF document";
		}

		@Override
		public String getMimeType()
		{
			return RtfDocument.MIME_TYPE;
		}

		@Override
		public String getFileExtension()
		{
			return "rtf";
		}
	},
	JRXML
	{
		@Override
		public String getOmschrijving()
		{
			return "Jasper Reports document";
		}

		@Override
		public String getMimeType()
		{
			return JasperReportsTemplate.MIME_TYPE;
		}

		@Override
		public String getFileExtension()
		{
			return "pdf";
		}
	},
	PDF
	{
		@Override
		public String getOmschrijving()
		{
			return "PDF document";
		}

		@Override
		public String getMimeType()
		{
			return "application/pdf";
		}

		@Override
		public String getFileExtension()
		{
			return "pdf";
		}
	},
	XLS
	{
		@Override
		public String getOmschrijving()
		{
			return "Excel document";
		}

		@Override
		public String getMimeType()
		{
			return ExcelWorkbook.MIME_TYPE;
		}

		@Override
		public String getFileExtension()
		{
			return "xls";
		}
	},

	XLSX
	{
		@Override
		public String getOmschrijving()
		{
			return "Excel 2007 document";
		}

		@Override
		public String getMimeType()
		{
			return Excel2007Workbook.MIME_TYPE;
		}

		@Override
		public String getFileExtension()
		{
			return "xlsx";
		}
	},

	DOCX
	{
		@Override
		public String getOmschrijving()
		{
			return "Word 2007 document";
		}

		@Override
		public String getMimeType()
		{
			return Word2007Document.MIME_TYPE;
		}

		@Override
		public String getFileExtension()
		{
			return "docx";
		}
	},
	DOTX
	{
		@Override
		public String getOmschrijving()
		{
			return "Word 2007 template";
		}

		@Override
		public String getMimeType()
		{
			return Word2007Template.MIME_TYPE;
		}

		@Override
		public String getFileExtension()
		{
			return "dotx";
		}
	},
	DOCM
	{
		@Override
		public String getOmschrijving()
		{
			return "Word 2007 document (macro-enabled)";
		}

		@Override
		public String getMimeType()
		{
			return Word2007MacroDocument.MIME_TYPE;
		}

		@Override
		public String getFileExtension()
		{
			return "docm";
		}
	},
	DOTM
	{
		@Override
		public String getOmschrijving()
		{
			return "Word 2007 template (macro-enabled)";
		}

		@Override
		public String getMimeType()
		{
			return Word2007MacroTemplate.MIME_TYPE;
		}

		@Override
		public String getFileExtension()
		{
			return "dotm";
		}
	},
	ZIP
	{
		@Override
		public String getOmschrijving()
		{
			return "Zip bestand";
		}

		@Override
		public String getMimeType()
		{
			return ZipFileDocument.MIME_TYPE;
		}

		@Override
		public String getFileExtension()
		{
			return "zip";
		}
	};

	/**
	 * @return een mooie omschrijving van het bestandstype.
	 */
	public abstract String getOmschrijving();

	/**
	 * @return geeft de mimetype in string formaat terug.
	 */
	public abstract String getMimeType();

	/**
	 * @return geeft de extensie terug welke bij deze type hoort.
	 */
	public abstract String getFileExtension();

}
