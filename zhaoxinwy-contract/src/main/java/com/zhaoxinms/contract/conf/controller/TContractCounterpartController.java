package com.zhaoxinms.contract.conf.controller;

import java.util.Arrays;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
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
import com.zhaoxinms.common.enums.BusinessType;
import com.zhaoxinms.contract.conf.domain.bo.TContractCounterpartBo;
import com.zhaoxinms.contract.conf.domain.vo.TContractCounterpartVo;
import com.zhaoxinms.contract.conf.service.ITContractCounterpartService;
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.contract.type.util.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

/**
 * 合作方Controller
 *
 * @author ruoyi
 * @date 2022-07-18
 */
@Validated
@Api(value = "合作方控制器", tags = {"合作方管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/conf/contractCounterpart")
public class TContractCounterpartController extends BaseController {

    private final ITContractCounterpartService iTContractCounterpartService;

    /**
     * 查询合作方列表
     */
    @ApiOperation("查询合作方列表")
    @GetMapping("/list")
    public TableDataInfo<TContractCounterpartVo> list(TContractCounterpartBo bo, PageQuery pageQuery) {
        return iTContractCounterpartService.queryPermissionPageList(bo, pageQuery);
    }
    
    /**
     * 获取相对方详细信息
     */
    @ApiOperation("获取相对方详细信息")
    @GetMapping("/{id}")
    public R<TContractCounterpartVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") String id) {
        return R.ok(iTContractCounterpartService.queryById(id));
    }


    /**
     * 新增合作方
     */
    @ApiOperation("新增合作方")
    @Log(title = "合作方", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated @RequestBody TContractCounterpartBo bo) {
    	iTContractCounterpartService.insertByBo(bo);
        return R.ok();
    }

    /**
     * 修改合作方
     */
    @ApiOperation("修改合作方")
    @Log(title = "合作方", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated @RequestBody TContractCounterpartBo bo) {
    	iTContractCounterpartService.updateByBo(bo);
        return R.ok();
    }

    /**
     * 状态修改
     */
    @ApiOperation("状态修改")
    @Log(title = "合作方", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<Void> changeStatus(@RequestBody TContractCounterpartBo bo) {
    	iTContractCounterpartService.updateStauts(bo);
        return R.ok();
    }

    /**
     * 删除合作方
     */
    @ApiOperation("删除合作方")
    @Log(title = "合作方", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                                       @NotEmpty(message = "主键不能为空")
                                       @PathVariable String[] ids) {
    	iTContractCounterpartService.deleteWithValidByIds(Arrays.asList(ids), true);
        return R.ok();
    }
    
    public static CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 30);
        titleStyle.setFont(font);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return titleStyle;
    }
}
