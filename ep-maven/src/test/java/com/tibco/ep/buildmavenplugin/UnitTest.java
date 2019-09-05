/*******************************************************************************
 * Copyright (C) 2018, TIBCO Software Inc.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.tibco.ep.buildmavenplugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests
 *
 */
public class UnitTest extends BetterAbstractMojoTestCase  {
    private Logger logger = LoggerFactory.getLogger(UnitTest.class);

    /**
     * rule
     */
    @Rule
    public MojoRule rule = new MojoRule();

    /**
     * resources
     */
    @Rule
    public TestResources resources = new TestResources();

    /**
     * Install product - do this first
     * 
     * @throws Exception on error
     */
    @Test
    public void testUnitTest() throws Exception  {    
        logger.info("Install Product");
        SimulatedLog simulatedLog = new SimulatedLog(false);

        File pom = new File( "target/projects", "pom.xml" );
        Assert.assertNotNull( pom );
        Assert.assertTrue( pom.exists());

        InstallProductMojo installProduct = (InstallProductMojo) lookupConfiguredMojo(pom, "install-product");
        Assert.assertNotNull( installProduct );
        installProduct.setLog(simulatedLog);     
        installProduct.execute();
        assertEquals(simulatedLog.getErrorLog(), 0, simulatedLog.getErrorLog().length());
        assertEquals(simulatedLog.getWarnLog(), 0, simulatedLog.getWarnLog().length());

        // install to a different directory
        //
        File productHome = new File(new File(".").getAbsolutePath(), "producthome");
        File markerFile;
        if (System.getProperty("os.name").toLowerCase().indexOf("mac") >=0) {
            markerFile = new File(new File(productHome, "dependency-maven-plugin-markers"), "com.tibco.ep.dtm-platform_osxx86_64-zip-1.5.0.marker");
        } else if (System.getProperty("os.name").toLowerCase().indexOf("linux") >=0 ) {
            if (System.getProperty("os.arch").equals("aarch64")) {
                markerFile = new File(new File(productHome, "dependency-maven-plugin-markers"), "com.tibco.ep.dtm-platform_linuxaarch64-zip-1.5.0.marker");
            } else {
                markerFile = new File(new File(productHome, "dependency-maven-plugin-markers"), "com.tibco.ep.dtm-platform_linuxx86_64-zip-1.5.0.marker");
            }
        } else {
            markerFile = new File(new File(productHome, "dependency-maven-plugin-markers"), "com.tibco.ep.dtm-platform_windowsx64-zip-1.5.0.marker");
        }
        installProduct = (InstallProductMojo) lookupConfiguredMojo(pom, "install-product");
        Assert.assertNotNull( installProduct );
        installProduct.productHome = productHome;
        try {
            Files.walkFileTree(productHome.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

            });
        } catch (IOException e) {
            // ignore
        }
        installProduct.setLog(simulatedLog);     
        installProduct.execute();
        assertEquals(simulatedLog.getErrorLog(), 0, simulatedLog.getErrorLog().length());
        assertTrue(markerFile.toString(), markerFile.exists());
        
