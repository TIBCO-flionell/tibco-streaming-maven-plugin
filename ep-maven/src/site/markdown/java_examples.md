# Java fragment examples

* [Codeline structure](#codeline-structure)
* [Basic build, junit test and install](#basic-build-junit-test-and-install)
* [Adding additional files to the java fragment](#adding-additional-files-to-the-java-fragment)
* [Java dependencies](#java-dependencies)
* [Code coverage via cobertura](#code-coverage-via-cobertura)

<a name="codeline-structure"></a>

## Codeline structure
  
The recommended java codeline structure is :
  
![Java directory structure](uml/java-structure.svg)

<a name="basic-build-junit-test-and-install"></a>

## Basic build, junit test and install

The following pom.xml will build, unit test and install to the local maven 
repository, a java fragment.

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <!-- vim: set tabstop=4 softtabstop=0 expandtab shiftwidth=4 smarttab : -->

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.tibco.ep</groupId>
    <artifactId>jfrag</artifactId>
    <packaging>ep-java-fragment</packaging>
    <version>1.0.0</version>
    <name>hello world</name>

    <!-- common definitions for this version of StreamBase -->
    <parent>
        <groupId>com.tibco.ep.sb.parent</groupId>
        <artifactId>ep-java-fragment</artifactId>
        <version>10.4.0</version>
    </parent>

</project>
```

When the maven install goal is called (mvn install), this pom.xml instructs
maven to perform the following steps :
  
1. Uses [install-product](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-product-mojo.html) to check if the 
    dependent product ( in this case com.tibco.ep.dtm:platform\_linuxx86_64 ) is
    installed.  If its not, maven will download the archive and the plugin
    will extract into $TIBCO\_EP\_HOME.
    
2. Uses the standard maven plugin [maven-compiler-plugin:compile](https://maven.apache.org/plugins/maven-compiler-plugin/compile-mojo.html)
    to compile the java sources to class files
    
3. Uses the standard maven plugin [maven-compiler-plugin:testCompile](https://maven.apache.org/plugins/maven-compiler-plugin/testCompile-mojo.html)
    to compile any java test sources to class files.
    
4. Uses [start-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/start-nodes-mojo.html) to start a test cluster.  
    Since this pom.xml has no configuration, a single node is started 
    A.${artifactId} (ie A.helloworld in this example) with a random but unused 
    discovery port.
    
5. Uses [test-java-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/test-java-fragment-mojo.html) to launch
    junit on the cluster and report the test results.  Should the test cases
    fail then no further processing occurs.
    
6. Uses [stop-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/stop-nodes-mojo.html) to stop and remove the test 
    nodes
  
7. Uses [package-java-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/package-java-fragment-mojo.html) to create
    a java fragment zip file in the build directory (by default, set to target)
    and attaches it to the build.
    
8. Uses the standard maven plugin [maven-install-plugin:install](https://maven.apache.org/plugins/maven-install-plugin/install-mojo.html)
    to install the built and tested artifacts to the local maven repository.
    
  
The java fragment application code, test cases and junit are deployed to the
test node :
  
![One node junit test](uml/one-node-junit.svg)

<a name="adding-additional-files-to-the-java-fragment"></a>

## Adding additional files to the java fragment

To add in any additional files, such as HOCON configurations or
ast.properties files, add them into a resource directory (by default, 
maven sets this to src/main/resources) and they will be included in the 
fragment zip.

<a name="java-dependencies"></a>

## Java dependencies

If the project defines any java runtime dependencies, these jars are added
into the java fragment zip file.

For example the following dependency in a pom.xml :

``` xml
    <dependencies>
        ....
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.12</version>
        </dependency>
        ....
    </dependencies>
```

Will result in the zip file containing dependencies/java/org.slf4j-slf4j-api-1.7.12.jar.
Any further transitive dependencies are also added.

<a name="code-coverage-via-cobertura"></a>

## Code coverage via cobertura

Java code coverage is supported using the [cobertura-maven-plugin](http://www.mojohaus.org/cobertura-maven-plugin/) -
this needs to be configured in both the dependencies and reporting sections of
the pom.xml :
  
``` xml
...
    <dependencies>
        ...
        <dependency>
            <groupId>net.sourceforge.cobertura</groupId>
            <artifactId>cobertura</artifactId>
            <version>2.1.1</version>
            <scope>provided</scope>
        </dependency>
        ...
    </dependencies> 
...
    <reporting>
        <plugins>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>cobertura-maven-plugin</artifactId>
                <version>2.7</version>
            </plugin>
        </plugins>
    </reporting>    
...
```

An example single node run is shown below :
  
``` shell
$ mvn cobertura:cobertura
[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building Java Fragment - hello world 3.0.0
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] >>> cobertura-maven-plugin:2.7:cobertura (default-cli) > [cobertura]test @ helloworld >>>
[INFO] 
[INFO] --- ep-maven-plugin:1.0.0:install-product (default-install-product-1) @ helloworld ---
[INFO] com.tibco.ep.dtm:platform_osxx86_64:zip:3.0.0:test product is already installed manually
[INFO] 
[INFO] --- maven-compiler-plugin:3.3:compile (default-compile) @ helloworld ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- cobertura-maven-plugin:2.7:instrument (default-cli) @ helloworld ---
[INFO] Cobertura 2.1.1 - GNU GPL License (NO WARRANTY) - See COPYRIGHT file
[INFO] Cobertura: Saved information on 2 classes.
[INFO] Cobertura: Saved information on 2 classes.
[INFO] Instrumentation was successful.
[INFO] NOT adding cobertura ser file to attached artifacts list.
[INFO] 
[INFO] --- maven-compiler-plugin:3.3:testCompile (default-testCompile) @ helloworld ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- ep-maven-plugin:1.0.0:start-nodes (default-start-nodes) @ helloworld ---
.... 
[INFO] --- ep-maven-plugin:1.0.0:test-java-fragment (default-test-java-fragment) @ helloworld ---
....
[INFO] Reading node coverage report /Users/plord/workspace/dtmexamples/java-fragments/helloworld/target/cobertura/A.helloworld.cobertura.ser
[INFO] Writing cluster coverage report /Users/plord/workspace/dtmexamples/java-fragments/helloworld/target/cobertura/cobertura.ser
[INFO] Cobertura: Loaded information on 2 classes.
[INFO] Cobertura: Loaded information on 1 classes.
[INFO] Cobertura: Saved information on 2 classes.
[INFO] 
[INFO] --- ep-maven-plugin:1.0.0:stop-nodes (default-stop-nodes-1) @ helloworld ---
....
[INFO] 
[INFO] --- cobertura-maven-plugin:2.7:cobertura (default-cli) @ helloworld ---
[INFO] Cobertura 2.1.1 - GNU GPL License (NO WARRANTY) - See COPYRIGHT file
[INFO] Cobertura: Loaded information on 2 classes.
Report time: 70ms

[INFO] Cobertura Report generation was successful.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 30.646 s
[INFO] Finished at: 2016-01-20T10:56:17+00:00
[INFO] Final Memory: 32M/555M
[INFO] ------------------------------------------------------------------------
```
  
By default a html report is generated in the site directory (by default 
target/site/cobertura/index.html).
  
The maven plugin will merge coverage reports from multiple nodes 
automatically.  For example :
  
``` shell
$ mvn cobertura:cobertura
...
[INFO] Reading node coverage report /Users/plord/workspace/dtmexamples/java-fragments/helloworld/target/cobertura/A.helloworld.cobertura.ser
[INFO] Reading node coverage report /Users/plord/workspace/dtmexamples/java-fragments/helloworld/target/cobertura/B.helloworld.cobertura.ser
[INFO] Reading node coverage report /Users/plord/workspace/dtmexamples/java-fragments/helloworld/target/cobertura/C.helloworld.cobertura.ser
[INFO] Writing cluster coverage report /Users/plord/workspace/dtmexamples/java-fragments/helloworld/target/cobertura/cobertura.ser
...
[INFO] <<< cobertura-maven-plugin:2.7:cobertura (default-cli) < [cobertura]test @ helloworld <<<
[INFO] 
[INFO] --- cobertura-maven-plugin:2.7:cobertura (default-cli) @ helloworld ---
[INFO] Cobertura 2.1.1 - GNU GPL License (NO WARRANTY) - See COPYRIGHT file
[INFO] Cobertura: Loaded information on 2 classes.
Report time: 73ms

[INFO] Cobertura Report generation was successful.
....
```
