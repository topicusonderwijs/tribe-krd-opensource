/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs.incident;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.dbs.gedrag.Incident;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.web.components.quicksearch.deelnemer.DeelnemerSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.medewerker.MedewerkerSearchEditor;
import nl.topicus.onderwijs.incidentkoppeling.model.IrisAfhandeling;
import nl.topicus.onderwijs.incidentkoppeling.model.IrisMotief;
import nl.topicus.onderwijs.incidentkoppeling.model.IrisRolBijIncident;
import nl.topicus.onderwijs.incidentkoppeling.model.interfaces.IBetrokkene;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class IrisBetrokkene extends InstellingEntiteit implements IBetrokkene
{

	/**
	 * EduArte versie van IrisRolOpSchool
	 * 
	 * Het verschil is dat EduArte de termen 'deelnemer' en 'medewerker' gebruikt in
	 * plaats van 'leerling' en 'personeel'
	 * 
	 * @author niesink
	 */

	public enum EduArteRolOpSchool
	{
		Deelnemer("deelnemer", 1),
		Medewerker("medewerker", 2),
		Ouder("ouder/verzorger", 3),
		Anders("andere relatie", 4);

		String naam;

		int code;

		EduArteRolOpSchool(String naam, int code)
		{
			this.naam = naam;
			this.code = code;
		}

		public String getNaam()
		{
			return naam;
		}

		public int getCode()
		{
			return code;
		}

		@Override
		public String toString()
		{
			return StringUtil.firstCharUppercase(naam);
		}
	}

	private static final long serialVersionUID = 1L;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "incident", nullable = true)
	@Index(name = "idx_IrisInc_incident")
	private Incident incident;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "irisincident", nullable = false)
	@Index(name = "idx_IrisInc_irincident")
	@IgnoreInGebruik
	private IrisIncident irisIncident;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "betrokkene")
	@IgnoreInGebruik
	private List<IrisBetrokkeneAfhandeling> irisAfhandeling =
		new ArrayList<IrisBetrokkeneAfhandeling>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "betrokkene")
	@IgnoreInGebruik
	private List<IrisBetrokkeneMotief> irisMotief = new ArrayList<IrisBetrokkeneMotief>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = true)
	@Index(name = "idx_IrisInc_medewerker")
	@AutoForm(editorClass = MedewerkerSearchEditor.class)
	private Medewerker medewerker;

	@Column(nullable = false)
	private boolean letsel = false;

	@Column(nullable = false)
	private int rolOpSchoolCode;

	@Column(nullable = true)
	private int rolBijIncidentCode;

	@Column(nullable = true)
	private String toelichting;

	@Column(nullable = true)
	private String ingevoerdeNaam;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private Geslacht ingevoerdGeslacht;

	public IrisBetrokkene()
	{
		setRolOpSchool(EduArteRolOpSchool.Deelnemer);
		setRolBijIncident(IrisRolBijIncident.Anders);
	}

	public IrisBetrokkene(IrisIncident ii)
	{
		this();
		setIrisIncident(ii);
	}

	/**
	 * Wicket's ListMultipleChoice wil perse iedere keer hetzelfde object terug van de
	 * getter, vandaar dit transient object
	 */
	@Transient
	private transient List<IrisAfhandeling> afhandelingenCache = new ArrayList<IrisAfhandeling>();

	@Override
	public List<IrisAfhandeling> getAfhandeling()
	{
		List<IrisAfhandeling> afhandelingen = afhandelingenCache;
		afhandelingen.clear();
		if (getBetrokkeneAfhandelingen() != null)
		{
			for (IrisBetrokkeneAfhandeling ba : getBetrokkeneAfhandelingen())
				afhandelingen.add(ba.getAfhandeling());
			return afhandelingen;
		}
		else
		{
			return null;
		}
	}

	public List<IrisBetrokkeneAfhandeling> getIrisAfhandeling()
	{
		return getBetrokkeneAfhandelingen();
	}

	public void setIrisAfhandeling(List<IrisBetrokkeneAfhandeling> afhandelingen)
	{
		this.irisAfhandeling = afhandelingen;
	}

	@AutoForm(htmlClasses = "unit_100")
	public void setAfhandeling(List<IrisAfhandeling> afhandelingen)
	{
		if (afhandelingen != null)
		{
			List<IrisAfhandeling> nieuw = new ArrayList<IrisAfhandeling>(afhandelingen);
			List<IrisAfhandeling> huidig = getAfhandeling();
			for (IrisAfhandeling iv : nieuw)
				if (!huidig.contains(iv))
					addAfhandeling(iv);

			for (IrisAfhandeling iv : huidig)
				if (!nieuw.contains(iv))
					removeAfhandeling(iv);
		}
	}

	public void removeAfhandeling(IrisAfhandeling toDelete)
	{
		for (IrisBetrokkeneAfhandeling afh : getBetrokkeneAfhandelingen())
		{
			if (afh.getAfhandeling().equals(toDelete))
			{
				getBetrokkeneAfhandelingen().remove(afh);
				return;
			}
		}
	}

	public List<IrisBetrokkeneAfhandeling> getBetrokkeneAfhandelingen()
	{
		return irisAfhandeling;
	}

	@Override
	public Geslacht getGeslacht()
	{
		Geslacht geslacht = Geslacht.Onbekend;
		if (getPersoon() != null && getPersoon().getGeslacht() != null)
			geslacht = getPersoon().getGeslacht();
		else
			geslacht = getIngevoerdGeslacht();
		return geslacht;
	}

	public void setGeslacht(Geslacht geslacht)
	{
		this.ingevoerdGeslacht = geslacht;
	}

	public void setNaam(String naam)
	{
		this.ingevoerdeNaam = naam;
	}

	@Override
	public boolean getLetsel()
	{
		return letsel;
	}

	/**
	 * Wicket's ListMultipleChoice wil perse iedere keer hetzelfde object terug van de
	 * getter, vandaar dit transient object
	 */
	@Transient
	private transient List<IrisMotief> motiefCache = new ArrayList<IrisMotief>();

	@Override
	public List<IrisMotief> getMotief()
	{
		List<IrisMotief> motieven = motiefCache;
		motieven.clear();
		if (getBetrokkeneMotief() != null)
		{
			for (IrisBetrokkeneMotief ba : getBetrokkeneMotief())
				motieven.add(ba.getMotief());
			return motieven;
		}
		else
		{
			return null;
		}
	}

	public List<IrisBetrokkeneMotief> getIrisMotief()
	{
		return getBetrokkeneMotief();
	}

	public void setIrisMotief(List<IrisBetrokkeneMotief> motief)
	{
		this.irisMotief = motief;
	}

	@AutoForm(htmlClasses = "unit_max")
	public void setMotief(List<IrisMotief> motief)
	{
		if (motief != null)
		{
			List<IrisMotief> nieuw = new ArrayList<IrisMotief>(motief);
			List<IrisMotief> huidig = getMotief();
			for (IrisMotief iv : nieuw)
				if (!huidig.contains(iv))
					addMotief(iv);

			for (IrisMotief iv : huidig)
				if (!nieuw.contains(iv))
					removeMotief(iv);
		}
	}

	public void removeMotief(IrisMotief toDelete)
	{
		for (IrisBetrokkeneMotief afh : getBetrokkeneMotief())
		{
			if (afh.getMotief().equals(toDelete))
			{
				getBetrokkeneMotief().remove(afh);
				return;
			}
		}
	}

	public List<IrisBetrokkeneMotief> getBetrokkeneMotief()
	{
		return irisMotief;
	}

	@Override
	public String getNaam()
	{
		return getPersoon() != null ? getPersoon().getVolledigeNaam() : getIngevoerdeNaam();
	}

	public Persoon getPersoon()
	{
		if (getIncident() != null)
			return getIncident().getDeelnemer().getPersoon();
		if (getMedewerker() != null)
			return getMedewerker().getPersoon();
		return null;

	}

	@Override
	public int getRolBijIncidentCode()
	{
		return rolBijIncidentCode;
	}

	public IrisRolBijIncident getRolBijIncident()
	{
		for (IrisRolBijIncident rol : IrisRolBijIncident.values())
		{
			if (rol.getCode() == getRolBijIncidentCode())
				return rol;
		}
		return IrisRolBijIncident.Anders;
	}

	@Override
	public int getRolOpSchoolCode()
	{
		return rolOpSchoolCode;
	}

	public EduArteRolOpSchool getRolOpSchool()
	{
		for (EduArteRolOpSchool rol : EduArteRolOpSchool.values())
		{
			if (rol.getCode() == getRolOpSchoolCode())
				return rol;
		}
		return EduArteRolOpSchool.Anders;
	}

	@Override
	public String getToelichting()
	{
		return toelichting;
	}

	public void setIncident(Incident incident)
	{
		this.incident = incident;
	}

	public Incident getIncident()
	{
		return incident;
	}

	public Deelnemer getDeelnemer()
	{
		return getIncident() != null ? getIncident().getDeelnemer() : null;
	}

	@AutoForm(editorClass = DeelnemerSearchEditor.class)
	public void setDeelnemer(Deelnemer deelnemer)
	{
		if (getIncident() == null)
		{
			setIncident(new Incident());
			getIncident().setBetrokkene(this);
		}
		getIncident().setDeelnemer(deelnemer);
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	public void setRolBijIncident(IrisRolBijIncident rol)
	{
		setRolBijIncidentCode(rol.getCode());
	}

	public void setRolOpSchool(EduArteRolOpSchool rol)
	{
		setRolOpSchoolCode(rol.getCode());
	}

	public void setRolOpSchoolCode(int rolCode)
	{
		this.rolOpSchoolCode = rolCode;
	}

	public void setRolBijIncidentCode(int rolCode)
	{
		this.rolBijIncidentCode = rolCode;
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setIrisIncident(IrisIncident irisIncident)
	{
		this.irisIncident = irisIncident;
	}

	public IrisIncident getIrisIncident()
	{
		return irisIncident;
	}

	public void addAfhandeling(IrisAfhandeling ia)
	{
		IrisBetrokkeneAfhandeling iba = new IrisBetrokkeneAfhandeling();
		iba.setAfhandeling(ia);
		iba.setBetrokkene(this);
		irisAfhandeling.add(iba);
	}

	public void addMotief(IrisMotief ia)
	{
		IrisBetrokkeneMotief iba = new IrisBetrokkeneMotief();
		iba.setMotief(ia);
		iba.setBetrokkene(this);
		irisMotief.add(iba);
	}

	public void setToelichting(String toelichting)
	{
		this.toelichting = toelichting;
	}

	public void setLetsel(boolean letsel)
	{
		this.letsel = letsel;
	}

	public void setIngevoerdeNaam(String ingevoerdeNaam)
	{
		this.ingevoerdeNaam = ingevoerdeNaam;
	}

	public String getIngevoerdeNaam()
	{
		return ingevoerdeNaam;
	}

	public void setIngevoerdGeslacht(Geslacht ingevoerdGeslacht)
	{
		this.ingevoerdGeslacht = ingevoerdGeslacht;
	}

	public Geslacht getIngevoerdGeslacht()
	{
		return ingevoerdGeslacht != null ? ingevoerdGeslacht : Geslacht.Onbekend;
	}

	/**
	 * Verwijder ingevoerde naam/geslacht en/of koppelingen met een deelnemer of
	 * medewerker n.a.v. de rol op school
	 */
	public void updatePersoonsKoppeling()
	{
		if (getRolOpSchool().equals(EduArteRolOpSchool.Deelnemer))
		{
			setMedewerker(null);
			setIngevoerdeNaam(null);
			setIngevoerdGeslacht(null);
		}
		else if (getRolOpSchool().equals(EduArteRolOpSchool.Medewerker))
		{
			getIncident().setDeelnemer(null);
			setIngevoerdeNaam(null);
			setIngevoerdGeslacht(null);
		}
		else if (getRolOpSchool().equals(EduArteRolOpSchool.Anders)
			|| getRolOpSchool().equals(EduArteRolOpSchool.Ouder))
		{
			getIncident().setDeelnemer(null);
			setMedewerker(null);
		}
	}

	public boolean isDeelnemer()
	{
		return getIncident() != null;
	}

	public boolean isMedewerker()
	{
		return getMedewerker() != null;
	}

}
