package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import static nl.topicus.cobra.util.StringUtil.*;

import java.sql.Timestamp;
import java.util.Date;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.AjaxButtonColumn;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.krd.dao.helpers.MutatieLogVerwerkersLogDataAccessHelper;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkersLog;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hibernate.Hibernate;

/**
 * @author jutten
 */
public abstract class MutatieLogVerwerkersLogQueueTable extends
		CustomDataPanelContentDescription<MutatieLogVerwerkersLog>
{
	private static final long serialVersionUID = 1L;

	public MutatieLogVerwerkersLogQueueTable()
	{
		super("Mutatielog verwerkerslog wachtrij");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<MutatieLogVerwerkersLog>("Verwerker", "Verwerker",
			"verwerker")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel< ? > createLabelModel(IModel<MutatieLogVerwerkersLog> rowModel)
			{
				return Model.of(convertCamelCase(Hibernate.getClass(
					rowModel.getObject().getVerwerker()).getSimpleName()));
			}
		});
		addColumn(new BooleanPropertyColumn<MutatieLogVerwerkersLog>("Actief", "Actief", "actief"));
		addColumn(new BooleanPropertyColumn<MutatieLogVerwerkersLog>("Test geconfigureerd",
			"Test geconfigureerd", "testGeconfigureerd"));
		addColumn(new BooleanPropertyColumn<MutatieLogVerwerkersLog>("Productie geconfigureerd",
			"Productie geconfigureerd", "productieGeconfigureerd"));
		addColumn(new CustomPropertyColumn<MutatieLogVerwerkersLog>("Aantal", "Aantal",
			"queueCount"));
		addColumn(new AjaxButtonColumn<MutatieLogVerwerkersLog>("Reset", "Reset")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<MutatieLogVerwerkersLog> rowModel,
					AjaxRequestTarget target)
			{
				MutatieLogVerwerkersLog mutatieLogVerwerkersLog = rowModel.getObject();
				if (mutatieLogVerwerkersLog != null)
				{
					MutatieLogVerwerkersLogDataAccessHelper helper =
						DataAccessRegistry.getHelper(MutatieLogVerwerkersLogDataAccessHelper.class);

					Long laatstTeVerwerktenId =
						helper.getLaatstTeVerwerkenMutatieId(mutatieLogVerwerkersLog);

					if (laatstTeVerwerktenId != null)
					{
						mutatieLogVerwerkersLog.setLaatstVerwerktId(laatstTeVerwerktenId);
						mutatieLogVerwerkersLog.setLaatstVerwerktOp(new Timestamp(new Date()
							.getTime()));

						mutatieLogVerwerkersLog.saveOrUpdate();
						mutatieLogVerwerkersLog.commit();
					}
				}

				refreshDataPanel(target);
			}

			@Override
			protected String getCssDisabled()
			{
				return "";
			}

			@Override
			protected String getCssEnabled()
			{
				return "ui-icon ui-icon-arrowreturnthick-1-w";
			}
		});
	}

	protected abstract void refreshDataPanel(AjaxRequestTarget target);
}
