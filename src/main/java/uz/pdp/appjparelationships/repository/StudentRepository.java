package uz.pdp.appjparelationships.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;

public interface StudentRepository extends JpaRepository<Student, Integer> {


    Page<Student> findAllByGroup_Faculty_UniversityId(Integer group_faculty_university_id, Pageable pageable);

    @Query("select s from Student s where s.group.faculty.id = ?1")
    Page<Student> findAllByGroup_FacultyId(Integer group_faculty_id, Pageable pageable);

    @Query("select s from Student s where s.group.id = ?1")
    Page<Student> findAllByGroupId(Integer group_id, Pageable pageable);
}
