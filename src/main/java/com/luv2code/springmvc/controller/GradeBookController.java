package com.luv2code.springmvc.controller;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GradeBookController {

	@Autowired
	private GradeBook gradebook;

	@Autowired
	private StudentAndGradeService studentAndGradeService;


	@GetMapping(value = "/")
	public String getStudents(Model m) {
		Iterable<CollegeStudent> students = studentAndGradeService.getGradeBook();
		m.addAttribute("students", students);
		return "index";
	}


	@GetMapping("/studentInformation/{id}")
		public String studentInformation(@PathVariable int id, Model m) {
		return "studentInformation";
		}

}
