package nl.topicus.eduarte.entities.dbs.trajecten;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.text.HtmlLabel;
import nl.topicus.eduarte.app.security.checks.ZorgvierkantObjectSecurityCheck;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.web.components.text.samenvatting.SamenvattingTextEditorPanel;

import org.apache.wicket.security.actions.Render;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * @author maatman
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Gesprek extends GeplandeBegeleidingsHandeling
{
	private static final long serialVersionUID = 1L;

	public static final String GESPREK = "GESPREK";

	@Lob()
	@AutoForm(displayClass = HtmlLabel.class, editorClass = SamenvattingTextEditorPanel.class)
	private String samenvatting;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GesprekSoort", nullable = true)
	@Basic(optional = false)
	@ForeignKey(name = "FK_Gesprek_soort")
	@Index(name = "idx_Gesprek_soort")
	private GesprekSoort gesprekSoort;

	public Gesprek()
	{
		soort = "Gesprek";
	}

	public Gesprek(Medewerker auteur, Traject traject)
	{
		this();
		setEigenaar(auteur);
		setTraject(traject);
	}

	public String getSamenvatting()
	{
		return samenvatting;
	}

	public void setSamenvatting(String samenvatting)
	{
		this.samenvatting = samenvatting;
	}

	public GesprekSoort getGesprekSoort()
	{
		return gesprekSoort;
	}

	public void setGesprekSoort(GesprekSoort gesprekSoort)
	{
		this.gesprekSoort = gesprekSoort;
	}

	public void setGesprekSoortUitgebreid(GesprekSoort gesprekSoort)
	{
		if (gesprekSoort != null)
		{
			setVerslagVersturen(gesprekSoort.isStandaardVerslagVersturen());
			if (gesprekSoort.getStandaardStatus() != null)
				setStatus(gesprekSoort.getStandaardStatus());
			if (gesprekSoort.getVerslagTemplate() != null)
				setSamenvatting(gesprekSoort.getVerslagTemplate());
		}
		setGesprekSoort(gesprekSoort);
	}

	@Override
	public String getSecurityId()
	{
		return GESPREK;
	}

	public boolean isSamenvattingVisible()
	{
		if (getStatus() != null)
			if (getStatus().equals(BegeleidingsHandelingsStatussoort.Voltooid))
				return true;
		return false;
	}

	@Override
	public String handelingsSoort()
	{
		if (getGesprekSoort() != null)
			return getGesprekSoort().getNaam();
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
	public String getExportableSamenvatting()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getSamenvatting();
		return null;

	}

	@Exportable
	public GesprekSoort getExportableGesprekSoort()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getGesprekSoort();
		return null;
	}

}
