# REPORT4J

Utility component to generate excel, word and pdf documents.

# Excel Report

* Generate the template according to the specification of [jxls](http://jxls.sourceforge.net/).
* Fill the variables according to the following methods:

```java
    public abstract Report assignTemplate(InputStream plantilla, Map<String, Object> parametros) throws IOException;

    public abstract Report assignTemplate(File plantilla, Map<String, Object> parametros) throws IOException;

    public abstract Report assignTemplate(byte[] plantilla, Map<String, Object> parametros);

    public abstract Report buildReport() throws Throwable;

    public abstract byte[] exportBytes();

    public abstract OutputStream exportOS();

    public abstract String exportBase64();

    public abstract byte[] exportPdf() throws IOException;

    public abstract byte[] exportHtml() throws IOException;
```

* Instantiate the class:

```java
 byte[] result = Reporte.getExcelInstance().assignTemplate(TEMPLATE,Map<String, Object> parametros).buildReport().exportBytes();
```

# Word Report

* Generate the chord template according to the example of the unit tests.
* Fill the variables according to the following methods:

```java
public abstract Report assignTemplate(InputStream plantilla, Map<String, Object> parametros) throws IOException;

public abstract Report assignTemplate(File plantilla, Map<String, Object> parametros) throws IOException;

public abstract Report assignTemplate(byte[] plantilla, Map<String, Object> parametros);

public abstract Report buildReport() throws Throwable;

public abstract byte[] exportBytes();

public abstract OutputStream exportOS();

public abstract String exportBase64();

public abstract byte[] exportPdf() throws IOException;

public abstract byte[] exportHtml() throws IOException;

```
* Instantiate the class:

```java
 byte[] resultado = Reporte.getWordInstance().assignTemplate(PLANTILLA,Map<String, Object> parametros).buildReport().exportBytes();
```

If the word report presents tables, instantiate the class as follows:

```java
WordReport report = (WordReport) Reporte.getWordInstance();
byte[] pdf = report.assignTemplate(TEMPLATE, Map<String, Object> parametros, List<TablaWordUtil> tablas).buildReport().exportxxx();
        
```

# You want to collaborate with the project

Feel free to generate pull requests that help improve the functionality of the project. Any post change is welcome.
