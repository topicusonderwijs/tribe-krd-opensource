/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AccessMenuItemKey;

/**
 * @author papegaaij
 */
public enum BeheerMenuItem implements AccessMenuItemKey
{
	Beheer('B'),
	LopendeTaken('T'),
	Organisatieboom('O'),
	Caches('C'),
	FotosUploaden("Foto's uploaden", 'F'),
	Databaseconnecties('C'),
	Functies('F'),
	RedenUitDienst('U'),
	Schooladvies('A'),
	SoortVooropleiding('V'),
	RedenUitschrijving('R'),
	Relatiesoort('S'),
	Gebruikers('G'),
	RollenEnRechten('R'),
	SoortContactgegevens('G'),
	Organisatie_eenheden("Organisatie-eenheden", 'E'),
	SoortOrganisatie_Eenheden("Soort organisatie-eenheden", 'S'),
	Groepstype('T'),
	SoortExterneOrganisaties('E'),
	Locaties('L'),
	AanwezigheidInstellingen("Aanwezigheid instellingen", 'A'),
	AlgemeneInstellingen('I'),
	Signalen('S'),
	Schalen('C'),
	Structuurcategorieën('U'),
	SoortProductregels('P'),
	SoortOnderwijsproducten('O'),
	Leerstijlen('L'),
	SoortPraktijklokalen('P'),
	TypeToetsen('T'),
	Gebruiksmiddelen('G'),
	Verbruiksmiddelen('V'),
	Aggregatieniveaus('A'),
	Teams('T'),
	TypeLocaties('L'),
	Samenvoegdocumenten('S'),
	OverzichtSamenvoegvelden('O'),
	VrijeVelden('V'),
	ExterneOrganisatieContactPersoonRol("Contactpersoon rollen", 'C'),
	SoortContracten('S'),
	TypeFinancieringen("Soort financieringen", 'F'),
	Kenmerkcategorien("Kenmerkcategorieën", 'C'),
	Kenmerken('K'),
	DocumentCategorien("Documentcategorieën", 'D'),
	DocumentTypes("Documenttypes", 'Y'),
	UitkomstIntakegesprek('G'),
	Grootboekrekeningen('B'),
	Kostendragers('D'),
	Kostenplaatsen('P'),
	FinancieelInstellingen('F'),
	AcceptEmailCertificaat('A'),
	Modules('M'),
	TokensImporteren("Vasco tokens importeren"),
	TokensOrganisatieOverzicht("Vasco tokens "),
	AfspraakTypes("Afspraaktypes", 'T'),
	MutatieLogVerwerkers("Mutatielog verwerkers"),
	MutatieLogInstellingen("Mutatielog instellingen"),
	MutatieLogVerwerkersTester("Mutatielog verwerkers tester"),
	SchrapkaartenExporteren("Schrapkaarten exporteren"),
	SchrapkaartenImporteren("Schrapkaarten importeren"),
	OnderwijsproductNiveau("Niveauaanduiding"),
	Fases("Fases"),
	ModuleAfnames("Afgenomen modules");

	private final String label;

	private Character key;

	private BeheerMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
	}

	private BeheerMenuItem(String label)
	{
		this.label = label;
	}

	private BeheerMenuItem(Character key)
	{
		this.label = StringUtil.convertCamelCase(name());
		this.key = key;
	}

	private BeheerMenuItem(String label, Character key)
	{
		this.label = label;
		this.key = key;
	}

	/**
	 * @see nl.topicus.cobra.web.components.menu.MenuItemKey#getLabel()
	 */
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
