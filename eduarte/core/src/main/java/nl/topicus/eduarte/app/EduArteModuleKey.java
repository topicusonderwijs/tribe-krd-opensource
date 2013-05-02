package nl.topicus.eduarte.app;

import nl.topicus.cobra.modules.IModuleKey;

public enum EduArteModuleKey implements IModuleKey
{
	BASISFUNCTIONALITEIT("Basisfunctionaliteit"),

	@Afneembaar
	KERNREGISTRATIE_DEELNEMERS("Kernregistratie Deelnemers"),

	@Afneembaar
	DIGITAALAANMELDEN("Digitaalaanmelden"),

	BPV("Beroepspraktijkvorming/Stage"),

	@Afneembaar
	BPV_MBO("Beroepspraktijkvorming"),

	@Afneembaar
	BPV_HOGERONDERWIJS("Stage hogeronderwijs"),

	@Afneembaar
	DEELNEMER_BEGELEIDING("Deelnemer begeleiding"),

	@Afneembaar
	IRIS_KOPPELING("Synchronisatie met IRIS+"),

	@Afneembaar
	ONDERWIJSCATALOGUS("Onderwijscatalogus"),

	SELFSERVICE("SelfService"),

	@Afneembaar
	SUMMATIEVE_RESULTATEN("Summatieve resultaten"),

	@Afneembaar
	FORMATIEVE_RESULTATEN("Formatieve resultaten"),

	@Afneembaar
	HOGER_ONDERWIJS("Alluris"),

	@Afneembaar
	PARTICIPATIE("Aanwezigheid"),

	NOISE("nOISe"),

	@Afneembaar
	RESULTATENINVOER("Resultateninvoer"),

	KRD_PARTICIPATIE("KRD-Participatie"),

	@Afneembaar
	GROEPS_AUTHORISATIE("Authorisatie op groepen"),

	@Afneembaar
	FINANCIEEL("Financieel"),

	@Afneembaar
	VASCO_TOKENS("Vasco tokens"),

	@Afneembaar
	COMPETENTIEMETER("Competentiemeter"),

	@Afneembaar
	SCHRAPKAARTEN("Schrapkaarten import en export"),

	@Afneembaar
	EXACT_CSV_EXPORT("Exact CSV export"),

	@Afneembaar
	LVS_CSV_EXPORT("LVS CSV export"),

	@Afneembaar
	GENERIEK_KOPPELPLATFORM("Generiek koppelplatform"),

	@Afneembaar
	WS_AMARANTIS("Webservices voor Amarantis"),

	@Afneembaar
	WS_AVENTUS("Webservices voor Aventus"),

	@Afneembaar
	WS_ALBEDA("Webservices voor Albeda"),

	@Afneembaar
	WS_VERSEON("Synchronisatie met Verseon"),

	@Afneembaar
	WS_REDSPIDER_CLIENT("Synchronisatie met RedSpider"),

	@Afneembaar
	WS_LEIDEN("Webservices voor ROC Leiden"),

	@Afneembaar
	ONDERWIJSCATALOGUS_AMARANTIS("Uitbreiding Onderwijscatalogus Amarantis");

	private String name;

	private EduArteModuleKey(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public static EduArteModuleKey getModuleKey(String name)
	{
		for (EduArteModuleKey curKey : values())
		{
			if (curKey.getName().equals(name))
			{
				return curKey;
			}
		}
		throw new IllegalArgumentException("No module with name '" + name + "'");
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public boolean isAfneembaar()
	{
		try
		{
			return EduArteModuleKey.class.getField(name()).isAnnotationPresent(Afneembaar.class);
		}
		catch (NoSuchFieldException e)
		{
			throw new RuntimeException(e);
		}
	}
}
