
package ec.report4j.comun.report.excepcion;

/**
 *
 * @author fochoa
 *
 * @version 1.0
 *
 *          16/04/2019
 *
 *          La clase {@link ReportException} sirve para gestionar las excepciones.
 */
public class ReportException extends Exception {

    private static final long serialVersionUID = 6841328548958871024L;

    public ReportException() {
        super();

    }

    public ReportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }

    public ReportException(String message, Throwable cause) {
        super(message, cause);

    }

    public ReportException(String message) {
        super(message);

    }

    public ReportException(Throwable cause) {
        super(cause);

    }

}
