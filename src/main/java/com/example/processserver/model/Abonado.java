package com.example.processserver.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "ABONADOS")
public class Abonado {

    @Id
    @Column(name = "AboCod", nullable = false)
    private Integer aboCod;

    @Column(name = "AboNom", length = 25)
    private String aboNom;

    @Column(name = "AboApe", length = 25)
    private String aboApe;

    @Column(name = "AboDir", length = 50)
    private String aboDir;

    @Column(name = "AboDir1", length = 200)
    private String aboDir1;

    @Column(name = "AboTel", length = 15)
    private String aboTel;

    @Column(name = "AboTel1", length = 15)
    private String aboTel1;

    @Column(name = "AboTel2", length = 15)
    private String aboTel2;

    @Column(name = "AboFchIng")
    private Instant aboFchIng;

    @Column(name = "AboFchIns")
    private Instant aboFchIns;

    @Column(name = "AboFchCort")
    private Instant aboFchCort;

    @Column(name = "AboDocNro", length = 15)
    private String aboDocNro;

    @Column(name = "AboSts")
    private Character aboSts;

    @Column(name = "AboCodCli")
    private Integer aboCodCli;

    @Column(name = "SecNro", length = 8)
    private String secNro;

    @Column(name = "SectCod", length = 3)
    private String sectCod;

    @Column(name = "AboFchUltF")
    private Instant aboFchUltF;

    @Column(name = "AboRif", length = 15)
    private String aboRif;

    @Column(name = "AboNatJur")
    private Character aboNatJur;

    @Column(name = "AboRuc", length = 50)
    private String aboRuc;

    @Column(name = "AboTap")
    private Integer aboTap;

    @Column(name = "AboBoca")
    private Short aboBoca;

    @Column(name = "AboNroPre", length = 15)
    private String aboNroPre;

    @Column(name = "AboSis")
    private Character aboSis;

    @Column(name = "TarBcoNroC", length = 20)
    private String tarBcoNroC;

    @Column(name = "TarFchVto")
    private Instant tarFchVto;

    @Column(name = "GrpAfiCod")
    private Short grpAfiCod;

    @Column(name = "MedCobCod")
    private Short medCobCod;

    @Column(name = "AboCodAnt", length = 8)
    private String aboCodAnt;

    @Column(name = "AboFchNac")
    private Instant aboFchNac;

    @Column(name = "CalleNro")
    private Integer calleNro;

    @Column(name = "NroCasa", length = 6)
    private String nroCasa;

    @Column(name = "EdifCod", length = 3)
    private String edifCod;

    @Column(name = "AboStsVer")
    private Character aboStsVer;

    @Column(name = "AboTapNro")
    private Short aboTapNro;

    @Column(name = "MsjCod")
    private Short msjCod;

    @Column(name = "TarBcoDsc", length = 30)
    private String tarBcoDsc;

    @Column(name = "NodoCod", length = 8)
    private String nodoCod;

    @Column(name = "UrbCod")
    private Short urbCod;

    @Column(name = "ManNro")
    private Short manNro;

    @Column(name = "VeredaCod", length = 4)
    private String veredaCod;

    @Column(name = "NroPiso", length = 3)
    private String nroPiso;

    @Column(name = "NroApto", length = 5)
    private String nroApto;

    @Column(name = "AboApeC", length = 25)
    private String aboApeC;

    @Column(name = "AboNomC", length = 25)
    private String aboNomC;

    @Column(name = "AboTelTrab", length = 15)
    private String aboTelTrab;

    @Column(name = "AboDocNroC", length = 15)
    private String aboDocNroC;

    @Column(name = "AboTelCelC", length = 15)
    private String aboTelCelC;

    @Column(name = "AboAlqPro")
    private Character aboAlqPro;

    @Column(name = "AboAlqFecV")
    private Instant aboAlqFecV;

    @Column(name = "AboIde")
    private Character aboIde;

    @Column(name = "AboEmail", length = 50)
    private String aboEmail;

    @Column(name = "AboTelCod", length = 4)
    private String aboTelCod;

    @Column(name = "AboTelCod1", length = 4)
    private String aboTelCod1;

    @Column(name = "AboTelCod2", length = 4)
    private String aboTelCod2;

    @Column(name = "PIntNetCod")
    private Integer pIntNetCod;

    @Column(name = "AboTelOfiC", length = 4)
    private String aboTelOfiC;

