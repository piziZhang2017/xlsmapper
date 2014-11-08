package org.mygreen.xlsmapper.cellconvert.converter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.mygreen.xlsmapper.POIUtils;
import org.mygreen.xlsmapper.Utils;
import org.mygreen.xlsmapper.XlsMapperConfig;
import org.mygreen.xlsmapper.XlsMapperException;
import org.mygreen.xlsmapper.annotation.converter.XlsArrayConverter;
import org.mygreen.xlsmapper.annotation.converter.XlsConverter;
import org.mygreen.xlsmapper.cellconvert.AbstractCellConverter;
import org.mygreen.xlsmapper.fieldprocessor.FieldAdaptor;


/**
 * {@link Set}型を変換するためのConverter。
 *
 * @version 1.0
 * @since 1.0
 * @author T.TSUCHIE
 *
 */
@SuppressWarnings("rawtypes")
public class SetCellConverter extends AbstractCellConverter<Set> {
    
    @SuppressWarnings("unchecked")
    @Override
    public Set toObject(final Cell cell, final FieldAdaptor adaptor, final XlsMapperConfig config)
            throws XlsMapperException {
        
        final ListCellConverter converter = new ListCellConverter();
        final XlsArrayConverter anno = converter.getLoadingAnnotation(adaptor);
        
        Class<?> itemClass = anno.itemClass();
        if(itemClass == Object.class) {
            itemClass = adaptor.getLoadingGenericClassType();
        }
        
        final List<?> list = converter.toObject(cell, adaptor, config);
        return new LinkedHashSet(list);
    }
    
    @Override
    public Cell toCell(final FieldAdaptor adaptor, final Object targetObj, final Sheet sheet, final int column, final int row,
            final XlsMapperConfig config) throws XlsMapperException {
        
        return toCell(adaptor, targetObj, sheet, column, row, config, null);
    }
    
    @Override
    public Cell toCellWithMap(FieldAdaptor adaptor, String key, Object targetObj, Sheet sheet,
            int column, int row, XlsMapperConfig config)
            throws XlsMapperException {
        return toCell(adaptor, targetObj, sheet, column, row, config, key);
    }
    
    @SuppressWarnings("unchecked")
    private Cell toCell(final FieldAdaptor adaptor, final Object targetObj, final Sheet sheet, final int column, final int row,
            final XlsMapperConfig config, final String mapKey) throws XlsMapperException {
        
        final ListCellConverter converter = new ListCellConverter();
        
        final XlsConverter converterAnno = adaptor.getLoadingAnnotation(XlsConverter.class);
        final XlsArrayConverter anno = converter.getSavingAnnotation(adaptor);
        
        Class<?> itemClass = anno.itemClass();
        if(itemClass == Object.class) {
            itemClass = adaptor.getSavingGenericClassType();
        }
        
        final Cell cell = POIUtils.getCell(sheet, column, row);
        
        // セルの書式設定
        if(converterAnno != null) {
            POIUtils.wrapCellText(cell, converterAnno.forceWrapText());
            POIUtils.shrinkToFit(cell, converterAnno.forceShrinkToFit());
        }
        
        Set value;
        if(mapKey == null) {
            value = (Set) adaptor.getValue(targetObj);
        } else {
            value = (Set) adaptor.getValueOfMap(mapKey, targetObj);
        }
        
        // デフォルト値から値を設定する
        if(Utils.isEmpty(value) && Utils.hasDefaultValue(converterAnno)) {
            final List<?> list = converter.convertList(Utils.getDefaultValue(converterAnno), itemClass, converterAnno, anno);
            value = new LinkedHashSet(list);
        }
        
        if(Utils.isNotEmpty(value)) {
            final String cellValue = Utils.join(value, anno.separator(), anno.ignoreEmptyItem());
            cell.setCellValue(cellValue);
        } else {
            cell.setCellType(Cell.CELL_TYPE_BLANK);
        }
        
        return cell;
    }

}