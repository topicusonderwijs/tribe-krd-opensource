package nl.topicus.eduarte.entities;

import nl.topicus.cobra.util.StringUtil;

public enum BronEntiteitStatus
{
	/**
	 * Entiteit is nooit aan BRON aangeleverd geweest.
	 */
	Geen,
	/**
	 * Een melding klaar staat om in een batch te worden opgenomen. Wanneer de melding
	 * wordt afgekeurd, staat de entiteit niet in BRON.
	 */
	Wachtrij,
	/**
	 * Een melding klaar staat om in een batch te worden opgenomen.Wanneer de melding
	 * wordt afgekeurd, staat de entiteit echter nog wel in BRON.
	 */
	WachtrijWelInBron,
	/**
	 * Entiteit is voor de eerste keer onderweg naar BRON. Wanneer de melding wordt
	 * afgekeurd, staat de entiteit niet in BRON.
	 */
	InBehandeling,
	/**
	 * Aanpassing op de entiteit is onderweg naar BRON. Wanneer de melding wordt
	 * afgekeurd, staat de entiteit echter nog wel in BRON.
	 */
	InBehandelingWelInBron,
	/**
	 * Entiteit staat correct in BRON.
	 */
	Goedgekeurd,
	/**
	 * Entiteit is afgekeurd en staat niet in BRON.
	 */
	Afgekeurd,
	/**
	 * Laatste aanpassing op de entiteit is afgekeurd, maar entiteit staat wel in BRON.
	 */
	AfgekeurdWelInBron;

	@Override
	public String toString()
	{
		return StringUtil.convertCamelCase(name());
	}

	/**
	 * De entiteit is ooit goedgekeurd in BRON en zou dus bekend moeten zijn.
	 */
	public boolean isBekendInBron()
	{
		return equals(Goedgekeurd) || equals(AfgekeurdWelInBron) || equals(InBehandelingWelInBron)
			|| equals(WachtrijWelInBron);
	}

	/**
	 * De entiteit is ooit gemeld aan BRON en is ooit goedgekeurd of is nog in
	 * behandeling. Volgende meldingen zijn Aanpassingen, geen Toevoegingen.
	 */
	public boolean isGemeldAanBron()
	{
		return !equals(Geen) && !equals(Afgekeurd) && !equals(Wachtrij);
	}

	/**
	 * De vervolgstatus bij een goedkeurende of afkeurende terugkoppeling. Als voor de
	 * entiteit geen melding onderweg was, wordt de status niet gewijzigd.
	 */
	public BronEntiteitStatus getVervolgStatus(boolean goedgekeurd)
	{
		if (goedgekeurd)
			return Goedgekeurd;
		if (!isBekendInBron())
			return Afgekeurd;
		if (isBekendInBron())
			return AfgekeurdWelInBron;
		return this;
	}

	public BronEntiteitStatus getInBehandelingStatus()
	{
		if (isBekendInBron())
			return InBehandelingWelInBron;
		return InBehandeling;
	}

	public boolean isInBehandeling()
	{
		return equals(InBehandeling) || equals(InBehandelingWelInBron);
	}

	public boolean isWachtrij()
	{
		return equals(Wachtrij) || equals(WachtrijWelInBron);
	}

	public BronEntiteitStatus getNietInBehandelingStatus()
	{
		if (!isBekendInBron())
			return Geen;
		return Goedgekeurd;
	}

	public BronEntiteitStatus getWachtrijStatus()
	{
		if (isBekendInBron())
			return WachtrijWelInBron;
		return Wachtrij;
	}
}