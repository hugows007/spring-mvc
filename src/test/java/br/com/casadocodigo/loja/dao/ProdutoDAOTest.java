package br.com.casadocodigo.loja.dao;

import br.com.casadocodigo.loja.builder.ProdutoBuilder;
import br.com.casadocodigo.loja.conf.DataSourceConfigurationTest;
import br.com.casadocodigo.loja.conf.JPAConfiguration;
import br.com.casadocodigo.loja.models.Produto;
import br.com.casadocodigo.loja.models.TipoPreco;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        JPAConfiguration.class,
        ProdutoDAO.class,
        DataSourceConfigurationTest.class
})
@ActiveProfiles("test")
public class ProdutoDAOTest {

    @Autowired
    ProdutoDAO produtoDAO;

    @Test
    @Transactional
    public void deveSomarTodosPrecosPorTipoLivro() {
        List<Produto> livrosImpressos = ProdutoBuilder
                .newProduto(TipoPreco.IMPRESSO, BigDecimal.TEN)
                .more(3)
                .buildAll();

        List<Produto> livrosEbook = ProdutoBuilder
                .newProduto(TipoPreco.EBOOK, BigDecimal.TEN)
                .more(3)
                .buildAll();

        livrosImpressos.stream().forEach(produtoDAO::gravar);
        livrosEbook.stream().forEach(produtoDAO::gravar);

        BigDecimal valor = produtoDAO.somaPrecosPorTipo(TipoPreco.EBOOK);
        Assert.assertEquals(new BigDecimal(40).setScale(2), valor);
    }

}
