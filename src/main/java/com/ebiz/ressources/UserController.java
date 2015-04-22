package com.ebiz.ressources;

import java.util.ArrayList;
import java.util.List;

import com.ebiz.dao.mongoDAO.MongoUserDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.ebiz.model.Adress;
import com.ebiz.model.User;
import com.wordnik.swagger.annotations.Api;
@RestController
@RequestMapping("/contact/users")
public class UserController {
	//private IUserDao userDao = new UserDao();
	//private IUserDao userDao = new MongoUserDao();
	@Autowired
	private MongoUserDao userDao;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<User>> getUsers(){
		List<User> listeUsers = new ArrayList<User>();
		listeUsers = userDao.findAll();
		if(listeUsers.isEmpty())
			return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<List<User>>(listeUsers, HttpStatus.OK);
	}

	@RequestMapping(value="{id}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<User> getUser(@PathVariable String id){
		User u = userDao.findOne(id);
		if(u == null)
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		else{
			//u.add(detail);
			//u.add(linkTo(methodOn(UserController.class).userDao).withSelfRel());
			return  new ResponseEntity<User>(u, HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> createUser(@RequestBody User u){
		if(u == null)
			return new ResponseEntity<String>("Syntax error", HttpStatus.BAD_REQUEST);
		else{
			int nbRes = userDao.createUser(u);
			if(nbRes < 1)
				return new ResponseEntity<String>("Conflict : Unable to add user", HttpStatus.CONFLICT);
			else	
				return new ResponseEntity<String>(HttpStatus.CREATED);
		}
	}

	@RequestMapping(value="{id}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<String> modifyUser(@PathVariable String id, @RequestBody User u){
		if(u == null)
			return new ResponseEntity<String>("Syntax error", HttpStatus.BAD_REQUEST);
		else{
			int nbRes = userDao.modifyUser(id, u);
			System.out.println(nbRes);
			if(nbRes < 1)
				return new ResponseEntity<String>("Conflict : Unable to modify user", HttpStatus.CONFLICT);	
			else
				return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		}
	}

	@RequestMapping(value ="{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> deleteUser(@PathVariable String id){
		int nbRes = userDao.deleteUser(id);
		if(nbRes < 1){
			return new ResponseEntity<String>("Unable to delete user", HttpStatus.NOT_FOUND);
		}
		else{
			return new ResponseEntity<String>(HttpStatus.OK);
		}

	}


	@RequestMapping(value ="/{idUser}/adresses", method = RequestMethod.GET)
	public ResponseEntity<List<Adress>> getUserAdresses(@PathVariable String idUser){
		List<Adress> listeAdress = userDao.getUserAdresses(idUser);
		if(listeAdress.isEmpty())
			return new ResponseEntity<List<Adress>>(HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<List<Adress>>(listeAdress, HttpStatus.OK);
	}

	@RequestMapping(value ="/{idUser}/adresses/{idAdress}", method = RequestMethod.GET)
	public ResponseEntity<Adress> getUserAdresses(@PathVariable String idUser, @PathVariable String idAdress){
		Adress adress = userDao.getUserAdressById(idAdress,idUser);
		if(adress == null)
			return new ResponseEntity<Adress>(HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<Adress>(adress, HttpStatus.OK);
	}	

	@RequestMapping(value ="/{idUser}/adresses", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> createAdress(@RequestBody Adress a, @PathVariable String idUser){
		if(a == null){
			return new ResponseEntity<String>("Syntax error ", HttpStatus.BAD_REQUEST);
		}
		else{
			int nbRes = userDao.createAdress(a,idUser);
			if(nbRes < 1)
				return new ResponseEntity<String>("Unable to create adress", HttpStatus.CONFLICT);
			else
				return new ResponseEntity<String>(HttpStatus.CREATED);
		}
	}

	@RequestMapping(value ="/{idUser}/adresses/{idAdress}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> deleteAdress(@PathVariable String idUser, @PathVariable String idAdress){
		int nbRes = userDao.deleteAdress(idUser,idAdress);
		if(nbRes < 1)
			return new ResponseEntity<String>("Unable to delete adress", HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<String>(HttpStatus.OK);
	}
}
