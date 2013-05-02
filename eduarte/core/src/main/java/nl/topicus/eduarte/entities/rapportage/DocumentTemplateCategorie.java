package nl.topicus.eduarte.entities.rapportage;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratiePanel;
import nl.topicus.eduarte.web.components.resultaat.ResultaatRapportageConfiguratiePanel;

public enum DocumentTemplateCategorie
{
	Intake,
	Identiteit,
	Verbintenis,
	Brieven,
	Resultaten
	{
		@Override
		public DocumentTemplateContext getBeperkteContext()
		{
			return DocumentTemplateContext.Verbintenis;
		}

		@Override
		public Class<ResultaatRapportageConfiguratiePanel> getConfiguratiePanel()
		{
			return ResultaatRapportageConfiguratiePanel.class;
		}
	},

	/**
	 * Een enum waarde welke alleen bestemd is voor de {@link OnderwijsDocumentTemplate}.
	 * Omdat we niet kunnen erven van een Enum moeten het maar zo.
	 */
	Examens,

	/**
	 * Een enum waarde welke alleen bestemd is voor de {@link OnderwijsDocumentTemplate}.
	 * Omdat we niet kunnen erven van een Enum moeten het maar zo.
	 */
	Onderwijsovereenkomst,

	/**
	 * Een enum waarde welke alleen bestemd is voor de {@link OnderwijsDocumentTemplate}.
	 * Omdat we niet kunnen erven van een Enum moeten het maar zo.
	 */
	BPVOvereenkomst,

	/**
	 * Een enum waarde welke alleen bestemd is voor templates in andere modules. Omdat we
	 * niet kunnen erven van een Enum moet het maar zo.
	 */
	Overig;

	/**
	 * Alternatief voor values(), dit geeft niet de
	 * {@link DocumentTemplateCategorie#Examens} en
	 * {@link DocumentTemplateCategorie#Onderwijsovereenkomst} en
	 * {@link DocumentTemplateCategorie#BPVOvereenkomst} waarde mee.
	 */
	public static DocumentTemplateCategorie[] getValues()
	{
		List<DocumentTemplateCategorie> values = new ArrayList<DocumentTemplateCategorie>();
		values.add(DocumentTemplateCategorie.Intake);
		values.add(DocumentTemplateCategorie.Identiteit);
		values.add(DocumentTemplateCategorie.Verbintenis);
		values.add(DocumentTemplateCategorie.Brieven);
		values.add(DocumentTemplateCategorie.Resultaten);
		values.add(DocumentTemplateCategorie.Overig);

		return values.toArray(new DocumentTemplateCategorie[0]);
	}

	public static DocumentTemplateCategorie[] getExamenValues()
	{
		List<DocumentTemplateCategorie> values = new ArrayList<DocumentTemplateCategorie>();
		values.add(DocumentTemplateCategorie.Examens);
		values.add(DocumentTemplateCategorie.Onderwijsovereenkomst);
		values.add(DocumentTemplateCategorie.BPVOvereenkomst);

		return values.toArray(new DocumentTemplateCategorie[0]);
	}

	public DocumentTemplateContext getBeperkteContext()
	{
		return null;
	}

	public Class< ? extends RapportageConfiguratiePanel< ? extends IBijlageKoppelEntiteit< ? extends BijlageEntiteit>>> getConfiguratiePanel()
	{
		return null;
	}
}
