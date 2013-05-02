package nl.topicus.eduarte.web.components.text;

import java.util.Random;

import nl.topicus.cobra.dao.EncryptionProvider;
import nl.topicus.cobra.security.CobraEncryptonProvider;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class APIKeyField extends TypedPanel<String>
{
	private static final long serialVersionUID = 1L;

	public APIKeyField(String id, IModel<String> model)
	{
		super(id, model);

		final Label apikey = new Label("apikey", model);
		apikey.setOutputMarkupId(true);
		apikey.add(new SimpleAttributeModifier("class", "unit_160 left"));

		AjaxLink<Void> generateAPIKey = new AjaxLink<Void>("generateAPIKey")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				EncryptionProvider encryptionProvider = new CobraEncryptonProvider();
				String genAPIKey =
					encryptionProvider.encrypt(TimeUtil.getInstance().currentDate().toString()
						+ getRandom());
				APIKeyField.this.setDefaultModelObject(genAPIKey.substring(0, 16));

				target.addComponent(apikey);
			}
		};

		add(apikey);
		add(generateAPIKey);
	}

	private String getRandom()
	{
		int MIN_LENGTH = 10;
		char[] goodChar =
			{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't',
				'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
				'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4',
				'5', '6', '7', '8', '9'};
		Random r = new Random();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < MIN_LENGTH; i++)
		{
			sb.append(goodChar[r.nextInt(goodChar.length)]);
		}
		return sb.toString();
	}
}
