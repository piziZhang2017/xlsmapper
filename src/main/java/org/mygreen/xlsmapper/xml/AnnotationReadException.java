package org.mygreen.xlsmapper.xml;

import org.mygreen.xlsmapper.XlsMapperException;


/**
 *
 *
 * @author T.TSUCHIE
 *
 */
public class AnnotationReadException extends XlsMapperException {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    public AnnotationReadException(final Throwable e) {
        super(e);
    }
    
    public AnnotationReadException(final String message, final Throwable e) {
        super(message, e);
    }
}
