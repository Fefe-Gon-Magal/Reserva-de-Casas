package service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.model.Casa;
import com.example.repository.CasaRepository;

@Service
public class CasaService 
{
    
     private final CasaRepository casaRepository;
  
     //Construtor para uma injeção de dependência
  
    public CasaService(CasaRepository casaRepository)
    {
        this.casaRepository = casaRepository;
    }
  
    //Metodo para cadastrar uma nova casa 
    public Casa cadastrarCasa (Casa casa)
    {
     
        //Verifica se o endereço já existe, validar campos, etc.
        return casaRepository.save(casa);

    }
  
  
    public List<Casa> listarTodasAsCasas() 
    {

        return casaRepository.findAll();

    }

    //Método para buscar uma casa por ID
    public Optional<Casa> buscarCasaPorId(Long id)
    {
        return casaRepository.findById(id);
    }
 
    //Método para atualizar uma casa existente
    public Casa atualizarCasa(Long id, Casa casaAtualizada)
    {
        return casaRepository.findById(id).map(casaExistente ->{

            casaExistente.setNome(casaAtualizada.getNome());
            casaExistente.setEndereco(casaAtualizada.getEndereco());
            casaExistente.setDescricao(casaAtualizada.getDescricao());
            casaExistente.setLatitude(casaAtualizada.getLatitude());
            casaExistente.setLongitude(casaAtualizada.getLongitude());
            casaExistente.setQuantidadeQuartos(casaAtualizada.getQuantidadeQuartos());
            casaExistente.setQuantidadeBanheiros(casaAtualizada.getQuantidadeBanheiros());

            casaExistente.setPrecoDiaria(casaAtualizada.getPrecoDiaria());
            casaExistente.setCapacidadePessoas(casaAtualizada.getCapacidadePessoas());

            return casaRepository.save(casaExistente);
        
        }).orElseThrow(()-> new RuntimeException("Casa não encontrada com ID: " + id)); 
    }
        
        //Metódo para deletar a casa
        public void deletarCasa(Long id)
        {
          
          // Uma verificação para garantir que a casa existe antes de deletar
          if (!casaRepository.existsById(id))
          {
            throw new RuntimeException("Casa não encontrada com ID: " + id);
          }

          casaRepository.deleteById(id);
        }

        
    }   