package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.payload.StudentDTO;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    SubjectRepository subjectRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

    //3. FACULTY DEKANAT
    @GetMapping("/forDean/{deanId}")
    public Page<Student> getStudentListForDean(@PathVariable Integer deanId, @RequestParam int page){
        Pageable pageable = PageRequest.of(page, 10 );
        return studentRepository.findAllByGroup_FacultyId(deanId,pageable);
    }
    //4. GROUP OWNER

    @GetMapping("/forGroup/{groupId}")
    public Page<Student> getStudentListForGroup(@PathVariable Integer groupId, @RequestParam int page){
        Pageable pageable = PageRequest.of(page, 10 );
        return studentRepository.findAllByGroupId(groupId, pageable);
    }

    @PostMapping
    public String addStudent(@RequestBody StudentDTO studentDTO){

        if (!addressRepository.existsById(studentDTO.getAddressId()))
            return "No such address found";

        if (!groupRepository.existsById(studentDTO.getGroupId()))
            return "No such group found";

        Student student = new Student();
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setAddress(addressRepository.getOne(studentDTO.getAddressId()));
        student.setGroup(groupRepository.getOne(studentDTO.getGroupId()));
        student.setSubjects(subjectRepository.findAllById(studentDTO.getSubjectIds()));
        studentRepository.save(student);
        return "Student saved";
    }

    @PutMapping("/{id}")
    public String editStudent(@RequestBody StudentDTO studentDTO, @PathVariable Integer id){
        if (subjectRepository.existsById(id))
            return "No such student";

        if (!addressRepository.existsById(studentDTO.getAddressId()))
            return "No such address found";

        if (!groupRepository.existsById(studentDTO.getGroupId()))
            return "No such group found";

        Student student = studentRepository.getOne(id);
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setAddress(addressRepository.getOne(studentDTO.getAddressId()));
        student.setGroup(groupRepository.getOne(studentDTO.getGroupId()));
        student.setSubjects(subjectRepository.findAllById(studentDTO.getSubjectIds()));
        studentRepository.save(student);
        return "Student saved";
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Integer id){
        if (!studentRepository.existsById(id))
            return "No such student";
        studentRepository.deleteById(id);
        return "Student deleted";
    }


}
