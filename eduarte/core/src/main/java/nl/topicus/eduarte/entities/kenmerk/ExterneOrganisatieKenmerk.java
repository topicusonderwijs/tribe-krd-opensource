package nl.topicus.eduarte.entities.kenmerk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.providers.ExterneOrganisatieProvider;
import nl.topicus.eduarte.providers.KenmerkProvider;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@BatchSize(size = 20)
@Exportable
@Table(appliesTo = "ExterneOrganisatieKenmerk", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
public class ExterneOrganisatieKenmerk extends BeginEinddatumInstellingEntiteit implements
		ExterneOrganisatieProvider, KenmerkProvider
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "externeOrganisatie")
	@Index(name = "idx_extorgkenmerk_extorg")
	private ExterneOrganisatie externeOrganisatie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "kenmerk")
	@Index(name = "idx_extorgkenmerk_kenmerk")
	@AutoFormEmbedded
	private Kenmerk kenmerk;

	@Column(nullable = true)
	@Lob
	private String toelichting;

	public ExterneOrganisatieKenmerk()
	{
	}

	public ExterneOrganisatieKenmerk(ExterneOrganisatie externeOrganisatie)
	{
		setExterneOrganisatie(externeOrganisatie);
	}

	public ExterneOrganisatie getExterneOrganisatie()
	{
		return externeOrganisatie;
	}

	public void setExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		this.externeOrganisatie = externeOrganisatie;
	}

	public Kenmerk getKenmerk()
	{
		return kenmerk;
	}

	public void setKenmerk(Kenmerk kenmerk)
	{
		this.kenmerk = kenmerk;
	}

	public String getToelichting()
	{
		return toelichting;
	}

	public void setToelichting(String toelichting)
	{
		this.toelichting = toelichting;
	}

}
