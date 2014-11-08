package org.mygreen.xlsmapper.fieldprocessor;

import java.lang.annotation.Annotation;

import org.mygreen.xlsmapper.XlsMapperException;
import org.mygreen.xlsmapper.annotation.converter.XlsConverter;
import org.mygreen.xlsmapper.cellconvert.CellConverter;
import org.mygreen.xlsmapper.cellconvert.CellConverterRegistry;
import org.mygreen.xlsmapper.cellconvert.ConversionException;
import org.mygreen.xlsmapper.cellconvert.DefaultCellConverter;


/**
 * 各種アノテーションを処理するためのクラスの抽象クラス。
 * <p>通常はこのクラスを継承して作成する。
 * 
 * @author T.TSUCHIE
 *
 */
public abstract class AbstractFieldProcessor<A extends Annotation> implements LoadingFieldProcessor<A>, SavingFieldProcessor<A> {
    
    /**
     * 該当するタイプのConverterが見つからないときの例外のインスタンスを作成する。
     * @param targetType クラスタイプ
     * @return
     */
    protected ConversionException newNotFoundConverterExpcetion(final Class<?> targetType) {
        return new ConversionException(
                String.format("not found CellConverter for type '%s'.", targetType.getName()),
                targetType);
    }
    
    /**
     * 読み込み時用のConveterを取得する。
     * <p>アノテーション「{@link XlsConverter#converterClass()}」が設定されていた場合を考慮した、個別のConverterを考慮する。
     * 
     * @param adaptor
     * @param converterResolver
     * @return
     * @throws XlsMapperException Converterが見つからない場合。
     */
    protected CellConverter<?> getLoadingCellConverter(final FieldAdaptor adaptor, final CellConverterRegistry converterResolver) throws XlsMapperException {
        
        final XlsConverter converterAnno = adaptor.getLoadingAnnotation(XlsConverter.class);
        final CellConverter<?> converter;
        
        if(converterAnno != null && !converterAnno.converterClass().equals(DefaultCellConverter.class)) {
            converter = converterResolver.createCellConverter((Class<CellConverter>) converterAnno.converterClass());
        } else {
            converter = converterResolver.getConverter(adaptor.getTargetClass());
            if(converter == null) {
                throw newNotFoundConverterExpcetion(adaptor.getTargetClass());
            }
        }
        
        return converter;
    }
    
    /**
     * 書き込み時用のConveterを取得する。
     * <p>アノテーション「{@link XlsConverter#converterClass()}」が設定されていた場合を考慮した、個別のConverterを考慮する。
     * 
     * @param adaptor
     * @param converterResolver
     * @return
     * @throws XlsMapperException Converterが見つからない場合。
     */
    protected CellConverter<?> getSavingCellConverter(final FieldAdaptor adaptor, final CellConverterRegistry converterResolver) throws XlsMapperException {
        
        final XlsConverter converterAnno = adaptor.getSavingAnnotation(XlsConverter.class);
        final CellConverter<?> converter;
        
        if(converterAnno != null && !converterAnno.converterClass().equals(DefaultCellConverter.class)) {
            try {
                converter = converterAnno.converterClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new XlsMapperException(String.format("fail create '%s' instance.", converterAnno.converterClass().getName()),
                        e);
            }
        } else {
            converter = converterResolver.getConverter(adaptor.getTargetClass());
            if(converter == null) {
                throw newNotFoundConverterExpcetion(adaptor.getTargetClass());
            }
        }
        
        return converter;
    }
}
