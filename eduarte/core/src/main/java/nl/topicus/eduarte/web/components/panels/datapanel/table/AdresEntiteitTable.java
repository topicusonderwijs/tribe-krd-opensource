package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.adres.AdresEntiteit;

public class AdresEntiteitTable<T extends AdresEntiteit<T>> extends
		CustomDataPanelContentDescription<T>
{
	private static final long serialVersionUID = 1L;

	public AdresEntiteitTable()
	{
		super("Adressen");
		addColumn(new CustomPropertyColumn<T>("Volledig adres", "Adres",
			"adres.volledigAdresOp1Regel"));
		addColumn(new CustomPropertyColumn<T>("Vanaf", "Vanaf", "begindatum", "begindatum"));
		addColumn(new CustomPropertyColumn<T>("T/m", "T/m", "einddatum", "einddatum")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Type", "Type", "type", "type"));
		addColumn(new CustomPropertyColumn<T>("Straat", "Straat", "adres.straat", "adres.straat")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Huisnummer", "Huisnummer", "adres.huisnummer",
			"adres.huisnummer").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Huisnummertoevoeging", "Toevoeging",
			"adres.huisnummerToevoeging", "adres.huisnummerToevoeging").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Postcode", "Postcode", "adres.postcode",
			"adres.postcode").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Plaats", "Plaats", "adres.plaats", "adres.plaats")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Land", "Land", "adres.land.naam", "adres.land")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Gemeente", "Gemeente", "adres.gemeente.naam",
			"adres.gemeente").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Provincie", "Provincie", "adres.provincie.naam",
			"adres.provincie").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<T>("Locatie", "Locatie", "adres.locatie",
			"adres.locatie").setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<T>("Geheim", "Geheim", "adres.geheim", "adres.geheim")
			.setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<T>("Postadres", "Postadres", "postadres", "postadres")
			.setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<T>("Fysiek adres", "Fysiek adres", "fysiekadres",
			"fysiekadres").setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<T>("Factuur adres", "Factuur adres", "factuuradres",
			"factuuradres").setDefaultVisible(false));
	}
}
