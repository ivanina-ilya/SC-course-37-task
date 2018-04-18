package org.ivanina.course.spring37.cinema.dao;

import org.ivanina.course.spring37.cinema.config.ApplicationSpringConfig;
import org.ivanina.course.spring37.cinema.config.JdbcConfig;
import org.ivanina.course.spring37.cinema.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationSpringConfig.class, JdbcConfig.class})
public class UserDaoTest {
    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;

    @Test
    public void getAllTest(){
        Set<User> users = userDao.getAll();
        assertNotNull(users);
        assertTrue(users.size() > 0);
    }

    @Test
    public void saveInsertTest() {
        User user = new User("FirstTest","LastTest", "test.user.01@test.com");
        Long id = userDao.save(user);

        assertNotNull(id);
        assertEquals(id, user.getId());

        userDao.remove(user);
    }

    @Test
    public void saveUpdateTest() {
        String newEmail = "test.user.01.NEW@test.com";
        User user = new User("FirstName","LastName", "test.user.01@test.com");
        Long id = userDao.save(user);

        user.setEmail(newEmail);
        Long idSame = userDao.save(user);

        assertNotNull(idSame);
        assertEquals(id, idSame);
        assertEquals(idSame, user.getId());
        assertEquals(newEmail, user.getEmail());

        User userFromDb = userDao.get(id);
        assertEquals(newEmail, userFromDb.getEmail());
        assertEquals(idSame, userFromDb.getId());

        userDao.remove(user);
    }

    @Test
    public void removeTest() {
        User user = new User("FirstTest","LastTest", "test.user.01@test.com");
        Long id = userDao.save(user);

        assertNotNull(id);

        userDao.remove(user);

        assertNull(user.getId());
        assertNull( userDao.get(id) );
    }

    @Test
    public void getWrongTest() {
        assertNull( userDao.get(-1L) );
    }

    @Test
    public void getByEmailTest(){
        String email = "test.user.02@test-email.com";
        User user = new User("FirstName","LastName", email);
        Long id = userDao.save(user);

        User userFromDb = userDao.getByEmail(email);

        assertEquals(user.getEmail(),userFromDb.getEmail());

        String newEmail = userFromDb.getEmail() + ".ua";
        userFromDb.setEmail( newEmail );
        Long id2 = userDao.save(userFromDb);

        assertEquals(id, id2);
        User userFromDb2 = userDao.getByEmail(newEmail);

        assertEquals(id, userFromDb2.getId());

        userDao.remove(user);
    }


}
