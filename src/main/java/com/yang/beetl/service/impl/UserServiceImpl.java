package com.yang.beetl.service.impl;

import com.yang.beetl.dao.UserDao;
import com.yang.beetl.entity.User;
import com.yang.beetl.service.UserService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.query.LambdaQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户业务service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserDao userDao;
    
    /**
     * 获取所有用户
     * @return
     */
    @Override
    public List<User> getAll() {
        return userDao.all();
    }
    
    /**
     * 保存用户
     * @param user
     * @return
     */
    @Override
    public boolean save(User user) {
        if(user.getId() == null){
            user.setCreateDate(new Date());
            userDao.insert(user);
            return true;
        }
    
        User unique = userDao.unique(user.getId());
        if(unique == null){
            user.setCreateDate(new Date());
            userDao.insert(user);
            return true;
        }
        return false;
    
    }
    
    /**
     * 更新用户
     * @param user
     * @return
     */
    @Override
    public boolean update(User user) {
        User unique = userDao.unique(user.getId());
        if(unique != null){
            userDao.updateTemplateById(user);
//            userDao.updateById(user);
        }else{
            userDao.insert(user);
        }
        return true;
    }
    
    /**
     * 根据id删除用户
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        userDao.deleteById(id);
    }
    
    /**
     * 根据id获取一个用户
     * @param id
     * @return
     */
    @Override
    public User getById(Integer id) {
        User unique = userDao.unique(id);
        return unique;
    }
    
    /**
     * 分页获取用户
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Map<String, Object> getPage(Integer pageNum, Integer pageSize, String sreachKey) {
        PageQuery<User> page = null;
        
        if(!StringUtils.isEmpty(sreachKey)){
            sreachKey = "%" + sreachKey + "%";
            page = userDao.createLambdaQuery()
                    .orLike("name", sreachKey)
                    .orLike("phone", sreachKey)
                    .page(pageNum, pageSize);
        }else{
            page = userDao.createLambdaQuery().page(pageNum, pageSize);
        }
        
        Map<String, Object> map = new HashMap<>();
        map.put("total", page.getTotalRow());// 总记录数
        map.put("items", page.getList());// 当前页数据
        map.put("totalPage", page.getTotalPage());// 总页数
        return map;
    }
    
    /**
     * 导出用户到excel
     * @return
     */
    @Override
    public Workbook exportExcel() {
        try{
            Workbook workbook = new SXSSFWorkbook();
    
            Sheet sheet = workbook.createSheet();
            // 设置表头
            Row head = sheet.createRow(0);
            Cell head1 = head.createCell(0);
            head1.setCellValue("姓名");
            Cell head2 = head.createCell(1);
            head2.setCellValue("年龄");
            Cell head3 = head.createCell(2);
            head3.setCellValue("性别");
            Cell head4 = head.createCell(3);
            head4.setCellValue("生日");
            Cell head5 = head.createCell(4);
            head5.setCellValue("手机号");
    
            // 每处理一万条查询一次
            long startRow = 1;
            long pageSize = 10000;
            List<User> list = userDao.createLambdaQuery().limit(startRow, pageSize).select();
    
            int index = 0;// 查询次数
    
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            while(list.size() > 0){
        
                if(index >= 100){// 查询次数到100次，是一百万条，每一百万条一个新的sheet
                    sheet = workbook.createSheet();
                    // 设置表头
                    head = sheet.createRow(0);
                    head1 = head.createCell(0);
                    head1.setCellValue("姓名");
                    head2 = head.createCell(1);
                    head2.setCellValue("年龄");
                    head3 = head.createCell(2);
                    head3.setCellValue("性别");
                    head4 = head.createCell(3);
                    head4.setCellValue("生日");
                    head5 = head.createCell(4);
                    head5.setCellValue("手机号");
                    index = 0;
                }
        
                for(int i = 0; i < list.size(); i++){
                    User user = list.get(i);
            
                    Row row = sheet.createRow(i + 1);
            
                    // 姓名
                    if(user.getName() != null){
                        Cell cell0 = row.createCell(0);
                        cell0.setCellValue(user.getName());
                    }
            
                    // 年龄
                    if(user.getAge() != null) {
                        Cell cell1 = row.createCell(1);
                        cell1.setCellValue(user.getAge());
                    }
                    
                    // 性别
                    Cell cell2 = row.createCell(2);
                    Integer gender = user.getGender();
                    if(gender == null){
                        cell2.setCellValue("未知");
                    }else{
                        cell2.setCellValue(gender == 1 ? "男" : "女");
                    }
            
                    // 生日
                    if(user.getBirthday() != null) {
                        Cell cell3 = row.createCell(3);
                        cell3.setCellValue(sdf.format(user.getBirthday()));
                    }
                    
                    // 电话
                    if(user.getPhone() != null) {
                        Cell cell4 = row.createCell(4);
                        cell4.setCellValue(user.getPhone());
                    }
                }
        
                index++;
        
                startRow += pageSize;
                list = userDao.createLambdaQuery().limit(startRow, pageSize).select();
            }
            
            return workbook;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
