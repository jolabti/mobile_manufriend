package Model;

public class Riwayatmodel {

    public String getNamaService() {
        return namaService;
    }

    public void setNamaService(String namaService) {
        this.namaService = namaService;
    }

    public String getWaktuService() {
        return waktuService;
    }

    public void setWaktuService(String waktuService) {
        this.waktuService = waktuService;
    }

    public String getNoteService() {
        return noteService;
    }

    public void setNoteService(String noteService) {
        this.noteService = noteService;
    }

    public String namaService;
    public String waktuService;
    public String noteService;

    public String getIdRecordServices() {
        return idRecordServices;
    }

    public void setIdRecordServices(String idRecordServices) {
        this.idRecordServices = idRecordServices;
    }

    public String idRecordServices;

    public String getStatusRequest() {
        return statusRequest;
    }

    public void setStatusRequest(String statusRequest) {
        this.statusRequest = statusRequest;
    }

    public String statusRequest;


    public Riwayatmodel(String namaService, String waktuService, String noteService, String statusRequest, String idRecordServices){
        this.namaService = namaService;
        this.waktuService =waktuService;
        this.noteService = noteService;
        this.statusRequest=statusRequest;
        this.idRecordServices=idRecordServices;
    }



}
