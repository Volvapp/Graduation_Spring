package com.nbu.graduation.spring.model.dto;

import com.nbu.graduation.spring.model.enums.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ThesisAssignmentDTO {
    @NotBlank
    @Size(min = 2, max = 250)
    private String topic;
    @NotBlank
    @Size(min = 2, max = 1000)
    private String goal;
    @NotBlank
    @Size(min = 2, max = 1000)
    private String tasks;
    @NotBlank
    @Size(min = 2, max = 1000)
    private String technologies;
    private boolean approved;
    private boolean departmentApproved;
    private Department department;
    private Long studentId;
    private Long supervisorId;
    private Long thesisId;


    public ThesisAssignmentDTO() {
        this.approved = false;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(Long supervisorId) {
        this.supervisorId = supervisorId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Long getThesisId() {
        return thesisId;
    }

    public void setThesisId(Long thesisId) {
        this.thesisId = thesisId;
    }
}
