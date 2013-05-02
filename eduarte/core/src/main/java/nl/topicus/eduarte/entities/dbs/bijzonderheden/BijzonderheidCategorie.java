package nl.topicus.eduarte.entities.dbs.bijzonderheden;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefLandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
public class BijzonderheidCategorie extends CodeNaamActiefLandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bijzonderheidCategorie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@AutoForm(include = false)
	private List<ToegestaanHulpmiddel> toegestaneHulpmiddelen =
		new ArrayList<ToegestaanHulpmiddel>();

	public BijzonderheidCategorie()
	{
	}

	public List<ToegestaanHulpmiddel> getToegestaneHulpmiddelen()
	{
		return toegestaneHulpmiddelen;
	}

	public void setToegestaneHulpmiddelen(List<ToegestaanHulpmiddel> toegestaneHulpmiddelen)
	{
		this.toegestaneHulpmiddelen = toegestaneHulpmiddelen;
	}

	@Override
	public String toString()
	{
		return getCode() + " - " + getNaam();
	}

	public boolean isHulpmiddelToegestaan(Hulpmiddel hulpmiddel)
	{
		for (ToegestaanHulpmiddel curHulpmiddel : getToegestaneHulpmiddelen())
		{
			if (curHulpmiddel.getHulpmiddel().equals(hulpmiddel))
				return true;
		}
		return false;
	}
}
