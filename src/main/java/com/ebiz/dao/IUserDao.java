package com.ebiz.dao;

import java.util.List;

import com.ebiz.model.Adress;
import com.ebiz.model.User;

public interface IUserDao {

	List<User> findAll();
	int createUser(User newUser);
	int modifyUser(String id, User user);
	int deleteUser(String idUser) ;
	User findOne(String id);
	List<Adress> getUserAdresses(String id);
	int createAdress(Adress a, String idUser);
	int deleteAdress(String idUser, String idAdress);
	void updateAdress(String id, Adress a);
	Adress getUserAdressById(String idAdress, String idUser);
}
