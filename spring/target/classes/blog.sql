-- 权限表
CREATE TABLE permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,  -- 将ID类型改为BIGINT并添加自增
    status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',  -- 改为TINYINT并添加默认值和注释
    remark VARCHAR(255) DEFAULT NULL,  -- 添加默认值
    permission VARCHAR(255) UNIQUE NOT NULL COMMENT '权限标识',  -- 添加注释
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',  -- 添加创建时间字段
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'  -- 添加更新时间字段
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 角色表
CREATE TABLE role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,  -- 将ID类型改为BIGINT并添加自增
    role_name VARCHAR(255) NOT NULL COMMENT '角色名称',  -- 修改字段名为role_name并添加注释
    status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',  -- 添加状态字段
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',  -- 添加备注字段
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',  -- 添加创建时间字段
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'  -- 添加更新时间字段
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 用户表
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    avatar VARCHAR(255),
    introduction VARCHAR(255) DEFAULT '用户未填写',
    email VARCHAR(255) UNIQUE NOT NULL,
    status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '用户状态：0-禁用，1-启用',
    address VARCHAR(255) DEFAULT '用户未填写',
    last_login_ip VARCHAR(45),
    last_login DATETIME,
    role_id BIGINT NOT NULL,  -- 将role_id类型改为BIGINT
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    password VARCHAR(255) NOT NULL COMMENT '加密后的密码',
    FOREIGN KEY (role_id) REFERENCES role(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- Token表
CREATE TABLE token (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID，自增',
    user_id BIGINT NOT NULL COMMENT '用户ID，外键关联user表',
    token VARCHAR(255) NOT NULL COMMENT 'Token值',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', -- 添加更新时间字段
    token_type VARCHAR(20) NOT NULL COMMENT '令牌类型',  -- 新增字段
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Token表';

-- 路由表（处理树形结构）
CREATE TABLE router (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID，自增',  -- 修改ID为BIGINT并添加自增
    pid BIGINT COMMENT '父路由ID',  -- 添加注释
    menu_order INT NOT NULL COMMENT '菜单顺序',  -- 添加注释
    status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',  -- 修改为TINYINT并添加默认值和注释
    remark VARCHAR(255) COMMENT '备注',  -- 添加注释
    permission VARCHAR(255) COMMENT '权限标识',  -- 添加注释
    path VARCHAR(255) NOT NULL COMMENT '路由路径',  -- 添加注释
    name VARCHAR(255) NOT NULL COMMENT '路由名称',  -- 添加注释
    type ENUM('C', 'M','F') NOT NULL COMMENT '类型：C-目录，M-菜单，F-功能',  -- 修改为ENUM类型并添加注释
    component VARCHAR(255) COMMENT '组件路径',  -- 添加注释
    redirect VARCHAR(255) COMMENT '重定向路径',  -- 添加注释
    always_show BOOLEAN NOT NULL DEFAULT FALSE COMMENT '总是显示',  -- 添加默认值和注释
    meta_title VARCHAR(255) NOT NULL COMMENT '元标题',  -- 添加注释
    meta_icon VARCHAR(255) COMMENT '元图标',  -- 添加注释
    meta_hidden BOOLEAN DEFAULT FALSE COMMENT '元隐藏',  -- 添加默认值和注释
    meta_roles JSON COMMENT '元角色，存储角色名称数组',  -- 添加注释
    meta_keep_alive BOOLEAN DEFAULT FALSE COMMENT '元保持活跃',  -- 添加默认值和注释
    hidden BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否隐藏',  -- 添加默认值和注释
    FOREIGN KEY (pid) REFERENCES router(id) ON DELETE CASCADE,  -- 修正外键约束语法错误
    FOREIGN KEY (permission) REFERENCES permission(permission) ON DELETE SET NULL  -- 修正外键约束语法错误
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='路由表';

-- 角色-权限关联表
CREATE TABLE role_permission (
    role_id BIGINT COMMENT '角色ID',  -- 修改role_id类型为BIGINT
    permission_id BIGINT COMMENT '权限ID',  -- 修改permission_id类型为BIGINT
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE
) COMMENT='角色-权限关联表';

-- 可选：用户-角色关联表（若支持多角色）
CREATE TABLE user_role (
    user_id BIGINT COMMENT '用户ID，外键关联user表',
    role_id BIGINT COMMENT '角色ID，外键关联role表',
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
) COMMENT='用户-角色关联表';


-- 模拟日志表

CREATE TABLE nginx_access_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ip VARCHAR(50) NOT NULL COMMENT 'IP地址',
    access_time VARCHAR(50) NOT NULL COMMENT '访问时间',
    method VARCHAR(10) NOT NULL COMMENT 'HTTP方法',
    path VARCHAR(255) NOT NULL COMMENT '访问路径',
    protocol VARCHAR(20) COMMENT 'HTTP协议版本',
    status VARCHAR(10) NOT NULL COMMENT 'HTTP状态码',
    bytes VARCHAR(20) COMMENT '响应大小(字节)',
    referrer TEXT COMMENT '引用来源',
    user_agent TEXT COMMENT '用户代理',
    user_id VARCHAR(50) COMMENT '用户ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    INDEX idx_ip (ip),
    INDEX idx_user_id (user_id),
    INDEX idx_path (path),
    INDEX idx_status (status),
    INDEX idx_method (method)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Nginx访问日志表';

-- 插入默认角色
INSERT INTO role (role_name, status, remark, create_time, update_time)
VALUES ('ROLE_USER', 1, '默认用户角色', NOW(), NOW());

-- 插入默认用户
ALTER TABLE token
MODIFY COLUMN token_type VARCHAR(20) NOT NULL COMMENT '令牌类型: BEARER';

-- 添加过期时间字段
ALTER TABLE token ADD COLUMN expired BOOLEAN DEFAULT FALSE;

-- 添加revoked字段
ALTER TABLE token ADD COLUMN revoked BOOLEAN DEFAULT FALSE;

-- 添加revoked字段
ALTER TABLE router ADD COLUMN icon VARCHAR(255) DEFAULT NULL;

-- 添加revoked字段
ALTER TABLE permission ADD COLUMN user_id BIGINT COMMENT '用户ID' DEFAULT NULL;

-- 添加revoked字段
ALTER TABLE router ADD COLUMN role_id BIGINT COMMENT '角色ID' DEFAULT NULL;


ALTER TABLE role ADD COLUMN user_id BIGINT COMMENT '用户ID' DEFAULT NULL;

-- 插入系统管理路由
INSERT INTO router (pid, menu_order, status, remark, permission, path, name, type, component, always_show, hidden, meta_title, meta_icon, meta_hidden, meta_roles, meta_keep_alive)
VALUES (NULL, 1, 1, '系统管理', NULL, '/console', '系统管理', 'C', 'DataBoard', FALSE, FALSE, '系统管理', NULL, FALSE, NULL, FALSE);

-- 插入子路由
INSERT INTO router (pid, menu_order, status, remark, permission, path, name, type, component, always_show, hidden, meta_title, meta_icon, meta_hidden, meta_roles, meta_keep_alive)
VALUES
    (LAST_INSERT_ID(), 1, 1, '仪表盘', NULL, '/console/dashboard', '仪表盘', 'M', 'DataBoard', FALSE, FALSE, '仪表盘', NULL, FALSE, NULL, FALSE),
    (LAST_INSERT_ID(), 2, 1, '用户管理', NULL, '/console/usermanagement', '用户管理', 'M', 'User', FALSE, FALSE, '用户管理', NULL, FALSE, NULL, FALSE),
    (LAST_INSERT_ID(), 3, 1, '菜单管理', NULL, '/console/menumanagement', '菜单管理', 'M', 'IconMenu', FALSE, FALSE, '菜单管理', NULL, FALSE, NULL, FALSE),
    (LAST_INSERT_ID(), 4, 1, '设置', NULL, '/console/settings', '设置', 'M', 'Setting', FALSE, FALSE, '设置', NULL, FALSE, NULL, FALSE),
    (LAST_INSERT_ID(), 5, 1, '用户', NULL, '/console/user', '用户', 'C', 'User', FALSE, FALSE, '用户', NULL, FALSE, NULL, FALSE);

-- 插入用户子路由
INSERT INTO router (pid, menu_order, status, remark, permission, path, name, type, component, always_show, hidden, meta_title, meta_icon, meta_hidden, meta_roles, meta_keep_alive)
VALUES
    (LAST_INSERT_ID(), 1, 1, '用户列表', NULL, '/console/user/list', '用户列表', 'M', 'User', FALSE, FALSE, '用户列表', NULL, FALSE, NULL, FALSE),
    (LAST_INSERT_ID(), 2, 1, '添加用户', NULL, '/console/user/add', '添加用户', 'M', 'User', FALSE, FALSE, '添加用户', NULL, FALSE, NULL, FALSE);