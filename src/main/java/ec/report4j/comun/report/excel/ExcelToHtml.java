
package ec.report4j.comun.report.excel;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellRangeAddress;

import ec.report4j.comun.report.excepcion.ReportException;

/**
 *
 * @author fochoa
 *
 * @version 1.0
 *
 *          16/04/2019
 *
 *          La clase {@link ExcelToHtml} sirve para ayudar a generar el reporte de xls a html.
 */
class ExcelToHtml {

    private final StringBuilder out = new StringBuilder(65536);
    private final SimpleDateFormat sdf;
    private final HSSFWorkbook book;
    private final HSSFPalette palette;
    private final FormulaEvaluator evaluator;
    private short colIndex;
    private int rowIndex;
    private int mergeStart;
    private int mergeEnd;
    private Map<Integer, Map<Short, List<HSSFPictureData>>> pix = new HashMap<>();

    /**
     * Generates HTML from the InputStream of an Excel file. Generates sheet name in HTML h1 element.
     * 
     * @param in
     *            InputStream of the Excel file.
     * @throws ReportException
     *             When POI cannot read from the input stream.
     */
    public ExcelToHtml(final InputStream in) throws ReportException {
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if (in == null) {
            book = null;
            palette = null;
            evaluator = null;
            return;
        }
        try {
            book = new HSSFWorkbook(in);

            palette = book.getCustomPalette();
            evaluator = book.getCreationHelper().createFormulaEvaluator();
            for (int i = 0; i < book.getNumberOfSheets(); ++i) {
                table(book.getSheetAt(i));
            }
        } catch (Exception e) {
            throw new ReportException("Error de instanciación", e);
        }
    }

