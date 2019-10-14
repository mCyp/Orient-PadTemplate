package com.orient.padtemplate.core.data.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import com.orient.padtemplate.core.data.dao.DaoSession;
import com.orient.padtemplate.core.data.dao.UserDao;
import com.orient.padtemplate.core.data.dao.TroubleDao;

import java.util.Date;

/**
 * 故障记录
 * Author WangJie
 * Created on 2019/8/5.
 */
@Entity
public class Trouble {

    // 注意：
    // 视频和录音只允许有一份，照片路径可以有多张
    @Id
    private String id;
    private String name; // 故障名称
    private String videoPath; // 视频路径
    private double videoTime; // 视频时长
    private String photoDirectory; // 照片的路径
    private String audioPath; // 录音地址
    private double audioTime; // 录音时长
    private String desc; // 描述性
    private String position; // 位置
    private Date createDate; // 创建的日期
    private String userId;
    @ToOne(joinProperty = "userId")
    private User user;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1675525870)
    private transient TroubleDao myDao;
    @Generated(hash = 1867105156)
    private transient String user__resolvedKey;
    
    @Generated(hash = 1123787452)
    public Trouble() {
    }
    @Generated(hash = 1703186249)
    public Trouble(String id, String name, String videoPath, double videoTime,
            String photoDirectory, String audioPath, double audioTime, String desc,
            String position, Date createDate, String userId) {
        this.id = id;
        this.name = name;
        this.videoPath = videoPath;
        this.videoTime = videoTime;
        this.photoDirectory = photoDirectory;
        this.audioPath = audioPath;
        this.audioTime = audioTime;
        this.desc = desc;
        this.position = position;
        this.createDate = createDate;
        this.userId = userId;
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
    public String getVideoPath() {
        return this.videoPath;
    }
    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
    public String getPhotoDirectory() {
        return this.photoDirectory;
    }
    public void setPhotoDirectory(String photoDirectory) {
        this.photoDirectory = photoDirectory;
    }
    public String getAudioPath() {
        return this.audioPath;
    }
    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getPosition() {
        return this.position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
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
    @Generated(hash = 1105003757)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTroubleDao() : null;
    }
    
    public Date getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public double getVideoTime() {
        return this.videoTime;
    }
    public void setVideoTime(double videoTime) {
        this.videoTime = videoTime;
    }
    public double getAudioTime() {
        return this.audioTime;
    }
    public void setAudioTime(double audioTime) {
        this.audioTime = audioTime;
    }

    
}
