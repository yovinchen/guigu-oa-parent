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
 * ClassName: ProcessTestDome002
 * Package: com.atguigu.auth.activti
 *
 * @author yovinchen
 * @Create 2023/6/13 16:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessTestDome002 {


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
     * 1.部署流程定义
     */
    @Test
    public void deployProcess() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/leave001.bpmn20.xml").name("请假申请流程002").deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    /**
     * 2.启动流程实例
     */
    @Test
    public void startProcessInstance() {
        Map<String, Object> map = new HashMap<>();
        //设置请假天数
        map.put("day", "3");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave001", map);
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }

    //3 查询个人的代办任务--zhaoliu
    @Test
    public void findTaskList() {

//        String assign = "wang5";

//        流程实例id：851c5a11-09cc-11ee-a8fc-2aee215b35b7
//        任务id：85200399-09cc-11ee-a8fc-2aee215b35b7
//        任务负责人：wang5
//        任务名称：Department manager approval

//        String assign = "gouwa";

//        流程实例id：851c5a11-09cc-11ee-a8fc-2aee215b35b7
//        任务id：8520039b-09cc-11ee-a8fc-2aee215b35b7

        String assign = "xiaoli";

//        流程实例id：851c5a11-09cc-11ee-a8fc-2aee215b35b7
//        任务id：d3756b55-09cc-11ee-9240-2aee215b35b7
//        任务负责人：xiaoli
//        任务名称：Personnel record

        List<Task> list = taskService.createTaskQuery().taskAssignee(assign).list();
        for (Task task : list) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        Task task = taskService.createTaskQuery()
                .taskAssignee("wang5")
                .taskAssignee("gouwa")
                .singleResult();//返回一条

        //完成任务,参数：任务id
        taskService.complete(task.getId());
    }
}
