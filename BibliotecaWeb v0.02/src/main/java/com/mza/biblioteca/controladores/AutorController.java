/*
 * Sin licencia.
 * Uso para capacitación
 * 2021 Año de la Prevención y Lucha contra el COVID-19.
 */
package com.mza.biblioteca.controladores;

import com.mza.biblioteca.entidades.Autor;
import com.mza.biblioteca.excepciones.MiExcepcion;
import com.mza.biblioteca.servicios.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;


/**
 * @author Adrian E. Camus
 */
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    AutorService autorServicio;

    /**
     * Método get para la creación de nuevos Autores
     * @return nAutor.html
     */
    @GetMapping("/registroAutor")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    public String formulario() {
        return "nAutor.html";
    }

    /**
     * Metodo Post para el registro de nuevos Objetos de la Entidad Autor
     * @param modelo ModelMap
     * @param nombre String
     * @return nAutor.html
     */
    @PostMapping("/registroAutor")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    public String registro(ModelMap modelo, @RequestParam String nombre) {

        try {
            autorServicio.creaAutor(nombre);
            modelo.put("exito", "Registro Exitoso");
            return "nAutor";
        } catch (MiExcepcion e) {
            modelo.put("error", "Ya existe un Autor con ese Nombre");
            return "nAutor";
        }

    }

    /**
     * Get para acceder a la lista de autores, puede recibir un parámetro de búsqueda
     * @param modelo ModelMap
     * @param buscar String
     * @return autores.html
     */
    @GetMapping("/lista")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    public String listaAutores(ModelMap modelo, @RequestParam(required = false) String buscar) {

        //si el parametro "buscar" NO es nulo, agrega al modelo una lista de Autores buscados
        if (buscar != null) {
            modelo.addAttribute("autores", autorServicio.buscaPorNombre(buscar));
        } else //si no viene parametro de busqueda, agrega al modelo una lista con todos los Autores
        {
            modelo.addAttribute("autores", autorServicio.buscaAutores());
        }
        return "autores";
    }

    /**
     * Método GET para desactivar un autor, mediante un método de la clase AutorService, incluye un Objeto de tipo autor en el modelo
     * @param modelo ModelMap
     * @param id String
     * @return bAutor.html
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/borrar")
    public String borrarAutor(ModelMap modelo, @RequestParam(required = false) String id) {

        if (id != null) {
            Optional<Autor> optional = autorServicio.opcionalPorId(id);
            if (optional.isPresent()) {
                modelo.addAttribute("autor", optional.get());
            } else {
                return "redirect:/autores/lista";
            }
        } else {
            return "bAutor";
        }
        return "bAutor";
    }

    /**
     * Método GET para desactivar un autor, recibe en el modelo un objeto de tipo Autor y mediante un método del servicio intenta desactivar el autor e incluye en el modelo un mensaje de éxito
     * @param modelo ModelMap
     * @param redirectAttributes RedirectAttributes
     * @param autor Autor
     * @return bAutor.html
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/borrar")
    public String borrarAutor(ModelMap modelo, RedirectAttributes redirectAttributes, @ModelAttribute Autor autor) {

        try {
            autorServicio.borraAutor(autor);
            modelo.put("exito", "Se Modifico el estado del Autor correctamente");
            return "redirect:/autores/lista";

        } catch (MiExcepcion e) {
            modelo.put("error", "NO SE BORRO EL AUTOR");
            return "bAutor";
        }

    }

    /**
     * Método GET para activar un autor, mediante un método de la clase AutorService, incluye un Objeto de tipo autor en el modelo
     * @param modelo ModelMap
     * @param id String
     * @return aAutor.html
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/activar")
    public String activarAutor(ModelMap modelo, @RequestParam(required = false) String id) {

        if (id != null) {
            Optional<Autor> optional = autorServicio.opcionalPorId(id);
            if (optional.isPresent()) {
                modelo.addAttribute("autor", optional.get());
            } else {
                return "redirect:/autores/lista";
            }
        } else {
            return "aAutor";
        }
        return "aAutor";
    }

    /**
     * Método GET para activar un autor, recibe en el modelo un objeto de tipo Autor y mediante un método del servicio intenta desactivar el autor e incluye en el modelo un mensaje de éxito
     * @param modelo ModelMap
     * @param redirectAttributes RedirectAttributes
     * @param autor Autor
     * @return aAutor.html
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/activar")
    public String activarAutor(ModelMap modelo, RedirectAttributes redirectAttributes, @ModelAttribute Autor autor) {

        try {
            autorServicio.activaAutor(autor);
            modelo.put("exito", "El Autor se activo correctamente");
            return "aAutor";

        } catch (MiExcepcion e) {
            modelo.put("error", "NO SE ACTIVO EL AUTOR");
            return "aAutor";
        }

    }

}
