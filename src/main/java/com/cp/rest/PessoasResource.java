package com.cp.rest;

import com.cp.data.crud.BeanCrudCidade;
import com.cp.data.crud.BeanCrudPessoa;
import com.cp.data.model.Cidade;
import com.cp.data.model.Pessoa;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.stream.Collectors;

@Path("pessoas")
@Stateless
public class PessoasResource {

    @EJB
    private BeanCrudPessoa beanCrudPessoa;

    @EJB
    private BeanCrudCidade beanCrudCidade;

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<JsonPessoa> getAll() {
        return beanCrudPessoa.getAll().stream()
                .map(pessoa -> new JsonPessoa(
                        pessoa.getId(),
                        pessoa.getNome(),
                        pessoa.getCidade() != null 
                                ? new JsonCidade(pessoa.getCidade().getId(), pessoa.getCidade().getNome()) 
                                : null))
                .collect(Collectors.toList());
    }

    @GET
    @Path("cid")
    @Produces(MediaType.APPLICATION_JSON)
    public List<JsonCidade> getCids() {
        return beanCrudCidade.getAll().stream()
                .map(cidade -> new JsonCidade(cidade.getId(), cidade.getNome()))
                .collect(Collectors.toList());
    }

    public record JsonCidade(int id, String nome) {}
    public record JsonPessoa(int id, String nome, JsonCidade cidade) {}
}
