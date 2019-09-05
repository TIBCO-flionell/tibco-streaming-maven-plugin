# Goals

* [Goals](#goals)
* [Lifecycle bindings](#lifecycle-bindings)

<a name="goals"></a>

## Goals

The maven goals available are :

* [com.tibco.ep:ep-maven-plugin:package-java-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/package-java-fragment-mojo.html)

* [com.tibco.ep:ep-maven-plugin:test-java-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/test-java-fragment-mojo.html)

* [com.tibco.ep:ep-maven-plugin:package-eventflow-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/package-eventflow-fragment-mojo.html)

* [com.tibco.ep:ep-maven-plugin:test-eventflow-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/test-eventflow-fragment-mojo.html)

* [com.tibco.ep:ep-maven-plugin:package-tcs-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/package-tcs-fragment-mojo.html)

* [com.tibco.ep:ep-maven-plugin:package-liveview-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/package-liveview-fragment-mojo.html)

* [com.tibco.ep:ep-maven-plugin:test-liveview-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/test-liveview-fragment-mojo.html)

* [com.tibco.ep:ep-maven-plugin:package-application](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/package-application-mojo.html)

* [com.tibco.ep:ep-maven-plugin:start-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/start-nodes-mojo.html)

* [com.tibco.ep:ep-maven-plugin:stop-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/stop-nodes-mojo.html)

* [com.tibco.ep:ep-maven-plugin:administer-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/administer-nodes-mojo.html)

* [com.tibco.ep:ep-maven-plugin:deploy-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/deploy-fragment-mojo.html)

* [com.tibco.ep:ep-maven-plugin:install-product](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-product-mojo.html)

* [com.tibco.ep:ep-maven-plugin:unpack-nar](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/unpack-nar-mojo.html)

* [com.tibco.ep:ep-maven-plugin:unpack-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/unpack-fragment-mojo.html)

* [com.tibco.ep:ep-maven-plugin:check-testcases](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/check-testcases-mojo.html)

* [ep-maven-plugin:help](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/help-mojo.html)

<a name="lifecycle-bindings"></a>

## Lifecycle bindings

The maven plugin configures lifecycle bindings for the new packaging types as follows :
  
* Packaging *ep-java-fragment*

Default lifecycle :
  
| phase                | goals |
|----------------------|-------|
| validate             | <br>[com.tibco.ep:ep-maven-plugin:install-product](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-product-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:set-resources](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin//set-resources.html)</br>
| process-resources    | [org.apache.maven.plugins:maven-resources-plugin:resources](https://maven.apache.org/plugins/maven-resources-plugin/)
| compile              | [org.apache.maven.plugins:maven-compiler-plugin:compile](https://maven.apache.org/plugins/maven-compiler-plugin/compile-mojo.htm)
| process-test-resources | [org.apache.maven.plugins:maven-resources-plugin:testResources](https://maven.apache.org/plugins/maven-resources-plugin/)
| test-compile         | [org.apache.maven.plugins:maven-compiler-plugin:testCompile](https://maven.apache.org/plugins/maven-compiler-plugin/testCompile-mojo.html)
| test                 | <br>[com.tibco.ep:ep-maven-plugin:check-testcases](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/check-testcases-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:start-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/start-nodes-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:test-java-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/test-java-fragment-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:stop-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/stop-nodes-mojo.html)</br>
| package              | [com.tibco.ep:ep-maven-plugin:package-java-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/package-java-fragment-mojo.html)
| install              | [org.apache.maven.plugins:maven-install-plugin:install](https://maven.apache.org/plugins/maven-install-plugin/install-mojo.html)
| deploy               | [org.apache.maven.plugins:maven-deploy-plugin:deploy](https://maven.apache.org/plugins/maven-deploy-plugin/)

Clean lifecycle :
  
| phase                | goals |
|----------------------|-------|
| pre-clean            | [com.tibco.ep:ep-maven-plugin:install-product](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-product-mojo.html)
| clean                | <br>[com.tibco.ep:ep-maven-plugin:stop-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/stop-nodes-mojo.html),</br><br>[org.apache.maven.plugins:maven-clean-plugin:clean](https://maven.apache.org/plugins/maven-clean-plugin/clean-mojo.html)</br>

* Packaging **jar**

This is the same as ep-java-fragment, except a jar file is created instead of a fragment zip.
  
Default lifecycle :
  
| phase                | goals |
|----------------------|-------|
| validate             | <br>[com.tibco.ep:ep-maven-plugin:install-product](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-product-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:set-resources](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/set-resources.html)</br>
| process-resources    | [org.apache.maven.plugins:maven-resources-plugin:resources](https://maven.apache.org/plugins/maven-resources-plugin/)
| compile              | [org.apache.maven.plugins:maven-compiler-plugin:compile](https://maven.apache.org/plugins/maven-compiler-plugin/compile-mojo.htm)
| process-test-resources | [org.apache.maven.plugins:maven-resources-plugin:testResources](https://maven.apache.org/plugins/maven-resources-plugin/)
| test-compile         | [org.apache.maven.plugins:maven-compiler-plugin:testCompile](https://maven.apache.org/plugins/maven-compiler-plugin/testCompile-mojo.html)
| test                 | <br>[com.tibco.ep:ep-maven-plugin:check-testcases](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/check-testcases-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:start-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/start-nodes-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:test-java-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/test-java-fragment-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:stop-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/stop-nodes-mojo.html)</br>
| package              | [com.tibco.ep:ep-maven-plugin:package-java-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/package-java-fragment-mojo.html)
| install              | [org.apache.maven.plugins:maven-install-plugin:install](https://maven.apache.org/plugins/maven-install-plugin/install-mojo.html)
| deploy               | [org.apache.maven.plugins:maven-deploy-plugin:deploy](https://maven.apache.org/plugins/maven-deploy-plugin/)

Clean lifecycle :
  
| phase                | goals |
|----------------------|-------|
| pre-clean            | [com.tibco.ep:ep-maven-plugin:install-product](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-product-mojo.html)
| clean                | <br>[com.tibco.ep:ep-maven-plugin:stop-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/stop-nodes-mojo.html),</br><br>[org.apache.maven.plugins:maven-clean-plugin:clean](https://maven.apache.org/plugins/maven-clean-plugin/clean-mojo.html)</br>

* Packaging **ep-eventflow-fragment**

Default lifecycle :
  
| phase                | goals |
|----------------------|-------|
| validate             | <br>[com.tibco.ep:ep-maven-plugin:install-product](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-product-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:set-resources](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-set-resources.html)</br>
| process-resources    | [org.apache.maven.plugins:maven-resources-plugin:resources](https://maven.apache.org/plugins/maven-resources-plugin/)
| compile              | <br>[com.tibco.ep:ep-maven-plugin:unpack-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/unpack-fragment-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:unpack-nar](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/unpack-nar-mojo.html),</br><br>[org.apache.maven.plugins:maven-compiler-plugin:compile](https://maven.apache.org/plugins/maven-compiler-plugin/compile-mojo.htm)</br>
| process-test-resources | [org.apache.maven.plugins:maven-resources-plugin:testResources](https://maven.apache.org/plugins/maven-resources-plugin/)
| test-compile         | [org.apache.maven.plugins:maven-compiler-plugin:testCompile](https://maven.apache.org/plugins/maven-compiler-plugin/testCompile-mojo.html)
| test                 | <br>[com.tibco.ep:ep-maven-plugin:check-testcases](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/check-testcases-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:start-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/start-nodes-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:test-eventflow-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/test-eventflow-fragment-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:stop-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/stop-nodes-mojo.html)</br>
| package              | [com.tibco.ep:ep-maven-plugin:package-eventflow-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/package-eventflow-fragment-mojo.html)
| install              | [org.apache.maven.plugins:maven-install-plugin:install](https://maven.apache.org/plugins/maven-install-plugin/install-mojo.html)
| deploy               | [org.apache.maven.plugins:maven-deploy-plugin:deploy](https://maven.apache.org/plugins/maven-deploy-plugin/)

Clean lifecycle :-
  
| phase                | goals |
|----------------------|-------|
| pre-clean            | [com.tibco.ep:ep-maven-plugin:install-product](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-product-mojo.html)
| clean                | <br>[com.tibco.ep:ep-maven-plugin:stop-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/stop-nodes-mojo.html),</br><br>[org.apache.maven.plugins:maven-clean-plugin:clean](https://maven.apache.org/plugins/maven-clean-plugin/clean-mojo.html)</br>

* Packaging **ep-tcs-fragment**

Default lifecycle :
  
| phase                | goals |
|----------------------|-------|
| validate             | <br>[com.tibco.ep:ep-maven-plugin:install-product](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-product-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:set-resources](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-set-resources.html)</br>
| process-resources    | [org.apache.maven.plugins:maven-resources-plugin:resources](https://maven.apache.org/plugins/maven-resources-plugin/)
| compile              | [org.apache.maven.plugins:maven-compiler-plugin:compile](https://maven.apache.org/plugins/maven-compiler-plugin/compile-mojo.htm)
| process-test-resources | [org.apache.maven.plugins:maven-resources-plugin:testResources](https://maven.apache.org/plugins/maven-resources-plugin/)
| test-compile         | [org.apache.maven.plugins:maven-compiler-plugin:testCompile](https://maven.apache.org/plugins/maven-compiler-plugin/testCompile-mojo.html)
| package              | [com.tibco.ep:ep-maven-plugin:package-tcs-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/package-tcs-fragment-mojo.html)
| install              | [org.apache.maven.plugins:maven-install-plugin:install](https://maven.apache.org/plugins/maven-install-plugin/install-mojo.html)
| deploy               | [org.apache.maven.plugins:maven-deploy-plugin:deploy](https://maven.apache.org/plugins/maven-deploy-plugin/)

Clean lifecycle :-
  
| phase                | goals |
|----------------------|-------|
| pre-clean            | [com.tibco.ep:ep-maven-plugin:install-product](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-product-mojo.html)
| clean                | <br>[com.tibco.ep:ep-maven-plugin:stop-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/stop-nodes-mojo.html),</br><br>[org.apache.maven.plugins:maven-clean-plugin:clean](https://maven.apache.org/plugins/maven-clean-plugin/clean-mojo.html)</br>

* Packaging **ep-liveview-fragment**

Default lifecycle :
  
| phase                | goals |
|----------------------|-------|
| validate             | <br>[com.tibco.ep:ep-maven-plugin:install-product](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-product-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:set-resources](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-set-resources.html)</br>
| process-resources    | [org.apache.maven.plugins:maven-resources-plugin:resources](https://maven.apache.org/plugins/maven-resources-plugin/)
| compile              | <br>[com.tibco.ep:ep-maven-plugin:unpack-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/unpack-fragment-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:unpack-nar](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/unpack-nar-mojo.html),</br><br>[org.apache.maven.plugins:maven-compiler-plugin:compile](https://maven.apache.org/plugins/maven-compiler-plugin/compile-mojo.htm)</br>
| process-test-resources | [org.apache.maven.plugins:maven-resources-plugin:testResources](https://maven.apache.org/plugins/maven-resources-plugin/)
| test-compile         | [org.apache.maven.plugins:maven-compiler-plugin:testCompile](https://maven.apache.org/plugins/maven-compiler-plugin/testCompile-mojo.html)
| test                 | <br>[com.tibco.ep:ep-maven-plugin:check-testcases](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/check-testcases-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:start-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/start-nodes-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:test-liveview-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/test-liveview-fragment-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:stop-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/stop-nodes-mojo.html)</br>
| package              | [com.tibco.ep:ep-maven-plugin:package-liveview-fragment](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/package-liveview-fragment-mojo.html)
| install              | [org.apache.maven.plugins:maven-install-plugin:install](https://maven.apache.org/plugins/maven-install-plugin/install-mojo.html)
| deploy               | [org.apache.maven.plugins:maven-deploy-plugin:deploy](https://maven.apache.org/plugins/maven-deploy-plugin/)

Clean lifecycle :
  
| phase                | goals |
|----------------------|-------|
| pre-clean            | [com.tibco.ep:ep-maven-plugin:install-product](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-product-mojo.html)
| clean                | <br>[com.tibco.ep:ep-maven-plugin:stop-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/stop-nodes-mojo.html),</br><br>[org.apache.maven.plugins:maven-clean-plugin:clean](https://maven.apache.org/plugins/maven-clean-plugin/clean-mojo.html)</br>

* Packaging **ep-application**

Default lifecycle :
  
| phase                | goals |
|----------------------|-------|
| validate             | <br>[com.tibco.ep:ep-maven-plugin:install-product](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-product-mojo.html),</br><br>[com.tibco.ep:ep-maven-plugin:set-resources](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-set-resources.html)</br>
| process-resources    | [org.apache.maven.plugins:maven-resources-plugin:resources](https://maven.apache.org/plugins/maven-resources-plugin/)
| compile              | [org.apache.maven.plugins:maven-compiler-plugin:compile](https://maven.apache.org/plugins/maven-compiler-plugin/compile-mojo.htm)
| process-test-resources | [org.apache.maven.plugins:maven-resources-plugin:testResources](https://maven.apache.org/plugins/maven-resources-plugin/)
| test-compile         | [org.apache.maven.plugins:maven-compiler-plugin:testCompile](https://maven.apache.org/plugins/maven-compiler-plugin/testCompile-mojo.html)
| package              | [com.tibco.ep:ep-maven-plugin:package-application](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/package-application-mojo.html)
| pre-integration-test | [com.tibco.ep:ep-maven-plugin:start-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/start-nodes-mojo.html)
| post-integration-test| [com.tibco.ep:ep-maven-plugin:stop-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/stop-nodes-mojo.html)
| install              | [org.apache.maven.plugins:maven-install-plugin:install](https://maven.apache.org/plugins/maven-install-plugin/install-mojo.html)
| deploy               | [org.apache.maven.plugins:maven-deploy-plugin:deploy](https://maven.apache.org/plugins/maven-deploy-plugin/)

Clean lifecycle :
  
| phase                | goals |
|----------------------|-------|
| pre-clean            | [com.tibco.ep:ep-maven-plugin:install-product](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/install-product-mojo.html)
| clean                | <br>[com.tibco.ep:ep-maven-plugin:stop-nodes](https://tibcosoftware.github.io/tibco-streaming-maven-plugin/1.5.0/ep-maven-plugin/stop-nodes-mojo.html),</br><br>[org.apache.maven.plugins:maven-clean-plugin:clean](https://maven.apache.org/plugins/maven-clean-plugin/clean-mojo.html)</br>
