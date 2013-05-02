package nl.topicus.eduarte.entities.personen;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Embeddable;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.CumiCategorie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.CumiRatio;

/**
 * 
 * Met ingang van 1 maart 2006 is de huidige CUMI-regeling vervallen en vervangen door de
 * 'Nieuwkomerregeling'. Onder de nieuwe regeling vallen alléén personen die een andere
 * nationaliteit dan de Nederlandse hebben en minder dan twee jaar in Nederland zijn. De
 * nieuwe CUMI-categorie is 4a. </p>
 * <p>
 * De nieuwe CUMI-ratio zijn:
 * </p>
 * <ul>
 * <li>d1 - Leerling is gerekend vanaf 1 oktober van het schooljaar korter dan een jaar in
 * Nederland</li>
 * <li>d2 - Leerling is gerekend vanaf 1 oktober van het schooljaar tussen een en twee
 * jaar in Nederland.</li>
 * </ul>
 * <p>
 * Deze nieuwe CUMI-categorie en -ratio moet u gebruiken voor inschrijvingen met een datum
 * inschrijving vanaf 01-08-2006. Als u voor inschrijvingen vanaf deze datum de oude
 * CUMI-waarden gebruikt, zullen deze worden afgekeurd. De letters in de categorie en
 * ratio moeten met kleine letters worden aangemeld. Gebruik van hoofdletters leidt tot
 * afkeuringen.
 * </p>
 * <p>
 * Voor inschrijvingen met een eerdere datum gelden de oude CUMI-categorieën en -ratio.
 * Van leerlingen die al in BRON geregistreerd staan met oude CUMI-waarden (en een
 * inschrijving voor 01-08-2006) en waarvan u de CUMI-gegevens niet aanpast, worden de
 * CUMI-gegevens niet meer vermeld in de foto's met peildatum 1 oktober 2006 en verder.
 * </p>
 * 
 * @author hop
 */
@Embeddable
public class Cumi implements Serializable
{
	private static final long serialVersionUID = 1L;

	private CumiCategorie categorie;

	private CumiRatio ratio;

	public Cumi()
	{
		categorie = null;
		ratio = null;
	}

	public Cumi(CumiCategorie categorie, CumiRatio ratio)
	{
		this.categorie = categorie;
		this.ratio = ratio;
	}

	public CumiCategorie getCategorie()
	{
		return categorie;
	}

	public void setCategorie(CumiCategorie categorie)
	{
		this.categorie = categorie;
	}

	public CumiRatio getRatio()
	{
		return ratio;
	}

	public void setRatio(CumiRatio ratio)
	{
		this.ratio = ratio;
	}

	/**
	 * code "0000" ONBEKEND verwijderd uit de lijst van geen Cumi4a codes, aangezien dan
	 * de leerling uit het buitenland komt. Als de cumi berekening dan wel cumi aangeeft
	 * moet de school dit bewijzen, maar dat moesten ze toch al.
	 */
	private static List<String> geenCumi4aCodes = Arrays.asList(new String[] {"0001" // NEDERLANDSE
		});

	/**
	 * Berekent de cumi van de leerling volgens de nieuwkomer-regeling. De leerling heeft
	 * categorie 4a als geen van beide nationaliteiten nederlands is. Als beide
	 * nationaliteiten leeg zijn, wordt geen categorie 4a teruggegeven.
	 */
	public static Cumi getCumi(Persoon persoon, Date peildatum)
	{
		CumiCategorie categorie = CumiCategorie.Categorie4a;
		CumiRatio ratio = null;

		if (persoon.getNationaliteit1() != null
			&& geenCumi4aCodes.contains(persoon.getNationaliteit1().getCode()))
		{
			categorie = null;
		}

		if (persoon.getNationaliteit2() != null
			&& geenCumi4aCodes.contains(persoon.getNationaliteit2().getCode()))
		{
			categorie = null;
		}

		if (persoon.getNationaliteit1() == null && persoon.getNationaliteit2() == null)
		{
			categorie = null;
		}

		// minder dan 1 jaar in Nederland --> d1
		// tussen 1 en 2 jaar in Nederland --> d2
		if (categorie != null)
		{
			if (persoon.getDatumInNederland() == null)
			{
				categorie = null;
			}
			else
			{
				int inNederland =
					TimeUtil.getInstance().getDifferenceInYears(peildatum,
						persoon.getDatumInNederland());
				if (inNederland < 1)
					ratio = CumiRatio.d1;
				else if (inNederland < 2)
					ratio = CumiRatio.d2;
				else
					categorie = null;
			}
		}

		if (categorie != null)
			return new Cumi(categorie, ratio);
		return null;
	}

	/**
	 * Retourneert true als zowel de categorie als de ratio gelijk is
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Cumi)
		{
			Cumi cumi = (Cumi) obj;

			return cumi.getCategorie().equals(getCategorie()) && cumi.getRatio().equals(getRatio());
		}
		return false;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		if (categorie == null)
			return "-";
		if (ratio == null)
			return categorie + " / -";
		return categorie + " / " + ratio;
	}
}
