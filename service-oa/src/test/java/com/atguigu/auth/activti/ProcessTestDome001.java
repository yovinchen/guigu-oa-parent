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
 * ClassName: ProcessTestDome001
 * Package: com.atguigu.auth.activti
 *
 * @author yovinchen
 * @Create 2023/6/13 16:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessTestDome001 {


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
                .addClasspathResource("process/overtime001.bpmn20.xml").name("请假申请流程001").deploy();
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

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("overtime001", map);
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }

    //3 查询个人的代办任务--zhaoliu
    @Test
    public void findTaskList() {

//        String assign = "zhaoliu";
        String assign = "gousheng";
        List<Task> list = taskService.createTaskQuery().taskAssignee(assign).list();
        for (Task task : list) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

//    流程实例id：0b64992c-09ca-11ee-89e6-2aee215b35b7
//    任务id：0b675853-09ca-11ee-89e6-2aee215b35b7
//    任务负责人：gousheng
//    任务名称：General manager approval
//    流程实例id：99e01586-09c9-11ee-b45a-2aee215b35b7
//    任务id：99e2ad9b-09c9-11ee-b45a-2aee215b35b7
//    任务负责人：gousheng
//    任务名称：General manager approval
//    流程实例id：a1ef91c6-09ca-11ee-a1d9-2aee215b35b7
//    任务id：a1f202cd-09ca-11ee-a1d9-2aee215b35b7
//    任务负责人：gousheng
//    任务名称：General manager approval
//    流程实例id：ffa761de-09c9-11ee-a564-2aee215b35b7
//    任务id：ffaa4815-09c9-11ee-a564-2aee215b35b7
//    任务负责人：gousheng
//    任务名称：General manager approval
    /**
     * 删除流程定义
     */
    public void deleteDeployment() {
        //部署id
        String deploymentId = "ffa761de-09c9-11ee-a564-2aee215b35b7";
        //删除流程定义，如果该流程定义已有流程实例启动则删除时出错
        repositoryService.deleteDeployment(deploymentId);
        //设置true 级联删除流程定义，即使该流程有流程实例启动也可以删除，设置为false非级别删除方式
        repositoryService.deleteDeployment(deploymentId, true);
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        Task task = taskService.createTaskQuery()
//                .taskAssignee("zhaoliu")
                .taskAssignee("gousheng")
                .singleResult();//返回一条

        //完成任务,参数：任务id
        taskService.complete(task.getId());
    }
}
