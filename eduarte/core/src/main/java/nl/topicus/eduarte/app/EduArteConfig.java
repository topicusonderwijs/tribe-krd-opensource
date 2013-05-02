package nl.topicus.eduarte.app;

import org.slf4j.LoggerFactory;

public enum EduArteConfig
{
	Ontwikkel,
	Test,
	Acceptatie,
	Productie;

	public boolean isOntwikkel()
	{
		return this == Ontwikkel;
	}

	public boolean isTest()
	{
		return this == Test;
	}

	public boolean isAcceptatie()
	{
		return this == Acceptatie;
	}

	public boolean isProductie()
	{
		return this == Productie;
	}

	public static EduArteConfig parse(String configString)
	{
		for (EduArteConfig config : values())
		{
			if (config.name().equalsIgnoreCase(configString))
				return config;
		}
		LoggerFactory.getLogger(EduArteConfig.class).error(
			"Onbekende configuratie voor eduarte.config {}", configString);
		throw new IllegalArgumentException(
			"Onbekende configuratie string voor eduarte.config gevonden: " + configString);
	}
}
