package com.nbu.graduation.spring.service.Impl;

import com.nbu.graduation.spring.model.User;
import com.nbu.graduation.spring.model.enums.Department;
import com.nbu.graduation.spring.model.enums.Role;
import com.nbu.graduation.spring.model.service.StudentServiceModel;
import com.nbu.graduation.spring.model.service.TeacherServiceModel;
import com.nbu.graduation.spring.model.service.UserServiceModel;
import com.nbu.graduation.spring.repository.UserRepository;
import com.nbu.graduation.spring.service.StudentService;
import com.nbu.graduation.spring.service.TeacherService;
import com.nbu.graduation.spring.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, StudentService studentService, TeacherService teacherService, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserServiceModel createUser(UserServiceModel userServiceModel) {
        User user = modelMapper.map(userServiceModel, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = this.userRepository.save(user);

        if (user.getRole() == Role.STUDENT) {
            StudentServiceModel studentServiceModel = new StudentServiceModel();
            studentServiceModel.setUser(this.modelMapper.map(user, UserServiceModel.class));
            this.studentService.createStudent(studentServiceModel);

        } else if (user.getRole() == Role.TEACHER) {
            TeacherServiceModel teacherServiceModel = new TeacherServiceModel();
            teacherServiceModel.setUser(this.modelMapper.map(user, UserServiceModel.class));
            this.teacherService.createTeacher(teacherServiceModel);

        }

        return modelMapper.map(user, UserServiceModel.class);
    }


    @Override
    public UserServiceModel updateUser(UserServiceModel userServiceModel) {
        User existingUser = this.userRepository.findById(userServiceModel.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        return this.modelMapper.map(this.userRepository.save(existingUser), UserServiceModel.class);
    }

    @Override
    public UserServiceModel getUserById(Long id) {
        return this.modelMapper.map(this.userRepository.findById(id).orElseThrow(), UserServiceModel.class);
    }

    @Override
    public void deleteUserById(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public UserServiceModel findByUsername(String username) {
        return this.modelMapper.map(this.userRepository.findByUsername(username), UserServiceModel.class);
    }

    @Override
    public List<UserServiceModel> findAllByRole(Role role) {
        List<User> users = this.userRepository.findAllByRole(role);
        return users.stream()
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .collect(Collectors.toList());
    }


    @Override
    public boolean usernameOrEmailExists(String username, String email) {
        return this.userRepository.findByUsername(username) != null || this.userRepository.findByEmail(email) != null;
    }

        @Override
        public void initializeUsers() {
            // --- STUDENTS ---
            String[][] studentData = {
                    {"vlado", "Vladimir", "Dobrev", "vlado@abv.bg", "ENGINEERING"},
                    {"petur", "Petur", "Nikolov", "pesho@abv.bg", "IT"},
                    {"ivan", "Ivan", "Ivanov", "ivan@abv.bg", "MARKETING"},
                    {"maria", "Maria", "Petrova", "maria@abv.bg", "IT"},
                    {"georgi", "Georgi", "Georgiev", "georgi@abv.bg", "ENGINEERING"},
                    {"ani", "Ani", "Markova", "ani@abv.bg", "MARKETING"},
                    {"kiril", "Kiril", "Kirilov", "kiril@abv.bg", "IT"},
                    {"iva", "Iva", "Veselinova", "iva@abv.bg", "ENGINEERING"},
                    {"dimo", "Dimitar", "Dimitrov", "dimo@abv.bg", "MARKETING"},
                    {"nina", "Nina", "Stoimenova", "nina@abv.bg", "IT"},
                    {"sasho", "Sasho", "Stoyanov", "sasho@abv.bg", "ENGINEERING"},
                    {"elena", "Elena", "Ilieva", "elena@abv.bg", "MARKETING"},
                    {"tony", "Tony", "Popov", "tony@abv.bg", "IT"}
            };

            for (int i = 0; i < studentData.length; i++) {
                StudentServiceModel student = this.studentService.getStudentById(i + 1L);
                User user = new User(
                        studentData[i][0],
                        passwordEncoder.encode("1234"),
                        studentData[i][1],
                        studentData[i][2],
                        studentData[i][3],
                        Role.STUDENT,
                        Department.valueOf(studentData[i][4])
                );
                this.userRepository.save(user);
                student.setUser(this.modelMapper.map(user, UserServiceModel.class));
                this.studentService.updateStudent(student);
            }

            // --- TEACHERS ---
            String[][] teacherData = {
                    {"anka", "Anka", "Taseva", "taseva@gmail.com", "IT"},
                    {"gergana", "Gergana", "Ivanova", "ivanova@gmail.com", "IT"},
                    {"anton", "Anton", "Tuparov", "tuparoff@gmail.com", "MARKETING"},
                    {"kristiyan", "Kristiyan", "Petrov", "petrov@gmail.com", "ENGINEERING"},
                    {"milena", "Milena", "Staneva", "milena@faculty.bg", "IT"},
                    {"bozhidar", "Bozhidar", "Georgiev", "bozhidar@faculty.bg", "ENGINEERING"},
                    {"diana", "Diana", "Petrova", "diana@faculty.bg", "MARKETING"},
                    {"martin", "Martin", "Mihaylov", "martin@faculty.bg", "IT"},
                    {"rositsa", "Rositsa", "Borisova", "rositsa@faculty.bg", "ENGINEERING"},
                    {"pavel", "Pavel", "Vasilev", "pavel@faculty.bg", "MARKETING"},
                    {"stela", "Stela", "Yordanova", "stela@faculty.bg", "IT"},
                    {"vasil", "Vasil", "Kolev", "vasil@faculty.bg", "ENGINEERING"},
                    {"yuliana", "Yuliana", "Toneva", "yuliana@faculty.bg", "MARKETING"}
            };

            for (int i = 0; i < teacherData.length; i++) {
                TeacherServiceModel teacher = this.teacherService.findById(i + 1L);
                User user = new User(
                        teacherData[i][0],
                        passwordEncoder.encode("1234"),
                        teacherData[i][1],
                        teacherData[i][2],
                        teacherData[i][3],
                        Role.TEACHER,
                        Department.valueOf(teacherData[i][4])
                );
                this.userRepository.save(user);
                teacher.setUser(this.modelMapper.map(user, UserServiceModel.class));
                this.teacherService.updateTeacher(teacher);
            }
        }

    }
