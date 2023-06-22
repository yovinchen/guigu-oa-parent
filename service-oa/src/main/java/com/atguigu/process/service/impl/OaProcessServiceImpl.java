package com.atguigu.process.service.impl;

import com.atguigu.model.process.Process;
import com.atguigu.process.mapper.OaProcessMapper;
import com.atguigu.process.service.OaProcessService;
import com.atguigu.vo.process.ProcessQueryVo;
import com.atguigu.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
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

}
