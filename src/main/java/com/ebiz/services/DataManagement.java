package com.ebiz.services;

import com.ebiz.connection.ESConnection;
import com.ebiz.connection.MongoConnection;
import com.ebiz.dao.mongoDAO.MongoUserDao;
import com.ebiz.model.Adress;
import com.ebiz.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;


import static org.elasticsearch.common.xcontent.XContentFactory.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by ebiz on 05/02/2015.
 */
public class DataManagement {
    private Client esConnection;
    private DB mongoConnection;

    public DataManagement(){
        esConnection = ESConnection.getInstance().getDB();
        mongoConnection = MongoConnection.getInstance().getDB();
    }

    public void run() throws IOException {
        //Creation un index avec mapping
        String newIndexName = "contact_"+ System.currentTimeMillis();
        String oldIndexName = "";
        final CreateIndexRequestBuilder createIndexRequestBuilder = esConnection.admin().indices().prepareCreate(newIndexName);

        // MAPPING GOES HERE
        String uri =
                "http://localhost:9201/" + newIndexName;
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(uri);
        StringEntity params = new StringEntity("{\n" +
                "    \"mappings\": {\n" +
                "        \"users\": {\n" +
                "            \"properties\": {\n" +
                "                \"username\": {\n" +
                "                    \"type\": \"string\"\n" +
                "                },\n" +
                "                \"password\": {\n" +
                "                    \"type\": \"string\"\n" +
                "                },\n" +
                "                \"age\": {\n" +
                "                    \"type\": \"string\"\n" +
                "                },\n" +
                "                \"phone\": {\n" +
                "                    \"type\": \"string\"\n" +
                "                },\n" +
                "                \"mail\": {\n" +
                "                    \"type\": \"string\"\n" +
                "                },\n" +
                "                \"adresses\": {\n" +
                "                    \"type\": \"nested\",\n" +
                "                    \"include_in_parent\": true,\n" +
                "                    \"properties\": {\n" +
                "                        \"id\": {\n" +
                "                            \"type\": \"string\"\n" +
                "                        },\n" +
                "                        \"idUser\": {\n" +
                "                            \"type\": \"string\"\n" +
                "                        },\n" +
                "                        \"rue\": {\n" +
                "                            \"type\": \"string\"\n" +
                "                        },\n" +
                "                        \"city\": {\n" +
                "                            \"type\": \"string\"\n" +
                "                        },\n" +
                "                        \"country\": {\n" +
                "                            \"type\": \"string\"\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");
        params.setContentType("application/json");

        post.setEntity(params);
        HttpResponse response = client.execute(post);

        //System.out.println(response);

        // Get index link with this alias
        //String aliasName = ...
       // client.admin().cluster().state(new ClusterStateRequest()).actionGet().getState().getMetaData().aliases().get(aliasName);

        GetAliasesRequest gat = new GetAliasesRequest("contact");
        System.out.println("Retrieve index aliases");
        GetAliasesResponse getAliasesRequestBuilder = esConnection.admin().indices().getAliases(gat).actionGet();
        oldIndexName = getAliasesRequestBuilder.getAliases().keys().toArray()[0].toString();
        System.out.println("oldIndex " + getAliasesRequestBuilder.getAliases().keys().toArray()[0]);

        // Create link between new Index and the alias and  Delete old link between old index and the alias
        esConnection.admin().indices().prepareAliases().addAlias(newIndexName, "contact").removeAlias(oldIndexName, "contact").execute().actionGet();


        // Delete old index
        // sConnection.admin().indices().flush(new FlushRequest(oldIndexName)).actionGet();
       // esConnection.admin().indices().prepareFlush(oldIndexName).execute().actionGet();
        esConnection.admin().indices().delete(new DeleteIndexRequest(oldIndexName)).actionGet();


        List<User> userlist = this.getAllFromMongo();
        this.preparebulk(userlist);
    }


    public List<User> getAllFromMongo(){
        MongoUserDao dao = new MongoUserDao();
        return dao.findAll();
    }

    public void preparebulk(List<User> userList) throws IOException {
        // BULK
        //BulkRequestBuilder bulkRequest = esConnection.prepareBulk();
        System.out.println("RX launch");
        for(int i =0; i < 960; i++)
            userList.add(userList.get(0));
        long time = System.currentTimeMillis();

        RXServices rx = new RXServices();
        rx.defineObservable(userList);
        System.out.println("Time :  " + (System.currentTimeMillis() -  time) );


/*
        for(User u : userList ){
            IndexRequestBuilder index = null;
            index = esConnection.prepareIndex("contact", "users");
            index.setSource(jsonBuilder()
                            .startObject()
                            .field("username", u.getUsername())
                            .field("password", u.getPassword())
                            .field("age", u.getAge())
                            .field("phone", u.getPhone())
                            .field("mail", u.getMail())
                            .endObject()
            );
            index.execute().actionGet();

            List<Adress> listAdress = u.getAdresses();

            for(Adress a : u.getAdresses()){
                a.setId(index.get().getId());
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(a);

                //System.out.println("Begin adress" + a.toString());
                esConnection
                        .prepareUpdate("contact", "users", index.get().getId())
                        .addScriptParam("adress", json)
                        .setScript("if (ctx._source.adresses == null) { ctx._source.adresses = [adress] } else { ctx._source.adresses.add(adress) }", null)
                        .execute().actionGet();

            }
        }
        System.out.println("Size " + (System.currentTimeMillis() -  time) );*/
    }
}