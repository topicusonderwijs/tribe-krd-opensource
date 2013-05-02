package nl.topicus.eduarte.entities.dbs.trajecten;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.app.security.checks.ZorgvierkantObjectSecurityCheck;
import nl.topicus.eduarte.entities.participatie.Afspraak;

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
public abstract class GeplandeBegeleidingsHandeling extends BegeleidingsHandeling
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	@Basic(optional = false)
	private boolean uitnodigingenVersturen;

	@Column(nullable = true)
	@Basic(optional = false)
	private boolean verslagVersturen;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "afspraak", nullable = true)
	@Basic(optional = false)
	@ForeignKey(name = "FK_handeling_afspraak")
	@Index(name = "idx_handeling_afspraak")
	@AutoFormEmbedded()
	private Afspraak afspraak;

	public GeplandeBegeleidingsHandeling()
	{
	}

	public boolean isUitnodigingenVersturen()
	{
		return uitnodigingenVersturen;
	}

	public void setUitnodigingenVersturen(boolean uitnodigingenVersturen)
	{
		this.uitnodigingenVersturen = uitnodigingenVersturen;
	}

	public boolean isVerslagVersturen()
	{
		return verslagVersturen;
	}

	public void setVerslagVersturen(boolean verslagVersturen)
	{
		this.verslagVersturen = verslagVersturen;
	}

	public Afspraak getAfspraak()
	{
		return afspraak;
	}

	public void setAfspraak(Afspraak afspraak)
	{
		this.afspraak = afspraak;
	}

	public boolean isAfspraakVeldenEditable()
	{
		if (getStatus() != null)
		{
			if (getStatus().equals(BegeleidingsHandelingsStatussoort.Uitvoeren))
				return true;
			else if (getStatus().equals(BegeleidingsHandelingsStatussoort.Bespreken))
				return true;
			else if (getStatus().equals(BegeleidingsHandelingsStatussoort.Voltooid))
				return true;
		}
		return false;
	}

	@Override
	public Date getSorteerDatum()
	{
		if (getAfspraak().getBeginDatumTijd() != null)
			return getAfspraak().getBeginDatumTijd();
		else
			return getDeadline();
	}

	public boolean isDeadlineVisible()
	{
		if (getStatus() != null)
			if (getStatus().equals(BegeleidingsHandelingsStatussoort.Inplannen))
			{
				return true;
			}
			else
			{
				setDeadlineStatusovergang(null);
				return false;
			}
		return false;
	}

	/**************************************************************************************************************************
	 *** Sectie met getters voor samenvoeg velden
	 ************************************************************************************************************************** 
	 */

	private boolean doVertrouwlijkeSercurityCheck()
	{
		return ZorgvierkantObjectSecurityCheck.isAllowed(Render.class, this);
	}

	@Override
	@Exportable
	public Date getExportableSorteerDatum()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getSorteerDatum();
		return null;
	}
}
