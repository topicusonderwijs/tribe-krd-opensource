package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

@Entity
@Table(appliesTo = "RapportageTemplateIJkpunt")
public class RapportageTemplateIJkpunt extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "ijkpunt", nullable = false)
	@Index(name = "idx_rapportageTIJ_ijkpunt")
	private IJkpunt ijkpunt;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "config", nullable = true)
	@Index(name = "idx_rapportageTIJ_config")
	private VoortgangHtmlConfig config;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "configPdf", nullable = true)
	@Index(name = "idx_rapportageTIJ_configP")
	private VoortgangPdfConfig configPdf;

	public VoortgangPdfConfig getConfigPdf()
	{
		return configPdf;
	}

	public void setConfigPdf(VoortgangPdfConfig configPdf)
	{
		this.configPdf = configPdf;
	}

	protected RapportageTemplateIJkpunt()
	{
	}

	public RapportageTemplateIJkpunt(VoortgangPdfConfig configPdf, IJkpunt ijkpunt)
	{
		this.configPdf = configPdf;
		this.ijkpunt = ijkpunt;
	}

	public RapportageTemplateIJkpunt(VoortgangHtmlConfig config, IJkpunt ijkpunt)
	{
		this.config = config;
		this.ijkpunt = ijkpunt;
	}

	public VoortgangHtmlConfig getConfig()
	{
		return config;
	}

	public void setConfig(VoortgangHtmlConfig config)
	{
		this.config = config;
	}

	public IJkpunt getIjkpunt()
	{
		return ijkpunt;
	}

	public void setIjkpunt(IJkpunt ijkpunt)
	{
		this.ijkpunt = ijkpunt;
	}

}
