/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.rapportage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.web.components.choice.ActionClassCombobox;

import org.apache.wicket.security.actions.WaspAction;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class DocumentTemplateRecht extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "rol")
	@Index(name = "idx_DTRecht_rol")
	private Rol rol;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "documentTemplate")
	@Index(name = "idx_DTRecht_template")
	private DocumentTemplate documentTemplate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "documentCategorie")
	@Index(name = "idx_DTRecht_categorie")
	private DocumentCategorie documentCategorie;

	@Column(nullable = false, length = 200)
	private String actionClassName;

	public DocumentTemplateRecht()
	{
	}

	public DocumentTemplateRecht(DocumentTemplate template)
	{
		setDocumentTemplate(template);
	}

	public DocumentTemplateRecht(DocumentCategorie cat)
	{
		setDocumentCategorie(cat);
	}

	public Rol getRol()
	{
		return rol;
	}

	public void setRol(Rol rol)
	{
		this.rol = rol;
	}

	public DocumentTemplate getDocumentTemplate()
	{
		return documentTemplate;
	}

	public void setDocumentTemplate(DocumentTemplate documentTemplate)
	{
		this.documentTemplate = documentTemplate;
	}

	public String getActionClassName()
	{
		return actionClassName;
	}

	public void setActionClassName(String actionClassName)
	{
		this.actionClassName = actionClassName;
	}

	@AutoForm(label = "Niveau", editorClass = ActionClassCombobox.class, required = true)
	public Class< ? extends WaspAction> getActionClass()
	{
		if (getActionClassName() == null)
			return null;
		try
		{
			return Class.forName(getActionClassName()).asSubclass(WaspAction.class);
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void setActionClass(Class< ? extends WaspAction> actionClass)
	{
		setActionClassName(actionClass.getName());
	}

	public String getActionName()
	{
		return getActionClass().getAnnotation(Description.class).value();
	}

	public void setDocumentCategorie(DocumentCategorie documentCategorie)
	{
		this.documentCategorie = documentCategorie;
	}

	public DocumentCategorie getDocumentCategorie()
	{
		return documentCategorie;
	}
}
