package com.dlerroan.course.boot.web.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dlerroan.course.boot.domain.Employee;
import com.dlerroan.course.boot.domain.Role;
import com.dlerroan.course.boot.domain.UF;
import com.dlerroan.course.boot.service.EmployeeService;
import com.dlerroan.course.boot.service.RoleService;
import com.dlerroan.course.boot.web.validator.EmployeeValidator;

@Controller
@RequestMapping("/funcionarios")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private RoleService roleService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new EmployeeValidator());
	}
	
	@GetMapping("/cadastrar")
	public String insert(Employee employee) {
		return "funcionario/cadastro";
	}
	
	@GetMapping("/listar")
	public String findAll(ModelMap model) {
		model.addAttribute("funcionarios", employeeService.findAll());
		return "funcionario/lista";
	}
	
	@PostMapping("/salvar")
	public String save(@Valid Employee employee, BindingResult result, RedirectAttributes attr) {
		
		if(result.hasErrors()) {
			return "funcionario/cadastro";
		}
		
		employeeService.save(employee);
		attr.addFlashAttribute("success", "Funcionário inserido com sucesso");
		return "redirect:/funcionarios/cadastrar";
	}
	
	@GetMapping("/editar/{id}")
	public String preUpdate(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("employee", employeeService.findById(id));
		return "funcionario/cadastro";
	}
	
	@PostMapping("/editar")
	public String update(@Valid Employee employee, BindingResult result, RedirectAttributes attr) {
		
		if(result.hasErrors()) {
			return "funcionario/cadastro";
		}
		
		employeeService.update(employee);
		attr.addFlashAttribute("success", "Registro atualizado com sucesso.");
		return "redirect:/funcionarios/cadastrar";
	}
	
	@GetMapping("/excluir/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes attr) {
			employeeService.delete(id);
			attr.addFlashAttribute("success", "Funcionário removido com sucesso");
			return "redirect:/funcionarios/listar";
	}
	
	@GetMapping("/buscar/nome")
	public String getByName(@RequestParam("nome") String name, ModelMap model) {
		model.addAttribute("funcionarios", employeeService.findByName(name));
		return "funcionario/lista";
	}
	
	@GetMapping("/buscar/cargo")
	public String getByRole(@RequestParam("id") Long id, ModelMap model) {
		model.addAttribute("funcionarios", employeeService.findByRole(id));
		return "funcionario/lista";
	}
	
	@GetMapping("/buscar/data")
	public String getByDate(@RequestParam("entrada") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate in, 
			@RequestParam("saida") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate out, 
			ModelMap model) {
		model.addAttribute("funcionarios", employeeService.findByDate(in, out));
		return "funcionario/lista";
	}
	
	@ModelAttribute("cargos")
	public List<Role> getRoles(){
		return roleService.findAll();
	}
	
	@ModelAttribute("ufs")
	public UF[] getUFs() {
		return UF.values();
	}
}
