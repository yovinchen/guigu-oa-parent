package com.atguigu.process.service.impl;

import com.atguigu.model.process.ProcessTemplate;
import com.atguigu.model.process.ProcessType;
import com.atguigu.process.mapper.OaProcessTemplateMapper;
import com.atguigu.process.service.OaProcessService;
import com.atguigu.process.service.OaProcessTemplateService;
import com.atguigu.process.service.OaProcessTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 审批模板 服务实现类
 * </p>
 *
 * @author yovinchen
 * @since 2023-06-15
 */
@Service
public class OaProcessTemplateServiceImpl extends ServiceImpl<OaProcessTemplateMapper, ProcessTemplate> implements OaProcessTemplateService {


    @Resource
    private OaProcessTypeService oaprocessTypeService;

    @Autowired
    private OaProcessService oaProcessService;

    /**
     * 分页查询审批模板，把审批类型对应名称查询
     *
     * @param pageInfo
     * @return
     */
    @Override
    public IPage<ProcessTemplate> selectPageProcessTemplate(Page<ProcessTemplate> pageInfo) {
        // 调用mapper的方法实现分页查询
        Page<ProcessTemplate> processTemplatePage = baseMapper.selectPage(pageInfo, null);
        // 第一步分页查询返回分页数据，从分页数据获取列表List集合
        List<ProcessTemplate> processTemplateList = processTemplatePage.getRecords();
        // 遍历list集合
//        for (ProcessTemplate processTemplate : processTemplateList) {
//            //得到每个对象的审批类型id
//            Long processTypeId = processTemplate.getProcessTypeId();
//            // 根据审批类型d，查询获取对应名称
//            ProcessType processType = oaprocessTypeService.getOne(new LambdaQueryWrapper<ProcessType>().eq(ProcessType::getId, processTypeId));
//            if (processType == null) {
//                continue;
//            }
//            // 完成最终封装
//            processTemplate.setProcessTypeName(processType.getName());
//        }
        processTemplateList.forEach(processTemplate -> Optional.ofNullable(oaprocessTypeService.getOne(new LambdaQueryWrapper<ProcessType>().eq(ProcessType::getId, processTemplate.getProcessTypeId()))).ifPresent(processType -> processTemplate.setProcessTypeName(processType.getName())));

        return processTemplatePage;
    }

    /**
     * 发布审批模版
     *
     * @param id
     */
    @Override
    public void publish(Long id) {
        ProcessTemplate processTemplate = baseMapper.selectById(id);
        processTemplate.setStatus(1);
        baseMapper.updateById(processTemplate);

        //流程定义部署
        if (StringUtils.isEmpty(processTemplate.getProcessDefinitionPath())) {
            oaProcessService.deployByZip(processTemplate.getProcessDefinitionPath());
        }
    }
}
