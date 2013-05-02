package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import nl.topicus.eduarte.entities.taxonomie.MBONiveau;

/**
 * @author vandenbrink
 */
@Entity
public class TaalvaardigheidseisLlb extends Taalvaardigheidseis
{

	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private MBONiveau mboNiveau;

	public MBONiveau getMboNiveau()
	{
		return mboNiveau;
	}

	public void setMboNiveau(MBONiveau mboNiveau)
	{
		this.mboNiveau = mboNiveau;
	}
}
