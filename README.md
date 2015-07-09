# ant-mongo-ext
Mongo Tasks for Ant

Copy ant-mongo-ext-1.0.1.jar to the lib directory of your Ant installation. 

Add the following line to your ant build file:
```xml
<taskdef resource="net/sf/antcontrib/antcontrib.properties"/>
```

Call the new task from within your build:
```xml
<mongoPrimary db="[server1:27017,server2:27017]"/>
```

Properties will then be created and ready for use within the rest of your build file:
```xml
<echo message="${mongo.primary.host} ${mongo.primary.port}"/>
```
