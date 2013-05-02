package nl.topicus.eduarte.web.components.panels.signalen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.signalering.EventAbonnementInstelling;
import nl.topicus.eduarte.dao.helpers.EventAbonnementSettingDataAccessHelper;
import nl.topicus.eduarte.entities.signalering.settings.AbstractEventAbonnementConfiguration;
import nl.topicus.eduarte.entities.signalering.settings.GlobaalAbonnementSetting;
import nl.topicus.eduarte.entities.signalering.settings.PersoonlijkAbonnementSetting;

import org.apache.wicket.model.IModel;

public abstract class PersoonlijkBeheerSignalenPanel extends
		BeheerSignalenPanel<PersoonlijkAbonnementSetting>
{
	private static final long serialVersionUID = 1L;

	private Map<String, IModel<GlobaalAbonnementSetting>> standaardWaarden =
		new HashMap<String, IModel<GlobaalAbonnementSetting>>();

	public PersoonlijkBeheerSignalenPanel(String id,
			Class<PersoonlijkAbonnementSetting> settingClazz)
	{
		super(id, settingClazz);

		for (GlobaalAbonnementSetting curSetting : DataAccessRegistry.getHelper(
			EventAbonnementSettingDataAccessHelper.class).list(GlobaalAbonnementSetting.class))
		{
			standaardWaarden.put(getKey(curSetting), ModelFactory.getModel(curSetting));
		}
	}

	@Override
	public List<EventAbonnementInstelling> getAbonnementInstellingen()
	{
		return Arrays.asList(EventAbonnementInstelling.Aan, EventAbonnementInstelling.Uit);
	}

	@Override
	protected PersoonlijkAbonnementSetting createSetting()
	{
		return new PersoonlijkAbonnementSetting(EduArteContext.get().getGebruiker());
	}

	@Override
	protected boolean isEnabled(String key)
	{
		GlobaalAbonnementSetting standaard = getStandaardWaarde(key);
		return standaard == null
			|| !EventAbonnementInstelling.Verplicht.equals(standaard.getWaarde());
	}

	@Override
	public EventAbonnementInstelling getWaarde(String key)
	{
		GlobaalAbonnementSetting standaard = getStandaardWaarde(key);
		EventAbonnementInstelling instelling = getInstellingen().get(key);
		if (standaard == null)
			return instelling == null ? EventAbonnementInstelling.Aan : instelling;

		switch (standaard.getWaarde())
		{
			case Aan:
				return instelling == null ? EventAbonnementInstelling.Aan : instelling;
			case Uit:
				return instelling == null ? EventAbonnementInstelling.Uit : instelling;
			case Verplicht:
				return EventAbonnementInstelling.Aan;
		}
		return EventAbonnementInstelling.Aan;
	}

	@Override
	protected AbstractEventAbonnementConfiguration< ? > getStandaardConfiguratie(String key)
	{
		GlobaalAbonnementSetting standaard = getStandaardWaarde(key);
		return standaard == null ? null : standaard.getConfiguratie();
	}

	private GlobaalAbonnementSetting getStandaardWaarde(String key)
	{
		IModel<GlobaalAbonnementSetting> ret = standaardWaarden.get(key);
		return ret == null ? null : ret.getObject();
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(standaardWaarden);
	}
}
