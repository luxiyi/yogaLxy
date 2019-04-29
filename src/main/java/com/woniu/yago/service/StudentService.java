package com.woniu.yago.service;

import com.woniu.yago.pojo.Student;

/**
 * @Description: java类作用描述StudentService
 * @Author: lxy
 * @time: 2019/4/25 15:34
 */
public interface StudentService {
    Student findStudentByUserId(Integer userId);

   void saveStudent(Student student);
}
