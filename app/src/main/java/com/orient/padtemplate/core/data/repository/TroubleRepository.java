package com.orient.padtemplate.core.data.repository;

import com.orient.padtemplate.core.data.dao.DaoSession;
import com.orient.padtemplate.core.data.dao.TroubleDao;
import com.orient.padtemplate.core.data.db.Trouble;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * 故障的本地仓库
 */
public class TroubleRepository extends BaseRepository {

    private TroubleDao troubleDao;

    @Inject
    public TroubleRepository(DaoSession daoSession) {
        this.troubleDao = daoSession.getTroubleDao();
    }

    /********* insert **********/

    /**
     * 插入任务
     */
    public void insertTrouble(Trouble trouble) {
        troubleDao.insertOrReplace(trouble);
    }



    /********* search **********/

    /**
     * 获取所有的故障记录
     * @return Observable<List<Trouble>>
     */
    public Observable<List<Trouble>> searchTroublesByUserId(String userId){
        QueryBuilder<Trouble> queryBuilder = troubleDao.queryBuilder()
                .where(TroubleDao.Properties.UserId.eq(userId));
        return queryListToTx(queryBuilder);
    }

    /**
     * 通过id搜索故障记录
     * @param id 故障id
     * @return Trouble
     */
    public Trouble searchTroubleById(String id){
        QueryBuilder<Trouble> queryBuilder = troubleDao.queryBuilder()
                .where(TroubleDao.Properties.Id.eq(id));
        return queryBuilder.unique();
    }
}
