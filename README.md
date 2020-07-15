# Java Agent for OpenTracing

> Automatically instruments 3rd-party libraries in Java applications

[![Build Status](https://img.shields.io/circleci/build/gh/opentracing-contrib/java-specialagent/master)][circleci]
[![Coverage Status](https://coveralls.io/repos/github/opentracing-contrib/java-specialagent/badge.svg?branch=master)](https://coveralls.io/github/opentracing-contrib/java-specialagent?branch=master)
[![Javadocs](https://www.javadoc.io/badge/io.opentracing.contrib.specialagent/opentracing-specialagent.svg)](https://www.javadoc.io/doc/io.opentracing.contrib.specialagent/opentracing-specialagent)
[![Released Version](https://img.shields.io/maven-central/v/io.opentracing.contrib.specialagent/specialagent.svg)](https://mvnrepository.com/artifact/io.opentracing.contrib.specialagent/opentracing-specialagent)

## What is SpecialAgent?

The <ins>SpecialAgent</ins> is software that attaches to Java applications, and automatically instruments 3rd-party libraries within. The <ins>SpecialAgent</ins> uses the OpenTracing API for <ins>[Integrations](#41-integrations)</ins> that instrument 3rd-party libraries, as well as <ins>[Trace Exporters](#42-trace-exporters)</ins> that export trace data to OpenTracing <ins>[Tracer](#61-tracer)</ins> vendors. The architecture of <ins>SpecialAgent</ins> was specifically designed to include contributions from the community, whereby its platform automates the installation of OpenTracing <ins>[Integrations](#63-integration)</ins> written by individual contributors. In addition to <ins>[Integrations](#63-integration)</ins>, the <ins>SpecialAgent</ins> also supports <ins>[Trace Exporters](#62-trace-exporter)</ins>, which connect an instrumented runtime to OpenTracing-compliant tracer vendors, such as [LightStep][lightstep], [Wavefront][wavefront], or [Jaeger][jaeger]. Both the <ins>[Integrations](#63-integration)</ins> and the <ins>[Trace Exporters](#62-trace-exporter)</ins> are decoupled from <ins>SpecialAgent</ins> -- i.e. neither need to know about <ins>SpecialAgent</ins>. At its core, the <ins>SpecialAgent</ins> is itself nothing more than an engine that abstracts the functionality for the automatic installation of <ins>[Integrations](#63-integration)</ins>, and their connection to <ins>[Trace Exporters](#62-trace-exporter)</ins>. A benefit of this approach is that the <ins>SpecialAgent</ins> intrinsically embodies and encourages community involvement.

Both the <ins>[Integrations](#41-integrations)</ins> and the <ins>[Trace Exporters](#42-trace-exporters)</ins> are open-source, and are developed and supported by the OpenTracing community.

The <ins>SpecialAgent</ins> supports Oracle Java and OpenJDK.

## Table of Contents

<samp>&nbsp;&nbsp;</samp>1 [Introduction](#1-introduction)<br>
<samp>&nbsp;&nbsp;</samp>2 [Quick Start](#2-quick-start)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>2.1 [Installation](#21-installation)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>2.1.1 [In Application](#211-in-application)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>2.1.1.1 [Stable](#2111-stable)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>2.1.1.2 [Development](#2112-development)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>2.1.2 [For Development](#212-for-development)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>2.1.2.1 <ins>[Integrations](#2121-integrations)</ins><br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>2.1.2.1.1 <ins>[Uncoupled Integrations](#21211-uncoupled-integrations)</ins><br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>2.1.2.1.2 <ins>[Coupled Integrations](#21212-coupled-integrations)</ins><br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>2.1.2.1.3 [Development of <ins>Integration Rules</ins>](#21213-development-of-integration-rules)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>2.1.2.2 <ins>[Trace Exporters](#2122-trace-exporters)</ins><br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>2.1.2.2.1 <ins>[Short Name](#21221-short-name)</ins><br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>2.2 [Usage](#22-usage)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>2.2.1 <ins>[Static Attach](#221-static-attach)</ins><br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>2.2.2 <ins>[Dynamic Attach](#222-dynamic-attach)</ins><br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>2.2.3 <ins>[Static Deferred Attach](#223-static-deferred-attach)</ins><br>
<samp>&nbsp;&nbsp;</samp>3 [Configuration](#3-configuration)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>3.1 [Overview](#31-overview)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>3.2 [Properties](#32-properties)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>3.2.1 [Logging](#321-logging)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>3.2.2 [Integration](#322-integration)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>3.2.3 [General](#323-general)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>3.3 [Selecting the <ins>Trace Exporter</ins>](#33-selecting-the-trace-exporter)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>3.4 [Disabling <ins>Integration Rules</ins>](#34-disabling-integration-rules)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>3.4.1 [Disabling All <ins>Integration Rules</ins>](#341-disabling-all-integration-rules)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>3.4.2 [Disabling (or enabling) One <ins>Integration Rule</ins>](#342-disabling-or-enabling-one-integration-rule)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</samp>3.4.3 [Disabling `AgentRule`s of an <ins>Integration Rule</ins>](#343-disabling-agentrules-of-an-integration-rule)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>3.5 [Disabling <ins>Trace Exporters</ins>](#35-disabling-trace-exporters)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>3.6 [Including custom <ins>Integration Rules</ins>](#36-including-custom-integration-rules)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>3.7 <ins>[Rewritable Tracer](#37-rewritable-tracer)</ins><br>
<samp>&nbsp;&nbsp;</samp>4 [Supported <ins>Integrations</ins> and <ins>Trace Exporters</ins>](#4-supported-integrations-and-trace-exporters)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>4.1 <ins>[Integrations](#41-integrations)</ins><br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>4.2 <ins>[Trace Exporters](#42-trace-exporters)</ins><br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>4.3 [Libraries instrumented via other <ins>Integrations</ins>](#43-libraries-instrumented-via-other-integrations)<br>
<samp>&nbsp;&nbsp;</samp>5 [Objectives](#5-objectives)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>5.1 [Goals](#51-goals)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>5.2 [Non-Goals](#52-non-goals)<br>
<samp>&nbsp;&nbsp;</samp>6 [Definitions](#6-definitions)<br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>6.1 <ins>[Tracer](#61-tracer)</ins><br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>6.2 <ins>[Trace Exporter](#62-trace-exporter)</ins><br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>6.3 <ins>[Integration](#63-integration)</ins><br>
<samp>&nbsp;&nbsp;&nbsp;&nbsp;</samp>6.4 <ins>[Integration Rule](#64-integration-rule)</ins><br>
<samp>&nbsp;&nbsp;</samp>7 [Credits](#7-credits)<br>
<samp>&nbsp;&nbsp;</samp>8 [Contributing](#8-contributing)<br>
<samp>&nbsp;&nbsp;</samp>9 [License](#9-license)

## 1 Introduction

This file contains the operational instructions for the use and development of <ins>SpecialAgent</ins>.

## 2 Quick Start

The <ins>SpecialAgent</ins> is a Java Agent that attaches to an application (either [statically or dynamically](#22-usage)). Once attached, the <ins>SpecialAgent</ins> it loads its bundled <ins>[Integrations](#63-integration)</ins>, which are all enabled by default.

The <ins>SpecialAgent</ins> is stable -- any exception that occurs during attachment of <ins>SpecialAgent</ins> will not adversely affect the stability of the target application. It is, however, important to note that <ins>SpecialAgent</ins> bundles <ins>[Integrations](#63-integration)</ins> that are developed by 3rd parties and individual contributors. We strive to assert the stability of <ins>SpecialAgent</ins> with rigorous [integration tests][circleci], yet it is still possible that the code in a bundled <ins>[Integration](#63-integration)</ins> may result in an exception that is not properly handled, which could potentially destabilize a target application.

### 2.1 Installation

The Maven build of the <ins>SpecialAgent</ins> project generates 2 artifacts: **main** and **test**. These artifacts can be obtained by downloading directly from [Maven's Central Repository](https://repo1.maven.org/maven2/io/opentracing/contrib/specialagent/opentracing-specialagent/1.7.4/), or by cloning this repository and following the [Development Instructions](#212-for-development).

#### 2.1.1 In Application

The <ins>SpecialAgent</ins> is contained in a single JAR file. This JAR file is the **main** artifact built by Maven, and bundles the <ins>[Integrations](#63-integration)</ins> from [opentracing-contrib][opentracing-contrib] for which <ins>[Integration Rules](#64-integration-rule)</ins> have been implemented.

To use the <ins>SpecialAgent</ins> on an application, please download the [stable](#2111-stable) or [development](#2112-development) **main** artifact.

The artifact JAR can be provided to an application with the `-javaagent:${SPECIAL_AGENT_JAR}` vm argument for <ins>[Static Attach](#221-static-attach)</ins> and <ins>[Static Deferred Attach](#223-static-deferred-attach)</ins>. The artifact JAR can also be executed in standalone fashion, which requires an argument to be passed for the PID of a target process to which <ins>SpecialAgent</ins> is to <ins>[dynamically attach](#222-dynamic-attach)</ins>. Please refer to [Usage](#22-usage) section for usage instructions.

##### 2.1.1.1 Stable

The latest stable release is: [1.7.4][main-release]

```bash
wget -O opentracing-specialagent-1.7.4.jar "https://repo1.maven.org/maven2/io/opentracing/contrib/specialagent/opentracing-specialagent/1.7.4/opentracing-specialagent-1.7.4.jar"
```

##### 2.1.1.2 Development

The latest development release is: [1.7.5-SNAPSHOT][main-snapshot]

```bash
wget -O opentracing-specialagent-1.7.5-SNAPSHOT.jar "https://oss.sonatype.org/service/local/artifact/maven/redirect?r=snapshots&g=io.opentracing.contrib.specialagent&a=opentracing-specialagent&v=LATEST"
```

**Note**: Sometimes the web service call (in the line above) to retrieve the latest SNAPSHOT build fails to deliver the correct download. In order to work around this issue, please consider using the following command (for Linux and Mac OS):

```bash
wget -O opentracing-specialagent-1.7.5-SNAPSHOT.jar $(curl -s https://oss.sonatype.org/content/repositories/snapshots/io/opentracing/contrib/specialagent/opentracing-specialagent/1.7.5-SNAPSHOT/ | grep '".*\d\.jar"' | tail -1 | awk -F\" '{print $2}')
```

#### 2.1.2 For Development

The <ins>SpecialAgent</ins> is built in 2 passes utilizing different profiles:

1. The `default` profile is used for development of <ins>[Integration Rules](#64-integration-rule)</ins>. It builds and runs tests for each rule, but _does not bundle the rules_ into the main JAR (i.e. `opentracing-specialagent-1.7.5-SNAPSHOT.jar`).

   To run this profile:
   ```bash
   mvn clean install
   ```

   _**Note**: If you skip tests, the `assemble` profile will display an error stating that tests have not been run. See [Convenient One-Liners](#convenient-one-liners) for quick ways to build and package <ins>SpecialAgent</ins>_.

1. The `assemble` profile is used to bundle the <ins>[Integrations](#64-integration-rule)</ins> into the main JAR (i.e. `opentracing-specialagent-1.7.5-SNAPSHOT.jar`). It builds each rule, but _does not run tests._ Once the build with the `assemble` profile is finished, the main JAR (i.e. `opentracing-specialagent-1.7.5-SNAPSHOT.jar`) will contain the built rules inside it.

   _**Note**: If you do not run this step, the `opentracing-specialagent-1.7.5-SNAPSHOT.jar` from the previous step will not contain any <ins>[Integrations](#63-integration)</ins>!_

   _**Note**: It is important to **not** run Maven's `clean` lifecycle when executing the `assemble` profile, otherwise the <ins>[Integrations](#63-integration)</ins> built in with the `default` profile will be cleared._

   To run this profile:
   ```bash
   mvn -Dassemble install
   ```

* For a one-line build command to build <ins>SpecialAgent</ins>, its rules, run all tests, and create the `assemble` package:

  ```bash
  mvn clean install && mvn -Dassemble install
  ```

##### Convenient One-Liners

1. Skipping tests when building <ins>SpecialAgent</ins>.

   ```bash
   mvn -DskipTests clean install
   ```

1. Skipping compatibility tests when building <ins>SpecialAgent</ins> rules.

   ```bash
   mvn -DskipCompatibilityTests clean install
   ```

1. Packaging <ins>SpecialAgent</ins> with rules that skipped test execution.

   ```bash
   mvn -Dassemble -DignoreMissingTestManifest install
   ```

##### 2.1.2.1 <ins>[Integrations](#63-integration)</ins>

The <ins>SpecialAgent</ins> supports two kinds of <ins>[Integrations](#63-integration)</ins>:

###### 2.1.2.1.1 <ins>Uncoupled [Integrations](#63-integration)</ins>

<ins>Uncoupled [Integrations](#63-integration)</ins> are those that can be used _without_ <ins>SpecialAgent</ins>. These <ins>[Integrations](#63-integration)</ins> are not coupled to <ins>SpecialAgent</ins>, and can be used via **manual integration** in an application.

<ins>Uncoupled [Integrations](#63-integration)</ins> are implemented in [opentracing-contrib][opentracing-contrib], and do not know about <ins>SpecialAgent</ins>.

To support <ins>Uncoupled [Integrations](#63-integration)</ins>, <ins>SpecialAgent</ins> requires the implementation of an <ins>[Integration Rule](#64-integration-rule)</ins> that bridges the <ins>Uncoupled [Integrations](#63-integration)</ins> to <ins>SpecialAgent</ins>'s auto-instrumentation mechanism.

The implementation of <ins>[Integrations](#63-integration)</ins> as <ins>uncoupled</ins> is preferred, as this pattern allows users to instrument their applications manually, if so desired. However, not all 3rd-party libraries can be instrumented to allow manual integration, leaving the alternative pattern: <ins>Coupled [Integrations](#63-integration)</ins>.

###### 2.1.2.1.2 <ins>Coupled [Integrations](#63-integration)</ins>

<ins>Coupled [Integrations](#63-integration)</ins> are those that _can only be used with_ <ins>SpecialAgent</ins>. These <ins>[Integrations](#63-integration)</ins> are coupled to <ins>SpecialAgent</ins>, and can only be used via **automatic installation** in an application.

<ins>Coupled [Integrations](#63-integration)</ins> are effectively <ins>[Integration Rules](#64-integration-rule)</ins> that implement the full scope of the instrumentation of the 3rd-party library, and directly bridge this integration into the <ins>SpecialAgent</ins>'s auto-instrumentation mechanism.

The implementation of <ins>[Integrations](#63-integration)</ins> as <ins>coupled</ins> is _discouraged_, as this pattern prohibits users from instrumenting their applications manually, if so desired. However, not all 3rd-party libraries can be instrumented to allow manual integration, leaving <ins>Coupled [Integrations](#63-integration)</ins> as the only option.

###### 2.1.2.1.3 Development of <ins>[Integration Rules](#64-integration-rule)</ins>

For development of <ins>[Integration Rules](#64-integration-rule)</ins>, import the `opentracing-specialagent-api` and `test-jar` of the `opentracing-specialagent`.

```xml
<properties>
  <special-agent-version>1.7.4</special-agent-version> <!-- 1.7.5-SNAPSHOT -->
</properties>
...
<dependency>
  <!-- Allows you to write Integration Rules for Special Agent -->
  <groupId>io.opentracing.contrib.specialagent</groupId>
  <artifactId>opentracing-specialagent-api</artifactId>
  <version>${special-agent-version}</version>
</dependency>
<dependency>
  <!-- Allows the Integration Rules to use OpenTracing API -->
  <groupId>io.opentracing.contrib.specialagent</groupId>
  <artifactId>opentracing-adapter</artifactId>
  <version>${special-agent-version}</version>
</dependency>
<dependency>
  <!-- Allows the Integration Rules be tested with SpecialAgent's `AgentRunner` -->
  <groupId>io.opentracing.contrib.specialagent</groupId>
  <artifactId>opentracing-specialagent</artifactId>
  <version>${special-agent-version}</version>
  <type>test-jar</type>
  <scope>test</scope>
</dependency>
<dependency>
  <!-- Needed for JUnit tests from IDE -->
  <groupId>org.apache.maven</groupId>
  <artifactId>maven-model</artifactId>
</dependency>
```

The `test-jar` is the test artifact that contains the `AgentRunner` class, which is a JUnit runner provided for testing of the ByteBuddy auto-instrumentation rules. This JAR does not contain <ins>[Integration Rules](#64-integration-rule)</ins> themselves, and is only intended to be applied to the test phase of the build lifecycle of a single <ins>[Integration Rule](#64-integration-rule)</ins> implementation.

For direction with the development of <ins>[Integration Rules](#64-integration-rule)</ins>, please refer to the [`opentracing-specialagent-api`][api] module.

##### 2.1.2.2 <ins>[Trace Exporters](#62-trace-exporter)</ins>

<ins>[Trace Exporters](#62-trace-exporter)</ins> integrate with the <ins>SpecialAgent</ins> via the [OpenTracing TracerResolver](https://github.com/opentracing-contrib/java-tracerresolver), which connects the <ins>SpecialAgent</ins> to a <ins>[Tracer](#61-tracer)</ins>.

<ins>[Trace Exporters](#62-trace-exporter)</ins> integrate to the <ins>SpecialAgent</ins> via the [SPI mechanism](https://docs.oracle.com/javase/tutorial/ext/basics/spi.html) defined in the [TracerResolver](https://github.com/opentracing-contrib/java-tracerresolver), and are therefore not coupled to the <ins>SpecialAgent</ins>.

<ins>[Trace Exporters](#62-trace-exporter)</ins> must be provided as "fat JARs" that contain the full set of all classes necessary for operation.

If the <ins>[Trace Exporter](#62-trace-exporter)</ins> JAR imports any `io.opentracing:opentracing-*` dependencies, the `io.opentracing.contrib:opentracing-tracerresolver`, or any other OpenTracing dependencies that are guaranteed to be provided by <ins>SpecialAgent</ins>, then these dependencies **MUST BE** excluded from the JAR, as well as from the dependency spec.

<ins>[Trace Exporters](#62-trace-exporter)</ins> are integrated with the <ins>SpecialAgent</ins> by specifying a dependency in the `<isolatedDependencies>` configuration element of the `specialagent-maven-plugin` in the [root POM][specialagent-pom]. For instance, the dependency for the [Jaeger Trace Exporter](https://github.com/opentracing-contrib/java-opentracing-jaeger-bundle) is:

```xml
<isolatedDependencies>
...
  <dependency>
    <groupId>io.opentracing.contrib</groupId>
    <artifactId>jaeger-client-bundle</artifactId>
  </dependency>
...
</isolatedDependencies>
```

###### 2.1.2.2.1 <ins>Short Name</ins>

Each <ins>[Trace Exporter](#62-trace-exporter)</ins> integrated with the <ins>SpecialAgent</ins> must define a <ins>Short Name</ins>, which is a string that is used to reference the plugin with the `-Dsa.exporter=${SHORT_NAME}` system property. To provide a <ins>Short Name</ins> for the <ins>[Trace Exporter](#62-trace-exporter)</ins>, you must define a Maven property in the [root POM][specialagent-pom] with the name matching the `artifactId` of the <ins>[Trace Exporter](#62-trace-exporter)</ins> module. For instance, the <ins>Short Name</ins> for the [Jaeger Trace Exporter](https://github.com/opentracing-contrib/java-opentracing-jaeger-bundle) is defined as:

```xml
<properties>
...
  <jaeger-client-bundle>jaeger</jaeger-client-bundle>
...
</properties>
```

### 2.2 Usage

The <ins>SpecialAgent</ins> is used by attaching to a target application. Once attached, the <ins>SpecialAgent</ins> relies on [Java’s Instrumentation mechanism](https://docs.oracle.com/javase/7/docs/api/java/lang/instrument/package-summary.html) to transform the behavior of the application.

<ins>SpecialAgent</ins> supports the following attach modes:

| Attach Mode | Number of Required<br>Commands to Attach | Plugin and Rule<br>Initialization Timeline |
|:-|:-:|:-:|
| <ins>[Static Attach](#221-static-attach)</ins><br>&nbsp; | 1 (sync)<br>&nbsp; | Before app start<br><sup>(any application)</sup> |
| <ins>[Dynamic Attach](#222-dynamic-attach)</ins><br>&nbsp; | 2 (async)<br>&nbsp; | After app start<br><sup>(any application)</sup> |
| <ins>[Static Deferred Attach](#223-static-deferred-attach)</ins><br>&nbsp; | 1 (sync)<br>&nbsp; | After app start<br><sup>([some applications](#static-deferred-attach-is-currently-supported-for))</sup> |

#### 2.2.1 <ins>Static Attach</ins>

With <ins>[Static Attach](#221-static-attach)</ins>, the application is executed with the `-javaagent` argument, and the agent initialization occurs before the application is started. This mode requires 1 command from the command line.

Statically attaching to a Java application involves the use of the `-javaagent` vm argument at the time of startup of the target Java application. The following command can be used as an example:

```bash
java -javaagent:opentracing-specialagent-1.7.4.jar -jar MyApp.jar
```

This command statically attaches <ins>SpecialAgent</ins> to the application in `MyApp.jar`.

#### 2.2.2 <ins>Dynamic Attach</ins>

With <ins>[Dynamic Attach](#222-dynamic-attach)</ins>, the application is allowed to start first, afterwhich an agent VM is dynamically attached to the application's PID. This mode requires 2 commands from the command line: the first for the application, and the second for the agent VM.

Dynamically attaching to a Java application involves the use of a running application’s PID, after the application’s startup. The following commands can be used as an example:

1. To obtain the `PID` of the target application:
    ```bash
    jps
    ```

1. To attach to the target `PID`:
   * For jdk1.8
     ```bash
     java -Xbootclasspath/a:$JAVA_HOME/lib/tools.jar -jar opentracing-specialagent-1.7.4.jar ${PID}
     ```

   * For jdk9+
     ```bash
     java -jar opentracing-specialagent-1.7.4.jar ${PID}
     ```

**Note:** Properties that are provided in the command to dynamically attach will be absorbed by the target application. This applies to properties specific to <ins>SpecialAgent</ins>, such as `-Dsa.log.level=FINER`, as well as other properties such as `-Djava.util.logging.config.file=out.log`.

**Troubleshooting:** If you encounter an exception stating `Unable to open socket file`, make sure the attaching VM is executed with the same permissions as the target VM.

#### 2.2.3 <ins>Static Deferred Attach</ins>

With <ins>Static Deferred Attach</ins>, the application is executed with the `-javaagent` argument, but the agent initialization is deferred until the application is started. This mode requires 1 command from the command line, and is designed specifically for runtimes that have complex initialization lifecycles that may result in extraneously lengthy startup times when attached with <ins>[Static Attach](#221-static-attach)</ins>.

##### <ins>Static Deferred Attach</ins> is currently supported for:

1. Spring WebMVC (1.0 to LATEST).
1. Spring Boot (1.0.0.RELEASE to LATEST).

If the above supported application environment is detected, <ins>Static Deferred Attach</ins> is automatically activated.

**To deactivate** <ins>Static Deferred Attach</ins>, specify the following system property on the command line:

```bash
-Dsa.init.defer=false
```

The following command can be used as an example:

```bash
java -javaagent:opentracing-specialagent-1.7.4.jar -Dsa.init.defer=false -jar MySpringBootApp.jar
```

## 3 Configuration

### 3.1 Overview

The <ins>SpecialAgent</ins> exposes a simple pattern for configuration of <ins>SpecialAgent</ins>, the <ins>[Integrations](#63-integration)</ins>, as well as <ins>[Trace Exporters](#62-trace-exporter)</ins>. The configuration pattern is based on system properties, which can be defined on the command-line, in a properties file, or in [@AgentRunner.Config][agentrunner-config] for JUnit tests:

**Configuration Layers**

1. Properties passed on the command-line via `-D${PROPERTY}=...` override same-named properties defined in the subsequent layers.

1. The [@AgentRunner.Config][agentrunner-config] annotation allows one to define log level and re/transformation event logging settings. Properties defined in the `@Config` annotation override same-named properties defined in the subsequent layers.

1. The `-Dsa.config=${PROPERTIES_FILE}` command-line argument can be specified for <ins>SpecialAgent</ins> to load property names from a `${PROPERTIES_FILE}`. Properties defined in the `${PROPERTIES_FILE}` override same-named properties defined in the subsequent layer.

1. The <ins>SpecialAgent</ins> has a `default.properties` file that defines default values for properties that need to be defined.

### 3.2 Properties

The following properties are supported by all <ins>[Integration Rules](#64-integration-rule)</ins>:

#### 3.2.1 Logging

* `-Dsa.log.level`

  Set the logging level for <ins>SpecialAgent</ins>. Acceptable values are: `SEVERE`, `WARNING`, `INFO`, `CONFIG`, `FINE`, `FINER`, or `FINEST`, or any numerical log level value is accepted also. The default logging level is set to `WARNING`.

* `-Dsa.log.events`

  Set the re/transformation events to be logged: `DISCOVERY`, `IGNORED`, `TRANSFORMATION`, `ERROR`, `COMPLETE`. The property accepts a comma-delimited list of event names. By default, the `ERROR` event is logged (only when run with `AgentRunner`).

* `-Dsa.log.file`

  Set the logging output file for <ins>SpecialAgent</ins>.

#### 3.2.2 Integration

1. <ins>Verbose mode</ins>

   &nbsp;&nbsp;&nbsp;&nbsp;`-Dsa.integration.*.verbose`<br>
   &nbsp;&nbsp;&nbsp;&nbsp;`-Dsa.integration.${INTEGRATION_NAME_PATTERN}.verbose`

   Sets verbose mode for all plugins (i.e. `*`) or one plugin (i.e. `${INTEGRATION_NAME_PATTERN}`). This property can also be set in an `AgentRunner` JUnit test with the `@AgentRunner.Config(verbose=true)` for all tests in a JUnit class, or `@AgentRunner.TestConfig(verbose=true)` for an individual JUnit test method.

   The [Java Concurrent API plugin](https://github.com/opentracing-contrib/java-concurrent) supports verbose mode, which is disabled by default. To enable, set `sa.integration.concurrent.verbose=true`. In non-verbose mode, parent span context is propagating to task execution (if a parent span context exists). In verbose mode, a parent span is always created upon task submission to the executor, and a child span is created when the task is started.

#### 3.2.3 General

1. <ins>Skip fingerprint verification:</ins>

   &nbsp;&nbsp;&nbsp;&nbsp;`-Dsa.fingerprint.skip`

   Tells the <ins>SpecialAgent</ins> to skip the fingerprint verification when linking <ins>[Integrations](#63-integration)</ins> into class loaders. This option allows one to work around an unexpected fingerprint verification failure, which can happen in complex runtimes that do not contain all class definitions on the class path. It must be noted, however, that if the fingerprint verification is disabled, the <ins>SpecialAgent</ins> will indiscriminately install all plugins regardless of library version compatibility issues, which may lead to `NoClassDefFoundError`, `IllegalAccessError`, `AbstractMethodError`, `LinkageError`, etc.

### 3.3 Selecting the <ins>[Trace Exporter](#62-trace-exporter)</ins>

The <ins>SpecialAgent</ins> supports OpenTracing-compatible <ins>[Tracers](#61-tracer)</ins>. There are 2 ways to connect a <ins>[Tracer](#61-tracer)</ins> to the <ins>SpecialAgent</ins> runtime:

1. **Bundled <ins>[Trace Exporters](#62-trace-exporter)</ins>**

    The <ins>SpecialAgent</ins> bundles the following <ins>[Trace Exporters](#62-trace-exporter)</ins>:

    1. [Jaeger Trace Exporter](https://github.com/opentracing-contrib/java-opentracing-jaeger-bundle)
    1. [LightStep Trace Exporter](https://github.com/lightstep/lightstep-tracer-java/tree/master/lightstep-tracer-jre-bundle)
    1. [Wavefront Trace Exporter](https://github.com/wavefrontHQ/wavefront-opentracing-bundle-java)
    1. [OpenTelemetry Bridge Trace Exporter](https://github.com/opentracing-contrib/java-opentelemetry-bridge)
    1. [`MockTracer`](https://github.com/opentracing/opentracing-java/blob/master/opentracing-mock/)

    The `-Dsa.exporter=${SHORT_NAME}` property specifies which <ins>[Trace Exporter](#62-trace-exporter)</ins> is to be used. The value of `${SHORT_NAME}` is the <ins>[Short Name](#21221-short-name)</ins> of the <ins>[Trace Exporter](#62-trace-exporter)</ins>, i.e. `jaeger`, `lightstep`, `wavefront`, `otel`, or `mock`.

1. **External <ins>[Trace Exporters](#62-trace-exporter)</ins>**

    The <ins>SpecialAgent</ins> allows external <ins>[Trace Exporters](#62-trace-exporter)</ins> to be attached to the runtime.

    The `-Dsa.exporter=${TRACE_EXPORTER_JAR}` property specifies the JAR path of the <ins>[Trace Exporter](#62-trace-exporter)</ins> to be used. The `${TRACE_EXPORTER_JAR}` must be a JAR that supplies an implementation of the [`TracerFactory`](https://github.com/opentracing-contrib/java-tracerresolver#tracer-factory) interface of the [TracerResolver](https://github.com/opentracing-contrib/java-tracerresolver) project.

_**NOTE**: If a tracer is not specified with the `-Dsa.exporter=...` property, the <ins>SpecialAgent</ins> will present a warning in the log that states: `Tracer NOT RESOLVED`._

### 3.4 Disabling <ins>[Integration Rules](#64-integration-rule)</ins>

<ins>[Integrations](#63-integration)</ins> bundled with the <ins>SpecialAgent</ins> are enabled by default.

Multiple properties to <ins>disable</ins> or to <ins>enable</ins> all or individual plugins can be declared via the [Configuration Pattern](#3-configuration). The processing order of the properties is equal to the order of their declaration.

#### 3.4.1 Disabling All <ins>[Integration Rules](#64-integration-rule)</ins>

To <ins>disable</ins> _all <ins>[Integrations](#63-integration)</ins>_:

```
sa.integration.*.disable
```
<sup>The suffix `.disable` is interchangeable with `.enable=false`.</sup>

#### 3.4.2 Disabling (or enabling) One <ins>[Integration Rule](#64-integration-rule)</ins>

To <ins>disable</ins> _an individual <ins>[Integration](#63-integration)</ins>_:

```
sa.integration.${INTEGRATION_NAME_PATTERN}.disable
```
<sup>The suffix `.disable` is interchangeable with `.enable=false`.</sup>

Conversely, to <ins>enable</ins> _an individual <ins>[Integration](#63-integration)</ins>_.

```
sa.integration.${INTEGRATION_NAME_PATTERN}.enable
```
<sup>The suffix `.enable` is interchangeable with `.disable=false`.</sup>

##### Integration Name Pattern

The value of `${INTEGRATION_NAME_PATTERN}` represents the name of the <ins>[Integration Rule](#64-integration-rule)</ins>, as specified in <ins>[Integrations](#41-integrations)</ins> ("Integration Rule" column). The `${INTEGRATION_NAME_PATTERN}` allows for the use of `*` and `?` characters to match multiple rules simultaneously. For instance:

1. `dubbo:2.?`<br>Matches all <ins>Dubbo</ins> rules, including `dubbo:2.6`, and `dubbo:2.7`.
1. `cassandra:driver:*`<br>Matches all <ins>Cassandra Driver</ins> rules, including `cassandra:driver:3`, and `cassandra:driver:4`.
1. `spring:web*`<br>Matches all <ins>Spring WebMVC</ins>, <ins>Spring WebFlux</ins> and <ins>Spring WebSocket</ins> rules, including `spring:webmvc`, `spring:webflux`, and `spring:websocket`.
1. `cassandra:driver`<br>Matches all <ins>Cassandra Driver</ins> rules, including `cassandra:driver:3` and `cassandra:driver:4`.

If the _version part_ of the `${INTEGRATION_NAME_PATTERN}` does not end with a `*` or `?` character, a `*` will be appended automatically. Therefore:

1. `dubbo:2`<br>Matches all <ins>Dubbo</ins> v2 rules, including `dubbo:2.6`, and `dubbo:2.7`.
1. `cassandra:driver`<br>Matches all <ins>Cassandra Driver</ins> rules, `cassandra:driver:3`, and `cassandra:driver:4`.
1. `spring`<br>Matches all <ins>Spring</ins> rules.
1. `spring:w`<br>Does not match any rules.

#### 3.4.3 Disabling `AgentRule`s of an <ins>[Integration Rule](#64-integration-rule)</ins>

To disable _an individual `AgentRule` of an <ins>[Integration](#63-integration)</ins>_:

```
sa.integration.${INTEGRATION_NAME_PATTERN}#${AGENT_RULE_SIMPLE_CLASS_NAME}.disable
```
<sup>The suffix `.disable` is interchangeable with `.enable=false`.</sup>

The value of `${AGENT_RULE_SIMPLE_CLASS_NAME}` is the simple class name of the `AgentRule` subclass that is to be disabled.

### 3.5 Disabling <ins>[Trace Exporters](#62-trace-exporter)</ins>

All <ins>[Trace Exporters](#62-trace-exporter)</ins> bundled in <ins>SpecialAgent</ins> are enabled by default, and can be disabled.

To disable _all <ins>[Trace Exporters](#62-trace-exporter)</ins>_:

```
sa.exporter.*.disable
```
<sup>The suffix `.disable` is interchangeable with `.enable=false`.</sup>

To disable _an individual <ins>Trace Exporter</ins>_:

```
sa.exporter.${SHORT_NAME}.disable
```
<sup>The suffix `.disable` is interchangeable with `.enable=false`.</sup>

The value of `${SHORT_NAME}` is the <ins>[Short Name](#21221-short-name)</ins> of the plugin, such as `jaeger`, `lightstep`, `wavefront`, `otel`, or `mock`.

### 3.6 Including Custom <ins>[Integration Rules](#64-integration-rule)</ins>

Custom <ins>[Integration Rules](#64-integration-rule)</ins> can be implemented by following the [SpecialAgent Rule API](https://github.com/opentracing-contrib/java-specialagent/tree/master/opentracing-specialagent-api). JARs containing custom <ins>[Integration Rules](#64-integration-rule)</ins> can be loaded by <ins>SpecialAgent</ins> via:

```
-Dsa.classpath=${JARs}
```

Here, `${JARs}` refers to a `File.pathSeparator`-delimited (`:` for \*NIX, `;` for Windows) string of JARs containing the custom <ins>[Integration Rules](#64-integration-rule)</ins>.

### 3.7 Rewritable Tracer

The <ins>[Rewritable Tracer](#37-rewritable-tracer)</ins> allows one to rewrite data in the spans created by <ins>[Integrations](#41-integrations)</ins> without having to modify the source code.

The <ins>[Rewritable Tracer](#37-rewritable-tracer)</ins> is a rules engine that is configured via JSON files [that conform to a specification][rewrite].

For example:

* The following JSON defines a rule for all <ins>[Integrations](#41-integrations)</ins> to drop all **tag**s in spans matching `key` literal `http.url` and `value` regex `.*secret.*`.

  ```json
  {
    "*": [
      {
        "input": {
          "type": "tag",
          "key": "http.url",
          "value": ".*secret.*"
        }
      }
    ]
  }
  ```

* The following JSON defines a rule for the `jedis` <ins>[Integration](#41-integrations)</ins> to rewrite all **log**s matching `key` literal `http.method` as a **tag**.

```json
{
  "jedis": [
    {
      "input": {
        "type": "log",
        "key": "http.method",
      },
      "output": {
        "type": "tag"
      }
    }
  ]
}
```

For a configuration spec and other use-case examples, please refer to the [`rewrite` plugin][rewrite].

## 4 Supported <ins>[Integrations](#63-integration)</ins> and <ins>[Trace Exporters](#62-trace-exporter)</ins>

### 4.1 <ins>[Integrations](#63-integration)</ins>

Intrinsically, the <ins>SpecialAgent</ins> includes support for the instrumentation of the following 3rd-party libraries. Each row refers to an <ins>[Integration](#63-integration)</ins>, the <ins>[Integration Rule](#64-integration-rule)</ins>, and the minimum and maximum version tested by the build.

For the development of <ins>[Integration Rules](#64-integration-rule)</ins>, please refer to the [`opentracing-specialagent-api`][api] module.

| Integration<br/><sup>(link to impl. of <ins>[Integration](#63-integration)</ins>)</sup> | Integration Rule<br/><sup>(link to impl. of <ins>[Integration Rule](#64-integration-rule)</ins>)</sup> | Min Version<br/><sup>(min supported)</sup> | Max Version<br/><sup>(max supported)</sup> |
|:-|:-|:-:|:-:|
| [Akka Actor](https://github.com/opentracing-contrib/java-akka) | [`akka:actor`][akka-actor] | 2.5.0 | LATEST |
| Akka Http | [`akka:http`][akka-http] | 10.1.0 | LATEST |
| [Apache Camel](https://github.com/apache/camel/tree/master/components/camel-opentracing) | [`camel`][camel] | 2.24.0 | 2.24.2 |
| Apache CXF | [`cxf`][cxf] | 3.3.3 | LATEST |
| Apache Dubbo | [`dubbo:2.6`][dubbo-2.6] | 2.6.2 | 2.6.7 |
| | [`dubbo:2.7`][dubbo-2.7] | 2.7.1 | LATEST |
| [Apache HttpClient](https://github.com/opentracing-contrib/java-apache-httpclient) | [`apache:httpclient`][apache-httpclient] | 4.4 | LATEST |
| [Async Http Client](https://github.com/opentracing-contrib/java-asynchttpclient) | [`asynchttpclient`][asynchttpclient] | 2.7.0 | LATEST |
| [AWS SDK](https://github.com/opentracing-contrib/java-aws-sdk) | [`aws:sdk:1`][aws-sdk-1] | 1.11.79 | LATEST |
| | [`aws:sdk:2`][aws-sdk-2] | 2.1.4 | **FIXME** |
| [Cassandra Driver](https://github.com/opentracing-contrib/java-cassandra-driver) | [`cassandra:driver:3`][cassandra-driver-3] | 3.0.0 | 3.7.2 |
| | [`cassandra:driver:4`][cassandra-driver-4] | 4.0.0 | LATEST |
| Couchbase Client | [`couchbase-client`][couchbase-client] | 2.7.3 | 2.7.13 |
| Dynamic<br>&nbsp; | [`dynamic`<br><sup>(configurable)</sup>][dynamic] | **\***<br>&nbsp; | **\***<br>&nbsp; |
| [Elasticsearch Client<br>&nbsp;](https://github.com/opentracing-contrib/java-elasticsearch-client) | [`elasticsearch:client-transport`][elasticsearch-7-transport-client]<br>[`elasticsearch:client-rest`][elasticsearch-7-rest-client] | 6.4.0<br>6.4.0 | LATEST<br>6.8.7 |
| [Feign](https://github.com/OpenFeign/feign-opentracing/tree/master/feign-opentracing) | [`feign`][feign] | 9.0.0 | LATEST |
| Google Http Client | [`google-http-client`][google-http-client] | 1.19.0 | LATEST |
| [Grizzly AsyncHttpClient](https://github.com/opentracing-contrib/java-grizzly-ahc) | [`grizzly:ahc`][grizzly-ahc] | 1.15 | LATEST |
| [Grizzly HTTP Server](https://github.com/opentracing-contrib/java-grizzly-http-server) | [`grizzly:http-server`][grizzly-http-server] | 2.3.35 | LATEST |
| [GRPC](https://github.com/opentracing-contrib/java-grpc) | [`grpc`][grpc] | 1.7.3 | LATEST |
| [Hazelcast](https://github.com/opentracing-contrib/opentracing-hazelcast) | [`hazelcast`][hazelcast] | 3.12 | 3.12.6 |
| [Java Concurrent API \[`java.util.concurrent`\]](https://github.com/opentracing-contrib/java-concurrent) | [`concurrent`][concurrent] | 1.5 | 11 |
| [Java JDBC API \[`java.sql`\]][java-jdbc]<br>&nbsp; | [`jdbc`<br><sup>(configurable)</sup>][jdbc] | 3.1<br>&nbsp; | 4.3<br>&nbsp; |
| [Java JMS API \[`javax.jms`\]][java-jms] | [`jms`][jms] | 1.1-rev-1 | LATEST |
| [Java Servlet API \[`javax.servlet`\]](https://github.com/opentracing-contrib/java-web-servlet-filter)<br>&nbsp; | [`servlet`<br><sup>(configurable)</sup>][servlet] | 2.3<br>&nbsp; | 3.1<br>&nbsp; |
| &nbsp;&nbsp;&nbsp;&nbsp;Jetty | | 7.6.21.v20160908 | 9.2.15.v20160210 |
| &nbsp;&nbsp;&nbsp;&nbsp;Tomcat | | 7.0.65 | 9.0.27 |
| Java Thread [`java.lang.Thread`] | [`thread`][thread] | 1.0 | 11 |
| HttpURLConnection [`java.net.HttpURLConnection`] | [`httpurlconnection`][httpurlconnection] | 1.1 | 11 |
| [JAX-RS Client](https://github.com/opentracing-contrib/java-jaxrs) | [`jax-rs`][jaxrs] | 2.0 | LATEST |
| [Kafka Client](https://github.com/opentracing-contrib/java-kafka-client) | [`kafka:client`][kafka-client] | 1.1.0 | LATEST |
| [Kafka Streams](https://github.com/opentracing-contrib/java-kafka-client) | [`kafka:streams`][kafka-streams] | 1.1.0 | LATEST |
| [Lettuce Client](https://github.com/opentracing-contrib/java-redis-client/tree/master/opentracing-redis-lettuce) | [`lettuce`][lettuce] | 5.0.0.RELEASE | LATEST |
| [MongoDB Driver](https://github.com/opentracing-contrib/java-mongo-driver) | [`mongo:driver`][mongo-driver] | 3.9.0 | LATEST |
| Mule 4 Artifact Module | [`mule:artifact-module:4`][mule-4-module-artifact] | 4.2.2 | LATEST |
| Mule 4 Core | [`mule:core:4`][mule-4-core] | 4.2.2 | LATEST |
| [Neo4j Driver](https://github.com/opentracing-contrib/java-neo4j-driver) | [`neo4j:driver`][neo4j-driver] | 4.0.0 | LATEST |
| Netty | [`netty`][netty] | 4.1.0 | 4.1.46.Final |
| [OkHttp][java-okhttp] | [`okhttp`][okhttp] | 3.5.0 | LATEST |
| Play Framework | [`play`][play] | 2.6.0 | LATEST |
| Play WS | [`play:ws`][play-ws] | 2.0.0 | **FIXME** |
| Pulsar Client | [`pulsar:client`][pulsar-client] | 2.2.0 | **FIXME** |
| Pulsar Functions <br>&nbsp; | [`pulsar-functions`<br><sup>(configurable)</sup>][pulsar-functions] | 2.2.0<br>&nbsp; | **FIXME**<br>&nbsp; |
| [RabbitMQ Client](https://github.com/opentracing-contrib/java-rabbitmq-client) | [`rabbitmq:client`][rabbitmq-client] | 5.0.0 | LATEST |
| [Reactor](https://github.com/opentracing-contrib/java-reactor) | [`reactor`][reactor] | 3.2.3.RELEASE | LATEST |
| [Redis Clients](https://github.com/opentracing-contrib/java-redis-client) | [`jedis`][jedis] | 2.7.0 | LATEST |
| [Redisson](https://github.com/opentracing-contrib/java-redis-client/tree/master/opentracing-redis-redisson) | [`redisson`][redisson] | 3.11.0 | 3.11.5 |
| [RxJava](https://github.com/opentracing-contrib/java-rxjava) | [`rxjava:2`][rxjava-2] | 2.1.0 | LATEST |
| | [`rxjava:3`][rxjava-3] | 3.0.0 | LATEST |
| [Spring JMS](https://github.com/opentracing-contrib/java-jms/tree/master/opentracing-jms-spring) | [`spring:jms`][spring-jms] | 5.0.0.RELEASE | LATEST |
| [Spring Kafka](https://github.com/opentracing-contrib/java-kafka-client/tree/master/opentracing-kafka-spring) | [`spring:kafka`][spring-kafka] | 2.2.0.RELEASE | LATEST |
| [Spring Messaging](https://github.com/opentracing-contrib/java-spring-messaging) | [`spring:messaging`][spring-messaging] | 5.1.0.RELEASE | 5.3.0.RELEASE |
| [Spring RabbitMQ](https://github.com/opentracing-contrib/java-spring-rabbitmq) | [`spring:rabbitmq`][spring-rabbitmq] | 2.0.0.RELEASE | LATEST |
| [Spring WebFlux](https://github.com/opentracing-contrib/java-spring-web) | [`spring:webflux`][spring-webflux] | 5.1.0.RELEASE | LATEST |
| [Spring Boot WebSocket STOMP](https://github.com/opentracing-contrib/java-spring-cloud/tree/master/instrument-starters/opentracing-spring-cloud-websocket-starter) | [`spring:websocket`][spring-websocket] | 2.1.0.RELEASE | LATEST |
| [Spring \[`@Async` and `@Scheduled`\]](https://github.com/opentracing-contrib/java-spring-cloud/tree/master/instrument-starters/opentracing-spring-cloud-core) | [`spring:scheduling`][spring-scheduling] | 5.0.0.RELEASE | LATEST |
| [Spring Web MVC](https://github.com/opentracing-contrib/java-spring-web) | [`spring:webmvc`][spring-webmvc] | 3.0.2.RELEASE | LATEST |
| [Spymemcached](https://github.com/opentracing-contrib/java-memcached-client/tree/master/opentracing-spymemcached) | [`spymemcached`][spymemcached] | 2.11.0 | LATEST |
| [Thrift](https://github.com/opentracing-contrib/java-thrift) | [`thrift`][thrift] | 0.10.0 | 0.13.0 |
| [Zuul](https://github.com/opentracing-contrib/java-spring-cloud/tree/master/instrument-starters/opentracing-spring-cloud-zuul-starter) | [`zuul`][zuul] | 1.0.0 | 2.1.1 |

### 4.2 <ins>[Trace Exporters](#62-trace-exporter)</ins>

Intrinsically, the <ins>SpecialAgent</ins> includes support for the following <ins>[Trace Exporters](#62-trace-exporter)</ins>. A demo can be referenced [here](https://github.com/opentracing-contrib/java-specialagent-demo).

| Trace Exporter<br/><sup>(link to impl. of <ins>trace exporter</ins>)</sup> | [Short Name](#21221-short-name)<br/><sup>(`-Dsa.exporter=${short_name}`)</sup> |
|:-|:-|
| [Jaeger Trace Exporter](https://github.com/opentracing-contrib/java-opentracing-jaeger-bundle)<br/><sup>[(configuration reference)](https://github.com/jaegertracing/jaeger-client-java/blob/master/jaeger-core/README.md#configuration-via-environment)</sup> | `jaeger`<br/>&nbsp; |
| [LightStep Trace Exporter](https://github.com/lightstep/lightstep-tracer-java/tree/master/lightstep-tracer-jre-bundle)<br/><sup>[(configuration reference)](https://docs.lightstep.com/docs/create-projects-for-your-environments)</sup> | `lightstep`<br/>&nbsp; |
| [Wavefront Trace Exporter](https://github.com/wavefrontHQ/wavefront-opentracing-bundle-java)<br/><sup>[(configuration reference)](https://github.com/wavefrontHQ/wavefront-jersey-sdk-java#quickstart)</sup> | `wavefront`<br/>&nbsp; |
| [OpenTelemetry Bridge Trace Exporter](https://github.com/opentracing-contrib/java-opentelemetry-bridge)<br/><sup><ins>(configuration reference)</ins></sup> | `otel`<br/>&nbsp; |
| [`MockTracer`](https://github.com/opentracing/opentracing-java/blob/master/opentracing-mock/)| `mock` |

### 4.3 Libraries instrumented via other Integrations

The following libraries are instrumented by other <ins>[Integration Rules](#64-integration-rule)</ins>.

| Library<br/>&nbsp; | Integration Rule<br/><sup>(link to impl. of <ins>[Integration Rule](#64-integration-rule)</ins>)</sup> | Min Version<br/><sup>(min supported)</sup> | Max Version<br/><sup>(max supported)</sup> |
|:-|:-|:-:|:-:|
| Hystrix | [`concurrent`][concurrent] | 1.5 | 11 |
| JDBI<br>&nbsp; | [`jdbc`<br><sup>(configurable)</sup>][jdbc] | \*<br>&nbsp; | \*<br>&nbsp; |
| Ratpack | [`netty`][netty] | 1.4.0 | LATEST |
| Solr Client | [`apache:httpclient`][apache-httpclient] | 4.0.0 | LATEST |
| SparkJava | [`javax.servlet`][servlet] | 2.2 | LATEST |
| Spring Cloud | \*<br>&nbsp; | \*<br>&nbsp; | \*<br>&nbsp; |
| Spring Data<br>&nbsp; | [`jdbc`<br><sup>(configurable)</sup>][jdbc] | \*<br>&nbsp; | \*<br>&nbsp; |
| Spring Web | [`httpurlconnection`][httpurlconnection] | \*<br>&nbsp; | \*<br>&nbsp; |
| Twilio | [`apache:httpclient`][apache-httpclient] | 0.0.1 | LATEST |

## 5 Objectives

### 5.1 Goals

1. The <ins>SpecialAgent</ins> must allow any <ins>[Integration](#63-integration)</ins> available in [opentracing-contrib][opentracing-contrib] to be automatically installable in applications that utilize a 3rd-party library for which an <ins>[Integration](#63-integration)</ins> exists.
1. The <ins>SpecialAgent</ins> must automatically install the <ins>[Integration](#63-integration)</ins> for each 3rd-party library, regardless in which class loader the 3rd-party library is loaded.
1. The <ins>SpecialAgent</ins> must not adversely affect the runtime stability of the application on which it is intended to be used. This goal applies only to the code in the <ins>SpecialAgent</ins>, and transitively applies to the code of the <ins>[Integration](#63-integration)</ins> made available in [opentracing-contrib][opentracing-contrib].
1. The <ins>SpecialAgent</ins> must support <ins>[Static Attach](#221-static-attach)</ins> and <ins>[Dynamic Attach](#222-dynamic-attach)</ins> for applications running on JVM versions 1.7, 1.8, 9, and 11.
1. The <ins>SpecialAgent</ins> must implement a lightweight test methodology that can be easily applied to a module that implements <ins>[Integration](#63-integration)</ins> for a 3rd-party library. This test must simulate:
   1. Launch the test in a process simulating the `-javaagent` vm argument that points to the <ins>SpecialAgent</ins> (in order to test auto-instrumentation functionality).
   1. Elevate the test code to be executed from a custom class loader that is disconnected from the system class loader (in order to test bytecode injection into an isolated class loader that cannot resolve classes on the system classpath).
   1. Allow tests to specify their own `Tracer` instances via `GlobalTracer`, or initialize a `MockTracer` if no instance is specified. The test must provide a reference to the `Tracer` instance in the test method for assertions with JUnit.
1. The <ins>SpecialAgent</ins> must provide a means by which <ins>[Integrations](#63-integration)</ins> can be configured for use on a target application.

### 5.2 Non-Goals

1. The <ins>SpecialAgent</ins> is not designed to modify application code, beyond the installation of <ins>[Integrations](#63-integration)</ins>. For example, there is no facility for dynamically augmenting arbitrary code.

## 6 Definitions

The following terms are used throughout this documentation.

#### 6.1 <ins>[Tracer](#61-tracer)</ins>

Service provider of the OpenTracing standard, providing an implementation of the `io.opentracing.Tracer` interface.

Examples:
* [Jaeger Tracer][jaeger]
* [LightStep Tracer][lightstep]
* [Wavefront Tracer][wavefront]

<sub>_<ins>[Tracers](#61-tracer)</ins> **are not** coupled to the <ins>SpecialAgent</ins>._</sub>

#### 6.2 <ins>[Trace Exporter](#62-trace-exporter)</ins>

A bridge providing automatic discovery of <ins>[Tracers](#61-tracer)</ins> in a runtime instrumented with the OpenTracing API. This bridge implements the `TracerFactory` interface of [TracerResolver](https://github.com/opentracing-contrib/java-tracerresolver/blob/master/opentracing-tracerresolver/), and is distributed as a single "fat JAR" that can be conveniently added to the classpath of a Java process.

<sub>_<ins>[Trace Exporters](#62-trace-exporter)</ins> **are not** coupled to the <ins>SpecialAgent</ins>._</sub>

#### 6.3 <ins>[Integration](#63-integration)</ins>

An OpenTracing Integration for a 3rd-party library, existing as individual repositories in [opentracing-contrib][opentracing-contrib].

Examples:
* [`opentracing-contrib/java-okhttp`][java-okhttp]
* [`opentracing-contrib/java-jdbc`][java-jdbc]
* [`opentracing-contrib/java-jms`][java-jms]

<sub>_<ins>[Integrations](#63-integration)</ins> **are not** coupled to the <ins>SpecialAgent</ins>._</sub>

#### 6.4 <ins>[Integration Rule](#64-integration-rule)</ins>

A submodule of the <ins>SpecialAgent</ins> that implements the auto-instrumentation rules for <ins>[Integrations](#63-integration)</ins> via the [`opentracing-specialagent-api`][api]. See <ins>[Integrations](#2121-integrations)</ins> for a description of Uncoupled and Coupled Integrations.

Examples:
* [`rule/okhttp`][okhttp]
* [`rule/jdbc`][jdbc]
* [`rule/jms-1`][jms-1]

<sub>_<ins>[Integration Rules](#64-integration-rule)</ins> **are** coupled to the <ins>SpecialAgent</ins>._</sub>

## 7 Credits

Thank you to the following contributors for developing <ins>[Integrations](#2121-integrations)</ins> and <ins>[Integration Rules](#64-integration-rule)</ins>:

* [Sergei Malafeev](https://github.com/malafeev)
* [Jose Montoya](https://github.com/jam01)
* [Przemyslaw Maciolek](https://github.com/pmaciolek)
* [Jianshao Wu](https://github.com/jianshaow)
* [Gregor Zeitlinger](https://github.com/zeitlinger)
* [@limfriend](https://github.com/limfriend)

Thank you to the following contributors for developing <ins>[Trace Exporters](#62-trace-exporter)</ins>:

* [Carlos Alberto Cortez](https://github.com/carlosalberto)
* [Han Zhang](https://github.com/hanwavefront)

Thank you to the following developers for filing issues and helping us fix them:

* [Louis-Etienne](https://github.com/ledor473)
* [Marcos Trejo Munguia](https://github.com/mtrejo)
* [@kaushikdeb](https://github.com/kaushikdeb)
* [@deepakgoenka](https://github.com/deepakgoenka)
* [Prometheus](https://github.com/etsangsplk)
* [Randall Theobald](https://github.com/randallt)

Thank you to the following individuals for all other general contributions to the codebase:

* [Daniel Rodriguez Hernandez](https://github.com/drodriguezhdez)
* [qudongfang](https://github.com/qudongfang)
* [Pontus Rydin](https://github.com/prydin)

Finally, thanks for all of the feedback! Please share your comments [as an issue](https://github.com/opentracing-contrib/java-specialagent/issues)!

## 8 Contributing

Pull requests are welcome. For major changes, please [open an issue](https://github.com/opentracing-contrib/java-specialagent/issues) first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## 9 License

This project is licensed under the Apache 2 License - see the [LICENSE.txt](LICENSE.txt) file for details.

[akka-actor]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/akka-actor
[akka-http]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/akka-http
[apache-httpclient]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/apache-httpclient
[asynchttpclient]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/asynchttpclient
[aws-sdk-1]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/aws-sdk-1
[aws-sdk-2]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/aws-sdk-2
[camel]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/camel
[cassandra-driver-3]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/cassandra-driver-3
[cassandra-driver-4]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/cassandra-driver-4
[concurrent]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/concurrent
[couchbase-client]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/couchbase-client
[cxf]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/cxf
[dubbo-2.6]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/dubbo-2.6
[dubbo-2.7]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/dubbo-2.7
[dynamic]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/dynamic
[elasticsearch-7-rest-client]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/elasticsearch-7-rest-client
[elasticsearch-7-transport-client]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/elasticsearch-7-transport-client
[feign]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/feign
[google-http-client]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/google-http-client
[grizzly-ahc]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/grizzly-ahc
[grizzly-http-server]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/grizzly-http-server
[grpc]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/grpc
[hazelcast]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/hazelcast
[httpurlconnection]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/httpurlconnection
[jaxrs]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/jaxrs
[jdbc]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/jdbc
[jedis]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/jedis
[jms]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/jms
[kafka-client]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/kafka-client
[kafka-streams]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/kafka-streams
[lettuce]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/lettuce
[mongo-driver]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/mongo-driver
[mule-4-core]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/mule-4-core
[mule-4-module-artifact]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/mule-4-module-artifact
[neo4j-driver]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/neo4j-driver
[netty]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/netty
[okhttp]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/okhttp
[play-ws]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/play-ws
[play]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/play
[pulsar-client]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/pulsar-client
[pulsar-functions]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/pulsar-functions
[rabbitmq-client]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/rabbitmq-client
[reactor]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/reactor
[redisson]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/redisson
[rewrite]: https://github.com/opentracing-contrib/java-specialagent/tree/master/opentracing-rewrite
[rxjava-2]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/rxjava-2
[rxjava-3]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/rxjava-3
[servlet]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/servlet
[spring-jms]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/spring-jms
[spring-kafka]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/spring-kafka
[spring-messaging]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/spring-messaging
[spring-rabbitmq]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/spring-rabbitmq
[spring-scheduling]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/spring-scheduling
[spring-webflux]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/spring-webflux
[spring-webmvc]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/spring-webmvc
[spring-websocket]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/spring-websocket
[spymemcached]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/spymemcached
[thread]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/thread
[thrift]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/thrift
[zuul]: https://github.com/opentracing-contrib/java-specialagent/tree/master/rule/zuul

[jaeger]: https://github.com/jaegertracing/jaeger
[lightstep]: https://github.com/lightstep/lightstep-tracer-java
[wavefront]: https://github.com/wavefrontHQ/wavefront-opentracing-sdk-java

[agentrunner-config]: https://github.com/opentracing-contrib/java-specialagent/tree/master/opentracing-specialagent-api#51-configuring-agentrunner
[api]: https://github.com/opentracing-contrib/java-specialagent/tree/master/opentracing-specialagent-api
[java-jdbc]: https://github.com/opentracing-contrib/java-jdbc
[java-jms]: https://github.com/opentracing-contrib/java-jms
[java-okhttp]: https://github.com/opentracing-contrib/java-okhttp
[opentracing-contrib]: https://github.com/opentracing-contrib/
[specialagent-pom]: https://github.com/opentracing-contrib/java-specialagent/blob/master/pom.xml
[circleci]: https://circleci.com/gh/opentracing-contrib/java-specialagent/tree/master

[main-release]: https://repo1.maven.org/maven2/io/opentracing/contrib/specialagent/opentracing-specialagent/1.7.4/opentracing-specialagent-1.7.4.jar
[main-snapshot]: https://oss.sonatype.org/content/repositories/snapshots/io/opentracing/contrib/specialagent/opentracing-specialagent/1.7.5-SNAPSHOT
