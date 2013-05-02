package nl.topicus.eduarte.entities.resultaatstructuur;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ResultaatZoekFilterInstelling extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "medewerker")
	@Index(name = "idx_RZFI_medewerker")
	private Medewerker medewerker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "categorie")
	@Index(name = "idx_RZFI_categorie")
	private ResultaatstructuurCategorie categorie;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private Type type;

	@Column(nullable = true)
	private String codePath;

	@Column(nullable = false)
	private boolean gekoppeldAanVerbintenis;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "filterInstelling")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<GroepResultaatZoekFilterInstelling> groepInstellingen =
		new ArrayList<GroepResultaatZoekFilterInstelling>();

	public ResultaatZoekFilterInstelling()
	{
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	public ResultaatstructuurCategorie getCategorie()
	{
		return categorie;
	}

	public void setCategorie(ResultaatstructuurCategorie categorie)
	{
		this.categorie = categorie;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	public String getCodePath()
	{
		return codePath;
	}

	public void setCodePath(String codePath)
	{
		this.codePath = codePath;
	}

	public boolean isGekoppeldAanVerbintenis()
	{
		return gekoppeldAanVerbintenis;
	}

	public void setGekoppeldAanVerbintenis(boolean gekoppeldAanVerbintenis)
	{
		this.gekoppeldAanVerbintenis = gekoppeldAanVerbintenis;
	}

	public List<GroepResultaatZoekFilterInstelling> getGroepInstellingen()
	{
		return groepInstellingen;
	}

	public void setGroepInstellingen(List<GroepResultaatZoekFilterInstelling> groepInstellingen)
	{
		this.groepInstellingen = groepInstellingen;
	}
}
