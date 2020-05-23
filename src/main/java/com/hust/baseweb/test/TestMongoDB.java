package com.hust.baseweb.test;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Iterator;

public class TestMongoDB {

    public static void main(String[] args) {

        System.out.println("Hello World!");
        try {

            MongoClient mongo = new MongoClient("localhost", 27017);

            // Accessing the database
            MongoDatabase database = mongo.getDatabase("vinamilk");
            // Retrieving a collection
            MongoCollection<Document> collection = database
                .getCollection("distance");
            System.out
                .println("Collection sampleCollection selected successfully");

            // Getting the iterable object
            FindIterable<Document> iterDoc = collection.find();
            int i = 1;

            // Getting the iterator
            Iterator it = iterDoc.iterator();

            while (it.hasNext()) {
                System.out.println(it.next());
                i++;

            }
            mongo.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
