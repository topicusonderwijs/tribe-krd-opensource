package nl.topicus.eduarte.entities.settings;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Configuratie om te bepalen of een organisatie al dan niet gebruik maakt van Vasco
 * tokens en deze door EduArte/KRD/Tribe/ laat beheren.
 */
@Embeddable
public class VascoTokensConfiguration implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private boolean actief;

	public VascoTokensConfiguration()
	{
	}

	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}
}
