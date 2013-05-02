/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.participatie.helpers.LesweekIndelingDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.LesdagIndeling;
import nl.topicus.eduarte.entities.participatie.LesuurIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.participatie.zoekfilters.LesweekindelingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 * Class voor het invoeren van een tijd of lesuurin een textfield. indien er een 2 cijfers
 * ingevoerd worden gaan we er vanuit dat het om een lesuur gaat
 * 
 * @author vandekamp
 */
public class TijdLesuurField extends TextField<Time>
{
	private IModel<OrganisatieEenheid> organisatieEenheidModel;

	private IModel<Locatie> locatieModel;

	private Date datum = TimeUtil.getInstance().currentDate();

	private int geldigLesuur;

	private boolean isBeginTijd;

	private Time time;

	public class DatumFieldAjaxHandler extends AjaxEventBehavior
	{
		private static final long serialVersionUID = 1L;

		public DatumFieldAjaxHandler()
		{
			super("onchange");
		}

		@Override
		protected CharSequence getEventHandler()
		{
			return generateCallbackScript(new AppendingStringBuffer("wicketAjaxPost('").append(
				getCallbackUrl()).append("', wicketSerialize(this)"));
		}

		@Override
		protected void onEvent(AjaxRequestTarget target)
		{
			TijdLesuurField field = TijdLesuurField.this;
			field.processInput();
			target.addComponent(field);
		}
	}

	private static final long serialVersionUID = 6883784609679074032L;

	public TijdLesuurField(String id)
	{
		this(id, null, null, null, true);
	}

	public TijdLesuurField(String id, IModel<Time> model,
			IModel<OrganisatieEenheid> organisatieEenheidModel, IModel<Locatie> locatieModel,
			boolean isBeginTijd)
	{
		super(id, model);
		this.organisatieEenheidModel = organisatieEenheidModel;
		this.locatieModel = locatieModel;
		this.isBeginTijd = isBeginTijd;
		setOutputMarkupId(true);
		add(new DatumFieldAjaxHandler());
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheidModel.getObject();
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheidModel.setObject(organisatieEenheid);
	}

	public Date getDatum()
	{
		return datum;
	}

	public void setDatum(Date datum)
	{
		this.datum = datum;
	}

	public int getGeldigLesuur()
	{
		return geldigLesuur;
	}

	public void setGeldigLesuur(int geldigLesuur)
	{
		this.geldigLesuur = geldigLesuur;
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		organisatieEenheidModel.detach();
		locatieModel.detach();
	}

	@Override
	protected Time convertValue(String[] value) throws ConversionException
	{
		String timeString = value != null && value.length > 0 ? value[0] : null;

		if (StringUtil.isNotEmpty(timeString))
		{
			if (timeString.trim().length() < 3)
			{
				int lesuur = 0;
				try
				{
					lesuur = Integer.parseInt(timeString);
				}
				catch (NumberFormatException exception)
				{
					error("Dit is geen geldig getal");
					return null;
				}
				List<LesuurIndeling> lesTijdenList = new ArrayList<LesuurIndeling>();

				LesweekindelingZoekFilter filter = new LesweekindelingZoekFilter();
				filter.setOrganisatieEenheid(getOrganisatieEenheid());
				filter.setLocatie(locatieModel.getObject());

				filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
					new AlwaysGrantedSecurityCheck()));

				LesweekIndeling lesweekIndeling =
					DataAccessRegistry.getHelper(LesweekIndelingDataAccessHelper.class)
						.getlesweekIndeling(filter);
				if (lesweekIndeling != null)
				{
					List<LesdagIndeling> lesdagen =
						new ArrayList<LesdagIndeling>(lesweekIndeling.getLesdagIndelingen());
					if (getDatum() == null)
						setDatum(TimeUtil.getInstance().currentDate());
					for (LesdagIndeling lesdag : lesdagen)
					{
						if (lesdag.valtOpDatum(datum))
							lesTijdenList = lesdag.getLesuurIndeling();
					}
				}
				if (lesuur != 0)
				{
					for (LesuurIndeling lestijd : lesTijdenList)
					{
						if (lestijd.getLesuur() == lesuur)
						{
							if (isBeginTijd)
								time = new Time(lestijd.getBeginTijd().getTime());
							else
								time = new Time(lestijd.getEindTijd().getTime());
							setGeldigLesuur(lesuur);
						}
					}
				}
				if (time == null)
				{
					error("Er is geen geldig lesuur opgegeven");
					return null;
				}
			}
			else
			{
				time = TimeUtil.getInstance().parseTimeString(timeString);
				if (time == null)
					setGeldigLesuur(0);
			}
			return time;
		}
		return null;
	}

}
