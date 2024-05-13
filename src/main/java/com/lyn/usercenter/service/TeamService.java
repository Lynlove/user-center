package com.lyn.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyn.usercenter.model.domain.Team;
import com.lyn.usercenter.model.domain.User;
import com.lyn.usercenter.model.dto.TeamQuery;
import com.lyn.usercenter.model.request.TeamJoinRequest;
import com.lyn.usercenter.model.request.TeamQuitRequest;
import com.lyn.usercenter.model.request.TeamUpdateRequest;
import com.lyn.usercenter.model.vo.TeamUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author lyn
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-05-09 10:27:46
*/
public interface TeamService extends IService<Team> {

    /**
     * 创建队伍
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);

    /**
     * 展示队伍列表
     * @param teamQuery
     * @param request
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, HttpServletRequest request);

    /**
     * 更新队伍
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 用户加入队伍
     * @param teamJoinRequest
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 用户退出队伍
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 队长解散队伍
     * @param id
     * @param loginUser
     * @return
     */
    boolean deleteTeam(long id, User loginUser);


}
