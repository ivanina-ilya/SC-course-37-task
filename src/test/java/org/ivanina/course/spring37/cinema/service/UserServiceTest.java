package org.ivanina.course.spring37.cinema.service;

import org.ivanina.course.spring37.cinema.config.JdbcConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.ivanina.course.spring37.cinema.config.ApplicationSpringConfig;
import org.ivanina.course.spring37.cinema.domain.User;


import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationSpringConfig.class, JdbcConfig.class})
public class UserServiceTest {

    @Autowired
    @Qualifier("userService")
    private UserService userService;


    @Before
    public void init(){
    }

    @Test
    public void addUserTest(){
        User user = new User("John","test", "test@test.com");
        int cnt = userService.getAll().size();
        userService.save(user);
        assertEquals(cnt+1, userService.getAll().size());
        assertEquals( user, userService.getUserByEmail( user.getEmail() ));
    }

    @Test
    public void removeUserTest(){
        User user = userService.getAll().stream().findFirst().get();
        int cnt = userService.getAll().size();
        userService.remove(user);
        assertEquals(cnt-1, userService.getAll().size());
        assertNull( userService.getUserByEmail( user.getEmail() ));
    }

    @Test(expected = DuplicateKeyException.class)
    public void addDuplicateUserTest(){
        User user = new User("John","Duplicate", "testDuplicate@test.com");
        userService.save(user);
        User userDuplicate = new User("John","Duplicate", "testDuplicate@test.com");
        userService.save(userDuplicate);
    }
}
