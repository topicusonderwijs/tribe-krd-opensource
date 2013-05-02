package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;

/**
 * Tabel met de mogelijke kolommen voor opleidingzoekschermen.
 * 
 * @author loite
 */
public class MeeteenheidKoppelTable extends AbstractVrijVeldableTable<Opleiding>
{
	private static final long serialVersionUID = 1L;

	public MeeteenheidKoppelTable()
	{
		super("Meeteenheid koppelingen");
		createColumns();
		createVrijVeldKolommen(VrijVeldCategorie.OPLEIDING, "");
		createGroupProperties();
	}

	private void createColumns()
	{
		// addColumn(new CustomPropertyColumn("Meeteenheidnaam", "Meeteenheidnaam",
		// "meeteenheid.naam", "meeteenheid.naam"));
		// addColumn(new CustomPropertyColumn("Type", "Type", "type", "type"));

		addColumn(new CustomPropertyColumn<Opleiding>("Leerweg", "Leerweg", " leerweg", "leerweg"));
		// addColumn(new CustomPropertyColumn("Cohort", "Cohort", "cohort", "cohort"));
		addColumn(new CustomPropertyColumn<Opleiding>("Verbintenisgebied", "Verbintenisgebied",
			"verbintenisgebied.naam"));
		addColumn(new CustomPropertyColumn<Opleiding>("naam", "naam", "naam"));

		addColumn(new CustomPropertyColumn<Opleiding>("Taxonomiecode", "Taxonomiecode",
			"verbintenisgebied.taxonomiecode"));
		addColumn(new CustomPropertyColumn<Opleiding>("Externe code", "Externe code",
			"verbintenisgebied.externeCode").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<Opleiding>("Code", "Code", "code"));

		addColumn(new CustomPropertyColumn<Opleiding>("Begindatum", "Begindatum", "begindatum",
			"begindatum").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Opleiding>("Einddatum", "Einddatum", "einddatum",
			"einddatum").setDefaultVisible(false));
	}

	private void createGroupProperties()
	{
		addGroupProperty(new GroupProperty<Opleiding>("verbintenisgebied.naam",
			"Verbintenisgebied", " verbintenisgebied.naam"));
	}
}
