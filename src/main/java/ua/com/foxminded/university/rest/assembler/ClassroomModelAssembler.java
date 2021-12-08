package ua.com.foxminded.university.rest.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.Classroom;
import ua.com.foxminded.university.rest.ClassroomRestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public
class ClassroomModelAssembler implements RepresentationModelAssembler<Classroom, EntityModel<Classroom>> {

    @Override
    public EntityModel<Classroom> toModel(Classroom classroom) {
        return EntityModel.of(classroom,
                linkTo(methodOn(ClassroomRestController.class).getClassroom(classroom.getId())).withSelfRel(),
                linkTo(methodOn(ClassroomRestController.class).findAll()).withRel("classrooms"));
    }
}
