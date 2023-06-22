package com.atguigu.process.service;

import com.atguigu.model.process.ProcessType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author yovinchen
 * @since 2023-06-15
 */
public interface OaProcessTypeService extends IService<ProcessType> {

    /**
     * 查询所有审批分类和每一个分类所有审批模版
     *
     * @return
     */
    List<ProcessType> findProcessType();
}
