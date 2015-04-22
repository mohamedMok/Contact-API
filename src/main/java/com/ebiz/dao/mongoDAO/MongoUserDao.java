package com.ebiz.dao.mongoDAO;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.ebiz.connection.MongoConnection;
import com.ebiz.dao.IUserDao;
import com.ebiz.model.Adress;
import com.ebiz.model.User;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

@Repository
public class MongoUserDao implements IUserDao{
	DBCollection collection;

	public  MongoUserDao() {
		this.collection = MongoConnection.getInstance().getDB()
				.getCollection("contact");
	}

	@Override
	public List<User> findAll() {
		List<User> listUsers = new ArrayList<User>();
		DBCursor cursor = this.collection.find();
		while (cursor.hasNext()) {
			System.out.println("Find documents : ");
			DBObject dboject = cursor.next();
			User u = new User();
			u.setUsername(dboject.get("username").toString());
			u.setPassword(dboject.get("password").toString());
			u.setAge((Integer) dboject.get("age"));
			u.setPhone(dboject.get("phone").toString());
			u.setMail(dboject.get("mail").toString());
			BasicDBList adresses = (BasicDBList) dboject.get("adresses");
			if(adresses != null){
				List<Adress> listAdresses = new ArrayList<Adress>();
				for(Object adress : adresses ){
					Adress a = new Adress();
					a.setId((String) ((DBObject) adress).get("id"));
					a.setRue(((DBObject) adress).get("rue").toString());
					a.setCity(((DBObject) adress).get("city").toString());
					a.setCountry(((DBObject) adress).get("country").toString());
					listAdresses.add(a);
					System.out.println("Begin adress" + a.toString());
				}
				u.setAdresses(listAdresses);
			}
			listUsers.add(u);
		}
		return listUsers;
	}

	@Override
	public int createUser(User newUser) {
		BasicDBObject user = new BasicDBObject();
		user.put("username", newUser.getUsername());
		user.put("password", newUser.getPassword());
		user.put("age", newUser.getAge());
		user.put("phone", newUser.getPhone());
		user.put("mail", newUser.getMail());
		//user.put("adresses", newUser.getAdresses());

		WriteResult res = this.collection.insert(user);
		return res.getN();
	}


	@Override
	public User findOne(String id) {
		BasicDBObject query= new BasicDBObject();
		query.put("_id", new ObjectId(id));
		System.out.println(id);
		DBObject dboject = this.collection.findOne(query);
		User u = new User();
		u.setUsername(dboject.get("username").toString());
		u.setPassword(dboject.get("password").toString());
		u.setAge((Integer) dboject.get("age"));
		u.setPhone(dboject.get("phone").toString());
		u.setMail(dboject.get("mail").toString());
		BasicDBList adresses = (BasicDBList) dboject.get("adresses");
		if(adresses != null){
			List<Adress> listAdresses = new ArrayList<Adress>();
			for(Object adress : adresses ){
				Adress a = new Adress();
				//a.setId((Integer) ((DBObject) adress).get("id"));
				a.setRue(((DBObject) adress).get("rue").toString());
				a.setCity(((DBObject) adress).get("city").toString());
				a.setCountry(((DBObject) adress).get("country").toString());
				listAdresses.add(a);
			}
			u.setAdresses(listAdresses);
		}
		return u;
	}

	@Override
	public int deleteUser(String idUser) {
		BasicDBObject query = new BasicDBObject();
		query.append("_id", new ObjectId(idUser));
		WriteResult res = this.collection.remove(query);
		return res.getN();
	}

	@Override
	public int modifyUser(String id, User user) {
		BasicDBObject newUser = new BasicDBObject();
		newUser.append("user", user.getUsername());
		newUser.append("password", user.getPassword());
		newUser.append("age", user.getAge());
		newUser.append("phone",user.getPhone());
		newUser.append("mail", user.getMail());

		BasicDBObject query = new BasicDBObject().append("_id", id);
		WriteResult res = this.collection.update(query, newUser);
		return res.getN();
	}

	@Override
	public int createAdress(Adress a, String idUser) {
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(idUser));
		DBObject dbUser = this.collection.findOne(query);
		DBObject newAdress = new BasicDBObject();
		newAdress.put("_id", new ObjectId());
		newAdress.put("rue", a.getRue());
		newAdress.put("city", a.getCity());
		newAdress.put("country", a.getCountry());

		DBObject listItem = new BasicDBObject("adresses", newAdress);
		DBObject updateQuery = new BasicDBObject("$push", listItem);
		WriteResult res = this.collection.update(dbUser, updateQuery);
		return res.getN();
	}

	@Override
	public List<Adress> getUserAdresses(String id) {
		BasicDBObject query= new BasicDBObject();
		query.put("_id", new ObjectId(id));
		DBObject dboject = this.collection.findOne(query);
		BasicDBList adresses = (BasicDBList) dboject.get("adresses");
		List<Adress> listAdresses = new ArrayList<Adress>();
		if(adresses != null){
			for(Object adress : adresses ){
				Adress a = new Adress();
				//a.setId((Integer) ((DBObject) adress).get("id"));
				a.setRue(((DBObject) adress).get("rue").toString());
				a.setCity(((DBObject) adress).get("city").toString());
				a.setCountry(((DBObject) adress).get("country").toString());
				listAdresses.add(a);
			}
		}
		return listAdresses;
	}

	@Override
	public Adress getUserAdressById(String idAdress, String idUser) {
		BasicDBObject eleMatch = new BasicDBObject();
		eleMatch.put("_id",new ObjectId(idAdress));
		BasicDBObject up = new BasicDBObject();
		up.put("$elemMatch",eleMatch);
		BasicDBObject sec = new BasicDBObject();
		sec.put("adresses", up);

		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(idUser));
		System.out.println(query.toString());
		DBObject object = this.collection.findOne(query, sec);
		BasicDBList adresses = (BasicDBList) object.get("adresses");
		
		DBObject adr = (DBObject) adresses.get(0);
		//System.out.println(adr.get("rue"));
		Adress a = new Adress();
		a.setRue(adr.get("rue").toString());
		a.setCity(adr.get("city").toString());	
		a.setCountry(adr.get("country").toString());

		return a;
	}

	@Override
	public int deleteAdress(String idUser, String idAdress) {

		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(idUser));
		
		BasicDBObject adressObject = new BasicDBObject();
		adressObject.put("_id",new ObjectId(idAdress));
		BasicDBObject pullObject = new BasicDBObject();
		pullObject.put("adresses", adressObject);
		
		DBObject updateQuery = new BasicDBObject("$pull", pullObject);
		WriteResult res = this.collection.update(query, updateQuery);
		return res.getN();
	}

	@Override
	public void updateAdress(String id, Adress a) {
		BasicDBObject eleMatch = new BasicDBObject();
		eleMatch.put("_id",new ObjectId(id));
		BasicDBObject up = new BasicDBObject();
		up.put("$elemMatch",eleMatch);
		BasicDBObject sec = new BasicDBObject();
		sec.put("adresses", up);

		BasicDBObject query = new BasicDBObject();
		//query.put("_id", new ObjectId(idUser));
		System.out.println(query.toString());
		DBObject object = this.collection.findOne(query, sec);
		BasicDBList adresses = (BasicDBList) object.get("adresses");
		
		DBObject adr = (DBObject) adresses.get(0);
		adr.put("rue", a.getRue());
		adr.put("city", a.getCity());
		adr.put("country", a.getCountry());

		//WriteResult res = this.collection.save(adr);
	}


}
