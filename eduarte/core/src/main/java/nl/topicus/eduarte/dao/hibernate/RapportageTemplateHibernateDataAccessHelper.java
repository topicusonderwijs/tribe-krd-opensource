package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.RapportageTemplateDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.*;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.RapportageTemplate.OutputForm;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.RapportageTemplate.Purpose;

import org.hibernate.Criteria;

public class RapportageTemplateHibernateDataAccessHelper extends
		HibernateDataAccessHelper<RapportageTemplate> implements RapportageTemplateDataAccessHelper
{

	public RapportageTemplateHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List<RapportageTemplate> getTemplates(Medewerker medewerker)
	{
		Criteria criteria = createCriteria(RapportageTemplate.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("medewerker", medewerker);
		return cachedList(criteria);
	}

	@Override
	public void deleteTemplate(RapportageTemplate template)
	{
		if (template.isSaved())
		{
			template.delete();
			template.getSamenvoegenHtmlConfig().delete();
			template.getVoortgangHtmlConfig().delete();
			template.commit();
		}
	}

	@Override
	public void saveTemplate(RapportageTemplate template)
	{
		template.getSamenvoegenHtmlConfig().saveOrUpdate();
		template.getVoortgangHtmlConfig().saveOrUpdate();
		template.getSamenvoegenPdfConfig().saveOrUpdate();
		template.getVoortgangPdfConfig().saveOrUpdate();
		for (RapportageTemplateIJkpunt ijkpunt : template.getVoortgangPdfConfig().getIjkpunten())
			ijkpunt.saveOrUpdate();

		for (RapportageTemplateIJkpunt ijkpunt : template.getVoortgangHtmlConfig().getIjkpunten())
			ijkpunt.saveOrUpdate();
		template.saveOrUpdate();
		template.commit();
	}

	@Override
	public RapportageTemplate newTemplate(Medewerker medewerker)
	{
		RapportageTemplate template = new RapportageTemplate(EntiteitContext.INSTELLING);
		template.setMedewerker(medewerker);
		template.setOutputForm(OutputForm.HTML);
		template.setPurpose(Purpose.HUIDIGE_STAND);
		template.setIncludeUitstroom(true);
		template.setBegindatum(EduArteContext.get().getPeildatum());
		template.setVoortgangHtmlConfig(new VoortgangHtmlConfig());
		template.getVoortgangHtmlConfig().setGraphType(GraphType.BAR);
		template.getVoortgangHtmlConfig().setCategoryProperty(CategoryProperty.WERKPROCESSEN);
		template.getVoortgangHtmlConfig().setAantalBeoordelingen(3);
		template.getVoortgangHtmlConfig().setIncludeEvcEvk(true);
		template.getVoortgangHtmlConfig().setIncludeInvidueleIJkpunten(false);

		template.setSamenvoegenHtmlConfig(new SamenvoegenHtmlConfig());
		template.getSamenvoegenHtmlConfig().setGraphType(GraphType.BARCLUSTER);
		template.getSamenvoegenHtmlConfig().setCategoryProperty(CategoryProperty.WERKPROCESSEN);
		template.getSamenvoegenHtmlConfig().setSamenvoegenVanaf(
			Cohort.getHuidigCohort().getBegindatum());
		template.getSamenvoegenHtmlConfig().setSamenvoegenTot(TimeUtil.getInstance().currentDate());

		template.setVoortgangPdfConfig(new VoortgangPdfConfig());
		template.getVoortgangPdfConfig().setGraphType(GraphType.BAR);
		template.getVoortgangPdfConfig().setCategoryProperty(CategoryProperty.WERKPROCESSEN);
		template.getVoortgangPdfConfig().setAantalBeoordelingen(3);
		template.getVoortgangPdfConfig().setIncludeEvcEvk(true);
		template.getVoortgangPdfConfig().setIncludeInvidueleIJkpunten(false);
		template.getVoortgangPdfConfig().setCategoryAggregation(CategoryAggregation.KERNTAKEN);

		template.setSamenvoegenPdfConfig(new SamenvoegenPdfConfig());
		template.getSamenvoegenPdfConfig().setGraphType(GraphType.BARCLUSTER);
		template.getSamenvoegenPdfConfig().setCategoryProperty(CategoryProperty.WERKPROCESSEN);
		template.getSamenvoegenPdfConfig().setSamenvoegenVanaf(
			Cohort.getHuidigCohort().getBegindatum());
		template.getSamenvoegenPdfConfig().setSamenvoegenTot(TimeUtil.getInstance().currentDate());
		template.getSamenvoegenPdfConfig().setCategoryAggregation(CategoryAggregation.KERNTAKEN);

		return template;
	}
}
