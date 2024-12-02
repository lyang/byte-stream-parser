<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>{groupId}</groupId>
    <artifactId>{artifactId}</artifactId>
    <version>{version}</version>
    <dependencies>
        {dependencies}
    </dependencies>

    <name>${project.groupId}:${project.artifactId}</name>
    <description>A library to parse structured data from byte stream input</description>
    <url>https://github.com/lyang/byte-stream-parser</url>

    <licenses>
        <license>
            <name>Apache-2.0 License</name>
            <url>https://www.apache.org/licenses/LICENSE-2.0.txt</url>
        </license>
    </licenses>

    <developers>
        <developer>
            <name>Lin Yang</name>
            <email>github@linyang.me</email>
            <organizationUrl>https://github.com/lyang</organizationUrl>
        </developer>
    </developers>

    <scm>
        <connection>scm:git:git://github.com/lyang/byte-stream-parser.git</connection>
        <developerConnection>scm:git:ssh://github.com:lyang/byte-stream-parser.git</developerConnection>
        <url>https://github.com/lyang/byte-stream-parser</url>
    </scm>

    <issueManagement>
        <system>GitHub Issues</system>
        <url>https://github.com/lyang/byte-stream-parser/issues</url>
    </issueManagement>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
    </properties>
</project>