package com.example.cloudtypetest.web.controller;

import com.example.cloudtypetest.base.BaseException;
import com.example.cloudtypetest.base.BaseResponse;
import com.example.cloudtypetest.domain.user.User;
import com.example.cloudtypetest.service.user.UserService;
import com.example.cloudtypetest.web.dto.LoginUserReq;
import com.example.cloudtypetest.web.dto.PostUserReq;
import com.example.cloudtypetest.web.dto.TokenRes;
import com.example.cloudtypetest.web.dto.user.UserReq;
import com.example.cloudtypetest.web.dto.user.UserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.cloudtypetest.base.BaseResponseStatus.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping("/login")
    public BaseResponse<TokenRes> login(@RequestBody LoginUserReq loginUserReq){
        if(loginUserReq.getUsername()==null){
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }
        if(loginUserReq.getPassword()==null){
            return new BaseResponse<>(USERS_EMPTY_USER_PASSWORD);
        }

        try {
            TokenRes tokenRes = userService.login(loginUserReq);
            return new BaseResponse<>(tokenRes);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/signup")
    public BaseResponse<TokenRes> signup(@RequestBody PostUserReq postUserReq) {
        try {
            if (userService.checkUserId(postUserReq.getUsername())) {
                return new BaseResponse<>(USERS_EXISTS_ID);
            }
            if (userService.checkNickName(postUserReq.getNickname())) {
                return new BaseResponse<>(USERS_EXISTS_NICKNAME);
            }
            TokenRes tokenRes = userService.signup(postUserReq);

            User user = userService.findUserById(tokenRes.getUserId());
            userService.postKeyword(user,postUserReq.getKeywordList());
            userService.postTendency(user,postUserReq.getTendencyList());

            return new BaseResponse<>(tokenRes);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/user/signup")
    public BaseResponse<List<UserRes.GetKeywordRes>> getKeywordList(@RequestParam("keyword") String keyword){
        List<UserRes.GetKeywordRes> getKeywordRes = userService.getKeywordList(keyword);
        return new BaseResponse<>(getKeywordRes);
    }
    @GetMapping("/my_page")
    public BaseResponse<String> getMyPage(){

        return null;
    }

    @GetMapping("/my_room")
    public BaseResponse<String> getMyRoom(){
        return null;
    }

    @PatchMapping
    public BaseResponse<String> patchUserInfo(@RequestBody UserReq.PatchUserReq patchUserReq){

        return null;
    }


}
