package com.nbu.graduation.spring;

import com.nbu.graduation.spring.model.User;
import com.nbu.graduation.spring.model.enums.Department;
import com.nbu.graduation.spring.model.enums.Role;
import com.nbu.graduation.spring.model.service.StudentServiceModel;
import com.nbu.graduation.spring.model.service.UserServiceModel;
import com.nbu.graduation.spring.repository.UserRepository;
import com.nbu.graduation.spring.service.Impl.UserServiceImpl;
import com.nbu.graduation.spring.service.StudentService;
import com.nbu.graduation.spring.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private StudentService studentService;
    private TeacherService teacherService;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        studentService = mock(StudentService.class);
        teacherService = mock(TeacherService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        modelMapper = new ModelMapper();

        userService = new UserServiceImpl(userRepository, studentService, teacherService, passwordEncoder, modelMapper);
    }

    @Test
    void testCreateStudentUser() {
        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setUsername("test");
        userServiceModel.setPassword("pass");
        userServiceModel.setFirstName("Test");
        userServiceModel.setLastName("User");
        userServiceModel.setEmail("test@abv.bg");
        userServiceModel.setRole(Role.STUDENT);
        userServiceModel.setDepartment(Department.IT);

        when(passwordEncoder.encode("pass")).thenReturn("encoded");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        UserServiceModel result = userService.createUser(userServiceModel);

        assertEquals("test", result.getUsername());
        verify(studentService).createStudent(any(StudentServiceModel.class));
        verify(teacherService, never()).createTeacher(any());
    }

    @Test
    void testFindByUsername() {
        User user = new User("user", "encoded", "First", "Last", "mail", Role.STUDENT, Department.IT);
        when(userRepository.findByUsername("user")).thenReturn(user);

        UserServiceModel result = userService.findByUsername("user");

        assertEquals("user", result.getUsername());
        assertEquals("First", result.getFirstName());
    }

    @Test
    void testUsernameOrEmailExists_true() {
        when(userRepository.findByUsername("exists")).thenReturn(new User());
        when(userRepository.findByEmail("any@mail.com")).thenReturn(null);

        assertTrue(userService.usernameOrEmailExists("exists", "any@mail.com"));
    }

    @Test
    void testUsernameOrEmailExists_false() {
        when(userRepository.findByUsername("new")).thenReturn(null);
        when(userRepository.findByEmail("new@mail.com")).thenReturn(null);

        assertFalse(userService.usernameOrEmailExists("new", "new@mail.com"));
    }

    @Test
    void testGetUserById() {
        User user = new User("user", "encoded", "First", "Last", "mail", Role.STUDENT, Department.IT);
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserServiceModel result = userService.getUserById(1L);

        assertEquals(1L, result.getId());
        assertEquals("user", result.getUsername());
    }

    @Test
    void testDeleteUserById() {
        userService.deleteUserById(2L);
        verify(userRepository).deleteById(2L);
    }

    @Test
    void testFindAllByRole() {
        User user = new User("t", "x", "F", "L", "e", Role.TEACHER, Department.IT);
        when(userRepository.findAllByRole(Role.TEACHER)).thenReturn(List.of(user));

        List<UserServiceModel> result = userService.findAllByRole(Role.TEACHER);

        assertEquals(1, result.size());
        assertEquals("t", result.get(0).getUsername());
    }
}
