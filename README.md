# ant-mongo-ext
Mongo Tasks for Ant

Copy ant-mongo-ext-1.0.1.jar to the lib directory of your Ant installation. 

Add the following line to your ant build file:
<pre>
<taskdef resource="net/sf/antcontrib/antcontrib.properties"/>
</pre>

Call the new task from within your build:
<pre>
<mongoPrimary db="[server1:27017,server2:27017]"/>
</pre>

Properties will then be created and ready for use within the rest of your build file:
<pre>
<echo message="${mongo.primary.host} ${mongo.primary.port}"/>
</pre>
