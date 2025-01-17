
<img height="80px" src="https://i.postimg.cc/FKDhP2kT/Easy-RPA-Full-Logo.png">

# EasyRPA Open Framework

### Table of Contents
* [Introduction](#introduction)
* [Usage](#usage)
* [Libraries](#libraries)
* [Examples](#examples)
* [Links](#links)
* [Contributing](#contributing)
* [License](#license)

## Introduction

The Robotic Process Automation (RPA) supposes the doing of things in a way as human does it, via UI elements. But the 
work with different document formats or services such as Excel, PDF, Google Docs, etc. can be done without actual 
manipulation with UI elements. If RPA platform supports describing of robot scenarios based on the one of popular 
program language like Java, the automation of these things can be done using tens of existing functional libraries 
and APIs. Such approach allows significantly speedup the robot work and increase performance. At the same time, if you 
are not familiar with these libraries it takes much time to find them and investigate. Moreover, the found 
functionality will be poorly adapted for usage in the code of robot scenarios. As result the using of functional 
libraries and APIs can become a nightmare. 

**EasyRPA Open Framework** is a collection of open-source Java-libraries for Robotic Process Automation designed to be 
used with [EasyRPA](http://easyrpa.eu) platform. It keeps in one place libraries to work with most popular document 
formats and services. The functionality is clear and easy to use with minimal amount of preparation or configuration 
steps in the code that ``significantly simplifies development of RPA processes.

The project is:
- 100% Open Source
- Optimized for EasyRPA platform
- Accepting external contributions

![License](https://img.shields.io/github/license/easyrpa/openframework?color=blue)

## Usage

EasyRPA Open Framework consist of several independent libraries. All of them are deployed on Maven Central repository.

![mavenVersion](https://img.shields.io/maven-central/v/eu.easyrpa/easy-rpa-openframework-database)
![mavenVersion](https://img.shields.io/maven-central/v/eu.easyrpa/easy-rpa-openframework-email)
![mavenVersion](https://img.shields.io/maven-central/v/eu.easyrpa/easy-rpa-openframework-excel)
![mavenVersion](https://img.shields.io/maven-central/v/eu.easyrpa/easy-rpa-openframework-google-drive)
![mavenVersion](https://img.shields.io/maven-central/v/eu.easyrpa/easy-rpa-openframework-google-sheets)

In order to use any of EasyRPA Open Framework's library you need simply add it as a dependency in your Maven POM file. 
E.g.:
```xml
<dependency>
    <groupId>eu.easyrpa</groupId>
    <artifactId>easy-rpa-openframework-email</artifactId>
    <version>1.0</version>
</dependency>
```

Additionally, to let libraries collaborate with RPA platform it's necessary to add as dependency corresponding adapter. 
Since this framework initially was intended and optimized to work with EasyRPA platform currently only adapter for 
EasyRPA platform is implemented and supported.

![mavenVersion](https://img.shields.io/maven-central/v/eu.easyrpa/easy-rpa-adapter-for-openframework)
```xml
<dependency>
    <groupId>eu.easyrpa</groupId>
    <artifactId>easy-rpa-adapter-for-openframework</artifactId>
    <version>1.0</version>
</dependency>
```

> There is no limitation to implement similar adapter for any other RPA platform that uses Java program language for 
> describing of robot scenarios but it's out of this project scope.  

## Libraries

The EasyRPA Open Framework includes following libraries:

<table>
    <tr><th align="left">Name</th><th align="left">Description</th></tr>
    <tr><td valign="top"><a href="/packages/database">Database</a></td><td>
        Functionality to work with remote databases (MySQL, PostgreSQL, Oracle, DB2, MS SQL Server). 
    </td></tr>
    <tr><td valign="top"><a href="/packages/email">Email</a></td><td>
        Functionality to work with mailboxes and email messages. 
    </td></tr>
    <tr><td valign="top"><a href="/packages/excel">Excel</a></td><td>
        Functionality to work with Excel documents. 
    </td></tr>   
    <tr><td valign="top"><a href="/packages/google-drive">GoogleDrive</a></td><td>
        Functionality to work with Google Drive files and folders. 
    </td></tr>
    <tr><td valign="top"><a href="/packages/google-sheets">GoogleSheets</a></td><td>
        Functionality to work with Google Sheets. 
    </td></tr>
</table> 

## Examples

Please refer to [Examples page](examples) to see the full list of examples of using EasyRPA Open Framework 
libraries.

## Links

Here is you can find useful links to other resources:

* [StackOverFlow](https://ru.stackoverflow.com/search?q=openframework)

## Contributing

Found a bug and it is necessary to make a fast fix? Wants to add a critical feature? Interested in contributing? Head 
over to the [Contribution guide](./CONTRIBUTING.md) to see where to get started.

## License
This project is open-source and licensed under the terms of the [Apache License 2.0](https://apache.org/licenses/LICENSE-2.0).
