package com.nbu.graduation.spring;

import com.nbu.graduation.spring.model.ThesisReview;
import com.nbu.graduation.spring.model.service.*;
import com.nbu.graduation.spring.repository.ThesisReviewRepository;
import com.nbu.graduation.spring.service.DefenseService;
import com.nbu.graduation.spring.service.TeacherService;
import com.nbu.graduation.spring.service.ThesisService;
import com.nbu.graduation.spring.service.UserService;
import com.nbu.graduation.spring.service.Impl.ThesisReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThesisReviewServiceImplTest {

    @Mock
    private ThesisReviewRepository thesisReviewRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ThesisService thesisService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ThesisReviewServiceImpl service;

    private ThesisReview thesisReviewEntity;
    private ThesisReviewServiceModel thesisReviewServiceModel;
    private UserServiceModel userServiceModel;
    private TeacherServiceModel teacherServiceModel;
    private ThesisServiceModel thesisServiceModel;

    @BeforeEach
    void setUp() {
        thesisReviewEntity = new ThesisReview();
        thesisReviewEntity.setId(1L);
        thesisReviewEntity.setPositive(true);

        thesisReviewServiceModel = new ThesisReviewServiceModel();
        thesisReviewServiceModel.setId(1L);
        thesisReviewServiceModel.setPositive(true);

        userServiceModel = new UserServiceModel();
        userServiceModel.setId(5L);
        userServiceModel.setUsername("user");

        teacherServiceModel = new TeacherServiceModel();
        teacherServiceModel.setId(10L);
        teacherServiceModel.setUser(userServiceModel);

        thesisServiceModel = new ThesisServiceModel();
        thesisServiceModel.setId(100L);
    }

    @Test
    void createThesisReview_ShouldSaveAndReturn() {
        when(modelMapper.map(thesisReviewServiceModel, ThesisReview.class)).thenReturn(thesisReviewEntity);
        when(thesisReviewRepository.save(thesisReviewEntity)).thenReturn(thesisReviewEntity);
        when(modelMapper.map(thesisReviewEntity, ThesisReviewServiceModel.class)).thenReturn(thesisReviewServiceModel);

        ThesisReviewServiceModel result = service.createThesisReview(thesisReviewServiceModel);

        assertNotNull(result);
        verify(thesisReviewRepository).save(thesisReviewEntity);
    }

    @Test
    void getThesisReviewById_ShouldReturnReview() {
        when(thesisReviewRepository.findById(1L)).thenReturn(Optional.of(thesisReviewEntity));
        when(modelMapper.map(thesisReviewEntity, ThesisReviewServiceModel.class)).thenReturn(thesisReviewServiceModel);

        ThesisReviewServiceModel result = service.getThesisReviewById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getThesisReviewById_NotFound_ShouldThrow() {
        when(thesisReviewRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getThesisReviewById(1L));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void updateThesisReview_ShouldSaveUpdated() {
        when(thesisReviewServiceModel.getId()).thenReturn(1L);
        when(thesisReviewRepository.existsById(1L)).thenReturn(true);
        when(modelMapper.map(thesisReviewServiceModel, ThesisReview.class)).thenReturn(thesisReviewEntity);
        when(thesisReviewRepository.save(thesisReviewEntity)).thenReturn(thesisReviewEntity);
        when(modelMapper.map(thesisReviewEntity, ThesisReviewServiceModel.class)).thenReturn(thesisReviewServiceModel);

        ThesisReviewServiceModel result = service.updateThesisReview(thesisReviewServiceModel);

        assertNotNull(result);
        verify(thesisReviewRepository).save(thesisReviewEntity);
    }

    @Test
    void updateThesisReview_IdNullOrNotExist_ShouldThrow() {
        thesisReviewServiceModel.setId(null);
        RuntimeException ex1 = assertThrows(RuntimeException.class, () -> service.updateThesisReview(thesisReviewServiceModel));
        assertTrue(ex1.getMessage().contains("not found"));

        thesisReviewServiceModel.setId(1L);
        when(thesisReviewRepository.existsById(1L)).thenReturn(false);
        RuntimeException ex2 = assertThrows(RuntimeException.class, () -> service.updateThesisReview(thesisReviewServiceModel));
        assertTrue(ex2.getMessage().contains("not found"));
    }

    @Test
    void deleteThesisReview_ShouldDeleteIfExists() {
        when(thesisReviewRepository.findById(1L)).thenReturn(Optional.of(thesisReviewEntity));

        service.deleteThesisReview(1L);

        verify(thesisReviewRepository).delete(thesisReviewEntity);
    }

    @Test
    void deleteThesisReview_NotFound_ShouldThrow() {
        when(thesisReviewRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.deleteThesisReview(1L));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void prepareReviewForm_ShouldReturnReviewModel() {
        when(userService.findByUsername("user")).thenReturn(userServiceModel);
        when(teacherService.findByUserId(userServiceModel.getId())).thenReturn(teacherServiceModel);
        when(thesisService.getThesisById(100L)).thenReturn(thesisServiceModel);

        ThesisReviewServiceModel review = service.prepareReviewForm(100L, "user");

        assertNotNull(review);
        assertEquals(teacherServiceModel, review.getReviewer());
        assertEquals(thesisServiceModel, review.getThesis());
    }

    @Test
    void prepareReviewForm_NoTeacher_ShouldThrow() {
        when(userService.findByUsername("user")).thenReturn(userServiceModel);
        when(teacherService.findByUserId(userServiceModel.getId())).thenReturn(null);
        when(thesisService.getThesisById(100L)).thenReturn(thesisServiceModel);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.prepareReviewForm(100L, "user"));
        assertTrue(ex.getMessage().contains("Teacher"));
    }

    @Test
    void prepareReviewForm_NoThesis_ShouldThrow() {
        when(userService.findByUsername("user")).thenReturn(userServiceModel);
        when(teacherService.findByUserId(userServiceModel.getId())).thenReturn(teacherServiceModel);
        when(thesisService.getThesisById(100L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.prepareReviewForm(100L, "user"));
        assertTrue(ex.getMessage().contains("Thesis"));
    }

    @Test
    void countNegativeReviews_ShouldReturnCount() {
        when(thesisReviewRepository.countByPositive(false)).thenReturn(7);

        long count = service.countNegativeReviews();

        assertEquals(7L, count);
    }


}
