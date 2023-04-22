package com.ray.controller;

import com.ray.domain.ResponseResult;
import com.ray.domain.dto.AddUserDto;
import com.ray.domain.dto.RoleDto;
import com.ray.domain.dto.UpdateRoleDto;
import com.ray.domain.dto.UpdateUserDto;
import com.ray.domain.entity.Menu;
import com.ray.domain.entity.Role;
import com.ray.service.MenuService;
import com.ray.service.RoleService;
import com.ray.service.UserService;
import jdk.nashorn.internal.ir.ReturnNode;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;


/**
 * @author liuris
 * @create 2023-04-15-17:18
 */
@RestController
@RequestMapping("/system")
public class SystemController {
    @Autowired
    MenuService menuService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;
//    菜单管理
    @GetMapping("/menu/list")
    public ResponseResult menuList(String status, String menuName){
        return menuService.menuList(status,menuName);
    }
    @PostMapping
    public ResponseResult add(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }
    @GetMapping("/menu/{id}")
    public  ResponseResult  queryMenu(@PathVariable("id") Long id){
        return menuService.queryMenu(id);
    }

    @PutMapping("/menu")
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    @DeleteMapping("/menu/{menuId}")
    public ResponseResult deleteMenu(@PathVariable("menuId") Long id){
        return menuService.deleteMenu(id);
    }

    @GetMapping("/menu/treeselect")
    public  ResponseResult treeSelect(){
        return menuService.treeSelect();
    }

    @GetMapping("/menu/roleMenuTreeselect/{id}")
    public  ResponseResult roleMenuTreeselect(@PathVariable("id") Long id){
        return menuService.roleMenuTreeselect(id);
    }


//    角色管理
    @GetMapping("/role/{id}")
    public ResponseResult getRole(@PathVariable("id") Long id){
        return roleService.getRole(id);
    }

    @GetMapping("/role/list")
    public ResponseResult roleList(Integer pageNum,Integer pageSize,String roleName,String status){
        return roleService.roleList(pageNum,pageSize,roleName,status);
    }

    @PutMapping("/role/changeStatus")
    public ResponseResult changeStatus(Long roleId,String status){
        return roleService.changeStatus(roleId,status);
    }



    @PostMapping("/role")
    public ResponseResult addRole(@RequestBody RoleDto roleDto){
        return roleService.addRole(roleDto);
    }

    @PutMapping("/role")
    public ResponseResult updateRoleInfo(@RequestBody UpdateRoleDto updateRoleDto){
        return roleService.updateRoleInfo(updateRoleDto);
    }

    @DeleteMapping("/role/{id}")
    public ResponseResult deleteRole(@PathVariable("id") Long id) {
        roleService.removeById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/role/listAllRole")
    public  ResponseResult listAllRole(){
        List<Role> roles = roleService.list();
        return  ResponseResult.okResult(roles);
    }

//    用户管理
    @GetMapping("/user/list")
    public ResponseResult userList(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status){
        return userService.userList(pageNum,pageSize,userName,phonenumber,status);
    }

    @PostMapping("/user")
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }

    @DeleteMapping("/user/{id}")
    public ResponseResult deleteUser(@PathVariable("id") Long id){
         userService.removeById(id);
         return ResponseResult.okResult();
    }

    @GetMapping("/user/{id}")
    public ResponseResult adminUserInfo(@PathVariable("id") Long id){
        return userService.adminUserInfo(id);
    }

   @PutMapping("/user")
    public ResponseResult updateUser(@RequestBody UpdateUserDto updateUserDto){
        return userService.updateAdminUserInfo(updateUserDto);
   }

}
