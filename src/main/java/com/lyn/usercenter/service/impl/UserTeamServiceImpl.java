package com.lyn.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyn.usercenter.model.domain.UserTeam;
import com.lyn.usercenter.mapper.UserTeamMapper;
import com.lyn.usercenter.service.UserTeamService;
import org.springframework.stereotype.Service;

/**
* @author lyn
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2024-05-09 10:27:51
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




