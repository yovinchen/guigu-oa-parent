package com.atguigu.process;

import com.atguigu.common.result.Result;
import com.atguigu.model.process.ProcessTemplate;
import com.atguigu.model.system.SysRole;
import com.atguigu.process.service.OaProcessTemplateService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * ClassName: Test
 * Package: com.atguigu.process
 *
 * @author yovinchen
 * @Create 2023/6/15 18:32
 */
@SpringBootTest
public class Test {

    @Autowired
    private OaProcessTemplateService processTemplateService;
    @org.junit.jupiter.api.Test
    public void getPage() {
        Page<ProcessTemplate> pageInfo = new Page<>(1, 10);
        //分页查询审批模板，把审批类型对应名称查询
        IPage<ProcessTemplate> pageModel =
                processTemplateService.selectPageProcessTemplate(pageInfo);

    }
}
