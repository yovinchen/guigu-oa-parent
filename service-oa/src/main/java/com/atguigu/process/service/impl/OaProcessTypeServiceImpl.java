package com.atguigu.process.service.impl;

import com.atguigu.model.process.ProcessType;
import com.atguigu.process.mapper.OaProcessTypeMapper;
import com.atguigu.process.service.OaProcessTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author yovinchen
 * @since 2023-06-15
 */
@Service
public class OaProcessTypeServiceImpl extends ServiceImpl<OaProcessTypeMapper, ProcessType> implements OaProcessTypeService {

    /**
     * 查询所有审批分类和每一个分类所有审批模版
     *
     * @return
     */
    @Override
    public List<ProcessType> findProcessType() {



        return null;
    }
}
