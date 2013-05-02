package nl.topicus.cobra.update.dbupdate.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;

public class CompositeAction extends AbstractAction
{
	private List<Action> actions = new ArrayList<Action>();

	public CompositeAction()
	{
	}

	public void addAction(Action action)
	{
		actions.add(action);
	}

	@Override
	protected void doAction(Option option)
	{
		for (Action curAction : actions)
			curAction.performAction(option);
	}
}
