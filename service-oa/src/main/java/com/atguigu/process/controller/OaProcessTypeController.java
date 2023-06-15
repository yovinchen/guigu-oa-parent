package com.atguigu.process.controller;


import com.atguigu.common.result.Result;
import com.atguigu.model.process.ProcessType;
import com.atguigu.process.service.OaProcessTypeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 审批类型 前端控制器
 *
 * @author yovinchen
 * @since 2023-06-15
 */
@Api(value = "审批类型", tags = "审批类型")
@RestController
@RequestMapping(value = "/admin/process/processType")
//@SuppressWarnings({"unchecked", "rawtypes"})
public class OaProcessTypeController {

    @Autowired
    private OaProcessTypeService processTypeService;

    /**
     * 查询所有的审批类型
     *
     * @return
     */
    @ApiOperation(value = "获取全部审批分类")
    @GetMapping("findAll")
    public Result findAll() {
        return Result.ok(processTypeService.list());
    }

    /**
     * 分页查询 审批类型
     *
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{pageSize}")
    public Result index(@PathVariable Long page, @PathVariable Long pageSize) {

        Page<ProcessType> pageInfo = new Page<>(page, pageSize);
        Page<ProcessType> pageModel = processTypeService.page(pageInfo);

        return Result.ok(pageModel);
    }

    /**
     * 获取审批类型
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('bnt.processType.list')")
    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        ProcessType processType = processTypeService.getById(id);
        return Result.ok(processType);
    }

    /**
     * 新增审批类型
     *
     * @param processType
     * @return
     */
    @PreAuthorize("hasAuthority('bnt.processType.add')")
    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody ProcessType processType) {
        processTypeService.save(processType);
        return Result.ok();
    }

    /**
     * 修改审批类型
     *
     * @param processType
     * @return
     */
    @PreAuthorize("hasAuthority('bnt.processType.update')")
    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody ProcessType processType) {
        processTypeService.updateById(processType);
        return Result.ok();
    }

    /**
     * 删除审批类型
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        processTypeService.removeById(id);
        return Result.ok();
    }

}

