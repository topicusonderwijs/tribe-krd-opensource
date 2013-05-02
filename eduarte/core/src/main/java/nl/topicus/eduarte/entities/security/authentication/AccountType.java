/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.security.authentication;

/**
 * @author marrink
 */
public enum AccountType
{
	/**
	 * Alle type accounts.
	 */
	All("Alle types")
	{
		/**
		 * @see nl.topicus.eduarte.entities.security.authentication.AccountType#getType()
		 */
		@Override
		public Class< ? extends Account> getType()
		{
			return Account.class;
		}
	},
	/**
	 * Account type voor medewerkers.
	 */
	Medewerker("Medewerkers")
	{
		/**
		 * @see nl.topicus.eduarte.entities.security.authentication.AccountType#getType()
		 */
		@Override
		public Class< ? extends Account> getType()
		{
			return MedewerkerAccount.class;
		}
	},
	/**
	 * Account type voor deelnemers.
	 */
	Deelnemer("Deelnemers")
	{
		/**
		 * @see nl.topicus.eduarte.entities.security.authentication.AccountType#getType()
		 */
		@Override
		public Class< ? extends Account> getType()
		{
			return DeelnemerAccount.class;
		}
	};

	private final String label;

	private AccountType(String label)
	{
		this.label = label;

	}

	/**
	 * De class van de specifieke subclass van dit type {@link Account}.
	 * 
	 * @return subclass
	 */
	public abstract Class< ? extends Account> getType();

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		return label;
	}
}
