package com.orient.padtemplate.contract.presenter;

import android.content.SharedPreferences;
import android.widget.Toast;

import com.orient.padtemplate.base.contract.presenter.BasePresenter;
import com.orient.padtemplate.base.rx.BaseObserver;
import com.orient.padtemplate.common.Common;
import com.orient.padtemplate.contract.view.LoginView;
import com.orient.padtemplate.core.data.db.Flow;
import com.orient.padtemplate.core.data.db.Table;
import com.orient.padtemplate.core.data.db.Task;
import com.orient.padtemplate.core.data.db.User;
import com.orient.padtemplate.core.data.model.FlowModel;
import com.orient.padtemplate.core.data.model.TableModel;
import com.orient.padtemplate.core.data.model.TaskModel;
import com.orient.padtemplate.core.data.repository.TaskRepository;
import com.orient.padtemplate.core.data.repository.UserRepository;
import com.orient.padtemplate.utils.AppPrefUtils;
import com.orient.padtemplate.utils.FileUtils;
import com.orient.padtemplate.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Author WangJie
 * Created on 2019/7/25.
 */
@SuppressWarnings("WeakerAccess")
public class LoginPresenter extends BasePresenter<LoginView> {

    @Inject
    UserRepository userRepository;

    @Inject
    TaskRepository taskRepository;

    @Inject
    public LoginPresenter() {
    }

    /**
     * 登录
     *
     * @param account  账号
     * @param password 密码
     */
    public void login(String account, String password) {
        // TODO
        // 完善正常的登录写法


        User user = userRepository.login(account, password);
        if (user != null) {
            Toast.makeText(mContext, "登录处理中...", Toast.LENGTH_SHORT).show();
            mView.onLoginResult(true);
        }
    }

    public void saveUser(User user) {
        userRepository.insertUser(user);
    }

    /**
     * 第一次初始化数据
     */
    public void onFirstInit() {
        new RxUtils<>(createFirstInitObservable())
                .execute(mLifecycleProvider, new BaseObserver<Boolean>(mView) {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);

                        mView.onLoginResult(aBoolean);
                    }
                });
    }

    /**
     * 发射第一次初始化的数据
     */
    @SuppressWarnings("ConstantConditions")
    private Observable<Boolean> createFirstInitObservable() {
        // 构建用户
        User user = new User("1", "jie", "wang", "123456");
        userRepository.insertUser(user);

        AppPrefUtils.putString(Common.Constant.SP_USER_ID,"1");

        // 初始化任务
        TaskModel taskModel = FileUtils.copyTaskModel(mContext);
        Task task = taskModel.toTask();
        taskRepository.insertTask(task);
        List<FlowModel> flowModels = taskModel.getFlowModels();
        for (FlowModel flowModel : flowModels) {
            Flow flow = flowModel.toFlow(task.getId());
            taskRepository.insertFlow(flow);
            List<TableModel> tableModels = flowModel.getTableModels();
            if (tableModels != null)
                for (TableModel tableModel : tableModels) {
                    Table table = tableModel.toTable(flow.getId(), user.getId());
                    taskRepository.insertTable(table);
                }
        }

        // 初始化结束
        return Observable.just(true);
    }


}
