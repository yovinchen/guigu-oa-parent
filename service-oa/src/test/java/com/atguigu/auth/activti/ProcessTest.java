package com.atguigu.auth.activti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * ClassName: ProcessTest
 * Package: com.atguigu.auth.activti
 *
 * @author yovinchen
 * @Create 2023/6/11 20:03
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessTest {

    /**
     * 流程定义
     */
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 流程实例
     */
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 流程任务
     */
    @Autowired
    private TaskService taskService;

    /**
     * 相关操作历史
     */
    @Autowired
    private HistoryService historyService;

    /**
     * 单个流程挂起
     */
    @Test
    public void SingleSuspendProcessInstance() {
        String processInstanceId = "ce1f3cc0-08cf-11ee-b8cb-e645a9a03302";
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        //获取到当前流程定义是否为暂停状态   suspended方法为true代表为暂停   false就是运行的
        boolean suspended = processInstance.isSuspended();
        if (suspended) {
            runtimeService.activateProcessInstanceById(processInstanceId);
            System.out.println("流程实例:" + processInstanceId + "激活");
        } else {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println("流程实例:" + processInstanceId + "挂起");
        }
    }

    /**
     * 全部流程实例挂起
     */
    @Test
    public void suspendProcessInstance() {
        ProcessDefinition leave = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave").singleResult();
        // 获取到当前流程定义是否为暂停状态 suspended方法为true是暂停的，suspended方法为false是运行的
        boolean suspended = leave.isSuspended();
        if (suspended) {
            // 暂定,那就可以激活
            // 参数1:流程定义的id  参数2:是否激活    参数3:时间点
            repositoryService.activateProcessDefinitionById(leave.getId(), true, null);
            System.out.println("流程定义:" + leave.getId() + "激活");
        } else {
            repositoryService.suspendProcessDefinitionById(leave.getId(), true, null);
            System.out.println("流程定义:" + leave.getId() + "挂起");
        }
    }

    /**
     * 启动流程实例，添加businessKey
     */
    @Test
    public void startUpProcessAddBusinessKey() {
        String businessKey = "1";
        // 启动流程实例，指定业务标识businessKey，也就是请假申请单id
        ProcessInstance processInstance = runtimeService.
                startProcessInstanceByKey("leave", businessKey);
        // 输出
        System.out.println("业务id:" + processInstance.getBusinessKey());
    }

    /**
     * 删除流程定义
     */
    public void deleteDeployment() {
        //部署id
        String deploymentId = "ce1f3cc0-08cf-11ee-b8cb-e645a9a03302";
        //删除流程定义，如果该流程定义已有流程实例启动则删除时出错
        repositoryService.deleteDeployment(deploymentId);
        //设置true 级联删除流程定义，即使该流程有流程实例启动也可以删除，设置为false非级别删除方式
        repositoryService.deleteDeployment(deploymentId, true);
    }

    /**
     * 查询流程定义
     */
    @Test
    public void findProcessDefinitionList() {
        List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        //输出流程定义信息
        for (ProcessDefinition processDefinition : definitionList) {
            System.out.println("流程定义 id= " + processDefinition.getId());
            System.out.println("流程定义 name= " + processDefinition.getName());
            System.out.println("流程定义 key= " + processDefinition.getKey());
            System.out.println("流程定义 Version= " + processDefinition.getVersion());
            System.out.println("流程部署ID = " + processDefinition.getDeploymentId());
        }
    }

    /**
     * 查询已处理历史任务
     */
    @Test
    public void findProcessedTaskList() {
        //张三已处理过的历史任务
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().taskAssignee("zhangsan").finished().list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println("流程实例id：" + historicTaskInstance.getProcessInstanceId());
            System.out.println("任务id：" + historicTaskInstance.getId());
            System.out.println("任务负责人：" + historicTaskInstance.getAssignee());
            System.out.println("任务名称：" + historicTaskInstance.getName());
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void completTask() {
        //要查询的负责人
        Task task = taskService.createTaskQuery().taskAssignee("zhangsan").singleResult();//返回一条

        //完成任务,参数：任务id
        taskService.complete(task.getId());
    }

    /**
     * 查询当前个人待执行的任务 zhangsan
     */
    @Test
    public void findPendingTaskList() {
        //任务负责人
        String assignee = "zhangsan";
        List<Task> list = taskService.createTaskQuery().taskAssignee(assignee)//只查询该任务负责人的任务
                .list();
        for (Task task : list) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startUpProcess() {
        //创建流程实例,我们需要知道流程定义的key
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave");
        //输出实例的相关信息
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
        System.out.println("当前活动Id：" + processInstance.getActivityId());
    }

    /**
     * 单个文件部署
     */
    @Test
    public void deployProcess() {
        // 流程部署
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("process/leave.bpmn20.xml").addClasspathResource("process/leave.png").name("请假申请流程").deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }
}
