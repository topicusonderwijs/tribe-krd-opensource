package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;

public class VooropleidingTable extends AbstractVrijVeldableTable<Vooropleiding>
{

	private static final long serialVersionUID = 1L;

	public VooropleidingTable()
	{
		super("Vooropleidingen");
		createColumns();
		createVrijVeldKolommen(VrijVeldCategorie.VOOROPLEIDING, "");
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Vooropleiding>("Onderwijsinstelling",
			"Onderwijsinstelling", "organisatieOmschrijving"));
		addColumn(new CustomPropertyColumn<Vooropleiding>("Soort vooropleiding",
			"Soort vooropleiding", "soortVooropleiding.naam"));
		addColumn(new CustomPropertyColumn<Vooropleiding>("Soort vooropleiding HO",
			"Soort vooropleiding", "soortVooropleidingHO.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Vooropleiding>("Begindatum", "Begindatum", "begindatum"));
		addColumn(new CustomPropertyColumn<Vooropleiding>("Einddatum", "Einddatum", "einddatum"));
		addColumn(new CustomPropertyColumn<Vooropleiding>("AantalJarenOnderwijs",
			"Aantal jaren onderwijs", "aantalJarenOnderwijs"));
		addColumn(new BooleanPropertyColumn<Vooropleiding>("Diploma", "Diploma", "diplomaBehaald",
			"diplomaBehaald"));
	}
}