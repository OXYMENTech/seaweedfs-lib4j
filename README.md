# SeaweedFS Client For Java
[![Maven Central](https://img.shields.io/maven-central/v/tech.oxymen.seaweedfs/seaweedfs-lib4j.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22tech.oxymen.seaweedfs%22%20AND%20a:%22seaweedfs-lib4j%22)
[![Apache license](https://img.shields.io/badge/license-Apache-blue.svg)](http://opensource.org/licenses/Apache)



# Quick Start

##### Maven
```maven
    
 <dependency>
   <groupId>tech.oxymen.seaweedfs</groupId>
   <artifactId>seaweedfs-lib4j</artifactId>
   <version>1.1.0.RELEASE</version>
 </dependency>

```


##### Create a connection manager
```java
    
    FileSystem ofs = new FileSystem("192.168.0.111", 9333, 20);


    File file = new File("/Users/luck/Downloads/IMG_3475.jpg");
    FileHandleStatus fhs = ofs.saveFile(file);

    System.out.println(fhs.toString());

    ofs.stop();
```

##### API Reference
```java
FileSystemTest.java
```

## License

The Apache Software License, Version 2.0

Copyright  [2021]  [OXYMEN]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
