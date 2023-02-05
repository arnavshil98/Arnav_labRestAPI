package com.crud.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.crud.demo.dao.StudentRepository;
import com.crud.demo.model.Student;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
	
	private final StudentRepository studentRepository;

	@Override
	public List<Student> findAll() {
		 return studentRepository.findAll();
	}

	@Override
	public Student findById(int id) {
		return this.studentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("invalid student id"));
	}

	@Override
	public Student save(Student student) {
		return this.studentRepository.save(student);
	}

	@Override
	public void deleteById(int id) {
		this.studentRepository.deleteById(id);
	}

}
