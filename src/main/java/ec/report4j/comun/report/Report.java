
package ec.report4j.comun.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import ec.report4j.comun.report.excel.ExcelReport;
import ec.report4j.comun.report.excepcion.ReportException;
import ec.report4j.comun.report.word.WordReport;

/**
 *
 * @author fochoa
 *
 * @version 1.0
 *
 *          16/04/2019
 *
 *          La clase {@link Report} sirve para generar reportes acorde a lo que se necesita.
 */
public abstract class Report {
    protected ByteArrayInputStream inputStream;
    protected ByteArrayOutputStream outputStream;
    protected WordprocessingMLPackage wordMLPackage;

    public Report() {

    }

    /**
     * Metodo que retorna una instancia para generar un reporte de word
     * 
     * @return {@link Report}
     */
    public static final Report getWordInstance() {
        return new WordReport();
    }

    /**
     * Metodo que retorna una instancia para generar un reporte de excel
     * 
     * @return
     */
    public static final Report getExcelInstance() {
        return new ExcelReport();
    }

    /**
     * Metodo para asignar la plantilla para generar un reporte
     * 
     * @param plantilla
     *            {@link InputStream}
     * @param parametros
     *            {@link Map}
     * @return {@link Report}
     * @throws ReportException
     *             en caso de error
     */
    public abstract Report assignTemplate(InputStream plantilla, Map<String, Object> parametros) throws ReportException;

    /**
     * Metodo para asignar la plantilla para generar un reporte
     * 
     * @param plantilla
     *            {@link File}
     * @param parametros
     *            {@link Map}
     * @return {@link Report}
     * @throws ReportException
     *             en caso de error
     */
    public abstract Report assignTemplate(File plantilla, Map<String, Object> parametros) throws ReportException;

    /**
     * Metodo para asignar la plantilla para generar un reporte
     * 
     * @param plantilla
     *            arreglo de {@link Byte}
     * @param parametros
     *            {@link Map}
     * @return {@link Report}
     * @throws ReportException
     *             en caso de error
     */
    public abstract Report assignTemplate(byte[] plantilla, Map<String, Object> parametros);

    /**
     * Metodo para construir le reporte. Debe ser usado despues del metodo: {@link #asignarPlantilla(byte[], Map)}, {@link #asignarPlantilla(File, Map)},
     * {@link #asignarPlantilla(InputStream, Map)}
     * 
     * @return {@link Report}
     * @throws ReportException
     *             en caso de error
     */
    public abstract Report buildReport() throws ReportException;

    /**
     * Metodo a ejecutarse despues de llamar al metodo: {@link #contruirReporte()}. Permite exportar el reporte en arreglo de bytes.
     * 
     * @return arreglo de {@link Byte}
     */
    public abstract byte[] exportBytes();

    /**
     * Metodo a ejecutarse despues de llamar al metodo: {@link #contruirReporte()}. Permite exportar el reporte en {@link OutputStream}.
     * 
     * @return {@link OutputStream}
     */
    public abstract OutputStream exportOS();

    /**
     * Metodo a ejecutarse despues de llamar al metodo: {@link #contruirReporte()}. Permite exportar el reporte en Base64.
     * 
     * @return cadena en base64
     */
    public abstract String exportBase64();

    /**
     * Metodo a ejecutarse despues de llamar al metodo: {@link #contruirReporte()}. Permite exportar el reporte a un arreglo de bytes que representan el
     * pdf.
     * 
     * @return arreglo de {@link Byte}
     */
    public abstract byte[] exportPdf() throws ReportException;

    /**
     * Metodo a ejecutarse despues de llamar al metodo: {@link #contruirReporte()}. Permite exportar el reporte en arreglo bytes que representan el html.
     * 
     * @return arreglo de {@link Byte}
     */
    public abstract byte[] exportHtml() throws ReportException;
}
