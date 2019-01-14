package com.eason.orm.test.dao;

import com.eason.orm.core.Configuration;
import com.eason.orm.core.Session;
import com.eason.orm.test.entity.User;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Eason
 * @createTime 2019-01-14 22:22
 * @description
 */
public class UserDaoTest {

    private Configuration config;

    @Before
    public void init() {
        //1. 创建ORMConfig对象
        config = new Configuration();
    }

    @Test
    public void testSave() throws Exception {
        //2. 创建ORMSession对象
        Session session = config.buildSession();
        //3. 创建实体类对象并保存
        User user = new User();
        user.setId("11");
        user.setUsername("阿里巴巴");
        user.setName("Eason");
        user.setPassword("123456");
        user.setPhone("17602154321");
        session.save(user);
        //4. 释放资源
        session.close();
    }

    @Test
    public void testFindOne() throws Exception {
        //2. 创建ORMSession对象
        Session session = config.buildSession();
        //3. 创建实体类对象并查询
        User user = (User) session.findOne(User.class, "11");
        System.out.println(user);
        //4. 释放资源
        session.close();
    }

    @Test
    public void testDelete() throws Exception {
        //2. 创建ORMSession对象
        Session session = config.buildSession();

        //3. 创建实体类对象并删除
        User user = new User();
        user.setId("11");
        session.delete(user);

        //4. 释放资源
        session.close();
    }
}
