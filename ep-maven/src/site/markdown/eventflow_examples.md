# EventFlow fragment examples

* [Directory structure](#directory-structure)
* [Basic build, sbunit test and install](#basic-build-sbunit-test-and-install)
* [Calling out to java](#calling-out-to-java)
* [External dependency](#external-dependency)
* [Building native libraries](#building-native-libraries)
* [Third party native libraries](#third-party-native-libraries)
* [Multiple sbapps](#multiple-sbapps)
* [Java adapters](#java-adapters)
* [EventFlow fragment referencing another EventFlow fragment](#eventflow-fragment-referencing-another-eventflow-fragment)

<a name="directory-structure"></a>

# Directory structure

The recommended EventFlow directory structure is :

![EventFlow directory structure](uml/eventflow-structure.svg)

Note that the default source directory is set by the plugin to 
src/main/eventflow.

<a name="basic-build-sbunit-test-and-install"></a>

## Basic build, sbunit test and install

The following pom.xml will build, unit test and install to the local maven 
repository, an eventflow fragment.

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <!-- vim: set tabstop=4 softtabstop=0 expandtab shiftwidth=4 smarttab : -->

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.tibco.ep</groupId>
    <artifactId>effrag</artifactId>
    <packaging>ep-eventflow-fragment</packaging>
    <version>1.0.0</version>
    <name>hello world</name>

    <!-- common definitions for this version of StreamBase -->
    <parent>
        <groupId>com.tibco.ep.sb.parent</groupId>
        <artifactId>ep-eventflow-fragment</artifactId>
        <version>10.4.0</version>
    </parent>

</project>
```

When the maven install goal is called (mvn install), this pom.xml instructs
maven to perform the following steps :

1. Uses [install-product](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-product-mojo.html) to check if the 
    dependent product ( in this case com.tibco.ep.thirdparty:tibco-sb\_linuxx86_64 ) is
    installed.  If its not, maven will download the archive and the plugin
    will extract into $TIBCO_EP_HOME.

2. Uses the standard maven plugin [maven-compiler-plugin:testCompile](https://maven.apache.org/plugins/maven-compiler-plugin/testCompile-mojo.html)
    to compile any java test sources to class files.

3. Uses [start-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/start-nodes-mojo.html) to start a test cluster.  
    Since this pom.xml has no configuration, a single node is started 
    A.${artifactId} (ie A.goldylocks in this example) with a random but unused 
    discovery port.
 
4. Uses [test-java-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/test-java-fragment-mojo.html) to launch
    sbunit on the cluster and report the test results.  Should the test cases
    fail then no further processing occurs.

5. Uses [stop-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/stop-nodes-mojo.html) to stop and remove the test 
    nodes

6. Uses [package-eventflow-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/package-eventflow-fragment-mojo.html) to create
    a EventFlow fragment zip file in the build directory (by default, set to target)
    and attaches it to the build.

7. Uses the standard maven plugin [maven-install-plugin:install](https://maven.apache.org/plugins/maven-install-plugin/install-mojo.html)
    to install the built and tested artifacts to the local maven repository.

The EventFlow fragment application code, test cases and sbunit are deployed 
to the test node :

![One node sbunit test](uml/one-node-sbunit.svg)

An example run is shown below :

``` shell
$ mvn install
[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building EventFlow Fragment - GoldyLocks 3.0.0
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- ep-maven-plugin:1.0.0:install-product (default-install-product-1) @ goldylocks ---
[INFO] com.tibco.ep.thirdparty:tibco-sb_osxx86_64:zip:7.6.0:test already installed
[INFO] 
[INFO] --- buildnumber-maven-plugin:1.4:create-timestamp (default) @ goldylocks ---
[INFO] 
[INFO] --- maven-compiler-plugin:3.3:testCompile (default-testCompile) @ goldylocks ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- ep-maven-plugin:1.0.0:start-nodes (default-start-nodes) @ goldylocks ---
[INFO] [A.goldylocks] Running "install node"
[INFO] [dtm]    Installing node
[INFO] [dtm]        DEVELOPMENT executables
[INFO] [dtm]        File shared memory
[INFO] [dtm]        7 concurrent allocation segments
[INFO] [dtm]        Host name plordmac
[INFO] [dtm]        Container tibco/dtm
[INFO] [dtm]        Starting container services
[INFO] [dtm]        Loading node configuration
[INFO] [dtm]        Auditing node security
[INFO] [dtm]        Administration port is 11072
[INFO] [dtm]        Service name is A.goldylocks
[INFO] [dtm]    Node installed
[INFO] [A.goldylocks] Finished "install node"
[INFO] [goldylocks] Running "start node"
[INFO] Node           Status                       
[INFO] [A.goldylocks] Start node                   
[INFO] [A.goldylocks] Loading node configuration   
[INFO] [A.goldylocks] Auditing node security       
[INFO] [A.goldylocks] Host name plordmac           
[INFO] [A.goldylocks] Administration port is 11072 
[INFO] [A.goldylocks] Service name is A.goldylocks 
[INFO] [A.goldylocks] Node started                 
[INFO] [goldylocks] Finished "start node"
[INFO] 
[INFO] --- ep-maven-plugin:1.0.0:test-eventflow-fragment (default-test-eventflow-fragment) @ goldylocks ---
[INFO] [dtm] INFO: Deployment tool version: [TIBCO Distributed Transactional Memory Platform 3.0.0 (build 160201)] starting at [Mon Feb 01 14:19:46 GMT 2016]
[INFO] [dtm] INFO: Node version: [TIBCO Distributed Transactional Memory Platform 3.0.0 (build 160201)]
[INFO] [dtm] INFO: Starting com.tibco.ep.buildmavenplugin.surefire.Runner on node A.goldylocks ...
[INFO] [dtm] INFO: com.tibco.ep.buildmavenplugin.surefire.Runner started on JVM com_tibco_ep_buildmavenplugin_surefire_Runner0 on node A.goldylocks.
[INFO] [A.goldylocks] 
[INFO] [A.goldylocks] -------------------------------------------------------
[INFO] [A.goldylocks] T E S T S
[INFO] [A.goldylocks] -------------------------------------------------------
[INFO] [A.goldylocks] Running com.tibco.sb.test.GoldylocksTest
[INFO] [A.goldylocks] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.502 sec
[INFO] [A.goldylocks] 
[INFO] [A.goldylocks] Results :
[INFO] [A.goldylocks] 
[INFO] [A.goldylocks] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] [A.goldylocks] 
[INFO] [dtm] INFO: JVM com_tibco_ep_buildmavenplugin_surefire_Runner0 exited with status [0]
[INFO] [goldylocks] Finished "junit"
[INFO] 
[INFO] --- ep-maven-plugin:1.0.0:stop-nodes (default-stop-nodes-1) @ goldylocks ---
[INFO] [goldylocks] Running "stop node"
[INFO] Node           Status                                                              
[INFO] [A.goldylocks] Stopping node                                                       
[INFO] [A.goldylocks] Dtm::distribution stopping                                          
[INFO] [A.goldylocks] application::com_tibco_ep_buildmavenplugin_surefire_Runner0 stopped 
[INFO] [A.goldylocks] Node stopped                                                        
[INFO] [goldylocks] Finished "stop node"
[INFO] [goldylocks] Running "remove node"
[INFO] Node           Status                           
[INFO] [A.goldylocks] Removing node                    
[INFO] [A.goldylocks] Shutting down container services 
[INFO] [A.goldylocks] Removing node directory          
[INFO] [A.goldylocks] Node removed                     
[INFO] [goldylocks] Finished "remove node"
[INFO] 
[INFO] --- ep-maven-plugin:1.0.0:package-eventflow-fragment (default-package-eventflow-fragment) @ goldylocks ---
[INFO] Building zip: /Users/plord/workspace/dtmexamples/eventflow-fragments/goldylocks/target/goldylocks-3.0.0-ep-eventflow-fragment.zip
[INFO] 
[INFO] >>> maven-source-plugin:2.4:jar (attach-sources) > generate-sources @ goldylocks >>>
[INFO] 
[INFO] --- ep-maven-plugin:1.0.0:install-product (default-install-product-1) @ goldylocks ---
[INFO] com.tibco.ep.thirdparty:tibco-sb_osxx86_64:zip:7.6.0:test already installed
[INFO] 
[INFO] --- buildnumber-maven-plugin:1.4:create-timestamp (default) @ goldylocks ---
[INFO] 
[INFO] <<< maven-source-plugin:2.4:jar (attach-sources) < generate-sources @ goldylocks <<<
[INFO] 
[INFO] --- maven-source-plugin:2.4:jar (attach-sources) @ goldylocks ---
[INFO] 
[INFO] --- maven-javadoc-plugin:2.10.3:jar (attach-javadocs) @ goldylocks ---
[INFO] Not executing Javadoc as the project is not a Java classpath-capable package
[INFO] 
[INFO] --- maven-install-plugin:2.5.2:install (default-install) @ goldylocks ---
[INFO] No primary artifact to install, installing attached artifacts instead.
[INFO] Installing /Users/plord/workspace/dtmexamples/eventflow-fragments/goldylocks/pom.xml to /Users/plord/workspace/BUILD/repository/com/tibco/ep/dtmexamples/eventflowfragment/goldylocks/3.0.0/goldylocks-3.0.0.pom
[INFO] Installing /Users/plord/workspace/dtmexamples/eventflow-fragments/goldylocks/target/goldylocks-3.0.0-ep-eventflow-fragment.zip to /Users/plord/workspace/BUILD/repository/com/tibco/ep/dtmexamples/eventflowfragment/goldylocks/3.0.0/goldylocks-3.0.0-ep-eventflow-fragment.zip
[INFO] Installing /Users/plord/workspace/dtmexamples/eventflow-fragments/goldylocks/target/goldylocks-3.0.0-sources.jar to /Users/plord/workspace/BUILD/repository/com/tibco/ep/dtmexamples/eventflowfragment/goldylocks/3.0.0/goldylocks-3.0.0-sources.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 27.410 s
[INFO] Finished at: 2016-02-01T14:20:02+00:00
[INFO] Final Memory: 38M/580M
[INFO] ------------------------------------------------------------------------
```

<a name="calling-out-to-java"></a>

## Calling out to java

Java code that needs to be included with the application can be placed in 
java source directory ( src/main/java ).  This code will be added to the 
fragment and included with any test cases.

<a name="external-dependency"></a>

## External dependency

External java (jar) dependencies can be added to the maven pom.xml - these are
then included in build, test and package steps.

For example :

``` xml
...
    <dependencies>

        <!-- compile, test and deploy dependencies -->
        <dependency>
            <groupId>org.jpos</groupId>
            <artifactId>jpos</artifactId>
            <version>1.6.8</version>
        </dependency>

        ...

    </dependencies>
...
```

<a name="building-native-libraries"></a>

## Building native libraries

Whilst a maven build could use [exec-maven-plugin](http://www.mojohaus.org/exec-maven-plugin/)
to call the native toolchain ( make, g++ etc ), the maven plugin 
[nar-maven-plugin](http://maven-nar.github.io/) is recommended - this plugin
performs the native build and creates a nar archive.  The nar archive can be
treated like any other maven dependency.

An example of creating a nar archive is shown below :

``` xml
<project>

    <groupId>com.tibco.ep.dtmexamples.eventflowfragment</groupId>
    <artifactId>cpp-lib</artifactId>
    <packaging>nar</packaging>
    <version>3.0.0</version>
    
    ....
   
    <build>

        <plugins>

            <plugin>
                <groupId>com.github.maven-nar</groupId>
                <artifactId>nar-maven-plugin</artifactId>
                <version>3.2.0</version>
                <extensions>true</extensions>
                <configuration>
                    <libraries>
                        <library>
                            <type>jni</type>
                            <narSystemPackage>com.tibco.ep.dtmexamples.eventflowfragment.sbappcallscpp</narSystemPackage>
                        </library>
                    </libraries>
                </configuration>
            </plugin>

        </plugins>

    </build>

</project>
```

A EventFlow fragment can then use this nar archive :

``` xml
...
    <!-- common definitions for this version of StreamBase -->
    <parent>
        <groupId>com.tibco.ep.sb.parent</groupId>
        <artifactId>ep-eventflow-fragment</artifactId>
        <version>10.4.0</version>
    </parent>
...
    <dependencies>

        <dependency>
            <groupId>com.tibco.ep.dtmexamples.eventflowfragment</groupId>
            <artifactId>cpp-lib</artifactId>
            <version>3.0.0</version>
            <type>nar</type>
            <classifier>amd64-Linux-gpp-jni</classifier>
        </dependency>
        <dependency>
            <groupId>com.tibco.ep.dtmexamples.eventflowfragment</groupId>
            <artifactId>cpp-lib</artifactId>
            <version>3.0.0</version>
            <type>nar</type>
            <classifier>x86_64-MacOSX-gpp-jni</classifier>
        </dependency>
        
        ....
        
    </dependencies>
...
```

Note that if the nar module is built on different platforms, the EventFlow
fragment module will add all architectures to the fragment zip.

See [aol](http://maven-nar.github.io/aol.html) for the list of 
Architecture-OperatingSystem-Linker (AOL) values references in the classifer
above.  The maven plugin will map these to the internal paths when the 
fragment is built.

<a name="third-party-native-libraries"></a>

## Third party native libraries

Its also possible to build a nar archive containing shared libraries from 
a third party - ie when the source isn't available.  The directory
structure for such a nar project is :

![Nar directory structure](uml/thirdparty-nar-structure.svg)

The pom.xml for this project will use the nar-maven-plugin and specify
shared libraries :

``` xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>thirdpartylibs</artifactId>
    <packaging>nar</packaging>
    <name>ThirdParty Shared Libraries</name>
    <version>1.0.0</version>

    <parent>
        <groupId>com.tibco.ep.sb.parent</groupId>
        <artifactId>common</artifactId>
        <version>10.4.0</version>
        <relativePath/>
    </parent>  

    <build>
        <plugins>
            <plugin>
                <groupId>com.github.maven-nar</groupId>
                <artifactId>nar-maven-plugin</artifactId>
                <extensions>true</extensions>
                <configuration>
                    <libraries>
                        <library>
                            <type>shared</type>
                        </library>
                    </libraries>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

The built artifact can then be added to a project as a normal maven dependency
and the shared libraries will be included in the runtime library path.

``` xml
    <!-- Bring in right shared library for this platform -->
    <profiles>
        <profile>
            <id>linux profile</id>
            <activation>
                <os><name>linux</name></os>
            </activation>
            <dependencies>
                <dependency>
                    <groupId>com.example</groupId>
                    <artifactId>thirdpartylibs</artifactId>
                    <version>1.0.0</version>
                    <type>nar</type>
                    <classifier>amd64-Linux-gpp-shared</classifier>
                </dependency>
            </dependencies>
        </profile>
        <profile>
            <id>mac profile</id>
            <activation>
                <os><name>mac os x</name></os>
            </activation>
            <dependencies>
                <dependency>
                    <groupId>com.example</groupId>
                    <artifactId>thirdpartylibs</artifactId>
                    <version>1.0.0</version>
                    <type>nar</type>
                    <classifier>x86_64-MacOSX-gpp-shared</classifier>
                </dependency>
            </dependencies>
        </profile>
        <profile>
            <id>windows profile</id>
            <activation>
                <property>
                    <name>path.separator</name>
                    <value>;</value>
                </property>
            </activation>
            <dependencies>
                <dependency>
                    <groupId>com.example</groupId>
                    <artifactId>thirdpartylibs</artifactId>
                    <version>1.0.0</version>
                    <type>nar</type>
                    <classifier>amd64-Windows-msvc-shared</classifier>
                </dependency>
            </dependencies>
        </profile>
    </profiles>
```

<a name="multiple-sbapps"></a>

## Multiple sbapps

There is no difference between a fragment consisting of one sbapp file
and a fragment consisting of multiple sbapp files.  Multiple sbapp and
sbint files are added to the EventFlow fragment.

<a name="java-adapters"></a>

## Java adapters

EventFlow adapters written in java can be built in a maven module with a
packaging type of jar and use a EventFlow application with sbunit to test.

An example pom.xml file for an adapter is shown below :

``` xml
...
    <groupId>com.tibco.ep.dtmexamples.eventflowfragment</groupId>
    <artifactId>adapter</artifactId>
    <packaging>jar</packaging>
    <version>3.0.0</version>
...
    <!-- common definitions for this version of StreamBase -->
    <parent>
        <groupId>com.tibco.ep.sb.parent</groupId>
        <artifactId>ep-java-fragment</artifactId>
        <version>10.4.0</version>
    </parent>
...
```

To use this adapter in a EventFlow application just set as a dependency :

```
    <dependencies>

        <dependency>
            <groupId>com.tibco.ep.dtmexamples.eventflowfragment</groupId>
            <artifactId>adapter</artifactId>
            <version>3.0.0</version>
        </dependency>

        ....
```

<a name="eventflow-fragment-referencing-another-eventflow-fragment"></a>

## EventFlow fragment referencing another EventFlow fragment

To import a EventFlow application add the fragment to the pom.xml
dependencies :-

``` xml
    <dependencies>

        <dependency>
            <groupId>com.tibco.ep.dtmexamples.eventflowfragment</groupId>
            <artifactId>sbapp-calls-adapter</artifactId>
            <version>3.0.0</version>
            <type>ep-eventflow-fragment</type>
        </dependency>

        ....
```

The maven plugin will extract this fragment dependency into the target 
directory and referenced in test cases.  Note that the extracted dependency
is not included in the built fragment zip.

