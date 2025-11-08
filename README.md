# CUID for Java

[![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg?logo=github)](https://raw.githubusercontent.com/thibaultmeyer/cuid-java/master/LICENSE)
[![Repository release](https://img.shields.io/github/v/release/thibaultmeyer/cuid-java?logo=github)](https://github.com/thibaultmeyer/cuid-java/releases)
[![Maven](https://img.shields.io/maven-central/v/io.github.thibaultmeyer/cuid.svg?logo=apache-maven)](https://central.sonatype.com/artifact/io.github.thibaultmeyer/cuid)
[![Repository size](https://img.shields.io/github/repo-size/thibaultmeyer/cuid-java.svg?logo=git)](https://github.com/thibaultmeyer/cuid-java)

[![Javadoc](https://javadoc.io/badge2/io.github.thibaultmeyer/cuid/javadoc.svg)](https://javadoc.io/doc/io.github.thibaultmeyer/cuid)

Java implementation of CUID. Read more at <a href="https://usecuid.org/">CUID official website</a>.
*****


## Build
To compile CUID for Java, you must ensure that Java 11 (or above) and Maven are correctly
installed.

    #> mvn package
    #> mvn install

To speed up process, you can ignore unit tests by using: `-DskipTests=true -Dmaven.test.skip=true`.



## How to use

```xml
<dependency>
  <groupId>io.github.thibaultmeyer</groupId>
  <artifactId>cuid</artifactId>
  <version>x.y.z</version>
</dependency>
```

```java
final CUID cuid = CUID.randomCUID1();
System.out.println("CUID: " + cuid);
```

```java
final CUID cuid = CUID.randomCUID2();
System.out.println("CUID (Version 2): " + cuid);
```

```java
final int customLength = 8;  // Length must be, at least, 1
final CUID cuid = CUID.randomCUID2(customLength);
System.out.println("CUID (Version 2): " + cuid);
```

```java
final CUID cuid = CUID.fromString("cl9gts1kw00393647w1z4v2tc");
System.out.println("CUID: " + cuid);
```

```java
final boolean isValid = CUID.isValid("cl9gts1kw00393647w1z4v2tc");
System.out.println("Is 'cl9gts1kw00393647w1z4v2tc' a valid CUID ? " + isValid);
```


## License
This project is released under terms of the [MIT license](https://raw.githubusercontent.com/thibaultmeyer/cuid-java/master/LICENSE).
