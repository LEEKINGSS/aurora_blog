package com.aurora.service;

import com.aurora.model.dto.*;
import com.aurora.model.vo.ConditionVO;
import com.aurora.model.vo.PasswordVO;
import com.aurora.model.vo.QQLoginVO;
import com.aurora.model.vo.UserVO;

import java.util.List;

public interface UserAuthService {

    void sendCode(String username);

    List<UserAreaDTO> listUserAreas(ConditionVO conditionVO);

    void register(UserVO userVO);

    void updatePassword(UserVO userVO);

    void updateAdminPassword(PasswordVO passwordVO);

    PageResultDTO<UserAdminDTO> listUsers(ConditionVO condition);

    UserLogoutStatusDTO logout();

    UserInfoDTO qqLogin(QQLoginVO qqLoginVO);

}
