/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.rapportage;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelId;
import nl.topicus.cobra.web.components.datapanel.DefaultCustomDataPanelId;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.util.XmlSerializer;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class DeelnemerZoekOpdracht extends InstellingEntiteit implements IContextInfoObject
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 200)
	private String omschrijving;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "account")
	@Index(name = "idx_DZO_account")
	private Account account;

	@Lob
	private String filter;

	private boolean kolommenVastzetten = true;

	private boolean peildatumVastzetten = true;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "zoekOpdracht")
	@BatchSize(size = 20)
	private List<DeelnemerZoekOpdrachtRecht> rechten = new ArrayList<DeelnemerZoekOpdrachtRecht>();

	public DeelnemerZoekOpdracht()
	{
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	@Override
	public String getContextInfoOmschrijving()
	{
		return getOmschrijving();
	}

	public List<DeelnemerZoekOpdrachtRecht> getRechten()
	{
		return rechten;
	}

	public void setRechten(List<DeelnemerZoekOpdrachtRecht> rechten)
	{
		this.rechten = rechten;
	}

	public String getFilter()
	{
		return filter;
	}

	public void setFilter(String filter)
	{
		this.filter = filter;
	}

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public boolean isPersoonlijk()
	{
		return account != null;
	}

	/**
	 * Let op, dit gebruikt EduArteContext.get().getAccount()!
	 */
	public void setPersoonlijk(boolean persoonlijk)
	{
		account = persoonlijk ? EduArteContext.get().getAccount() : null;
	}

	public boolean isKolommenVastzetten()
	{
		return kolommenVastzetten;
	}

	public void setKolommenVastzetten(boolean kolommenVastzetten)
	{
		this.kolommenVastzetten = kolommenVastzetten;
	}

	public boolean isPeildatumVastzetten()
	{
		return peildatumVastzetten;
	}

	public void setPeildatumVastzetten(boolean peildatumVastzetten)
	{
		this.peildatumVastzetten = peildatumVastzetten;
	}

	public CustomDataPanelId createDataPanelId()
	{
		return new DefaultCustomDataPanelId(DeelnemerZoekOpdracht.class, getId().toString(),
			isPersoonlijk());
	}

	public VerbintenisZoekFilter deserializeFilter()
	{
		VerbintenisZoekFilter ret =
			(VerbintenisZoekFilter) XmlSerializer.deserializeValues(new String[] {getFilter()},
				new Class< ? >[] {VerbintenisZoekFilter.class})[0];
		if (!isPeildatumVastzetten())
		{
			ret.setCustomPeildatumModel(EduArteContext.get().getPeildatumModel());
			ret.setPeilEindDatum(ret.getPeildatum());
		}
		return ret;
	}
}