        // save good md5
        //
        String goodmd5 = "good not read";
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(markerFile));
            goodmd5 = input.readLine();
        } catch (IOException e) {
        } finally {
            if (input != null) {
                input.close();
            }
        }
        
        // broken md5
        //
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(markerFile));
            output.write("junk");
        } catch (IOException e) {
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                }
            }
        }
        installProduct = (InstallProductMojo) lookupConfiguredMojo(pom, "install-product");
        Assert.assertNotNull( installProduct );
        installProduct.productHome = productHome;
        installProduct.setLog(simulatedLog);     
        installProduct.execute();
        assertEquals(simulatedLog.getErrorLog(), 0, simulatedLog.getErrorLog().length());
        assertTrue(markerFile.exists());
        String reinstallmd5 = "updated not read";
        input = null;
        try {
            input = new BufferedReader(new FileReader(markerFile));
            reinstallmd5 = input.readLine();
        } catch (IOException e) {
        } finally {
            if (input != null) {
                input.close();
            }
        }
        assertEquals(reinstallmd5, goodmd5);
        
        logger.info("java unit test");
        simulatedLog = new SimulatedLog(true);

        File testPom = new File( "target/projects/java", "test.xml" );
        Assert.assertNotNull(testPom);
        Assert.assertTrue(testPom.exists());

        File startPom = new File( "target/projects/java", "start.xml" );
        Assert.assertNotNull(startPom);
        Assert.assertTrue(startPom.exists());

        File stopPom = new File( "target/projects/java", "stop.xml" );
        Assert.assertNotNull(stopPom);
        Assert.assertTrue(stopPom.exists());

        File deployPom = new File( "target/projects/java", "deploy.xml" );
        Assert.assertNotNull(deployPom);
        Assert.assertTrue(deployPom.exists());

        try {

            // copy test file
            //
            try {
                new File("target/projects/java/target/test-classes/com/tibco/ep/buildmavenplugin").mkdirs();
                Files.copy(new File("target/test-classes/com/tibco/ep/buildmavenplugin/DummyTest.class").toPath(),
                        new File("target/projects/java/target/test-classes/com/tibco/ep/buildmavenplugin/DummyTest.class").toPath());
            } catch (FileAlreadyExistsException e) {
                // ignore
            }

            new File("target/projects/java/target/cobertura").mkdirs();

            logger.info("   Start nodes");
            StartNodesMojo startNodes = (StartNodesMojo) lookupConfiguredMojo(startPom, "start-nodes");
            Assert.assertNotNull(startNodes);
            startNodes.environment = new String[] { "BUILD_ID="+System.getenv("BUILD_ID") };
            simulatedLog.reset();
            startNodes.setLog(simulatedLog);
            startNodes.execute();
            assertEquals(simulatedLog.getErrorLog(), 0, simulatedLog.getErrorLog().length());
            assertEquals(simulatedLog.getWarnLog(), 0, simulatedLog.getWarnLog().length());
            assertTrue(simulatedLog.getInfoLog(), simulatedLog.getInfoLog().contains("Node started"));

            logger.info("   Test nodes");
            TestJavaFragmentMojo testMojo = (TestJavaFragmentMojo) lookupConfiguredMojo(testPom, "test-java-fragment");
            Assert.assertNotNull(testMojo);
            startNodes.environment = new String[] { "BUILD_ID="+System.getenv("BUILD_ID") };
            simulatedLog.reset();
            testMojo.setLog(simulatedLog);
            testMojo.optionsProperty = new String[]{ "-verbose" };
            testMojo.nodeOptionsProperty = new String[]{ "debug=true", "ignoreoptionsfile=cwxxx" };
            testMojo.execute();
            assertEquals(simulatedLog.getErrorLog(), 0, simulatedLog.getErrorLog().length());
            assertEquals(simulatedLog.getWarnLog(), 0, simulatedLog.getWarnLog().length());
            assertTrue(simulatedLog.getInfoLog(), simulatedLog.getInfoLog().contains("Failures: 0, Errors: 0, Skipped: 0"));

            logger.info("   Test nodes with junit port");
            startNodes.environment = new String[] { "BUILD_ID="+System.getenv("BUILD_ID") };
            simulatedLog.reset();
            testMojo.setLog(simulatedLog);
            testMojo.optionsProperty = new String[]{ "-verbose" };
            testMojo.nodeOptionsProperty = new String[]{ "debug=true" };
            testMojo.systemPropertyVariables = new HashMap<String, String>();
            testMojo.systemPropertyVariables.put("com.tibco.junit.results.port", "2000");
            testMojo.execute();
            
            logger.info("   Deploy nodes");
            DeployFragmentMojo deployMojo = (DeployFragmentMojo) lookupConfiguredMojo(deployPom, "deploy-fragment");
            Assert.assertNotNull(deployMojo);
            startNodes.environment = new String[] { "BUILD_ID="+System.getenv("BUILD_ID") };
            simulatedLog.reset();
            deployMojo.setLog(simulatedLog);
            deployMojo.execute();
            assertEquals(simulatedLog.getErrorLog(), 0, simulatedLog.getErrorLog().length());
            assertEquals(simulatedLog.getWarnLog(), 0, simulatedLog.getWarnLog().length());

        } finally {
            
            logger.info("   Stop nodes");
            StopNodesMojo stopNodes = (StopNodesMojo) lookupConfiguredMojo(stopPom, "stop-nodes");
            Assert.assertNotNull(stopNodes);  
            simulatedLog.reset();
            stopNodes.setLog(simulatedLog);
            stopNodes.execute();
            assertEquals(simulatedLog.getErrorLog(), 0, simulatedLog.getErrorLog().length());
            assertEquals(simulatedLog.getWarnLog(), 0, simulatedLog.getWarnLog().length());
            assertTrue(simulatedLog.getInfoLog(), simulatedLog.getInfoLog().contains("Node removed"));
             
        }

        logger.info("EventFlow unit test");
        simulatedLog = new SimulatedLog(true);

        testPom = new File( "target/projects/eventflow", "pom.xml" );
        Assert.assertNotNull(testPom);
        Assert.assertTrue(testPom.exists());

        try {

            logger.info("   Start nodes");
            StartNodesMojo startNodes = (StartNodesMojo) lookupConfiguredMojo(testPom, "start-nodes");
            Assert.assertNotNull(startNodes);
            startNodes.environment = new String[] { "BUILD_ID="+System.getenv("BUILD_ID") };
            simulatedLog.reset();
            startNodes.setLog(simulatedLog);
            startNodes.execute();
            assertEquals(simulatedLog.getErrorLog(), 0, simulatedLog.getErrorLog().length());
            assertEquals(simulatedLog.getWarnLog(), 0, simulatedLog.getWarnLog().length());
            assertTrue(simulatedLog.getInfoLog(), simulatedLog.getInfoLog().contains("Node started"));

            logger.info("   Test nodes");
            TestEventFlowFragmentMojo testMojo = (TestEventFlowFragmentMojo) lookupConfiguredMojo(testPom, "test-eventflow-fragment");
            Assert.assertNotNull(testMojo);
            testMojo.environment = new String[] { "BUILD_ID="+System.getenv("BUILD_ID") };
            simulatedLog.reset();
            testMojo.setLog(simulatedLog);
            testMojo.execute();
            assertEquals(simulatedLog.getErrorLog(), 0, simulatedLog.getErrorLog().length());
            assertEquals(simulatedLog.getWarnLog(), 0, simulatedLog.getWarnLog().length());

        } finally {

            logger.info("   Stop nodes");
            StopNodesMojo stopNodes = (StopNodesMojo) lookupConfiguredMojo(testPom, "stop-nodes");
            stopNodes.environment = new String[] { "BUILD_ID="+System.getenv("BUILD_ID") };
            Assert.assertNotNull(stopNodes);  
            simulatedLog.reset();
            stopNodes.setLog(simulatedLog);
            stopNodes.execute();
            assertEquals(simulatedLog.getErrorLog(), 0, simulatedLog.getErrorLog().length());
            assertEquals(simulatedLog.getWarnLog(), 0, simulatedLog.getWarnLog().length());
            assertTrue(simulatedLog.getInfoLog(), simulatedLog.getInfoLog().contains("Node removed"));

        }

        logger.info("LiveView unit test");
        simulatedLog = new SimulatedLog(false);

        testPom = new File( "target/projects/liveview", "pom.xml" );
        Assert.assertNotNull(testPom);
        Assert.assertTrue(testPom.exists());

        try {

            logger.info("   Start nodes");
            StartNodesMojo startNodes = (StartNodesMojo) lookupConfiguredMojo(testPom, "start-nodes");
            Assert.assertNotNull(startNodes);
            startNodes.environment = new String[] { "BUILD_ID="+System.getenv("BUILD_ID") };
            simulatedLog.reset();
            startNodes.setLog(simulatedLog);
            startNodes.execute();
            assertEquals(simulatedLog.getErrorLog(), 0, simulatedLog.getErrorLog().length());
            assertEquals(simulatedLog.getWarnLog(), 0, simulatedLog.getWarnLog().length());
            assertTrue(simulatedLog.getInfoLog(), simulatedLog.getInfoLog().contains("Node started"));

            logger.info("   Test nodes");
            TestLiveViewFragmentMojo testMojo = (TestLiveViewFragmentMojo) lookupConfiguredMojo(testPom, "test-liveview-fragment");
            Assert.assertNotNull(testMojo);
            testMojo.environment = new String[] { "BUILD_ID="+System.getenv("BUILD_ID") };
            simulatedLog.reset();
            testMojo.setLog(simulatedLog);
            testMojo.execute();
            assertEquals(simulatedLog.getErrorLog(), 0, simulatedLog.getErrorLog().length());
            assertEquals(simulatedLog.getWarnLog(), 0, simulatedLog.getWarnLog().length());

        } finally {

            logger.info("   Stop nodes");
            StopNodesMojo stopNodes = (StopNodesMojo) lookupConfiguredMojo(testPom, "stop-nodes");
            stopNodes.environment = new String[] { "BUILD_ID="+System.getenv("BUILD_ID") };
            Assert.assertNotNull(stopNodes);  
            simulatedLog.reset();
            stopNodes.setLog(simulatedLog);
            stopNodes.execute();
            assertEquals(simulatedLog.getErrorLog(), 0, simulatedLog.getErrorLog().length());
            assertEquals(simulatedLog.getWarnLog(), 0, simulatedLog.getWarnLog().length());
            assertTrue(simulatedLog.getInfoLog(), simulatedLog.getInfoLog().contains("Node removed"));

        }

        logger.info("application test");
        simulatedLog = new SimulatedLog(false);

        testPom = new File( "target/projects/application", "pom.xml" );
        Assert.assertNotNull(testPom);
        Assert.assertTrue(testPom.exists());

        deployPom = new File( "target/projects/application", "deploy.xml" );
        Assert.assertNotNull(testPom);
        Assert.assertTrue(testPom.exists());
        
