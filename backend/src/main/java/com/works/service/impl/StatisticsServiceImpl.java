package com.works.service.impl;

import com.works.mapper.WorksMapper;
import com.works.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private WorksMapper worksMapper;

    @Override
    public List<Map<String, Object>> countByWorkType(String authorName, String workType) {
        return worksMapper.countByWorkType(authorName, workType);
    }

    @Override
    public List<Map<String, Object>> countByStatus(String authorName, String workType) {
        return worksMapper.countByStatus(authorName, workType);
    }

    @Override
    public List<Map<String, Object>> countByRank(String authorName, String workType) {
        return worksMapper.countByRank(authorName, workType);
    }

    @Override
    public List<Map<String, Object>> countByYear(String authorName, String workType) {
        return worksMapper.countByYear(authorName, workType);
    }
}
