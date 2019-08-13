package com.orient.padtemplate.core.data.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.orient.padtemplate.core.data.dao.DaoSession;
import com.orient.padtemplate.core.data.dao.TableDao;
import com.orient.padtemplate.core.data.dao.CellDao;

/**
 * 单元格
 * <p>
 * Author WangJie
 * Created on 2019/8/12.
 */
@Entity
public class Cell {
    // 描述、编辑、检查、日期、选择和照片
    public static final int CELL_DESC = 1;
    public static final int CELL_EDIT = 2;
    public static final int CELL_CHECK = 3;
    public static final int CELL_DATE = 4;
    public static final int CELL_SELECTION = 5;
    public static final int CELL_PHOTO = 6;

    @Id
    private String id;
    // 行
    private int row;
    // 列
    private int col;
    // 单元格类型
    private int type;
    // 单元格名称
    private String labelName;
    // 单元格的值
    private String inputValue;
    // 是否是标题
    private boolean isTitle;
    // 照片路径
    private String path;
    // 表格Id 外键
    private String tableId;
    @ToOne(joinProperty = "tableId")
    private Table table;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1701490087)
    private transient CellDao myDao;
    @Generated(hash = 1170138811)
    public Cell(String id, int row, int col, int type, String labelName,
            String inputValue, boolean isTitle, String path, String tableId) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.type = type;
        this.labelName = labelName;
        this.inputValue = inputValue;
        this.isTitle = isTitle;
        this.path = path;
        this.tableId = tableId;
    }
    @Generated(hash = 739260143)
    public Cell() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getRow() {
        return this.row;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public int getCol() {
        return this.col;
    }
    public void setCol(int col) {
        this.col = col;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getLabelName() {
        return this.labelName;
    }
    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }
    public String getInputValue() {
        return this.inputValue;
    }
    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }
    public boolean getIsTitle() {
        return this.isTitle;
    }
    public void setIsTitle(boolean isTitle) {
        this.isTitle = isTitle;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getTableId() {
        return this.tableId;
    }
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
    @Generated(hash = 579284854)
    private transient String table__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2086210944)
    public Table getTable() {
        String __key = this.tableId;
        if (table__resolvedKey == null || table__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TableDao targetDao = daoSession.getTableDao();
            Table tableNew = targetDao.load(__key);
            synchronized (this) {
                table = tableNew;
                table__resolvedKey = __key;
            }
        }
        return table;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1666285429)
    public void setTable(Table table) {
        synchronized (this) {
            this.table = table;
            tableId = table == null ? null : table.getId();
            table__resolvedKey = tableId;
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
    @Generated(hash = 1167289043)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCellDao() : null;
    }


}
