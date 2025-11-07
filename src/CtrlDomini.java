public class CtrlDomini {
        //Tiene que estar todos los gestores creados
        private GestorEnquesta gestorEnquesta;
        private GestorUsuaris gestorUsuaris;

        public CtrlDomini() {
            //Crea todas las instancias de los gestores
            gestorEnquesta = new GestorEnquesta();
            gestorUsuaris = new GestorUsuaris();
        }
    public GestorEnquesta getCtrlEnquesta() {
        return gestorEnquesta;
    }

    public GestorUsuaris getCtrlUsuari() {
        return gestorUsuaris;
    }
}

