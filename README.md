# CUID for Java

[![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg?logo=github)](https://raw.githubusercontent.com/thibaultmeyer/cuid-java/master/LICENSE)
[![Repository size](https://img.shields.io/github/repo-size/thibaultmeyer/cuid-java.svg?logo=git)](https://github.com/thibaultmeyer/cuid-java)

Java implementation of CUID. Read more at <a href="https://usecuid.org/">CUID official website</a>.
*****


## Build
To compile CUID for Java, you must ensure that Java 11 (or above) and Maven are correctly
installed.

    #> mvn package
    #> mvn install

To speed up process, you can ignore unit tests by using: `-DskipTests=true -Dmaven.test.skip=true`.



## How to use

```java
final CUID cuid = CUID.randomCUID();
System.out.println("CUID: " + cuid);
```

```java
final CUID cuid = CUID.fromString("cl9gts1kw00393647w1z4v2tc");
System.out.println("CUID: " + cuid);
```


## License
This project is released under terms of the [MIT license](https://raw.githubusercontent.com/thibaultmeyer/cuid-java/master/LICENSE).
