package com.works.service.impl;

import com.works.mapper.WorksMapper;
import com.works.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * 统计业务实现：委托 Mapper 执行分组聚合查询
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private WorksMapper worksMapper;

    /** 按著作类型分组统计数量 */
    @Override
    public List<Map<String, Object>> countByWorkType(String authorName, String workType) {
        return worksMapper.countByWorkType(authorName, workType);
    }

    /** 按状态分组统计数量 */
    @Override
    public List<Map<String, Object>> countByStatus(String authorName, String workType) {
        return worksMapper.countByStatus(authorName, workType);
    }

    /** 按个人排名区间（每5名一档）分组统计 */
    @Override
    public List<Map<String, Object>> countByRank(String authorName, String workType) {
        return worksMapper.countByRank(authorName, workType);
    }

    /** 按取得年份分组统计 */
    @Override
    public List<Map<String, Object>> countByYear(String authorName, String workType) {
        return worksMapper.countByYear(authorName, workType);
    }
}
