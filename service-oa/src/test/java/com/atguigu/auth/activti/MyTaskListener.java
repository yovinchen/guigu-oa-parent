package com.atguigu.auth.activti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * ClassName: MyTaskListener
 * Package: com.atguigu.auth.activti
 *
 * @author yovinchen
 * @Create 2023/6/12 15:12
 */
public class MyTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        if (delegateTask.getName().equals("经理审批")) {
            //这里指定任务负责人
            delegateTask.setAssignee("Jack1");
        } else if (delegateTask.getName().equals("人事审批")) {
            //这里指定任务负责人
            delegateTask.setAssignee("Jack2");
        }
    }
}
