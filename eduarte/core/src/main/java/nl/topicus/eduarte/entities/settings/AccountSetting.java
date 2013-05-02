/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.settings;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.security.authentication.Account;

import org.hibernate.annotations.Index;

/**
 * Setting die per account wordt bijgehouden.
 * 
 * @author loite
 * @param <T>
 */
@Entity()
public abstract class AccountSetting<T> extends OrganisatieSetting<T>
{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account", nullable = true)
	@Index(name = "idx_OrganisatieSetting_account")
	private Account account;

	public AccountSetting()
	{
	}

	public AccountSetting(Account account)
	{
		setAccount(account);
	}

	/**
	 * @return Returns the account.
	 */
	public Account getAccount()
	{
		return account;
	}

	/**
	 * @param account
	 *            The account to set.
	 */
	public void setAccount(Account account)
	{
		this.account = account;
	}

}
