package com.atguigu.process.service;

import com.atguigu.model.process.Process;
import com.atguigu.vo.process.ApprovalVo;
import com.atguigu.vo.process.ProcessFormVo;
import com.atguigu.vo.process.ProcessQueryVo;
import com.atguigu.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author yovinchen
 * @since 2023-06-20
 */
public interface OaProcessService extends IService<Process> {
    /**
     * 获取分页列表
     *
     * @param pageParam
     * @param processQueryVo
     * @return
     */

    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo);

    /**
     * 部署流程定义
     */
    void deployByZip(String deployPath);

    /**
     * 启动流程
     *
     * @param processFormVo
     */
    void startUp(ProcessFormVo processFormVo);

    /**
     * 查询待处理的任务列表
     *
     * @param pageParam
     * @return
     */
    IPage<ProcessVo> findPending(Page<Process> pageParam);

    /**
     * 查看审批详情信息
     *
     * @param id
     * @return
     */
    Map<String, Object> show(Long id);

    /**
     * 审批
     *
     * @param approvalVo
     */
    void approve(ApprovalVo approvalVo);

    /**
     * 分页查询已处理
     *
     * @param pageParam
     * @return
     */
    IPage<ProcessVo> findProcessed(Page<Process> pageParam);

    /**
     * 分页查询已发起
     *
     * @param pageParam
     * @return
     */
    IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam);
}
