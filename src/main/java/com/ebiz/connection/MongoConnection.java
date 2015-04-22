package com.ebiz.connection;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public final class MongoConnection {
	private static volatile MongoConnection connec;
	private static DB db;

	private MongoConnection() {
		MongoClient mongoClient;
		try {
			System.out.println("Mongo connection");
			mongoClient = new MongoClient();
			DB dbase = mongoClient.getDB("contact");
			db = dbase;
		} catch (UnknownHostException e) {
			System.out.println("Fail to connect to db");
		}
	}

	public static MongoConnection getInstance(){
		synchronized(MongoConnection.class){
			if(connec==null){
				connec = new MongoConnection();
			}
		}
		return connec;
	}

	public DB getDB() {
		return db;
	}
}
