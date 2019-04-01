# Generating reports with allure

Take the following steps to generate allure reports

* Execute the tests using maven `mvn clean test`
* Collect allure information `mvn allure:serve`
* Generate report `mvn allure:report`

The generated report is available in /target/site/allure-mave-plugin.

## Troubleshooting
### Maven Allure Plugin issues 
The allure maven-plugin will only work with Java 8. I used docker to generate the sources. 
Make sure to share your device and set the path_to_wikilenium_root correclty
```
docker run --rm -v <path_to_wikilenium_root>:/opt/maven -w /opt/maven maven:3.3.9-jdk-8 mvn allure:serve
docker run --rm -v <path_to_wikilenium_root>:/opt/maven -w /opt/maven maven:3.3.9-jdk-8 mvn allure:serve
```
In my case the command would be
```
docker run --rm -v C:\Users\rla\Documents\FS19\SWT\wikilenium\:/opt/maven -w /opt/maven maven:3.3.9-jdk-8 mvn allure:serve
```

### Displaying Issue in Broser
Chrome browser might not correctly display the report when you show index.html, because javascripts are not executed.
Opening the page with MS Edge worked correclty for me. 
