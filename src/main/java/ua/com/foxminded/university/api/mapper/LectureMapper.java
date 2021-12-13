package ua.com.foxminded.university.api.mapper;

import org.mapstruct.Mapper;
import ua.com.foxminded.university.api.dto.LectureDto;
import ua.com.foxminded.university.model.Lecture;

@Mapper(componentModel = "spring")
public interface LectureMapper {
    LectureDto lectureToLectureDTO(Lecture lecture);

    Lecture lectureDtoToLecture(LectureDto lectureDto);

}
