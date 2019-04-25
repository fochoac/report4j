
package ec.report4j.comun.report.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import ec.report4j.comun.report.excepcion.ReportException;

/**
 *
 * @author fochoa
 *
 * @version 1.0
 *
 *          16/04/2019
 *
 *          La clase {@link Utilitary} sirve para gestionar converisiones.
 */
public class Utilitary {
    private static final Logger LOG = Logger.getLogger(Utilitary.class.getName());
    private static final String ERROR_PARSEO = "Error de conversión";

    private Utilitary() {
        super();
    }

    public static final byte[] convertir(InputStream inputStream) throws ReportException {
        try (InputStream stream = inputStream) {
            return IOUtils.toByteArray(stream);
        } catch (Exception e) {

            throw new ReportException(ERROR_PARSEO, e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                LOG.log(Level.SEVERE, ERROR_PARSEO, e);

            }
        }
    }

    public static final byte[] convertir(File file) throws ReportException {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new ReportException(ERROR_PARSEO, e);
        }

    }
}