//        try {

            logger.info("   Start nodes");
            StartNodesMojo startNodes = (StartNodesMojo) lookupConfiguredMojo(deployPom, "start-nodes");
            Assert.assertNotNull(startNodes);  
            startNodes.environment = new String[] { "BUILD_ID="+System.getenv("BUILD_ID") };
            simulatedLog.reset();
            startNodes.setLog(simulatedLog);
            startNodes.execute();
            assertEquals(simulatedLog.getErrorLog(), 0, simulatedLog.getErrorLog().length());
            assertEquals(simulatedLog.getWarnLog(), 0, simulatedLog.getWarnLog().length());
            assertTrue(simulatedLog.getInfoLog(), simulatedLog.getInfoLog().contains("Node started"));

//        } finally {

            logger.info("   Stop nodes");
            StopNodesMojo stopNodes = (StopNodesMojo) lookupConfiguredMojo(testPom, "stop-nodes");
            Assert.assertNotNull(stopNodes);  
            stopNodes.environment = new String[] { "BUILD_ID="+System.getenv("BUILD_ID") };
            simulatedLog.reset();
            stopNodes.setLog(simulatedLog);
            stopNodes.execute();
            assertEquals(simulatedLog.getErrorLog(), 0, simulatedLog.getErrorLog().length());
            assertEquals(simulatedLog.getWarnLog(), 0, simulatedLog.getWarnLog().length());
            assertTrue(simulatedLog.getInfoLog(), simulatedLog.getInfoLog().contains("Node removed"));

//        }
    }

    /**
     * Test correct exit when non-daemon threads are still running.
     * 
     * @throws Exception on error
     */
    @Test
    public void testExitWhenThreadsRunning() throws Exception  {    

        new Thread() {
            public synchronized void run() {
                try {
                    wait();
                } catch (final InterruptedException e) {
                    return;
                }
            }
        }.start();
    }
}
