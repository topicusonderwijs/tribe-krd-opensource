package nl.topicus.cobra.templates.documents.csv;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

/**
 * Grotendeels dummy-implementatie van {@link CellStyle} voor het onthouden van een
 * {@link CSVDataFormat}.
 * 
 * @author hop
 */
public class CSVCellStyle implements CellStyle
{
	private short dataFormatIndex = -1;

	@Override
	public void cloneStyleFrom(CellStyle arg0)
	{
	}

	@Override
	public short getAlignment()
	{
		return 0;
	}

	@Override
	public short getBorderBottom()
	{
		return 0;
	}

	@Override
	public short getBorderLeft()
	{
		return 0;
	}

	@Override
	public short getBorderRight()
	{
		return 0;
	}

	@Override
	public short getBorderTop()
	{
		return 0;
	}

	@Override
	public short getBottomBorderColor()
	{
		return 0;
	}

	@Override
	public short getDataFormat()
	{
		return dataFormatIndex;
	}

	@Override
	public String getDataFormatString()
	{
		return new CSVDataFormat().getFormat(dataFormatIndex);
	}

	@Override
	public short getFillBackgroundColor()
	{
		return 0;
	}

	@Override
	public short getFillForegroundColor()
	{
		return 0;
	}

	@Override
	public short getFillPattern()
	{
		return 0;
	}

	@Override
	public short getFontIndex()
	{
		return 0;
	}

	@Override
	public boolean getHidden()
	{
		return false;
	}

	@Override
	public short getIndention()
	{
		return 0;
	}

	@Override
	public short getIndex()
	{
		return 0;
	}

	@Override
	public short getLeftBorderColor()
	{
		return 0;
	}

	@Override
	public boolean getLocked()
	{
		return false;
	}

	@Override
	public short getRightBorderColor()
	{
		return 0;
	}

	@Override
	public short getRotation()
	{
		return 0;
	}

	@Override
	public short getTopBorderColor()
	{
		return 0;
	}

	@Override
	public short getVerticalAlignment()
	{
		return 0;
	}

	@Override
	public boolean getWrapText()
	{
		return false;
	}

	@Override
	public void setAlignment(short arg0)
	{
	}

	@Override
	public void setBorderBottom(short arg0)
	{
	}

	@Override
	public void setBorderLeft(short arg0)
	{
	}

	@Override
	public void setBorderRight(short arg0)
	{
	}

	@Override
	public void setBorderTop(short arg0)
	{
	}

	@Override
	public void setBottomBorderColor(short arg0)
	{
	}

	@Override
	public void setDataFormat(short index)
	{
		dataFormatIndex = index;
	}

	@Override
	public void setFillBackgroundColor(short arg0)
	{
	}

	@Override
	public void setFillForegroundColor(short arg0)
	{
	}

	@Override
	public void setFillPattern(short arg0)
	{
	}

	@Override
	public void setFont(Font arg0)
	{
	}

	@Override
	public void setHidden(boolean arg0)
	{
	}

	@Override
	public void setIndention(short arg0)
	{
	}

	@Override
	public void setLeftBorderColor(short arg0)
	{
	}

	@Override
	public void setLocked(boolean arg0)
	{
	}

	@Override
	public void setRightBorderColor(short arg0)
	{
	}

	@Override
	public void setRotation(short arg0)
	{
	}

	@Override
	public void setTopBorderColor(short arg0)
	{
	}

	@Override
	public void setVerticalAlignment(short arg0)
	{
	}

	@Override
	public void setWrapText(boolean arg0)
	{
	}
}
