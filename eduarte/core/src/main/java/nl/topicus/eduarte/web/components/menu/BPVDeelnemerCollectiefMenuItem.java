package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AccessMenuItemKey;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;

/**
 * @author schimmel
 */
public enum BPVDeelnemerCollectiefMenuItem implements AccessMenuItemKey
{
	BPVKandidaten("BPV-kandidaten");

	private String label;

	private Character key;

	private BPVDeelnemerCollectiefMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
	}

	private BPVDeelnemerCollectiefMenuItem(String label)
	{
		this.label = label;
	}

	private BPVDeelnemerCollectiefMenuItem(Character key)
	{
		this.label = StringUtil.convertCamelCase(name());
		this.key = key;
	}

	private BPVDeelnemerCollectiefMenuItem(String label, Character key)
	{
		this.label = label;
		this.key = key;
	}

	@Override
	public String getLabel()
	{
		if (EduArteApp.get().isModuleActive(EduArteModuleKey.BPV_HOGERONDERWIJS))
		{
			this.label = "Stagekandidaten";
		}
		else
		{
			this.label = "BPV-kandidaten";
		}
		return label;
	}

	@Override
	public Character getAccessKey()
	{
		return key;
	}
}
