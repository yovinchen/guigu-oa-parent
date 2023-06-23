package com.atguigu.process.service;

import com.atguigu.model.process.ProcessRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 审批记录 服务类
 * </p>
 *
 * @author yovinchen
 * @since 2023-06-23
 */
public interface OaProcessRecordService extends IService<ProcessRecord> {

    /**
     * 记录操作审批信息记录
     *
     * @param processId
     * @param status
     * @param description
     */
    void record(Long processId, int status, String description);
}
