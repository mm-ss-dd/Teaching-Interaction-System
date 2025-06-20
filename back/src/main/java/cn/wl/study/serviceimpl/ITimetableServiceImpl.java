package cn.wl.study.serviceimpl;

import cn.wl.study.mapper.TimetableMapper;
import cn.wl.study.entity.Timetable;
import cn.wl.study.service.ITimetableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 课表 服务层接口实现
 * @author 郑为中
 */
@Slf4j
@Service
@Transactional
public class ITimetableServiceImpl extends ServiceImpl<TimetableMapper, Timetable> implements ITimetableService {

    @Autowired
    private TimetableMapper timetableMapper;
}