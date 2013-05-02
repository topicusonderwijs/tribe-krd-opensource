package nl.topicus.eduarte.web.components.resultaat;

import java.io.Serializable;
import java.util.Comparator;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

public abstract class AbstractToetsBoomComparator implements Comparator<Toets>, Serializable
{
	private static final long serialVersionUID = 1L;

	public static final boolean ASCENDING = true;

	public static final boolean DESCENDING = false;

	private int factor;

	public AbstractToetsBoomComparator(boolean ascending)
	{
		factor = ascending ? 1 : -1;
	}

	@Override
	@SuppressWarnings("all")
	public int compare(Toets arg0, Toets arg1)
	{
		if (arg0.equals(arg1))
			return 0;

		return factor * getPath(arg0).compareTo(getPath(arg1));
	}

	private String getPath(Toets toets)
	{
		Toets curToets = toets;
		String ret = "";
		while (curToets != null)
		{
			String path = getPathSegment(curToets);
			if (StringUtil.isNotEmpty(ret))
				ret = path + "/" + ret;
			else
				ret = path;
			curToets = curToets.getParent();
		}
		String prefix = getPathPrefix(toets.getResultaatstructuur());
		String postfix = getPathPostfix(toets.getResultaatstructuur());
		if (StringUtil.isNotEmpty(prefix))
			ret = prefix + "/" + ret;
		if (StringUtil.isNotEmpty(postfix))
			ret = ret + "/" + postfix;
		return ret;
	}

	protected abstract String getPathPrefix(Resultaatstructuur structuur);

	protected abstract String getPathPostfix(Resultaatstructuur structuur);

	protected abstract String getPathSegment(Toets toets);

	protected String volgnummerToString(int volgnummer)
	{
		return String.format("%05d", volgnummer);
	}

	protected String getResultaatstructuurPrefix(Resultaatstructuur structuur)
	{
		return structuur.getCohort() + "/" + structuur.getOnderwijsproduct().getCode() + "/"
			+ structuur.getId();
	}
}
