package com.woniu.yago.service.impl;

import com.woniu.yago.pojo.Student;
import com.woniu.yago.repository.StudentRepository;
import com.woniu.yago.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @Description: java类作用描述StudentServiceImpl
 * @Author: lxy
 * @time: 2019/4/25 15:35
 */
@Service
@Repository
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;


    @Override
    public Student findStudentByUserId(Integer userId) {
        return studentRepository.findStudentByUserId(userId);
    }

    @Override
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }
}
