package com.atguigu.process.controller;


import com.atguigu.common.result.Result;
import com.atguigu.process.service.OaProcessService;
import com.atguigu.vo.process.ProcessQueryVo;
import com.atguigu.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 审批类型 前端控制器
 * </p>
 *
 * @author yovinchen
 * @since 2023-06-20
 */
@Api(tags = "审批流管理")
@RestController
@RequestMapping(value = "/admin/process")
@CrossOrigin//跨域
//@SuppressWarnings({"unchecked", "rawtypes"})
public class OaProcessController {

    @Autowired
    private OaProcessService oaprocessService;

    /**
     * 获取分页列表
     *
     * @param page
     * @param limit
     * @param processQueryVo
     * @return
     */
    @PreAuthorize("hasAuthority('bnt.process.list')")
    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@ApiParam(name = "page", value = "当前页码", required = true) @PathVariable Long page,

                        @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable Long limit,

                        @ApiParam(name = "processQueryVo", value = "查询对象", required = false) ProcessQueryVo processQueryVo) {
        Page<ProcessVo> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> pageModel = oaprocessService.selectPage(pageParam, processQueryVo);
        return Result.ok(pageModel);
    }

}

