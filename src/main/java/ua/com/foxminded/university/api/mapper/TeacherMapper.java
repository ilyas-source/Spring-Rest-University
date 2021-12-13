package ua.com.foxminded.university.api.mapper;

import org.mapstruct.Mapper;
import ua.com.foxminded.university.api.dto.TeacherDto;
import ua.com.foxminded.university.model.Teacher;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
    TeacherDto teacherToTeacherDTO(Teacher teacher);

    Teacher teacherDtoToTeacher(TeacherDto teacherDto);
}
