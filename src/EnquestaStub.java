public class EnquestaStub extends Enquesta {
    private final int id;

    public EnquestaStub(int id) {
        super(id, "Stub", "Descripcio stub", 0, new java.util.ArrayList<>());
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getNumPreguntes() {
        return 0;
    }
}
