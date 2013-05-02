package nl.topicus.eduarte.entities.vrijevelden;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(name = "VrijVeldOptieKeuze")
public class VrijVeldOptieKeuze extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "optie", nullable = false)
	@Index(name = "idx_VVKeuze_optie")
	@IgnoreInGebruik
	private VrijVeldKeuzeOptie optie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entiteit", nullable = false)
	@Index(name = "idx_VVKkeuze_entiteit")
	@IgnoreInGebruik
	private VrijVeldEntiteit entiteit;

	public VrijVeldOptieKeuze()
	{
	}

	public VrijVeldKeuzeOptie getOptie()
	{
		return optie;
	}

	public void setOptie(VrijVeldKeuzeOptie optie)
	{
		this.optie = optie;
	}

	public VrijVeldEntiteit getEntiteit()
	{
		return entiteit;
	}

	public void setEntiteit(VrijVeldEntiteit entiteit)
	{
		this.entiteit = entiteit;
	}
}
