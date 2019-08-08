package com.orient.padtemplate.core.data.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.orient.padtemplate.core.data.dao.DaoSession;
import com.orient.padtemplate.core.data.dao.UserDao;
import com.orient.padtemplate.core.data.dao.FlowDao;
import com.orient.padtemplate.core.data.dao.TableDao;

/**
 * 表格 通常与用户挂钩
 *
 * Author WangJie
 * Created on 2019/8/5.
 */
@Entity
public class Table {
    // 等待填写
    public static final int STATUS_WAIT = 100;
    // 填写完成 等待上传
    public static final int STATUS_SUBMIT = 200;

    @Id
    private String id;
    private String name; // 表格名称

    // 绑定流程
    private String flowId;
    @ToOne(joinProperty = "flowId")
    private Flow flow;

    // 绑定用户
    private String userId;
    @ToOne(joinProperty = "userId")
    private User user;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1200932703)
    private transient TableDao myDao;
    @Generated(hash = 1566420916)
    public Table(String id, String name, String flowId, String userId) {
        this.id = id;
        this.name = name;
        this.flowId = flowId;
        this.userId = userId;
    }
    @Generated(hash = 752389689)
    public Table() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFlowId() {
        return this.flowId;
    }
    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Generated(hash = 1011065397)
    private transient String flow__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1168115083)
    public Flow getFlow() {
        String __key = this.flowId;
        if (flow__resolvedKey == null || flow__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FlowDao targetDao = daoSession.getFlowDao();
            Flow flowNew = targetDao.load(__key);
            synchronized (this) {
                flow = flowNew;
                flow__resolvedKey = __key;
            }
        }
        return flow;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1073786866)
    public void setFlow(Flow flow) {
        synchronized (this) {
            this.flow = flow;
            flowId = flow == null ? null : flow.getId();
            flow__resolvedKey = flowId;
        }
    }
    @Generated(hash = 1867105156)
    private transient String user__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 538271798)
    public User getUser() {
        String __key = this.userId;
        if (user__resolvedKey == null || user__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
                user__resolvedKey = __key;
            }
        }
        return user;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1065606912)
    public void setUser(User user) {
        synchronized (this) {
            this.user = user;
            userId = user == null ? null : user.getId();
            user__resolvedKey = userId;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1052514995)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTableDao() : null;
    }

}
