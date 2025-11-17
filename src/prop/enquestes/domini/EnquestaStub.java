package prop.enquestes.domini;
import prop.enquestes.domini.Enquesta;

import java.util.List;

public class EnquestaStub extends Enquesta {
    private final Integer id;
    public EnquestaStub(Integer id) {
        super(id, "Stub", "Descripcio stub", 0, new java.util.ArrayList<>());
        this.id = id;

    }

    public EnquestaStub(Integer id, List<Pregunta> preguntes) {
        super(id, "Stub", "Descripcio stub", 0, preguntes);
        this.id = id;

    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public int getNumPreguntes() {
        return 0;
    }

}

