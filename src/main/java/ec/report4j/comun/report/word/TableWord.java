
package ec.report4j.comun.report.word;

import java.util.List;
import java.util.Map;

/**
 *
 * @author fochoa
 *
 * @version 1.0
 *
 *          16/04/2019
 *
 *          La clase {@link TableWord} sirve para generar tablas en word.
 */
public class TableWord {

    private String[] columnas;
    private List<Map<String, String>> filas;

    public TableWord() {
        super();
    }

    public TableWord(String[] columnas, List<Map<String, String>> filas) {

        this.columnas = columnas;
        this.filas = filas;
    }

    public String[] getColumnas() {
        return columnas;
    }

    public void setColumnas(String[] columnas) {
        this.columnas = columnas;
    }

    public List<Map<String, String>> getFilas() {
        return filas;
    }

    public void setFilas(List<Map<String, String>> filas) {
        this.filas = filas;
    }

}