    @Column(name = "AboTelOfi", length = 15)
    private String aboTelOfi;

    @Column(name = "AboTelCod4", length = 4)
    private String aboTelCod4;

    @Column(name = "AboTel4", length = 15)
    private String aboTel4;

    @Column(name = "TarAboNom", length = 25)
    private String tarAboNom;

    @Column(name = "TarAboApe", length = 25)
    private String tarAboApe;

    @Column(name = "TarCedula", length = 15)
    private String tarCedula;

    @Column(name = "TarAboTelC", length = 4)
    private String tarAboTelC;

    @Column(name = "TarAboTel", length = 15)
    private String tarAboTel;

    @Column(name = "TarAboCelC", length = 4)
    private String tarAboCelC;

    @Column(name = "TarAboCel", length = 15)
    private String tarAboCel;

    @Column(name = "Calles", length = 20)
    private String calles;

    @Column(name = "CaTrAv", length = 20)
    private String caTrAv;

    @Column(name = "CaNroPa", length = 15)
    private String caNroPa;

    @Column(name = "AboCivil")
    private Character aboCivil;

    @Column(name = "AboSexo")
    private Character aboSexo;

    @Column(name = "AboProfesi", length = 20)
    private String aboProfesi;

    @Column(name = "AboPoste", length = 15)
    private String aboPoste;

    @Column(name = "AboCortCnt")
    private Short aboCortCnt;

    @Column(name = "AboNroPreA", length = 15)
    private String aboNroPreA;

    @Column(name = "AboSistema")
    private Character aboSistema;

    @Column(name = "AboDiaInsc")
    private Short aboDiaInsc;

    @Column(name = "AboDia")
    private Instant aboDia;

    @Column(name = "AboPgoDes")
    private Instant aboPgoDes;

    @Column(name = "AboPgoHas")
    private Instant aboPgoHas;

    @Column(name = "AboDebDes")
    private Instant aboDebDes;

    @Column(name = "AboDebHas")
    private Instant aboDebHas;

    @Column(name = "AboMtoDeud")
    private BigDecimal aboMtoDeud;

    @Column(name = "AboFchReco")
    private Instant aboFchReco;

    @Column(name = "AboObserva")
    private String aboObserva;

    @Column(name = "AboObsv")
    private String aboObsv;

    @Column(name = "AboFacEle")
    private Character aboFacEle;

    @Column(name = "AboIp", length = 15)
    private String aboIp;

    @Column(name = "AboGpon")
    private Character aboGpon;

    // Getters y setters

    public Integer getAboCod() {
        return aboCod;
    }

    public void setAboCod(Integer aboCod) {
        this.aboCod = aboCod;
    }

    public String getAboNom() {
        return aboNom;
    }

    public void setAboNom(String aboNom) {
        this.aboNom = aboNom;
    }

    public String getAboApe() {
        return aboApe;
    }

    public void setAboApe(String aboApe) {
        this.aboApe = aboApe;
    }

    public String getAboDir() {
        return aboDir;
    }

    public void setAboDir(String aboDir) {
        this.aboDir = aboDir;
    }

    public String getAboDir1() {
        return aboDir1;
    }

    public void setAboDir1(String aboDir1) {
        this.aboDir1 = aboDir1;
    }

    public String getAboTel() {
        return aboTel;
    }

    public void setAboTel(String aboTel) {
        this.aboTel = aboTel;
    }

    public String getAboTel1() {
        return aboTel1;
    }

    public void setAboTel1(String aboTel1) {
        this.aboTel1 = aboTel1;
    }

    public String getAboTel2() {
        return aboTel2;
    }

    public void setAboTel2(String aboTel2) {
        this.aboTel2 = aboTel2;
    }

    public Instant getAboFchIng() {
        return aboFchIng;
    }

    public void setAboFchIng(Instant aboFchIng) {
        this.aboFchIng = aboFchIng;
    }

    public Instant getAboFchIns() {
        return aboFchIns;
    }

    public void setAboFchIns(Instant aboFchIns) {
        this.aboFchIns = aboFchIns;
    }

    public Instant getAboFchCort() {
        return aboFchCort;
    }

    public void setAboFchCort(Instant aboFchCort) {
        this.aboFchCort = aboFchCort;
    }

    public String getAboDocNro() {
        return aboDocNro;
    }

    public void setAboDocNro(String aboDocNro) {
        this.aboDocNro = aboDocNro;
    }

    public Character getAboSts() {
        return aboSts;
    }

