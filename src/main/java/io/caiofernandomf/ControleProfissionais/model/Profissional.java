package io.caiofernandomf.ControleProfissionais.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "profissionais")
@SoftDelete(strategy = SoftDeleteType.ACTIVE,columnName = "ativo")
//@NamedNativeQuery(query = "SELECT * FROM Profissionais e WHERE e.id=?1 ",name = "findById")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCargo cargo;

    @Column(nullable = false)
    private LocalDate nascimento;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDate created_date;

    /*@JsonIgnore
    //@Column(name = "ativo",columnDefinition = "boolean default TRUE",insertable = false)
    private Boolean ativo=true;*/

//    @OnDelete(action = OnDeleteAction.CASCADE)

    @OneToMany(mappedBy = "profissional"/*,cascade = CascadeType.ALL,orphanRemoval = true*/)
//    @JsonManagedReference
    @JsonIgnoreProperties("profissional")
    private List<Contato> contatos;

    public static List<String> camposParaSelect(){
        return
                List.of("id"
                        ,"nome"
                        ,"cargo"
                        ,"nascimento"
                        ,"created_date");
    }

    /*public void inativar(){
        this.setAtivo(Boolean.FALSE);
    }*/

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        if(this.contatos!=null && !this.contatos.isEmpty()){
            strBuilder.append(",contatos=[");

            strBuilder.append(
            this.contatos.stream().map(contato -> {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("{ id ="+contato.getId());
                stringBuilder.append(",nome ="+contato.getNome());
                stringBuilder.append(" contato ="+contato.getContato());
                stringBuilder.append(" created_date ="+contato.getCreated_date());
                stringBuilder.append("}");
                return stringBuilder.toString();
            }).collect(Collectors.joining(","))
            );
            strBuilder.append("]");
        }
        return "Profissional{" +
                " created_date=" + created_date +
                ", nascimento=" + nascimento +
                ", cargo=" + cargo +
                ", nome='" + nome + '\'' +
                ", id=" + id +
                ",contatos=" + strBuilder.toString() +
                '}';
    }

    public Profissional(long id){
        this.id=id;
    }

    public ProfissionalDto toDto(){
        ProfissionalDto profissionalDto=new ProfissionalDto(
                this.id,this.nome,this.cargo,this.nascimento,this.created_date,null);

        return profissionalDto;
    }

    public ProfissionalDto toDto(List<String> camposParaPrjection){
        ProfissionalDto profissionalDto = null;


        if(null!=camposParaPrjection && !camposParaPrjection.isEmpty())
            profissionalDto=new ProfissionalDto(
                    camposParaPrjection.contains("id")?this.id:null
                    ,camposParaPrjection.contains("nome")?this.nome:null
                    ,camposParaPrjection.contains("cargo")?this.cargo:null
                    ,camposParaPrjection.contains("nascimento")?this.nascimento:null
                    ,camposParaPrjection.contains("created_date")?this.created_date:null
                    ,null
            );
        else
            profissionalDto=mapToDto();

        return profissionalDto;
    }

    public ProfissionalDto mapToDto(){
        List<ContatoDto> listaContatoDto = contatos.stream().filter(Objects::nonNull).map(Contato::mapToDto).toList();
        ProfissionalDto profissionalDto=new ProfissionalDto(
                this.id,this.nome,this.cargo,this.nascimento,this.created_date,listaContatoDto);

        return profissionalDto;
    }
}
