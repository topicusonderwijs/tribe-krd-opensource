package nl.topicus.eduarte.resultaten.web.components.resultaat;

import java.util.concurrent.TimeoutException;

import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.resultaat.ResultaatVersionCollection;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.web.components.resultaat.AbstractResultatenPanel;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultatenOpslaanButton extends OpslaanButton
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(ResultatenOpslaanButton.class);

	private ResultatenEditModel resultatenModel;

	private ResultaatVersionCollection versions;

	private SecurePage returnPage;

	private boolean commit;

	@SpringBean
	private ResultaatDataAccessHelper resultaatHelper;

	public ResultatenOpslaanButton(BottomRowPanel bottomRow,
			AbstractResultatenPanel< ? , ResultatenEditModel> resultatenPanel,
			SecurePage returnPage, boolean commit)
	{
		this(bottomRow, resultatenPanel.getForm(), resultatenPanel.getResultatenModel(),
			returnPage, commit);
	}

	public ResultatenOpslaanButton(BottomRowPanel bottomRow, Form<Void> form,
			ResultatenEditModel resultatenModel, SecurePage returnPage, boolean commit)
	{
		super(bottomRow, form);
		this.resultatenModel = resultatenModel;
		this.returnPage = returnPage;
		this.commit = commit;
		versions = new ResultaatVersionCollection(resultatenModel.getDeelnemers());
	}

	@Override
	protected void onSubmit()
	{
		saveResultaten();
		setResponsePage(returnPage);
	}

	private void saveResultaten()
	{
		final ResultaatVersionCollection newVersions =
			new ResultaatVersionCollection(resultatenModel.getDeelnemers());
		try
		{
			log.info("Entering resultaten mutex for " + newVersions.getLockKeys().size() + " keys");
			EduArteApp.get().getResultaatMutex().execute(newVersions, 5000, new Runnable()
			{
				@Override
				public void run()
				{
					log.info("Inside lock: verifying versions");
					if (newVersions.verifyVersions(versions))
					{
						log.info("Inside lock: incrementing versions");
						newVersions.incrementAndSave();
						log.info("Inside lock: recalculating");
						resultatenModel.recalculateResultaten();
						resultaatHelper.flush();
						log.info("Inside lock: checking");
						resultatenModel.checkResultaten(false);
						if (commit)
							resultaatHelper.batchExecute();
					}
					else
					{
						log.info("Inside lock: version mismatch");
						returnPage.error("Een andere gebruiker heeft resultaten gewijzigd. "
							+ "Voer de resultaten a.u.b. opnieuw in.");
					}
				}
			});
			log.info("Left resultaten mutex");
		}
		catch (TimeoutException e)
		{
			log.info("Could not aquire lock: timeout");
			returnPage.error("De resultaten zijn in gebruik door een andere gebruiker of "
				+ "door het systeem. Probeer het later nog eens.");
		}
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		resultatenModel.detach();
	}
}
