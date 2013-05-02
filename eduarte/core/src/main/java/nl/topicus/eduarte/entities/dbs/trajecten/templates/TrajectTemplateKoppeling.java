package nl.topicus.eduarte.entities.dbs.trajecten.templates;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity()
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public abstract class TrajectTemplateKoppeling extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trajTemplAutoKopp", nullable = false)
	@ForeignKey(name = "FK_TrajTemplKopp_TTAutoKopp")
	@Index(name = "idx_TrajTemplKopp_TTAutoKopp")
	private TrajectTemplateAutomatischeKoppeling trajectTemplateAutomatischeKoppeling;

	public TrajectTemplateAutomatischeKoppeling getTrajectTemplateAutomatischeKoppeling()
	{
		return trajectTemplateAutomatischeKoppeling;
	}

	public void setTrajectTemplateAutomatischeKoppeling(
			TrajectTemplateAutomatischeKoppeling trajectTemplateAutomatischeKoppeling)
	{
		this.trajectTemplateAutomatischeKoppeling = trajectTemplateAutomatischeKoppeling;
	}
}
