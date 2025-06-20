package com.nbu.graduation.spring.service;

import com.nbu.graduation.spring.model.enums.Department;
import com.nbu.graduation.spring.model.service.StudentServiceModel;
import com.nbu.graduation.spring.model.service.ThesisAssignmentServiceModel;
import com.nbu.graduation.spring.model.service.ThesisServiceModel;
import com.nbu.graduation.spring.model.service.UserServiceModel;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface ThesisService {
    ThesisServiceModel createThesis(ThesisServiceModel thesisServiceModel, String fileName);

    ThesisServiceModel updateThesis(ThesisServiceModel thesisServiceModel);

    void deleteThesisById(Long id);

    ThesisServiceModel getThesisById(Long id);

    void uploadThesis(String title, MultipartFile file, String name) throws IOException;

    List<ThesisServiceModel> getThesesByDepartment(Department department);

    boolean studentHasThesis(Long id);

    Resource loadThesisFile(String filePath) throws FileNotFoundException, MalformedURLException;
    public ThesisAssignmentServiceModel getStudentAssignment(String username);


    public List<ThesisServiceModel> getThesesByTeacherDepartment(String username);

    public UserServiceModel findUserByUsername(String username);

    ThesisServiceModel getThesisByStudentId(Long studentById);

}
