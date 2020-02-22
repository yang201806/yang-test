package com.yang.beetl.controller;

import com.yang.beetl.entity.User;
import com.yang.beetl.service.UserService;
import com.yang.beetl.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = "操作用户控制器")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    /**
     * 根据id获取一个用户
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id获取一个用户")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "用户id", dataType = "Integer", required = true, paramType = "path")
    })
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id){
        User user = userService.getById(id);
        return R.ok().data("user", user);
    }
    
    /**
     * 分页获取用户
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "分页获取用户")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "Integer", required = true, paramType = "path"),
        @ApiImplicitParam(name = "pageSize", value = "页长", dataType = "Integer", required = true, paramType = "path"),
        @ApiImplicitParam(name = "sreachKey", value = "查询关键字", dataType = "String", required = false, paramType = "query")
    })
    @GetMapping("/page/{pageNum}/{pageSize}")
    public R getPage(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                     @RequestParam(value = "sreachKey", required = false) String sreachKey){
        Map<String, Object> map = userService.getPage(pageNum, pageSize, sreachKey);
        return R.ok().data(map);
    }
    
    /**
     * 添加用户
     * @param user
     * @return
     */
    @ApiOperation(value = "添加用户")
    @PostMapping("/add")
    public R addUser(User user){
//        if(!checkBirth(user.getAge(), user.getBirthday())){
//            return R.error().message("生日与年龄不匹配");
//        }
        
        boolean save = userService.save(user);
        if(save){
            return R.ok();
        }
        return R.error().message("用户id重复");
    }
    
    /**
     * 更新用户
     * @param user
     * @return
     */
    @ApiOperation(value = "更新用户")
    @PostMapping("/update")
    public R updateUser(User user){
//        if(!checkBirth(user.getAge(), user.getBirthday())){
//            return R.error().message("生日与年龄不匹配");
//        }
        
        if(user.getId() == null){
            return R.error().message("请传入id");
        }
    
        userService.update(user);
        return R.ok();
    }
    
    /**
     * 根据id删除一个用户
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id删除一个用户")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "用户id", dataType = "Integer", required = true, paramType = "path")
    })
    @DeleteMapping("/{id}")
    public R deleleUser(@PathVariable("id") Integer id){
        userService.deleteById(id);
        return R.ok();
    }
    
    /**
     * 所有用户
     * @return
     */
    @ApiOperation(value = "所有用户")
    @GetMapping
    public R allUser(){
        List<User> list = userService.getAll();
        return R.ok().data("list", list).data("total", list.size());
    }
    
    /**
     * 导出到excel
     * @return
     */
    @ApiOperation(value = "导出到excel")
    @GetMapping("/export")
    public R export(HttpServletResponse response) {
        BufferedOutputStream bos = null;
        try {
            Workbook workbook = userService.exportExcel();
        
            response.setContentType("application/octet-stream;");
            response.setHeader("Content-disposition", "attachment; filename=" + new String("用户列表.xlsx".getBytes("utf-8"), "ISO8859-1"));
            bos = new BufferedOutputStream(response.getOutputStream());
            
            workbook.write(bos);
        }catch (Exception e){
        
        }finally {
            if (bos != null){
                try {
                    bos.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }
    
    
    /**
     * 检查生日与年龄是否匹配
     * @return
     */
    private boolean checkBirth(Integer age, Date birth){
        Date now = new Date();
        Calendar b = Calendar.getInstance();
        Calendar n = Calendar.getInstance();
        b.setTime(birth);
        n.setTime(now);
        
        int birthYear = b.get(Calendar.YEAR);
        int nowYear = n.get(Calendar.YEAR);
        if((nowYear - birthYear) != age){
            return false;
        }
        
        return true;
    }
    
}
