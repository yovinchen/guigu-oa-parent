package com.atguigu.auth.activti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: ProcessTestDome2
 * Package: com.atguigu.auth.activti
 *
 * @author yovinchen
 * @Create 2023/6/12 20:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessTestDome2 {


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

//    /**
//     * 部署流程定义
//     */
//    @Test
//    public void deployProcess02() {
//        Deployment deploy = repositoryService.createDeployment().addClasspathResource("process/overtime.bpmn20.xml").name("加班申请流程02").deploy();
//        System.out.println("deploy.getId() = " + deploy.getId());
//        System.out.println("deploy.getName() = " + deploy.getName());
//    }
//
//    /**
//     * 启动流程实例
//     */
//    @Test
//    public void startProcessInstance02() {
//        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("overtime02");
//        System.out.println("processInstance.getProcessDefinitionId() = " + processInstance.getProcessDefinitionId());
//        System.out.println("processInstance.getId() = " + processInstance.getId());
//    }
//
//
//    /**
//     * 查询个人的代办任务--Jack1
//     */
//    @Test
//    public void findTaskList02() {
//        String assign = "Jack1";
//        List<Task> list = taskService.createTaskQuery().taskAssignee(assign).list();
//        for (Task task : list) {
//            System.out.println("task.getProcessInstanceId() = " + task.getProcessInstanceId());
//            System.out.println("任务id：" + task.getId());
//            System.out.println("任务负责人：" + task.getAssignee());
//            System.out.println("任务名称：" + task.getName());
//        }
//    }
//
////-----------------------uel-method---------------------------------
//
//    /**
//     * 部署流程定义
//     */
//    @Test
//    public void deployProcess01() {
//        // 流程部署
//        Deployment deploy = repositoryService.createDeployment()
//                .addClasspathResource("process/overtime01.bpmn20.xml")
//                .name("加班申请流程01")
//                .deploy();
//        System.out.println(deploy.getId());
//        System.out.println(deploy.getName());
//    }
//
//    /**
//     * 启动流程实例
//     */
//    @Test
//    public void startUpProcess02() {
//        //创建流程实例,我们需要知道流程定义的key
//        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("overtime01");
//        //输出实例的相关信息
//        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
//        System.out.println("流程实例id：" + processInstance.getId());
//    }
//
//    /**
//     * 查询个人的代办任务--Mac
//     */
//    @Test
//    public void findTaskList01() {
//        String assign = "Mac";
//        List<Task> list = taskService.createTaskQuery()
//                .taskAssignee(assign).list();
//        for (Task task : list) {
//            System.out.println("task.getProcessInstanceId() = " + task.getProcessInstanceId()); // abb9c7c4-bb20-11ed-b608-005056c00001
//            System.out.println("任务id：" + task.getId()); // abbd4a38-bb20-11ed-b608-005056c00001
//            System.out.println("任务负责人：" + task.getAssignee()); // LiLei
//            System.out.println("任务名称：" + task.getName()); // 经理审批
//        }
//    }
//
////---------------------------------uel-value---------------------------------------

    /**
     * 部署流程定义
     */
    @Test
    public void deployProcess() {
        // 流程部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/overtime.bpmn20.xml")
                .name("加班申请流程")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startUpProcess() {
        Map<String, Object> map = new HashMap<>();
        map.put("assignee1", "Lucy01");
        map.put("assignee2", "Mack01");
        //创建流程实例,我们需要知道流程定义的key
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("overtime", map);
        //输出实例的相关信息
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
    }


    /**
     * 查询当前个人待执行的任务
     */
    @Test
    public void findPendingTaskList() {
        //任务负责人
        String assignee = "Lucy01";
        List<Task> list = taskService.createTaskQuery().taskAssignee(assignee)//只查询该任务负责人的任务
                .list();
        for (Task task : list) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }
//---------------------------------------------------------------------------------

    /**
     * 部署流程定义
     */
    @Test
    public void deployProcess03() {
        // 流程部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/overtime.bpmn20.xml")
                .name("加班申请流程")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startUpProcess03() {
        Map<String, Object> map = new HashMap<>();
        map.put("assignee1", "Lucy03");
//        map.put("assignee2", "Mack01");
        //创建流程实例,我们需要知道流程定义的key
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("overtime", map);
        //输出实例的相关信息
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
    }


    @Test
    public void completTask() {
        Task task = taskService.createTaskQuery()
                .taskAssignee("Lucy03")  //要查询的负责人
                .singleResult();//返回一条

        Map<String, Object> variables = new HashMap<>();
        variables.put("assignee2", "zhao");
        //完成任务,参数：任务id
        taskService.complete(task.getId(), variables);
    }

    /**
     * 查询当前个人待执行的任务
     * <p>
     * 流程实例id：245a58e7-0923-11ee-8637-b60fbe4e0138
     * 任务id：29c9f30a-0923-11ee-abdf-b60fbe4e0138
     * 任务负责人：zhao
     * 任务名称：Personnel approval
     */
    @Test
    public void findPendingTaskList03() {
        //任务负责人
        String assignee = "zhao";
        List<Task> list = taskService.createTaskQuery().taskAssignee(assignee)//只查询该任务负责人的任务
                .list();
        for (Task task : list) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }


}
