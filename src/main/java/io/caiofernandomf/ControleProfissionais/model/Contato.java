package io.caiofernandomf.ControleProfissionais.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    @Column(nullable = false, name = "created_date")
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
        if(this.profissional!=null){
            stringBuilder.append("Profissional{");
            stringBuilder.append(" ativo=" + this.profissional.getAtivo());
            stringBuilder.append(", created_date=" + this.profissional.getCreated_date() );
            stringBuilder.append(", nascimento=" + this.profissional.getNascimento());
            stringBuilder.append(", cargo=" + this.profissional.getCargo());
            stringBuilder.append(", nome='" + nome + '\'' );
            stringBuilder.append(", id=" + id );
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
}
