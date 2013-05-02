package nl.topicus.eduarte.web.components.panels.sidebar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.entities.sidebar.Bookmark;
import nl.topicus.eduarte.entities.sidebar.BookmarkConstructorArgument;
import nl.topicus.eduarte.entities.sidebar.BookmarkFolder;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.entities.sidebar.Bookmark.SoortBookmark;
import nl.topicus.eduarte.util.XmlSerializer;
import nl.topicus.eduarte.web.components.choice.BookmarkFolderCombobox;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Page voor BookmarkToevoegenModalWindow
 * 
 * @author loite
 */
public class BookmarkToevoegenPanel extends ModalWindowBasePanel<Bookmark>
{
	private static final long serialVersionUID = 1L;

	private SecurePage basePage;

	private Form<Bookmark> form;

	private IModel<Bookmark> bookmarkModel;

	private JaNeeCombobox contextgegevensVastzetten;

	public BookmarkToevoegenPanel(String id, SecurePage page,
			BookmarkToevoegenModalWindow modalWindow, IChangeRecordingModel<BookmarkFolder> folder)
	{
		super(id, modalWindow);

		this.basePage = page;

		Bookmark newBookmark = new Bookmark();
		newBookmark.setSoort(SoortBookmark.Bookmark);
		newBookmark.setPageClass(basePage.getClass().getName());
		if (folder != null)
			newBookmark.setBookmarkFolder(folder.getObject());

		bookmarkModel =
			ModelFactory.getCompoundChangeRecordingModel(newBookmark, folder != null ? folder
				.getManager() : new DefaultModelManager(Bookmark.class, BookmarkFolder.class));

		form = new Form<Bookmark>("form", bookmarkModel);
		RequiredTextField<String> omschrijving = new RequiredTextField<String>("omschrijving");
		ComponentUtil.fixLength(omschrijving, Bookmark.class);
		form.add(omschrijving);
		BookmarkFolderCombobox foldercombo =
			new BookmarkFolderCombobox("bookmarkFolder", new PropertyModel<BookmarkFolder>(
				bookmarkModel, "bookmarkFolder"));
		form.add(foldercombo);
		WebMarkupContainer vastzetten = new WebMarkupContainer("vastzetten");

		contextgegevensVastzetten =
			new JaNeeCombobox("contextgegevensVastzetten", new Model<Boolean>(Boolean.TRUE),
				new IChoiceRenderer<Boolean>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Object getDisplayValue(Boolean val)
					{
						return val ? "Huidige gegevens" : "Huidige pagina";
					}

					@Override
					public String getIdValue(Boolean val, int index)
					{
						return val.toString();
					}

				}, false);
		contextgegevensVastzetten.setNullValid(false);

		vastzetten.setVisible(basePage.containsBookmarkContextInformation());
		vastzetten.add(contextgegevensVastzetten);

		Label contextLabel = new Label("contextLabel", "Soort");
		vastzetten.add(contextLabel);

		form.add(vastzetten);
		add(form);

		WebMarkupContainer contextgegevens = new WebMarkupContainer("contextgegevens");
		final ListView<Class< ? extends IContextInfoObject>> contextInfoListView =
			new ListView<Class< ? extends IContextInfoObject>>("contextInfo", basePage
				.getContextParameterTypes())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<Class< ? extends IContextInfoObject>> item)
				{
					Class< ? extends IContextInfoObject> clazz = item.getModelObject();
					item.add(new Label("naam", clazz.getSimpleName()));
					IContextInfoObject obj = basePage.getContextValue(clazz);
					String value = obj == null ? null : obj.getContextInfoOmschrijving();
					item.add(new Label("waarde", value));
					basePage.detachModels();
				}

			};
		contextgegevens.setVisible(basePage.containsBookmarkContextInformation());
		contextgegevens.add(contextInfoListView);
		add(contextgegevens);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AjaxOpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > aform)
			{
				refreshFeedback(target);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > aform)
			{
				EduArteRequestCycle.get().openAndPushHibernateSession();
				try
				{
					Bookmark bookmark = form.getModelObject();
					bookmark.setAccount(EduArteContext.get().getAccount());
					List<Class< ? >> types = new ArrayList<Class< ? >>();
					List<Object> values = new ArrayList<Object>();
					basePage.getBookmarkConstructorArguments(types, values);
					boolean vastgezet = (contextgegevensVastzetten.getModelObject()).booleanValue();
					List<Class< ? extends IContextInfoObject>> contextParameterTypes;
					if (vastgezet)
					{
						contextParameterTypes = Collections.emptyList();
					}
					else
					{
						contextParameterTypes = basePage.getContextParameterTypes();
					}
					List<String> serializedValues = XmlSerializer.serializeValues(values);
					List<BookmarkConstructorArgument> arguments =
						bookmark.createBookmarkConstructorArguments(types, serializedValues,
							contextParameterTypes);
					bookmark.setArguments(arguments);
					bookmark.save();
					for (BookmarkConstructorArgument arg : arguments)
					{
						arg.save();
					}
					bookmark.commit();
				}
				finally
				{
					EduArteRequestCycle.get().closeAndPopHibernateSession();
				}
				getModalWindow().close(target);
			}
		}.setMakeDefault(true));
		panel.addButton(new AjaxAnnulerenButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				getModalWindow().close(target);
			}
		});
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(bookmarkModel);
	}
}
