package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AccessMenuItemKey;

/**
 * @author loite
 */
public enum OnderwijsCollectiefMenuItem implements AccessMenuItemKey
{
	OnderwijsproductenZoeken("Zoeken", 'N'),
	OnderwijsproductenUitgebreidZoeken("Uitgebreid zoeken", 'U'),
	OpleidingenZoeken("Zoeken", 'O'),
	ProductregelsKopieren("Productregels kopiëren", 'P'),
	CriteriaKopieren("Criteria kopiëren", 'K'),
	ResultaatstructurenKopieren("Resultaatstructuren kopiëren", 'R'),
	Normeringen('M'),
	OnderwijsproductenImporteren("Ond. producten import", 'I'),
	ToegestaneOnderwijsproductenImporteren("Toeg. ond. producten import", 'W'),
	TaxonomieZoeken("Taxonomie", 'T'),
	OpleidingRapportages("Opleiding rapportages", 'R'),
	Samenvoegdocumenten('S'),
	Voorbeelden('V'),
	Toetsfilters('F'),
	Herberekeningen('H'),
	Herstellen("Opleidingen herstellen", 'G'),
	ResultaatstructurenExporteren('E'),
	ResultaatstructurenImporteren('I'),
	InrichtingExporteren('E'),
	InrichtingImporteren('I'),
	Curriculum('C');

	private final String label;

	private Character key;

	private OnderwijsCollectiefMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
	}

	private OnderwijsCollectiefMenuItem(String label)
	{
		this.label = label;
	}

	private OnderwijsCollectiefMenuItem(Character key)
	{
		this.label = StringUtil.convertCamelCase(name());
		this.key = key;
	}

	private OnderwijsCollectiefMenuItem(String label, Character key)
	{
		this.label = label;
		this.key = key;
	}

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
