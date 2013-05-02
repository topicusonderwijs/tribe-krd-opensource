package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import nl.topicus.eduarte.entities.groep.Groep;

import org.hibernate.annotations.Index;

/**
 * Een beoordeling die voor een gehele groep in een keer wordt aangemaakt. Kan van
 * toepassing zijn op een uitstroom of LLB matrix. N.b., deze beoordeling
 * tonen/samenvoegen/bewerken bij deelnemers gaat o.b.v. de
 * GroepsBeoordelingOverschrijvingen, die standaard leeg worden aangemaakt.
 * 
 * @author vandenbrink
 */
@Entity
public class Groepsbeoordeling extends Beoordeling
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "groep", nullable = true)
	@Index(name = "idx_groepsB_groep")
	private Groep groep;

	@OneToMany(mappedBy = "groepsBeoordeling")
	private List<GroepsbeoordelingOverschrijving> groepsbeoordelingOverschrijvingen =
		new ArrayList<GroepsbeoordelingOverschrijving>();

	public Groepsbeoordeling()
	{
	}

	public Groep getGroep()
	{
		return groep;
	}

	public void setGroep(Groep groep)
	{
		this.groep = groep;
	}

	public List<GroepsbeoordelingOverschrijving> getGroepsbeoordelingOverschrijvingen()
	{
		return groepsbeoordelingOverschrijvingen;
	}

	public void setGroepsbeoordelingOverschrijvingen(
			List<GroepsbeoordelingOverschrijving> groepsbeoordelingOverschrijvingen)
	{
		this.groepsbeoordelingOverschrijvingen = groepsbeoordelingOverschrijvingen;
	}

	public void addGroepsbeoordelingOverschrijving(GroepsbeoordelingOverschrijving overschrijving)
	{
		this.groepsbeoordelingOverschrijvingen.add(overschrijving);
	}

	public boolean isOverschrijvingOpgenomen()
	{
		for (GroepsbeoordelingOverschrijving curOverschrijving : getGroepsbeoordelingOverschrijvingen())
		{
			if (curOverschrijving.getOpgenomenIn() != null)
			{
				return true;
			}
		}
		return false;
	}
}