    public void setAboSts(Character aboSts) {
        this.aboSts = aboSts;
    }

    public Integer getAboCodCli() {
        return aboCodCli;
    }

    public void setAboCodCli(Integer aboCodCli) {
        this.aboCodCli = aboCodCli;
    }

    public String getSecNro() {
        return secNro;
    }

    public void setSecNro(String secNro) {
        this.secNro = secNro;
    }

    public String getSectCod() {
        return sectCod;
    }

    public void setSectCod(String sectCod) {
        this.sectCod = sectCod;
    }

    public Instant getAboFchUltF() {
        return aboFchUltF;
    }

    public void setAboFchUltF(Instant aboFchUltF) {
        this.aboFchUltF = aboFchUltF;
    }

    public String getAboRif() {
        return aboRif;
    }

    public void setAboRif(String aboRif) {
        this.aboRif = aboRif;
    }

    public Character getAboNatJur() {
        return aboNatJur;
    }

    public void setAboNatJur(Character aboNatJur) {
        this.aboNatJur = aboNatJur;
    }

    public String getAboRuc() {
        return aboRuc;
    }

    public void setAboRuc(String aboRuc) {
        this.aboRuc = aboRuc;
    }

    public Integer getAboTap() {
        return aboTap;
    }

    public void setAboTap(Integer aboTap) {
        this.aboTap = aboTap;
    }

    public Short getAboBoca() {
        return aboBoca;
    }

    public void setAboBoca(Short aboBoca) {
        this.aboBoca = aboBoca;
    }

    public String getAboNroPre() {
        return aboNroPre;
    }

    public void setAboNroPre(String aboNroPre) {
        this.aboNroPre = aboNroPre;
    }

    public Character getAboSis() {
        return aboSis;
    }

    public void setAboSis(Character aboSis) {
        this.aboSis = aboSis;
    }

    public String getTarBcoNroC() {
        return tarBcoNroC;
    }

    public void setTarBcoNroC(String tarBcoNroC) {
        this.tarBcoNroC = tarBcoNroC;
    }

    public Instant getTarFchVto() {
        return tarFchVto;
    }

    public void setTarFchVto(Instant tarFchVto) {
        this.tarFchVto = tarFchVto;
    }

    public Short getGrpAfiCod() {
        return grpAfiCod;
    }

    public void setGrpAfiCod(Short grpAfiCod) {
        this.grpAfiCod = grpAfiCod;
    }

    public Short getMedCobCod() {
        return medCobCod;
    }

    public void setMedCobCod(Short medCobCod) {
        this.medCobCod = medCobCod;
    }

    public String getAboCodAnt() {
        return aboCodAnt;
    }

    public void setAboCodAnt(String aboCodAnt) {
        this.aboCodAnt = aboCodAnt;
    }

    public Instant getAboFchNac() {
        return aboFchNac;
    }

    public void setAboFchNac(Instant aboFchNac) {
        this.aboFchNac = aboFchNac;
    }

    public Integer getCalleNro() {
        return calleNro;
    }

    public void setCalleNro(Integer calleNro) {
        this.calleNro = calleNro;
    }

    public String getNroCasa() {
        return nroCasa;
    }

    public void setNroCasa(String nroCasa) {
        this.nroCasa = nroCasa;
    }

    public String getEdifCod() {
        return edifCod;
    }

    public void setEdifCod(String edifCod) {
        this.edifCod = edifCod;
    }

    public Character getAboStsVer() {
        return aboStsVer;
    }

    public void setAboStsVer(Character aboStsVer) {
        this.aboStsVer = aboStsVer;
    }

    public Short getAboTapNro() {
        return aboTapNro;
    }

    public void setAboTapNro(Short aboTapNro) {
        this.aboTapNro = aboTapNro;
    }

    public Short getMsjCod() {
        return msjCod;
    }

    public void setMsjCod(Short msjCod) {
        this.msjCod = msjCod;
    }

    public String getTarBcoDsc() {
        return tarBcoDsc;
    }

    public void setTarBcoDsc(String tarBcoDsc) {
        this.tarBcoDsc = tarBcoDsc;
    }

    public String getNodoCod() {
        return nodoCod;
    }

    public void setNodoCod(String nodoCod) {
        this.nodoCod = nodoCod;
    }

    public Short getUrbCod() {
        return urbCod;
    }

    public void setUrbCod(Short urbCod) {
        this.urbCod = urbCod;
    }

