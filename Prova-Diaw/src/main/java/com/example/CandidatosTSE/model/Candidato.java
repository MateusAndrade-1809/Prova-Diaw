package com.example.CandidatosTSE.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class Candidato {

    private String sqCandidato;      
    private String nrCandidato;      
    private String nomeCandidato;    
    private String nomeUrna;         
    private String cargo;            
    private String siglaPartido;     
    private String nomePartido;      
    private String uf;               
    private String municipio;        
    private String situacaoCandidatura; 
    private String genero;           
    private String grauInstrucao;    
    private String ocupacao;         
    private String dtNascimento;     
    private String nrCpfCandidato;  

    private String sqCandidatoParaFoto;

    public Candidato() {
    }

    public String getSqCandidato() {
        return sqCandidato;
    }

    public void setSqCandidato(String sqCandidato) {
        this.sqCandidato = sqCandidato;
    }

    public String getNrCandidato() {
        return nrCandidato;
    }

    public void setNrCandidato(String nrCandidato) {
        this.nrCandidato = nrCandidato;
    }

    public String getNomeCandidato() {
        return nomeCandidato;
    }

    public void setNomeCandidato(String nomeCandidato) {
        this.nomeCandidato = nomeCandidato;
    }

    public String getNomeUrna() {
        return nomeUrna;
    }

    public void setNomeUrna(String nomeUrna) {
        this.nomeUrna = nomeUrna;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getSiglaPartido() {
        return siglaPartido;
    }

    public void setSiglaPartido(String siglaPartido) {
        this.siglaPartido = siglaPartido;
    }

    public String getNomePartido() {
        return nomePartido;
    }

    public void setNomePartido(String nomePartido) {
        this.nomePartido = nomePartido;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getSituacaoCandidatura() {
        return situacaoCandidatura;
    }

    public void setSituacaoCandidatura(String situacaoCandidatura) {
        this.situacaoCandidatura = situacaoCandidatura;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getGrauInstrucao() {
        return grauInstrucao;
    }

    public void setGrauInstrucao(String grauInstrucao) {
        this.grauInstrucao = grauInstrucao;
    }

    public String getOcupacao() {
        return ocupacao;
    }

    public void setOcupacao(String ocupacao) {
        this.ocupacao = ocupacao;
    }

    public String getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(String dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getNrCpfCandidato() {
        return nrCpfCandidato;
    }

    public void setNrCpfCandidato(String nrCpfCandidato) {
        this.nrCpfCandidato = nrCpfCandidato;
    }

    public void setSqCandidatoParaFoto(String sqCandidatoParaFoto) {
        this.sqCandidatoParaFoto = sqCandidatoParaFoto;
    }


    public String getNomeArquivoFoto() {
        String sq = (sqCandidatoParaFoto != null) ? sqCandidatoParaFoto : sqCandidato;
        return "FMG" + sq + "_div.jpg";
    }

    private static final DateTimeFormatter FORMATO_DATA_TSE = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public int getIdade() {
        if (dtNascimento == null || dtNascimento.isBlank()) {
            return -1;
        }
        try {
            LocalDate nascimento = LocalDate.parse(dtNascimento, FORMATO_DATA_TSE);
            return Period.between(nascimento, LocalDate.now()).getYears();
        } catch (DateTimeParseException e) {
            return -1;
        }
    }
}