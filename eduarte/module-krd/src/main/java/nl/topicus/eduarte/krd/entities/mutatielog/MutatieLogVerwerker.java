package nl.topicus.eduarte.krd.entities.mutatielog;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.LandelijkEntiteit;
import nl.topicus.eduarte.krd.jobs.GewijzigdRecord;
import nl.topicus.eduarte.util.IWebServiceClientContext;

import org.hibernate.Hibernate;

@Entity
@Table(name = "MUTATIELOGVERWERKERS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "naam", length = 4000, discriminatorType = DiscriminatorType.STRING)
public abstract class MutatieLogVerwerker extends LandelijkEntiteit
{
	public enum UpdateSoort
	{
		INSERT,
		UPDATE,
		DELETE;

		public static UpdateSoort from(String value)
		{
			for (UpdateSoort soort : values())
			{
				if (soort.name().startsWith(value.toUpperCase().substring(0, 1)))
					return soort;
			}
			return null;
		}
	}

	private static final long serialVersionUID = 1L;

	@ElementCollection(targetClass = String.class)
	@JoinTable(name = "MUTATIELOGVERWERKERSTABELLEN", joinColumns = {@JoinColumn(name = "VERWERKER", referencedColumnName = "ID")})
	@Column(name = "TABLE_NAME", length = 30, nullable = false)
	private List<String> tabellen;

	@Transient
	private Set<Number> verwerkteEntiteiten = new HashSet<Number>();

	public MutatieLogVerwerker()
	{
	}

	public abstract void verwerk(GewijzigdRecord record, IWebServiceClientContext webserviceContext);

	/**
	 * Om te voorkomen dat we binnen één run van de verwerker meerdere berichten versturen
	 * voor dezelfde (root)entiteit kan vastgehouden worden of deze al een keer verzonden
	 * is in de actuele run.
	 * <p>
	 * Bijvoorbeeld een deelnemer bericht kan getriggerd worden doordat er een adres
	 * gewijzigd is, en een contactgegeven en iets op zijn persoon. Hierdoor zou er 3x een
	 * bericht voor de deelnemer verzonden worden. Meeste client systemen zijn alleen
	 * geinteresseerd in de meest actuele gegevens, en niet in de individuele transacties.
	 * <p>
	 * Door de deelnemer te registreren als verwerkt (bijvoorbeeld door het
	 * deelnemernummer te markeren) wordt er slechts één keer een bericht verzonden.
	 * <p>
	 * Dit kan niet algemeen geregeld worden (a priori) omdat we op het algemene niveau
	 * niet weten wat de root entiteit is, en of de service geinteresseerd is in alle
	 * transacties of alleen een update.
	 * 
	 * @return <tt>true</tt> wanneer de entiteitId al gemarkeerd is als verwerkt
	 * @see #markeerVerwerkt(Entiteit)
	 */
	protected boolean isVerwerkt(Long entiteitId)
	{
		return verwerkteEntiteiten.contains(entiteitId);
	}

	protected boolean isVerwerkt(Entiteit entiteit)
	{
		return entiteit != null && verwerkteEntiteiten.contains(entiteit.getId());
	}

	/**
	 * @see #isVerwerkt(Long)
	 */
	protected void markeerVerwerkt(Entiteit entiteit)
	{
		if (entiteit != null && entiteit.getId() != null)
		{
			verwerkteEntiteiten.add(entiteit.getId());
		}
	}

	/**
	 * @see #isVerwerkt(Long)
	 */
	protected void markeerVerwerkt(Long entiteitId)
	{
		if (entiteitId != null)
		{
			verwerkteEntiteiten.add(entiteitId);
		}
	}

	/**
	 * Voegt de entiteit weer toe aan het eind van de mutatielog door de entiteit te
	 * 'touchen'.
	 */
	protected void markeerFout(Entiteit entiteit)
	{
		if (entiteit != null)
		{
			entiteit.touch();
		}
	}

	public String getNaam()
	{
		return Hibernate.getClass(this).getName();
	}

	public List<String> getTabellen()
	{
		return tabellen;
	}

	public void setTabellen(List<String> tabellen)
	{
		this.tabellen = tabellen;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName();
	}
}