    public Short getManNro() {
        return manNro;
    }

    public void setManNro(Short manNro) {
        this.manNro = manNro;
    }

    public String getVeredaCod() {
        return veredaCod;
    }

    public void setVeredaCod(String veredaCod) {
        this.veredaCod = veredaCod;
    }

    public String getNroPiso() {
        return nroPiso;
    }

    public void setNroPiso(String nroPiso) {
        this.nroPiso = nroPiso;
    }

    public String getNroApto() {
        return nroApto;
    }

    public void setNroApto(String nroApto) {
        this.nroApto = nroApto;
    }

    public String getAboApeC() {
        return aboApeC;
    }

    public void setAboApeC(String aboApeC) {
        this.aboApeC = aboApeC;
    }

    public String getAboNomC() {
        return aboNomC;
    }

    public void setAboNomC(String aboNomC) {
        this.aboNomC = aboNomC;
    }

    public String getAboTelTrab() {
        return aboTelTrab;
    }

    public void setAboTelTrab(String aboTelTrab) {
        this.aboTelTrab = aboTelTrab;
    }

    public String getAboDocNroC() {
        return aboDocNroC;
    }

    public void setAboDocNroC(String aboDocNroC) {
        this.aboDocNroC = aboDocNroC;
    }

    public String getAboTelCelC() {
        return aboTelCelC;
    }

    public void setAboTelCelC(String aboTelCelC) {
        this.aboTelCelC = aboTelCelC;
    }

    public Character getAboAlqPro() {
        return aboAlqPro;
    }

    public void setAboAlqPro(Character aboAlqPro) {
        this.aboAlqPro = aboAlqPro;
    }

    public Instant getAboAlqFecV() {
        return aboAlqFecV;
    }

    public void setAboAlqFecV(Instant aboAlqFecV) {
        this.aboAlqFecV = aboAlqFecV;
    }

    public Character getAboIde() {
        return aboIde;
    }

    public void setAboIde(Character aboIde) {
        this.aboIde = aboIde;
    }

    public String getAboEmail() {
        return aboEmail;
    }

    public void setAboEmail(String aboEmail) {
        this.aboEmail = aboEmail;
    }

    public String getAboTelCod() {
        return aboTelCod;
    }

    public void setAboTelCod(String aboTelCod) {
        this.aboTelCod = aboTelCod;
    }

    public String getAboTelCod1() {
        return aboTelCod1;
    }

    public void setAboTelCod1(String aboTelCod1) {
        this.aboTelCod1 = aboTelCod1;
    }

    public String getAboTelCod2() {
        return aboTelCod2;
    }

    public void setAboTelCod2(String aboTelCod2) {
        this.aboTelCod2 = aboTelCod2;
    }

    public Integer getpIntNetCod() {
        return pIntNetCod;
    }

    public void setpIntNetCod(Integer pIntNetCod) {
        this.pIntNetCod = pIntNetCod;
    }

    public String getAboTelOfiC() {
        return aboTelOfiC;
    }

    public void setAboTelOfiC(String aboTelOfiC) {
        this.aboTelOfiC = aboTelOfiC;
    }

    public String getAboTelOfi() {
        return aboTelOfi;
    }

    public void setAboTelOfi(String aboTelOfi) {
        this.aboTelOfi = aboTelOfi;
    }

    public String getAboTelCod4() {
        return aboTelCod4;
    }

    public void setAboTelCod4(String aboTelCod4) {
        this.aboTelCod4 = aboTelCod4;
    }

    public String getAboTel4() {
        return aboTel4;
    }

    public void setAboTel4(String aboTel4) {
        this.aboTel4 = aboTel4;
    }

    public String getTarAboNom() {
        return tarAboNom;
    }

    public void setTarAboNom(String tarAboNom) {
        this.tarAboNom = tarAboNom;
    }

    public String getTarAboApe() {
        return tarAboApe;
    }

    public void setTarAboApe(String tarAboApe) {
        this.tarAboApe = tarAboApe;
    }

    public String getTarCedula() {
        return tarCedula;
    }

    public void setTarCedula(String tarCedula) {
        this.tarCedula = tarCedula;
    }

    public String getTarAboTelC() {
        return tarAboTelC;
    }

    public void setTarAboTelC(String tarAboTelC) {
        this.tarAboTelC = tarAboTelC;
    }

    public String getTarAboTel() {
        return tarAboTel;
    }

