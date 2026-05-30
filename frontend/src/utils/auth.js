/* 鉴权工具函数：检查当前用户是否为 admin 角色 */
export const isAdmin = () => localStorage.getItem('role') === 'admin'