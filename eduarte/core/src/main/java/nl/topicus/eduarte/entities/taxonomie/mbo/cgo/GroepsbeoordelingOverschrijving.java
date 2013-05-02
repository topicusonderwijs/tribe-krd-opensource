package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.providers.GroepProvider;

import org.hibernate.annotations.Index;

/**
 * Houdt uitzonderingen voor een individuele deelnemer t.o.v. een Groepsbeoordeling bij.
 * Dus alleen de gewijzigde leerpunten worden hierin opgeslagen! N.b., deze overschrijving
 * wordt standaard leeg aangemaakt voor alle deelnemers op het moment dat een nieuwe
 * GroepsBeoordeling wordt aangemaakt. Voor het tonen van niet-overschreven leerpunten
 * worden die uit de GroepsBeoordeling gebruikt.
 * 
 * @author vandenbrink
 */
@Entity
public class GroepsbeoordelingOverschrijving extends Beoordeling implements GroepProvider
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "groepsBeoordeling", nullable = true)
	@Index(name = "idx_groepsBOV_groepsB")
	private Groepsbeoordeling groepsBeoordeling;

	public GroepsbeoordelingOverschrijving()
	{
	}

	public Groepsbeoordeling getGroepsBeoordeling()
	{
		return groepsBeoordeling;
	}

	public void setGroepsBeoordeling(Groepsbeoordeling groepsBeoordeling)
	{
		this.groepsBeoordeling = groepsBeoordeling;
	}

	@Override
	public Map<Leerpunt, CompetentieNiveau> getCompetentieNiveauAsMap()
	{
		Map<Leerpunt, CompetentieNiveau> ret =
			new HashMap<Leerpunt, CompetentieNiveau>(getGroepsBeoordeling()
				.getCompetentieNiveauAsMap());
		ret.putAll(getLocalCompetentieNiveauAsMap());
		return ret;
	}

	public boolean isAangepast()
	{
		return super.getCompetentieNiveaus().size() > 0;
	}

	private Map<Leerpunt, CompetentieNiveau> getLocalCompetentieNiveauAsMap()
	{
		return super.getCompetentieNiveauAsMap();
	}

	@Override
	public void setScore(Leerpunt leerpunt, MeeteenheidWaarde score)
	{

		Map<Leerpunt, CompetentieNiveau> groep = getGroepsBeoordeling().getCompetentieNiveauAsMap();
		Map<Leerpunt, CompetentieNiveau> local = getLocalCompetentieNiveauAsMap();
		if (groep.containsKey(leerpunt))
		{
			// NULL scores moeten in dit geval ook opgeslagen worden, omdat ze een
			// bestaand niveau uit de groepsbeoordeling overschrijven!
			CompetentieNiveau groepNiveau = groep.get(leerpunt);
			if (local.containsKey(leerpunt))
			{
				CompetentieNiveau localNiveau = local.get(leerpunt);
				if (groepNiveau.getScore().equals(score))
					competentieNiveaus.remove(localNiveau);
				else
					local.get(leerpunt).setScore(score);
			}

			else
			{
				if (!groepNiveau.getScore().equals(score))
				{
					CompetentieNiveau newNiveau = new CompetentieNiveau();
					newNiveau.setLeerpunt(leerpunt);
					newNiveau.setScore(score);
					newNiveau.setNiveauVerzameling(this);
					addCompetentieNiveau(newNiveau);
				}
			}
		}
		else
		{
			// NULL scores hoeven niet opgeslagen te worden als er geen corresponderend
			// niveau is in de originele groepsbeoordeling.
			if (local.containsKey(leerpunt))
			{
				CompetentieNiveau localNiveau = local.get(leerpunt);
				if (score != null)
					localNiveau.setScore(score);
				else
					competentieNiveaus.remove(localNiveau);
			}

			else
			{
				CompetentieNiveau newNiveau = new CompetentieNiveau();
				newNiveau.setLeerpunt(leerpunt);
				newNiveau.setScore(score);
				newNiveau.setNiveauVerzameling(this);
				addCompetentieNiveau(newNiveau);
			}
		}
	}

	public boolean isOpgenomen()
	{
		return getOpgenomenIn() != null;
	}

	@Override
	public Groep getGroep()
	{
		return getGroepsBeoordeling().getGroep();
	}
}
