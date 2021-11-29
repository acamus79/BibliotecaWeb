/*
 * Sin licencia.
 * Uso para capacitación
 * 2021 Año de la Prevención y Lucha contra el COVID-19.
 */
package com.mza.biblioteca.controladores;

import com.mza.biblioteca.entidades.Autor;
import com.mza.biblioteca.entidades.Editorial;
import com.mza.biblioteca.entidades.Libro;
import com.mza.biblioteca.excepciones.MiExcepcion;
import com.mza.biblioteca.servicios.AutorService;
import com.mza.biblioteca.servicios.EditorialService;
import com.mza.biblioteca.servicios.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Adrian E. Camus
 */
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    LibroService sLibro;

    @Autowired
    EditorialService sEditorial;

    @Autowired
    AutorService sAutor;

    /**
     * Método get que inyecta al modelo un objeto de tipo Libro con los atributos seteados y el formulario de creación de un nuevo Libro funciona como uno de edición del objeto en el modelo.
     * Además inyecta una lista de autores y de editoriales para poder utilizarlos en la vista como una lista desplegable de selección
     * @param modelo ModelMap
     * @param session HttpSession
     * @param id String
     * @return nLibro.html
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/registroLibro")
    public String registro(ModelMap modelo,
                           HttpSession session,
                           @RequestParam(required = false) String id) {

        if (id != null) {
            modelo.addAttribute("libro", sLibro.buscarPorId(id));
            return "redirect:/libros/lista";
        } else {
            Libro aux = new Libro();
            aux.setAlta(Boolean.TRUE);
            aux.setTitulo("Ingrese el título del Libro");
            aux.setSinopsis("Ingrese un resumen del libro, breve y general");
            aux.setAnio(0);
            aux.setEjemplares(0);
            aux.setEjemplaresPrestados(0);
            aux.setEjemplaresRestantes(0);
            aux.setIsbn("Ingrese el código");
            modelo.addAttribute("libro", aux);
        }

        //hermosamente uso los servicios para traerme una lista de autores y editoriales
        List<Autor> autores = sAutor.buscaActivos();
        modelo.addAttribute("autores", autores);

        List<Editorial> editoriales = sEditorial.buscaActivos();
        modelo.addAttribute("editoriales", editoriales);

        return "nLibro";
    }

    /**
     * Método Post que recibe un modelo de un objeto Libro, e intenta persisitirlo mediante un método del Servicio
     * @param modelo ModelMap
     * @param redirectAttributes RedirectAttributes
     * @param libro Libro
     * @param archivo MultipartFile
     * @return nLibro.html
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/registroLibro")
    public String registro(ModelMap modelo,
                           RedirectAttributes redirectAttributes,
                           @ModelAttribute Libro libro,
                           @RequestParam(required = false) MultipartFile archivo) {

        try {
            sLibro.creaLibro(archivo, libro);
            modelo.put("exito", "Registro Exitoso");
            return "redirect:/libros/lista";

        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "nLibro";
        }

    }

    /**
     * Método get para modificar Libros, recibe un ID por parámetro y lo transforma en un objeto Libro mediante un método del servicio.
     * Inyecta en el modelo objetos de tipo Lista con Autores y Editoriales
     * @param modelo ModelMap
     * @param session HttpSession
     * @param id String
     * @return modLibro.html
     * @throws MiExcepcion e
     */
    @GetMapping("/modificar")
    public String modificarLibro(ModelMap modelo,
                                 HttpSession session,
                                 @RequestParam String id) throws MiExcepcion {

        Libro libro = sLibro.buscarPorId(id);
        if (libro == null) {
            throw new MiExcepcion("No se puede asignar el archivo a ese ID");
        }

        //Traemos el listado de Autores y Editoriales para inyectarlos en el tag <select> del HTML
        List<Autor> autores = sAutor.buscaActivos();
        List<Editorial> editoriales = sEditorial.buscaActivos();
        modelo.put("autores", autores);
        modelo.put("editoriales", editoriales);

        //Inyectamos el Libro a modificar con sus atributos actuales
        modelo.put("libro", libro);

        return "modLibro";
    }

    /**
     * Persiste mediante llamado a métodos del Servicio, los cambios en el Libro modificado
     * @param modelo ModelMap
     * @param redirectAttributes RedirectAttributes
     * @param libro libro
     * @param archivo MultipartFile
     * @return modLibro.html
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/modificar")
    public String editar(ModelMap modelo,
                         RedirectAttributes redirectAttributes,
                         @ModelAttribute Libro libro,
                         @RequestParam(required = false) MultipartFile archivo) {
        try {
            sLibro.editaLibro(archivo, libro);
            modelo.put("exito", "El Libro se modificó correctamente");
            return "redirect:/libros/lista";

        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "modLibro";
        }
    }

    @GetMapping("/lista")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    public String listaLibros(ModelMap modelo, HttpSession session, @RequestParam(required = false) String buscar) {
        //si el parametro "buscar" NO es nulo, agrega al modelo una lista de libros buscados
        if (buscar != null) {
            modelo.addAttribute("libros", sLibro.listaBuscada(buscar));

        } else //si no viene parametro de busqueda, agrega al modelo una lista con todos los libros
        {
            modelo.addAttribute("libros", sLibro.listaLibro());
        }
        return "listalibros";
    }

    @GetMapping("/vista")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    public String libro(ModelMap modelo, HttpSession session, @RequestParam(required = false) String buscar) throws MiExcepcion {
        //si el parametro "buscar" NO es nulo, agrega al modelo una lista de libros buscados activos
        if (buscar != null) {
            modelo.addAttribute("libros", sLibro.listaBuscadaActivos(buscar));

        } else //si no viene parametro de busqueda, agrega al modelo una lista con todos los libros activos
        {
            modelo.addAttribute("libros", sLibro.listaActivos());
        }
        return "libros";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/borrar")
    public String borrarLibro(ModelMap modelo, @RequestParam String id) {

        if (id != null) {
            modelo.addAttribute("libro", sLibro.buscarPorId(id));
        }
        return "bLibro";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/borrar")
    public String borrarLibro(ModelMap modelo, RedirectAttributes redirectAttributes, @ModelAttribute Libro libro) {

        try {
            sLibro.bajaLibro(libro);
            modelo.put("exito", "El libro se dio de baja");
            //return "redirect:/libros/lista";
            return "bLibro";
        } catch (MiExcepcion e) {
            modelo.put("error", "NO SE BORRO EL LIBRO");
            return "bLibro";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/activar")
    public String activarLibro(ModelMap modelo, HttpSession session, @RequestParam(required = false) String id) {

        if (id != null) {
            modelo.addAttribute("libro", sLibro.buscarPorId(id));
            return "aLibro";
        }
        return "aLibro";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/activar")
    public String activarLibro(ModelMap modelo, RedirectAttributes redirectAttributes, @ModelAttribute Libro libro) {

        try {
            sLibro.altaLibro(libro);
            modelo.put("exito", "El libro activo correctamente");
            //return "redirect:/libros/lista";
            return "aLibro";
        } catch (MiExcepcion e) {
            modelo.put("error", "NO SE BORRO EL LIBRO");
            return "aLibro";
        }

    }

}
