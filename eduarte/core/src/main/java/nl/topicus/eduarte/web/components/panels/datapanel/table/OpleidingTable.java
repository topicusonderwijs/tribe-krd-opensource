package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;

/**
 * Tabel met de mogelijke kolommen voor opleidingzoekschermen.
 * 
 * @author loite
 */
public class OpleidingTable extends AbstractVrijVeldableTable<Opleiding>
{
	private static final long serialVersionUID = 1L;

	public OpleidingTable()
	{
		super("Opleidingen");
		createColumns();
		createVrijVeldKolommen(VrijVeldCategorie.OPLEIDING, "");
		createGroupProperties();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Opleiding>("Code", "Code", "code", "code"));
		if (RechtenSoort.DIGITAALAANMELDER == EduArteContext.get().getAccount().getRechtenSoort())
			addColumn(new CustomPropertyColumn<Opleiding>("Naam", "Naam", "wervingsnaam",
				"wervingsnaam"));
		else
			addColumn(new CustomPropertyColumn<Opleiding>("Naam", "Naam", "naam", "naam"));
		addColumn(new CustomPropertyColumn<Opleiding>("Leerweg", "Leerweg", "leerweg", "leerweg"));
		addColumn(new CustomPropertyColumn<Opleiding>("Default intensiteit", "Default intensiteit",
			"defaultIntensiteit", "defaultIntensiteit").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Opleiding>("Verbintenisgebied", "Verbintenisgebied",
			"verbintenisgebied.naam", "verbintenisgebied.naam"));
		addColumn(new CustomPropertyColumn<Opleiding>("Taxonomiecode", "Taxonomiecode",
			"verbintenisgebied.sorteercode", "verbintenisgebied.taxonomiecode"));
		addColumn(new CustomPropertyColumn<Opleiding>("Externe code", "Externe code",
			"verbintenisgebied.externeCode", "verbintenisgebied.externeCode"));
		addColumn(new CustomPropertyColumn<Opleiding>("Niveau (MBO)", "Niveau",
			"verbintenisgebied.niveauNaam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Opleiding>("Prijsfactor", "Prijsfactor",
			"verbintenisgebied.prijsfactor").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Opleiding>("Begindatum", "Begindatum", "begindatum",
			"begindatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Opleiding>("Einddatum", "Einddatum", "einddatum",
			"einddatum").setDefaultVisible(false));
	}

	private void createGroupProperties()
	{
		addGroupProperty(new GroupProperty<Opleiding>("verbintenisgebied.naam",
			"Verbintenisgebied", "verbintenisgebied.naam"));
	}
}
