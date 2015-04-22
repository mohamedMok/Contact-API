package com.ebiz.dao.EsDAO.repository;

import com.ebiz.model.User;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Created by ebiz on 11/02/2015.
 */

public interface UserRepository extends ElasticsearchRepository<User, String> {
    List<User> findByUsername(String name);
    List<User> findByAge(int age);
    List<User> findByMail(String mail);
    List<User> findByPhone(String phone);
    List<User> findByAdresses(String rue);
    Page<User> findAll(Pageable pageable);
    User save(User user);
}
