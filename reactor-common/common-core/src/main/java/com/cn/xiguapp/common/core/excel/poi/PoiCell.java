package com.cn.xiguapp.common.core.excel.poi;

import com.cn.xiguapp.common.core.excel.Cell;
import com.cn.xiguapp.common.core.excel.CellDataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import java.math.BigDecimal;
import java.util.Optional;

import static org.apache.poi.ss.usermodel.DateUtil.isADateFormat;

/**
 * @author xiguaapp
 */
@Getter
@AllArgsConstructor
public class PoiCell implements Cell {

    private int sheetIndex;

    private org.apache.poi.ss.usermodel.Cell cell;

    private boolean end;

    private Object value;

    public PoiCell(int sheetIndex, org.apache.poi.ss.usermodel.Cell cell, boolean end) {
        this.sheetIndex = sheetIndex;
        this.cell = cell;
        this.end = end;
        this.value = convertValue();
    }

    private Object convertValue() {
        if (cell == null)
            return null;
        switch (cell.getCellType()) {
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                if (isCellDateFormatted()) {
                    return cell.getDateCellValue();
                }
                BigDecimal value = new BigDecimal(cell.getNumericCellValue());
                return value.scale() == 0 ? value.longValue() : value;
            case STRING:
                return cell.getRichStringCellValue().getString();
            case FORMULA:
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = evaluator.evaluate(cell);
                switch (cellValue.getCellType()) {
                    case BOOLEAN:
                        return cell.getBooleanCellValue();
                    case NUMERIC:
                        if (isCellDateFormatted()) {
                            return cell.getDateCellValue();
                        }
                        value = new BigDecimal(cell.getNumericCellValue());
                        return value.scale() == 0 ? value.longValue() : value;
                    case BLANK:
                        return "";
                    default:
                        return cellValue.getStringValue();
                }
            default:
                return cell.getStringCellValue();
        }
    }

    public boolean isCellDateFormatted() {
        if (cell == null) return false;
        boolean bDate = false;
        double d = cell.getNumericCellValue();
        if (DateUtil.isValidExcelDate(d)) {
            CellStyle style = cell.getCellStyle();
            if (style == null) return false;
            int i = style.getDataFormat();
            if (i == 58 || i == 31) return true;
            String f = style.getDataFormatString();
            f = f.replaceAll("[\"|\']", "").replaceAll("[年|月|日|时|分|秒|毫秒|微秒]", "");
            bDate = isADateFormat(i, f);
        }
        return bDate;
    }


    @Override
    public int getSheetIndex() {
        return sheetIndex;
    }

    @Override
    public long getRowIndex() {
        return cell.getRowIndex();
    }

    @Override
    public int getColumnIndex() {
        return cell.getColumnIndex();
    }

    @Override
    public Optional<Object> value() {
        return Optional.ofNullable(value);
    }

    @Override
    public CellDataType getType() {
        switch (cell.getCellType()) {
            case NUMERIC:
                return CellDataType.NUMBER;
            case FORMULA:
                return CellDataType.FORMULA;
            case BOOLEAN:
                return CellDataType.BOOLEAN;
            default:
                if (isCellDateFormatted()) {
                    return CellDataType.DATE_TIME;
                }
                return CellDataType.STRING;
        }
    }

}
