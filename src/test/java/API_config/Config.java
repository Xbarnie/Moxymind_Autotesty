package API_config;

public class Config {

    //URL pre ReqRes API, používa sa pri všetkých volaniach API
    public static final String BASE_URL = "https://reqres.in";

    // API kľúč načítaný zo systémovej premennej REQRES_API_KEY
    // Slúži na autentifikáciu pri volaniach API
    public static final String API_KEY = System.getenv("REQRES_API_KEY");
}
