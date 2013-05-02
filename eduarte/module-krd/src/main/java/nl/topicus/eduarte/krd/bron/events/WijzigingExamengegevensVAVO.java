package nl.topicus.eduarte.krd.bron.events;

import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.TimeUtil;
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
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.ExamengegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.VakgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.ToepassingBeoordelingWerkstuk;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.UitslagExamen;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.ExamenUitslag;

public class WijzigingExamengegevensVAVO implements BronEvent<ExamengegevensRecord>
{
	private final BronAanleverMelding melding;

	private final Examendeelname deelname;

	private final BronEduArteModel model = new BronEduArteModel();

	public WijzigingExamengegevensVAVO(BronAanleverMelding melding, Examendeelname deelname)
	{
		this.melding = melding;
		this.deelname = deelname;
	}

	@Override
	public boolean isToepasselijk()
	{
		Verbintenis verbintenis = deelname.getVerbintenis();
		return verbintenis.isBronCommuniceerbaar() && deelname.isVAVO()
			&& deelname.getExamenjaar() != null;
	}

	@Override
	public ExamengegevensRecord createMelding()
	{
		if (!isToepasselijk())
			return null;

		melding.setBronOnderwijssoort(BronOnderwijssoort.VAVO);

		Verbintenis verbintenis = deelname.getVerbintenis();

		ExamengegevensRecord examen = findOrNew();
		examen.setExamenjaar(deelname.getExamenjaar());
		examen.setDatumUitslagExamen(deelname.getDatumUitslag());
		examen.setExamen(verbintenis.getExterneCode());
		examen.setUitslagExamen(getUitslag(deelname.getExamenstatus()));

		// TODO: nog bepalen wanneer dit een verwijdering betreft
		if (examen.getSoortMutatie() == null)
		{
			examen.setSoortMutatie(SoortMutatie.Toevoeging);
		}
		if (melding.getIngangsDatum() == null)
		{
			melding.setIngangsDatum(TimeUtil.vandaag());
		}

		createVakgegevensVAVOMelding(examen);
		return examen;
	}

	private ExamengegevensRecord findOrNew()
	{
		ExamengegevensRecord examen =
			melding.getExamengegevensRecord(ExamengegevensRecord.class, deelname);
		if (examen == null)
		{
			examen = BronBveAanleverRecord.newVavoExamengegevensRecord(melding, deelname);
		}
		return examen;
	}

	private UitslagExamen getUitslag(Examenstatus uitslag)
	{
		if (uitslag.isAfgewezen())
			return UitslagExamen.Afgewezen;
		if (uitslag.isCertificaten())
			return UitslagExamen.Certificaten;
		if (uitslag.isTeruggetrokken())
			return UitslagExamen.Teruggetrokken;
		if (uitslag.isGeslaagd())
			return UitslagExamen.Geslaagd;
		return null;
	}

