package com.atguigu.process.controller.api;

import com.atguigu.auth.service.SysUserService;
import com.atguigu.common.result.Result;
import com.atguigu.model.process.Process;
import com.atguigu.model.process.ProcessTemplate;
import com.atguigu.model.process.ProcessType;
import com.atguigu.process.service.OaProcessService;
import com.atguigu.process.service.OaProcessTemplateService;
import com.atguigu.process.service.OaProcessTypeService;
import com.atguigu.vo.process.ApprovalVo;
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
import java.util.Map;

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
//@SuppressWarnings({"unchecked", "rawtypes"})
public class ProcessController {

    @Autowired
    private OaProcessTypeService oaProcessTypeService;

    @Autowired
    private OaProcessTemplateService oaProcessTemplateService;

    @Autowired
    private OaProcessService oaProcessService;



    @ApiOperation(value = "待处理")
    @GetMapping("/findPending/{page}/{limit}")
    public Result findPending(@ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Long page,

                              @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit) {
        Page<Process> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> pageModel = oaProcessService.findPending(pageParam);
        return Result.ok(pageModel);
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

    /**
     * 查看审批详情信息
     *
     * @param id
     * @return
     */
    @GetMapping("show/{id}")
    public Result show(@PathVariable Long id) {
        Map<String, Object> map = oaProcessService.show(id);
        return Result.ok(map);
    }

    /**
     * 审批
     *
     * @param approvalVo
     * @return
     */
    @ApiOperation(value = "审批")
    @PostMapping("approve")
    public Result approve(@RequestBody ApprovalVo approvalVo) {
        oaProcessService.approve(approvalVo);
        return Result.ok();
    }

    /**
     * 分页查询已处理
     *
     * @param page
     * @param limit
     * @return
     */
    @ApiOperation(value = "已处理")
    @GetMapping("/findProcessed/{page}/{limit}")
    public Result findProcessed(@ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Long page, @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit) {
        Page<Process> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> pageModel = oaProcessService.findProcessed(pageParam);
        return Result.ok(pageModel);
    }

    /**
     * 分页查询已发起
     *
     * @param page
     * @param limit
     * @return
     */
    @ApiOperation(value = "已发起")
    @GetMapping("/findStarted/{page}/{limit}")
    public Result findStarted(@ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Long page, @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit) {
        Page<ProcessVo> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> pageModel = oaProcessService.findStarted(pageParam);
        return Result.ok(pageModel);
    }
}
