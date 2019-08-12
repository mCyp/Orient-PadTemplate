package com.orient.padtemplate.core.data.repository;

import com.orient.padtemplate.core.data.dao.CellDao;
import com.orient.padtemplate.core.data.dao.DaoSession;
import com.orient.padtemplate.core.data.dao.FlowDao;
import com.orient.padtemplate.core.data.dao.TableDao;
import com.orient.padtemplate.core.data.dao.TaskDao;
import com.orient.padtemplate.core.data.db.Cell;
import com.orient.padtemplate.core.data.db.Flow;
import com.orient.padtemplate.core.data.db.Table;
import com.orient.padtemplate.core.data.db.Task;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * 任务仓库
 *
 * Author WangJie
 * Created on 2019/8/8.
 */
@SuppressWarnings("ALL")
public class TaskRepository extends BaseRepository{
    private TaskDao taskDao;
    private FlowDao flowDao;
    private TableDao tableDao;
    private CellDao cellDao;

    @Inject
    public TaskRepository(DaoSession daoSession) {
        this.taskDao = daoSession.getTaskDao();
        this.flowDao = daoSession.getFlowDao();
        this.tableDao = daoSession.getTableDao();
        this.cellDao = daoSession.getCellDao();
    }

    /********* Insert **********/

    /**
     * 插入任务
     */
    public void insertTask(Task task){
        taskDao.insertOrReplace(task);
    }

    /**
     * 插入流程
     */
    public void insertFlow(Flow flow){
        flowDao.insertOrReplace(flow);
    }

    /**
     * 插入表格
     */
    public void insertTable(Table table){
        tableDao.insertOrReplace(table);
    }

    /**
     * 插入一个单元格
     */
    public void insertCell(Cell cell){
        cellDao.insertOrReplace(cell);
    }

    /********* search **********/

    /**
     * 通过流程Id和用户Id查询表格
     */
    public Observable<List<Table>> searchTableByFlowIdAndUserIdRx(String flowId, String userId){
        QueryBuilder<Table> queryBuilder = tableDao.queryBuilder()
                .where(TableDao.Properties.UserId.eq(userId), TableDao.Properties.FlowId.eq(flowId));
        return queryListToTx(queryBuilder);
    }

    /**
     * 通过任务Id查询流程
     */
    public Observable<List<Flow>> searchFlowByFlowIdAndUserIdRx(String taskId){
        QueryBuilder<Flow> queryBuilder = flowDao.queryBuilder()
                .where(FlowDao.Properties.TaskId.eq(taskId));
        return queryListToTx(queryBuilder);
    }
}
