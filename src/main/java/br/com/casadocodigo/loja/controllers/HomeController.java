package br.com.casadocodigo.loja.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.com.casadocodigo.loja.dao.UsuarioDAO;
import br.com.casadocodigo.loja.models.Role;
import br.com.casadocodigo.loja.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.casadocodigo.loja.dao.ProdutoDAO;
import br.com.casadocodigo.loja.models.Produto;

@Controller
public class HomeController {

	@Autowired
	private ProdutoDAO produtoDao;

	@Autowired
	private UsuarioDAO usuarioDAO;

	@RequestMapping("/")
	@Cacheable(value="produtosHome")
	public ModelAndView index() {
		List<Produto> produtos = produtoDao.listar();
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("produtos", produtos);
		
		return modelAndView;
	}

	@Transactional
	@ResponseBody
	@RequestMapping("/adiciona-usuario-senha-master")
	public String urlAdicionaUsuarioAdminHeroku(){
		Usuario usuario = new Usuario();
		usuario.setNome("Hugo");
		usuario.setEmail("hugobezerrapimentel@gmail.com");
		usuario.setSenha("$2a$10$lt7pS7Kxxe5JfP.vjLNSyOXP11eHgh7RoPxo5fvvbMCZkCUss2DGu");
		usuario.setRoles(Collections.singletonList(new Role("ROLE_ADMIN")));

		usuarioDAO.gravar(usuario);

		return "Feito";
	}

}
