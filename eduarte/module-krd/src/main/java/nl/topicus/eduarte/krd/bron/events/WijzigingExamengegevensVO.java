package nl.topicus.eduarte.krd.bron.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.dao.hibernate.ResultaatHibernateDataAccessHelper.TypeResultaat;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.examen.Examenstatus;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal.Schaaltype;
import nl.topicus.eduarte.krd.bron.BronEduArteModel;
import nl.topicus.eduarte.krd.bron.BronEvent;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronExamenresultaatVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronVakGegegevensVOMelding;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.ExamenUitslag;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.ToepassingResultaat;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;

public class WijzigingExamengegevensVO implements BronEvent<BronExamenresultaatVOMelding>
{
	private final BronExamenresultaatVOMelding melding;

	private final Examendeelname deelname;

	private final BronEduArteModel model = new BronEduArteModel();

	public WijzigingExamengegevensVO(BronExamenresultaatVOMelding melding, Examendeelname deelname)
	{
		this.melding = melding;
		this.deelname = deelname;
	}

	@Override
	public boolean isToepasselijk()
	{
		Verbintenis verbintenis = deelname.getVerbintenis();
		return verbintenis.isVOVerbintenis() && verbintenis.isBronCommuniceerbaar();
	}

	@Override
	public BronExamenresultaatVOMelding createMelding()
	{
		if (!isToepasselijk())
			return null;

		Verbintenis verbintenis = deelname.getVerbintenis();
		Deelnemer deelnemer = verbintenis.getDeelnemer();
		melding.setExamendeelname(deelname);
		melding.setILTCode(Integer.parseInt(verbintenis.getExterneCode()));
		melding.setSoortMutatie(getSoortMutatie());
		melding.setExamenJaar(deelname.getExamenjaar());

		// Gooi de bestaande vakgegevens weg, anders komen ze dubbel in de melding te
		// staan.
		if (melding.isSaved() && melding.getVakgegevens().size() > 0)
		{
			for (BronVakGegegevensVOMelding vakmelding : melding.getVakgegevens())
				vakmelding.delete();
			melding.getVakgegevens().clear();
		}

		if (melding.getSoortMutatie() == SoortMutatie.Toevoeging)
		{
			melding.setUitslagExamen(getUitslag(deelname.getExamenstatus()));
			if (melding.getUitslagExamen() != null
				&& ExamenUitslag.GespreidExamen != melding.getUitslagExamen())
				melding.setDatumUitslagExamen(deelname.getDatumUitslag());
			List<BronVakGegegevensVOMelding> vakgegevens =
				new ArrayList<BronVakGegegevensVOMelding>();
			List<OnderwijsproductAfname> afnames = verbintenis.getOnderwijsproductAfnames();
			ResultaatDataAccessHelper helper =
				DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class);
			Map<Onderwijsproduct, List<Resultaat>> ceResultaten =
				helper.getDefinitieveCentraalExamenResultaten(deelnemer, afnames);
			Map<Onderwijsproduct, Resultaat> seResultaten =
				helper.getDefinitieveSchoolexamenResultaten(deelnemer, afnames,
					TypeResultaat.Geldend);

			for (OnderwijsproductAfnameContext context : verbintenis.getAfnameContexten())
			{
				OnderwijsproductAfname afname = context.getOnderwijsproductAfname();
				Onderwijsproduct product = afname.getOnderwijsproduct();
				ToepassingResultaat toepResultaat = context.getToepassingResultaat();
				if (product.getExterneCode() != null
					&& (seResultaten.containsKey(product)
						|| ToepassingResultaat.Vrijstelling == toepResultaat || ToepassingResultaat.Dispensatie == toepResultaat))
				{
					BronVakGegegevensVOMelding vakMelding = new BronVakGegegevensVOMelding(melding);
					vakMelding.setExamenVak(Integer.parseInt(product.getExterneCode()));
					vakMelding.setDiplomaVak(context.isDiplomavak());

					if (product.getExterneCode().equals("9999")
						|| product.getExterneCode().equals("9998"))
					{
						melding.setTitelOfThemaWerkstuk(afname.getWerkstuktitel());
						melding.setToepassingBeoordelingWerkstuk(context.getToepassingResultaat());
						if (seResultaten.get(product).getToets().getSchaal().getSchaaltype()
							.equals(Schaaltype.Tekstueel))
							melding.setBeoordelingWerkstuk(model
								.getBeoordelingWerkstuk(seResultaten.get(product)));
						else
							melding.setCijferWerkstuk(model.getCijfer(seResultaten.get(product)));
					}

					vakMelding.setToepassingResultaatOfBeoordelingExamenVak(toepResultaat);
					vakMelding.setIndicatieWerkstuk(context.isWerkstukHoortBijProduct());

					if (seResultaten.containsKey(product))
					{
						Resultaat seResultaat = seResultaten.get(product);
						if (seResultaat.getWaarde() != null)
							vakMelding.setBeoordelingSchoolExamen(model.getBeoordeling(seResultaat
								.getWaarde()));
						else
						{
							List<Resultaat> eindcijfers = null;
							if (seResultaat.getCijfer() != null)
							{
								Resultaat SE = seResultaten.get(product);
								vakMelding.setCijferSchoolExamen(model.getCijfer(SE));
								eindcijfers =
									helper.getActueleResultaten(SE.getToets().getParent(),
										deelnemer);
							}

							if ((melding.getUitslagExamen() != null && melding
								.getDatumUitslagExamen() != null)
								|| ExamenUitslag.GespreidExamen == melding.getUitslagExamen())
							{
								if (ceResultaten.containsKey(product)
									&& !ceResultaten.get(product).isEmpty())
								{
									List<Resultaat> ceCijfers = ceResultaten.get(product);
									eindcijfers =
										helper.getActueleResultaten(ceCijfers.get(0).getToets()
											.getParent(), deelnemer);

									vakMelding.setCijferCE1(model.getCijfer(getHerkansing(
										ceCijfers, 0)));
									if (vakMelding.getCijferCE1() != null)
									{
										vakMelding.setEersteEindcijfer(model
											.getCijfer(getHerkansing(eindcijfers, 0)));
										if (vakMelding.getEersteEindcijfer() == null)
											vakMelding.setEersteEindcijfer(model
												.getCijfer(getHerkansing(eindcijfers, -1)));
									}

									vakMelding.setCijferCE2(model.getCijfer(getHerkansing(
										ceCijfers, 1)));
									if (vakMelding.getCijferCE2() != null)
										vakMelding.setTweedeEindcijfer(model
											.getCijfer(getHerkansing(eindcijfers, 1)));

									vakMelding.setCijferCE3(model.getCijfer(getHerkansing(
										ceCijfers, 2)));
									if (vakMelding.getCijferCE3() != null)
										vakMelding.setDerdeEindcijfer(model
											.getCijfer(getHerkansing(eindcijfers, 2)));
								}
								if (eindcijfers != null)
								{
									vakMelding.setCijferCijferlijst(model
										.getCijfer(getGeldendResultaat(eindcijfers)));
								}
							}
						}

						if (melding.getUitslagExamen() != null
							&& (melding.getUitslagExamen().equals(ExamenUitslag.Afgewezen) || melding
								.getUitslagExamen().equals(ExamenUitslag.DeelEindExamen)))
						{
							vakMelding.setCertificaat(seResultaat.isBehaald());
						}
					}
					if (vakMelding.getCertificaat() == null)
						vakMelding.setCertificaat(false);
					vakMelding.setVerwezenNaarVolgendeTijdvak(context
						.isVerwezenNaarVolgendTijdvak());
					vakMelding.setIndicatieCombinatieCijfer(context
						.isOnderdeelVanCombinatiecijfer(verbintenis.getAfnameContexten()));
					if (context.isHogerNiveau())
					{
						vakMelding.setHogerNiveau(context.getHogerNiveau());
						vakMelding.setExamenVak(Integer.parseInt(context.getExamenvakcode()));
						vakMelding
							.setVakCodeHogerNiveau(Integer.parseInt(product.getExterneCode()));
					}
					// als het deze code heeft, is het een profielwerkstuk. Deze staat al
					// vermeld in het examenresultaat, en niet in de vakgegevens
					if (!(product.getExterneCode().equals("9999") || product.getExterneCode()
						.equals("9998")))
						vakgegevens.add(vakMelding);
				}
			}
			if (ExamenUitslag.Teruggetrokken != melding.getUitslagExamen() && vakgegevens.isEmpty())
				return null;
			melding.setVakgegevens(vakgegevens);
		}
		return melding;

	}

	private Resultaat getGeldendResultaat(List<Resultaat> eindcijfers)
	{
		for (Resultaat resultaat : eindcijfers)
		{
			if (resultaat.isGeldend())
				return resultaat;
		}
		return null;
	}

	private Resultaat getHerkansing(List<Resultaat> resultaten, int herkansingNr)
	{
		for (Resultaat resultaat : resultaten)
		{
			if (resultaat.getHerkansingsnummer() == herkansingNr)
				return resultaat;
		}
		return null;
	}

	private ExamenUitslag getUitslag(Examenstatus uitslag)
	{
		if (uitslag.isAfgewezen())
			return ExamenUitslag.Afgewezen;
		if (uitslag.isTeruggetrokken())
			return ExamenUitslag.Teruggetrokken;
		if (uitslag.isGespreidExamen())
			return ExamenUitslag.GespreidExamen;
		if (uitslag.isCertificaten())
			return ExamenUitslag.DeelEindExamen;
		if (uitslag.isGeslaagd())
			return ExamenUitslag.Geslaagd;
		return null;
	}

	private SoortMutatie getSoortMutatie()
	{
		if (deelname.getExamenstatus().isVerwijderd())
			return SoortMutatie.Verwijdering;
		return SoortMutatie.Toevoeging;
	}

}
