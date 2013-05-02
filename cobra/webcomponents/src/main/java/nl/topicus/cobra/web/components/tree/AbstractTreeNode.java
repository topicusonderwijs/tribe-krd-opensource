/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.tree;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.modelsv2.HibernateModel;

import org.apache.wicket.model.IDetachable;

/**
 * Abstracte treenode die ook een IModel is, deze klasse doet net alsof het een
 * LoadableDetachableModel is, maar dan voor een TreeNode.
 * 
 * @author Martijn Dashorst
 * @author bos
 * @param <T>
 */
public abstract class AbstractTreeNode<T extends IdObject> extends HibernateModel<T> implements
		TreeNode
{
	private static final long serialVersionUID = 1L;

	private final TreeNode parent;

	private List< ? extends TreeNode> children = null;

	public AbstractTreeNode(TreeNode parent, T object)
	{
		super(object);
		this.parent = parent;
	}

	public T getEntiteit()
	{
		return getObject();
	}

	public final TreeNode getParent()
	{
		return parent;
	}

	public boolean hasChildrenLoaded()
	{
		return children != null;
	}

	public void unloadChildren()
	{
		children = null;
	}

	@SuppressWarnings("unchecked")
	public final Enumeration children()
	{
		if (getChildren() != null)
		{
			return Collections.enumeration(children);
		}
		return Collections.enumeration(Collections.EMPTY_LIST);
	}

	protected List< ? extends TreeNode> getChildren()
	{
		if (children == null)
		{
			children = onGetChildren();
		}
		return children;
	}

	protected abstract List< ? extends TreeNode> onGetChildren();

	public boolean getAllowsChildren()
	{
		return true;
	}

	public final TreeNode getChildAt(int childIndex)
	{
		return getChildren().get(childIndex);
	}

	public int getChildCount()
	{
		return getChildren().size();
	}

	public int getIndex(TreeNode node)
	{
		return getChildren().indexOf(node);
	}

	public boolean isLeaf()
	{
		return getChildCount() == 0;
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		if (children != null)
		{
			for (Object child : children)
			{
				if (child instanceof IDetachable)
				{
					IDetachable detachable = (IDetachable) child;
					detachable.detach();
				}
			}
		}
	}
}
