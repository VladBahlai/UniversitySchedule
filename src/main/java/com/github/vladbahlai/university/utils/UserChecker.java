package com.github.vladbahlai.university.utils;

import com.github.vladbahlai.university.security.MyUserDetails;
import com.github.vladbahlai.university.service.StudentService;
import com.github.vladbahlai.university.service.TeacherService;

public class UserChecker {

    public static boolean isStudent(MyUserDetails myUserDetails, StudentService studentService) {
        try {
            studentService.getStudentById(myUserDetails.getUser().getId());
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    public static boolean isTeacher(MyUserDetails myUserDetails, TeacherService teacherService) {
        try {
            teacherService.getTeacherById(myUserDetails.getUser().getId());
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}
