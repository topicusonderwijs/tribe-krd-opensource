package nl.topicus.eduarte.entities.dbs.trajecten.templates;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.dbs.trajecten.Traject;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.security.authorization.Rol;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * @author maatman
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class GekoppeldeTemplate extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	public static enum KoppelingsRol
	{
		UITVOERENDE,
		VERANTWOORDELIJKE
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private KoppelingsRol koppelingsRol;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UitvoerendeType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = true)
	@ForeignKey(name = "FK_UitvoerTempl_medewerker")
	@Index(name = "idx_UitvoerTempl_medewerker")
	private Medewerker medewerker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rol", nullable = true)
	@ForeignKey(name = "FK_UitvoerTempl_rol")
	@Index(name = "idx_UitvoerTempl_rol")
	private Rol rol;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trajectTemplate", nullable = false)
	@ForeignKey(name = "FK_UitvoerTempl_templ")
	@Index(name = "idx_UitvoerTempl_templ")
	@IgnoreInGebruik
	private TrajectTemplate trajectTemplate;

	public GekoppeldeTemplate()
	{
	}

	public GekoppeldeTemplate(KoppelingsRol koppelingsRol, TrajectTemplate trajectTemplate)
	{
		setKoppelingsRol(koppelingsRol);
		setTrajectTemplate(trajectTemplate);
	}

	public KoppelingsRol getKoppelingsRol()
	{
		return koppelingsRol;
	}

	public void setKoppelingsRol(KoppelingsRol koppelingsRol)
	{
		this.koppelingsRol = koppelingsRol;
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	public Rol getRol()
	{
		return rol;
	}

	public void setRol(Rol rol)
	{
		this.rol = rol;
	}

	public TrajectTemplate getTrajectTemplate()
	{
		return trajectTemplate;
	}

	public void setTrajectTemplate(TrajectTemplate trajectTemplate)
	{
		this.trajectTemplate = trajectTemplate;
	}

	public UitvoerendeType getType()
	{
		return type;
	}

	public void setType(UitvoerendeType type)
	{
		this.type = type;
	}

	public List<Medewerker> getGekoppeldeMedewerkers(Traject traject)
	{
		switch (getType())
		{
			case Medewerker:
				return Arrays.asList(getMedewerker());
			case Mentor:
				return traject.getDeelnemer().getBegeleidersOpDatum(traject.getBegindatum());
			case Rol:
				return Collections.emptyList();
		}
		throw new IllegalStateException();
	}
}