    public void setTarAboTel(String tarAboTel) {
        this.tarAboTel = tarAboTel;
    }

    public String getTarAboCelC() {
        return tarAboCelC;
    }

    public void setTarAboCelC(String tarAboCelC) {
        this.tarAboCelC = tarAboCelC;
    }

    public String getTarAboCel() {
        return tarAboCel;
    }

    public void setTarAboCel(String tarAboCel) {
        this.tarAboCel = tarAboCel;
    }

    public String getCalles() {
        return calles;
    }

    public void setCalles(String calles) {
        this.calles = calles;
    }

    public String getCaTrAv() {
        return caTrAv;
    }

    public void setCaTrAv(String caTrAv) {
        this.caTrAv = caTrAv;
    }

    public String getCaNroPa() {
        return caNroPa;
    }

    public void setCaNroPa(String caNroPa) {
        this.caNroPa = caNroPa;
    }

    public Character getAboCivil() {
        return aboCivil;
    }

    public void setAboCivil(Character aboCivil) {
        this.aboCivil = aboCivil;
    }

    public Character getAboSexo() {
        return aboSexo;
    }

    public void setAboSexo(Character aboSexo) {
        this.aboSexo = aboSexo;
    }

    public String getAboProfesi() {
        return aboProfesi;
    }

    public void setAboProfesi(String aboProfesi) {
        this.aboProfesi = aboProfesi;
    }

    public String getAboPoste() {
        return aboPoste;
    }

    public void setAboPoste(String aboPoste) {
        this.aboPoste = aboPoste;
    }

    public Short getAboCortCnt() {
        return aboCortCnt;
    }

    public void setAboCortCnt(Short aboCortCnt) {
        this.aboCortCnt = aboCortCnt;
    }

    public String getAboNroPreA() {
        return aboNroPreA;
    }

    public void setAboNroPreA(String aboNroPreA) {
        this.aboNroPreA = aboNroPreA;
    }

    public Character getAboSistema() {
        return aboSistema;
    }

    public void setAboSistema(Character aboSistema) {
        this.aboSistema = aboSistema;
    }

    public Short getAboDiaInsc() {
        return aboDiaInsc;
    }

    public void setAboDiaInsc(Short aboDiaInsc) {
        this.aboDiaInsc = aboDiaInsc;
    }

    public Instant getAboDia() {
        return aboDia;
    }

    public void setAboDia(Instant aboDia) {
        this.aboDia = aboDia;
    }

    public Instant getAboPgoDes() {
        return aboPgoDes;
    }

    public void setAboPgoDes(Instant aboPgoDes) {
        this.aboPgoDes = aboPgoDes;
    }

    public Instant getAboPgoHas() {
        return aboPgoHas;
    }

    public void setAboPgoHas(Instant aboPgoHas) {
        this.aboPgoHas = aboPgoHas;
    }

    public Instant getAboDebDes() {
        return aboDebDes;
    }

    public void setAboDebDes(Instant aboDebDes) {
        this.aboDebDes = aboDebDes;
    }

    public Instant getAboDebHas() {
        return aboDebHas;
    }

    public void setAboDebHas(Instant aboDebHas) {
        this.aboDebHas = aboDebHas;
    }

    public BigDecimal getAboMtoDeud() {
        return aboMtoDeud;
    }

    public void setAboMtoDeud(BigDecimal aboMtoDeud) {
        this.aboMtoDeud = aboMtoDeud;
    }

    public Instant getAboFchReco() {
        return aboFchReco;
    }

    public void setAboFchReco(Instant aboFchReco) {
        this.aboFchReco = aboFchReco;
    }

    public String getAboObserva() {
        return aboObserva;
    }

    public void setAboObserva(String aboObserva) {
        this.aboObserva = aboObserva;
    }

    public String getAboObsv() {
        return aboObsv;
    }

    public void setAboObsv(String aboObsv) {
        this.aboObsv = aboObsv;
    }

    public Character getAboFacEle() {
        return aboFacEle;
    }

    public void setAboFacEle(Character aboFacEle) {
        this.aboFacEle = aboFacEle;
    }

    public String getAboIp() {
        return aboIp;
    }

    public void setAboIp(String aboIp) {
        this.aboIp = aboIp;
    }

    public Character getAboGpon() {
        return aboGpon;
    }

    public void setAboGpon(Character aboGpon) {
        this.aboGpon = aboGpon;
    }
}
