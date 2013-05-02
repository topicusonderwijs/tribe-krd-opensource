/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.rapportage;

import javax.persistence.*;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.zoekfilters.DocumentTemplateZoekFilter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Document template voor onderwijs items.
 * 
 * @author hoeve
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class OnderwijsDocumentTemplate extends DocumentTemplate
{
	private static final long serialVersionUID = 1L;

	public static enum ExamenDocumentTemplateType
	{
		Onderwijsovereenkomst
		{

			@Override
			public DocumentTemplateCategorie getTemplateCategorie()
			{
				return DocumentTemplateCategorie.Onderwijsovereenkomst;
			}

			@Override
			public DocumentTemplateContext getTemplateContext()
			{
				return DocumentTemplateContext.Verbintenis;
			}

		},
		BPVovereenkomst
		{
			@Override
			public String toString()
			{
				return "BPV-overeenkomst";
			}

			@Override
			public DocumentTemplateCategorie getTemplateCategorie()
			{
				return DocumentTemplateCategorie.BPVOvereenkomst;
			}

			@Override
			public DocumentTemplateContext getTemplateContext()
			{
				return DocumentTemplateContext.BPVVerbintenis;
			}
		},
		Diploma
		{
			@Override
			public DocumentTemplateCategorie getTemplateCategorie()
			{
				return DocumentTemplateCategorie.Examens;
			}

			@Override
			public DocumentTemplateContext getTemplateContext()
			{
				return DocumentTemplateContext.Examendeelname;
			}
		},
		Cijferlijst
		{
			@Override
			public DocumentTemplateCategorie getTemplateCategorie()
			{
				return DocumentTemplateCategorie.Examens;
			}

			@Override
			public DocumentTemplateContext getTemplateContext()
			{
				return DocumentTemplateContext.Examendeelname;
			}
		},
		Certificaat
		{
			@Override
			public DocumentTemplateCategorie getTemplateCategorie()
			{
				return DocumentTemplateCategorie.Examens;
			}

			@Override
			public DocumentTemplateContext getTemplateContext()
			{
				return DocumentTemplateContext.Examendeelname;
			}
		};

		public abstract DocumentTemplateCategorie getTemplateCategorie();

		public abstract DocumentTemplateContext getTemplateContext();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "taxonomie", nullable = true)
	@Basic(optional = false)
	@Index(name = "idx_ODT_Taxonomie")
	@AutoForm(required = true)
	private Taxonomie taxonomie;

	@Column(nullable = true)
	@Enumerated(value = EnumType.STRING)
	@AutoForm(label = "Het type onderwijsdocument")
	private ExamenDocumentTemplateType examenDocumentType;

	public OnderwijsDocumentTemplate()
	{
	}

	public Taxonomie getTaxonomie()
	{
		return taxonomie;
	}

	public void setTaxonomie(Taxonomie taxonomie)
	{
		this.taxonomie = taxonomie;
	}

	public ExamenDocumentTemplateType getExamenDocumentType()
	{
		return examenDocumentType;
	}

	public void setExamenDocumentType(ExamenDocumentTemplateType examenDocumentType)
	{
		this.examenDocumentType = examenDocumentType;
	}

	@Override
	public boolean matchesFilter(DocumentTemplateZoekFilter filter)
	{
		if (!super.matchesFilter(filter))
			return false;

		if (filter.getTaxonomie() != null && !filter.getTaxonomie().equals(getTaxonomie()))
			return false;

		if (filter.getExamenDocumentType() != null
			&& !filter.getExamenDocumentType().equals(getExamenDocumentType()))
			return false;

		return true;
	}
}
