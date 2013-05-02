package nl.topicus.cobra.templates.mocks;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.templates.annotations.Exportable;

@Exportable
public class AutoFabrikant
{
	private String naam;

	private List<AutoModel> modellen;

	private AutoFabrikant(String naam, List<AutoModel> modellen)
	{
		this.naam = naam;
		this.modellen = modellen;
	}

	@Exportable
	public String getNaam()
	{
		return naam;
	}

	@Exportable
	public List<AutoModel> getModellen()
	{
		return modellen;
	}

	public static List<AutoFabrikant> createManufacturersList()
	{
		List<AutoFabrikant> manufacturers = new ArrayList<AutoFabrikant>();

		manufacturers.add(createManufacturerAudi());
		manufacturers.add(createManufacturerMaserati());
		manufacturers.add(createManufacturerUnicode());

		return manufacturers;
	}

	public static AutoFabrikant createManufacturerAudi()
	{
		List<AutoModel> modellen = new ArrayList<AutoModel>();
		modellen.add(new AutoModel("A8", "zwart", Versnellingsbak.AUTOMAAT));
		modellen.add(new AutoModel("A6", "blauw", Versnellingsbak.HANDMATIG));
		modellen.add(new AutoModel("A4", "wit", Versnellingsbak.HANDMATIG));
		modellen.add(new AutoModel("A3", "rood", Versnellingsbak.HANDMATIG));

		return new AutoFabrikant("Audi", modellen);
	}

	public static AutoFabrikant createManufacturerMaserati()
	{
		List<AutoModel> modellen = new ArrayList<AutoModel>();
		modellen.add(new AutoModel("Granturismo", "zwart", Versnellingsbak.HANDMATIG));
		modellen.add(new AutoModel("Quattroporte", "grijs", Versnellingsbak.AUTOMAAT));

		return new AutoFabrikant("Maserati", modellen);
	}

	public static AutoFabrikant createManufacturerUnicode()
	{
		List<AutoModel> modellen = new ArrayList<AutoModel>();
		modellen.add(new AutoModel("ãçéèëêęěĕēėüűů", "rood", Versnellingsbak.AUTOMAAT));
		modellen.add(new AutoModel("кириллица", "wit", Versnellingsbak.HANDMATIG));
		modellen.add(new AutoModel("Εβραις", "blauw", Versnellingsbak.AUTOMAAT));

		return new AutoFabrikant("Unicode", modellen);
	}
}
