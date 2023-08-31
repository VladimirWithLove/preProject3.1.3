package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao{

    @PersistenceContext
    private EntityManager entityManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserDaoImpl(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void add(User user) {
        User userFromDB = findByUserName(user.getName());
        if (userFromDB != null) {
            return;
        }
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        entityManager.persist(user);
    }

    @Override
    public List<User> getAllUsers() {
        TypedQuery<User> query = entityManager.createQuery("from User", User.class);
        return query.getResultList();
    }

    @Override
    public User getUser(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void deleteUser(long id) {
        Query query = entityManager.createQuery("delete from User where id = :userid");
        query.setParameter("userid", id);
        query.executeUpdate();
    }

    @Override
    public void updateUser(long id, User user) {
        Query query = entityManager.createQuery("update User set login= :newLogin, name= :newName, password= :newPassword, surname= :newSurname, " +
                "age= :newAge, profession= :newProfession where id= :userId");
        query.setParameter("newLogin", user.getLogin());
        query.setParameter("newName", user.getName());
        query.setParameter("newPassword", bCryptPasswordEncoder.encode(user.getPassword()));
        query.setParameter("newSurname", user.getSurname());
        query.setParameter("newAge", user.getAge());
        query.setParameter("newProfession", user.getProfession());
        query.setParameter("userId", id);

        query.executeUpdate();
    }

    @Override
    public User findByUserName(String name) {
        TypedQuery<User> query = entityManager.createQuery("from User where name =: username", User.class);
        query.setParameter("username", name);
        User user;
        try {
            user = query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
        return user;
    }
}
