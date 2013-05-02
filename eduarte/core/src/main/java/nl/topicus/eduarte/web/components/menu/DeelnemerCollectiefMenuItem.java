/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AccessMenuItemKey;
import nl.topicus.eduarte.app.EduArteApp;

/**
 * @author loite
 */
public enum DeelnemerCollectiefMenuItem implements AccessMenuItemKey
{

	DeelnemerZoeken("Zoeken", 'N'),
	DeelnemerUitgebreidZoeken("Uitgebreid zoeken", 'U'),
	OpgeslagenZoekopdrachten('O'),
	MijnDeelnemers("Mijn deelnemers", 'M')
	{
		@Override
		public String getLabel()
		{
			return "Mijn " + EduArteApp.get().getDeelnemerTermMeervoud();
		}
	},
	OnderwijsproductenToekennen("Toekennen", 'T'),
	KeuzesAangeven('K'),
	KeuzesControleren('D'),
	ActieOverzicht("Actie-overzicht", 'A'),
	Criteriumbankcontrole('C'),
	Rapportages('R'),
	ExactCsvRapportage('E'),
	LvsCsvRapportage('L'),
	Invoeren('I'),
	Bevriezen('Z'),
	Verbintenissen("Status verbintenis aanpassen", 'V'),
	BPVs("Status BPV's aanpassen", 'B'),
	OrgEhdLocatieWijzigen("Organisatie-eenheden aanpassen", 'O'),
	BronMutatiesAanmaken("Bronmutaties aanmaken", 'U'),
	Aanmaken("Verbintenissen aanmaken", 'F'),
	SeResultatenInlezen('S'),
	ResultatenImporteren('M');

	private final String label;

	private Character key;

	private DeelnemerCollectiefMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
	}

	private DeelnemerCollectiefMenuItem(String label)
	{
		this.label = label;
	}

	private DeelnemerCollectiefMenuItem(Character key)
	{
		this.label = StringUtil.convertCamelCase(name());
		this.key = key;
	}

	private DeelnemerCollectiefMenuItem(String label, Character key)
	{
		this.label = label;
		this.key = key;
	}

	@Override
	public String getLabel()
	{
		return label;
	}

	@Override
	public Character getAccessKey()
	{
		return key;
	}
}
