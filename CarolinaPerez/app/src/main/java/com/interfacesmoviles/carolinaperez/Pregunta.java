package com.interfacesmoviles.carolinaperez;

/**
 * Created by Carolina on 17/03/2016.
 */
public class Pregunta {

    private String pregunta;
    private String respuestaCorrecta;
    private String respuestaIncorrecta;

    public Pregunta(String pregunta, String respuestaCorrecta, String respuestaIncorrecta) {
        this.pregunta = pregunta;
        this.respuestaCorrecta = respuestaCorrecta;
        this.respuestaIncorrecta = respuestaIncorrecta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public void setRespuestaCorrecta(String respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public String getRespuestaIncorrecta() {
        return respuestaIncorrecta;
    }

    public void setRespuestaIncorrecta(String respuestaIncorrecta) {
        this.respuestaIncorrecta = respuestaIncorrecta;
    }
}
