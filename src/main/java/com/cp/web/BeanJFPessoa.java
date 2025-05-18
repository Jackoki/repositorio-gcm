
package com.cp.web;

import com.cp.data.crud.BeanCrudPessoa;
import com.cp.data.crud.BeanCrudCidade;
import com.cp.data.model.Pessoa;
import com.cp.util.GenId;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author utfpr
 */
@Named("beanJFPessoa")
@RequestScoped
public class BeanJFPessoa {
    
    @Getter @Setter
    private int id;
    
    @Getter @Setter
    private String nome;

    @Getter @Setter
    private int cidade;
    
    @EJB 
    BeanCrudPessoa beanPessoa;

    @EJB
    BeanCrudCidade beanCidade;
            
    public void add() {
        if (!validate()) return;

        Pessoa p = createPerson();
        beanPessoa.persist(p);
        beanPessoa.merge(p);
    }

    private boolean validate() {
        if(id == 0){
            FacesContext.getCurrentInstance().addMessage("ERRO", new FacesMessage("Erro: Código não pode ser zero."));
            return false;
        }

        if(beanPessoa.find(id) != null){
            FacesContext.getCurrentInstance().addMessage("ERRO", new FacesMessage("Erro: Código existente."));
            return false;
        }

        return true;
    }

    private Pessoa createPerson() {
        Pessoa p = new Pessoa();
        p.setNome(nome);
        p.setId(id);
        p.setCidade(beanCidade.find(cidade));
        return p;
    }

    
    public List<Pessoa> getAll(){
        return beanPessoa.getAll();
    }

    public void newID(){
        this.id=new GenId().getIdPrimo();
        this.nome=new GenId().getNome();
    }
}
