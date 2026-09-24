package com.example.CandidatosTSE.controller;

import com.example.CandidatosTSE.model.Candidato;
import com.example.CandidatosTSE.service.CandidatosTseService;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CandidatosTseController {

    private final CandidatosTseService service;
    private final boolean exibirFotos;

    public CandidatosTseController(CandidatosTseService service,
            @Value("${app.exibir-fotos:false}") boolean exibirFotos) {
        this.service = service;
        this.exibirFotos = exibirFotos;
    }

    @GetMapping("/")
    public String index(
            @RequestParam(name = "cargo", required = false) String cargo,
            @RequestParam(name = "partido", required = false) String partido,
            @RequestParam(name = "texto", required = false) String texto,
            Model model) {
        List<Candidato> candidatos = service.filtrar(cargo, partido, texto);

        model.addAttribute("candidatos", candidatos);
        model.addAttribute("exibirFotos", exibirFotos);
        model.addAttribute("total", candidatos.size());
        model.addAttribute("cargos", service.listarCargos());
        model.addAttribute("partidos", service.listarPartidos());
        model.addAttribute("cargoSelecionado", cargo == null ? "" : cargo);
        model.addAttribute("partidoSelecionado", partido == null ? "" : partido);
        model.addAttribute("textoSelecionado", texto == null ? "" : texto);
        return "index";
    }
}
