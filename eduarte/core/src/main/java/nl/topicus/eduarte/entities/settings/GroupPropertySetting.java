/*
 * $Id: ExtendableDataViewComponentSetting.java,v 1.1 2007/07/24 08:15:31 marrink Exp $
 * $Revision: 1.1 $
 * $Date: 2007/07/24 08:15:31 $
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.settings;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelId;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEntiteit;
import nl.topicus.eduarte.entities.security.authentication.Account;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author loite
 */
@Entity()
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"account", "panelId"})})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class GroupPropertySetting extends OrganisatieEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account", nullable = false)
	@Index(name = "idx_grouppropsetting_account")
	private Account account;

	@Column(nullable = false, length = 512)
	private String panelId;

	@Column(nullable = true, length = 256)
	private String property;

	public GroupPropertySetting()
	{
	}

	public GroupPropertySetting(Account account, CustomDataPanelId panelId, String groupProperty)
	{
		this.panelId = panelId.generateId();
		this.account = account;
		this.property = groupProperty;
	}

	public String getPanelId()
	{
		return panelId;
	}

	public void setPanelId(String panelId)
	{
		this.panelId = panelId;
	}

	public Serializable getAccountId()
	{
		return getAccount().getIdAsSerializable();
	}

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public String getProperty()
	{
		return property;
	}

	public void setProperty(String property)
	{
		this.property = property;
	}
}
