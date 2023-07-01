package curso.api.rest.cursospringrestapi;

public class ObjetoErro {

    private String error; // erro e o codigo do erro
    private String code;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
