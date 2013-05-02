package nl.topicus.eduarte.krd.web.components.choice;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.providers.OpleidingProvider;

public class LeerwegCombobox extends EnumCombobox<MBOLeerweg>
{
	private static final long serialVersionUID = 1L;

	private final OpleidingProvider provider;

	public LeerwegCombobox(String id, OpleidingProvider provider)
	{
		super(false, id, MBOLeerweg.values());
		this.provider = provider;
	}

	@Override
	public boolean isRequired()
	{
		Opleiding opleiding = provider.getOpleiding();
		if (opleiding != null && opleiding.getVerbintenisgebied() != null
			&& opleiding.getVerbintenisgebied().getTaxonomie() != null)
		{
			return opleiding.getVerbintenisgebied().getTaxonomie().isBO();
		}
		return false;
	}

	@Override
	public boolean isVisible()
	{
		Opleiding opleiding = provider.getOpleiding();
		if (opleiding != null && opleiding.getVerbintenisgebied() != null
			&& opleiding.getVerbintenisgebied().getTaxonomie() != null)
		{
			return opleiding.getVerbintenisgebied().getTaxonomie().isBO();
		}
		return false;
	}

	@Override
	public boolean isEnabled()
	{
		Opleiding opleiding = provider.getOpleiding();
		return opleiding == null || !opleiding.isSaved();
	}
}
