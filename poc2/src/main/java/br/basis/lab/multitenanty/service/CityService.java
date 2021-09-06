package br.basis.lab.multitenanty.service;

import br.basis.lab.multitenanty.model.City;
import br.basis.lab.multitenanty.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public void save(City city){
        cityRepository.save(city);
    }

    public List<City> getAll() throws SQLException {
        return cityRepository.findAll();
    }

    public City get(Long id){
        return cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cidade não encontrada!") );
    }

    public City getByName(String name){
        return cityRepository.findByName(name);
    }

    public void delete(String name){
        cityRepository.deleteByName(name);
    }
}
