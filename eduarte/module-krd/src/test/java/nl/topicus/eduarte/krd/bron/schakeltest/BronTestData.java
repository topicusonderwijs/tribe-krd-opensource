package nl.topicus.eduarte.krd.bron.schakeltest;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;

public class BronTestData
{
	private final BronBuilder builder;

	private final Deelnemer deelnemer;

	private final Verbintenis verbintenis;

	public BronTestData(BronBuilder builder, Deelnemer deelnemer, Verbintenis verbintenis)
	{
		this.builder = builder;
		this.deelnemer = deelnemer;
		this.verbintenis = verbintenis;
	}

	public BronBuilder getBuilder()
	{
		return builder;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public Verbintenis getVerbintenis()
	{
		return verbintenis;
	}
}
