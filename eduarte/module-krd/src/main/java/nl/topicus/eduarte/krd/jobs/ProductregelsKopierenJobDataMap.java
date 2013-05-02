package nl.topicus.eduarte.krd.jobs;

import java.util.List;
import java.util.Set;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.web.pages.shared.KopieerSettings;

public class ProductregelsKopierenJobDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public ProductregelsKopierenJobDataMap(KopieerSettings kopieerSettings,
			Set<Opleiding> opleidingen, List<Productregel> productregels)
	{
		setKopieerSettings(kopieerSettings);
		setOpleidingen(opleidingen);
		setProductregels(productregels);
	}

	public KopieerSettings getKopieerSettings()
	{
		return (KopieerSettings) get("kopieerSettings");
	}

	public void setKopieerSettings(KopieerSettings kopieerSettings)
	{
		put("kopieerSettings", kopieerSettings);
	}

	@SuppressWarnings("unchecked")
	public Set<Opleiding> getOpleidingen()
	{
		return (Set<Opleiding>) get("opleidingen");
	}

	public void setOpleidingen(Set<Opleiding> opleidingen)
	{
		put("opleidingen", opleidingen);
	}

	@SuppressWarnings("unchecked")
	public List<Productregel> getProductregels()
	{
		return (List<Productregel>) get("productregels");
	}

	public void setProductregels(List<Productregel> productregels)
	{
		put("productregels", productregels);
	}
}
