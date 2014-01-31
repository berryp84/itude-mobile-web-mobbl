# Web MOBBL

A development framework for data centric mobile web apps.

## Overview

Most current app development is about connecting consumers and employees to back-end systems through their tablet or smartphone. MOBBL aims to make these type of apps easier to develop, port and maintain.

## Documentation

Generic documentation can be found at [Mobbl.org](http://mobbl.org/doc.html).
Documentation about how to get started can be [here](http://mobbl.org/mobileweb.html).

## [Changelog](https://github.com/ItudeMobile/itude-mobile-web-mobbl/wiki/Changelog)
Current version: 0.1.1.2

## Build
#### Maven

First add the [ItudeMobile repository](https://github.com/ItudeMobile/maven-repository) to your pom.xml

```xml
<repository>
	<id>itudemobile-github-repository</id>
	<name>ItudeMobile Github repository</name>
	<url>http://mobbl.org/maven-repository/releases</url>
</repository>
```

Now add Web Mobbl

```xml
<dependency>
    <groupId>com.itude.mobile.web.mobbl</groupId>
    <artifactId>mobbl-core-lib</artifactId>
    <version>${core.lib.version}</version>
    <type>jar</type>
</dependency>
```
and the [Web Util](https://github.com/ItudeMobile/itude-mobile-web-util)

```xml
<dependency>
	<groupId>com.itude.mobile.web.util</groupId>
	<artifactId>web-util-lib</artifactId>
	<version>${util.lib.version}</version>
	<type>jar</type>
</dependency>
```
to your pom.xml.

## Contribute

If you find a bug or have a new feature you want to add, just create a pull request and submit it to us. You can also [file an issue](https://github.com/ItudeMobile/itude-mobile-web-mobbl/issues/new).

Please note, if you have a pull request, make sure to use the [develop branch](https://github.com/ItudeMobile/itude-mobile-web-mobbl/tree/develop) as your base.

#### Formatting

For contributors using Eclipse there's a [formatter](http://mobbl.org/downloads/code-format.xml) available.

## License
The code in this project is licensed under the Apache Software License 2.0, per the terms of the included LICENSE file.
