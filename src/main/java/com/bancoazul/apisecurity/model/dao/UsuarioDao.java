package com.bancoazul.apisecurity.model.dao;

import com.bancoazul.apisecurity.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UsuarioDao extends CrudRepository<User, Integer> {
  User findByUsername (String username);

}
