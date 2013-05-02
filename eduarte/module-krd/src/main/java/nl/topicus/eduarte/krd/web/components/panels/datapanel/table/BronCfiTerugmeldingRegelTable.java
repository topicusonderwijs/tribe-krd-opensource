package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiRegelType;
import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiTerugmeldingRegel;

/**
 * @author vandekamp
 */
public class BronCfiTerugmeldingRegelTable extends
		CustomDataPanelContentDescription<BronCfiTerugmeldingRegel>
{
	private static final long serialVersionUID = 1L;

	public BronCfiTerugmeldingRegelTable(BronCfiRegelType regelType)
	{
		super(regelType.getOmschrijving());
		createColumns(regelType);
	}

	private void createColumns(BronCfiRegelType regelType)
	{
		addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("Crebo", "Crebo", "creboCode",
			"creboCode"));
		if (BronCfiRegelType.BEK != regelType)
			addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("Leerweg", "Leerweg",
				"leerweg", "leerweg"));
		switch (regelType)
		{
			case BEK:
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("Opleiding",
					"Opleiding", "opleiding", "opleiding"));
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("VT BOL 1-10",
					"VT BOL 1-10", "aantalInschrVtBolOp0110", "aantalInschrVtBolOp0110"));
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("DT BBL 1-10",
					"DT BBL 1-10", "aantalInschrDtBblOp0110", "aantalInschrDtBblOp0110"));
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("DT DTBOL 1-10",
					"DT DTBOL 1-10", "aantalInschrDtDtbolOp0110", "aantalInschrDtDtbolOp0110"));
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("Diplomas 1-10",
					"Diplomas 1-10", "aantalDiplomasOp0110", "aantalDiplomasOp0110"));
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("VT BOL 1-2",
					"VT BOL 1-2", "aantalInschrVtBolOp0102", "aantalInschrVtBolOp0102"));
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("DT BBL 1-2",
					"DT BBL 1-2", "aantalInschrDtBblOp0102", "aantalInschrDtBblOp0102"));
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("DT DTBOL 1-2",
					"DT DTBOL 1-2", "aantalInschrDtDtbolOp0102", "aantalInschrDtDtbolOp0102"));
				break;
			case EXP:
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("Opleiding",
					"Opleiding", "opleiding", "opleiding"));
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("Aantal deelnemers",
					"Aantal deelnemers", "aantalDeelnemers", "aantalDeelnemers"));
				break;
			case SAG:
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("Signaal", "Signaal",
					"sagSignaal", "sagSignaal"));
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("Inschr sign 1-10",
					"Inschr sign 1-10", "aantalInschrSignalenOp0110", "aantalInschrSignalenOp0110"));
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("Inschr. sign 1-2",
					"Inschr sign. 1-2", "aantalInschrSignalenOp0102", "aantalInschrSignalenOp0102"));
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("Diploma signalen",
					"Diploma signalen", "aantalDiplomaSignalen", "aantalDiplomaSignalen"));
				break;
			case SBH:
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("Aantal", "Aantal",
					"aantal", "aantal"));
				break;
			case SBL:
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("BSN", "BSN", "bsn",
					"bsn"));
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>(
					"Volgnummer inschrijving", "Volgnummer inschrijving", "volgnummerInschrijving",
					"volgnummerInschrijving"));
				break;
			case SIN:
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("BSN", "BSN", "bsn",
					"bsn"));
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>(
					"Volgnummer inschrijving", "Volgnummer inschrijving", "volgnummerInschrijving",
					"volgnummerInschrijving"));
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("Signaal", "Signaal",
					"sinSignaal", "sinSignaal"));
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("Inschr sign 1-10",
					"Inschr sign 1-10", "inschrijving0110Signaal", "inschrijving0110Signaal"));
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("Inschr sign 1-2",
					"Inschr sign 1-2", "inschrijving0102Signaal", "inschrijving0102Signaal"));
				addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("Diploma signaal",
					"Diploma signaal", "diplomaSignaal", "diplomaSignaal"));

				break;
			default:
				break;
		}
		addColumn(new CustomPropertyColumn<BronCfiTerugmeldingRegel>("Opmerking", "Opmerking",
			"opmerking", "opmerking"));
	}

}
