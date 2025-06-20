package cn.wl.data.controller;

import cn.wl.basics.utils.PageUtil;
import cn.wl.basics.utils.ResultUtil;
import cn.wl.basics.baseVo.PageVo;
import cn.wl.basics.baseVo.Result;
import cn.wl.data.entity.Dict;
import cn.wl.data.entity.DictData;
import cn.wl.data.service.IDictDataService;
import cn.wl.data.service.IDictService;
import cn.wl.data.utils.WlNullUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author 郑为中
 * CSDN: Designer 小郑
 */
@RestController
@RequestMapping("/wl/dictData")
@Api(tags = "字典数据值接口")
@Transactional
public class DictDataController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IDictService iDictService;

    @Autowired
    private IDictDataService iDictDataService;

    private static final String REDIS_DIST_DATA_PRE_STR = "dictData::";

    @RequestMapping(value = "/getByType/{type}", method = RequestMethod.GET)
    @ApiOperation(value = "查询单个数据字典的值")
    public Result<Object> getByType(@PathVariable String type){
        QueryWrapper<Dict> qw = new QueryWrapper<>();
        qw.eq("type",type);
        Dict selectDict = iDictService.getOne(qw);
        if (selectDict == null) {
            return ResultUtil.error("字典 "+ type +" 不存在");
        }
        QueryWrapper<DictData> dataQw = new QueryWrapper<>();
        dataQw.eq("dict_id",selectDict.getId());
        return ResultUtil.data(iDictDataService.list(dataQw));
    }

    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
    @ApiOperation(value = "查询数据字典值")
    public Result<IPage<DictData>> getByCondition(@ModelAttribute DictData dictData, @ModelAttribute PageVo page) {
        QueryWrapper<DictData> qw = new QueryWrapper<>();
        if(!WlNullUtils.isNull(dictData.getDictId())) {
            qw.eq("dict_id",dictData.getDictId());
        }
        if(!Objects.equals(null,dictData.getStatus())) {
            qw.eq("status",dictData.getStatus());
        }
        if(!WlNullUtils.isNull(dictData.getTitle())) {
            qw.like("title",dictData.getTitle());
        }
        if(!WlNullUtils.isNull(dictData.getValue())) {
            qw.like("value",dictData.getValue());
        }
        if(!WlNullUtils.isNull(dictData.getDescription())) {
            qw.like("description",dictData.getDescription());
        }
        IPage<DictData> data = iDictDataService.page(PageUtil.initMpPage(page),qw);
        for (DictData dd : data.getRecords()) {
            if(dd != null) {
                Dict dict = iDictService.getById(dd.getDictId());
                if(dict != null) {
                    dd.setDictName(dict.getTitle());
                }
            }
        }
        return new ResultUtil<IPage<DictData>>().setData(data);
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除数据字典值")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for(String dictDataId : ids){
            DictData dictData = iDictDataService.getById(dictDataId);
            Dict dict = iDictService.getById(dictData.getDictId());
            iDictDataService.removeById(dictDataId);
            redisTemplate.delete(REDIS_DIST_DATA_PRE_STR + dict.getType());
        }
        return ResultUtil.success();
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加数据字典值")
    public Result<Object> add(DictData dictData){
        Dict selectDict = iDictService.getById(dictData.getDictId());
        if (selectDict == null) {
            return ResultUtil.error("字典不存在");
        }
        iDictDataService.saveOrUpdate(dictData);
        redisTemplate.delete(REDIS_DIST_DATA_PRE_STR + selectDict.getType());
        return ResultUtil.success();
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑数据字典值")
    public Result<Object> edit(DictData dictData){
        iDictDataService.saveOrUpdate(dictData);
        Dict selectDict = iDictService.getById(dictData.getDictId());
        redisTemplate.delete(REDIS_DIST_DATA_PRE_STR + selectDict.getType());
        return ResultUtil.success();
    }
}
