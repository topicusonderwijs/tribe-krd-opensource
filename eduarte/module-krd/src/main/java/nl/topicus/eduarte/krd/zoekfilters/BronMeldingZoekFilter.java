/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.zoekfilters;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.BronMeldingOnderdeel;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding.VoMeldingSoort;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronTerugkoppeling;
import nl.topicus.eduarte.krd.web.components.choice.BronAanleverpuntComboBox;
import nl.topicus.eduarte.krd.web.components.choice.BronMeldingGeblokkeerdComboBox;
import nl.topicus.eduarte.web.components.quicksearch.deelnemer.DeelnemerSearchEditor;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.IBronBatch;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

/**
 * Filter voor {@link IBronMelding} .
 * 
 * @author vandekamp
 */
public class BronMeldingZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<IBronMelding>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(label = "Batchnr", htmlClasses = "unit_40")
	private Integer batchnummer;

	@AutoForm(editorClass = BronAanleverpuntComboBox.class, htmlClasses = "unit_100")
	private IModel<BronAanleverpunt> aanleverpunt;

	@AutoForm(editorClass = DropDownChoice.class, htmlClasses = "unit_100")
	private IModel<Schooljaar> schooljaar;

	private IModel<IBronBatch> batch;

	@AutoForm(editorClass = DeelnemerSearchEditor.class)
	private IModel<Deelnemer> deelnemer;

	private IModel<Verbintenis> verbintenis;

	private IModel<Examendeelname> examendeelname;

	private IModel<BPVInschrijving> bpv;

	private BronMeldingOnderdeel bronMeldingOnderdeel;

	private SoortMutatie soortMutatie;

	private String creboIltCode;

	private BronMeldingStatus meldingStatus;

	@AutoForm(editorClass = BronMeldingGeblokkeerdComboBox.class)
	private Boolean geblokkeerd;

	@AutoForm(editorClass = EnumCombobox.class, htmlClasses = "unit_120")
	private BronOnderwijssoort bronOnderwijssoort;

	public enum TypeMelding
	{
		Regulier,
		Accountant,
	}

	private TypeMelding typeMelding;

	private IModel<BronSchooljaarStatus> bronSchooljaarStatus;

	private IModel<IBronTerugkoppeling> terugkoppelbestand;

	private BronMeldingOnderdeel onderdeelNot;

	private Integer deelnemerNummer;

	private VoMeldingSoort voMeldingSoort;

	private Boolean bekostigingsRelevant;

	public BronMeldingZoekFilter()
	{
	}

	public BronMeldingZoekFilter(Deelnemer deelnemer)
	{
		setDeelnemer(deelnemer);
	}

	public BronMeldingZoekFilter(Verbintenis verbintenis)
	{
		setVerbintenis(verbintenis);
	}

	public BronMeldingZoekFilter(BPVInschrijving bpv)
	{
		setBPV(bpv);
	}

	public BronMeldingZoekFilter(BronAanleverpunt aanleverpunt, Schooljaar schooljaar)
	{
		setAanleverpunt(aanleverpunt);
		setSchooljaar(schooljaar);
	}

	public BronMeldingZoekFilter(IModel<BronSchooljaarStatus> bronSchooljaarStatus)
	{
		this.bronSchooljaarStatus = bronSchooljaarStatus;
		setAanleverpunt(getBronSchooljaarStatus().getAanleverpunt());
		setSchooljaar(getBronSchooljaarStatus().getSchooljaar());
	}

	public void setBronMeldingOnderdeel(BronMeldingOnderdeel bronMeldingOnderdeel)
	{
		this.bronMeldingOnderdeel = bronMeldingOnderdeel;
	}

	public BronMeldingOnderdeel getBronMeldingOnderdeel()
	{
		return bronMeldingOnderdeel;
	}

	public void setCreboIltCode(String creboIltCode)
	{
		this.creboIltCode = creboIltCode;
	}

	public String getCreboIltCode()
	{
		return creboIltCode;
	}

	public void setSoortMutatie(SoortMutatie soortMutatie)
	{
		this.soortMutatie = soortMutatie;
	}

	public SoortMutatie getSoortMutatie()
	{
		return soortMutatie;
	}

	public void setMeldingStatus(BronMeldingStatus meldingStatus)
	{
		this.meldingStatus = meldingStatus;
	}

	public BronMeldingStatus getMeldingStatus()
	{
		return meldingStatus;
	}

	public void setTypeMelding(TypeMelding typeMelding)
	{
		this.typeMelding = typeMelding;
	}

	public TypeMelding getTypeMelding()
	{
		return typeMelding;
	}

	public void setBronOnderwijssoort(BronOnderwijssoort bronOnderwijssoort)
	{
		this.bronOnderwijssoort = bronOnderwijssoort;
	}

	public BronOnderwijssoort getBronOnderwijssoort()
	{
		return bronOnderwijssoort;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public void setBPV(BPVInschrijving bpv)
	{
		this.bpv = makeModelFor(bpv);
	}

	public BPVInschrijving getBPV()
	{
		return getModelObject(bpv);
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = makeModelFor(verbintenis);
	}

	public Verbintenis getVerbintenis()
	{
		return getModelObject(verbintenis);
	}

	public void setExamendeelname(Examendeelname examendeelname)
	{
		this.examendeelname = makeModelFor(examendeelname);
	}

	public Examendeelname getExamendeelname()
	{
		return getModelObject(examendeelname);
	}

	public void setBatch(IBronBatch batch)
	{
		this.batch = makeModelFor(batch);
	}

	public IBronBatch getBatch()
	{
		return getModelObject(batch);
	}

	public void setTerugkoppelbestand(IBronTerugkoppeling modelObject)
	{
		this.terugkoppelbestand = makeModelFor(modelObject);
	}

	public IBronTerugkoppeling getTerugkoppelbestand()
	{

		return getModelObject(terugkoppelbestand);
	}

	public void setSchooljaar(Schooljaar schooljaar)
	{
		this.schooljaar = makeModelFor(schooljaar);
	}

	public Schooljaar getSchooljaar()
	{
		return getModelObject(schooljaar);
	}

	public void setAanleverpunt(BronAanleverpunt aanleverpunt)
	{
		this.aanleverpunt = makeModelFor(aanleverpunt);
	}

	public BronAanleverpunt getAanleverpunt()
	{
		return getModelObject(aanleverpunt);
	}

	public void setBronSchooljaarStatus(BronSchooljaarStatus bronSchooljaarStatus)
	{
		this.bronSchooljaarStatus = makeModelFor(bronSchooljaarStatus);
	}

	public BronSchooljaarStatus getBronSchooljaarStatus()
	{
		return getModelObject(bronSchooljaarStatus);
	}

	public void setBatchnummer(Integer batchnummer)
	{
		this.batchnummer = batchnummer;
	}

	public Integer getBatchnummer()
	{
		return batchnummer;
	}

	public List<Schooljaar> getSchooljaarList()
	{
		List<Schooljaar> ret = new ArrayList<Schooljaar>();
		Schooljaar huidig = Schooljaar.huidigSchooljaar();
		ret.add(huidig.getVorigSchooljaar().getVorigSchooljaar());
		ret.add(huidig.getVorigSchooljaar());
		ret.add(huidig);
		ret.add(huidig.getVolgendSchooljaar());
		return ret;
	}

	public void setGeblokkeerd(Boolean geblokkeerd)
	{
		this.geblokkeerd = geblokkeerd;
	}

	public Boolean getGeblokkeerd()
	{
		return geblokkeerd;
	}

	public BronMeldingOnderdeel getBronMeldingOnderdeelNot()
	{
		return onderdeelNot;
	}

	public void setBronMeldingOnderdeelNot(BronMeldingOnderdeel onderdeelNot)
	{
		this.onderdeelNot = onderdeelNot;
	}

	public void setDeelnemerNummer(Integer deelnemerNummer)
	{
		this.deelnemerNummer = deelnemerNummer;
	}

	public Integer getDeelnemerNummer()
	{
		return deelnemerNummer;
	}

	public void setVoMeldingSoort(VoMeldingSoort soort)
	{
		this.voMeldingSoort = soort;
	}

	public VoMeldingSoort getVoMeldingSoort()
	{
		return voMeldingSoort;
	}

	public void setBekostigingsRelevant(Boolean b)
	{
		this.bekostigingsRelevant = b;
	}

	public Boolean getBekostigingsRelevant()
	{
		return this.bekostigingsRelevant;
	}
}
