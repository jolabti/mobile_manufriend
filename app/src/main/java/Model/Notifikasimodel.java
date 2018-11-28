package Model;

public class Notifikasimodel {

    String tglTrx;

    public String getTglTrx() {
        return tglTrx;
    }

    public void setTglTrx(String tglTrx) {
        this.tglTrx = tglTrx;
    }

    public String getNamaService() {
        return namaService;
    }

    public void setNamaService(String namaService) {
        this.namaService = namaService;
    }

    public String getNoteStatus() {
        return noteStatus;
    }

    public void setNoteStatus(String noteStatus) {
        this.noteStatus = noteStatus;
    }

    String namaService;
    String noteStatus;

    public  Notifikasimodel(String tglTrx,String namaService,String noteStatus){

        this.tglTrx = tglTrx;
        this.namaService = namaService;
        this.noteStatus=noteStatus;
    }
}
