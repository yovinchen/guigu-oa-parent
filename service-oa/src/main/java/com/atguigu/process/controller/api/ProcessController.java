package com.atguigu.process.controller.api;

import com.atguigu.common.result.Result;
import com.atguigu.model.process.ProcessType;
import com.atguigu.process.service.OaProcessTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: ProcessController
 * Package: com.atguigu.process.controller.api
 *
 * @author yovinchen
 * @Create 2023/6/22 18:24
 */

@Api(tags = "审批流管理")
@RestController
@RequestMapping(value = "/admin/process")
@CrossOrigin//跨域
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProcessController {

    @Autowired
    private OaProcessTypeService oaProcessTypeService;


    /**
     * 查询所有审批分类和每一个分类所有审批模版
     *
     * @return
     */
    @ApiOperation(value = "获取全部审批分类及模板")
    @GetMapping("findProcessType")
    public Result findProcessType() {
        List<ProcessType> list = oaProcessTypeService.findProcessType();
        return Result.ok(list);
    }


}
