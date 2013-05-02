package nl.topicus.eduarte.entities.dbs.trajecten.templates;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.dbs.trajecten.Traject;
import nl.topicus.eduarte.entities.dbs.trajecten.TrajectUitvoerder;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.AfspraakParticipant;
import nl.topicus.eduarte.entities.participatie.enums.UitnodigingStatus;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author maatman
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public abstract class GeplandeBegeleidingsHandelingTemplate extends BegeleidingsHandelingTemplate
{
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "handeling")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<AanwezigenTemplate> aanwezigen = new ArrayList<AanwezigenTemplate>();

	public List<AanwezigenTemplate> getAanwezigen()
	{
		return aanwezigen;
	}

	public void setAanwezigen(List<AanwezigenTemplate> aanwezigen)
	{
		this.aanwezigen = aanwezigen;
	}

	@Override
	public String getAanwezigenOmschrijving()
	{
		String ret = StringUtil.toString(getAanwezigen(), ", ", "-");
		return StringUtil.truncate(ret, 100, "...");
	}

	protected void createParticipanten(Traject traject, Afspraak afspraak,
			List<AfspraakParticipant> participanten)
	{
		List<AanwezigenTemplate> templateAanwezigen = new ArrayList<AanwezigenTemplate>();

		if ("Gesprek".equals(getTypeOmschrijving()))
			templateAanwezigen = ((GesprekTemplate) doUnproxy()).getAanwezigen();
		else if ("TestAfname".equals(getTypeOmschrijving()))
			templateAanwezigen = ((TestAfnameTemplate) doUnproxy()).getAanwezigen();

		for (AanwezigenTemplate aanwezige : templateAanwezigen)
		{
			switch (aanwezige.getType())
			{
				case AlleUitvoerenden:
					for (TrajectUitvoerder uitvoerder : traject.getUitvoerders())
					{
						AfspraakParticipant uitvoerderParticipant = new AfspraakParticipant();
						uitvoerderParticipant.setMedewerker(uitvoerder.getMedewerker());
						uitvoerderParticipant
							.setUitnodigingStatus(UitnodigingStatus.DIRECTE_PLAATSING);
						uitvoerderParticipant.setAfspraak(afspraak);
						participanten.add(uitvoerderParticipant);
					}
					break;
				case Deelnemer:
					AfspraakParticipant deelnemerParticipant = new AfspraakParticipant();
					deelnemerParticipant.setDeelnemer(traject.getDeelnemer());
					deelnemerParticipant.setUitnodigingStatus(UitnodigingStatus.DIRECTE_PLAATSING);
					deelnemerParticipant.setAfspraak(afspraak);
					participanten.add(deelnemerParticipant);
					break;
				case EersteUitvoerende:
					if (traject.getUitvoerders().size() > 0)
					{
						AfspraakParticipant eersteUitvoerendeParticipant =
							new AfspraakParticipant();
						eersteUitvoerendeParticipant.setMedewerker(traject.getUitvoerders().get(0)
							.getMedewerker());
						eersteUitvoerendeParticipant
							.setUitnodigingStatus(UitnodigingStatus.DIRECTE_PLAATSING);
						eersteUitvoerendeParticipant.setAfspraak(afspraak);
						participanten.add(eersteUitvoerendeParticipant);
					}
					break;
				case Mentor:
					for (Medewerker mentor : traject.getDeelnemer().getBegeleidersOpDatum(
						traject.getBegindatum()))
					{
						AfspraakParticipant mentorParticipant = new AfspraakParticipant();
						mentorParticipant.setMedewerker(mentor);
						mentorParticipant.setUitnodigingStatus(UitnodigingStatus.DIRECTE_PLAATSING);
						mentorParticipant.setAfspraak(afspraak);
						participanten.add(mentorParticipant);
					}
					break;
				case OudersVerzorgers:
					// for (Relatie relatie :
					// traject.getDeelnemer().getVerzorgers())
					// {
					// AfspraakParticipant deelnemer =
					// new AfspraakParticipant();
					// verantwoordelijke.setDeelnemer(relatie.getVerzorger());
					// deelnemer.setUitnodigingStatus(UitnodigingStatus.DIRECTE_PLAATSING);
					// deelnemer.setAfspraak(afspraak);
					// participanten.add(deelnemer);
					// }
					break;
				case Verantwoordelijke:
					AfspraakParticipant verantwoordelijkeParticipant = new AfspraakParticipant();
					verantwoordelijkeParticipant.setMedewerker(traject.getVerantwoordelijke());
					verantwoordelijkeParticipant
						.setUitnodigingStatus(UitnodigingStatus.DIRECTE_PLAATSING);
					verantwoordelijkeParticipant.setAfspraak(afspraak);
					participanten.add(verantwoordelijkeParticipant);
					break;
				case GeselecteerdePersoon:
					break;
			}
		}
	}
}