	private void createVakgegevensVAVOMelding(ExamengegevensRecord examenmelding)
	{
		// Alleen bij een toevoeging alle vakgegevens meesturen
		if (examenmelding.getSoortMutatie() == SoortMutatie.Toevoeging)
		{
			Verbintenis verbintenis = deelname.getVerbintenis();
			Deelnemer deelnemer = verbintenis.getDeelnemer();

			for (OnderwijsproductAfnameContext context : verbintenis.getAfnameContexten())
			{
				OnderwijsproductAfname afname = context.getOnderwijsproductAfname();
				Onderwijsproduct product = afname.getOnderwijsproduct();

				ResultaatDataAccessHelper helper =
					DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class);

				List<OnderwijsproductAfname> afnames = verbintenis.getOnderwijsproductAfnames();

				Map<Onderwijsproduct, List<Resultaat>> ceResultaten =
					helper.getDefinitieveCentraalExamenResultaten(deelnemer, afnames);
				Map<Onderwijsproduct, Resultaat> seResultaten =
					helper.getDefinitieveSchoolexamenResultaten(deelnemer, afnames,
						TypeResultaat.InSamengesteld);

				if (product.getExterneCode() != null && seResultaten.containsKey(product))
				{
					if (product.getExterneCode().equals("9999")
						|| product.getExterneCode().equals("9998"))
					{
						// Profielwerkstuk. Geen vakgegevensmelding aanmaken maar gegevens
						// in examenmelding stoppen
						String werkstuktitel = afname.getWerkstuktitel();
						if (werkstuktitel != null && werkstuktitel.length() > 150)
							werkstuktitel = afname.getWerkstuktitel().substring(0, 150);
						examenmelding.setTitelThemaWerkstuk(werkstuktitel);

						if (context.getToepassingResultaatExamenvak() != null)
							examenmelding
								.setToepassingBeoordelingWerkstuk(ToepassingBeoordelingWerkstuk.Dispensatie
									.parse(context.getToepassingResultaatExamenvak()
										.getBRONString()));

						if (seResultaten.get(product).getToets().getSchaal().getSchaaltype()
							.equals(Schaaltype.Tekstueel))
							examenmelding.setBeoordelingWerkstuk(model
								.getBeoordelingWerkstuk(seResultaten.get(product)));
						else
						{
							Integer cijfer = model.getCijfer(seResultaten.get(product));
							if (cijfer != null)
								examenmelding.setCijferWerkstuk(String.valueOf(cijfer));
						}
					}
					else
					{
						VakgegevensRecord vakmelding = findOrNewVakmelding(context);

						vakmelding.setExamen(examenmelding.getExamen());
						vakmelding.setExamenjaar(deelname.getExamenjaar());
						vakmelding.setExamenvak(product.getExterneCode());
						vakmelding.setIndicatieDiplomavak(context.isDiplomavak());
						vakmelding.setToepassingResultaatExamenvak(context
							.getToepassingResultaatExamenvak());
						vakmelding.setIndicatieWerkstuk(context.isWerkstukHoortBijProduct());
						vakmelding.setVerwezenNaarVolgendeTijdvak(context
							.isVerwezenNaarVolgendTijdvak());

						// Voor tweede fase HAVO/VWO geldt: Certificaat mag niet gevuld
						// zijn
						String taxCode =
							verbintenis.getOpleiding().getVerbintenisgebied().getTaxonomiecode();
						if (!taxCode.startsWith("3.2.1") && !taxCode.startsWith("3.2.2"))
							vakmelding.setCertificaat(context.isCertificaatBehaald());

						vakmelding.setIndicatieCombinatieCijfer(context
							.isOnderdeelVanCombinatiecijfer(verbintenis.getAfnameContexten()));

						Resultaat seResultaat = seResultaten.get(product);
						if (seResultaat.getWaarde() != null)
						{
							vakmelding.setBeoordelingSchoolExamen(model.getBeoordeling(seResultaat
								.getWaarde()));
						}
						else
						{
							List<Resultaat> eindcijfers = null;
							if (seResultaat.getCijfer() != null)
							{
								Resultaat SE = seResultaten.get(product);
								vakmelding.setCijferSchoolExamen(model.getCijfer(SE));
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

									vakmelding.setCijferCE1(model.getCijfer(getHerkansing(
										ceCijfers, 0)));
									if (vakmelding.getCijferCE1() != null)
										vakmelding.setEersteEindcijfer(model
											.getCijfer(getHerkansing(eindcijfers, 0)));

									vakmelding.setCijferCE2(model.getCijfer(getHerkansing(
										ceCijfers, 1)));
									if (vakmelding.getCijferCE2() != null)
										vakmelding.setTweedeEindcijfer(model
											.getCijfer(getHerkansing(eindcijfers, 1)));

									vakmelding.setCijferCE3(model.getCijfer(getHerkansing(
										ceCijfers, 2)));
									if (vakmelding.getCijferCE3() != null)
										vakmelding.setDerdeEindcijfer(model
											.getCijfer(getHerkansing(eindcijfers, 2)));

								}
								if (eindcijfers != null)
								{
									vakmelding.setCijferCijferlijst(model
										.getCijfer(getGeldendResultaat(eindcijfers)));
								}
							}
						}

						if (context.isHogerNiveau())
						{
							vakmelding.setHogerNiveau(context.getHogerNiveau());
							vakmelding.setExamenvak(context.getExamenvakcode());
							vakmelding.setVakCodeHogerNiveau(Integer.parseInt(product
								.getExterneCode()));
						}
						if (melding.getUitslagExamen() != null
							&& melding.getUitslagExamen().equals(ExamenUitslag.DeelEindExamen))
						{
							vakmelding.setCertificaat(seResultaat.isBehaald());
						}
					}
				}
			}
		}
	}

	private VakgegevensRecord findOrNewVakmelding(OnderwijsproductAfnameContext context)
	{
		VakgegevensRecord vakmelding =
			melding.getVakgegevensRecord(VakgegevensRecord.class, context);

		if (vakmelding == null)
		{
			vakmelding =
				BronBveAanleverRecord.newVavoVakgegevensRecord(melding, deelname.getVerbintenis(),
					deelname, context);
		}

		return vakmelding;
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

	private Resultaat getGeldendResultaat(List<Resultaat> eindcijfers)
	{
		for (Resultaat resultaat : eindcijfers)
		{
			if (resultaat.isGeldend())
				return resultaat;
		}
		return null;
	}
}