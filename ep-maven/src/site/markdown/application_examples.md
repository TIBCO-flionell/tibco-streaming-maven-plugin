# Application archive examples

* [Codeline structure](#codeline-structure)
* [Application dependencies](#application-dependencies)
* [Feed playback](#feed-playback)
* [Feed recording](#feed-recording)
* [Admin targets](#admin-targets)
* [External programs](#external-programs)
* [Additional files in application archive](#additional-files-in-application-archive)

<a name="codeline-structure"></a>

## Codeline structure
  
The recommended application codeline structure is :
  
![Application directory structure](uml/application-structure.svg)

* {Basic build, integration test and install}

  The following pom.xml will build, unit test and install to the local maven 
  repository, an application archive.

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <!-- vim: set tabstop=4 softtabstop=0 expandtab shiftwidth=4 smarttab : -->

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.tibco.ep</groupId>
    <artifactId>simpapp</artifactId>
    <packaging>ep-application</packaging>
    <version>1.0.0</version>
    <name>hello world</name>

    <!-- common definitions for this version of StreamBase -->
    <parent>
        <groupId>com.tibco.ep.sb.parent</groupId>
        <artifactId>ep-application</artifactId>
        <version>10.4.0</version>
    </parent>

    <!-- build and test rules -->
    <build>
        <plugins>

            <plugin>
                <groupId>com.tibco.ep</groupId>
                <artifactId>ep-maven-plugin</artifactId>
                <extensions>true</extensions>
                <configuration>
                    <nodeDeployFile>${project.basedir}/src/test/configurations/node.conf</nodeDeployFile>
                    <nodes>
                        <node>A</node>
                        <node>B</node>
                        <node>C</node>
                    </nodes>
                </configuration>
            </plugin>

        </plugins>

    </build>

</project>
```

When the maven install goal is called (mvn install), this pom.xml instructs
maven to perform the following steps :

1. Uses [install-product](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-product-mojo.html) to check if the 
dependent product ( in this case com.tibco.ep.dtm:platform\_linuxx86_64 ) is
installed.  If its not, maven will download the archive and the plugin
will extract into $TIBCO\_EP_HOME.

2. Uses [package-application](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/package-application-mojo.html) to create
a application archive zip file in the build directory (by default, set to 
target) and attaches it to the build.

3. Uses [start-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/start-nodes-mojo.html) to start a test cluster.  
Since this pom.xml has no configuration, a single node is started 
A.helloworld with a random but unused discovery port.  Also, the
application archive is deployed to the cluster using the supplied node
deployment configuration file.

4. Uses [stop-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/stop-nodes-mojo.html) to stop and remove the test 
nodes

5. Uses the standard maven plugin [maven-install-plugin:install](https://maven.apache.org/plugins/maven-install-plugin/install-mojo.html)
to install the built and tested artifacts to the local maven repository.
  
Note that the application definition file is detected by its configuration
type ( **type = "com.tibco.ep.dtm.configuration.application"** ).

<a name="application-dependencies"></a>

## Application dependencies

Usually, all dependencies are included into the fragment zip files and so
don't need to be included again in the application archive.  However
sometimes including additional dependencies into the application archive 
is required - for example late bound drivers.

See below for an example :

``` xml
....
    <dependencies>

        <dependency>
            <groupId>com.tibco.ep.dtmexamples.javafragment</groupId>
            <artifactId>helloworld</artifactId>
            <version>3.0.0</version>
            <type>ep-java-fragment</type>
        </dependency>
        
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.12</version>
        </dependency>

    </dependencies>
....
```

<a name="feed-playback"></a>

## Feed playback

The administration target **playback** can be used for 
injecting tuples to streams.

``` xml
.... 
    <build>
        <plugins>

            <plugin>
                <groupId>com.tibco.ep</groupId>
                <artifactId>ep-maven-plugin</artifactId>
                <extensions>true</extensions>

                <executions>

                    <execution>
                        <id>Run playback</id>
                        <phase>integration-test</phase>
                        <goals><goal>administer-nodes</goal></goals>
                        <configuration>
                            <command>start</command>
                            <target>playback</target>
                            <arguments>
                                <engine>default-engine-for-com.tibco.ep.dtmexamples.eventflowfragment.querytable</engine>
                                <connecttime>60</connecttime>
                                <maxtuples>10000</maxtuples>
                                <maxtime>60</maxtime>
                                <prefilltuplebuffer>false</prefilltuplebuffer>
                                <tuplebuffersize>100</tuplebuffersize>
                                <asfastaspossible>false</asfastaspossible>
                                <simulationfile>${project.basedir}/src/test/configurations/test.sbfs</simulationfile>
                            </arguments>
                        </configuration>
                    </execution>

                    <execution>
                        <id>Wait for playback to complete</id>
                        <phase>integration-test</phase>
                        <goals><goal>administer-nodes</goal></goals>
                        <configuration>
                            <command>wait</command>
                            <target>playback</target>
                        </configuration>
                    </execution>

                </executions>

            </plugin>

        </plugins>
    </build>
....
```

<a name="feed-recording"></a>

## Feed recording

The administration target **record** can be used for 
recording tuples from streams.

An example of recording a playback is shown below :-

``` xml
....
            <plugin>
                <groupId>com.tibco.ep</groupId>
                <artifactId>ep-maven-plugin</artifactId>
                <extensions>true</extensions>

                <executions>

                    <execution>
                        <id>Start record</id>
                        <phase>integration-test</phase>
                        <goals><goal>administer-nodes</goal></goals>
                        <configuration>
                            <command>start</command>
                            <target>record</target>
                            <arguments>
                                <engine>default-engine-for-com.tibco.ep.dtmexamples.eventflowfragment.goldylocks</engine>
                                <name>localtest</name>
                                <connecttime>60</connecttime>
                            </arguments>
                        </configuration>
                    </execution>

                    <execution>
                        <id>Run playback</id>
                        <phase>integration-test</phase>
                        <goals><goal>administer-nodes</goal></goals>
                        <configuration>
                            <command>start</command>
                            <target>playback</target>
                            <arguments>
                                <engine>default-engine-for-com.tibco.ep.dtmexamples.eventflowfragment.goldylocks</engine>
                                <maxtime>10</maxtime>
                                <simulationfile>${project.basedir}/src/test/configurations/test.sbfs</simulationfile>
                            </arguments>
                        </configuration>
                    </execution>

                    <execution>
                        <id>Wait for playback to complete</id>
                        <phase>integration-test</phase>
                        <goals><goal>administer-nodes</goal></goals>
                        <configuration>
                            <command>wait</command>
                            <target>playback</target>
                        </configuration>
                    </execution>

                    <execution>
                        <id>Display record</id>
                        <phase>integration-test</phase>
                        <goals><goal>administer-nodes</goal></goals>
                        <configuration>
                            <command>display</command>
                            <target>record</target>
                        </configuration>
                    </execution>

                    <execution>
                        <id>Stop record</id>
                        <phase>integration-test</phase>
                        <goals><goal>administer-nodes</goal></goals>
                        <configuration>
                            <command>stop</command>
                            <target>record</target>
                        </configuration>
                    </execution>

                </executions>
....
```

<a name="admin-targets"></a>

## Admin targets

In the case of testing an application archive, the pre-integration-test
phase is used to start the nodes, the integration-test to run the test cases
and post-integration-test to stop the nodes.  This allows running 
administration commands around the test case.

One example would be to gather runtime statistics :

``` xml
....
            <plugin>
                <groupId>com.tibco.ep</groupId>
                <artifactId>ep-maven-plugin</artifactId>
                <extensions>true</extensions>

                <executions>

                    <execution>
                        <id>enable statistics</id>
                        <phase>pre-integration-test</phase>
                        <goals><goal>administer-nodes</goal></goals>
                        <configuration>
                            <command>enable</command>
                            <target>statistics</target>
                            <arguments>
                                <statistics>transaction,object</statistics>
                            </arguments>
                        </configuration>
                    </execution>

                    <execution>
                        <id>display statistics</id>
                        <phase>integration-test</phase>
                        <goals><goal>administer-nodes</goal></goals>
                        <configuration>
                            <command>display</command>
                            <target>statistics</target>
                            <arguments>
                                <statistics>transaction,object</statistics>
                            </arguments>
                        </configuration>
                    </execution>

                </executions>

            </plugin>
....
```

Resulting in :

``` shell
$ mvn install
[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building Application Archive - local test 3.0.0
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- ep-maven-plugin:1.0.0:install-product (default-install-product-1) @ localtest ---
[INFO] 
[INFO] --- ep-maven-plugin:1.0.0:package-application (default-package-application) @ localtest ---
[INFO] Building zip: /Users/plord/workspace/dtmexamples/application-archives/localtest/target/localtest-3.0.0-ep-application.zip
[INFO] 
[INFO] --- ep-maven-plugin:1.0.0:start-nodes (default-start-nodes) @ localtest ---
[INFO] [A.localtest] Running "install node"
[INFO] [A.localtest]   Installing node
[INFO] [A.localtest]       DEVELOPMENT executables
[INFO] [A.localtest]       File shared memory
[INFO] [A.localtest]       7 concurrent allocation segments
[INFO] [A.localtest]       Host name plordmac
[INFO] [A.localtest]       Container tibco/dtm
[INFO] [A.localtest]       Starting container services
[INFO] [A.localtest]       Loading node configuration
[INFO] [A.localtest]       Auditing node security
[INFO] [A.localtest]   Deploying application
[INFO] [A.localtest]       Administration port is 24488
[INFO] [A.localtest]       Service name is A.localtest
[INFO] [A.localtest]   Node installed
[INFO] [A.localtest] Finished "install node"
[INFO] [localtest] Running "start node"
[INFO] [A.localtest] Start node 
[INFO] [A.localtest] Loading node configuration 
[INFO] [A.localtest] Auditing node security 
[INFO] [A.localtest] Host name plordmac 
[INFO] [A.localtest] Administration port is 24488 
[INFO] [A.localtest] Service name is A.localtest 
[INFO] [A.localtest] Node started 
[INFO] 
[INFO] --- ep-maven-plugin:1.0.0:administer-nodes (enable statistics) @ localtest ---
[INFO] [localtest] Running "enable statistics"
[INFO] [localtest] Finished "enable statistics"
[INFO] 
[INFO] --- ep-maven-plugin:1.0.0:administer-nodes (display statistics) @ localtest ---
[INFO] [localtest] Running "display statistics"
[INFO] [A.localtest] Class Cardinality Creates Destroys 
[INFO] [A.localtest] com.kabira.platform.Metadii 2 2 0 
[INFO] [A.localtest] com.tibco.ep.dtm.configuration.hocon.parser.HoconParser 2 2 0 
[INFO] [A.localtest] com.kabira.platform.management.LifeCycle$Cleanup 0 2 2 
[INFO] [A.localtest] com.kabira.platform.management.Factory 5 5 0 
....
[INFO] 
[INFO] --- ep-maven-plugin:1.0.0:stop-nodes (default-stop-nodes-1) @ localtest ---
[INFO] [localtest] Running "stop node"
[INFO] [A.localtest] Stopping node 
[INFO] [A.localtest] Dtm::distribution stopping 
[INFO] [A.localtest] application::javafragment stopping 
[INFO] [A.localtest] Node stopped 
[INFO] [localtest] Running "remove node"
[INFO] [A.localtest] Removing node 
[INFO] [A.localtest] Shutting down container services 
[INFO] [A.localtest] Removing node directory 
[INFO] [A.localtest] Node removed 
[INFO] 
[INFO] --- maven-install-plugin:2.5.2:install (default-install) @ localtest ---
[INFO] No primary artifact to install, installing attached artifacts instead.
[INFO] Installing /Users/plord/workspace/dtmexamples/application-archives/localtest/pom.xml to /Users/plord/workspace/BUILD/repository/com/tibco/ep/dtmexamples/applicationarchive/localtest/3.0.0/localtest-3.0.0.pom
[INFO] Installing /Users/plord/workspace/dtmexamples/application-archives/localtest/target/localtest-3.0.0-ep-application.zip to /Users/plord/workspace/BUILD/repository/com/tibco/ep/dtmexamples/applicationarchive/localtest/3.0.0/localtest-3.0.0-ep-application.zip
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 24.266 s
[INFO] Finished at: 2016-01-20T15:20:49+00:00
[INFO] Final Memory: 14M/309M
[INFO] ------------------------------------------------------------------------
```

<a name="external-programs"></a>

## External programs

Test cases may need supporting programs to run - for example a database server.
This can be achieved using the [exec-maven-plugin](http://www.mojohaus.org/exec-maven-plugin/)
plugin.  Typically the external program should be started before the tests are
run and stopped after the test cases have been completed.

``` xml
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <id>start mysqld</id>
                        <phase>process-test-resources</phase>
                        <goals>
                            <goal>exec</goal>
                        </goals>
                        <configuration>
                            <executable>${projectbasedir}/../mysqlserver.ksh</executable>
                            <successCodes><code>0</code></successCodes>
                            <arguments>
                                <argument>start</argument>
                                <argument>${example.mysqlport}</argument>
                            </arguments>
                        </configuration>
                    </execution>
                    <execution>
                        <id>stop mysqld</id>
                        <phase>prepare-package</phase>
                        <goals>
                            <goal>exec</goal>
                        </goals>
                        <configuration>
                            <executable>${projectbasedir}/../mysqlserver.ksh</executable>
                            <successCodes><code>0</code></successCodes>
                            <arguments>
                                <argument>stop</argument>
                            </arguments>
                        </configuration>
                    </execution>
                    <execution>
                        <id>clean mysqld</id>
                        <phase>clean</phase>
                        <goals>
                            <goal>exec</goal>
                        </goals>
                        <configuration>
                            <executable>${projectbasedir}/../mysqlserver.ksh</executable>
                            <successCodes><code>0</code></successCodes>
                            <arguments>
                                <argument>stop</argument>
                            </arguments>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
```

<a name="additional-files-in-application-archive"></a>

## Additional files in application archive

Sometimes its useful to add additional files to the application archive - once
added these will be available at runtime in the application/shared directory.

An easy way to do this is to add the files to add to the list of maven
resources.  For example :

``` xml
    <build>
        ...
        <resources>
            <resource>
                <directory>${env.TIBCO_EP_HOME}</directory>
                <includes>
                    <include>terr/**</include>
                </includes>
            </resource>
        </resources>
        ...
    </build>
```



