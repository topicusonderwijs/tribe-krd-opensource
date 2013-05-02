/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.DatePropertyColumn;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;

/**
 * @author loite
 */
public class GroepTable extends AbstractVrijVeldableTable<Groep>
{
	private static final long serialVersionUID = 1L;

	public GroepTable()
	{
		super("Groepen");
		createColumns();
		createVrijVeldKolommen(VrijVeldCategorie.GROEP, "");
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<Groep>("Code", "Code", "code", "code"));
		addColumn(new CustomPropertyColumn<Groep>("Naam", "Naam", "naam", "naam"));
		addColumn(new CustomPropertyColumn<Groep>("Groeptype", "Groeptype", "groepstype",
			"groepstype"));
		addColumn(new BooleanPropertyColumn<Groep>("Plaatsingsgroep", "Plaatsingsgroep",
			"groepstype.plaatsingsgroep").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Groep>("Organisatie-eenheid", "Organisatie-eenheid",
			"organisatieEenheid", "organisatieEenheid"));
		addColumn(new DatePropertyColumn<Groep>("Begindatum", "Begindatum", "begindatum",
			"begindatum"));
		addColumn(new DatePropertyColumn<Groep>("Einddatum", "Einddatum", "einddatum", "einddatum"));
		addColumn(new CustomPropertyColumn<Groep>("Docenten", "Docenten",
			"groepDocentenAfkortingenOpPeildatum"));
		addColumn(new CustomPropertyColumn<Groep>("Mentoren", "Mentoren",
			"groepMentorenAfkortingenOpPeildatum"));
		addColumn(new CustomPropertyColumn<Groep>("Locatie", "Locatie", "locatie", "locatie")
			.setDefaultVisible(false));
	}
}
