package nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.opleiding.Opleiding;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author hop
 */
public class LeerjarenModel extends LoadableDetachableModel<List<Integer>>
{
	private static final long serialVersionUID = 1L;

	private IModel<Verbintenis> verbintenisModel;

	public LeerjarenModel(IModel<Verbintenis> verbintenisModel)
	{
		this.verbintenisModel = verbintenisModel;
	}

	@Override
	protected List<Integer> load()
	{
		int min = 1;
		int max = 6;
		if (getOpleiding() != null)
		{
			if (getOpleiding().getBeginLeerjaar() != null)
				min = getOpleiding().getBeginLeerjaar();
			if (getOpleiding().getEindLeerjaar() != null)
				max = getOpleiding().getEindLeerjaar();
		}
		List<Integer> result = new ArrayList<Integer>();
		for (int i = min; i <= max; i++)
			result.add(i);
		return result;
	}

	private Opleiding getOpleiding()
	{
		Verbintenis verbintenis = verbintenisModel.getObject();

		if (verbintenis != null)
			return verbintenis.getOpleiding();

		return null;
	}

	@Override
	public void detach()
	{
		super.detach();
		verbintenisModel.detach();
	}
}
