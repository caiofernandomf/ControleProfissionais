package io.caiofernandomf.ControleProfissionais.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "contatos")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false,name = "nome")
    private String nome;

    @Column(nullable = false,name = "contato")
    private String contato;

    @CreationTimestamp
    @Column(nullable = false, name = "created_date", updatable = false)
    private LocalDate created_date;


    @JoinColumn(name = "profissional",referencedColumnName = "id")
    @ManyToOne
//    @JsonBackReference
    @JsonIgnoreProperties("contatos")
    private Profissional profissional;

    public static List<String> camposParaSelect(){
        return
                List.of("id"
                        ,"nome"
                        ,"contato"
                        ,"created_date"
                        ,"profissional");
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if(this.profissional !=null){
            stringBuilder.append("Profissional{");
            //stringBuilder.append(" ativo=" + this.profissional.getAtivo());
            stringBuilder.append(" created_date=" + this.profissional.getCreated_date() );
            stringBuilder.append(", nascimento=" + this.profissional.getNascimento());
            stringBuilder.append(", cargo=" + this.profissional.getCargo());
            stringBuilder.append(", nome='" + this.profissional.getNome() + '\'' );
            stringBuilder.append(", id=" + this.profissional.getId() );
            stringBuilder.append( "}");
        }

        return "Contato{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", contato='" + contato + '\'' +
                ", created_date=" + created_date +
                ", profissional=" + stringBuilder.toString() +
                '}';
    }
    @JsonIgnore
    public Boolean isDetach(){
        return
                null!=this.id &&
                (null==this.nome && null==this.created_date && null==this.contato );
    }

    public ContatoDto toDto(){
        ProfissionalDto profissionalDto = null;
        if(null!= profissional)
            profissionalDto= profissional.toDto();

        ContatoDto contatoDto=new ContatoDto(
                this.id,this.nome,this.contato,this.created_date,profissionalDto
                );

        return contatoDto;
    }

    public ContatoDto toDto(List<String> camposParaPrjection){
        ProfissionalDto profissionalDto = null;
        ContatoDto contatoDto;

        if(null!= profissional)
            profissionalDto= profissional.toDto();

        if(null==profissionalDto)
            profissionalDto= BeanUtils.instantiateClass(BeanUtils.getResolvableConstructor(ProfissionalDto.class),null,null,null,null,null,null);

                  //  new ProfissionalDto(null,null,null,null,null,null);

        if(null!=camposParaPrjection && !camposParaPrjection.isEmpty())
            contatoDto=new ContatoDto(
                camposParaPrjection.contains("id")?this.id:null
                ,camposParaPrjection.contains("nome")?this.nome:null
                ,camposParaPrjection.contains("contato")?this.contato:null
                ,camposParaPrjection.contains("created_date")?this.created_date:null
                ,camposParaPrjection.contains("profissional")?profissionalDto:null
            );
        else
            contatoDto=new ContatoDto(
                    this.id,this.nome,this.contato,this.created_date,profissionalDto);

        return contatoDto;
    }

    public ContatoDto mapToDto(){
        ContatoDto contatoDto=new ContatoDto(
                this.id,this.nome,this.contato,this.created_date,null);

        return contatoDto;
    }

}
