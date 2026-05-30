package com.works.mapper;

import com.works.model.Works;
import com.works.model.WorksQueryDTO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * 著作 Mapper：提供著作表的基础 CRUD 及统计查询方法
 */
public interface WorksMapper {
    List<Works> findByPage(WorksQueryDTO query);
    int count(WorksQueryDTO query);
    Works findById(Integer id);
    int insert(Works works);
    int update(Works works);
    int deleteById(Integer id);
    List<String> findAllAuthorNames();
    /** 按著作类型分组统计数量 */
    List<Map<String, Object>> countByWorkType(@Param("authorName") String authorName, @Param("workType") String workType);
    /** 按状态分组统计数量 */
    List<Map<String, Object>> countByStatus(@Param("authorName") String authorName, @Param("workType") String workType);
    /** 按个人排名区间（每5名一档）分组统计 */
    List<Map<String, Object>> countByRank(@Param("authorName") String authorName, @Param("workType") String workType);
    /** 按取得年份分组统计 */
    List<Map<String, Object>> countByYear(@Param("authorName") String authorName, @Param("workType") String workType);
}
