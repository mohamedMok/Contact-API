package com.ebiz.dao.EsDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.ebiz.connection.ESConnection;
import com.ebiz.dao.IUserDao;
import com.ebiz.model.Adress;
import com.ebiz.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

@Repository
public class EsUserDAO implements IUserDao {


	@Override
	public List<User> findAll() {
		List<User> liste = new ArrayList<User>();
		SearchResponse response = ESConnection.getInstance().getDB().prepareSearch().execute().actionGet();
		System.out.println(response.toString());
		for(SearchHit hit : response.getHits()){
			try {
				JSONObject jsonUser = new JSONObject(hit.getSourceAsString());
				User u = new User();
				u.setUsername(jsonUser.getString("username"));
				u.setPassword(jsonUser.getString("password"));
				u.setAge(jsonUser.getInt("age"));
				u.setPhone(jsonUser.getString("phone"));
				u.setMail(jsonUser.getString("mail"));
				u.setAdresses(this.getUserAdresses(hit.getId()));
				liste.add(u);
			}catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return liste;
	}

	@Override
	public int createUser(User newUser) {
		ObjectMapper mapper = new ObjectMapper(); // create once, reuse
		String json;
		IndexRequestBuilder index = null;
		try {
			json = mapper.writeValueAsString(newUser);
			index = ESConnection.getInstance().getDB().prepareIndex("contact", "users");
			index.setSource(json);
			index.execute()
			.actionGet();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(index.get().getId().isEmpty())
			return 0;
		else
			return 1;
	}

	@Override
	public int modifyUser(String id, User user) {
		UpdateRequest updateRequest = null;
		try {
			updateRequest = new UpdateRequest("contact", "users", id)
			.doc(jsonBuilder()
					.startObject()
					.field("username", user.getUsername())
					.field("password", user.getPassword())
					.field("age", user.getAge())
					.field("phone", user.getPhone())
					.field("mail", user.getMail())
					.endObject());
			ESConnection.getInstance().getDB().update(updateRequest).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(updateRequest.id().isEmpty())
			return 0;
		else
			return 1;

	}

	@Override
	public int deleteUser(String idUser) {
		ESConnection.getInstance().getDB().prepareDelete("contact", "users", idUser)
		.execute()
		.actionGet();
		return 1;
	}

	@Override
	public User findOne(String id) {
		GetRequestBuilder getRequestBuilder = ESConnection.getInstance().getDB().prepareGet("contact", "users", id);
		GetResponse hit = getRequestBuilder.execute().actionGet();
		User u = new User();
		try{
			JSONObject source = new JSONObject(hit.getSourceAsString());
			
			u.setUsername(source.getString("username"));
			u.setPassword(source.getString("password"));
			u.setAge(source.getInt("age"));
			u.setPhone(source.getString("phone"));
			u.setMail(source.getString("mail"));
			u.setAdresses(this.getUserAdresses(id));
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return u;
	}

	@Override
	public List<Adress> getUserAdresses(String id) {
		List<Adress> listAdresses = new ArrayList<Adress>();
		SearchRequestBuilder searchRequestBuilder = ESConnection.getInstance().getDB().prepareSearch("contact");
		SearchResponse response = searchRequestBuilder.setTypes("adresses")
				.setQuery(QueryBuilders.hasParentQuery("users",
		                QueryBuilders.termQuery("_id",id)))
				.execute().actionGet();
		System.out.println(response.toString());
		try {
			for(SearchHit hit : response.getHits()){
				JSONObject adress = new JSONObject(hit.getSourceAsString());
				Adress a = new Adress();
				a.setRue(adress.getString("rue"));
				a.setCity(adress.getString("city"));
				a.setCountry(adress.getString("country"));
				listAdresses.add(a);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listAdresses;
	}
	
	@Override
	public int createAdress(Adress a, String idUser) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(a);
			IndexRequestBuilder index = ESConnection.getInstance().getDB().prepareIndex("contact", "adresses");
			index.setParent(idUser);
			index.setSource(json);
			index.execute()
			.actionGet();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}

	@Override
	public int deleteAdress(String idUser, String idAdress) {
		ESConnection.getInstance().getDB().prepareDelete("contact", "adresses", idAdress)
		.execute()
		.actionGet();
		return 1;
	}

	@Override
	public void updateAdress(String id, Adress a) {
		UpdateRequest updateRequest;
		try {
			updateRequest = new UpdateRequest("contact", "adresses", id)
			.doc(jsonBuilder()
					.startObject()
					.field("rue", a.getRue())
					.field("city", a.getCity())
					.field("country", a.getCountry())
					.endObject());
			ESConnection.getInstance().getDB().update(updateRequest).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return 1;
	}

	@Override
	public Adress getUserAdressById(String idAdress, String idUser) {
		// TODO Auto-generated method stub
		return null;
	}

}
