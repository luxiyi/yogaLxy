package com.woniu.yago.service;

import com.woniu.yago.pojo.Coach;

/**
 * @Description: java类作用描述CoachService
 * @Author: lxy
 * @time: 2019/4/25 17:19
 */
public interface CoachService {

    void saveCoach(Coach coach);

    Coach findCoachByUserId(Integer userId);
}
