package io.caiofernandomf.ControleProfissionais.model.mapper;

import io.caiofernandomf.ControleProfissionais.model.Contato;
import io.caiofernandomf.ControleProfissionais.model.ContatoDto;
import io.caiofernandomf.ControleProfissionais.model.Profissional;
import io.caiofernandomf.ControleProfissionais.model.ProfissionalDto;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;

public abstract class BeanUtilsMapper extends BeanUtils {

    public static ContatoDto contatoToDto(Contato contato, List<String> camposParaPrjection){
        List<String> campos =
                (Objects.nonNull(camposParaPrjection) ? camposParaPrjection: Contato.camposParaSelect());

        ContatoDto contatoDto=
                instantiateClass(getResolvableConstructor(ContatoDto.class)
                        ,getDadosContato(contato,true,campos));
        System.out.println("contatoToDto: "+contatoDto);
        return contatoDto;
    }

    public static ProfissionalDto profissionalToDto(Profissional profissional,List<String> camposParaPrjection){
        List<String> campos =
                (Objects.nonNull(camposParaPrjection) ? camposParaPrjection: Profissional.camposParaSelect());

        Boolean exibeContatos = (Objects.isNull(camposParaPrjection) || camposParaPrjection.isEmpty());

        ProfissionalDto profissionalDto=
                instantiateClass(getResolvableConstructor(ProfissionalDto.class)
                        ,getDadosProfissional(profissional,exibeContatos,campos));

        return profissionalDto;
    }

    private static Object[] getDadosContato(Contato contato, Boolean comProfissional, List<String> camposParaPrjection){
        Object[] dados = new Object[]{null,null,null,null,null};
        camposParaPrjection.forEach(string -> {
            switch (string){
                case "id": dados[0]=contato.getId();
                break;
                case "nome": dados[1]=contato.getNome();
                    break;
                case "contato": dados[2]=contato.getContato();
                    break;
                case "created_date": dados[3]=contato.getCreated_date();
                    break;
                case "profissional": {

                    dados[4] = getProfissionalDtoOuNulo(contato,comProfissional);
                    break;
                }
            }
        });

        return dados;
    }

    private static ProfissionalDto getProfissionalDtoOuNulo(Contato contato, Boolean comProfissional){
        if (!comProfissional)
            return null;

        if (comProfissional && Objects.nonNull(contato.getProfissional()))
            return instantiateClass(getResolvableConstructor(ProfissionalDto.class)
                    , getDadosProfissional(
                            contato.getProfissional()
                            , !comProfissional
                            , Profissional.camposParaSelect()));

        return criarProfissionalDtoVazio();

    }

    private static Object[] getDadosProfissional(Profissional profissional, Boolean comContato, List<String> camposParaPrjection){
        List<ContatoDto> contatosDto = null;
        Object[] dados = new Object[]{null,null,null,null,null,null};
        camposParaPrjection.forEach(string -> {
                    switch (string){
                        case "id": dados[0]=profissional.getId();
                            break;
                        case "nome": dados[1]=profissional.getNome();
                            break;
                        case "cargo": dados[2]=profissional.getCargo();
                            break;
                        case "nascimento": dados[3]=profissional.getNascimento();
                            break;
                        case "created_date": dados[4]= profissional.getCreated_date();
                            break;
                    }
                }
        );

        if(comContato && Objects.nonNull(profissional.getContatos()))
            contatosDto=
            profissional.getContatos()
                    .stream()
                    .map(contato ->
                            instantiateClass(getResolvableConstructor(ContatoDto.class)
                                    ,getDadosContato(contato,!comContato,Contato.camposParaSelect()))
                    ).toList();
        dados[5]=contatosDto;

        return dados;
    }

    private static ProfissionalDto criarProfissionalDtoVazio(){
        return instantiateClass(getResolvableConstructor(ProfissionalDto.class),null,null,null,null,null,null);
    }

    public static void contatoDtoToContatoUpdate(ContatoDto contatoDto, Contato contato){

        Profissional profissional = null;
        ProfissionalDto profissionalDto = null;
        copyProperties(contatoDto,contato,"id","profissional");

        if(Objects.nonNull(contatoDto.profissional()) && Objects.nonNull(contatoDto.profissional().id())){
            profissionalDto= contatoDto.profissional();
            profissional=new Profissional();

            copyProperties(profissionalDto,profissional,"contatos");

        }
        contato.setProfissional(profissional);


    }

}
