package cn.wl.data.controller;

import cn.wl.basics.utils.ResultUtil;
import cn.wl.basics.baseVo.Result;
import cn.wl.data.entity.Dict;
import cn.wl.data.entity.DictData;
import cn.wl.data.service.IDictDataService;
import cn.wl.data.service.IDictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author 郑为中
 * CSDN: Designer 小郑
 */
@RestController
@Api(tags = "字典管理接口")
@RequestMapping("/wl/dict")
@Transactional
public class DictController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IDictService iDictService;

    @Autowired
    private IDictDataService iDictDataService;


    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询所有数据字典")
    public Result<List<Dict>> getAll(){
        return new ResultUtil<List<Dict>>().setData(iDictService.list());
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "模拟搜索数据字典")
    public Result<List<Dict>> search(@RequestParam String key){
        QueryWrapper<Dict> qw = new QueryWrapper<>();
        qw.like("title",key);
        return new ResultUtil<List<Dict>>().setData(iDictService.list(qw));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加数据字典")
    public Result<Object> add(Dict dict){
        QueryWrapper<Dict> qw = new QueryWrapper<>();
        qw.eq("type",dict.getType());
        long dictCount = iDictService.count(qw);
        if(dictCount < 1L) {
            iDictService.saveOrUpdate(dict);
            return ResultUtil.success();
        }
        return ResultUtil.error("字典已存在,不能同名");
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑数据字典")
    public Result<Object> edit(Dict dict){
        Dict oldDict = iDictService.getById(dict.getId());
        if(oldDict == null) {
            return ResultUtil.error("字典已被删除");
        }
        if(!Objects.equals(dict.getType(),oldDict.getType())){
            QueryWrapper<Dict> qw = new QueryWrapper<>();
            qw.eq("type",dict.getType());
            long dictCount = iDictService.count(qw);
            if(dictCount > 0L) {
                return ResultUtil.error("字典已存在,不能同名");
            }
        }
        iDictService.saveOrUpdate(dict);
        return ResultUtil.success();
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "删除数据字典")
    public Result<Object> delByIds(@RequestParam String[] ids){
        for (String distId : ids){
            Dict selectDict = iDictService.getById(distId);
            if(selectDict == null) {
                continue;
            }
            iDictService.removeById(distId);
            QueryWrapper<DictData> qw = new QueryWrapper<>();
            qw.eq("dict_id",distId);
            iDictDataService.remove(qw);
            redisTemplate.delete("dictData::" + selectDict.getType());
        }
        return ResultUtil.success();
    }
}
