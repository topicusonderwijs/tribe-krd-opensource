package nl.topicus.eduarte.entities.participatie;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.signalering.VerzuimTaakSignaalDefinitie;
import nl.topicus.eduarte.entities.signalering.settings.VerzuimTaakEventAbonnementConfiguration;

import org.hibernate.annotations.Index;

@Entity()
@Table(name = "VerTaSigDefEvConKoppel")
public class VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "signaalDefinitie", nullable = false)
	@Index(name = "idx_VerzuimTaakKop_def")
	private VerzuimTaakSignaalDefinitie signaalDefinitie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "abonnementConfiguration", nullable = false)
	@Index(name = "idx_VerzuimTaakKop_abbo")
	private VerzuimTaakEventAbonnementConfiguration abonnementConfiguration;

	public VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppel()
	{

	}

	public void setSignaalDefinitie(VerzuimTaakSignaalDefinitie signaalDefinitie)
	{
		this.signaalDefinitie = signaalDefinitie;
	}

	public VerzuimTaakSignaalDefinitie getSignaalDefinitie()
	{
		return signaalDefinitie;
	}

	public void setAbonnementConfiguration(
			VerzuimTaakEventAbonnementConfiguration abonnementConfiguration)
	{
		this.abonnementConfiguration = abonnementConfiguration;
	}

	public VerzuimTaakEventAbonnementConfiguration getAbonnementConfiguration()
	{
		return abonnementConfiguration;
	}

	@Override
	public String toString()
	{
		return signaalDefinitie.getSignaalNaam();
	}
}
