package com.works.service.impl;

import com.works.exception.BusinessException;
import com.works.mapper.WorksMapper;
import com.works.model.PageResult;
import com.works.model.Works;
import com.works.model.WorksQueryDTO;
import com.works.service.WorksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 著作业务实现：分页查询、详情、新增、修改、删除
 */
@Service
public class WorksServiceImpl implements WorksService {
    @Autowired
    private WorksMapper worksMapper;

    @Override
    public PageResult<Works> findByPage(WorksQueryDTO query) {
        List<Works> list = worksMapper.findByPage(query);
        int total = worksMapper.count(query);
        return new PageResult<>(list, total, query.getPage(), query.getSize());
    }

    @Override
    public Works findById(Integer id) {
        Works works = worksMapper.findById(id);
        if (works == null) throw new BusinessException(404, "著作信息不存在");
        return works;
    }

    /** 新增时校验个人排名必须为正整数 */
    @Override
    public int create(Works works) {
        if (works.getPersonalRank() == null || works.getPersonalRank() <= 0) {
            throw new BusinessException(400, "个人排名必须大于0");
        }
        return worksMapper.insert(works);
    }

    /** 修改时校验 ID 和排名合法性 */
    @Override
    public int update(Works works) {
        if (works.getId() == null) throw new BusinessException(400, "ID不能为空");
        if (works.getPersonalRank() != null && works.getPersonalRank() <= 0) {
            throw new BusinessException(400, "个人排名必须大于0");
        }
        return worksMapper.update(works);
    }

    @Override
    public int delete(Integer id) {
        return worksMapper.deleteById(id);
    }

    @Override
    public List<String> findAllAuthorNames() {
        return worksMapper.findAllAuthorNames();
    }
}
