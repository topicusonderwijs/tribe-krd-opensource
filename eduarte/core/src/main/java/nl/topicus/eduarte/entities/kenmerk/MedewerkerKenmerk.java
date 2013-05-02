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
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.providers.KenmerkProvider;
import nl.topicus.eduarte.providers.MedewerkerProvider;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@BatchSize(size = 20)
@Exportable
@Table(appliesTo = "MedewerkerKenmerk", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"})})
public class MedewerkerKenmerk extends BeginEinddatumInstellingEntiteit implements
		MedewerkerProvider, KenmerkProvider
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "medewerker")
	@Index(name = "idx_medewkenmerk_medewerker")
	private Medewerker medewerker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "kenmerk")
	@Index(name = "idx_medewkenmerk_kenmerk")
	@AutoFormEmbedded
	private Kenmerk kenmerk;

	@Column(nullable = true)
	@Lob
	private String toelichting;

	public MedewerkerKenmerk()
	{
	}

	public MedewerkerKenmerk(Medewerker medewerker)
	{
		setMedewerker(medewerker);
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
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
