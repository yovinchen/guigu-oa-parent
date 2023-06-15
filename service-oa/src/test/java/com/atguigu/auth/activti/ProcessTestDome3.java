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
 * ClassName: ProcessTestDome3
 * Package: com.atguigu.auth.activti
 *
 * @author yovinchen
 * @Create 2023/6/12 20:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessTestDome3 {


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
     * 1.部署流程定义和启动流程实例
     */
    @Test
    public void deployProcess03() {
        // 流程部署
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("process/overtime04.bpmn20.xml").name("加班申请流程04").deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());

        Map<String, Object> map = new HashMap<>();
//        map.put("assignee1", "Lucy03");
//        map.put("assignee2", "Mack01");
        //创建流程实例,我们需要知道流程定义的key
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("overtime04", map);
        //输出实例的相关信息
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
    }

    /**
     * 2.查询组任务
     */
    @Test
    public void findGroupTaskList() {
        List<Task> list = taskService.createTaskQuery()
                .taskCandidateUser("tom01")
                .list();
        for (Task task : list) {
            System.out.println("----------------------------");
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    /**
     * 3.拾取组任务
     */
    @Test
    public void claimTask() {
        //拾取任务,即使该用户不是候选人也能拾取(建议拾取时校验是否有资格)
        //校验该用户有没有拾取任务的资格
        Task task = taskService.createTaskQuery()
                .taskCandidateUser("tom01")//根据候选人查询
                .singleResult();
        if (task != null) {
            //拾取任务
            taskService.claim(task.getId(), "tom01");
            System.out.println("任务拾取成功");
        }
    }

    /**
     * 4.查询个人代办任务
     */
    @Test
    public void findGroupPendingTaskList() {
        //任务负责人
        String assignee = "tom01";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assignee)//只查询该任务负责人的任务
                .list();
        for (Task task : list) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    /**
     * 5.办理个人任务
     */
    @Test
    public void completGroupTask() {
        Task task = taskService.createTaskQuery()
                .taskAssignee("tom01")  //要查询的负责人
                .singleResult();//返回一条
        taskService.complete(task.getId());
    }
}
