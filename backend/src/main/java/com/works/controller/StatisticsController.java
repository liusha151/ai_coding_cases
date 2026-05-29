package com.works.controller;

import com.works.common.Result;
import com.works.model.StatisticsQueryDTO;
import com.works.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public Result<Map<String, List<Map<String, Object>>>> getStatistics(StatisticsQueryDTO query) {
        Map<String, List<Map<String, Object>>> data = new HashMap<>();
        data.put("byWorkType", statisticsService.countByWorkType(query.getAuthorName(), query.getWorkType()));
        data.put("byStatus", statisticsService.countByStatus(query.getAuthorName(), query.getWorkType()));
        data.put("byRank", statisticsService.countByRank(query.getAuthorName(), query.getWorkType()));
        data.put("byYear", statisticsService.countByYear(query.getAuthorName(), query.getWorkType()));
        return Result.success(data);
    }
}
