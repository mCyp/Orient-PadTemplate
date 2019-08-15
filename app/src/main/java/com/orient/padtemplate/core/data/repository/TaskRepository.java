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
 * <p>
 * Author WangJie
 * Created on 2019/8/8.
 */
@SuppressWarnings("ALL")
public class TaskRepository extends BaseRepository {
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
    public void insertTask(Task task) {
        taskDao.insertOrReplace(task);
    }

    /**
     * 插入流程
     */
    public void insertFlow(Flow flow) {
        flowDao.insertOrReplace(flow);
    }

    /**
     * 插入表格
     */
    public void insertTable(Table table) {
        tableDao.insertOrReplace(table);
    }

    /**
     * 插入一个单元格
     */
    public void insertCell(Cell cell) {
        cellDao.insertOrReplace(cell);
    }

    /********* search **********/

    /**
     * 通过流程Id和用户Id查询表格
     */
    public Observable<List<Table>> searchTableByFlowIdAndUserIdRx(String flowId, String userId) {
        QueryBuilder<Table> queryBuilder = tableDao.queryBuilder()
                .where(TableDao.Properties.UserId.eq(userId), TableDao.Properties.FlowId.eq(flowId));
        return queryListToTx(queryBuilder);
    }

    /**
     * 通过任务Id查询流程
     */
    public Observable<List<Flow>> searchFlowByFlowIdAndUserIdRx(String taskId) {
        QueryBuilder<Flow> queryBuilder = flowDao.queryBuilder()
                .where(FlowDao.Properties.TaskId.eq(taskId));
        return queryListToTx(queryBuilder);
    }

    /**
     * 根据表格获取单元格
     */
    public List<Cell> searchCellsByTableId(String tableId, boolean isTitle) {
        QueryBuilder<Cell> queryBuilder = cellDao.queryBuilder()
                .where(CellDao.Properties.TableId.eq(tableId), CellDao.Properties.IsTitle.eq(isTitle))
                .orderAsc(CellDao.Properties.Row, CellDao.Properties.Col);
        return queryBuilder.list();
    }

    /**
     * 获取初始加载的数量
     */
    public List<Cell> searchCellsByTableId(String tableId, int limitSize) {
        QueryBuilder<Cell> queryBuilder = cellDao.queryBuilder()
                .where(CellDao.Properties.TableId.eq(tableId), CellDao.Properties.IsTitle.eq(false))
                .orderAsc(CellDao.Properties.Row, CellDao.Properties.Col)
                .limit(limitSize);
        return queryBuilder.list();
    }

    /**
     * 加载指定行数的Cell
     */
    public List<Cell> searchCellByTableInRange(String tableId, int startPage, int endPage) {
        QueryBuilder<Cell> queryBuilder = cellDao.queryBuilder()
                .where(CellDao.Properties.TableId.eq(tableId), CellDao.Properties.Row.between(startPage, endPage))
                .orderAsc(CellDao.Properties.Row, CellDao.Properties.Col);
        return queryBuilder.list();
    }

    /**
     * 加载指定行数的Cell
     */
    public Observable<List<Cell>> searchCellByTableInRangeRx(String tableId, int startPage, int endPage) {
        QueryBuilder<Cell> queryBuilder = cellDao.queryBuilder()
                .where(CellDao.Properties.TableId.eq(tableId), CellDao.Properties.Row.between(startPage, endPage))
                .orderAsc(CellDao.Properties.Row, CellDao.Properties.Col);
        return queryListToTx(queryBuilder);
    }

    /**
     * 查询指定表格的列的数量
     */
    public long countTableCol(String tableId) {
        QueryBuilder<Cell> queryBuilder = cellDao.queryBuilder()
                .where(CellDao.Properties.TableId.eq(tableId), CellDao.Properties.IsTitle.eq(true));
        return queryBuilder.count();
    }
}
