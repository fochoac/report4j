
package ec.report4j.comun.report.word;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

/**
 *
 * @author fochoa
 *
 * @version 1.0
 *
 *          16/04/2019
 *
 *          The {@link WMLPackageUtil} is used for replace variables with respective value.
 */
class WMLPackageUtil {

    protected static final String CONTENT_TYPE = "";

    private WMLPackageUtil() {
        super();
    }

    public static WordprocessingMLPackage getWMLPackageTemplate(String filepath) throws Docx4JException, FileNotFoundException {
        return WordprocessingMLPackage.load(new FileInputStream(new File(filepath)));

    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getChildrenElements(Object source, Class<T> targetClass) {
        List<T> result = new ArrayList<>();

        Object target = XmlUtils.unwrap(source);

        if (targetClass.isAssignableFrom(target.getClass())) {
            result.add((T) target);
        } else if (target instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) target).getContent();

            if (targetClass.isAssignableFrom(children.getClass())) {
                result.add((T) children);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getTargetElements(Object source, Class<T> targetClass) {
        List<T> result = new ArrayList<>();

        Object target = XmlUtils.unwrap(source);

        if (targetClass.isAssignableFrom(target.getClass())) {
            result.add((T) target);
        } else if (target instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) target).getContent();
            for (Object child : children) {
                result.addAll(getTargetElements(child, targetClass));
            }
        }
        return result;
    }

    public static void replacePlaceholder(WordprocessingMLPackage template, String name, String placeholder) {

        List<Text> texts = getTargetElements(template.getMainDocumentPart(), Text.class);

        for (Text text : texts) {
            Text textElement = text;
            if (textElement.getValue().equals(placeholder)) {
                textElement.setValue(name);
            }
        }
    }

    public static void writeDocxToStream(WordprocessingMLPackage template, String target) throws Docx4JException {
        File f = new File(target);
        template.save(f);
    }

    public static void replaceParagraph(String placeholder, String textToAdd, WordprocessingMLPackage template, ContentAccessor addTo) {

        List<P> paragraphs = getTargetElements(template.getMainDocumentPart(), P.class);
        P toReplace = null;
        for (P p : paragraphs) {
            List<Text> texts = getTargetElements(p, Text.class);
            for (Text t : texts) {
                Text content = t;
                if (content.getValue().equals(placeholder)) {
                    toReplace = p;
                    break;
                }
            }
        }

        String[] as = StringUtils.splitPreserveAllTokens(textToAdd, '\n');

        for (int i = 0; i < as.length; i++) {
            String ptext = as[i];

            P copy = XmlUtils.deepCopy(toReplace);

            List<?> texts = getTargetElements(copy, Text.class);
            if (!texts.isEmpty()) {
                Text textToReplace = (Text) texts.get(0);
                textToReplace.setValue(ptext);
            }

            addTo.getContent().add(copy);
        }

        // 4. remove the original one
        if (toReplace != null) {
            ((ContentAccessor) toReplace.getParent()).getContent().remove(toReplace);
        }

    }

    public static Tbl getTable(List<Tbl> tables, String placeholder) {
        for (Iterator<Tbl> iterator = tables.iterator(); iterator.hasNext();) {
            Tbl tbl = iterator.next();

            List<Text> textElements = getTargetElements(tbl, Text.class);
            for (Text text : textElements) {
                Text textElement = text;

                if (textElement.getValue() != null
                        && textElement.getValue().equals(placeholder)) {
                    return tbl;
                }
            }
        }
        return null;
    }

    public static void replaceTable(String[] placeholders, List<Map<String, String>> textToAdd, WordprocessingMLPackage template) {
        List<Tbl> tables = getTargetElements(template.getMainDocumentPart(), Tbl.class);

        Tbl tempTable = getTable(tables, placeholders[0]);
        if (tempTable == null) {
            return;
        }
        List<Tr> rows = getTargetElements(tempTable, Tr.class);

        if (rows.size() == 2) {

            Tr templateRow = rows.get(1);

            for (Map<String, String> replacements : textToAdd) {

                addRowToTable(tempTable, templateRow, replacements);
            }

            tempTable.getContent().remove(templateRow);
        }
    }

    public static void addRowToTable(Tbl reviewtable, Tr templateRow, Map<String, String> replacements) {
        Tr workingRow = XmlUtils.deepCopy(templateRow);
        List<?> textElements = getTargetElements(workingRow, Text.class);
        for (Object object : textElements) {
            Text text = (Text) object;
            String replacementValue = replacements.get(text.getValue());
            if (replacementValue != null)
                text.setValue(replacementValue);
        }

        reviewtable.getContent().add(workingRow);
    }

}