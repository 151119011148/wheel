package com.wheel.controller;

import com.wheel.common.exception.ResultCode;
import com.wheel.controller.request.UserParam;
import com.wheel.controller.response.Response;
import com.wheel.controller.response.UserVO;
import com.wheel.dao.dataObject.UserDO;
import com.wheel.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description: TODO
 * @Author gaofeng
 * @Date 2023/9/3 9:54 AM
 **/
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    UserService userService;

    @Resource
    Mapper beanMapper;


    /**
     * 通过 userId 修改用户信息
     *
     * @return
     */
    @PutMapping("/{userId}")
    public Response<Boolean> edit(@PathVariable String userId, @RequestBody UserParam userParam) {
        UserDO user = userService.editOne(userParam);
        return new Response<>()
                .withData(beanMapper.map(user, UserVO.class))
                .withErrorMsg("修改信息成功");
    }

    /**
     * 登录
     *
     * @param
     * @return
     */
    @PostMapping("/login")
    public Response<UserVO> login(@RequestBody UserParam param) {

        if (StringUtils.isEmpty(param.getName())){
            return new Response<>(ResultCode.LOGIN_PASSWORD_IS_NULL.getCode(), "userName is invalid!");
        }
        if (StringUtils.isEmpty(param.getPassword())){
            return new Response<>(ResultCode.LOGIN_PASSWORD_IS_NULL.getCode(), "password is invalid!");
        }
        UserDO user = userService.login(param);
        return new Response<>()
                .withData(beanMapper.map(user, UserVO.class))
                .withErrorMsg("登陆成功");
    }



}
