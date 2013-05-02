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
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.providers.KenmerkProvider;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * 
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@BatchSize(size = 20)
@Exportable
@Table(appliesTo = "DeelnemerKenmerk", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
public class DeelnemerKenmerk extends BeginEinddatumInstellingEntiteit implements
		DeelnemerProvider, KenmerkProvider
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "deelnemer")
	@Index(name = "idx_deelkenmerk_deelnemer")
	private Deelnemer deelnemer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "kenmerk")
	@Index(name = "idx_deelkenmerk_kenmerk")
	@AutoFormEmbedded
	private Kenmerk kenmerk;

	@Column(nullable = true)
	@Lob
	private String toelichting;

	public DeelnemerKenmerk()
	{
	}

	public DeelnemerKenmerk(Deelnemer deelnemer)
	{
		setDeelnemer(deelnemer);
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
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
