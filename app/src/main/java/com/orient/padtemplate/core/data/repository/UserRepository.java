package com.orient.padtemplate.core.data.repository;

import com.orient.padtemplate.core.data.dao.DaoSession;
import com.orient.padtemplate.core.data.dao.UserDao;
import com.orient.padtemplate.core.data.db.User;

import javax.inject.Inject;

/**
 * 用户仓库
 *
 * Author WangJie
 * Created on 2019/7/25.
 */
public class UserRepository {
    private UserDao userDao;

    @Inject
    public UserRepository(DaoSession daoSession) {
        this.userDao = daoSession.getUserDao();
    }

    /**
     * 登录查询
     *
     * @return Wang
     */
    public User login(String account,String password){
        return userDao.queryBuilder()
                .where(UserDao.Properties.Account.eq(account),UserDao.Properties.Password.eq(password))
                .unique();
    }

    /**
     * 插入一个用户
     *
     * @param user Wang
     */
    public void insertUser(User user){
        userDao.insertOrReplace(user);
    }


}
