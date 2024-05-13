package com.lyn.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyn.usercenter.common.BaseResponse;
import com.lyn.usercenter.common.ErrorCode;
import com.lyn.usercenter.common.ResultUtils;
import com.lyn.usercenter.exception.BusinessException;
import com.lyn.usercenter.model.domain.Team;
import com.lyn.usercenter.model.domain.User;
import com.lyn.usercenter.model.domain.UserTeam;
import com.lyn.usercenter.model.dto.TeamQuery;
import com.lyn.usercenter.model.request.*;
import com.lyn.usercenter.model.vo.TeamUserVO;
import com.lyn.usercenter.service.TeamService;
import com.lyn.usercenter.service.UserService;
import com.lyn.usercenter.service.UserTeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 队伍接口
 */
@RestController
@RequestMapping("/team")
@CrossOrigin(origins = "http://localhost:5173/", allowCredentials = "true")
@Slf4j
public class TeamController {
    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;

    @Resource
    private UserTeamService userTeamService;

    @PostMapping("/add")
    public BaseResponse<Long> add(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getCurrentUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {

        if (deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getCurrentUser(request);
        boolean result = teamService.deleteTeam(deleteRequest.getId(), loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }


    @PostMapping("/update")
    public BaseResponse<Boolean> update(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getCurrentUser(request);
        boolean update = teamService.updateTeam(teamUpdateRequest, loginUser);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "修改失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    public BaseResponse<Team> get(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(team);
    }

    @GetMapping("/list")
    public BaseResponse<List<TeamUserVO>> listTeams(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery, request);

//        /**
//         * 这里新增一个逻辑，返回用户已加入的队伍列表，对接用户退出队伍的功能
//         */
//        //获取teamList的队伍id集合
//        List<Long> teamIdList =
//                teamList.stream().map(TeamUserVO::getId).collect(Collectors.toList());
//        //通过user_team这个表，根据用户的userId和队伍的teamId去查找当前登录用户的队伍
//        try {//这里使用try-catch是因为getLoginUser会抛异常，我们希望的是用户不登录也能够通过这个接口
//            User loginUser = userService.getCurrentUser(request);
//            QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
//            userTeamQueryWrapper.eq("userId", loginUser.getId());
//            userTeamQueryWrapper.in("teamId", teamIdList);//集合，所以用in
//            //这样就得到了当前登录用户和已加入队伍的信息
//            List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
//            //通过userTeamList获取队伍的id
//            Set<Long> hasJoinTeamIdSet =
//                    userTeamList.stream().map(UserTeam::getTeamId).collect(Collectors.toSet());
//            //遍历teamList，通过hasJoinTeamIdSet中的teamId判断teamId是否在teamList中，如果有就说明用户加入了这个队伍
//            teamList.forEach(team -> {
//                boolean hasJoin = hasJoinTeamIdSet.contains(team.getId());
//                //最后将是否加入队伍(true/false)的信息塞到teamList的team信息中
//                team.setHasJoin(hasJoin);
//            });
//        } catch (Exception e) {
//            log.error("teamListError");
//        }
//
//        /**
//         * 查询已加入队伍的人数
//         */
//        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
//        userTeamQueryWrapper.in("teamId", teamIdList);
//        List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
//        //根据teamId进行分组
//        Map<Long, List<UserTeam>> teamIdUserTeamList = userTeamList.stream().
//                collect(Collectors.groupingBy(UserTeam::getTeamId));
//        //给hasJoinNum设置值
//        teamList.forEach(team -> team.setHasJoinNum(teamIdUserTeamList.getOrDefault(team.getId(), new ArrayList<>()).size()));
        return ResultUtils.success(teamList);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamsByPage(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery, team);
        Page<Team> page = new Page<>(teamQuery.getPageNum(), teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> result = teamService.page(page, queryWrapper);
        return ResultUtils.success(result);
    }

    /**
     * 获取我创建的队伍
     *
     * @param teamQuery
     * @param request
     * @return
     */
    @GetMapping("/list/my/create")
    public BaseResponse<List<TeamUserVO>> listMyCreateTeams(TeamQuery
                                                                    teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getCurrentUser(request);
        teamQuery.setUserId(loginUser.getId());
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery, request);
        return ResultUtils.success(teamList);
    }

    /**
     * 获取我加入的队伍
     *
     * @param teamQuery
     * @param request
     * @return
     */
    @GetMapping("/list/my/join")
    public BaseResponse<List<TeamUserVO>> listMyJoinTeams(TeamQuery
                                                                  teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //当前登录用户
        User loginUser = userService.getCurrentUser(request);
        //获取当前登录用户加入队伍的列表，严谨点，进行过滤重复的队伍
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());
        List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
        Map<Long, List<UserTeam>> listMap = userTeamList.stream().
                collect(Collectors.groupingBy(UserTeam::getTeamId));
        List<Long> idList = new ArrayList<>(listMap.keySet());
        teamQuery.setIdList(idList);
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery, request);
        return ResultUtils.success(teamList);
    }

    @PostMapping("/join")
    public BaseResponse<Boolean> join(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getCurrentUser(request);
        boolean res = teamService.joinTeam(teamJoinRequest, loginUser);
        return ResultUtils.success(res);
    }

    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest,
                                          HttpServletRequest request) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getCurrentUser(request);
        boolean result = teamService.quitTeam(teamQuitRequest, loginUser);
        return ResultUtils.success(result);
    }


}
