/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.components.panels.datapanel.columns;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.dao.helpers.ExamendeelnameDataAccessHelper;
import nl.topicus.eduarte.krd.zoekfilters.ExamendeelnameZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;

/**
 * @author vandekamp
 */
public class ExamennummerColumn extends AbstractCustomColumn<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	public ExamennummerColumn(String id, String header)
	{
		super(id, header);
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<Verbintenis> rowModel, int span)
	{
		Verbintenis verbintenis = rowModel.getObject();
		ExamendeelnameDataAccessHelper helper =
			DataAccessRegistry.getHelper(ExamendeelnameDataAccessHelper.class);
		ExamendeelnameZoekFilter filter = new ExamendeelnameZoekFilter();
		filter.setVerbintenis(verbintenis);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));
		List<Examendeelname> deelnames = helper.list(filter);
		String nummer = "";
		if (!deelnames.isEmpty())
		{
			nummer = deelnames.get(0).getExamennummerMetPrefix();
		}
		cell.add(new Label(componentId, nummer));
	}
}
