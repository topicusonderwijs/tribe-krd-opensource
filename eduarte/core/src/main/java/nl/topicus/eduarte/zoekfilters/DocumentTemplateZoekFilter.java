/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.entities.rapportage.OnderwijsDocumentTemplate.ExamenDocumentTemplateType;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.web.components.choice.TaxonomieCombobox;

import org.apache.wicket.model.IModel;

/**
 * NOTE: Als je hier een property toevoegd/veranderd graag ook aanpassen in
 * {@link DocumentTemplate#matchesFilter(DocumentTemplateZoekFilter)}
 * 
 * @author hop
 */
public class DocumentTemplateZoekFilter extends AbstractZoekFilter<DocumentTemplate>
{
	private static final long serialVersionUID = 1L;

	private Class< ? extends DocumentTemplate> templateClass;

	private String omschrijving;

	private String bestandsnaam;

	@AutoForm(editorClass = JaNeeCombobox.class)
	private Boolean valide;

	@AutoForm(editorClass = JaNeeCombobox.class)
	private Boolean actief = Boolean.TRUE;

	private DocumentTemplateContext context;

	private DocumentTemplateCategorie categorie;

	private List<DocumentTemplateContext> contexten;

	private List<DocumentTemplateCategorie> categorieen;

	@AutoForm(editorClass = TaxonomieCombobox.class)
	private IModel<Taxonomie> taxonomie;

	private ExamenDocumentTemplateType examenDocumentType;

	private IModel<Account> account;

	public DocumentTemplateZoekFilter()
	{
		this.templateClass = DocumentTemplate.class;
	}

	public DocumentTemplateZoekFilter(Account account,
			Class< ? extends DocumentTemplate> templateClass)
	{
		setAccount(account);
		this.templateClass = templateClass;
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

	public Boolean getValide()
	{
		return valide;
	}

	public void setValide(Boolean valide)
	{
		this.valide = valide;
	}

	public DocumentTemplateContext getContext()
	{
		return context;
	}

	public void setContext(DocumentTemplateContext context)
	{
		this.context = context;
	}

	public DocumentTemplateCategorie getCategorie()
	{
		return categorie;
	}

	public void setCategorie(DocumentTemplateCategorie categorie)
	{
		this.categorie = categorie;
	}

	public List<DocumentTemplateContext> getContexten()
	{
		if (contexten == null)
			contexten = new ArrayList<DocumentTemplateContext>();

		return contexten;
	}

	public void setContexten(DocumentTemplateContext... contexten)
	{
		this.contexten = Arrays.asList(contexten);
	}

	public List<DocumentTemplateCategorie> getCategorieen()
	{
		if (categorieen == null)
			categorieen = new ArrayList<DocumentTemplateCategorie>();

		return categorieen;
	}

	public void setCategorieen(DocumentTemplateCategorie... categorieen)
	{
		this.categorieen = Arrays.asList(categorieen);
	}

	public Taxonomie getTaxonomie()
	{
		return getModelObject(taxonomie);
	}

	public void setTaxonomie(Taxonomie taxonomie)
	{
		if (this.taxonomie == null)
			this.taxonomie = makeModelFor(taxonomie);
		else
			this.taxonomie.setObject(taxonomie);
	}

	public void setTaxonomieModel(IModel<Taxonomie> taxonomieModel)
	{
		this.taxonomie = taxonomieModel;
	}

	public Class< ? extends DocumentTemplate> getTemplateClass()
	{
		return templateClass;
	}

	public void setTemplateClass(Class< ? extends DocumentTemplate> templateClass)
	{
		this.templateClass = templateClass;
	}

	public ExamenDocumentTemplateType getExamenDocumentType()
	{
		return examenDocumentType;
	}

	public void setExamenDocumentType(ExamenDocumentTemplateType examenDocumentType)
	{
		this.examenDocumentType = examenDocumentType;
	}

	public void setAccount(Account account)
	{
		this.account = makeModelFor(account);
	}

	public Account getAccount()
	{
		return getModelObject(account);
	}

	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}

	public Boolean getActief()
	{
		return actief;
	}

}
