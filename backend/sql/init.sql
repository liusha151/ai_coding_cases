CREATE DATABASE IF NOT EXISTS works_management DEFAULT CHARSET utf8mb4;

USE works_management;

-- 数据字典类型表
CREATE TABLE IF NOT EXISTS dict_type (
  id INT PRIMARY KEY AUTO_INCREMENT,
  type_code VARCHAR(50) NOT NULL UNIQUE,
  type_name VARCHAR(100) NOT NULL,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 数据字典项表
CREATE TABLE IF NOT EXISTS dict_item (
  id INT PRIMARY KEY AUTO_INCREMENT,
  type_code VARCHAR(50) NOT NULL,
  item_value VARCHAR(100) NOT NULL,
  sort_order INT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (type_code) REFERENCES dict_type(type_code),
  INDEX idx_dict_item_type_code (type_code),
  UNIQUE(type_code, item_value)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 著作信息表
CREATE TABLE IF NOT EXISTS works (
  id INT PRIMARY KEY AUTO_INCREMENT,
  work_no VARCHAR(50) NOT NULL COMMENT '工号',
  author_name VARCHAR(100) NOT NULL COMMENT '姓名',
  work_type VARCHAR(50) NOT NULL COMMENT '著作类型(字典)',
  work_name VARCHAR(200) NOT NULL COMMENT '名称',
  publish_path VARCHAR(1000) COMMENT '发表路径',
  status VARCHAR(50) NOT NULL COMMENT '状态(字典)',
  personal_rank INT NOT NULL CHECK (personal_rank > 0) COMMENT '个人排名(>0)',
  co_authors VARCHAR(500) COMMENT '著作人',
  acquire_date DATE NOT NULL COMMENT '取得时间',
  remark TEXT COMMENT '备注',
  create_by VARCHAR(100),
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 系统用户表
CREATE TABLE IF NOT EXISTS sys_user (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL DEFAULT 'user' COMMENT '管理员/普通用户',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始数据
INSERT INTO dict_type (type_code, type_name) VALUES
('work_type', '著作类型'),
('work_status', '状态');

INSERT INTO dict_item (type_code, item_value, sort_order) VALUES
('work_type', '科技论文', 1),
('work_type', '管理论文', 2),
('work_type', '发明专利', 3),
('work_type', '国防专利', 4),
('work_type', '实用新型专利', 5),
('work_status', '已提交', 1),
('work_status', '已审理', 2),
('work_status', '已公开', 3),
('work_status', '已授权', 4),
('work_status', '已驳回', 5),
('work_status', '已投稿', 6),
('work_status', '已录用', 7),
('work_status', '已发表', 8);

-- 默认管理员 admin/admin123
INSERT INTO sys_user (username, password, role) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin');
