package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.columns.GeslaagdVoorOpleidingColumn;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OpleidingTable;

import org.apache.wicket.model.IModel;

public class BereikbareDiplomasTable extends OpleidingTable
{
	private static final long serialVersionUID = 1L;

	public BereikbareDiplomasTable(IModel<Deelnemer> deelnemerModel, IModel<Cohort> cohortModel)
	{
		addColumn(new GeslaagdVoorOpleidingColumn("Geslaagd", deelnemerModel, cohortModel));
	}

}
