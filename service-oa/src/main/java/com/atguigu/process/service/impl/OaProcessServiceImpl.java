package com.atguigu.process.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.model.process.Process;
import com.atguigu.model.process.ProcessTemplate;
import com.atguigu.model.system.SysUser;
import com.atguigu.process.mapper.OaProcessMapper;
import com.atguigu.process.service.OaProcessRecordService;
import com.atguigu.process.service.OaProcessService;
import com.atguigu.process.service.OaProcessTemplateService;
import com.atguigu.security.custom.LoginUserInfoHelper;
import com.atguigu.vo.process.ProcessFormVo;
import com.atguigu.vo.process.ProcessQueryVo;
import com.atguigu.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author yovinchen
 * @since 2023-06-20
 */
@Service
public class OaProcessServiceImpl extends ServiceImpl<OaProcessMapper, Process> implements OaProcessService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private OaProcessTemplateService oaProcessTemplateService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private OaProcessRecordService oaProcessRecordService;

    @Autowired
    private HistoryService historyService;


    /**
     * 获取分页列表
     *
     * @param pageParam
     * @param processQueryVo
     * @return
     */
//审批管理列表
    @Override
    public IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo) {
        IPage<ProcessVo> pageModel = baseMapper.selectPage(pageParam, processQueryVo);
        return pageModel;
    }

    /**
     * 部署流程定义
     *
     * @param deployPath
     */
    @Override
    public void deployByZip(String deployPath) {
        // 定义zip输入流
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(deployPath);
        ZipInputStream zipInputStream = null;
        if (inputStream != null) {
            zipInputStream = new ZipInputStream(inputStream);
        }
        // 流程部署
        Deployment deployment = repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();

        System.out.println("deployment.getId() = " + deployment.getId());
        System.out.println("deployment.getName() = " + deployment.getName());
    }

    /**
     * 启动流程
     *
     * @param processFormVo
     */
    @Override
    public void startUp(ProcessFormVo processFormVo) {
        // 1 根据当前用户id获取用户信息
        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());

        // 2 根据审批模板id把模板信息查询
        ProcessTemplate processTemplate = oaProcessTemplateService.getById(processFormVo.getProcessTemplateId());

        // 3 保存提交审批信息到业务表，oa_process
        Process process = new Process();
        // processFormVo复制到process对象里面
        BeanUtils.copyProperties(processFormVo, process);
        // 其他值
        process.setStatus(1); //审批中
        String workNo = System.currentTimeMillis() + "";
        process.setProcessCode(workNo);
        process.setUserId(LoginUserInfoHelper.getUserId());
        process.setFormValues(processFormVo.getFormValues());
        process.setTitle(sysUser.getName() + "发起" + processTemplate.getName() + "申请");
        baseMapper.insert(process);


        // 4 启动流程实例 - RuntimeService
        // 4.1 流程定义key
        String processDefinitionKey = processTemplate.getProcessDefinitionKey();

        //4.2 业务key  processId
        String businessKey = String.valueOf(process.getId());

        //4.3 流程参数 form表单json数据，转换map集合
        String formValues = processFormVo.getFormValues();
        JSONObject jsonObject = JSON.parseObject(formValues);
        JSONObject formData = jsonObject.getJSONObject("formData");
        // 遍历formData得到的内容，封装map集合
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        Map<String, Object> variable = new HashMap<>();
        variable.put("data", map);

        // 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variable);

        // 5 查询下一个审批人
        // 审批人可能多个
        List<Task> taskList = this.getCurrentTaskList(processInstance.getId());
        List<String> nameList = new ArrayList<>();
        for (Task task : taskList) {
            String assigneeName = task.getAssignee();
            SysUser user = sysUserService.getUserByUsername(assigneeName);
            String name = user.getName();
            nameList.add(name);

            // TODO 6 推送消息
        }
        process.setProcessInstanceId(processInstance.getId());
        process.setDescription("等待" + StringUtils.join(nameList.toArray(), ",") + "审批");
        // 7 业务和流程关联  更新oa_process数据
        baseMapper.updateById(process);

        // 记录操作审批信息记录
        oaProcessRecordService.record(process.getId(), 1, "发起申请");
    }

    /**
     * 查询待处理的列表
     *
     * @param pageParam
     * @return
     */
    @Override
    public IPage<ProcessVo> findPending(Page<java.lang.Process> pageParam) {

        //1 封装查询条件，根据当前登录的用户名称
        TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(LoginUserInfoHelper.getUsername()).orderByTaskCreateTime().desc();

        //2 调用方法分页条件查询，返回list集合，待办任务集合
        //listPage方法有两个参数
        //第一个参数：开始位置  第二个参数：每页显示记录数
        int begin = (int) ((pageParam.getCurrent() - 1) * pageParam.getSize());
        int size = (int) pageParam.getSize();
        List<Task> taskList = taskQuery.listPage(begin, size);
        long totalCount = taskQuery.count();

        //3 封装返回list集合数据 到 List<ProcessVo>里面
        //List<Task> -- List<ProcessVo>
        List<ProcessVo> processVoList = new ArrayList<>();
        for (Task task : taskList) {

            //从task获取流程实例id
            String processInstanceId = task.getProcessInstanceId();
            //根据流程实例id获取实例对象
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            //从流程实例对象获取业务key---processId
            Long businessKey = Long.valueOf(processInstance.getBusinessKey());
            if (businessKey == null) {
                continue;
            }
            //根据业务key获取Process对象
            Process process = baseMapper.selectById(businessKey);

            //Process对象 复制 ProcessVo对象
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVo.setTaskId(task.getId());

            //放到最终list集合processVoList
            processVoList.add(processVo);
        }
        //4 封装返回IPage对象
        IPage<ProcessVo> page = new Page<>(pageParam.getCurrent(), pageParam.getSize(), totalCount);
        page.setRecords(processVoList);
        return page;
    }

    /**
     * 获取当前任务列表
     *
     * @param id
     * @return
     */
    private List<Task> getCurrentTaskList(String id) {
        return taskService.createTaskQuery().processInstanceId(id).list();
    }

}
