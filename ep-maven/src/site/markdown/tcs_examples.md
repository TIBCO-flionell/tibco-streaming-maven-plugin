# TIBCO Cloud Streaming fragment examples

* [Directory structure](#directory-structure)
* [Basic build and install](#basic-build-and-install)
* [Depending on block projects](#depending-on-block-projects)

<a name="directory-structure"></a>

# Directory structure

The recommended TIBCO Cloud Streaming directory structure is :

![EventFlow directory structure](uml/tcs-structure.svg)

Note that the default source directory is set by the plugin to 
src/main/eventflow.

<a name="basic-build-and-install"></a>

## Basic build and install

The following pom.xml will build and install to the local maven 
repository, a TIBCO Cloud Streaming fragment.

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <!-- vim: set tabstop=4 softtabstop=0 expandtab shiftwidth=4 smarttab : -->

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.tibco.ep</groupId>
    <artifactId>tcsfrag</artifactId>
    <packaging>ep-tcs-fragment</packaging>
    <version>1.0.0</version>
    <name>hello world</name>

    <!-- common definitions for this version of StreamBase -->
    <parent>
        <groupId>com.tibco.ep.sb.parent</groupId>
        <artifactId>ep-tcs-fragment</artifactId>
        <version>${sbrtVersion}</version>
    </parent>

</project>
```

 When the maven install goal is called (mvn install), this pom.xml instructs
maven to perform the following steps :
  
1. Uses [install-product](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-product-mojo.html) to check if the 
    dependent product ( in this case com.tibco.ep.thirdparty:tibco-sb\_linuxx86_64 ) is
    installed.  If its not, maven will download the archive and the plugin
    will extract into $TIBCO_EP_HOME.

2. Uses [package-tcs-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/package-tcs-fragment-mojo.html) to create
    a TIBCO Cloud Streaming fragment zip file in the build directory (by default, set to target)
    and attaches it to the build.

3. Uses the standard maven plugin [maven-install-plugin:install](https://maven.apache.org/plugins/maven-install-plugin/install-mojo.html)
    to install the built and tested artifacts to the local maven repository.

  An example run is shown below :

``` shell
$ mvn install
[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building TCS Fragment - GoldyLocks 3.0.0
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- ep-maven-plugin:1.0.0:install-product (default-install-product-1) @ goldylocks ---
[INFO] com.tibco.ep.thirdparty:tibco-sb_osxx86_64:zip:7.6.0:test already installed
[INFO] 
[INFO] --- ep-maven-plugin:1.3.1-SNAPSHOT:package-tcs-fragment (default-package-tcs-fragment) @ tcs ---
[INFO] Building zip: /Users/markl/examples/tcs/target/tcs-10.5.0-SNAPSHOT-ep-tcs-fragment.zip
[INFO] 
[INFO] >>> maven-source-plugin:3.0.1:jar (attach-sources) > generate-sources @ tcs >>>
[INFO] 
[INFO] --- ep-maven-plugin:1.3.1-SNAPSHOT:install-product (default-install-product-1) @ tcs ---
[INFO] com.tibco.ep.sb.rt:platform_osxx86_64:zip:10.5.0-SNAPSHOT:test already installed manually to /Users/markl/work/sb10dist
[INFO] 
[INFO] --- buildnumber-maven-plugin:1.4:create-timestamp (default) @ goldylocks ---
[INFO] 
[INFO] --- maven-source-plugin:3.0.1:jar (attach-sources) @ tcs ---
[INFO] 
[INFO] --- maven-javadoc-plugin:3.0.1:jar (attach-javadocs) @ tcs ---
[INFO] 
[INFO] --- maven-install-plugin:2.5.2:install (default-install) @ tcs ---
[INFO] Installing /Users/markl/examples/tcs/target/tcs-10.5.0-SNAPSHOT-ep-tcs-fragment.zip to /Users/markl/workspace/BUILD/repository/com/tibco/example/tcs/10.5.0-SNAPSHOT/tcs-10.5.0-SNAPSHOT.zip
[INFO] Installing /Users/markl/examples/tcs/pom.xml to /Users/markl/workspace/BUILD/repository/com/tibco/example/tcs/10.5.0-SNAPSHOT/tcs-10.5.0-SNAPSHOT.pom
[INFO] Installing /Users/markl/examples/tcs/target/tcs-10.5.0-SNAPSHOT-sources.jar to /Users/markl/workspace/BUILD/repository/com/tibco/example/tcs/10.5.0-SNAPSHOT/tcs-10.5.0-SNAPSHOT-sources.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 27.410 s
[INFO] Finished at: 2016-02-01T14:20:02+00:00
[INFO] Final Memory: 38M/580M
[INFO] ------------------------------------------------------------------------
```
    
<a name="depending-on-block-projects"></a>

## Depending on block projects

A TIBCO Cloud Streaming project needs to express dependencies on the block projects referenced from 
the project's flow diagram.  These projects' fragments will be added to the fragment.
  
For example :
  
``` xml
...
    <dependencies>

        <!-- compile, test and deploy dependencies -->
        <dependency>
            <groupId>com.tibco.example</groupId>
            <artifactId>myBlock</artifactId>
            <type>ep-eventflow-fragment</type>
            <version>1.0.0</version>
        </dependency>

        ...

    </dependencies>
```
    
