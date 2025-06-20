# Graduation Process Management

## Project Description  
This is a web application called "Graduation_Spring" designed to manage the stages a student goes through in the graduation process.

The process works as follows: a teacher submits a thesis proposal containing:  
- Thesis topic  
- Objective  
- Tasks to achieve the objective  
- Technologies to be used  
- Student preparing the thesis  
- Supervisor teacher  

The proposal is reviewed and either approved or rejected by the relevant department. Upon approval, the student uploads the completed thesis, including title, content, and upload date.

The theses are reviewed by registered teachers; the review contains a date, text, and a conclusion indicating approval or rejection. After the review, the student proceeds to the thesis defense stage, where a group of teachers evaluate and record the student's defense grade.

The system supports two roles:  
- **Student**  
- **Teacher**

---

## Functional Requirements

1. Ability to create, view, edit, and delete data for:  
    - Thesis proposals  
    - Students (name and student ID)  
    - Teachers (name and position: assistant, senior assistant, associate professor, professor)  
    - Thesis defenses (date held)

2. Role-based access:  
    - Teachers can manage all data in the system.  
    - Students can only track their own graduation process and view all approved thesis topics. Students can only upload their thesis document.

3. Reports and filters including:  
    - All approved thesis proposals  
    - Thesis topics containing a specific substring  
    - All approved thesis proposals supervised by a given teacher  
    - All students who graduated within a specific period  
    - All theses defended with grades within a given range  
    - Number of students who received a negative review  
    - Average number of students attending thesis defenses within a given period  
    - Number of students successfully defended under a specific teacher

---

## Technical Requirements

The application is developed as a Spring Boot web application based on RESTful web services with a database and user interface using HTML & CSS. It must provide functionality for creating, editing, deleting, and displaying data as described. Data validation, exception handling, testing, and user management should be implemented.
