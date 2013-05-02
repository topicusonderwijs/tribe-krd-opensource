/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs.gedrag;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.*;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormValidator;
import nl.topicus.cobra.web.components.text.HtmlLabel;
import nl.topicus.cobra.web.validators.DatumVandaagOfToekomstValidator;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.dbs.ZorgvierkantObject;
import nl.topicus.eduarte.entities.dbs.bijlagen.NotitieBijlage;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.web.components.choice.ZorglijnCombobox;
import nl.topicus.eduarte.web.components.text.samenvatting.SamenvattingTextEditorPanel;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Notitie extends InstellingEntiteit implements IBijlageKoppelEntiteit<NotitieBijlage>,
		ZorgvierkantObject
{
	private static final long serialVersionUID = 1L;

	public static final String NOTITIES = "NOTITIES";

	public static final String VERTROUWELIJKE_NOTITIES = "VERTROUWELIJKE_NOTITIES";

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "notitie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<NotitieBijlage> bijlagen = new ArrayList<NotitieBijlage>();

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	@Basic(optional = false)
	@AutoForm(label = "In dossier tot", htmlClasses = "unit_80", validators = @AutoFormValidator(validator = DatumVandaagOfToekomstValidator.class))
	private Date dossierEindDatum;

	@Column(length = 50, nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String titel;

	@Lob
	@AutoForm(editorClass = SamenvattingTextEditorPanel.class, displayClass = HtmlLabel.class)
	private String omschrijving;

	@Column(nullable = false)
	private boolean vertrouwelijk;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = false)
	@AutoForm(htmlClasses = "unit_80")
	private Date datumInvoer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auteur", nullable = false)
	@Index(name = "idx_notitie_auteur")
	private Medewerker auteur;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@AutoForm(label = "Zwaarte")
	private Kleur kleur;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	@Index(name = "idx_notitie_deelnemer")
	private Deelnemer deelnemer;

	@Column(nullable = true, length = 2)
	@AutoForm(editorClass = ZorglijnCombobox.class)
	private Integer zorglijn;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "notitie")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<NotitieNietTonenInZorgvierkant> nietTonenInZorgvierkants =
		new ArrayList<NotitieNietTonenInZorgvierkant>();

	public Notitie()
	{
	}

	public Notitie(Medewerker auteur, Deelnemer deelnemer)
	{
		setAuteur(auteur);
		setDeelnemer(deelnemer);
		setKleur(Kleur.Wit);
		setDatumInvoer(TimeUtil.getInstance().currentDate());
	}

	public List<NotitieBijlage> getBijlagen()
	{
		return bijlagen;
	}

	public void setBijlagen(List<NotitieBijlage> bijlagen)
	{
		this.bijlagen = bijlagen;
	}

	public Date getDossierEindDatum()
	{
		return dossierEindDatum;
	}

	public String getDossierEindDatumString()
	{
		if (getDossierEindDatum() != null)
		{
			return new SimpleDateFormat("dd MMMMM yyyy", new Locale("nl", "NL"))
				.format(getDossierEindDatum());
		}
		return "";
	}

	public void setDossierEindDatum(Date dossierEindDatum)
	{
		this.dossierEindDatum = dossierEindDatum;
	}

	@Override
	public NotitieBijlage addBijlage(Bijlage bijlage)
	{
		NotitieBijlage newBijlage = new NotitieBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setDeelnemer(getDeelnemer());
		newBijlage.setNotitie(this);

		getBijlagen().add(newBijlage);

		return newBijlage;
	}

	@Override
	public boolean bestaatBijlage(Bijlage bijlage)
	{
		for (NotitieBijlage deelbijlage : getBijlagen())
		{
			if (deelbijlage.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	@Override
	public String getSecurityId()
	{
		return NOTITIES;
	}

	@Override
	public String getVertrouwelijkSecurityId()
	{
		return VERTROUWELIJKE_NOTITIES;
	}

	public String getTitel()
	{
		return titel;
	}

	public void setTitel(String titel)
	{
		this.titel = titel;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public boolean isVertrouwelijk()
	{
		return vertrouwelijk;
	}

	public void setVertrouwelijk(boolean vertrouwelijk)
	{
		this.vertrouwelijk = vertrouwelijk;
	}

	public Date getDatumInvoer()
	{
		return datumInvoer;
	}

	public String getDatumInvoerString()
	{
		if (getDatumInvoer() != null)
		{
			return new SimpleDateFormat("dd MMMMM yyyy", new Locale("nl", "NL"))
				.format(getDatumInvoer());
		}
		return "";
	}

	public void setDatumInvoer(Date datumInvoer)
	{
		this.datumInvoer = datumInvoer;
	}

	public Medewerker getAuteur()
	{
		return auteur;
	}

	public void setAuteur(Medewerker auteur)
	{
		this.auteur = auteur;
	}

	public Kleur getKleur()
	{
		return kleur;
	}

	public void setKleur(Kleur kleur)
	{
		this.kleur = kleur;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public Integer getZorglijn()
	{
		return zorglijn;
	}

	public void setZorglijn(Integer zorglijn)
	{
		this.zorglijn = zorglijn;
	}

	private NotitieNietTonenInZorgvierkant findNietTonen()
	{
		Medewerker medewerker = EduArteContext.get().getMedewerker();
		for (NotitieNietTonenInZorgvierkant curTonen : getNietTonenInZorgvierkants())
		{
			if (curTonen.getMedewerker().equals(medewerker))
				return curTonen;
		}
		return null;
	}

	@Override
	public boolean isTonenInZorgvierkant()
	{
		return findNietTonen() == null;
	}

	public void setTonenInZorgvierkant(boolean tonenInZorgvierkant)
	{
		setTonenInZorgvierkant(tonenInZorgvierkant, false);
	}

	public void setTonenInZorgvierkant(boolean tonenInZorgvierkant, boolean save)
	{
		NotitieNietTonenInZorgvierkant nietTonen = findNietTonen();
		if (nietTonen == null && !tonenInZorgvierkant)
		{
			NotitieNietTonenInZorgvierkant newNietTonen =
				new NotitieNietTonenInZorgvierkant(this, EduArteContext.get().getMedewerker());
			getNietTonenInZorgvierkants().add(newNietTonen);

			if (save)
				newNietTonen.save();
		}
		else if (nietTonen != null && tonenInZorgvierkant)
		{
			getNietTonenInZorgvierkants().remove(nietTonen);

			if (save)
				nietTonen.delete();
		}
	}

	public List<NotitieNietTonenInZorgvierkant> getNietTonenInZorgvierkants()
	{
		return nietTonenInZorgvierkants;
	}

	public void setNietTonenInZorgvierkants(
			List<NotitieNietTonenInZorgvierkant> nietTonenInZorgvierkants)
	{
		this.nietTonenInZorgvierkants = nietTonenInZorgvierkants;
	}

	public String getCssClass()
	{
		if (getKleur() == null)
			return "";
		return getKleur().getCssClass();
	}

	@Override
	public String toString()
	{
		return getOmschrijving();
	}
}
