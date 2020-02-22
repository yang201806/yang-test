package com.yang.beetl.service;

import com.yang.beetl.entity.User;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;

/**
 * 用户业务service
 */
public interface UserService {
    
    /**
     * 获取所有用户
     * @return
     */
    public List<User> getAll();
    
    /**
     * 保存用户
     */
    public boolean save(User user);
    
    /**
     * 更新用户
     */
    public boolean update(User user);
    
    /**
     * 根据id删除用户
     * @param id
     */
    void deleteById(Integer id);
    
    /**
     * 根据id获取一个用户
     * @param id
     * @return
     */
    User getById(Integer id);
    
    /**
     * 分页获取用户
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String,Object> getPage(Integer pageNum, Integer pageSize, String sreachKey);
    
    /**
     * 导出用户到excel
     * @return
     */
    Workbook exportExcel();
}
