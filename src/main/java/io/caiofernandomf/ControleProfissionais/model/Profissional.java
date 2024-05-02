package io.caiofernandomf.ControleProfissionais.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "profissionais")
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
    @Column(nullable = false)
    private LocalDate created_date;

    @JsonIgnore
    @Column(columnDefinition = "boolean default TRUE",insertable = false)
    private Boolean ativo;

    @OneToMany(mappedBy = "profissional",cascade = CascadeType.ALL)
    @JsonManagedReference
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

    public void inativar(){
        this.setAtivo(Boolean.FALSE);
    }

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
                " ativo=" + ativo +
                ", created_date=" + created_date +
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
}
