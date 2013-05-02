package nl.topicus.cobra.update;

public interface DBVersionUpdateListener
{
	void onBeforeDefaultUpdates();

	void onAfterDefaultUpdates();

	void onBeforeSchemaUpdates();

	void onAfterSchemaUpdates();

	void onBeforeDataUpdates();

	void onAfterDataUpdates();

	void onBeforeConstraintUpdates();

	void onAfterConstraintUpdates();

	void onBeforeFinishUpdates();

	void onAfterFinishUpdates();
}
