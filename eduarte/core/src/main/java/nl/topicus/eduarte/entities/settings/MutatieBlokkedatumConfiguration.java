package nl.topicus.eduarte.entities.settings;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.cobra.web.components.form.AutoForm;

@Embeddable
public class MutatieBlokkedatumConfiguration implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	@AutoForm(description = "")
	private Date blokkadedatumVerbintenis;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	@AutoForm(label = "Blokkadedatum BPV", description = "")
	private Date blokkadedatumBPV;

	public MutatieBlokkedatumConfiguration()
	{
	}

	public Date getBlokkadedatumVerbintenis()
	{
		return blokkadedatumVerbintenis;
	}

	public Date getBlokkadedatumBPV()
	{
		return blokkadedatumBPV;
	}

	public void setBlokkadedatumVerbintenis(Date blokkadedatumVerbintenis)
	{
		this.blokkadedatumVerbintenis = blokkadedatumVerbintenis;
	}

	public void setBlokkadedatumBPV(Date blokkadedatumBPV)
	{
		this.blokkadedatumBPV = blokkadedatumBPV;
	}

}
