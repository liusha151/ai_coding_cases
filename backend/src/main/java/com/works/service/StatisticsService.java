package com.works.service;

import java.util.List;
import java.util.Map;

public interface StatisticsService {
    List<Map<String, Object>> countByWorkType(String authorName, String workType);
    List<Map<String, Object>> countByStatus(String authorName, String workType);
    List<Map<String, Object>> countByRank(String authorName, String workType);
    List<Map<String, Object>> countByYear(String authorName, String workType);
}
