package nl.topicus.eduarte.entities.examen;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Groepering van Examenstatus-objecten en toegestane statusovergangen die voor een lijst
 * van taxonomien de examenworkflow beschrijven. Landelijk zijn er twee examenworkflows
 * gedefinieerd: Voor VO/VAVO en voor MBO/CGO.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class ExamenWorkflow extends LandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 30)
	private String naam;

	@Column(nullable = false)
	private boolean heeftTijdvakken;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "examenWorkflow")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
	private List<Examenstatus> examenstatussen;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "examenWorkflow")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
	private List<ToegestaneExamenstatusOvergang> toegestaneExamenstatusOvergangen;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "examenWorkflow")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
	private List<ExamenWorkflowTaxonomie> examenWorflowTaxonomieen;

	/**
	 * Default constructor voor Hibernate.
	 */
	public ExamenWorkflow()
	{
	}

	public ExamenWorkflow(EntiteitContext context)
	{
		super(context);
	}

	/**
	 * @return Returns the naam.
	 */
	public String getNaam()
	{
		return naam;
	}

	/**
	 * @param naam
	 *            The naam to set.
	 */
	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	/**
	 * @return Returns the examenstatussen.
	 */
	public List<Examenstatus> getExamenstatussen()
	{
		if (examenstatussen == null)
			examenstatussen = new ArrayList<Examenstatus>();
		return examenstatussen;
	}

	/**
	 * @param examenstatussen
	 *            The examenstatussen to set.
	 */
	public void setExamenstatussen(List<Examenstatus> examenstatussen)
	{
		this.examenstatussen = examenstatussen;
	}

	public boolean isHeeftTijdvakken()
	{
		return heeftTijdvakken;
	}

	public void setHeeftTijdvakken(boolean heeftTijdvakken)
	{
		this.heeftTijdvakken = heeftTijdvakken;
	}

	public List<ToegestaneExamenstatusOvergang> getToegestaneExamenstatusOvergangen()
	{
		return toegestaneExamenstatusOvergangen;
	}

	public void setToegestaneExamenstatusOvergangen(
			List<ToegestaneExamenstatusOvergang> toegestaneExamenstatusOvergangen)
	{
		this.toegestaneExamenstatusOvergangen = toegestaneExamenstatusOvergangen;
	}

	public void setExamenWorflowTaxonomieen(List<ExamenWorkflowTaxonomie> examenWorflowTaxonomieen)
	{
		this.examenWorflowTaxonomieen = examenWorflowTaxonomieen;
	}

	public List<ExamenWorkflowTaxonomie> getExamenWorflowTaxonomieen()
	{
		return examenWorflowTaxonomieen;
	}

	public Examenstatus getGeslaagdStatus()
	{
		for (Examenstatus status : getExamenstatussen())
		{
			if (status.isGeslaagd())
			{
				return status;
			}
		}
		return null;
	}

	public Examenstatus getVoorlopigGeslaagdStatus()
	{
		for (Examenstatus status : getExamenstatussen())
		{
			if (status.getNaam().equals("Voorlopig geslaagd"))
			{
				return status;
			}
		}
		return null;
	}

	public Examenstatus getVoorlopigAfgewezenStatus()
	{
		for (Examenstatus status : getExamenstatussen())
		{
			if (status.getNaam().equals("Voorlopig afgewezen"))
			{
				return status;
			}
		}
		return null;
	}

	public Examenstatus getCertificatenStatus()
	{
		for (Examenstatus status : getExamenstatussen())
		{
			if (status.isCertificaten())
			{
				return status;
			}
		}
		return null;
	}

	@Override
	public String toString()
	{
		return naam;
	}
}
