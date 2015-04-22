package com.ebiz.connection;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ESConnection {

	private static volatile ESConnection connec;
	private static Client client;

	@SuppressWarnings("resource")
	private ESConnection() {
		System.out.println("Elastic Search Connection");
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", "elasticsearch_ebiz").build();
		client = new TransportClient(settings)
		.addTransportAddress(new InetSocketTransportAddress("127.0.0.1",9301));
	}

	public static ESConnection getInstance(){
		synchronized(ESConnection.class){
			if(connec==null){
				connec = new ESConnection();
			}
		}
		return connec;
	}

	public Client getDB() {
		return client;
	}
}