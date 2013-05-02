/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.security.authorization;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.app.security.EduArtePrincipal;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEntiteit;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdrachtRecht;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateRecht;
import nl.topicus.eduarte.entities.security.authentication.AccountRol;
import nl.topicus.eduarte.web.components.text.autocomplete.RolCategorieAutoCompleteTextField;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Combines several RechtenSet in a logical group. Rol is organisation specific and can
 * thus be customized for each organisation. Default roles have no organisation.
 * 
 * @author marrink
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"naam",
	"organisatie"})})
public class Rol extends OrganisatieEntiteit implements Comparable<Rol>
{
	private static final long serialVersionUID = -5971531262332382231L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rol")
	@BatchSize(size = 20)
	@AutoForm(include = false)
	private List<Recht> rechten = new ArrayList<Recht>();

	@Column(nullable = false, length = 100)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@Column(nullable = true)
	@AutoForm(include = false)
	private Integer zorglijn;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	@AutoForm(order = 10, readOnly = true, label = "Autorisatieniveau")
	private AuthorisatieNiveau authorisatieNiveau;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	@AutoForm(include = false)
	private RechtenSoort rechtenSoort;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rol")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@AutoForm(include = false)
	private List<AccountRol> accounts = new ArrayList<AccountRol>();

	/**
	 * Voor indeling van rollen in groepen
	 */
	@Column(length = 50)
	@AutoForm(order = -1, editorClass = RolCategorieAutoCompleteTextField.class, htmlClasses = "unit_max")
	private String categorie;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rol")
	@BatchSize(size = 20)
	@AutoForm(include = false)
	private List<DocumentTemplateRecht> documentTemplates = new ArrayList<DocumentTemplateRecht>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rol")
	@BatchSize(size = 20)
	@AutoForm(include = false)
	private List<DeelnemerZoekOpdrachtRecht> zoekOpdrachten =
		new ArrayList<DeelnemerZoekOpdrachtRecht>();

	public Rol()
	{
		super();
	}

	public Rol(String name)
	{
		setNaam(name);
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public Integer getZorglijn()
	{
		return zorglijn;
	}

	public void setZorglijn(Integer zorglijn)
	{
		this.zorglijn = zorglijn;
	}

	public List<Recht> getRechten()
	{
		return rechten;
	}

	public void setRechten(List<Recht> rechten)
	{
		this.rechten = rechten;
	}

	public AuthorisatieNiveau getAuthorisatieNiveau()
	{
		return authorisatieNiveau;
	}

	public void setAuthorisatieNiveau(AuthorisatieNiveau authorisatieNiveau)
	{
		this.authorisatieNiveau = authorisatieNiveau;
	}

	public RechtenSoort getRechtenSoort()
	{
		return rechtenSoort;
	}

	public void setRechtenSoort(RechtenSoort rechtenSoort)
	{
		this.rechtenSoort = rechtenSoort;
	}

	public String getCategorie()
	{
		return categorie;
	}

	public void setCategorie(String categorie)
	{
		this.categorie = categorie;
	}

	/**
	 * Natuurlijke ordening is alfabetisch op naam.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Rol o)
	{
		if (o == null)
			throw new NullPointerException("Kan niet vergelijken met null.");
		return getNaam().compareTo(o.getNaam());
	}

	public List<AccountRol> getAccounts()
	{
		return accounts;
	}

	public void setAccounts(List<AccountRol> accounts)
	{
		this.accounts = accounts;
	}

	public boolean containsRecht(EduArtePrincipal principal)
	{
		for (Recht curRecht : getRechten())
		{
			if (curRecht.isPrincipal(principal))
				return true;
		}
		return false;
	}

	public void addRecht(EduArtePrincipal principal)
	{
		Recht newRecht = new Recht(this, principal.getSourceClass(), principal.getActionClass());
		getRechten().add(newRecht);
	}

	public void removeRecht(EduArtePrincipal principal)
	{
		for (Recht curRecht : getRechten())
		{
			if (curRecht.isPrincipal(principal))
			{
				getRechten().remove(curRecht);
				return;
			}
		}
		throw new IllegalArgumentException(this + " does not have " + principal);
	}

	public void setDocumentTemplates(List<DocumentTemplateRecht> documentTemplates)
	{
		this.documentTemplates = documentTemplates;
	}

	public List<DocumentTemplateRecht> getDocumentTemplates()
	{
		return documentTemplates;
	}

	public void setZoekOpdrachten(List<DeelnemerZoekOpdrachtRecht> zoekOpdrachten)
	{
		this.zoekOpdrachten = zoekOpdrachten;
	}

	public List<DeelnemerZoekOpdrachtRecht> getZoekOpdrachten()
	{
		return zoekOpdrachten;
	}

	@Override
	public String toString()
	{
		return getNaam();
	}
}
