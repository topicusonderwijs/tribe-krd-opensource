/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.rapportage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.*;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.DocumentType;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.jobs.rapportage.RapportageBijlageJob;
import nl.topicus.eduarte.jobs.rapportage.RapportageJob;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratiePanel;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratieRegistratie;
import nl.topicus.eduarte.zoekfilters.DocumentTemplateZoekFilter;

import org.apache.wicket.security.checks.ISecurityCheck;
import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Document template. Bevat de inhoud van een RTF-bestand als LOB.
 * 
 * @author hop
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class DocumentTemplate extends InstellingEntiteit implements IContextInfoObject,
		IActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 200)
	private String omschrijving;

	@Column(length = 200, nullable = false)
	private String bestandsnaam;

	@Column(nullable = false)
	@AutoForm(label = "Valide")
	private boolean valid;

	@Column(nullable = false)
	private boolean actief = true;

	@Column(nullable = false)
	@AutoForm(label = "Kopie bij context bewaren")
	private boolean kopieBijContext;

	@Column(nullable = false)
	private boolean sectiePerElement = true;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@Index(name = "idx_DTCo_DT")
	@AutoForm(label = "Het onderwerp waar het over gaat")
	private DocumentTemplateContext context;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@Index(name = "idx_DTCa_DT")
	@AutoForm(label = "Plaats in de applicatie")
	private DocumentTemplateCategorie categorie;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@Index(name = "idx_DT_type")
	@AutoForm(label = "Type")
	private DocumentTemplateType type;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@Index(name = "idx_DT_forceertype")
	@AutoForm(label = "Forceer type", description = "Selecteer hier een andere waarde wanneer het "
		+ "niet gewenst is om het gegenereerde document in het orginele formaat te downloaden.")
	private DocumentTemplateType forceerType;

	@Column(nullable = false)
	private boolean beperkAutorisatie = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "documentType")
	@AutoForm(label = "Documenttype", description = "Het documenttype dat aan de gegenereerde documenten gekoppeld moet worden")
	@Index(name = "idx_DT_documentType")
	private DocumentType documentType;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "documentTemplate")
	@BatchSize(size = 20)
	private List<DocumentTemplateRecht> rechten = new ArrayList<DocumentTemplateRecht>();

	/**
	 * Dit heet zzzbestand vanwege ORA-24816. Deze variabele moet zover mogelijk achterin
	 * de insert komen.
	 */
	@Lob()
	private byte[] zzzBestand;

	public DocumentTemplate()
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

	public String getBestandsnaam()
	{
		return bestandsnaam;
	}

	public void setBestandsnaam(String bestandsnaam)
	{
		this.bestandsnaam = bestandsnaam;
	}

	public String getBestandsnaamZonderExtensie()
	{
		if (getBestandsnaam() == null)
			return null;
		int index = getBestandsnaam().indexOf('.');
		if (index > -1)
		{
			return getBestandsnaam().substring(0, index);
		}
		return getBestandsnaam();
	}

	public boolean isValid()
	{
		return valid;
	}

	public void setValid(boolean valid)
	{
		this.valid = valid;
	}

	@Override
	public String getContextInfoOmschrijving()
	{
		return getOmschrijving();
	}

	public DocumentTemplateContext getContext()
	{
		return context;
	}

	public void setContext(DocumentTemplateContext context)
	{
		this.context = context;
	}

	public DocumentTemplateType getType()
	{
		return type;
	}

	public void setType(DocumentTemplateType type)
	{
		this.type = type;
	}

	public DocumentTemplateCategorie getCategorie()
	{
		return categorie;
	}

	public void setCategorie(DocumentTemplateCategorie categorie)
	{
		this.categorie = categorie;
	}

	public String getUitvoerFormaat()
	{
		if (getForceerType() != null)
			return getForceerType().getOmschrijving();
		if (getType() != null)
			return getType().getOmschrijving();

		return "";
	}

	public void setKopieBijContext(boolean kopieBijContext)
	{
		this.kopieBijContext = kopieBijContext;
	}

	public boolean isKopieBijContext()
	{
		return kopieBijContext;
	}

	public void setSectiePerElement(boolean sectiePerElement)
	{
		this.sectiePerElement = sectiePerElement;
	}

	public boolean isSectiePerElement()
	{
		return sectiePerElement;
	}

	public byte[] getZzzBestand()
	{
		return zzzBestand;
	}

	public Map<String, Object> getExtraParameters()
	{
		return null;
	}

	public void setZzzBestand(byte[] bestand)
	{
		this.zzzBestand = bestand;
	}

	public DocumentTemplateType getForceerType()
	{
		return forceerType;
	}

	public void setForceerType(DocumentTemplateType forceerType)
	{
		this.forceerType = forceerType;
	}

	public List<DocumentTemplateRecht> getRechten()
	{
		return rechten;
	}

	public void setRechten(List<DocumentTemplateRecht> rechten)
	{
		this.rechten = rechten;
	}

	public void setBeperkAutorisatie(boolean beperkAutorisatie)
	{
		this.beperkAutorisatie = beperkAutorisatie;
	}

	public boolean isBeperkAutorisatie()
	{
		return beperkAutorisatie;
	}

	public DocumentType getDocumentType()
	{
		return documentType;
	}

	public void setDocumentType(DocumentType documentType)
	{
		this.documentType = documentType;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	public boolean isActief()
	{
		return actief;
	}

	@Override
	public String toString()
	{
		return omschrijving;
	}

	public Class< ? extends RapportageJob> getJobClass()
	{
		return isKopieBijContext() ? RapportageBijlageJob.class : RapportageJob.class;
	}

	public ISecurityCheck getCustomSecurityCheck()
	{
		return null;
	}

	public Class< ? extends RapportageConfiguratiePanel< ? extends IBijlageKoppelEntiteit< ? extends BijlageEntiteit>>> getConfiguratiePanel()
	{
		return getCategorie() == null ? null : getCategorie().getConfiguratiePanel();
	}

	public RapportageConfiguratieRegistratie getConfiguratieRegistratie()
	{
		return getConfiguratiePanel() == null ? null : getConfiguratiePanel().getAnnotation(
			RapportageConfiguratieRegistratie.class);
	}

	public boolean matchesFilter(DocumentTemplateZoekFilter filter)
	{
		if (!filter.getTemplateClass().isAssignableFrom(Hibernate.getClass(this)))
			return false;

		if (filter.getValide() != null && !filter.getValide().equals(isValid()))
			return false;

		if (filter.getActief() != null && !filter.getActief().equals(isActief()))
			return false;

		if (filter.getContext() != null && !filter.getContext().equals(getContext()))
			return false;

		if (!filter.getContexten().isEmpty() && !filter.getContexten().contains(getContext()))
			return false;

		if (filter.getCategorie() != null && !filter.getCategorie().equals(getCategorie()))
			return false;

		if (!filter.getCategorieen().isEmpty() && !filter.getCategorieen().contains(getCategorie()))
			return false;

		if (filter.getBestandsnaam() != null
			&& !getBestandsnaam().startsWith(filter.getBestandsnaam()))
			return false;

		if (filter.getOmschrijving() != null
			&& !getOmschrijving().contains(filter.getOmschrijving()))
			return false;

		if (isBeperkAutorisatie() && filter.getAccount() != null)
		{
			Set<Rol> accountRollen = filter.getAccount().getRollenAsRol();
			for (DocumentTemplateRecht curRecht : getRechten())
			{
				if (accountRollen.contains(curRecht.getRol()))
					return true;
			}
		}

		return true;
	}
}
