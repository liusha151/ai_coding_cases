package com.works.service;

import com.works.model.PageResult;
import com.works.model.Works;
import com.works.model.WorksQueryDTO;
import java.util.List;

public interface WorksService {
    PageResult<Works> findByPage(WorksQueryDTO query);
    Works findById(Integer id);
    int create(Works works);
    int update(Works works);
    int delete(Integer id);
    List<String> findAllAuthorNames();
}
