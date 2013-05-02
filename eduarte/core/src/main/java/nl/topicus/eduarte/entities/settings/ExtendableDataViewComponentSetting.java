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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.entities.dataview.IExtendableDataViewComponentSetting;
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
@Table(name = "edvcs", uniqueConstraints = {@UniqueConstraint(columnNames = {"account", "panelId",
	"headerId"})})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ExtendableDataViewComponentSetting extends OrganisatieEntiteit implements
		IExtendableDataViewComponentSetting
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account", nullable = false)
	@Index(name = "index_edvcs_account")
	private Account account;

	@Column(nullable = false, length = 512)
	private String panelId;

	@Column(nullable = false, length = 256)
	private String headerId;

	@Column(nullable = false)
	private int position;

	@Column(nullable = false)
	private boolean visible;

	public ExtendableDataViewComponentSetting()
	{
	}

	public ExtendableDataViewComponentSetting(Account account, CustomDataPanelId panelId,
			String headerId, int position, boolean visible)
	{
		this.account = account;
		this.panelId = panelId.generateId();
		this.headerId = headerId;
		this.position = position;
		this.visible = visible;
	}

	public String getPanelId()
	{
		return panelId;
	}

	public void setPanelId(String panelId)
	{
		this.panelId = panelId;
	}

	public String getHeaderId()
	{
		return headerId;
	}

	public void setHeaderId(String headerId)
	{
		this.headerId = headerId;
	}

	public int getPosition()
	{
		return position;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}

	public boolean isVisible()
	{
		return visible;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	@Override
	public int compareTo(IExtendableDataViewComponentSetting o)
	{
		return this.getPosition() - o.getPosition();
	}
}
