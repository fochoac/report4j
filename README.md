[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=org.report%3Areport4j&metric=alert_status)](https://sonarcloud.io/dashboard?id=org.report%3Areport4j)

# REPORT4J

Utility component to generate excel, word and pdf documents.

# Excel Report

* Generate the template according to the specification of [jxls](http://jxls.sourceforge.net/).
* Fill the variables according to the following methods:

```java
 InputReportFile inputReportFile = new InputReportFile(PATH_TEMPLATE,
     MAP < String, Object > parameters);
 OutputReportFile outputReportFile = new OutputReportFile(TEMPORAL_FOLDER, "testPdf", OutputReportTypeEnum.PDF);
 ReportConfiguration configuration = new ReportConfiguration(inputReportFile, outputReportFile);
 Report excelReport = new ExcelReport(configuration);
 OutputReportFile report = excelReport.buildReport();
 report.saveFile();
 report.getFileBase64();
```



# Word Report

* Generate the chord template according to the example of the unit tests.
* Fill the variables according to the following methods:

```java
InputReportFile inputReportFile = new InputReportFile(PATH_TEMPLATE,
     MAP < String, Object > parameters);
 OutputReportFile outputReportFile = new OutputReportFile(TEMPORAL_FOLDER, "testPdf", OutputReportTypeEnum.PDF);
 ReportConfiguration configuration = new ReportConfiguration(inputReportFile, outputReportFile);
 Report report = new WordReport(configuration);
 OutputReportFile outReportFile = report.buildReport();
 outReportFile.saveFile();
 outReportFile.getFileBase64();

```


## Export excel or word document to:

- PDF
- HTML
- DOC or XLS only supports replace of variables into the document.

# You want to collaborate with the project

Feel free to generate pull requests that help improve the functionality of the project. Any post change is welcome.



