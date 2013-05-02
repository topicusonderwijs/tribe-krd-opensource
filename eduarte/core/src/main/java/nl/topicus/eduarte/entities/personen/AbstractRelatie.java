package nl.topicus.eduarte.entities.personen;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.vrijevelden.RelatieVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldEntiteit;
import nl.topicus.eduarte.web.components.choice.RelatieSoortCombobox;
import nl.topicus.onderwijs.duo.bron.Bron;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * @author idserda
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
@org.hibernate.annotations.ForceDiscriminator
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(appliesTo = "AbstractRelatie", indexes = {
	@Index(name = "GENERATED_NAME_beg_eind_arch", columnNames = {"organisatie", "gearchiveerd",
		"einddatumNotNull", "begindatum"}),
	@Index(name = "GENERATED_NAME_beg_eind", columnNames = {"organisatie", "einddatumNotNull",
		"begindatum"}), @Index(name = "idx_per_relatie_soort", columnNames = {"relatieSoort"})})
@IsViewWhenOnNoise
public abstract class AbstractRelatie extends BeginEinddatumInstellingEntiteit implements
		VrijVeldable<RelatieVrijVeld>
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "persoon", nullable = false)
	@Index(name = "idx_Relatie_deelnemer")
	private Persoon deelnemer;

	@Column(nullable = true)
	private boolean wettelijkeVertegenwoordiger;

	@Column(nullable = true)
	private boolean betalingsplichtige;

	@Bron
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "relatieSoort", nullable = true)
	@AutoForm(label = "Relatie", required = true, editorClass = RelatieSoortCombobox.class)
	private RelatieSoort relatieSoort;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "relatie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@BatchSize(size = 20)
	private List<RelatieVrijVeld> vrijVelden = new ArrayList<RelatieVrijVeld>();

	public AbstractRelatie()
	{
	}

	public RelatieSoort getRelatieSoort()
	{
		return relatieSoort;
	}

	public void setRelatieSoort(RelatieSoort soort)
	{
		this.relatieSoort = soort;
	}

	public void setPersoon(Persoon persoon)
	{
		this.deelnemer = persoon;
	}

	public Persoon getPersoon()
	{
		return deelnemer;
	}

	public Persoon getDeelnemer()
	{
		return getPersoon();
	}

	public void setDeelnemer(Persoon deelnemer)
	{
		setPersoon(deelnemer);
	}

	public void setWettelijkeVertegenwoordiger(boolean wettelijkeVertegenwoordiger)
	{
		this.wettelijkeVertegenwoordiger = wettelijkeVertegenwoordiger;
	}

	public boolean isWettelijkeVertegenwoordiger()
	{
		return wettelijkeVertegenwoordiger;
	}

	public String isWettelijkeVertegenwoordigerOmschrijving()
	{
		return (isWettelijkeVertegenwoordiger() ? "Ja" : "Nee");
	}

	public void setBetalingsplichtige(boolean betalingsplichtige)
	{
		this.betalingsplichtige = betalingsplichtige;
	}

	public boolean isBetalingsplichtige()
	{
		return betalingsplichtige;
	}

	@AutoForm(include = true)
	public String isBetalingsplichtigeOmschrijving()
	{
		return (isBetalingsplichtige() ? "Ja" : "Nee");
	}

	public abstract VrijVeldable< ? extends VrijVeldEntiteit> getRelatie();

	@Override
	public List<RelatieVrijVeld> getVrijVelden()
	{
		return vrijVelden;
	}

	@Override
	public List<RelatieVrijVeld> getVrijVelden(VrijVeldCategorie categorie)
	{
		List<RelatieVrijVeld> res = new ArrayList<RelatieVrijVeld>();
		for (RelatieVrijVeld rvv : getVrijVelden())
		{
			if (rvv.getVrijVeld().getCategorie().equals(categorie))
			{
				res.add(rvv);
			}
		}
		return res;
	}

	@Exportable
	public String getVrijVeldWaarde(String naamVrijVeld)
	{
		for (RelatieVrijVeld vrijVeld : vrijVelden)
		{
			if (vrijVeld.getVrijVeld().getNaam().equals(naamVrijVeld))
				return vrijVeld.getOmschrijving();
		}
		return null;
	}

	@Override
	public RelatieVrijVeld newVrijVeld()
	{
		RelatieVrijVeld rvv = new RelatieVrijVeld();
		rvv.setRelatie(this);
		return rvv;
	}

	@Override
	public void setVrijVelden(List<RelatieVrijVeld> vrijvelden)
	{
		this.vrijVelden = vrijvelden;
	}
}
