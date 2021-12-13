package ua.com.foxminded.university.api.mapper;

import org.mapstruct.Mapper;
import ua.com.foxminded.university.api.dto.StudentDto;
import ua.com.foxminded.university.model.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentDto studentToStudentDTO(Student student);

    Student studentDtoToStudent(StudentDto studentDto);
}
