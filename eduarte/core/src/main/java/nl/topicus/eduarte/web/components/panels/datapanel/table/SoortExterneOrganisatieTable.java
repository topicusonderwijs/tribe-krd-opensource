package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.eduarte.entities.organisatie.SoortExterneOrganisatie;

public class SoortExterneOrganisatieTable extends CodeNaamActiefTable<SoortExterneOrganisatie>
{
	private static final long serialVersionUID = 1L;

	public SoortExterneOrganisatieTable(String title)
	{
		super(title);
		addColumn(new BooleanPropertyColumn<SoortExterneOrganisatie>("Tonen bij vooropleiding",
			"Tonen bij vooropleiding", "tonenBijVooropleiding", "tonenBijVooropleiding"));
	}
}
