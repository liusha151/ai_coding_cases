package com.works.mapper;

import com.works.model.Works;
import com.works.model.WorksQueryDTO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface WorksMapper {
    List<Works> findByPage(WorksQueryDTO query);
    int count(WorksQueryDTO query);
    Works findById(Integer id);
    int insert(Works works);
    int update(Works works);
    int deleteById(Integer id);
    List<String> findAllAuthorNames();
    List<Map<String, Object>> countByWorkType(@Param("authorName") String authorName, @Param("workType") String workType);
    List<Map<String, Object>> countByStatus(@Param("authorName") String authorName, @Param("workType") String workType);
    List<Map<String, Object>> countByRank(@Param("authorName") String authorName, @Param("workType") String workType);
    List<Map<String, Object>> countByYear(@Param("authorName") String authorName, @Param("workType") String workType);
}