    /**
     * (Each Excel sheet produces an HTML table) Generates an HTML table with no cell, border spacing or padding.
     * 
     * @param sheet
     *            The Excel sheet.
     * @throws ReportException
     */
    private void table(final HSSFSheet sheet) throws ReportException {
        if (sheet == null) {
            return;
        }
        if (sheet.getDrawingPatriarch() != null) {
            final List<HSSFShape> shapes = sheet.getDrawingPatriarch().getChildren();
            for (int i = 0; i < shapes.size(); ++i) {
                if (shapes.get(i) instanceof HSSFPicture) {
                    procesarImagen(shapes.get(i));
                }
            }
        }

        out.append("<table cellspacing='0' style='border-spacing:0; border-collapse:collapse;'>\n");
        for (rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); ++rowIndex) {
            tr(sheet.getRow(rowIndex));
        }
        out.append("</table>\n");
    }

    private void procesarImagen(HSSFShape hssfShape) throws ReportException {
        try {
            // Gain access to private field anchor.
            final HSSFShape pic = hssfShape;
            final Field f = HSSFShape.class.getDeclaredField("anchor");
            f.setAccessible(true);
            final HSSFClientAnchor anchor = (HSSFClientAnchor) f.get(pic);
            // Store picture cell row, column and picture data.
            if (!pix.containsKey(anchor.getRow1())) {
                pix.put(anchor.getRow1(), new HashMap<Short, List<HSSFPictureData>>());
            }
            if (!pix.get(anchor.getRow1()).containsKey(anchor.getCol1())) {
                pix.get(anchor.getRow1()).put(anchor.getCol1(), new ArrayList<HSSFPictureData>());
            }
            pix.get(anchor.getRow1()).get(anchor.getCol1()).add(book.getAllPictures().get(((HSSFPicture) pic).getPictureIndex()));
        } catch (final Exception e) {

            throw new ReportException("Error de genracion de imagen", e);
        }
    }

    /**
     * (Each Excel sheet row becomes an HTML table row) Generates an HTML table row which has the same height as the Excel row.
     * 
     * @param row
     *            The Excel row.
     * @throws ReportException
     */
    private void tr(final HSSFRow row) {
        if (row == null) {
            return;
        }
        out.append("<tr ");
        // Find merged cells in current row.
        for (int i = 0; i < row.getSheet().getNumMergedRegions(); ++i) {
            final CellRangeAddress merge = row.getSheet().getMergedRegion(i);
            if (rowIndex >= merge.getFirstRow()
                    && rowIndex <= merge.getLastRow()) {
                mergeStart = merge.getFirstColumn();
                mergeEnd = merge.getLastColumn();
                break;
            }
        }
        out.append("style='");
        if (row.getHeight() != -1) {
            out.append("height: ").append(Math.round(row.getHeight()
                    / 20.0
                    * 1.33333)).append("px; ");
        }
        out.append("'>\n");
        for (colIndex = 0; colIndex < row.getLastCellNum(); ++colIndex) {
            td(row.getCell(colIndex));
        }
        out.append("</tr>\n");
    }

    /**
     * (Each Excel sheet cell becomes an HTML table cell) Generates an HTML table cell which has the same font styles, alignments, colours and borders as
     * the Excel cell.
     * 
     * @param cell
     *            The Excel cell.
     * @throws ReportException
     */
    private void td(final HSSFCell cell) {
        int colspan = 1;
        if (colIndex == mergeStart) {
            // First cell in the merging region - set colspan.
            colspan = mergeEnd
                    - mergeStart
                    + 1;
        } else if (colIndex == mergeEnd) {
            // Last cell in the merging region - no more skipped cells.
            mergeStart = -1;
            mergeEnd = -1;
            return;
        } else if (mergeStart != -1
                && mergeEnd != -1
                && colIndex > mergeStart
                && colIndex < mergeEnd) {
            // Within the merging region - skip the cell.
            return;
        }
        out.append("<td ");
        if (colspan > 1) {
            out.append("colspan='").append(colspan).append("' ");
        }
        if (cell == null) {
            out.append("/>\n");
            return;
        }
        out.append("style='");
        final HSSFCellStyle style = cell.getCellStyle();
        // Text alignment
        configurarAlineacion(style);
        // Font style, size and weight
        configurarColores(style);
        // Border
        configurarBordes(style);
        out.append("'>");
        String val = obtenerValorCelda(cell);
        if (pix.containsKey(rowIndex)
                && pix.get(rowIndex).containsKey(colIndex)) {

            for (final HSSFPictureData pic : pix.get(rowIndex).get(colIndex)) {
                out.append("<img alt='Image in Excel sheet' src='data:");
                out.append(pic.getMimeType());
                out.append(";base64,");
                out.append(new String(Base64.encodeBase64(pic.getData()), StandardCharsets.UTF_8));
                out.append("'/>");
            }

        }
        out.append(val);
        out.append("</td>\n");
    }

    private String obtenerValorCelda(HSSFCell cell) {
        String val = "";
        try {
            switch (cell.getCellType()) {
            case STRING:
                val = cell.getStringCellValue();
                break;
            case NUMERIC:
                // POI does not distinguish between integer and double, thus:
                final double original = cell.getNumericCellValue();
                final double rounded = Math.round(original);
                if (Math.abs(rounded
                        - original) < 0.00000000000000001) {
                    val = String.valueOf((int) rounded);
                } else {
                    val = String.valueOf(original);
                }
                break;
            case FORMULA:
                final CellValue cv = evaluator.evaluate(cell);
                switch (cv.getCellType()) {
                case BOOLEAN:
                    out.append(cv.getBooleanValue());
                    break;
                case NUMERIC:
                    out.append(cv.getNumberValue());
                    break;
                case STRING:
                    out.append(cv.getStringValue());
                    break;
                case BLANK:
                    break;
                case ERROR:
                    break;
                default:
                    val = obtenerValorCeldaDefecto(cell);
                    break;
                }
                break;
            default:

            }

        } catch (Exception e) {

            val = e.getMessage();
        }
        if ("null".equals(val)) {
            val = "";
        }
        return val;
    }

    private String obtenerValorCeldaDefecto(HSSFCell cell) throws ReportException {
        // Neither string or number? Could be a date.
        try {
            return sdf.format(cell.getDateCellValue());
        } catch (final Exception e1) {
            throw new ReportException("Error de formato de celda", e1);
        }
    }

    private void configurarAlineacion(HSSFCellStyle style) {
        switch (style.getAlignment()) {
        case LEFT:
            out.append("text-align: left; ");
            break;
        case RIGHT:
            out.append("text-align: right; ");
            break;
        case CENTER:
            out.append("text-align: center; ");
            break;
        default:
            break;
        }
    }

    private void configurarColores(HSSFCellStyle style) {

        final HSSFFont font = style.getFont(book);
        if (font.getBold()) {
            out.append("font-weight: bold; ");
        }
        if (font.getItalic()) {
            out.append("font-style: italic; ");
        }
        if (font.getUnderline() != HSSFFont.U_NONE) {
            out.append("text-decoration: underline; ");
        }
        out.append("font-size: ").append(Math.floor(font.getFontHeightInPoints()
                * 0.8)).append("pt; ");
        // Cell background and font colours
        final short[] backRGB = style.getFillForegroundColorColor().getTriplet();
        final HSSFColor foreColor = palette.getColor(font.getColor());
        if (foreColor != null) {
            final short[] foreRGB = foreColor.getTriplet();
            if (foreRGB[0] != 0
                    || foreRGB[1] != 0
                    || foreRGB[2] != 0) {
                out.append("color: rgb(").append(foreRGB[0]).append(',').append(foreRGB[1]).append(',').append(foreRGB[2]).append(");");
            }
        }
        if (backRGB[0] != 0
                || backRGB[1] != 0
                || backRGB[2] != 0) {
            out.append("background-color: rgb(").append(backRGB[0]).append(',').append(backRGB[1]).append(',').append(backRGB[2]).append(");");
        }
    }

    private void configurarBordes(HSSFCellStyle style) {
        if (style.getBorderTop() != BorderStyle.NONE) {
            out.append("border-top-style: solid; ");
        }
        if (style.getBorderRight() != BorderStyle.NONE) {
            out.append("border-right-style: solid; ");
        }
        if (style.getBorderBottom() != BorderStyle.NONE) {
            out.append("border-bottom-style: solid; ");
        }
        if (style.getBorderLeft() != BorderStyle.NONE) {
            out.append("border-left-style: solid; ");
        }
    }

    public String getHTML() {
        return out.toString();
    }
}