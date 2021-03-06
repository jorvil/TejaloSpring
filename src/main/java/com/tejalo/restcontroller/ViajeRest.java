package com.tejalo.restcontroller;


import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.tejalo.entidades.Viaje;
import com.tejalo.repositorio.ReservaRepository;
import com.tejalo.repositorio.ViajeRepository;

@RestController
@RequestMapping("/api")
public class ViajeRest {

	@Autowired
	private ViajeRepository viajeRepository;
	
	@Autowired
	private ReservaRepository reservaRepository;
	
	@PostMapping("/registrarViaje")
	public Viaje RegistrarViaje(@Valid @RequestBody Viaje viaje) {

		return viajeRepository.save(viaje);		
	}	
	
	@GetMapping("/viajes")
	public List<Viaje> obtenerViajes(){		
		return(List<Viaje>) viajeRepository.findAll();		
	}
	
	//
	@GetMapping("/viaje/{codigo}")
	public Optional<Viaje> ObtenerViaje(@PathVariable(value = "codigo") Long codigo) {
		return viajeRepository.findById(codigo);
	}

	@PutMapping("/cancelarViaje/{idViaje}")
	public Viaje CancelarViaje(@PathVariable(value = "idViaje") Long idViaje) {

		Viaje viaje = viajeRepository.findByIdViaje(idViaje);
		viaje.setEstado("C");
		Viaje update = viajeRepository.save(viaje);
		
		reservaRepository.CancelarReservasViaje(viaje.getIdViaje());

		return update;
	}
	
	@PutMapping("/terminarViaje/{idViaje}")
	public Viaje TerminarViaje(@PathVariable(value = "idViaje") Long idViaje) {

		Viaje viaje = viajeRepository.findByIdViaje(idViaje);

		viaje.setEstado("T");
		Viaje update = viajeRepository.save(viaje);
		
		reservaRepository.TerminarReservasViaje(viaje.getIdViaje());

		return update;
	}
	
	@GetMapping("/viaje/{distritoorigen}/{distritodestino}/{fecha}/{codigo}")
	public List<Viaje> obtenerViajeByFetch(@PathVariable(value = "distritoorigen") Long distritoorigen,
			@PathVariable(value = "distritodestino") Long distritodestino,@PathVariable(value = "fecha") String fecha,@PathVariable(value = "codigo") Long codigo) {		
		return viajeRepository.findDataByViajeFecha(distritoorigen, distritodestino,fecha,codigo);
	}
	
	@GetMapping("/viajeConductor/{idConductor}")
	public List<Viaje> obtenerViajeByConductor(@PathVariable(value = "idConductor") Long codigo) {
		return viajeRepository.findDataByViajeUsuario(codigo);
	}
	
}
