package demoTest.infrastructure.SpringApp.RestServices.controllers;

import demoTest.Domain.Student;
import demoTest.DomianServices.exceptions.student.StudentNotExistsException;
import demoTest.DomianServices.services.IStudentService;
//import demoTest.infrastructure.SpringApp.tenatManger.MultiTenantManager;
import demoTest.infrastructure.SpringApp.tenatManger.TenantNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/students")
@AllArgsConstructor(onConstructor = @__(
        @Inject))
public class StudentController {

    private final IStudentService StudentService;


    @GetMapping(value = "")
    public List<Student> getAll() throws SQLException, TenantNotFoundException {
        return this.StudentService.getAll();
    }

    @GetMapping(value = "/{id}")
    public Student getOne(@PathVariable int id) throws StudentNotExistsException {
        return this.StudentService.getById(id);
    }





}
