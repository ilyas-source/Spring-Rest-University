package ua.com.foxminded.university.api.mapper;

import org.mapstruct.Mapper;
import ua.com.foxminded.university.api.dto.SubjectDto;
import ua.com.foxminded.university.model.Subject;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    SubjectDto subjectToSubjectDTO(Subject subject);

    Subject subjectDtoToSubject(SubjectDto subjectDto);
}
