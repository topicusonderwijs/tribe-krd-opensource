package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.CompetentieMatrixDataAccessHelper;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumLandelijkOfInstellingEntiteit;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

@Entity
@Table(appliesTo = "RapportageTemplate")
public class RapportageTemplate extends BeginEinddatumLandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	public static enum OutputForm
	{
		HTML
		{
			@Override
			public String toString()
			{
				return "Interactieve HTML weergave";
			}
		},
		PDF
		{
			@Override
			public String toString()
			{
				return "PDF rapport";
			}
		}
	}

	public static enum Purpose
	{
		HUIDIGE_STAND
		{
			@Override
			public String toString()
			{
				return "Overzicht huidige stand";
			}
		},
		SAMENVOEGEN
		{
			@Override
			public String toString()
			{
				return "Beoordelingen samenvoegen";
			}
		},
		DETAILS
		{
			@Override
			public String toString()
			{
				return "Matrix details inzien";
			}
		}
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OutputForm outputForm;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Purpose purpose;

	@Column(nullable = false)
	private boolean includeUitstroom;

	@Column(nullable = false)
	private boolean includeLLB;

	@Column(nullable = false)
	private boolean includeVrijeMatrices;

	@Column(nullable = false)
	private boolean includeTaal;

	@Column(nullable = false)
	private String naam;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "voortgangHtmlConfig", nullable = false)
	@Index(name = "idx_rapportageT_voortgangH")
	private VoortgangHtmlConfig voortgangHtmlConfig;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "samenvoegenHtml", nullable = false)
	@Index(name = "idx_rapportageT_samenvoegenH")
	private SamenvoegenHtmlConfig samenvoegenHtmlConfig;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "samenvoegenPdfConfig", nullable = false)
	@Index(name = "idx_rapportageT_samenvoegenP")
	private SamenvoegenPdfConfig samenvoegenPdfConfig;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "voortgangPdfConfig", nullable = false)
	@Index(name = "idx_rapportageT_voortgangP")
	private VoortgangPdfConfig voortgangPdfConfig;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = false)
	@Index(name = "idx_rapportageT_medewerker")
	private Medewerker medewerker;

	protected RapportageTemplate()
	{
	}

	public RapportageTemplate(EntiteitContext context)
	{
		super(context);
	}

	public OutputForm getOutputForm()
	{
		return outputForm;
	}

	public void setOutputForm(OutputForm outputForm)
	{
		this.outputForm = outputForm;
	}

	public Purpose getPurpose()
	{
		return purpose;
	}

	public void setPurpose(Purpose purpose)
	{
		this.purpose = purpose;
	}

	public boolean getIncludeUitstroom()
	{
		return includeUitstroom;
	}

	public void setIncludeUitstroom(boolean includeUitstroom)
	{
		this.includeUitstroom = includeUitstroom;
	}

	public boolean getIncludeLLB()
	{
		return includeLLB;
	}

	public void setIncludeLLB(boolean includeLLB)
	{
		this.includeLLB = includeLLB;
	}

	public boolean getIncludeVrijeMatrices()
	{
		return includeVrijeMatrices;
	}

	public void setIncludeVrijeMatrices(boolean includeVrijeMatrices)
	{
		this.includeVrijeMatrices = includeVrijeMatrices;
	}

	public boolean getIncludeTaal()
	{
		return includeTaal;
	}

	public void setIncludeTaal(boolean includeTaal)
	{
		this.includeTaal = includeTaal;
	}

	public List<CompetentieMatrix> findMatrices(List<Verbintenis> verbintenissen)
	{
		CompetentieMatrixDataAccessHelper cmDah =
			DataAccessRegistry.getHelper(CompetentieMatrixDataAccessHelper.class);
		List<CompetentieMatrix> ret = new ArrayList<CompetentieMatrix>();

		for (Verbintenis verbintenis : verbintenissen)
		{
			if (getIncludeUitstroom())
			{
				if (verbintenis.getOpleiding() != null)
				{

					if (Hibernate.getClass(verbintenis.getOpleiding().getVerbintenisgebied())
						.equals(Uitstroom.class))
						ret.add((Uitstroom) verbintenis.getOpleiding().getVerbintenisgebied()
							.doUnproxy());
					else if (Hibernate.getClass(verbintenis.getOpleiding().getVerbintenisgebied())
						.equals(Kwalificatiedossier.class))
						ret.add((Kwalificatiedossier) verbintenis.getOpleiding()
							.getVerbintenisgebied().doUnproxy());
				}
			}
			if (getIncludeLLB())
			{
				if (verbintenis.getOpleiding() != null)
				{
					ret.add(cmDah.getLLB(verbintenis.getBegindatum(), verbintenis.getOpleiding()
						.getNiveau()));
				}
			}
			if (getIncludeVrijeMatrices())
			{
				ret.addAll(cmDah.getVrijeMatrices(verbintenis.getDeelnemer()));
			}
		}

		return ret;
	}

	public List<CompetentieMatrix> findMatrices(Verbintenis verbintenis)
	{
		CompetentieMatrixDataAccessHelper cmDah =
			DataAccessRegistry.getHelper(CompetentieMatrixDataAccessHelper.class);
		List<CompetentieMatrix> ret = new ArrayList<CompetentieMatrix>();

		if (getIncludeUitstroom())
		{
			if (Hibernate.getClass(verbintenis.getOpleiding().getVerbintenisgebied()).equals(
				Uitstroom.class))
			{
				Uitstroom uitstroom =
					(Uitstroom) verbintenis.getOpleiding().getVerbintenisgebied().doUnproxy();
				if (uitstroom != null)
					ret.add(uitstroom);
			}
			else if (Hibernate.getClass(verbintenis.getOpleiding().getVerbintenisgebied()).equals(
				Kwalificatiedossier.class))
			{
				Kwalificatiedossier dossier =
					(Kwalificatiedossier) verbintenis.getOpleiding().getVerbintenisgebied()
						.doUnproxy();
				if (dossier != null)
					ret.add(dossier);
			}
		}
		if (getIncludeLLB())
		{
			LLBMatrix llb =
				cmDah.getLLB(verbintenis.getBegindatum(), verbintenis.getOpleiding().getNiveau());
			if (llb != null)
				ret.add(llb);
		}
		if (getIncludeVrijeMatrices())
		{
			ret.addAll(cmDah.getVrijeMatrices(verbintenis.getDeelnemer()));
		}
		return ret;
	}

	public VoortgangHtmlConfig getVoortgangHtmlConfig()
	{
		return voortgangHtmlConfig;
	}

	public void setVoortgangHtmlConfig(VoortgangHtmlConfig voortgangHtmlConfig)
	{
		this.voortgangHtmlConfig = voortgangHtmlConfig;
	}

	public SamenvoegenHtmlConfig getSamenvoegenHtmlConfig()
	{
		return samenvoegenHtmlConfig;
	}

	public void setSamenvoegenHtmlConfig(SamenvoegenHtmlConfig samenvoegenHtmlConfig)
	{
		this.samenvoegenHtmlConfig = samenvoegenHtmlConfig;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	@Override
	public String toString()
	{
		return naam;
	}

	public SamenvoegenPdfConfig getSamenvoegenPdfConfig()
	{
		return samenvoegenPdfConfig;
	}

	public void setSamenvoegenPdfConfig(SamenvoegenPdfConfig samenvoegenPdfConfig)
	{
		this.samenvoegenPdfConfig = samenvoegenPdfConfig;
	}

	public VoortgangPdfConfig getVoortgangPdfConfig()
	{
		return voortgangPdfConfig;
	}

	public void setVoortgangPdfConfig(VoortgangPdfConfig voortgangPdfConfig)
	{
		this.voortgangPdfConfig = voortgangPdfConfig;
	}
}
