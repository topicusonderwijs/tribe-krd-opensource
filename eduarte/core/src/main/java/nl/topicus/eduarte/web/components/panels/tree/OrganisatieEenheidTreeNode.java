/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.tree.TreeNode;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.OrganisatieEenheidDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

import org.apache.wicket.model.IModel;

/**
 * Tree node voor gebruik in een organisatie-eenheidboom.
 * 
 * @author vandenbrink
 * @author loite
 */
public class OrganisatieEenheidTreeNode extends
		nl.topicus.cobra.web.components.tree.AbstractTreeNode<OrganisatieEenheid>
{
	private static final long serialVersionUID = 1L;

	private IModel<Date> peildatumModel;

	public OrganisatieEenheidTreeNode(TreeNode parent, OrganisatieEenheid organisatieEenheid,
			IModel<Date> peildatumModel)
	{
		super(parent, organisatieEenheid);
		this.peildatumModel = peildatumModel;
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return getObject();
	}

	@Override
	protected List< ? extends TreeNode> onGetChildren()
	{
		OrganisatieEenheid orgEenheid = getOrganisatieEenheid();
		OrganisatieEenheidDataAccessHelper helper =
			DataAccessRegistry.getHelper(OrganisatieEenheidDataAccessHelper.class);
		List<OrganisatieEenheid> organisatieEenheidChildren =
			helper.getDirectChildren(orgEenheid, null);
		List<OrganisatieEenheidTreeNode> treeChildren = new ArrayList<OrganisatieEenheidTreeNode>();
		for (OrganisatieEenheid child : organisatieEenheidChildren)
		{
			Date einddatumOrganisatie = child.getEinddatum();
			if (einddatumOrganisatie == null || peildatumModel == null
				|| peildatumModel.getObject() == null
				|| peildatumModel.getObject().before(einddatumOrganisatie))
				treeChildren.add(new OrganisatieEenheidTreeNode(this, child, peildatumModel));
		}
		return treeChildren;
	}

	@SuppressWarnings("unchecked")
	public List<OrganisatieEenheidTreeNode> getChildrenList()
	{
		return Collections.unmodifiableList((List<OrganisatieEenheidTreeNode>) super.getChildren());
	}
}
