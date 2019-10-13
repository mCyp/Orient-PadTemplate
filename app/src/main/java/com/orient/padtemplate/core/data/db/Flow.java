package com.orient.padtemplate.core.data.db;

import android.graphics.Color;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import com.orient.me.data.ITimeItem;
import com.orient.padtemplate.core.data.dao.DaoSession;
import com.orient.padtemplate.core.data.dao.TaskDao;
import com.orient.padtemplate.core.data.dao.FlowDao;

import java.util.Date;

/**
 * 任务下面的流程
 *
 * Author WangJie
 * Created on 2019/8/5.
 */
@Entity
public class Flow implements ITimeItem {
    @Id
    private String id;
    private String name; // 流程名称
    private String taskId;
    private Date date;
    private String titleColor;
    @ToOne(joinProperty = "taskId")
    private Task task; // 绑定的任务
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 2067950489)
    private transient FlowDao myDao;

    @Generated(hash = 1952844042)
    public Flow() {
    }

    @Generated(hash = 18645818)
    public Flow(String id, String name, String taskId, Date date, String titleColor) {
        this.id = id;
        this.name = name;
        this.taskId = taskId;
        this.date = date;
        this.titleColor = titleColor;
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
    public String getTaskId() {
        return this.taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    @Generated(hash = 1106107757)
    private transient String task__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1528694183)
    public Task getTask() {
        String __key = this.taskId;
        if (task__resolvedKey == null || task__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaskDao targetDao = daoSession.getTaskDao();
            Task taskNew = targetDao.load(__key);
            synchronized (this) {
                task = taskNew;
                task__resolvedKey = __key;
            }
        }
        return task;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 711336367)
    public void setTask(Task task) {
        synchronized (this) {
            this.task = task;
            taskId = task == null ? null : task.getId();
            task__resolvedKey = taskId;
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
    @Generated(hash = 1642419477)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFlowDao() : null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public int getColor() {
        return Color.parseColor(titleColor);
    }

    @Override
    public int getResource() {
        return 0;
    }

    public Date getDate(){
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitleColor() {
        return this.titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

}
