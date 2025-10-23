package com.zhaoxinms.contract.conf.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhaoxinms.common.annotation.Log;
import com.zhaoxinms.common.annotation.RepeatSubmit;
import com.zhaoxinms.common.core.controller.BaseController;
import com.zhaoxinms.common.core.validate.AddGroup;
import com.zhaoxinms.common.core.validate.EditGroup;
import com.zhaoxinms.common.enums.BusinessType;
import com.zhaoxinms.contract.conf.domain.bo.TSigningSubjectBo;
import com.zhaoxinms.contract.conf.domain.vo.TSigningSubjectVo;
import com.zhaoxinms.contract.conf.service.ITSigningSubjectService;
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.contract.type.util.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

/**
 * 签约主体Controller
 *
 * @author ruoyi
 * @date 2022-07-12
 */
@Validated
@Api(value = "签约主体控制器", tags = {"签约主体管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/conf/signingSubject")
public class TSigningSubjectController extends BaseController {

    private final ITSigningSubjectService iTSigningSubjectService;

    /**
     * 查询签约主体列表
     */
    @ApiOperation("查询签约主体列表")
    @GetMapping("/list")
    public TableDataInfo<TSigningSubjectVo> list(TSigningSubjectBo bo, PageQuery pageQuery) {
        return iTSigningSubjectService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取签约主体详细信息
     */
    @ApiOperation("获取签约主体详细信息")
    @GetMapping("/{id}")
    public R<TSigningSubjectVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") String id) {
        return R.ok(iTSigningSubjectService.queryById(id));
    }

    /**
     * 新增签约主体
     */
    @ApiOperation("新增签约主体")
    @Log(title = "签约主体", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody TSigningSubjectBo bo) {
    	iTSigningSubjectService.insertByBo(bo);
        return R.ok();
    }

    /**
     * 修改签约主体
     */
    @ApiOperation("修改签约主体")
    @Log(title = "签约主体", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody TSigningSubjectBo bo) {
    	iTSigningSubjectService.updateByBo(bo);
        return R.ok();
    }

    /**
     * 删除签约主体
     */
    @ApiOperation("删除签约主体")
    @Log(title = "签约主体", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                                       @NotEmpty(message = "主键不能为空")
                                       @PathVariable String[] ids) {
    	iTSigningSubjectService.deleteWithValidByIds(Arrays.asList(ids), true) ;
        return R.ok();
    }
    
    /**
     * 状态修改
     */
    @ApiOperation("状态修改")
    @Log(title = "签约主体", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<Void> changeStatus(@RequestBody TSigningSubjectBo bo) {
    	iTSigningSubjectService.updateStatus(bo);
        return R.ok();
    }
    
    /**
     * 查询签约主体列表（用于查询签约主体的下拉控件）
     */
    @ApiOperation("查询签约主体列表")
    @GetMapping("/showList/aa")
    public  R<List<TSigningSubjectVo>> showList(TSigningSubjectBo bo) {
        return R.ok(iTSigningSubjectService.queryList(bo));
    }
}
