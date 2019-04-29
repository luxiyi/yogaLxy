package com.woniu.yago.service.impl;

import com.woniu.yago.pojo.Coach;
import com.woniu.yago.repository.CoachRepository;
import com.woniu.yago.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @Description: java类作用描述CoachServiceImpl
 * @Author: lxy
 * @time: 2019/4/25 17:24
 */
@Service
@Repository
public class CoachServiceImpl implements CoachService {
//    @Autowired
//    private CoachMapper coachMapper;
    @Autowired
    private CoachRepository coachRepository;

    @Override
    public void saveCoach(Coach coach) {
        coachRepository.save(coach);
    }

    @Override
    public Coach findCoachByUserId(Integer userId) {
        return coachRepository.findCoachByUserId(userId);
    }
}
