package com.atguigu.process.service;

/**
 * @author YoVinchen
 * @date 2023/6/25 下午 9:41
 */
public interface MessageService {

    /**
     * 推送待审批人员
     *
     * @param processId
     * @param userId
     * @param taskId
     */
    void pushPendingMessage(Long processId, Long userId, String taskId);

    /**
     * 审批后推送提交审批人员
     *
     * @param processId
     * @param userId
     * @param status
     */
    void pushProcessedMessage(Long processId, Long userId, Integer status);

}
