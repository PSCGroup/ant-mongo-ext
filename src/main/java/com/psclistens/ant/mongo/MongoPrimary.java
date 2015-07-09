package com.psclistens.ant.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User: mdehaan
 * Date: 7/9/2015
 */
public class MongoPrimary extends Task {

    String db;
    Map<String, Integer> hosts = new HashMap<String, Integer>();

    public static void main(String[] args) {
        String db = "\"mac-stage01.fairbanksllc.com:27017\",\"mac-stage02.fairbanksllc.com:27017\"";


        MongoPrimary primary = new MongoPrimary();

        primary.setDb(db);

        primary.execute();

    }

    public void execute() {

        if (hosts == null || hosts.size() == 0) {
            throw new BuildException("The 'db' propert must be set and cannot be blank.");
        }

        List<ServerAddress> serverAddresses = hosts.entrySet().stream().map(entry -> new ServerAddress(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        MongoClient mongoClient = new MongoClient(serverAddresses);

        try {
            MongoDatabase db = mongoClient.getDatabase("admin");
            BasicDBObject query = new BasicDBObject();
            query.put("serverStatus", "1");

            Document document = db.runCommand(query);

            Map repl = (Map) document.get("repl");

            String primary = (String)repl.get("primary");
            String[] primaryParts = primary.split(":");

            String primaryHost = primaryParts[0];
            String primaryPort = primaryParts[1];
            getProject().setNewProperty("mongo.primary.host", primaryHost);
            getProject().setNewProperty("mongo.primary.port", primaryPort);
        } finally {
            mongoClient.close();
        }
    }

    public void setDb(String db) {

        if (db == null || db.length() == 0) {
            throw new BuildException("The 'db' property must be set and cannot be blank");
        }

        // Clean the property
        db = db.replaceAll("(?m)[\"\\s\\[\\]]", "");

        this.db = db;

        // Clear out the old hosts
        hosts.clear();

        String [] hostnames = db.split(",");

        for(String hostname : hostnames) {
            System.out.println(hostname);
            String[] hostParts = hostname.split(":");
            hosts.put(hostParts[0], Integer.parseInt(hostParts[1]));
        }
    }
}
