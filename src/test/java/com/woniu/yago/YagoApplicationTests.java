package com.woniu.yago;

import com.woniu.yago.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YagoApplicationTests {
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private RoleMapper roleMapper;
//    @Autowired
//    private StudentMapper studentMapper;
//    @Autowired
//    private CoachMapper coachMapper;
//    @Autowired
//    private VenueMapper venueMapper;
//    @Autowired
//    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void contextLoads() {
//        System.out.println(roleMapper.findByRoleName("学员"));
//        User user=userMapper.queryUserByEmail("luxi982946720@163.com");
//        System.out.println(user.getUserEmail()+user.getUserVerifyCode());
//        redisTemplate.opsForValue().set(user.getUserEmail(),user.getUserVerifyCode());
//        System.out.println(redisTemplate.hasKey(user.getUserEmail()));
//        User user=new User();
//         user.setUserEmail("ssssss");
//       userMapper.saveUser(user);

//        user.setUserPwd("1111");
//        user.setUserEmail("ssssss");
//        System.out.println(user);
//        user=userRepository.queryUserByEmail("luxi982946720@163.com");
//        System.out.println(user);

//        redisTemplate.expire(user.getUserEmail(),30, TimeUnit.SECONDS);
//        System.out.println(redisTemplate.hasKey(user.getUserEmail()).toString());

//        Coach coach=new Coach();
//        Venue venue=new Venue();
//        coach.setUserId(3);
//        venue.setUserId(3);
//        venueMapper.saveVenue(venue);
//        System.out.println( coachMapper.findCoachByUserId(3));
//        System.out.println(NickNameUtil.getRandomNickName());
    }

}
