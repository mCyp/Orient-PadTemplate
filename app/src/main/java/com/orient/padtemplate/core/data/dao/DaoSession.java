package com.orient.padtemplate.core.data.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.orient.padtemplate.core.data.db.Cell;
import com.orient.padtemplate.core.data.db.Flow;
import com.orient.padtemplate.core.data.db.Table;
import com.orient.padtemplate.core.data.db.Task;
import com.orient.padtemplate.core.data.db.User;
import com.orient.padtemplate.core.data.db.Trouble;

import com.orient.padtemplate.core.data.dao.CellDao;
import com.orient.padtemplate.core.data.dao.FlowDao;
import com.orient.padtemplate.core.data.dao.TableDao;
import com.orient.padtemplate.core.data.dao.TaskDao;
import com.orient.padtemplate.core.data.dao.UserDao;
import com.orient.padtemplate.core.data.dao.TroubleDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig cellDaoConfig;
    private final DaoConfig flowDaoConfig;
    private final DaoConfig tableDaoConfig;
    private final DaoConfig taskDaoConfig;
    private final DaoConfig userDaoConfig;
    private final DaoConfig troubleDaoConfig;

    private final CellDao cellDao;
    private final FlowDao flowDao;
    private final TableDao tableDao;
    private final TaskDao taskDao;
    private final UserDao userDao;
    private final TroubleDao troubleDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        cellDaoConfig = daoConfigMap.get(CellDao.class).clone();
        cellDaoConfig.initIdentityScope(type);

        flowDaoConfig = daoConfigMap.get(FlowDao.class).clone();
        flowDaoConfig.initIdentityScope(type);

        tableDaoConfig = daoConfigMap.get(TableDao.class).clone();
        tableDaoConfig.initIdentityScope(type);

        taskDaoConfig = daoConfigMap.get(TaskDao.class).clone();
        taskDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        troubleDaoConfig = daoConfigMap.get(TroubleDao.class).clone();
        troubleDaoConfig.initIdentityScope(type);

        cellDao = new CellDao(cellDaoConfig, this);
        flowDao = new FlowDao(flowDaoConfig, this);
        tableDao = new TableDao(tableDaoConfig, this);
        taskDao = new TaskDao(taskDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);
        troubleDao = new TroubleDao(troubleDaoConfig, this);

        registerDao(Cell.class, cellDao);
        registerDao(Flow.class, flowDao);
        registerDao(Table.class, tableDao);
        registerDao(Task.class, taskDao);
        registerDao(User.class, userDao);
        registerDao(Trouble.class, troubleDao);
    }
    
    public void clear() {
        cellDaoConfig.clearIdentityScope();
        flowDaoConfig.clearIdentityScope();
        tableDaoConfig.clearIdentityScope();
        taskDaoConfig.clearIdentityScope();
        userDaoConfig.clearIdentityScope();
        troubleDaoConfig.clearIdentityScope();
    }

    public CellDao getCellDao() {
        return cellDao;
    }

    public FlowDao getFlowDao() {
        return flowDao;
    }

    public TableDao getTableDao() {
        return tableDao;
    }

    public TaskDao getTaskDao() {
        return taskDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public TroubleDao getTroubleDao() {
        return troubleDao;
    }

}
