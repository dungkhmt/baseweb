package com.hust.baseweb.test;

import java.util.Iterator;

import org.bson.Document;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
public class TestMongoDB {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World!");
		try {
			// TODO Auto-generated method stub
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
