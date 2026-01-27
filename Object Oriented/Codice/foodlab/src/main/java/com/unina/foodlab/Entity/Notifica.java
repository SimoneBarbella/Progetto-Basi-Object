package com.unina.foodlab.Entity;

import java.time.LocalDateTime;

public class Notifica {

    private Integer idNotifica;
    private String messaggio;
    private LocalDateTime dataInvio;
    private String emailChef;
    private Integer idCorso;

    public Notifica() {
    }

    public Integer getIdNotifica() {
        return idNotifica;
    }

    public void setIdNotifica(Integer idNotifica) {
        this.idNotifica = idNotifica;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public LocalDateTime getDataInvio() {
        return dataInvio;
    }

    public void setDataInvio(LocalDateTime dataInvio) {
        this.dataInvio = dataInvio;
    }

    public String getEmailChef() {
        return emailChef;
    }

    public void setEmailChef(String emailChef) {
        this.emailChef = emailChef;
    }

    public Integer getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(Integer idCorso) {
        this.idCorso = idCorso;
    }
}
