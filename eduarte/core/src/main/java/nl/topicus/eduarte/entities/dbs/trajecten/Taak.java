package nl.topicus.eduarte.entities.dbs.trajecten;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.eduarte.app.security.checks.ZorgvierkantObjectSecurityCheck;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.apache.wicket.security.actions.Render;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Taak extends BegeleidingsHandeling
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TaakSoort", nullable = true)
	@Basic(optional = false)
	@ForeignKey(name = "FK_Taak_soort")
	@Index(name = "idx_Taak_soort")
	private TaakSoort taakSoort;

	@Lob
	private String opmerkingen;

	public Taak()
	{
		soort = "Taak";
	}

	public Taak(Medewerker auteur, Traject traject)
	{
		this();
		setEigenaar(auteur);
		setTraject(traject);
	}

	public TaakSoort getTaakSoort()
	{
		return taakSoort;
	}

	public void setTaakSoort(TaakSoort taakSoort)
	{
		this.taakSoort = taakSoort;
	}

	public String getOpmerkingen()
	{
		return opmerkingen;
	}

	public void setOpmerkingen(String opmerkingen)
	{
		this.opmerkingen = opmerkingen;
	}

	@Override
	public String handelingsSoort()
	{
		if (getTaakSoort() != null)
			return getTaakSoort().getNaam();
		return "";
	}

	/**************************************************************************************************************************
	 *** Sectie met getters voor samenvoeg velden
	 ************************************************************************************************************************** 
	 */

	private boolean doVertrouwlijkeSercurityCheck()
	{
		return ZorgvierkantObjectSecurityCheck.isAllowed(Render.class, this);
	}

	@Exportable
	public TaakSoort getExportableTaakSoort()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getTaakSoort();
		return null;
	}

	@Exportable
	public String getExportableOpmerkingen()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getOpmerkingen();
		return null;
	}
}
