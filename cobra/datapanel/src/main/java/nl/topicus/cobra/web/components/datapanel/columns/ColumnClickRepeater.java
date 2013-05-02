package nl.topicus.cobra.web.components.datapanel.columns;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * Simpele interface om onClick door te geven aan een andere class. Omdat het model van
 * het item veelal de column elf zal bevatten is er een extra parameter waarin het row
 * model zit.
 * 
 * @author marrink
 */
public interface ColumnClickRepeater<T>
{
	/**
	 * @param item
	 *            this row
	 * @param rowModel
	 *            entity on display in this row
	 */
	public void onClick(WebMarkupContainer item, IModel<T> rowModel);
}
