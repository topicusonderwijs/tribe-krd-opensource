package nl.topicus.eduarte.entities.participatie;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.FieldContainerType;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.participatie.web.components.choice.combobox.KoppelingCombobox;

import org.apache.wicket.markup.html.form.PasswordTextField;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ExterneAgenda extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 50, nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	@Index(name = "idx_ExterneAgenda_koppeling")
	@AutoForm(editorClass = KoppelingCombobox.class)
	private ExterneAgendaKoppeling koppeling;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	@Index(name = "idx_ExterneAgenda_eigenaar")
	@AutoForm(include = false)
	private Persoon eigenaar;

	@Column(length = 100)
	@AutoForm(htmlClasses = "unit_max")
	private String gebruikersNaam;

	@Column(length = 100)
	@AutoForm(htmlClasses = "unit_max", editorClass = PasswordTextField.class, editorContainer = FieldContainerType.INPUT_PASSWORD)
	private String wachtwoord;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "externeAgenda")
	@AutoForm(include = false)
	private List<CacheRegion> cacheRegions = new ArrayList<CacheRegion>();

	public ExterneAgenda()
	{
	}

	public void setEigenaar(Persoon eigenaar)
	{
		this.eigenaar = eigenaar;
	}

	public Persoon getEigenaar()
	{
		return eigenaar;
	}

	public void setKoppeling(ExterneAgendaKoppeling koppeling)
	{
		this.koppeling = koppeling;
	}

	public ExterneAgendaKoppeling getKoppeling()
	{
		return koppeling;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getNaam()
	{
		return naam;
	}

	public List<CacheRegion> getCacheRegions()
	{
		return cacheRegions;
	}

	public void setCacheRegions(List<CacheRegion> cacheRegions)
	{
		this.cacheRegions = cacheRegions;
	}

	public void setGebruikersNaam(String gebruikersNaam)
	{
		this.gebruikersNaam = gebruikersNaam;
	}

	public String getGebruikersNaam()
	{
		return gebruikersNaam;
	}

	public void setWachtwoord(String wachtwoord)
	{
		this.wachtwoord = wachtwoord;
	}

	public String getWachtwoord()
	{
		return wachtwoord;
	}
}
