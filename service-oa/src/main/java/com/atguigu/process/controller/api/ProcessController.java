package com.atguigu.process.controller.api;

import com.atguigu.common.result.Result;
import com.atguigu.model.process.ProcessTemplate;
import com.atguigu.model.process.ProcessType;
import com.atguigu.process.service.OaProcessService;
import com.atguigu.process.service.OaProcessTemplateService;
import com.atguigu.process.service.OaProcessTypeService;
import com.atguigu.vo.process.ProcessFormVo;
import com.atguigu.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private OaProcessTemplateService oaProcessTemplateService;
    @Autowired
    private OaProcessService oaProcessService;


    @ApiOperation(value = "待处理")
    @GetMapping("/findPending/{page}/{limit}")
    public Result findPending(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {
        Page<Process> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> page1 = oaProcessService.findPending(pageParam);
        return Result.ok(page1);
    }

    /**
     * 启动流程
     *
     * @param processFormVo
     * @return
     */
    @ApiOperation(value = "启动流程")
    @PostMapping("/startUp")
    public Result start(@RequestBody ProcessFormVo processFormVo) {
        oaProcessService.startUp(processFormVo);
        return Result.ok();
    }

    /**
     * 获取审批模板
     *
     * @param processTemplateId
     * @return
     */
    @ApiOperation(value = "获取审批模板")
    @GetMapping("getProcessTemplate/{processTemplateId}")
    public Result get(@PathVariable Long processTemplateId) {
        ProcessTemplate processTemplate = oaProcessTemplateService.getById(processTemplateId);
        return Result.ok(processTemplate);
    }

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
