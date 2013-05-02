package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.TaalbeoordelingDataAccessHelper;
import nl.topicus.eduarte.entities.LandelijkEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author vandenbrink
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public abstract class TaalscoreNiveauVerzameling extends LandelijkEntiteit
{
	private static final long serialVersionUID = 1L;

	@Temporal(value = TemporalType.DATE)
	private Date datum;

	@ManyToOne(optional = true)
	@JoinColumn(name = "taal", nullable = true)
	@Index(name = "idx_taalscoreNV_taal")
	private ModerneTaal taal;

	@OneToMany(mappedBy = "taalscoreNiveauVerzameling")
	private List<Taalscore> taalscores = new ArrayList<Taalscore>();

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "uitstroom", nullable = true)
	@Index(name = "idx_taalscoreNV_uitstroom")
	private Uitstroom uitstroom;

	@ManyToOne
	@Basic(optional = false)
	@JoinColumn(name = "meeteenheid", nullable = false)
	@Index(name = "idx_taalscoreNV_meeteenheid")
	private Meeteenheid meeteenheid;

	public Meeteenheid getMeeteenheid()
	{
		return meeteenheid;
	}

	public void setMeeteenheid(Meeteenheid meeteenheid)
	{
		this.meeteenheid = meeteenheid;
	}

	public Date getDatum()
	{
		return datum;
	}

	public void setDatum(Date datum)
	{
		this.datum = datum;
	}

	public ModerneTaal getTaal()
	{
		return taal;
	}

	public void setTaal(ModerneTaal taal)
	{
		this.taal = taal;
	}

	public List<Taalscore> getTaalscores()
	{
		return taalscores;
	}

	public Taalscore getTaalscore(Taalvaardigheid vaardigheid)
	{
		for (Taalscore curScore : getTaalscores())
		{
			if (curScore.getTaalvaardigheid().equals(vaardigheid))
			{
				return curScore;
			}
		}
		return null;
	}

	public void setTaalscores(List<Taalscore> taalscores)
	{
		this.taalscores = taalscores;
	}

	public void addTaalscore(Taalscore taalscore)
	{
		taalscores.add(taalscore);
	}

	public Uitstroom getUitstroom()
	{
		return uitstroom;
	}

	public void setUitstroom(Uitstroom uitstroom)
	{
		this.uitstroom = uitstroom;
	}

	public String getTaalscoresAsString()
	{
		StringBuilder taalscoresStringBuilder = new StringBuilder();

		for (Taalscore score : DataAccessRegistry.getHelper(TaalbeoordelingDataAccessHelper.class)
			.getTaalscoresOrdered(this))
		{
			taalscoresStringBuilder.append(score.getTaalvaardigheid().getTitel());
			taalscoresStringBuilder.append(": ");
			taalscoresStringBuilder.append(score.getWaarde().getLabel());
			taalscoresStringBuilder.append(", ");
		}
		String taalscoresString = taalscoresStringBuilder.toString();
		return taalscoresString.length() > 0 ? taalscoresString.substring(0, taalscoresString
			.length() - 2) : "";
	}

	public boolean isGelijkOfHogerDan(TaalscoreNiveauVerzameling vergelijk)
	{

		if (vergelijk == null)
			return true;
		TaalbeoordelingDataAccessHelper dao =
			DataAccessRegistry.getHelper(TaalbeoordelingDataAccessHelper.class);
		for (Taalscore score : getTaalscores())
		{
			Taalscore vglScore = dao.getTaalscore(score.getTaalvaardigheid(), vergelijk);
			if (vglScore == null)
				return true;
			if (vglScore.getWaarde().getWaarde() > score.getWaarde().getWaarde())
			{
				return false;
			}
		}

		return true;
	}
}
