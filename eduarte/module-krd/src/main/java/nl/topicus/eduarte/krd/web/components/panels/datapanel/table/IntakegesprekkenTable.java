package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DateTimeColumn;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.web.components.panels.datapanel.table.AbstractVrijVeldableTable;

/**
 * Tabel met alle intakegesprekken van verschillende intakes
 * 
 * @author hop
 */
public class IntakegesprekkenTable extends AbstractVrijVeldableTable<Intakegesprek>
{
	private static final long serialVersionUID = 1L;

	public IntakegesprekkenTable()
	{
		super("Intakegesprekken");
		createColumns();
		createVrijVeldKolommen(VrijVeldCategorie.INTAKE, "");
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Intakegesprek>("Intake", "Intake",
			"verbintenis.organisatieEenheid"));
		addColumn(new DateTimeColumn<Intakegesprek>("Datum", "Datum/tijd", "datumTijd"));
		addColumn(new CustomPropertyColumn<Intakegesprek>("Intaker", "Intaker",
			"intaker.persoon.volledigeNaam"));
		addColumn(new CustomPropertyColumn<Intakegesprek>("Locatie", "Locatie", "locatie.naam"));
		addColumn(new CustomPropertyColumn<Intakegesprek>("Kamer", "Kamer", "kamer"));
		addColumn(new CustomPropertyColumn<Intakegesprek>("Status", "Status", "status"));
	}
}
