package nl.topicus.eduarte.entities.bijlage;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateRecht;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Categorie van documenten bij deelnemers en andere entiteiten.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
public class DocumentCategorie extends CodeNaamActiefInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "documentCategorie")
	@BatchSize(size = 20)
	private List<DocumentTemplateRecht> rechten = new ArrayList<DocumentTemplateRecht>();

	@Column(nullable = false)
	private boolean beperkAutorisatie = false;

	public DocumentCategorie()
	{
	}

	public void setRechten(List<DocumentTemplateRecht> rechten)
	{
		this.rechten = rechten;
	}

	public List<DocumentTemplateRecht> getRechten()
	{
		return rechten;
	}

	public void setBeperkAutorisatie(boolean beperkAutorisatie)
	{
		this.beperkAutorisatie = beperkAutorisatie;
	}

	public boolean isBeperkAutorisatie()
	{
		return beperkAutorisatie;
	}

}
