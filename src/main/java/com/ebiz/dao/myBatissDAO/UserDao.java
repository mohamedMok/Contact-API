package com.ebiz.dao.myBatissDAO;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.ebiz.connection.MyBatisConnection;
import com.ebiz.dao.IUserDao;
import com.ebiz.model.Adress;
import com.ebiz.model.User;

@Repository
public class UserDao implements IUserDao{
	

	@Override
	public List<User> findAll(){
		SqlSession session = MyBatisConnection.getSession();
		List<User> listeUser = session.selectList("com.ebiz.dao.myBatiss.UserDao.findAllWithAdresses");
		session.close(); 
		return listeUser;
	}

	@Override
	public int createUser(User newUser) {
		SqlSession session = MyBatisConnection.getSession();
		int nbInsert = session.insert("com.ebiz.dao.myBatiss.UserDao.insert", newUser);
		session.commit();
		session.close();
		return nbInsert;
	}

	@Override
	public int modifyUser(String id, User user) {
		SqlSession session = MyBatisConnection.getSession();
		
		User newUser = session.selectOne("com.ebiz.dao.myBatiss.UserDao.findOne", id);
		newUser.setUsername(user.getUsername());
		newUser.setPassword(user.getPassword());
		newUser.setAge(user.getAge());
		newUser.setPhone(user.getPhone());
		newUser.setMail(user.getMail());

		int nbInsert = session.insert("com.ebiz.dao.myBatiss.UserDao.update", newUser);
		session.commit();
		session.close();
		return nbInsert;
	}

	@Override
	public int deleteUser(String idUser) {
		User u = this.findOne(idUser);
		SqlSession session = MyBatisConnection.getSession();
		int nbDelete = session.delete("com.ebiz.dao.myBatiss.UserDao.delete", u);
		session.commit();
		session.close();
		return nbDelete;
	}

	@Override
	public User findOne(String id) {
		int idUser = Integer.parseInt(id);
		SqlSession session = MyBatisConnection.getSession();
		User user = session.selectOne("com.ebiz.dao.myBatiss.UserDao.findOne", idUser);
		session.close(); 
		return user;
	}
	
	@Override
	public List<Adress> getUserAdresses(String id) {
		SqlSession session = MyBatisConnection.getSession();
		List<Adress> listeAdress = session.selectList("com.ebiz.dao.myBatiss.AdressDao.findAll", id);
		session.close(); 
		return listeAdress;
	}

	@Override
	public int createAdress(Adress a, String idUser) {
		SqlSession session = MyBatisConnection.getSession();
		a.setId(idUser);
		int nbInsert = session.insert("com.ebiz.dao.myBatiss.AdressDao.insert", a);
		session.commit();
		session.close();
		return nbInsert;
	}
	
	@Override
	public int deleteAdress(String idUser, String idAdress) {
		Adress adress = this.getUserAdressById(idAdress, idUser);
		SqlSession session = MyBatisConnection.getSession();
		int nbDelete = session.delete("com.ebiz.dao.myBatiss.AdressDao.delete", adress);
		session.commit();
		session.close();
		return nbDelete;
	}
	
	@Override
	public void updateAdress(String id, Adress a) {
		SqlSession session = MyBatisConnection.getSession();
		
		Adress newAdress = session.selectOne("com.ebiz.dao.myBatiss.AdressDao.findOne", id);
		newAdress.setRue(a.getRue());
		newAdress.setCity(a.getCity());
		newAdress.setCountry(a.getCountry());

		session.update("com.ebiz.dao.myBatiss.UserDao.update", newAdress);
		session.commit();
		session.close();
	}

	@Override
	public Adress getUserAdressById(String id, String idUser) {
		Adress a = new Adress();
		a.setId(id);
		a.setIdUser(Integer.parseInt(idUser));
		SqlSession session = MyBatisConnection.getSession();
		Adress adress = session.selectOne("com.ebiz.dao.myBatiss.AdressDao.findOne", a);
		session.close(); 
		return adress;
	}
}
